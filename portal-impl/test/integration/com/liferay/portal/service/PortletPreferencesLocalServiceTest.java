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
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.model.PortletPreferencesIds;
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
	public void testAddPreferencesDefault() throws Exception {
		_layoutTypePortlet.addPortletId(
			TestPropsValues.getUserId(), _portlet.getPortletId(), false);

		javax.portlet.PortletPreferences javaxPortletPreferences =
			addPortelPreferencesReturnJavaxPreferences(_portlet.getPortletId());

		Assert.assertTrue(javaxPortletPreferences.getMap().isEmpty());
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

	@Test
	public void testAddPreferencesMultipleValues() throws Exception {
		String preferenceName = "name";

		String[] preferenceValues = {"value1", "value2"};

		String preferencesAsXml = getPreferencesAsXMLString(
			preferenceName, preferenceValues);

		_layoutTypePortlet.addPortletId(
			TestPropsValues.getUserId(), _portlet.getPortletId(), false);

		javax.portlet.PortletPreferences javaxPortletPreferences =
			addPortelPreferencesReturnJavaxPreferences(
				_portlet, preferencesAsXml);

		String[] actualValues =
			javaxPortletPreferences.getMap().get(preferenceName);

		Assert.assertArrayEquals(preferenceValues, actualValues);
	}

	@Test
	public void testDeletePortletPreferencesByPlid() throws Exception {
		Portlet[] portlets = getTestPortlets(2);

		PortletPreferences[] portletPreferences = addPortelsPreferences(
			portlets);

		PortletPreferences[] actualPortletPreferences = fetchPortletPreferences(
			portletPreferences);

		Assert.assertNotNull(actualPortletPreferences[0]);

		Assert.assertNotNull(actualPortletPreferences[1]);

		PortletPreferencesLocalServiceUtil.deletePortletPreferencesByPlid(
			_layout.getPlid());

		actualPortletPreferences = fetchPortletPreferences(portletPreferences);

		Assert.assertNull(actualPortletPreferences[0]);

		Assert.assertNull(actualPortletPreferences[1]);
	}

	@Test
	public void testDeletePortletPreferencesByPlidAndOwner() throws Exception {
		Group group2 = GroupTestUtil.addGroup();

		Portlet[] portlets = getTestPortlets(3);

		PortletPreferences[] portletPreferences = ArrayUtil.append(
			addPortelsPreferences(_group, ArrayUtil.subset(portlets, 0, 2)),
			addPortelPreferences(group2, portlets[2]));

		PortletPreferences[] actualPortletPreferences = fetchPortletPreferences(
			portletPreferences);

		Assert.assertNotNull(actualPortletPreferences[0]);

		Assert.assertNotNull(actualPortletPreferences[1]);

		Assert.assertNotNull(actualPortletPreferences[2]);

		PortletPreferencesLocalServiceUtil.deletePortletPreferences(
			_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
			_layout.getPlid());

		actualPortletPreferences = fetchPortletPreferences(portletPreferences);

		Assert.assertNull(actualPortletPreferences[0]);

		Assert.assertNull(actualPortletPreferences[1]);

		Assert.assertNotNull(actualPortletPreferences[2]);
	}

	@Test
	public void testDeletePortletPreferencesByPortletId() throws Exception {
		Portlet[] portlets = getTestPortlets(2);

		PortletPreferences[] portletPreferences = addPortelsPreferences(
			portlets);

		PortletPreferences[] actualPortletPreferences = fetchPortletPreferences(
			portletPreferences);

		Assert.assertNotNull(actualPortletPreferences[0]);

		Assert.assertNotNull(actualPortletPreferences[1]);

		PortletPreferencesLocalServiceUtil.deletePortletPreferences(
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
			portlets[0].getPortletId());

		actualPortletPreferences = fetchPortletPreferences(portletPreferences);

		Assert.assertNull(actualPortletPreferences[0]);

		Assert.assertNotNull(actualPortletPreferences[1]);
	}

	@Test
	public void testDeletePortletPreferencesByPortletPreferencesId()
		throws Exception {

		PortletPreferences portletPreferences =  addPortelPreferences();

		PortletPreferences portletPreferencesRecovered =
			PortletPreferencesLocalServiceUtil.fetchPortletPreferences(
				portletPreferences.getPortletPreferencesId());

		Assert.assertNotNull(portletPreferencesRecovered);

		PortletPreferencesLocalServiceUtil.deletePortletPreferences(
			portletPreferences.getPortletPreferencesId());

		portletPreferencesRecovered =
			PortletPreferencesLocalServiceUtil.fetchPortletPreferences(
				portletPreferences.getPortletPreferencesId());

		Assert.assertNull(portletPreferencesRecovered);
	}

	@Test
	public void testFetchPreferences() throws Exception {
		String preferenceName = "name";

		String[] preferenceValues = {"defaultValue"};

		String preferencesAsXml = getPreferencesAsXMLString(
			preferenceName, preferenceValues);

		addPortelPreferencesReturnJavaxPreferences(_portlet, preferencesAsXml);

		javax.portlet.PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.fetchPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertTrue(!portletPreferences.getMap().isEmpty());

		Assert.assertArrayEquals(
			preferenceValues, portletPreferences.getMap().get(preferenceName));
	}

	@Test
	public void testFetchPreferencesByPortletPreferencesIds() throws Exception {
		String preferenceName = "name";

		String[] preferenceValues = {"defaultValue"};

		String preferencesAsXml = getPreferencesAsXMLString(
			preferenceName, preferenceValues);

		addPortelPreferencesReturnJavaxPreferences(_portlet, preferencesAsXml);

		PortletPreferencesIds portletPreferencesIds =
			new PortletPreferencesIds(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		javax.portlet.PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.fetchPreferences(
				portletPreferencesIds);

		Assert.assertTrue(!portletPreferences.getMap().isEmpty());

		Assert.assertArrayEquals(
			preferenceValues, portletPreferences.getMap().get(preferenceName));
	}

	@Test
	public void testFetchPreferencesNull() throws Exception {
		String preferenceName = "name";

		String[] preferenceValues = {"defaultValue"};

		String preferencesAsXml = getPreferencesAsXMLString(
			preferenceName, preferenceValues);

		addPortelPreferencesReturnJavaxPreferences(_portlet, preferencesAsXml);

		PortletPreferencesLocalServiceUtil.deletePortletPreferences(
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
			_portlet.getPortletId());

		javax.portlet.PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.fetchPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertNull(portletPreferences);
	}

	private Layout addLayout() throws Exception {
		return addLayout(_group);
	}

	private Layout addLayout(Group group) throws Exception {
		return LayoutTestUtil.addLayout(
			group.getGroupId(), ServiceTestUtil.randomString(), false);
	}

	private PortletPreferences addPortelPreferences() throws Exception {
		return addPortelPreferences(
			null, null, _portlet, _portlet.getPortletId(), null);
	}

	private PortletPreferences addPortelPreferences(
			Group group, Layout layout, Portlet portlet, String portletId,
			String defaultPreferences)
		throws Exception {

		if (layout == null)
			layout = _layout;

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

	private PortletPreferences addPortelPreferences(
			Group group, Portlet portlet)
		throws Exception {

		return addPortelPreferences(
			group, null, portlet, portlet.getPortletId(), null);
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

	private javax.portlet.PortletPreferences
			addPortelPreferencesReturnJavaxPreferences(
				Portlet portlet, String defaultPreferences)
		throws Exception {

		PortletPreferences portletPreferences =
			addPortelPreferences(
				null, _layout, portlet, portlet.getPortletId(),
				defaultPreferences);

		return getJavaxPortalPreferences(
			portlet.getPortletId(), portletPreferences.getPreferences());
	}

	private javax.portlet.PortletPreferences
			addPortelPreferencesReturnJavaxPreferences(String portletId)
		throws Exception {

		PortletPreferences portletPreferences = addPortelPreferences(
			null, _layout, null, portletId, null);

		return getJavaxPortalPreferences(
			portletId, portletPreferences.getPreferences());
	}

	private PortletPreferences[] addPortelsPreferences(
			Group group, Portlet[] portlets)
		throws Exception {

		PortletPreferences[] results = new PortletPreferences[portlets.length];

		for (int i = 0; i < results.length; i++) {
			results[i] = addPortelPreferences(
				group, null, portlets[i], portlets[i].getPortletId(), null);
		}

		return results;
	}

	private PortletPreferences[] addPortelsPreferences(Portlet[] portlets)
		throws Exception {

		PortletPreferences[] results = new PortletPreferences[portlets.length];

		for (int i = 0; i < results.length; i++) {
			results[i] = addPortelPreferences(
				null, null, portlets[i], portlets[i].getPortletId(), null);
		}

		return results;
	}

	private PortletPreferences[] fetchPortletPreferences(
			PortletPreferences[] portletPreferenceses)
		throws Exception {

		PortletPreferences[] results =
			new PortletPreferences[portletPreferenceses.length];

		for (int i = 0; i < results.length; i++) {
			results[i] =
				PortletPreferencesLocalServiceUtil.fetchPortletPreferences(
					portletPreferenceses[i].getPortletPreferencesId());
		}

		return results;
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