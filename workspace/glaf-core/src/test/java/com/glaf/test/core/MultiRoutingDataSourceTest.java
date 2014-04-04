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

package com.glaf.test.core;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.junit.Test;

import com.glaf.core.config.Environment;
import com.glaf.core.domain.SysLog;
import com.glaf.core.service.ISysLogService;
import com.glaf.test.AbstractTest;

public class MultiRoutingDataSourceTest extends AbstractTest {

	protected ISysLogService sysLogService;

	@Test
	public void testMultiDS() throws IOException {
		sysLogService = super.getBean("sysLogService");
		// ���������������serice�������У�����ԭ�򣩣�ֻ�����ڿ��Ʋ�����
		String currentName = com.glaf.core.config.Environment
				.getCurrentSystemName();// ����֮ǰ������Դ������Ĭ����default
		try {
			java.util.Map<String, Properties> dataSourceProperties = com.glaf.core.config.DBConfiguration
					.getDataSourceProperties();
			java.util.Iterator<String> iter = dataSourceProperties.keySet()
					.iterator();
			while (iter.hasNext()) {
				String systemName = (String) iter.next();
				Environment.setCurrentSystemName(systemName);// ���õ�ǰҪ��ҵ�����������Դ���ƣ����������õ�ds02
				// ���service�߼�
				// dataService.doYourBussiness(....);//����Service������ÿ�����ݿ������Ķ��Ƕ������񣡣���
				for (int i = 0; i < 100; i++) {
					SysLog bean = new SysLog();
					bean.setAccount("test");
					bean.setCreateTime(new Date());
					bean.setFlag(1);
					bean.setIp("127.0.0.1");
					bean.setOperate("insert");
					sysLogService.create(bean);
				}
			}
		} finally {
			com.glaf.core.config.Environment.setCurrentSystemName(currentName);// �����˼ǵû�ԭ
		}
	}

}