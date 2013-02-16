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

package com.glaf.base.modules.others.springmvc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.base.modules.others.service.WorkCalendarService;

import com.glaf.base.utils.ParamUtil;
import com.glaf.base.utils.RequestUtil;

@Controller("/others/workCalendar")
@RequestMapping("/others/workCalendar.do")
public class WorkCalendarController {
	private static final Log logger = LogFactory
			.getLog(WorkCalendarController.class);

	@javax.annotation.Resource
	private WorkCalendarService workCalendarService;

	public void setWorkCalendarService(WorkCalendarService workCalendarService) {
		this.workCalendarService = workCalendarService;
		logger.info("setWorkCalendarService");
	}

	/**
	 * 显示工作日历列表
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return @
	 */
	@RequestMapping(params = "method=showList")
	public ModelAndView showList(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		RequestUtil.setRequestParameterToAttribute(request);
		Calendar cal = Calendar.getInstance();
		int year = ParamUtil.getIntParameter(request, "year",
				cal.get(Calendar.YEAR));
		request.setAttribute("year", String.valueOf(year));
		return new ModelAndView("/modules/others/calendar/work_calendar",
				modelMap);
	}

	/**
	 * 显示工作日历列表
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return @
	 */
	@RequestMapping(params = "method=showCalendar")
	public ModelAndView showCalendar(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
		RequestUtil.setRequestParameterToAttribute(request);
		Calendar cal = Calendar.getInstance();
		int month = ParamUtil.getIntParameter(request, "month",
				cal.get(Calendar.MONTH));
		int year = ParamUtil.getIntParameter(request, "year",
				cal.get(Calendar.YEAR));

		cal.set(Calendar.MONTH, month); // 设置月份
		cal.set(Calendar.YEAR, year); // 设置年份
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		logger.info("month:" + month);
		int firstIndex = cal.get(Calendar.DAY_OF_WEEK) - 1; // 当月第一天是星期几
		logger.info("firstIndex:" + firstIndex);
		int maxIndex = cal.getActualMaximum(Calendar.DAY_OF_MONTH);// 当月的天数
		logger.info("maxIndex:" + maxIndex);
		int weeks = Calendar.WEEK_OF_MONTH;// 当月的周数
		cal.set(Calendar.DATE, 1);// 当月1号是星期几
		if (cal.get(Calendar.DAY_OF_WEEK) == 7)
			weeks += 1;
		logger.info("day of week:" + cal.get(Calendar.DAY_OF_WEEK));
		logger.info("weeks:" + weeks);

		String days[] = new String[42];
		for (int i = 0; i < 42; i++) {
			days[i] = "";
		}
		for (int i = 0; i < maxIndex; i++) {
			days[firstIndex + i] = String.valueOf(i + 1);
		}

		List list = workCalendarService.getWorkDateList(year, month + 1);
		if (list == null)
			list = new ArrayList();

		request.setAttribute("list", list);
		request.setAttribute("year", String.valueOf(year));
		request.setAttribute("month", String.valueOf(month));
		request.setAttribute("weeks", String.valueOf(weeks));
		request.setAttribute("days", days);

		return new ModelAndView("/modules/others/calendar/calendar", modelMap);
	}
}