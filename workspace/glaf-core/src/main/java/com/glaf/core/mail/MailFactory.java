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

package com.glaf.core.mail;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.glaf.core.config.CustomProperties;
import com.glaf.core.config.SystemConfig;

public class MailFactory {
	protected static final Log logger = LogFactory.getLog(MailFactory.class);
	private static ClassPathXmlApplicationContext ctx;
	private final static String DEFAULT_CONFIG = "com/mxalloy/base/mail/mail-spring-context.xml";

	public synchronized  static Object getBean(Object key) {
		if (ctx == null) {
			ctx = reload();
		}
		return ctx.getBean((String) key);
	}

	public synchronized static ClassPathXmlApplicationContext reload() {
		if (ctx != null) {
			ctx.close();
			ctx = null;
		}
		String configLocation = CustomProperties.getString("mail.context");
		if (StringUtils.isEmpty(configLocation)) {
			configLocation = SystemConfig.getString("mail.context");
		}
		if (StringUtils.isEmpty(configLocation)) {
			configLocation = DEFAULT_CONFIG;
		}
		ctx = new ClassPathXmlApplicationContext(configLocation);
		logger.debug("start spring ioc from: " + configLocation);
		return ctx;
	}

}