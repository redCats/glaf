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

package com.glaf.core.security;

import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.entity.EntityService;
import com.glaf.core.identity.User;

import com.glaf.core.identity.Agent;

public class IdentityFactory {
	protected final static Log logger = LogFactory
			.getLog(IdentityFactory.class);
	protected static EntityService entityService;

	/**
	 * ��ȡί���˱�ż���
	 * 
	 * @param assignTo
	 *            �����˱��
	 * @return
	 */
	public static List<String> getAgentIds(String assignTo) {
		List<String> agentIds = new ArrayList<String>();
		List<Object> list = entityService.getList("getAgents", assignTo);
		if (list != null && !list.isEmpty()) {
			for (Object obj : list) {
				if (obj instanceof Agent) {
					Agent agent = (Agent) obj;
					agentIds.add(agent.getAssignFrom());
				}
			}
		}
		return agentIds;
	}

	/**
	 * ��ȡί���˶��󼯺�
	 * 
	 * @param assignTo
	 *            �����˱��
	 * @return
	 */
	public static List<Agent> getAgents(String assignTo) {
		List<Agent> agents = new ArrayList<Agent>();
		List<Object> list = entityService.getList("getAgents", assignTo);
		if (list != null && !list.isEmpty()) {
			for (Object obj : list) {
				if (obj instanceof Agent) {
					Agent agent = (Agent) obj;
					agents.add(agent);
				}
			}
		}
		return agents;
	}

	public static EntityService getEntityService() {
		if (entityService == null) {
			entityService = ContextFactory.getBean("entityService");
		}
		return entityService;
	}

	/**
	 * ��ȡ��¼�û���Ϣ
	 * 
	 * @param actorId
	 * @return
	 */
	public static LoginContext getLoginContext(String actorId) {
		User user = (User) entityService.getById("getUserById", actorId);
		if (user != null) {
			LoginContext loginContext = new LoginContext(user);

			return loginContext;
		}
		return null;
	}

	/**
	 * �����û�����ȡ�û�����
	 * 
	 * @param actorId
	 * @return
	 */
	public static User getUser(String actorId) {
		return (User) entityService.getById("getUserById", actorId);
	}

	/**
	 * ��ȡȫ���û�Map
	 * 
	 * @return
	 */
	public static Map<String, User> getUserMap() {
		Map<String, User> userMap = new LinkedHashMap<String, User>();
		List<Object> list = entityService.getList("getUsers", null);
		if (list != null && !list.isEmpty()) {
			for (Object obj : list) {
				if (obj instanceof User) {
					User user = (User) obj;
					userMap.put(user.getActorId(), user);
				}
			}
		}
		return userMap;
	}

	public static void setEntityService(EntityService entityService) {
		IdentityFactory.entityService = entityService;
	}

}