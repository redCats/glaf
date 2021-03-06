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

package com.glaf.batch.helper;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.glaf.batch.domain.JobDefinition;
import com.glaf.batch.domain.JobExecution;
import com.glaf.batch.domain.JobExecutionParam;
import com.glaf.batch.domain.JobInstance;
import com.glaf.batch.domain.StepDefinition;
import com.glaf.batch.domain.StepDefinitionParam;
import com.glaf.batch.domain.StepExecution;
import com.glaf.batch.service.IJobDefinitionService;
import com.glaf.batch.service.IJobService;
import com.glaf.core.context.ContextFactory;

public class JobHelper {

	protected volatile IJobService jobService;

	protected volatile IJobDefinitionService jobDefinitionService;

	public void createJobInstance(String jobKey) {
		JobDefinition jobDefinition = getJobDefinitionService()
				.getJobDefinitionByKey(jobKey);
		if (jobDefinition != null) {
			List<StepDefinition> steps = jobDefinition.getSteps();
			Collections.sort(steps);
			int version = 0;
			JobInstance job = getJobService().getJobInstanceByJobKey(jobKey);
			if (job != null && !getJobService().jobCompleted(jobKey)) {
				version = job.getVersion();
				// 如果Job已经存在并且还没有完成，不创建新任务实例
				return;
			}
			if (job == null) {
				version = version + 1;
				job = new JobInstance();
				job.setJobKey(jobKey);
				job.setJobName(jobDefinition.getJobName());
				job.setVersion(1);
				JobExecution jobExecution = new JobExecution();
				jobExecution.setCreateTime(new Date());
				jobExecution.setLastUpdated(new Date());
				jobExecution.setStartTime(new Date());
				jobExecution.setStatus("0");
				jobExecution.setVersion(version);
				job.addJobExecution(jobExecution);
				for (StepDefinition step : steps) {
					StepExecution stepExecution = new StepExecution();
					stepExecution.setJobClass(step.getJobClass());
					stepExecution.setJobStepKey(step.getJobStepKey());
					stepExecution.setLastUpdated(new Date());
					stepExecution.setListno(stepExecution.getListno());
					stepExecution.setStatus("0");
					stepExecution.setStartTime(new Date());
					stepExecution.setVersion(1);
					stepExecution.setStepKey(step.getStepKey());
					stepExecution.setStepName(step.getStepName());
					jobExecution.addStep(stepExecution);
					Collection<StepDefinitionParam> params = step.getParams();
					if (params != null && !params.isEmpty()) {
						for (StepDefinitionParam param : params) {
							JobExecutionParam p = new JobExecutionParam();
							p.setDateVal(param.getDateVal());
							p.setDoubleVal(param.getDoubleVal());
							p.setIntVal(param.getIntVal());
							p.setKeyName(param.getKeyName());
							p.setLongVal(param.getLongVal());
							p.setStringVal(param.getStringVal());
							p.setTypeCd(param.getTypeCd());
							p.setTextVal(param.getTextVal());
							jobExecution.addParam(p);
						}
					}
				}
				getJobService().saveJobInstance(job);
			}
		}
	}

	public IJobDefinitionService getJobDefinitionService() {
		if (jobDefinitionService == null) {
			jobDefinitionService = ContextFactory
					.getBean("jobDefinitionService");
		}
		return jobDefinitionService;
	}

	public IJobService getJobService() {
		if (jobService == null) {
			jobService = ContextFactory.getBean("jobService");
		}
		return jobService;
	}

	public void setJobDefinitionService(
			IJobDefinitionService jobDefinitionService) {
		this.jobDefinitionService = jobDefinitionService;
	}

	public void setJobService(IJobService jobService) {
		this.jobService = jobService;
	}

}
