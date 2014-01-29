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

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.model.PortletPreferencesIds;
import com.liferay.portal.service.impl.PortletPreferencesLocalServiceImpl;
import com.liferay.portal.service.persistence.PortletPreferencesPersistence;
import com.liferay.portal.service.util.PortletPreferencesTestUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalCallbackAwareExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portal.util.LayoutTestUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.PortletPreferencesImpl;
import com.liferay.portlet.StrictPortletPreferencesImpl;

import java.util.List;
import java.util.Map;

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

		_portlet = PortletLocalServiceUtil.getPortletById(
			TestPropsValues.getCompanyId(), String.valueOf(_PORTLET_ID));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddPreferencesWithDefaultInParameter() throws Exception {
		assertNullLayoutPreferences();

		String preferencesAsXml =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		PortletPreferences portletPreferences = addPortletLayoutPreferences(
			preferencesAsXml);

		assertPortletPreferencesOwnedByLayout(portletPreferences);

		assertPortletPreferenceValues(
			portletPreferences, _PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);
	}

	@Test
	public void testAddPreferencesWithDefaultInPortletObject()
		throws Exception {

		assertNullLayoutPreferences();

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		_portlet.setDefaultPreferences(preferencesAsXML);

		PortletPreferences portletPreferences = addPortletLayoutPreferences();

		assertPortletPreferencesOwnedByLayout(portletPreferences);

		assertPortletPreferenceValues(
			portletPreferences, _PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);
	}

	@Test
	public void testAddPreferencesWithMultipleValuedDefaultInParameter()
		throws Exception {

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_MULTIPLE);

		assertNullLayoutPreferences();

		PortletPreferences portletPreferences = addPortletLayoutPreferences(
			preferencesAsXML);

		assertPortletPreferencesOwnedByLayout(portletPreferences);

		assertPortletPreferenceValues(
			portletPreferences, _PREFERENCE_NAME, _PREFERENCE_VALUES_MULTIPLE);
	}

	@Test
	public void testAddPreferencesWithoutDefault() throws Exception {
		assertNullLayoutPreferences();

		PortletPreferences portletPreferences = addPortletLayoutPreferences();

		javax.portlet.PortletPreferences fetchedPortletPreferences =
			fetchLayoutPreferences();

		assertEmptyPortletPreferences(portletPreferences);

		assertEmptyPortletPreferences(fetchedPortletPreferences);
	}

	@Test
	public void testDeletePortletPreferencesByPortletPreferencesId()
		throws Exception {

		PortletPreferences portletPreferences =  addPortletLayoutPreferences();

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
	public void testDeletePreferencesByPlid() throws Exception {
		Portlet portlet2 = getTestPortlets(1)[0];

		PortletPreferences[] portletPreferences =
			addPortletLayoutWithoutDefaultPreferences(
				_layout, _portlet, portlet2);

		PortletPreferences[] currentPortletPreferences =
			fetchPortletPreferences(portletPreferences);

		Assert.assertNotNull(currentPortletPreferences[0]);

		Assert.assertNotNull(currentPortletPreferences[1]);

		PortletPreferencesLocalServiceUtil.deletePortletPreferencesByPlid(
			_layout.getPlid());

		currentPortletPreferences = fetchPortletPreferences(portletPreferences);

		Assert.assertNull(currentPortletPreferences[0]);

		Assert.assertNull(currentPortletPreferences[1]);
	}

	@Test
	public void testDeletePreferencesByPlidAndOwner() throws Exception {
		Group group2 = GroupTestUtil.addGroup();

		Portlet group2Portlet = getTestPortlets(1)[0];

		PortletPreferences portletPreferencesGroup1 =
			addPortletGroupWithoutDefaultPreferences(_group, _portlet)[0];

		PortletPreferences portletPreferencesGroup2 =
			addPortletGroupWithoutDefaultPreferences(group2, group2Portlet)[0];

		PortletPreferences[] currentPortletPreferences =
			fetchPortletPreferences(
				portletPreferencesGroup1, portletPreferencesGroup2);

		Assert.assertNotNull(currentPortletPreferences[0]);

		Assert.assertNotNull(currentPortletPreferences[1]);

		PortletPreferencesLocalServiceUtil.deletePortletPreferences(
			_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
			_layout.getPlid());

		currentPortletPreferences = fetchPortletPreferences(
			portletPreferencesGroup1, portletPreferencesGroup2);

		Assert.assertNull(currentPortletPreferences[0]);

		Assert.assertNotNull(currentPortletPreferences[1]);
	}

	@Test
	public void testDeletePreferencesByPortletId() throws Exception {
		Portlet portlet2 = getTestPortlets(1)[0];

		PortletPreferences[] portletPreferences =
			addPortletLayoutWithoutDefaultPreferences(
				_layout, _portlet, portlet2);

		PortletPreferences[] actualPortletPreferences = fetchPortletPreferences(
			portletPreferences);

		Assert.assertNotNull(actualPortletPreferences[0]);

		Assert.assertNotNull(actualPortletPreferences[1]);

		PortletPreferencesLocalServiceUtil.deletePortletPreferences(
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
			_portlet.getPortletId());

		actualPortletPreferences = fetchPortletPreferences(portletPreferences);

		Assert.assertNull(actualPortletPreferences[0]);

		Assert.assertNotNull(actualPortletPreferences[1]);
	}

	@Test
	public void testFetchPreferences() throws Exception {
		assertNullLayoutPreferences();

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		addPortletPreferencesReturnJavaxPreferences(_portlet, preferencesAsXML);

		javax.portlet.PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.fetchPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		assertPortletPreferenceValues(
			portletPreferences, _PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);
	}

	@Test
	public void testFetchPreferencesByPortletPreferencesIds() throws Exception {
		assertNullLayoutPreferences();

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		addPortletPreferencesReturnJavaxPreferences(_portlet, preferencesAsXML);

		PortletPreferencesIds portletPreferencesIds =
			new PortletPreferencesIds(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		javax.portlet.PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.fetchPreferences(
				portletPreferencesIds);

		assertPortletPreferenceValues(
			(PortletPreferencesImpl)portletPreferences, _PREFERENCE_NAME,
			_PREFERENCE_VALUES_SINGLE);
	}

	@Test
	public void testFetchPreferencesNull() throws Exception {
		assertNullLayoutPreferences();

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		addPortletPreferencesReturnJavaxPreferences(_portlet, preferencesAsXML);

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

		addPortletLayoutPreferences();

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

		addPortletLayoutWithoutDefaultPreferences(layouts[0], _portlet);

		addPortletLayoutWithoutDefaultPreferences(layouts[1], _portlet);

		List<PortletPreferences> portletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				layouts[0].getCompanyId(), layouts[0].getGroupId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _portlet.getPortletId(),
				false);

		Assert.assertEquals(1, portletPreferences.size());

		Assert.assertEquals(
			portletPreferences.get(0).getPlid(), layouts[0].getPlid());
	}

	@Test
	public void testGetPortletPreferencesByCompanyOwnerPortletRepeatedGroup()
		throws Exception {

		Layout[] layouts = addLayout(_group, 2);

		PortletPreferences portalPreferencesLayout =
			addPortletLayoutWithoutDefaultPreferences(layouts[0], _portlet)[0];

		addPortletLayoutWithoutDefaultPreferences(layouts[1], _portlet);

		List<PortletPreferences> listPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				layouts[0].getCompanyId(), _group.getGroupId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _portlet.getPortletId(),
				false);

		Assert.assertEquals(2, listPortletPreferences.size());

		for (PortletPreferences portletPreferences : listPortletPreferences) {
			if (portalPreferencesLayout.getPortletPreferencesId() ==
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
			addPortletGroupWithoutDefaultPreferences(_group, portlets),
			addPortletGroupWithoutDefaultPreferences(group2, portlets[0]));

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
			addPortletGroupWithoutDefaultPreferences(_group, portlets),
			addPortletGroupWithoutDefaultPreferences(group2, portlets[0]));

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

		addPortletLayoutWithoutDefaultPreferences(layouts[0], portlets);

		addPortletLayoutWithoutDefaultPreferences(layouts[1], portlets);

		List<PortletPreferences> listPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesByPlid(
				layouts[0].getPlid());

		Assert.assertEquals(2, listPortletPreferences.size());
	}

	@Test
	public void testGetPortletPreferencesByPortletLayout() throws Exception {
		Layout[] layouts = addLayout(2);

		Portlet[] portlets = getTestPortlets(2);

		addPortletLayoutWithoutDefaultPreferences(layouts[0], portlets);

		addPortletLayoutWithoutDefaultPreferences(layouts[1], portlets);

		List<PortletPreferences> listPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				layouts[0].getPlid(), portlets[0].getPortletId());

		Assert.assertEquals(1, listPortletPreferences.size());
	}

	@Test
	public void testGetPortletPreferencesByPortletOwnerTypeLayout()
		throws Exception {

		Group group2 = GroupTestUtil.addGroup();

		PortletPreferences[] portletPreferences = new PortletPreferences[2];
		portletPreferences[0] = addPortletLayoutPreferences();
		portletPreferences[1] = addPortletGroupWithoutDefaultPreferences(
			group2, _portlet)[0];

		PortletPreferences[] actualPortletPreferences = fetchPortletPreferences(
			portletPreferences);

		Assert.assertNotNull(actualPortletPreferences[0]);

		Assert.assertNotNull(actualPortletPreferences[1]);

		List<PortletPreferences> portletPreferencesList =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(1, portletPreferencesList.size());

		portletPreferencesList =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				PortletKeys.PREFS_OWNER_TYPE_GROUP, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(1, portletPreferencesList.size());

		portletPreferencesList =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				PortletKeys.PREFS_OWNER_TYPE_COMPANY, _layout.getPlid(),
				_portlet.getPortletId());

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

		addPortletGroupWithoutDefaultPreferences(_group, portlets);
		addPortletGroupWithoutDefaultPreferences(group2, portlets[0]);

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

		addPortletGroupWithoutDefaultPreferences(_group, portlets);

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

		addPortletGroupWithoutDefaultPreferences(_group, portlets);
		addPortletGroupWithoutDefaultPreferences(group2, portlets[0]);

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

		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_portlet.getPortletId(), true);

		Assert.assertEquals(0, initialCount);

		addPortletGroupWithoutDefaultPreferences(_group, _portlet);

		long actualCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_portlet.getPortletId(), true);

		Assert.assertEquals(0, actualCount);
	}

	@Test
	public void testGetPortletPreferencesCountByOwnerPortletNotExcludeDefault()
		throws Exception {

		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid(), _portlet, false);

		Assert.assertEquals(0, initialCount);

		addPortletGroupWithoutDefaultPreferences(_group, _portlet);

		Assert.assertEquals(0, initialCount);

		long actualCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_portlet.getPortletId(), false);

		Assert.assertEquals(1, actualCount);
	}

	@Test
	public void testGetPortletPreferencesCountByPortletOwnerType()
		throws Exception {

		Layout layout2 = addLayout();

		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _portlet.getPortletId());

		addPortletLayoutPreferences();
		addPortletLayoutWithoutDefaultPreferences(layout2, _portlet);
		addPortletGroupWithoutDefaultPreferences(_group, _portlet);

		long actualCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _portlet.getPortletId());

		Assert.assertEquals(initialCount + 2, actualCount);
	}

	@Test
	public void testGetPortletPreferencesCountyPortletOwnerTypeLayout()
		throws Exception {

		Group group2 = GroupTestUtil.addGroup();

		long initialCountTypeLayout =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(0, initialCountTypeLayout);

		long initialCountTypeGroup =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_GROUP, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(0, initialCountTypeGroup);

		long initialCountTypeCompany =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_COMPANY, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(0, initialCountTypeCompany);

		addPortletLayoutPreferences();
		addPortletGroupWithoutDefaultPreferences(group2, _portlet);

		long actualCountTypeLayout =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(1, actualCountTypeLayout);

		long actualCountTypeGroup =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_GROUP, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(1, actualCountTypeGroup);

		long actualCountTypeCompany =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_COMPANY, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(0, actualCountTypeCompany);
	}

	@Test
	public void testGetPreferencesByCompanyOwnerLayoutPortletAdded()
		throws Exception {

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		javax.portlet.PortletPreferences portletPreferences =
			addPortletPreferencesReturnJavaxPreferences(
				_portlet, preferencesAsXML);

		javax.portlet.PortletPreferences actualPortelPreferences =
			getPreferences(_portlet);

		Assert.assertArrayEquals(
			portletPreferences.getMap().get(_PREFERENCE_NAME),
			actualPortelPreferences.getMap().get(_PREFERENCE_NAME));
	}

	@Test
	public void testGetPreferencesByCompanyOwnerLayoutPortletNotAdded()
		throws Exception {

		assertNullLayoutPreferences();

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		PortletPreferencesImpl portletPreferences = (PortletPreferencesImpl )
			getPreferences(_portlet, preferencesAsXML);

		assertPortletPreferenceValues(
			portletPreferences, _PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);
	}

	@Test
	public void testGetPreferencesByCompanyOwnerLayoutPortletNotAddedDefault()
		throws Exception {

		assertNullLayoutPreferences();

		PortletPreferencesImpl portletPreferences = (PortletPreferencesImpl )
			getPreferences(_portlet, null);

		assertEmptyPortletPreferences(portletPreferences);
	}

	@Test
	public void testGetPreferencesByPortletPreferencesIds() throws Exception {

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		addPortletPreferencesReturnJavaxPreferences(_portlet, preferencesAsXML);

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
			_PREFERENCE_VALUES_SINGLE,
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

		assertStrictPortletPreferences(portletPreferences);
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

		assertStrictPortletPreferences(portletPreferences);
	}

	@Test
	public void testGetStrictPreferencesNotDefault() throws Exception {
		MockPortletPreferencesLocalServiceImpl mockservice = new
			MockPortletPreferencesLocalServiceImpl();

		mockservice.setPortletPreferencesPersistence(
			(PortletPreferencesPersistence)PortalBeanLocatorUtil.locate(
				PortletPreferencesPersistence.class.getName()));

		mockservice.setPortletLocalService(
			(PortletLocalService)PortalBeanLocatorUtil.locate(
				PortletLocalService.class.getName()));

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		javax.portlet.PortletPreferences portletPreferences =
			mockservice.getStrictPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId(), preferencesAsXML);

		assertStrictPortletPreferences(portletPreferences);
	}

	@Test
	public void testUpdatePreferencesMultipleValues() throws Exception {
		assertNullLayoutPreferences();

		addPortletLayoutPreferences();

		String[] values = {"value4", "value5", "value6"};

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, values);

		PortletPreferencesLocalServiceUtil.updatePreferences(
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
			_portlet.getPortletId(), preferencesAsXML);

		javax.portlet.PortletPreferences preferences =
			PortletPreferencesLocalServiceUtil.getPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		String[] actualValues = preferences.getMap().get(_PREFERENCE_NAME);

		Assert.assertEquals(values.length, actualValues.length);

		for (int i = 0; i < values.length; i++) {
			Assert.assertEquals(values[i], actualValues[i]);
		}
	}

	@Test
	public void testUpdatePreferencesMultipleValuesByPortletPreferencesImpl()
		throws Exception {

		assertNullLayoutPreferences();

		addPortletLayoutPreferences();

		String[] values = {"value4", "value5", "value6"};

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, values);

		PortletPreferencesImpl portletPreferencesImpl =
			(PortletPreferencesImpl)PortletPreferencesFactoryUtil.fromXML(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId(), preferencesAsXML);

		PortletPreferencesLocalServiceUtil.updatePreferences(
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
			_portlet.getPortletId(), portletPreferencesImpl);

		javax.portlet.PortletPreferences preferences =
			PortletPreferencesLocalServiceUtil.getPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		String[] actualValues = preferences.getMap().get(_PREFERENCE_NAME);

		Assert.assertEquals(values.length, actualValues.length);

		for (int i = 0; i < values.length; i++) {
			Assert.assertEquals(values[i], actualValues[i]);
		}
	}

	@Test
	public void testUpdatePreferencesMultipleValuesNoPreviouslyAdded()
		throws Exception {

		String[] values = {"value4", "value5", "value6"};

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, values);

		assertNullLayoutPreferences();

		PortletPreferencesLocalServiceUtil.updatePreferences(
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
			_portlet.getPortletId(), preferencesAsXML);

		javax.portlet.PortletPreferences preferences =
			PortletPreferencesLocalServiceUtil.getPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		String[] actualValues = preferences.getMap().get(_PREFERENCE_NAME);

		Assert.assertEquals(values.length, actualValues.length);

		for (int i = 0; i < values.length; i++) {
			Assert.assertEquals(values[i], actualValues[i]);
		}
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

	private PortletPreferences addPortelPreferences(Portlet portlet)
		throws Exception {

		return addPortletPreferences(
			null, null, portlet, portlet.getPortletId(), null);
	}

	private PortletPreferences[] addPortletGroupWithoutDefaultPreferences(
			Group group, Portlet... portlet)
		throws Exception {

		return addPortletsGroupPreferences(group, null, portlet);
	}

	private PortletPreferences addPortletLayoutPreferences() throws Exception {
		return addPortletLayoutPreferences(_layout, _portlet, null);
	}

	private PortletPreferences addPortletLayoutPreferences(
			Layout layout, Portlet portlet, String defaultPreferences)
		throws Exception {

		return PortletPreferencesLocalServiceUtil.addPortletPreferences(
			TestPropsValues.getCompanyId(), PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, layout.getPlid(),
			portlet.getPortletId(), portlet, defaultPreferences);
	}

	private PortletPreferences[] addPortletLayoutPreferences(
			Layout layout, String defaultPreferences, Portlet ... portlets)
		throws Exception {

		PortletPreferences[] results = new PortletPreferences[portlets.length];

		for (int i = 0; i < results.length; i++) {
			results[i] =  addPortletLayoutPreferences(
				layout, portlets[i], defaultPreferences);
		}

		return results;
	}

	private PortletPreferences addPortletLayoutPreferences(
			String defaultPreferences)
		throws Exception {

		return addPortletLayoutPreferences(
			_layout, _portlet, defaultPreferences);
	}

	private PortletPreferences[] addPortletLayoutWithoutDefaultPreferences(
			Layout layout, Portlet... portlets)
		throws Exception {

		return addPortletLayoutPreferences(layout, null, portlets);
	}

	private PortletPreferences addPortletPreferences(
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

	private javax.portlet.PortletPreferences
		addPortletPreferencesReturnJavaxPreferences(
			Portlet portlet, String defaultPreferences)
		throws Exception {

		PortletPreferences portletPreferences =
			addPortletPreferences(
				null, _layout, portlet, portlet.getPortletId(),
				defaultPreferences);

		return PortletPreferencesTestUtil.convert(portletPreferences);
	}

	private PortletPreferences[] addPortletsGroupPreferences(
			Group group, String defaultPreferences, Portlet ... portlets)
		throws Exception {

		PortletPreferences[] results = new PortletPreferences[portlets.length];

		for (int i = 0; i < results.length; i++) {
			results[i] =
				PortletPreferencesLocalServiceUtil.addPortletPreferences(
					TestPropsValues.getCompanyId(), group.getGroupId(),
					PortletKeys.PREFS_OWNER_TYPE_GROUP, _layout.getPlid(),
					portlets[i].getPortletId(), portlets[i],
					defaultPreferences);
		}

		return results;
	}

	private PortletPreferences[] addPortletsPreferences(Portlet[] portlets)
		throws Exception {

		PortletPreferences[] results = new PortletPreferences[portlets.length];

		for (int i = 0; i < results.length; i++) {
			results[i] = addPortelPreferences(portlets[i]);
		}

		return results;
	}

	private void assertEmptyPortletPreferences(
			javax.portlet.PortletPreferences portletPreferences)
		throws Exception {

		assertPortletPreferencesOwnedByLayout(
			(PortletPreferencesImpl)portletPreferences);

		PortletPreferencesImpl portletPreferencesImpl =
			(PortletPreferencesImpl)portletPreferences;

		Assert.assertTrue(portletPreferencesImpl.getMap().isEmpty());
	}

	private void assertEmptyPortletPreferences(
			PortletPreferences portletPreferences)
		throws Exception {

		assertPortletPreferencesOwnedByLayout(portletPreferences);

		PortletPreferencesImpl portletPreferencesImpl =
			PortletPreferencesTestUtil.convert(portletPreferences);

		Assert.assertTrue(portletPreferencesImpl.getMap().isEmpty());
	}

	private void assertNullLayoutPreferences() throws Exception {
		javax.portlet.PortletPreferences preferences = fetchLayoutPreferences();
		Assert.assertNull(preferences);
	}

	private void assertPortletPreferencesOwnedByLayout(
		PortletPreferences portletPreferences) {

		Assert.assertEquals(_layout.getPlid(), portletPreferences.getPlid());

		Assert.assertEquals(
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
			portletPreferences.getOwnerType());

		Assert.assertEquals(
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			portletPreferences.getOwnerId());
	}

	private void assertPortletPreferencesOwnedByLayout(
		PortletPreferencesImpl portletPreferences) {

		Assert.assertEquals(_layout.getPlid(), portletPreferences.getPlid());

		Assert.assertEquals(
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
			portletPreferences.getOwnerType());

		Assert.assertEquals(
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			portletPreferences.getOwnerId());
	}

	private void assertPortletPreferenceValues(
			javax.portlet.PortletPreferences portletPreferences,
			String preferenceName, String[] preferenceValues)
		throws Exception {

		PortletPreferencesImpl portletPreferencesImpl =
			(PortletPreferencesImpl)portletPreferences;

		Map<String, String[]> portletPreferencesMap =
			portletPreferencesImpl.getMap();

		Assert.assertFalse(portletPreferencesMap.isEmpty());

		Assert.assertArrayEquals(
			preferenceValues, portletPreferencesMap.get(preferenceName));
	}

	private void assertPortletPreferenceValues(
			PortletPreferences portletPreferences, String preferenceName,
			String[] preferenceValues)
		throws Exception {

		PortletPreferencesImpl portletPreferencesImpl =
			PortletPreferencesTestUtil.convert(portletPreferences);

		assertPortletPreferenceValues(
			portletPreferencesImpl, preferenceName, preferenceValues);
	}

	private void assertStrictPortletPreferences(
		javax.portlet.PortletPreferences portletPreferences) {

		Assert.assertTrue(
			portletPreferences instanceof StrictPortletPreferencesImpl);

		StrictPortletPreferencesImpl strictPortletPreferences =
			(StrictPortletPreferencesImpl)portletPreferences;

		Assert.assertEquals(0, strictPortletPreferences.getPlid());
		Assert.assertEquals(0, strictPortletPreferences.getOwnerType());
		Assert.assertEquals(0, strictPortletPreferences.getOwnerId());
		Assert.assertTrue(strictPortletPreferences.getMap().isEmpty());
	}

	private javax.portlet.PortletPreferences fetchLayoutPreferences()
		throws Exception {

		return PortletPreferencesLocalServiceUtil.fetchPreferences(
			TestPropsValues.getCompanyId(), PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
			_portlet.getPortletId());
	}

	private PortletPreferences[] fetchPortletPreferences(
			PortletPreferences... portletPreferenceses)
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

	private Portlet[] getTestPortlets(int numberPortlets) throws Exception {
		Portlet[] results = new Portlet[numberPortlets];

		for (int i = 0; i < results.length; i++) {
			results[i] = PortletLocalServiceUtil.getPortletById(
				TestPropsValues.getCompanyId(),
				String.valueOf(_PORTLET_ID + 1 + i));
		}

		return results;
	}

	private static final String _PORTEL_WITH_PREFERENCES_ID = "16";

	private static final int _PORTLET_ID = 1000;

	private static final String _PREFERENCE_NAME = "testPreferenceName";

	private static final String[] _PREFERENCE_VALUES_MULTIPLE =
		{"testPreferenceValue1", "testPreferenceValue2"};

	private static final String[] _PREFERENCE_VALUES_SINGLE =
		{"testPreferenceValue"};

	private Group _group;
	private Layout _layout;
	private Portlet _portlet;

	private class MockPortletPreferencesLocalServiceImpl
		extends PortletPreferencesLocalServiceImpl {

		public javax.portlet.PortletPreferences getStrictPreferences(
				long companyId, long ownerId, int ownerType, long plid,
				String portletId, String defaultPreferences)
			throws SystemException {

			return getPreferences(
				companyId, ownerId, ownerType, plid, portletId,
				defaultPreferences, !PropsValues.TCK_URL);
		}
	}

}