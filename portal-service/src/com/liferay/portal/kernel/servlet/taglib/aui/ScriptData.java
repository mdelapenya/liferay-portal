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
package com.liferay.portal.kernel.servlet.taglib.aui;

import com.liferay.portal.kernel.util.StringBundler;

import java.io.IOException;
import java.io.Writer;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Raymond Augé
 */
public interface ScriptData {

	public void append(String portletId, String content, String use);

	public void append(String portletId, StringBundler contentSB, String use);

	public Map<String, PortletScriptData> getPortletScriptDataMap();

	public void mark();

	public ScriptData merge(ScriptData scriptData);

	public void reset();

	public void writeTo(HttpServletRequest request, Writer writer)
		throws IOException;

}