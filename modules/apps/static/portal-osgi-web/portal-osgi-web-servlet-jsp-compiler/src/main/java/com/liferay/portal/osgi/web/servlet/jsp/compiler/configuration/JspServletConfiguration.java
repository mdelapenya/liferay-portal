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

package com.liferay.portal.osgi.web.servlet.jsp.compiler.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Manuel de la Peña
 */
@ExtendedObjectClassDefinition(category = "foundation")
@Meta.OCD(
	id = "com.liferay.portal.osgi.web.servlet.jsp.compiler.configuration.JspServletConfiguration",
	localization = "content/Language", name = "jsp.servlet.configuration.name"
)
public interface JspServletConfiguration {

	@Meta.AD(
		deflt = "com.liferay.portal.osgi.web.servlet.jsp.compiler.internal.JspCompiler",
		required = false
	)
	public String compilerClassName();

	@Meta.AD(deflt = "1.7", required = false)
	public String compilerSourceVM();

	@Meta.AD(deflt = "1.7", required = false)
	public String compilerTargetVM();

	@Meta.AD(deflt = "false", required = false)
	public boolean development();

	@Meta.AD(deflt = "GET|POST|HEAD", required = false)
	public String[] httpMethods();

	@Meta.AD(deflt = "false", required = false)
	public boolean keepgenerated();

	@Meta.AD(
		deflt = "NONE",
		optionLabels = {"NONE", "DEBUG", "INFO", "WARN", "ERROR"},
		optionValues = {"NONE", "DEBUG", "INFO", "WARN", "ERROR"},
		required = false
	)
	public String logVerbosityLevel();

	@Meta.AD(deflt = "true", required = false)
	public boolean saveBytecode();

}