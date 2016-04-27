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

package com.liferay.portal.osgi.web.portlet.container.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.test.log.CaptureAppender;
import com.liferay.portal.test.log.Log4JLoggerTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.test.PortletContainerTestUtil;
import com.liferay.portal.util.test.PortletContainerTestUtil.Response;
import com.liferay.portlet.PortletURLImpl;
import com.liferay.portlet.SecurityPortletContainerWrapper;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Collections;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Raymond Augé
 */
@RunWith(Arquillian.class)
public class RenderRequestPortletContainerTest
	extends BasePortletContainerTestCase implements JSR286TestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testCopyCurrentRenderParameters() throws Exception {
		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		PortletURL renderURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.RENDER_PHASE);

		renderURL.setParameter("testRenderParameter", "testRenderParameter");

		PortletURL portletURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.RENDER_PHASE);

		((LiferayPortletURL)portletURL).setCopyCurrentRenderParameters(true);

		Response response = PortletContainerTestUtil.request(
			renderURL.toString());

		Assert.assertEquals(200, response.getCode());
		Assert.assertTrue(testPortlet.isCalledRender());

		String url = portletURL.toString();

		Assert.assertTrue(
			url + " does not contain the parameter",
			url.contains("testRenderParameter"));
	}

	@Test
	public void testInvalidPortletId() throws Exception {
		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		String url =
			layout.getRegularURL(httpServletRequest) +
				"?p_p_id='\"><script>alert(1)</script>&p_p_lifecycle=0&" +
					"p_p_state=exclusive";

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					SecurityPortletContainerWrapper.class.getName(),
					Level.WARN)) {

			Response response = PortletContainerTestUtil.request(url);

			List<LoggingEvent> loggingEvents =
				captureAppender.getLoggingEvents();

			Assert.assertEquals(1, loggingEvents.size());

			LoggingEvent loggingEvent = loggingEvents.get(0);

			Assert.assertEquals(
				"Invalid portlet ID '\"><script>alert(1)</script>",
				loggingEvent.getMessage());
			Assert.assertEquals(200, response.getCode());
		}
	}

	@Test
	public void testIsAccessGrantedByPortletAuthenticationToken()
		throws Exception {

		TestPortlet testTargetPortlet = new TestPortlet();

		Dictionary<String, Object> properties = new HashMapDictionary<>();

		properties.put(
			"com.liferay.portlet.add-default-resource", Boolean.TRUE);
		properties.put("com.liferay.portlet.system", Boolean.TRUE);

		final String testTargetPortletId = "testTargetPortletId";

		setUpPortlet(testTargetPortlet, properties, testTargetPortletId, false);

		testPortlet = new TestPortlet() {

			@Override
			public void serveResource(
					ResourceRequest resourceRequest,
					ResourceResponse resourceResponse)
				throws IOException {

				PrintWriter printWriter = resourceResponse.getWriter();

				PortletURL portletURL = PortletURLFactoryUtil.create(
					resourceRequest, testTargetPortletId, layout.getPlid(),
					PortletRequest.RENDER_PHASE);

				String queryString = HttpUtil.getQueryString(
					portletURL.toString());

				Map<String, String[]> parameterMap = HttpUtil.getParameterMap(
					queryString);

				String portletAuthenticationToken = MapUtil.getString(
					parameterMap, "p_p_auth");

				printWriter.write(portletAuthenticationToken);
			}

		};

		properties = new HashMapDictionary<>();

		setUpPortlet(testPortlet, properties, TEST_PORTLET_ID);

		// Get the portlet authentication token by making a resource request

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		PortletURL portletURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.RESOURCE_PHASE);

		Response response = PortletContainerTestUtil.request(
			portletURL.toString());

		testTargetPortlet.reset();

		// Make a render request to the target portlet using the portlet
		// authentication token

		portletURL = new PortletURLImpl(
			httpServletRequest, testTargetPortletId, layout.getPlid(),
			PortletRequest.RENDER_PHASE);

		portletURL.setWindowState(WindowState.MAXIMIZED);

		String url = portletURL.toString();

		url = HttpUtil.setParameter(url, "p_p_auth", response.getBody());

		response = PortletContainerTestUtil.request(
			url, Collections.singletonMap("Cookie", response.getCookies()));

		Assert.assertEquals(200, response.getCode());
		Assert.assertTrue(testTargetPortlet.isCalledRender());
	}

	@Test
	public void testIsAccessGrantedByPortletOnPage() throws Exception {
		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		PortletURL portletURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.RENDER_PHASE);

		Response response = PortletContainerTestUtil.request(
			portletURL.toString());

		Assert.assertEquals(200, response.getCode());
		Assert.assertTrue(testPortlet.isCalledRender());
	}

	@Test
	public void testIsAccessGrantedByRuntimePortlet() throws Exception {
		testPortlet = new TestPortlet() {

			@Override
			public void render(
					RenderRequest renderRequest, RenderResponse renderResponse)
				throws IOException, PortletException {

				PortletContext portletContext = getPortletContext();

				PortletRequestDispatcher portletRequestDispatcher =
					portletContext.getRequestDispatcher("/runtime_portlet.jsp");

				portletRequestDispatcher.include(renderRequest, renderResponse);
			}

		};

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		PortletURL portletURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.RENDER_PHASE);

		TestPortlet testRuntimePortlet = new TestPortlet();
		String testRuntimePortletId = "testRuntimePortletId";

		setUpPortlet(
			testRuntimePortlet, new HashMapDictionary<String, Object>(),
			testRuntimePortletId, false);

		portletURL.setParameter("testRuntimePortletId", testRuntimePortletId);

		Response response = PortletContainerTestUtil.request(
			portletURL.toString());

		Assert.assertEquals(200, response.getCode());
		Assert.assertTrue(testRuntimePortlet.isCalledRender());
	}

	@Test
	public void testRemoveCopiedCurrentRenderParameter() throws Exception {
		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		PortletURL renderURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.RENDER_PHASE);

		renderURL.setParameter("testRenderParameter", "testRenderParameter");

		PortletURL portletURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.RENDER_PHASE);

		((LiferayPortletURL)portletURL).setCopyCurrentRenderParameters(true);

		portletURL.setParameter("testRenderParameter", "");

		Response response = PortletContainerTestUtil.request(
			renderURL.toString());

		Assert.assertEquals(200, response.getCode());
		Assert.assertTrue(testPortlet.isCalledRender());

		String url = portletURL.toString();

		Assert.assertFalse(
			url + " contains the parameter", url.contains("testRenderParameter"));
	}

	@Test
	public void testRemoveRegularParameter() throws Exception {
		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		PortletURL renderURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.RENDER_PHASE);

		renderURL.setParameter("testRenderParameter", "testRenderParameter");

		PortletURL portletURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.RENDER_PHASE);

		((LiferayPortletURL)portletURL).setCopyCurrentRenderParameters(true);

		portletURL.setParameter("name1", "value1");
		portletURL.setParameter("name2", "value2");
		portletURL.setParameter("name1", "");

		Response response = PortletContainerTestUtil.request(
			renderURL.toString());

		Assert.assertEquals(200, response.getCode());
		Assert.assertTrue(testPortlet.isCalledRender());

		String url = portletURL.toString();

		String message = url + " does not contains the parameter";

		Assert.assertTrue(message, url.contains("testRenderParameter"));
		Assert.assertFalse(message, url.contains("name1"));
		Assert.assertTrue(message, url.contains("name2"));
	}

}