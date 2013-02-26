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

package com.glaf.dts.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glaf.dts.input.TextFileImporter;

public class TextParserJob implements Job {
	public final static Log log = LogFactory.getLog(TextParserJob.class);

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		TextFileImporter imp = new TextFileImporter();
		boolean success = false;
		int retry = 0;
		while (retry < 2 && !success) {
			try {
				retry++;
				imp.importData();
				success = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}