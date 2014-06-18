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
package com.liferay.portal.kernel.dao.search;

import com.liferay.portal.kernel.util.StringPool;

/**
 * @author Raymond Augé
 */
public interface SearchEntry {

	public static final String DEFAULT_ALIGN = "left";
	public static final int DEFAULT_COLSPAN = 1;
	public static final String DEFAULT_CSS_CLASS = StringPool.BLANK;
	public static final String DEFAULT_VALIGN = "middle";

	public String getAlign();

	public int getColspan();

	public String getCssClass();

	public int getIndex();

	public String getValign();

	public void print(Object object) throws Exception;

	public void setAlign(String align);

	public void setColspan(int colspan);

	public void setCssClass(String cssClass);

	public void setIndex(int index);

	public void setValign(String valign);

}