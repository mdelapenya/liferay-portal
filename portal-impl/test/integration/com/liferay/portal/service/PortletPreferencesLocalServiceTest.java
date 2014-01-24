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

package com.liferay.portal.service;

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalCallbackAwareExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portal.util.LayoutTestUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Cristina González
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		TransactionalCallbackAwareExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class PortletPreferencesLocalServiceTest {

	@Before
	public void setUp() throws Exception {
		FinderCacheUtil.clearCache();

		_group = GroupTestUtil.addGroup();

		_layout = addLayout();

		_layoutTypePortlet = (LayoutTypePortlet)_layout.getLayoutType();

		_portlet = getTestPortlets(1)[0];
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddPreferencesDefaultPortlet() throws Exception {
		_layoutTypePortlet.addPortletId(
			TestPropsValues.getUserId(), _portlet.getPortletId(), false);

		String preferenceName = "name";

		String[] preferenceValues = {"defaultValue"};

		String preferencesAsXml = getPreferencesAsXMLString(
			preferenceName, preferenceValues);

		_portlet.setDefaultPreferences(preferencesAsXml);

		javax.portlet.PortletPreferences javaxPortletPreferences =
			addPortelPreferencesReturnJavaxPreferences(_portlet);

		Assert.assertTrue(!javaxPortletPreferences.getMap().isEmpty());

		Assert.assertArrayEquals(
			preferenceValues,
			javaxPortletPreferences.getMap().get(preferenceName));
	}

	private Layout addLayout() throws Exception {
		return addLayout(_group);
	}

	private Layout addLayout(Group group) throws Exception {
		return LayoutTestUtil.addLayout(
			group.getGroupId(), ServiceTestUtil.randomString(), false);
	}

	private PortletPreferences addPortelPreferences(
			Group group, Layout layout, Portlet portlet, String portletId,
			String defaultPreferences)
		throws Exception {

		if (group != null) {
			return PortletPreferencesLocalServiceUtil.addPortletPreferences(
				TestPropsValues.getCompanyId(), group.getGroupId(),
				PortletKeys.PREFS_OWNER_TYPE_GROUP, layout.getPlid(), portletId,
				portlet, defaultPreferences);
		}
		else {
			return PortletPreferencesLocalServiceUtil.addPortletPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, layout.getPlid(),
				portletId, portlet, defaultPreferences);
		}
	}

	private javax.portlet.PortletPreferences
			addPortelPreferencesReturnJavaxPreferences(Portlet portlet)
		throws Exception {

		PortletPreferences portletPreferences =
			addPortelPreferences(
				null, _layout, portlet, portlet.getPortletId(), null);

		return getJavaxPortalPreferences(
			portlet.getPortletId(), portletPreferences.getPreferences());
	}

	private javax.portlet.PortletPreferences getJavaxPortalPreferences(
			String portletId, String portletPreferences)
		throws Exception {

		return PortletPreferencesFactoryUtil.fromXML(
			TestPropsValues.getCompanyId(), PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(), portletId,
			portletPreferences);
	}

	private String getPreferencesAsXMLString(String name, String[] values) {

		String preferencesAsXml = "<portlet-preferences><preference>";
		preferencesAsXml+= "<name>" + name + "</name>";

		for (String value : values) {
			preferencesAsXml+= "<value>" + value + "</value>";
		}

		preferencesAsXml+="</preference></portlet-preferences>";
		return preferencesAsXml;
	}

	private Portlet[] getTestPortlets(int numberPortlets) throws Exception {
		Portlet[] results = new Portlet[numberPortlets];

		for (int i = 0; i < results.length; i++) {
			results[i] = PortletLocalServiceUtil.getPortletById(
				TestPropsValues.getCompanyId(),
				String.valueOf(_INIT_PORTLET_ID + i));
		}

		return results;
	}

	private static final int _INIT_PORTLET_ID = 1000;

	private Group _group;
	private Layout _layout;
	private LayoutTypePortlet _layoutTypePortlet;
	private Portlet _portlet;

}