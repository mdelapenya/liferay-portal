/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.osgi.web.wab.extender.internal.adapter;

import com.liferay.portal.osgi.web.servlet.jsp.compiler.JspServlet;
import com.liferay.portal.osgi.web.servlet.jsp.compiler.configuration.JspServletConfiguration;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

/**
 * @author Raymond Augé
 * @author Miguel Pastor
 */
public class JspServletWrapper extends HttpServlet {

	public JspServletWrapper(String jspFile) {
		this(jspFile, null);
	}

	public JspServletWrapper(
		String jspFile, JspServletConfiguration jspServletConfiguration) {

		this.jspFile = jspFile;

		servlet = new JspServlet(jspServletConfiguration);
	}

	@Override
	public void destroy() {
		servlet.destroy();
	}

	@Override
	public ServletConfig getServletConfig() {
		return servlet.getServletConfig();
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		servlet.init(servletConfig);
	}

	@Override
	public void service(
			ServletRequest servletRequest, ServletResponse servletResponse)
		throws IOException, ServletException {

		String curJspFile = (String)servletRequest.getAttribute(
			JspServlet.JSP_FILE);

		if (jspFile != null) {
			servletRequest.setAttribute(JspServlet.JSP_FILE, jspFile);
		}

		try {
			servlet.service(servletRequest, servletResponse);
		}
		finally {
			servletRequest.setAttribute(JspServlet.JSP_FILE, curJspFile);
		}
	}

	protected String jspFile;
	protected Servlet servlet;

}