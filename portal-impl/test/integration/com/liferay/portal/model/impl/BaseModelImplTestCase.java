/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.model.impl;

import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.util.HtmlImpl;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public abstract class BaseModelImplTestCase {

	public static void setUp() {
		new HtmlUtil().setHtml(new HtmlImpl());
	}

	public abstract BaseModel<?> getModel();

	public abstract BaseModel<?> getUnescapedModel();

	@Test
	public void testToUnescapedModel() {
		Assert.assertEquals(getModel(), getUnescapedModel());
	}

	protected static final String ESCAPED_TEXT = "old mc&#039;donald";
	protected static final String UNESCAPED_TEXT = "old mc'donald";

}