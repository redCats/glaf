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

package com.glaf.core.web.springmvc;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.core.base.TreeModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.template.Template;
import com.glaf.core.template.query.TemplateQuery;
import com.glaf.core.template.service.ITemplateService;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.JsonUtils;
import com.glaf.core.util.LogUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.Tools;

@Controller("/system/template")
@RequestMapping("/system/template")
public class MxSysTemplateController {
	protected final static Log logger = LogFactory
			.getLog(MxSysTemplateController.class);
	@Resource
	protected ITemplateService templateService;

	@RequestMapping("/delete")
	public ModelAndView deleteTemplate(HttpServletRequest request,
			ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String templateId = request.getParameter("templateId");
		if (StringUtils.isNotEmpty(templateId)) {
			templateService.deleteTemplate(templateId);
		}
		return this.list(request, modelMap);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String templateId = request.getParameter("templateId");
		if (StringUtils.isNotEmpty(templateId)) {
			Template template = templateService.getTemplate(templateId);
			modelMap.put("template", template);
		}
		String jx_view = request.getParameter("jx_view");

		if (StringUtils.isNotEmpty(jx_view)) {
			return new ModelAndView(jx_view, modelMap);
		}

		String x_view = ViewProperties.getString("sys_template.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/modules/sys/template/edit");
	}

	public ITemplateService getTemplateService() {
		return templateService;
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		Map<String, Object> paramMap = RequestUtils.getParameterMap(request);
		String queryString = request.getParameter("x_complex_query");
		if (StringUtils.isNotEmpty(queryString)) {
			queryString = RequestUtils.decodeString(queryString);
			if (LogUtils.isDebug()) {
				logger.debug(queryString);
			}
			Map<String, Object> xMap = JsonUtils.decode(queryString);
			if (xMap != null) {
				paramMap.putAll(xMap);
			}
		}

		TemplateQuery query = new TemplateQuery();
		Tools.populate(query, paramMap);

		query.setPageSize(-1);
		query.setParameter(paramMap);
		List<Template> templates = templateService.getTemplates(query);
		modelMap.put("templates", templates);

		String jx_view = request.getParameter("jx_view");

		if (StringUtils.isNotEmpty(jx_view)) {
			return new ModelAndView(jx_view, modelMap);
		}

		String x_view = ViewProperties.getString("sys_template.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/modules/sys/template/list");
	}

	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest request, ModelMap modelMap) {
		// RequestUtils.setRequestParameterToAttribute(request);
		String jx_view = request.getParameter("jx_view");

		RequestUtils.setRequestParameterToAttribute(request);
		TreeModel treeModel = IdentityFactory.getTreeModelByCode("template");
		if (treeModel != null) {
			modelMap.put("treeModel", treeModel);
			request.setAttribute("parent", treeModel.getId() + "");
		}

		if (StringUtils.isNotEmpty(jx_view)) {
			return new ModelAndView(jx_view, modelMap);
		}

		String x_view = ViewProperties.getString("sys_template.main");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/modules/sys/template/main");
	}

	@RequestMapping("/save")
	public ModelAndView save(HttpServletRequest request, ModelMap modelMap)
			throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		String templateId = request.getParameter("templateId");
		Template template = null;
		if (StringUtils.isNotEmpty(templateId)) {
			template = templateService.getTemplate(templateId);
		}

		if (template == null) {
			template = new Template();
			template.setCreateBy(loginContext.getActorId());
		}

		MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
		Map<String, Object> paramMap = RequestUtils.getParameterMap(req);
		Tools.populate(template, paramMap);

		String nodeId = ParamUtils.getString(paramMap, "nodeId");
		if (nodeId != null) {

		}

		Map<String, MultipartFile> fileMap = req.getFileMap();
		Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
		for (Entry<String, MultipartFile> entry : entrySet) {
			MultipartFile mFile = entry.getValue();
			String filename = mFile.getOriginalFilename();
			if (mFile.getSize() > 0) {
				template.setFileSize(mFile.getSize());
				int fileType = 0;

				if (filename.endsWith(".java")) {
					fileType = 50;
				} else if (filename.endsWith(".jsp")) {
					fileType = 51;
				} else if (filename.endsWith(".ftl")) {
					fileType = 52;
					template.setLanguage("freemarker");
				} else if (filename.endsWith(".vm")) {
					fileType = 54;
					template.setLanguage("velocity");
				} else if (filename.endsWith(".xml")) {
					fileType = 60;
				} else if (filename.endsWith(".htm")
						|| filename.endsWith(".html")) {
					fileType = 80;
				}

				template.setDataFile(filename);
				template.setFileType(fileType);
				template.setCreateDate(new Date());
				template.setData(mFile.getBytes());
				template.setLastModified(System.currentTimeMillis());
				template.setTemplateType(FileUtils.getFileExt(filename));
				break;
			}
		}

		templateService.saveTemplate(template);

		return this.list(request, modelMap);
	}

	public void setTemplateService(ITemplateService templateService) {
		this.templateService = templateService;
	}

	/**
	 * ��ʾ���ҳ��
	 * 
	 * @param mapping
	 * @param request
	 * @return
	 */
	@RequestMapping("/showFrame")
	public ModelAndView showFrame(HttpServletRequest request, ModelMap modelMap) {
		// RequestUtils.setRequestParameterToAttribute(request);
		TreeModel treeModel = IdentityFactory.getTreeModelByCode("template");
		if (treeModel != null) {
			modelMap.put("treeModel", treeModel);
			request.setAttribute("parent", treeModel.getId() + "");
		}
		return new ModelAndView("/modules/sys/template/template_frame",
				modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String templateId = request.getParameter("templateId");
		if (StringUtils.isNotEmpty(templateId)) {
			Template template = templateService.getTemplate(templateId);
			modelMap.put("template", template);
		}
		String jx_view = request.getParameter("jx_view");

		if (StringUtils.isNotEmpty(jx_view)) {
			return new ModelAndView(jx_view, modelMap);
		}

		String x_view = ViewProperties.getString("sys_template.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/modules/sys/template/view");
	}

}