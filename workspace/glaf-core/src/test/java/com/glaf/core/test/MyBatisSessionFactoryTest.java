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

package com.glaf.core.test;

import java.sql.Connection;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.glaf.core.domain.SystemParam;
import com.glaf.core.entity.mybatis.MyBatisSessionFactory;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.JdbcUtils;

public class MyBatisSessionFactoryTest {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory
				.getSessionFactory();
		SqlSession session = null;
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection();
			session = sqlSessionFactory.openSession(conn);
			SystemParam m = session
					.selectOne("getSystemParamById", "sys_table");
			System.out.println(m.toJsonObject().toJSONString());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			JdbcUtils.close(conn);
		}
		long time = System.currentTimeMillis() - start;
		System.out.println("用时（耗秒）：" + (time));

		for (int i = 0; i < 20; i++) {
			start = System.currentTimeMillis();
			session = null;
			conn = null;
			try {
				conn = DBConnectionFactory.getConnection("yz");
				session = sqlSessionFactory.openSession(conn);
				SystemParam m = session.selectOne("getSystemParamById",
						"sys_table");
				System.out.println(m.toJsonObject().toJSONString());
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (session != null) {
					session.close();
				}
				JdbcUtils.close(conn);
			}
			time = System.currentTimeMillis() - start;
			System.out.println("用时（耗秒）：" + (time));
		}
	}

}