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


package org.jpage.jbpm.monitor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jpage.actor.User;
import org.jpage.core.query.paging.Page;
import org.jpage.jbpm.config.ObjectFactory;
import org.jpage.jbpm.context.Context;
import org.jpage.jbpm.context.ProcessContext;
import org.jpage.jbpm.model.DeployInstance;
import org.jpage.jbpm.service.ActorManager;
import org.jpage.jbpm.service.ProcessManager;
import org.jpage.jbpm.service.ServiceManager;
import org.jpage.jbpm.util.Constant;
import org.jpage.util.RequestUtil;
import org.jpage.util.Tools;
import org.springframework.web.servlet.ModelAndView;

 
public class ProcessMonitorController implements
		org.springframework.web.servlet.mvc.Controller {
	private final static Log logger = LogFactory
			.getLog(ProcessMonitorController.class);

	private JbpmConfiguration jbpmConfiguration = JbpmConfiguration
			.getInstance();

	private ActorManager actorManager;

	private ProcessManager processManager;

	private ServiceManager serviceManager;

	public ProcessMonitorController() {
		actorManager = (ActorManager) org.jpage.jbpm.context.JbpmContextFactory
				.getBean("actorManager");
		processManager = (ProcessManager) org.jpage.jbpm.context.JbpmContextFactory
				.getBean("processManager");
		serviceManager = (ServiceManager) org.jpage.jbpm.context.JbpmContextFactory
				.getBean("serviceManager");
	}

	public ModelAndView abortProcess(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (!ObjectFactory.canAbortProcess()) {
			request.setAttribute(
					org.jpage.jbpm.util.Constant.APPLICATION_EXCEPTION_MESSAGE,
					"不允许中止该流程实例!");
			return new ModelAndView("error");
		}
		User user = (User) request.getSession().getAttribute(
				org.jpage.util.Constant.LOGIN_USER);
		String processInstanceId = request.getParameter("processInstanceId");
		logger.info("processInstanceId:" + processInstanceId);
		Map params = Tools.getParameters(request);
		JbpmContext jbpmContext = null;
		try {
			jbpmContext = jbpmConfiguration.createJbpmContext();
			ProcessContext ctx = new ProcessContext();
			ctx.setContextMap(params);
			ctx.setActorId(user.getActorId());
			Collection dataFields = (Collection) params
					.get(Constant.JBPM_DATA_FIELD);
			ctx.setDataFields(dataFields);
			processManager.abortProcess(ctx);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			return new ModelAndView("error");
		} finally {
			Context.close(jbpmContext);
		}
		return processInstances(request, response);
	}

	public ModelAndView chart(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String chartType = request.getParameter("chartType");
		if ("yearChart".equals(chartType)) {
			return new ModelAndView("yearChart");
		} else if ("monthChart".equals(chartType)) {
			return new ModelAndView("monthChart");
		} else if ("todayChart".equals(chartType)) {
			return new ModelAndView("todayChart");
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotBlank(view)) {
			return new ModelAndView(view);
		}
		return new ModelAndView("chart");
	}

	public ModelAndView deleteProcessDefinition(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String processDefinitionId = request
				.getParameter("processDefinitionId");
		logger.info("processDefinitionId:" + processDefinitionId);
		JbpmContext jbpmContext = null;
		try {
			jbpmContext = jbpmConfiguration.createJbpmContext();
			processManager.deleteProcessDefinition(jbpmContext,
					processDefinitionId);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			return new ModelAndView("error");
		} finally {
			Context.close(jbpmContext);
		}
		return processDefinitions(request, response);
	}

	public ModelAndView deleteProcessInstance(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (!ObjectFactory.canDeleteProcessInstance()) {
			request.setAttribute(
					org.jpage.jbpm.util.Constant.APPLICATION_EXCEPTION_MESSAGE,
					"不允许删除该流程实例!");
			return new ModelAndView("error");
		}
		String processInstanceId = request.getParameter("processInstanceId");
		logger.info("processInstanceId:" + processInstanceId);
		JbpmContext jbpmContext = null;
		try {
			jbpmContext = jbpmConfiguration.createJbpmContext();
			processManager
					.deleteProcessInstance(jbpmContext, processInstanceId);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			return new ModelAndView("error");
		} finally {
			Context.close(jbpmContext);
		}
		return processInstances(request, response);
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = (User) request.getSession().getAttribute(
				org.jpage.util.Constant.LOGIN_USER);
		if (user == null) {
			request.setAttribute(Constant.APPLICATION_EXCEPTION_MESSAGE,
					"您没有登录或会话已经超时，请重新登录！");
			return new ModelAndView("error");
		}

		try {

			String method = request.getParameter("method");
			if (method == null) {
				method = "processInstances";
			}

			if (method.equals("processDefinitions")) {
				return processDefinitions(request, response);
			} else if (method.equals("taskInstances")) {
				return taskInstances(request, response);
			} else if (method.equals("deleteProcessDefinition")) {
				return deleteProcessDefinition(request, response);
			} else if (method.equals("deleteProcessInstance")) {
				return deleteProcessInstance(request, response);
			} else if (method.equals("abortProcess")) {
				return abortProcess(request, response);
			} else if (method.equals("suspend")) {
				return suspend(request, response);
			} else if (method.equals("resume")) {
				return resume(request, response);
			} else if (method.equals("chart")) {
				return chart(request, response);
			} else if (method.equals("query")) {
				return query(request, response);
			} else if (method.equals("taskQuery")) {
				return taskQuery(request, response);
			} else if (method.equals("stateInstances")) {
				return stateInstances(request, response);
			} else if (method.equals("viewStateInstances")) {
				return viewStateInstances(request, response);
			}

			return processInstances(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			return new ModelAndView("error");
		}
	}

	public ModelAndView processDefinitions(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JbpmContext jbpmContext = null;
		try {
			jbpmContext = jbpmConfiguration.createJbpmContext();
			List rows = processManager.getAllProcessDefinitions(jbpmContext);
			request.setAttribute("rows", rows);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			return new ModelAndView("error");
		} finally {
			Context.close(jbpmContext);
		}

		RequestUtil.setRequestParameterToAttribute(request);

		return new ModelAndView("processDefinitions");
	}

	public ModelAndView processInstances(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int currPageNo = 1;
		String processDefinitionId = request
				.getParameter("processDefinitionId");
		String running = request.getParameter("running");
		String loadBusiness = request.getParameter("loadBusiness");
		String temp = request.getParameter(Page.PAGENO_PARAMNAME);
		if ((temp != null) && (temp.length() > 0)) {
			try {
				currPageNo = Integer.parseInt(temp);
			} catch (NumberFormatException e) {
			}
		}
		Page jpage = null;
		JbpmContext jbpmContext = null;
		try {
			jbpmContext = jbpmConfiguration.createJbpmContext();

			if (StringUtils.isNumeric(processDefinitionId)) {
				if (StringUtils.equals(running, "1")) {
					jpage = processManager.getPageRunningProcessInstances(
							jbpmContext, currPageNo, -1, new Long(
									processDefinitionId).longValue());
				} else {
					jpage = processManager.getPageFinishedProcessInstances(
							jbpmContext, currPageNo, -1, new Long(
									processDefinitionId).longValue());
				}
			}

			if (StringUtils.isNotBlank(loadBusiness)) {
				Set rowIds = new HashSet();
				if (jpage.getRows() != null && jpage.getRows().size() > 0) {
					Iterator iterator = jpage.getRows().iterator();
					while (iterator.hasNext()) {
						ProcessInstance processInstance = (ProcessInstance) iterator
								.next();
						rowIds.add(String.valueOf(processInstance.getId()));
					}
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			return new ModelAndView("error");
		} finally {
			Context.close(jbpmContext);
		}

		RequestUtil.setRequestParameterToAttribute(request);
		request.setAttribute("jpage", jpage);

		String view = request.getParameter("view");
		if (StringUtils.isNotBlank(view)) {
			return new ModelAndView(view);
		}

		return new ModelAndView("processInstances");
	}

	public ModelAndView query(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JbpmContext jbpmContext = null;
		try {
			jbpmContext = jbpmConfiguration.createJbpmContext();
			Map userMap = actorManager.getUserMap(jbpmContext);
			List deployInstances = serviceManager
					.getDeployInstances(jbpmContext);
			if (deployInstances != null && deployInstances.size() > 0) {
				Map deployMap = new TreeMap();
				Iterator iter = deployInstances.iterator();
				while (iter.hasNext()) {
					DeployInstance deploy = (DeployInstance) iter.next();
					deployMap.put(deploy.getProcessName(), deploy);
				}
				request.setAttribute("deployMap", deployMap);
			}
			request.setAttribute("deployInstances", deployInstances);
			request.setAttribute("userMap", userMap);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			return new ModelAndView("error");
		} finally {
			Context.close(jbpmContext);
		}
		return new ModelAndView("query");
	}

	public ModelAndView resume(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String processInstanceId = request.getParameter("processInstanceId");
		logger.info("processInstanceId:" + processInstanceId);
		JbpmContext jbpmContext = null;
		try {
			jbpmContext = jbpmConfiguration.createJbpmContext();
			processManager.resume(jbpmContext, processInstanceId);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			return new ModelAndView("error");
		} finally {
			Context.close(jbpmContext);
		}
		return processInstances(request, response);
	}

	public ModelAndView stateInstances(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String processInstanceId = request.getParameter("processInstanceId");
		ProcessDefinition processDefinition = null;
		ProcessInstance processInstance;
		List rows = null;
		JbpmContext jbpmContext = null;
		try {
			jbpmContext = jbpmConfiguration.createJbpmContext();

			rows = serviceManager.getStateInstances(jbpmContext,
					processInstanceId);

			List taskItems = processManager.getTaskItemsByProcessInstanceId(
					jbpmContext, processInstanceId);
			Map userMap = actorManager.getUserMap(jbpmContext);

			processInstance = jbpmContext.getProcessInstance(new Long(
					processInstanceId).longValue());
			if (processInstance != null) {
				Token rootToken = processInstance.getRootToken();
				processDefinition = processInstance.getProcessDefinition();
				if (processDefinition != null) {
					Hibernate.initialize(processDefinition);
				}
				long tokenInstanceId = rootToken.getId();
				request.setAttribute("tokenInstanceId", new Long(
						tokenInstanceId));
				String starterId = (String) processInstance
						.getContextInstance().getVariable(
								Constant.PROCESS_START_ACTORID);
				if (userMap != null && starterId != null) {
					User user = actorManager.getUser(jbpmContext, starterId);
					if (user != null) {
						request.setAttribute("starter", user);
					}
				}
			}

			RequestUtil.setRequestParameterToAttribute(request);

			request.setAttribute("taskItems", taskItems);
			request.setAttribute("userMap", userMap);
			request.setAttribute("rows", rows);
			request.setAttribute("processDefinition", processDefinition);
			request.setAttribute("processInstance", processInstance);
			request.setAttribute("jbpmContext", jbpmContext);

			String view = request.getParameter("view");
			if (StringUtils.isNotBlank(view)) {
				return new ModelAndView(view);
			}

			return new ModelAndView("stateInstances");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			return new ModelAndView("error");
		} finally {
			Context.close(jbpmContext);
		}
	}

	public ModelAndView suspend(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (!ObjectFactory.canSuspendProcess()) {
			request.setAttribute(
					org.jpage.jbpm.util.Constant.APPLICATION_EXCEPTION_MESSAGE,
					"不允许挂起该流程实例!");
			return new ModelAndView("error");
		}
		String processInstanceId = request.getParameter("processInstanceId");
		logger.info("processInstanceId:" + processInstanceId);
		JbpmContext jbpmContext = null;
		try {
			jbpmContext = jbpmConfiguration.createJbpmContext();
			processManager.suspend(jbpmContext, processInstanceId);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			return new ModelAndView("error");
		} finally {
			Context.close(jbpmContext);
		}
		return processInstances(request, response);
	}

	public ModelAndView taskInstances(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String actorId = request.getParameter("actorId");
		String actionType = request.getParameter("actionType");
		String processName = request.getParameter("processName");
		List taskItems = null;
		JbpmContext jbpmContext = null;
		try {
			jbpmContext = jbpmConfiguration.createJbpmContext();
			if (StringUtils.equals(actionType, "running")) {
				if (StringUtils.isNotBlank(processName)) {
					if (StringUtils.isNotBlank(actorId)) {
						taskItems = processManager.getTaskItemsByProcessName(
								jbpmContext, processName, actorId);
					} else {
						taskItems = processManager.getTaskItemsByProcessName(
								jbpmContext, processName);
					}
				} else {
					if (StringUtils.isNotBlank(actorId)) {
						taskItems = processManager.getTaskItems(jbpmContext,
								actorId);
					}
				}
			} else if (StringUtils.equals(actionType, "finished")) {
				if (StringUtils.isNotBlank(processName)) {
					if (StringUtils.isNotBlank(actorId)) {
						taskItems = processManager.getFinishedTaskItems(
								jbpmContext, processName, actorId);
					}
				} else {
					if (StringUtils.isNotBlank(actorId)) {
						taskItems = processManager.getFinishedTaskItems(
								jbpmContext, actorId);
					}
				}
			}

			if (taskItems == null) {
				taskItems = processManager.getAllTaskItems(jbpmContext);
			}

			RequestUtil.setRequestParameterToAttribute(request);

			request.setAttribute("taskItems", taskItems);

			Map userMap = actorManager.getUserMap(jbpmContext);

			request.setAttribute("userMap", userMap);

			String view = request.getParameter("view");
			if (StringUtils.isNotBlank(view)) {
				return new ModelAndView(view);
			}

			return new ModelAndView("taskInstances");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			return new ModelAndView("error");
		} finally {
			Context.close(jbpmContext);
		}
	}

	public ModelAndView taskQuery(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JbpmContext jbpmContext = null;
		try {
			jbpmContext = jbpmConfiguration.createJbpmContext();
			Map userMap = actorManager.getUserMap(jbpmContext);
			List deployInstances = serviceManager
					.getDeployInstances(jbpmContext);
			if (deployInstances != null && deployInstances.size() > 0) {
				Map deployMap = new TreeMap();
				Iterator iter = deployInstances.iterator();
				while (iter.hasNext()) {
					DeployInstance deploy = (DeployInstance) iter.next();
					deployMap.put(deploy.getProcessName(), deploy);
				}
				request.setAttribute("deployMap", deployMap);
			}
			request.setAttribute("deployInstances", deployInstances);
			request.setAttribute("userMap", userMap);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			return new ModelAndView("error");
		} finally {
			Context.close(jbpmContext);
		}
		return new ModelAndView("taskQuery");
	}

	public ModelAndView viewStateInstances(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String processInstanceId = request.getParameter("processInstanceId");
		ProcessDefinition processDefinition = null;
		ProcessInstance processInstance;
		List rows = null;
		JbpmContext jbpmContext = null;
		try {
			jbpmContext = jbpmConfiguration.createJbpmContext();

			rows = serviceManager.getStateInstances(jbpmContext,
					processInstanceId);

			List taskItems = processManager.getTaskItemsByProcessInstanceId(
					jbpmContext, processInstanceId);
			Map userMap = actorManager.getUserMap(jbpmContext);

			processInstance = jbpmContext.getProcessInstance(new Long(
					processInstanceId).longValue());
			if (processInstance != null) {
				Token rootToken = processInstance.getRootToken();
				processDefinition = processInstance.getProcessDefinition();
				if (processDefinition != null) {
					Hibernate.initialize(processDefinition);
				}
				long tokenInstanceId = rootToken.getId();
				request.setAttribute("tokenInstanceId", new Long(
						tokenInstanceId));
				String starterId = (String) processInstance
						.getContextInstance().getVariable(
								Constant.PROCESS_START_ACTORID);
				if (userMap != null && starterId != null) {
					User user = actorManager.getUser(jbpmContext, starterId);
					if (user != null) {
						request.setAttribute("starter", user);
					}
				}
			}

			RequestUtil.setRequestParameterToAttribute(request);

			request.setAttribute("taskItems", taskItems);
			request.setAttribute("userMap", userMap);
			request.setAttribute("rows", rows);
			request.setAttribute("processDefinition", processDefinition);
			request.setAttribute("processInstance", processInstance);
			request.setAttribute("jbpmContext", jbpmContext);

			String view = request.getParameter("view");
			if (StringUtils.isNotBlank(view)) {
				return new ModelAndView(view);
			}

			return new ModelAndView("viewStateInstances");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
			return new ModelAndView("error");
		} finally {
			Context.close(jbpmContext);
		}
	}

}