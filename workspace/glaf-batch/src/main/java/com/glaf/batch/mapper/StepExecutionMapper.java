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

package com.glaf.batch.mapper;

import java.util.*;
import org.springframework.stereotype.Component;
import com.glaf.batch.domain.*;
import com.glaf.batch.query.*;

@Component
public interface StepExecutionMapper {

	void deleteStepExecutionById(long id);

	void deleteStepExecutionByJobInstanceId(long jobInstanceId);

	StepExecution getStepExecutionById(long id);

	StepExecution getStepExecutionByKey(String jobStepKey);

	int getStepExecutionCount(Map<String, Object> parameter);

	int getStepExecutionCountByQueryCriteria(StepExecutionQuery query);

	List<StepExecution> getStepExecutions(Map<String, Object> parameter);

	List<StepExecution> getStepExecutionsByJobInstanceId(long jobInstanceId);

	List<StepExecution> getStepExecutionsByQueryCriteria(
			StepExecutionQuery query);

	void insertStepExecution(StepExecution model);

	void updateStepExecution(StepExecution model);

}