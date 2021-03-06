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

package com.glaf.batch.domain;

import java.io.*;

import javax.persistence.*;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.batch.util.JobExecutionContextJsonFactory;
import com.glaf.core.base.JSONable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "JOB_EXECUTION_CONTEXT")
public class JobExecutionContext implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "job_execution_id", nullable = false)
	protected Long jobExecutionId;

	/**
	 * short_context
	 */
	@Column(name = "short_context", length = 2500)
	protected String shortContext;

	/**
	 * serialized_context
	 */
	@Lob
	@Column(name = "serialized_context")
	protected String serializedContext;

	public JobExecutionContext() {

	}

	public Long getJobExecutionId() {
		return this.jobExecutionId;
	}

	public String getSerializedContext() {
		return this.serializedContext;
	}

	public String getShortContext() {
		return this.shortContext;
	}

	public JobExecutionContext jsonToObject(JSONObject jsonObject) {
		return JobExecutionContextJsonFactory.jsonToObject(jsonObject);
	}

	public void setJobExecutionId(Long jobExecutionId) {
		this.jobExecutionId = jobExecutionId;
	}

	public void setSerializedContext(String serializedContext) {
		this.serializedContext = serializedContext;
	}

	public void setShortContext(String shortContext) {
		this.shortContext = shortContext;
	}

	public JSONObject toJsonObject() {
		return JobExecutionContextJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return JobExecutionContextJsonFactory.toObjectNode(this);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}