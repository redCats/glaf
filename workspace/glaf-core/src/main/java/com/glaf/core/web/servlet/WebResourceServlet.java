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
package com.glaf.core.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.util.FileUtils;
import com.glaf.core.util.IOUtils;

public class WebResourceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected static final Log logger = LogFactory
			.getLog(WebResourceServlet.class);

	protected static ConcurrentMap<String, byte[]> concurrentMap = new ConcurrentHashMap<String, byte[]>();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String contextPath = request.getContextPath();
		String requestURI = request.getRequestURI();
		String resPath = requestURI.substring(contextPath.length(),
				requestURI.length());

		// logger.debug("contextPath:" + contextPath);
		// logger.debug("requestURI:" + requestURI);
		// logger.debug("resPath:" + resPath);

		int slash = request.getRequestURI().lastIndexOf("/");
		String file = request.getRequestURI().substring(slash + 1);

		int dot = file.lastIndexOf(".");
		String ext = file.substring(dot + 1);
		String contentType = "";
		if (StringUtils.equalsIgnoreCase(ext, "jpg")
				|| StringUtils.equalsIgnoreCase(ext, "jpeg")
				|| StringUtils.equalsIgnoreCase(ext, "gif")
				|| StringUtils.equalsIgnoreCase(ext, "png")
				|| StringUtils.equalsIgnoreCase(ext, "bmp")) {
			contentType = "image/" + ext;
		} else if (StringUtils.equalsIgnoreCase(ext, "svg")) {
			contentType = "image/svg+xml";
		} else if (StringUtils.equalsIgnoreCase(ext, "css")) {
			contentType = "text/css";
		} else if (StringUtils.equalsIgnoreCase(ext, "txt")) {
			contentType = "text/plain";
		} else if (StringUtils.equalsIgnoreCase(ext, "htm")
				|| StringUtils.equalsIgnoreCase(ext, "html")) {
			contentType = "text/html";
		} else if (StringUtils.equalsIgnoreCase(ext, "js")) {
			contentType = "application/javascript";
		} else if (StringUtils.equalsIgnoreCase(ext, "ttf")) {
			contentType = "application/x-font-ttf";
		} else if (StringUtils.equalsIgnoreCase(ext, "eot")) {
			contentType = "application/vnd.ms-fontobject";
		} else if (StringUtils.equalsIgnoreCase(ext, "woff")) {
			contentType = "application/x-font-woff";
		} else if (StringUtils.equalsIgnoreCase(ext, "swf")) {
			contentType = "application/x-shockwave-flash";
		}

		ServletOutputStream outraw = null;
		try {
			outraw = response.getOutputStream();
			byte[] raw = concurrentMap.get(resPath);
			if (raw == null) {
				InputStream is = WebResourceServlet.class
						.getResourceAsStream(resPath);
				raw = FileUtils.getBytes(is);
				IOUtils.closeStream(is);
				concurrentMap.put(resPath, raw);
				logger.debug("load resource:" + resPath);
			}
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType(contentType);
			response.setContentLength(raw.length);
			outraw.write(raw);
		} catch (IOException ex) {
			// ex.printStackTrace();
		} finally {
			IOUtils.closeStream(outraw);
		}

		response.flushBuffer();
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
