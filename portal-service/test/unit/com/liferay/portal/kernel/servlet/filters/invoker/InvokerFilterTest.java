/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.servlet.filters.invoker;

import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Mika Koivisto
 */
public class InvokerFilterTest {

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		HttpUtil httpUtil = new HttpUtil();

		httpUtil.setHttp(_mockHttp);
	}

	@Test
	public void testGetURIInvokesHttpRemovePathParameters() {

		InvokerFilter invokerFilter = new InvokerFilter();

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest(
				HttpMethods.GET,
				"/c/portal/login;jsessionid=ae01b0f2af.worker1");

		invokerFilter.getURI(mockHttpServletRequest);

		// assert that Http.removePathParameters was indeed invoked

		Mockito.verify(_mockHttp).removePathParameters(_argumentUri.capture());

		Assert.assertEquals(
			"URI processed by InvokerFilter must be handed over to the Http",
			"/c/portal/login;jsessionid=ae01b0f2af.worker1",
			_argumentUri.getValue());
	}

	private @Captor ArgumentCaptor<String> _argumentUri;
	private @Mock Http _mockHttp;

}