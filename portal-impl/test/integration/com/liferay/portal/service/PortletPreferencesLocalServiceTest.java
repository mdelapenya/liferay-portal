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
import com.liferay.portlet.PortletPreferencesImpl;
import com.liferay.portlet.StrictPortletPreferencesImpl;

import java.util.List;

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

		String preferencesAsXml = getPreferencesAsXMLString();

		_portlet.setDefaultPreferences(preferencesAsXml);

		javax.portlet.PortletPreferences javaxPortletPreferences =
			addPortelPreferencesReturnJavaxPreferences(_portlet);

		Assert.assertTrue(!javaxPortletPreferences.getMap().isEmpty());

		String[] actualValues =
			javaxPortletPreferences.getMap().get(_PREFERENCE_NAME);

		Assert.assertArrayEquals(_PREFERENCE_VALUES, actualValues);
	}

	@Test
	public void testAddPreferencesMultipleValues() throws Exception {
		String[] preferenceValues = {"value1", "value2"};
		String preferencesAsXml = getPreferencesAsXMLString(preferenceValues);

		_layoutTypePortlet.addPortletId(
			TestPropsValues.getUserId(), _portlet.getPortletId(), false);

		javax.portlet.PortletPreferences javaxPortletPreferences =
			addPortelPreferencesReturnJavaxPreferences(
				_portlet, preferencesAsXml);

		String[] actualValues =
			javaxPortletPreferences.getMap().get(_PREFERENCE_NAME);

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
		String preferencesAsXml = getPreferencesAsXMLString();

		addPortelPreferencesReturnJavaxPreferences(_portlet, preferencesAsXml);

		javax.portlet.PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.fetchPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertTrue(!portletPreferences.getMap().isEmpty());

		Assert.assertArrayEquals(
			_PREFERENCE_VALUES,
			portletPreferences.getMap().get(_PREFERENCE_NAME));
	}

	@Test
	public void testFetchPreferencesByPortletPreferencesIds() throws Exception {
		String preferencesAsXml = getPreferencesAsXMLString();

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
			_PREFERENCE_VALUES,
			portletPreferences.getMap().get(_PREFERENCE_NAME));
	}

	@Test
	public void testFetchPreferencesNull() throws Exception {
		String preferencesAsXml = getPreferencesAsXMLString();

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

	@Test
	public void testGetDefaultPreferences() throws Exception {
		javax.portlet.PortletPreferences defaultPreferences =
			PortletPreferencesLocalServiceUtil.getDefaultPreferences(
				TestPropsValues.getCompanyId(), _PORTEL_WITH_PREFERENCES_ID);

		Assert.assertTrue(!defaultPreferences.getMap().isEmpty());
		String[] symbols = {"GBP", "CNY", "EUR", "JPY", "USD"};

		Assert.assertArrayEquals(
			symbols, defaultPreferences.getMap().get("symbols"));
	}

	@Test
	public void testGetPortletPreferencesAll() throws Exception {
		List<PortletPreferences> initialPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences();

		int initialCount = initialPortletPreferences.size();

		addPortelPreferences();

		List<PortletPreferences> actualPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences();

		Assert.assertEquals(initialCount + 1, actualPortletPreferences.size());
	}

	@Test
	public void testGetPortletPreferencesByCompanyGroupOwnerPortlet()
		throws Exception {

		Layout[] layouts = new Layout[2];
		layouts[0] = addLayout(_group);
		layouts[1] = addLayout(GroupTestUtil.addGroup());

		Portlet[] portlets = getTestPortlets(1);

		addPortelsPreferences(layouts[0], portlets);

		addPortelsPreferences(layouts[1], portlets);

		List<PortletPreferences> portletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				layouts[0].getCompanyId(), layouts[0].getGroupId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, portlets[0].getPortletId(),
				false);

		Assert.assertEquals(1, portletPreferences.size());

		Assert.assertEquals(
			portletPreferences.get(0).getPlid(), layouts[0].getPlid());
	}

	@Test
	public void testGetPortletPreferencesByCompanyOwnerPortletRepeatedGroup()
		throws Exception {

		Layout[] layouts = addLayout(GroupTestUtil.addGroup(), 2);

		Portlet[] portlets = getTestPortlets(1);

		PortletPreferences[] arrayPortalPreferencesLayout1 =
			addPortelsPreferences(layouts[0], portlets);

		addPortelsPreferences(layouts[1], portlets);

		List<PortletPreferences> listPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				layouts[0].getCompanyId(), layouts[0].getGroupId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, portlets[0].getPortletId(),
				false);

		Assert.assertEquals(2, listPortletPreferences.size());

		for (PortletPreferences portletPreferences : listPortletPreferences) {
			if (arrayPortalPreferencesLayout1[0].getPortletPreferencesId() ==
					portletPreferences.getPortletPreferencesId()) {
						Assert.assertEquals(
							portletPreferences.getPlid(), layouts[0].getPlid());
			}
			else {
				Assert.assertEquals(
					portletPreferences.getPlid(), layouts[1].getPlid());
			}
		}
	}

	@Test
	public void testGetPortletPreferencesByOwnerAndLayout() throws Exception {
		Group group2 = GroupTestUtil.addGroup();

		Portlet[] portlets = getTestPortlets(2);

		PortletPreferences[] portletPreferences = ArrayUtil.append(
			addPortelsPreferences(_group, portlets),
			addPortelPreferences(group2, portlets[0]));

		PortletPreferences[] actualPortletPreferences = fetchPortletPreferences(
			portletPreferences);

		Assert.assertNotNull(actualPortletPreferences[0]);

		Assert.assertNotNull(actualPortletPreferences[1]);

		Assert.assertNotNull(actualPortletPreferences[2]);

		List<PortletPreferences> portletPreferencesList =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid());

		Assert.assertEquals(2, portletPreferencesList.size());

		portletPreferencesList =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				group2.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid());

		Assert.assertEquals(1, portletPreferencesList.size());
	}

	@Test
	public void testGetPortletPreferencesByOwnerLayoutPortlet()
		throws Exception {

		Group group2 = GroupTestUtil.addGroup();

		Portlet[] portlets = getTestPortlets(2);

		PortletPreferences[] arrayPortletPreferences = ArrayUtil.append(
			addPortelsPreferences(_group, portlets),
			addPortelPreferences(group2, portlets[0]));

		PortletPreferences[] actualPortletPreferences = fetchPortletPreferences(
			arrayPortletPreferences);

		Assert.assertNotNull(actualPortletPreferences[0]);

		Assert.assertNotNull(actualPortletPreferences[1]);

		Assert.assertNotNull(actualPortletPreferences[2]);

		PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid(), portlets[0].getPortletId());

		Assert.assertEquals(
			portletPreferences.getPortletId(), portlets[0].getPortletId());

		Assert.assertEquals(portletPreferences.getPlid(), _layout.getPlid());

		Assert.assertEquals(
			portletPreferences.getOwnerType(),
			PortletKeys.PREFS_OWNER_TYPE_GROUP);

		Assert.assertEquals(
			portletPreferences.getOwnerId(), _group.getGroupId());
	}

	@Test
	public void testGetPortletPreferencesByPlid() throws Exception {
		Layout[] layouts = addLayout(2);

		Portlet[] portlets = getTestPortlets(2);

		addPortelsPreferences(layouts[0], portlets);

		addPortelsPreferences(layouts[1], portlets);

		List<PortletPreferences> listPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesByPlid(
				layouts[0].getPlid());

		Assert.assertEquals(2, listPortletPreferences.size());
	}

	@Test
	public void testGetPortletPreferencesByPortletLayout() throws Exception {
		Layout[] layouts = addLayout(2);

		Portlet[] portlets = getTestPortlets(2);

		addPortelsPreferences(layouts[0], portlets);

		addPortelsPreferences(layouts[1], portlets);

		List<PortletPreferences> listPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				layouts[0].getPlid(), portlets[0].getPortletId());

		Assert.assertEquals(1, listPortletPreferences.size());
	}

	@Test
	public void testGetPortletPreferencesByPortletOwnerTypeLayout()
		throws Exception {

		Group group2 = GroupTestUtil.addGroup();

		Portlet[] portlets = getTestPortlets(1);

		PortletPreferences[] portletPreferences = ArrayUtil.append(
			addPortelsPreferences(portlets),
			addPortelPreferences(group2, portlets[0]));

		PortletPreferences[] actualPortletPreferences = fetchPortletPreferences(
			portletPreferences);

		Assert.assertNotNull(actualPortletPreferences[0]);

		Assert.assertNotNull(actualPortletPreferences[1]);

		List<PortletPreferences> portletPreferencesList =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				portlets[0].getPortletId());

		Assert.assertEquals(1, portletPreferencesList.size());

		portletPreferencesList =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				PortletKeys.PREFS_OWNER_TYPE_GROUP, _layout.getPlid(),
				portlets[0].getPortletId());

		Assert.assertEquals(1, portletPreferencesList.size());

		portletPreferencesList =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				PortletKeys.PREFS_OWNER_TYPE_COMPANY, _layout.getPlid(),
				portlets[0].getPortletId());

		Assert.assertEquals(0, portletPreferencesList.size());
	}

	@Test
	public void testGetPortletPreferencesCountByOwnerLayoutPortlet()
		throws Exception {

		Group group2 = GroupTestUtil.addGroup();

		Portlet[] portlets = getTestPortlets(2);

		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid(), portlets[0], false);

		Assert.assertEquals(0, initialCount);

		addPortelsPreferences(_group, portlets);
		addPortelPreferences(group2, portlets[0]);

		long actualCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid(), portlets[0], false);

		Assert.assertEquals(1, actualCount);
	}

	@Test
	public void testGetPortletPreferencesCountByOwnerLayoutPortletNotDefault()
		throws Exception {

		Portlet[] portlets = getTestPortlets(2);

		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid(), portlets[0], true);

		Assert.assertEquals(0, initialCount);

		addPortelsPreferences(_group, portlets);

		long actualCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid(), portlets[0], true);

		Assert.assertEquals(0, actualCount);
	}

	@Test
	public void testGetPortletPreferencesCountByOwnerPortlet()
		throws Exception {

		Group group2 = GroupTestUtil.addGroup();

		Portlet[] portlets = getTestPortlets(2);

		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid(), portlets[0], false);

		Assert.assertEquals(0, initialCount);

		addPortelsPreferences(_group, portlets);
		addPortelPreferences(group2, portlets[0]);

		Assert.assertEquals(0, initialCount);

		long actualCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP, -1,
				portlets[0], false);

		Assert.assertEquals(1, actualCount);
	}

	@Test
		public void testGetPortletPreferencesCountByOwnerPortletExcludeDefault()
			throws Exception {

		Group group2 = GroupTestUtil.addGroup();

		Portlet[] portlets = getTestPortlets(1);

		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				portlets[0].getPortletId(), true);

		Assert.assertEquals(0, initialCount);

		addPortelsPreferences(_group, portlets);

		long actualCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				portlets[0].getPortletId(), true);

		Assert.assertEquals(0, actualCount);
	}

	@Test
	public void testGetPortletPreferencesCountByOwnerPortletNotExcludeDefault()
		throws Exception {

		Portlet[] portlets = getTestPortlets(1);

		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid(), portlets[0], false);

		Assert.assertEquals(0, initialCount);

		addPortelsPreferences(_group, portlets);

		Assert.assertEquals(0, initialCount);

		long actualCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				portlets[0].getPortletId(), false);

		Assert.assertEquals(1, actualCount);
	}

	@Test
	public void testGetPortletPreferencesCountByPortletOwnerType()
		throws Exception {

		Layout layout2 = addLayout();

		Portlet[] portlets = getTestPortlets(1);

		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
				portlets[0].getPortletId());

		addPortelsPreferences(portlets);
		addPortelsPreferences(layout2, portlets);
		addPortelPreferences(_group, portlets[0]);

		long actualCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
				portlets[0].getPortletId());

		Assert.assertEquals(initialCount + 2, actualCount);
	}

	@Test
	public void testGetPortletPreferencesCountyPortletOwnerTypeLayout()
		throws Exception {

		Group group2 = GroupTestUtil.addGroup();

		Portlet[] portlets = getTestPortlets(1);

		long initialCountTypeLayout =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				portlets[0].getPortletId());

		Assert.assertEquals(0, initialCountTypeLayout);

		long initialCountTypeGroup =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_GROUP, _layout.getPlid(),
				portlets[0].getPortletId());

		Assert.assertEquals(0, initialCountTypeGroup);

		long initialCountTypeCompany =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_COMPANY, _layout.getPlid(),
				portlets[0].getPortletId());

		Assert.assertEquals(0, initialCountTypeCompany);

		addPortelsPreferences(portlets);
		addPortelPreferences(group2, portlets[0]);

		long actualCountTypeLayout =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				portlets[0].getPortletId());

		Assert.assertEquals(1, actualCountTypeLayout);

		long actualCountTypeGroup =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_GROUP, _layout.getPlid(),
				portlets[0].getPortletId());

		Assert.assertEquals(1, actualCountTypeGroup);

		long actualCountTypeCompany =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_COMPANY, _layout.getPlid(),
				portlets[0].getPortletId());

		Assert.assertEquals(0, actualCountTypeCompany);
	}

	@Test
	public void testGetPreferencesByCompanyOwnerLayoutPortletAdded()
		throws Exception {

		javax.portlet.PortletPreferences portletPreferences =
			addPortelPreferencesReturnJavaxPreferences(
				_portlet, getPreferencesAsXMLString());

		javax.portlet.PortletPreferences actualPortelPreferences =
			getPreferences(_portlet);

		Assert.assertArrayEquals(
			portletPreferences.getMap().get(_PREFERENCE_NAME),
			actualPortelPreferences.getMap().get(_PREFERENCE_NAME));
	}

	@Test
	public void testGetPreferencesByCompanyOwnerLayoutPortletNotAdded()
		throws Exception {

		javax.portlet.PortletPreferences initialPortletPreferences =
			PortletPreferencesLocalServiceUtil.fetchPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertNull(initialPortletPreferences);

		PortletPreferencesImpl portletPreferences = (PortletPreferencesImpl )
			getPreferences(_portlet, getPreferencesAsXMLString());

		Assert.assertNotNull(portletPreferences.getMap());

		Assert.assertArrayEquals(
			_PREFERENCE_VALUES,
			portletPreferences.getMap().get(_PREFERENCE_NAME));

		javax.portlet.PortletPreferences actualPortletPreferences =
			PortletPreferencesLocalServiceUtil.fetchPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertArrayEquals(
			_PREFERENCE_VALUES,
			actualPortletPreferences.getMap().get(_PREFERENCE_NAME));
	}

	@Test
	public void testGetPreferencesByCompanyOwnerLayoutPortletNotAddedDefault()
		throws Exception {

		javax.portlet.PortletPreferences initialPortletPreferences =
			PortletPreferencesLocalServiceUtil.fetchPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertNull(initialPortletPreferences);

		PortletPreferencesImpl portletPreferences = (PortletPreferencesImpl )
			getPreferences(_portlet, null);

		Assert.assertNotNull(portletPreferences.getMap());

		Assert.assertTrue(portletPreferences.getMap().isEmpty());

		javax.portlet.PortletPreferences actualPortletPreferences =
			PortletPreferencesLocalServiceUtil.fetchPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertTrue(actualPortletPreferences.getMap().isEmpty());
	}

	@Test
	public void testGetPreferencesByPortletPreferencesIds() throws Exception {
		addPortelPreferencesReturnJavaxPreferences(
			_portlet, getPreferencesAsXMLString());

		PortletPreferencesIds portletPreferencesIds =
			new PortletPreferencesIds(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		javax.portlet.PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.getPreferences(
				portletPreferencesIds);

		Assert.assertArrayEquals(
			_PREFERENCE_VALUES,
			portletPreferences.getMap().get(_PREFERENCE_NAME));
	}

	@Test
	public void testGetStrictPreferences() throws Exception {

		javax.portlet.PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.getStrictPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertTrue(
			portletPreferences instanceof StrictPortletPreferencesImpl);
	}

	@Test
	public void testGetStrictPreferencesByPortletPreferencesIds()
		throws Exception {

		PortletPreferencesIds portletPreferencesIds =
			new PortletPreferencesIds(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		javax.portlet.PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.getStrictPreferences(
				portletPreferencesIds);

		Assert.assertTrue(
			portletPreferences instanceof StrictPortletPreferencesImpl);
	}

	private Layout addLayout() throws Exception {
		return addLayout(_group);
	}

	private Layout addLayout(Group group) throws Exception {
		return LayoutTestUtil.addLayout(
			group.getGroupId(), ServiceTestUtil.randomString(), false);
	}

	private Layout[] addLayout(Group group, int numberLayouts)
		throws Exception {

		Layout[] layouts = new Layout[numberLayouts];

		for (int i = 0; i < numberLayouts; i++) {
			layouts[i] = addLayout(group);
		}

		return layouts;
	}

	private Layout[] addLayout(int numberLayouts) throws Exception {
		return addLayout(_group, numberLayouts);
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

		return addPortelsPreferences(group, portlets, null);
	}

	private PortletPreferences[] addPortelsPreferences(
			Group group, Portlet[] portlets, String preferences)
		throws Exception {

		PortletPreferences[] results = new PortletPreferences[portlets.length];

		for (int i = 0; i < results.length; i++) {
			results[i] = addPortelPreferences(
				group, null, portlets[i], portlets[i].getPortletId(),
				preferences);
		}

		return results;
	}

	private PortletPreferences[] addPortelsPreferences(
			Layout layout, Portlet[] portlets)
		throws Exception {

		PortletPreferences[] results = new PortletPreferences[portlets.length];

		for (int i = 0; i < results.length; i++) {
			results[i] = addPortelPreferences(
				null, layout, portlets[i], portlets[i].getPortletId(), null);
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

	private javax.portlet.PortletPreferences getPreferences(Portlet portlet)
		throws Exception {

		return getPreferences(portlet, null);
	}

	private javax.portlet.PortletPreferences getPreferences(
			Portlet portlet, String defaultPreferences)
		throws Exception {

		if (defaultPreferences != null) {
			return PortletPreferencesLocalServiceUtil.getPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				portlet.getPortletId(), defaultPreferences);
		}
		else {
			return PortletPreferencesLocalServiceUtil.getPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				portlet.getPortletId());
		}
	}

	private String getPreferencesAsXMLString() {
		return getPreferencesAsXMLString(_PREFERENCE_NAME, _PREFERENCE_VALUES);
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

	private String getPreferencesAsXMLString(String[] values) {

		return getPreferencesAsXMLString(_PREFERENCE_NAME, values);
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

	private static final String _PORTEL_WITH_PREFERENCES_ID = "16";

	private static final String _PREFERENCE_NAME = "name";

	private static final String[] _PREFERENCE_VALUES = {"defaultValue"};

	private Group _group;
	private Layout _layout;
	private LayoutTypePortlet _layoutTypePortlet;
	private Portlet _portlet;

}