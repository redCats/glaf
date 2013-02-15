/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package org.jpage.jbpm.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.JbpmException;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;
import org.jpage.actor.User;
import org.jpage.jbpm.config.ObjectFactory;
import org.jpage.jbpm.el.DefaultExpressionEvaluator;
import org.jpage.jbpm.ibatis.MutableSQLMapContainer;
import org.jpage.jbpm.util.Constant;
import org.jpage.util.Tools;

/**
 * 动态任务产生注意事项：需要将task-node节点的create-tasks的属性改成create-tasks="false" 例如：
 * <task-node name="采购部部长审批" create-tasks="false"><br>
 * <event type="node-enter"><br>
 * <action ref-name="taskinstance_role07x"/><br>
 * </event><br>
 * <task name="task07x" description="采购部部长审批" ></task><br>
 * <transition name="tr410" to="采购部部长审批通过？"></transition><br>
 * </task-node><br>
 */
public class SqlMapTaskInstanceAction implements ActionHandler {
	private static final Log logger = LogFactory
			.getLog(SqlMapTaskInstanceAction.class);

	private static final long serialVersionUID = 1L;

	/**
	 * 描述
	 */
	protected String description;

	/**
	 * 如果表达式不为空，则其计算结果必须为true才执行任务分派。
	 */
	protected String expression;

	/**
	 * 部门编号或表达式
	 */
	protected String deptId;

	/**
	 * 部门集合 <br>
	 * <deptIds><br>
	 * <element>310</element><br>
	 * <element>303</element><br>
	 * </deptIds><br>
	 */
	protected List<Object> deptIds;

	/**
	 * 角色编号或表达式
	 */
	protected String roleId;

	/**
	 * 角色集合 <br>
	 * <roleIds><br>
	 * <element>R001</element><br>
	 * <element>R002</element><br>
	 * </roleIds><br>
	 */
	protected List<Object> roleIds;

	/**
	 * SqlMap查询语句编号
	 */
	protected String queryId;

	/**
	 * 对象编号或表达式
	 */
	protected String objectId;

	/**
	 * 对象值或表达式
	 */
	protected String objectValue;

	/**
	 * 动态设置的参与者的参数名，环境变量可以通过contextInstance.getVariable()取得
	 * 例如：contextInstance.getVariable("SendDocAuditor");
	 */
	protected String dynamicActors;

	/**
	 * 转移路径的名称
	 */
	protected String transitionName;

	/**
	 * 任务名称
	 */
	protected String taskName;

	/**
	 * 如果多个审批者只要有一个通过就可以的，设置该值为true
	 */
	protected boolean isPooled;

	/**
	 * 根据表达式决定isPooled是否为true
	 */
	protected String pooledExpression;

	/**
	 * 如果不能获取任务参与者是否离开本节点（任务节点）
	 */
	protected boolean leaveNodeIfActorNotAvailable;

	public SqlMapTaskInstanceAction() {

	}

	@SuppressWarnings("unchecked")
	public void execute(ExecutionContext ctx) {
		logger.debug("-------------------------------------------------------");
		logger.debug("---------------SqlMapTaskInstanceAction----------------");
		logger.debug("-------------------------------------------------------");

		boolean executable = true;

		Map<String, Object> params = new HashMap<String, Object>();

		ProcessInstance processInstance = ctx.getProcessInstance();
		String processInstanceId = String.valueOf(processInstance.getId());
		params.put("processInstanceId", processInstanceId);
		ContextInstance contextInstance = ctx.getContextInstance();
		Map<String, Object> variables = contextInstance.getVariables();
		if (variables != null && variables.size() > 0) {
			Set<Entry<String, Object>> entrySet = variables.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String name = entry.getKey();
				Object value = entry.getValue();
				if (name != null && value != null && params.get(name) == null) {
					params.put(name, value);
				}
			}
		}

		if (StringUtils.isNotEmpty(expression)) {
			if (expression.startsWith("#{") && expression.endsWith("}")) {
				Object value = DefaultExpressionEvaluator.evaluate(expression,
						params);
				if (value != null) {
					if (value instanceof Boolean) {
						Boolean b = (Boolean) value;
						executable = b.booleanValue();
					}
				}
			}
		}

		if (!executable) {
			logger.debug("表达式计算后取值为false，不执行后续动作。");
			return;
		}

		List<String> actorIds = new ArrayList<String>();
		if (StringUtils.isNotEmpty(dynamicActors)) {
			String actorId = (String) contextInstance
					.getVariable(dynamicActors);
			if (StringUtils.isNotEmpty(actorId)) {
				StringTokenizer st2 = new StringTokenizer(actorId, ",");
				while (st2.hasMoreTokens()) {
					String elem = st2.nextToken();
					if (StringUtils.isNotEmpty(elem)) {
						actorIds.add(elem);
					}
				}
			}
		}

		/**
		 * 从数据库中根据查询条件查找参与者
		 */
		if (StringUtils.isNotEmpty(queryId)) {

			logger.debug("queryId:" + queryId);

			params.put("roleId", roleId);
			params.put("objectId", objectId);
			params.put("objectValue", objectValue);

			if (StringUtils.isNotEmpty(deptId)) {
				String tmp = deptId;
				Object value = deptId;
				if (tmp.startsWith("#{") && tmp.endsWith("}")) {
					value = DefaultExpressionEvaluator.evaluate(tmp, params);
				} else if (tmp.startsWith("#P{") && tmp.endsWith("}")) {
					tmp = Tools.replaceIgnoreCase(tmp, "#P{", "");
					tmp = Tools.replaceIgnoreCase(tmp, "}", "");
					value = contextInstance.getVariable(tmp);
				}
				params.put("deptId", value);
				if (value != null && value.toString().indexOf(",") != -1) {
					List<String> pieces = Tools.split(value.toString(), ",");
					params.put("deptIds", pieces);
					params.remove("deptId");
				}
			}

			if (StringUtils.isNotEmpty(roleId)) {
				String tmp = roleId;
				Object value = roleId;
				if (tmp.startsWith("#{") && tmp.endsWith("}")) {
					value = DefaultExpressionEvaluator.evaluate(tmp, params);
				} else if (tmp.startsWith("#P{") && tmp.endsWith("}")) {
					tmp = Tools.replaceIgnoreCase(tmp, "#P{", "");
					tmp = Tools.replaceIgnoreCase(tmp, "}", "");
					value = contextInstance.getVariable(tmp);
				}
				params.put("roleId", value);
				if (value != null && value.toString().indexOf(",") != -1) {
					List<String> pieces = Tools.split(value.toString(), ",");
					params.put("roleIds", pieces);
					params.remove("roleId");
				}
			}

			if (StringUtils.isNotEmpty(objectId)) {
				String tmp = objectId;
				Object value = objectId;
				if (tmp.startsWith("#{") && tmp.endsWith("}")) {
					value = DefaultExpressionEvaluator.evaluate(tmp, params);
				} else if (tmp.startsWith("#P{") && tmp.endsWith("}")) {
					tmp = Tools.replaceIgnoreCase(tmp, "#P{", "");
					tmp = Tools.replaceIgnoreCase(tmp, "}", "");
					value = contextInstance.getVariable(tmp);
				}
				params.put("objectId", value);
			}

			if (StringUtils.isNotEmpty(objectValue)) {
				String tmp = objectValue;
				Object value = objectValue;
				if (tmp.startsWith("#{") && tmp.endsWith("}")) {
					value = DefaultExpressionEvaluator.evaluate(tmp, params);
				} else if (tmp.startsWith("#P{") && tmp.endsWith("}")) {
					tmp = Tools.replaceIgnoreCase(tmp, "#P{", "");
					tmp = Tools.replaceIgnoreCase(tmp, "}", "");
					value = contextInstance.getVariable(tmp);
				}
				params.put("objectValue", value);
			}

			if (deptIds != null && deptIds.size() > 0) {
				params.put("deptIds", deptIds);
			}

			if (roleIds != null && roleIds.size() > 0) {
				params.put("roleIds", roleIds);
			}

			logger.debug("params:" + params);

			MutableSQLMapContainer container = MutableSQLMapContainer
					.getContainer();

			Collection<Object> actors = null;
			try {
				actors = container.getList(ctx.getJbpmContext(), queryId,
						params);
			} catch (Exception ex) {
				logger.error("params:" + params);
				logger.error(ex);
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}

			if (actors != null && actors.size() > 0) {
				Iterator<Object> iterator = actors.iterator();
				while (iterator.hasNext()) {
					Object object = iterator.next();
					String actorId = null;
					if (object instanceof String) {
						actorId = (String) object;
					} else if (object instanceof User) {
						User user = (User) object;
						actorId = user.getActorId();
					}
					if (actorId != null) {
						actorIds.add(actorId);
					}
				}
			}
		}

		logger.debug("actorIds:" + actorIds);

		if (actorIds.size() == 0) {

			if (leaveNodeIfActorNotAvailable) {
				contextInstance.setVariable(Constant.IS_AGREE, "true");
				if (StringUtils.isNotEmpty(transitionName)) {
					ctx.leaveNode(transitionName);
				} else {
					ctx.leaveNode();
				}
				return;
			}

			if (ObjectFactory.isDefaultActorEnable()) {
				String defaultActors = ObjectFactory.getDefaultActors();
				if (StringUtils.isNotEmpty(defaultActors)) {
					StringTokenizer st2 = new StringTokenizer(defaultActors,
							",");
					while (st2.hasMoreTokens()) {
						String elem = st2.nextToken();
						if (StringUtils.isNotEmpty(elem)) {
							actorIds.add(elem);
						}
					}
				}
			}
		}

		if (actorIds.size() > 0) {

			Task task = null;

			if (StringUtils.isNotEmpty(taskName)) {
				Node node = ctx.getNode();
				if (node instanceof TaskNode) {
					TaskNode taskNode = (TaskNode) node;
					task = taskNode.getTask(taskName);
				}
			}

			if (task == null) {
				task = ctx.getTask();
			}

			if (task == null) {
				throw new JbpmException(" task is null");
			}

			if (StringUtils.isNotEmpty(pooledExpression)) {
				if (pooledExpression.startsWith("#{")
						&& pooledExpression.endsWith("}")) {
					Object value = DefaultExpressionEvaluator.evaluate(
							pooledExpression, params);
					if (value != null) {
						if (value instanceof Boolean) {
							Boolean b = (Boolean) value;
							isPooled = b.booleanValue();
						}
					}
				}
			}
			
			logger.debug("#isPooled#:" + isPooled);

			Token token = ctx.getToken();
			TaskMgmtInstance tmi = ctx.getTaskMgmtInstance();

			if (isPooled) {
				TaskInstance taskInstance = tmi.createTaskInstance(task, token);
				taskInstance.setCreate(new Date());
				if (task != null) {
					taskInstance.setSignalling(task.isSignalling());
				}
				if (actorIds.size() == 1) {
					String actorId = actorIds.iterator().next();
					taskInstance.setActorId(actorId);
				} else {
					int i = 0;
					String[] pooledIds = new String[actorIds.size()];
					Iterator<String> iterator2 = actorIds.iterator();
					while (iterator2.hasNext()) {
						pooledIds[i++] = iterator2.next();
					}
					taskInstance.setPooledActors(pooledIds);
				}
			} else {
				Iterator<String> iterator = actorIds.iterator();
				while (iterator.hasNext()) {
					String actorId = iterator.next();
					TaskInstance taskInstance = tmi.createTaskInstance(task,
							token);
					taskInstance.setActorId(actorId);
					taskInstance.setCreate(new Date());
					if (task != null) {
						taskInstance.setSignalling(task.isSignalling());
					}
				}
			}
		}
	}

	public String getDeptId() {
		return deptId;
	}

	public List<Object> getDeptIds() {
		return deptIds;
	}

	public String getDescription() {
		return description;
	}

	public String getDynamicActors() {
		return dynamicActors;
	}

	public String getExpression() {
		return expression;
	}

	public String getObjectId() {
		return objectId;
	}

	public String getObjectValue() {
		return objectValue;
	}

	public String getPooledExpression() {
		return pooledExpression;
	}

	public String getQueryId() {
		return queryId;
	}

	public String getRoleId() {
		return roleId;
	}

	public List<Object> getRoleIds() {
		return roleIds;
	}

	public String getTaskName() {
		return taskName;
	}

	public String getTransitionName() {
		return transitionName;
	}

	public boolean isLeaveNodeIfActorNotAvailable() {
		return leaveNodeIfActorNotAvailable;
	}

	public boolean isPooled() {
		return isPooled;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public void setDeptIds(List<Object> deptIds) {
		this.deptIds = deptIds;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDynamicActors(String dynamicActors) {
		this.dynamicActors = dynamicActors;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setLeaveNodeIfActorNotAvailable(
			boolean leaveNodeIfActorNotAvailable) {
		this.leaveNodeIfActorNotAvailable = leaveNodeIfActorNotAvailable;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public void setObjectValue(String objectValue) {
		this.objectValue = objectValue;
	}

	public void setPooled(boolean isPooled) {
		this.isPooled = isPooled;
	}

	public void setPooledExpression(String pooledExpression) {
		this.pooledExpression = pooledExpression;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setRoleIds(List<Object> roleIds) {
		this.roleIds = roleIds;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}

}
