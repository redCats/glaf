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

package com.glaf.core.mail.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.glaf.core.mail.Mail;
import com.glaf.core.query.MailQuery;

@Component
public interface MailMapper {

	void deleteMails(MailQuery query);

	void deleteMailById(String id);

	Mail getMailById(String id);

	Mail getMailByMailId(String mailId);

	int getMailCount(MailQuery query);

	List<Mail> getMails(MailQuery query);

	void insertMail(Mail model);

	void updateMail(Mail model);

}