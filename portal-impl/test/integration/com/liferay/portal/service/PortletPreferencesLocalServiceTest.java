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

		_layout = LayoutTestUtil.addLayout(_group);

		_portlet = PortletLocalServiceUtil.getPortletById(
			TestPropsValues.getCompanyId(), String.valueOf(_PORTLET_ID));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddPreferencesWithDefaultInParameter() throws Exception {
		PortletPreferencesTestUtil.assertNullLayoutPreferences(
			_layout, _portlet);

		String preferencesAsXml =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		PortletPreferences portletPreferences =
			PortletPreferencesTestUtil.addPortletLayoutPreferences(
				_layout, preferencesAsXml, _portlet)[0];

		PortletPreferencesImpl portletPreferencesImpl =
			PortletPreferencesTestUtil.convert(portletPreferences);

		PortletPreferencesTestUtil.assertPortletPreferencesOwnedByLayout(
			_layout, portletPreferencesImpl);

		PortletPreferencesTestUtil.assertPortletPreferenceValues(
			portletPreferences, _PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);
	}

	@Test
	public void testAddPreferencesWithDefaultInPortletObject()
		throws Exception {

		PortletPreferencesTestUtil.assertNullLayoutPreferences(
			_layout, _portlet);

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		_portlet.setDefaultPreferences(preferencesAsXML);

		PortletPreferences portletPreferences =
			PortletPreferencesTestUtil.
				addPortletLayoutWithoutDefaultPreferences(_layout, _portlet)[0];

		PortletPreferencesImpl portletPreferencesImpl =
			PortletPreferencesTestUtil.convert(portletPreferences);

		PortletPreferencesTestUtil.assertPortletPreferencesOwnedByLayout(
			_layout, portletPreferencesImpl);

		PortletPreferencesTestUtil.assertPortletPreferenceValues(
			portletPreferences, _PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);
	}

	@Test
	public void testAddPreferencesWithMultipleValuedDefaultInParameter()
		throws Exception {

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_MULTIPLE);

		PortletPreferencesTestUtil.assertNullLayoutPreferences(
			_layout, _portlet);

		PortletPreferences portletPreferences =
			PortletPreferencesTestUtil.addPortletLayoutPreferences(
				_layout, preferencesAsXML, _portlet)[0];

		PortletPreferencesImpl portletPreferencesImpl =
			PortletPreferencesTestUtil.convert(portletPreferences);

		PortletPreferencesTestUtil.assertPortletPreferencesOwnedByLayout(
			_layout, portletPreferencesImpl);

		PortletPreferencesTestUtil.assertPortletPreferenceValues(
			portletPreferences, _PREFERENCE_NAME, _PREFERENCE_VALUES_MULTIPLE);
	}

	@Test
	public void testAddPreferencesWithoutDefault() throws Exception {
		PortletPreferencesTestUtil.assertNullLayoutPreferences(
			_layout, _portlet);

		PortletPreferences portletPreferences =
			PortletPreferencesTestUtil.
				addPortletLayoutWithoutDefaultPreferences(_layout, _portlet)[0];

		PortletPreferencesImpl portletPreferencesImpl =
			PortletPreferencesTestUtil.convert(portletPreferences);

		javax.portlet.PortletPreferences fetchedPortletPreferences =
			PortletPreferencesTestUtil.fetchLayoutPreferences(
				_layout, _portlet);

		PortletPreferencesTestUtil.assertEmptyPortletPreferences(
			_layout, portletPreferencesImpl);

		PortletPreferencesTestUtil.assertEmptyPortletPreferences(
			_layout, fetchedPortletPreferences);
	}

	@Test
	public void testDeletePreferencesByPlid() throws Exception {
		Portlet portlet2 = getTestPortlets(1)[0];

		PortletPreferences[] portletPreferences =
			PortletPreferencesTestUtil.
				addPortletLayoutWithoutDefaultPreferences(
					_layout, _portlet, portlet2);

		PortletPreferences[] currentPortletPreferences =
			PortletPreferencesTestUtil.fetchPortletPreferences(
				portletPreferences);

		Assert.assertNotNull(
			"After added a porlet preferences for the portlet: " +
			_portlet.getPortletId() + " these should not be null",
			currentPortletPreferences[0]);

		Assert.assertNotNull(
			"After added porlet preferences for the portlet: " +
			portlet2.getPortletId() + " these should not be null",
			currentPortletPreferences[1]);

		PortletPreferencesLocalServiceUtil.deletePortletPreferencesByPlid(
			_layout.getPlid());

		currentPortletPreferences =
			PortletPreferencesTestUtil.fetchPortletPreferences(
				portletPreferences);

		Assert.assertNull(
			"After delete the porlet preferences for the portlet: " +
			_portlet.getPortletId() + " these should be null",
			currentPortletPreferences[0]);

		Assert.assertNull(
			"After delete the porlet preferences for the portlet: " +
				portlet2.getPortletId() + " these should be null",
			currentPortletPreferences[1]);
	}

	@Test
	public void testDeletePreferencesByPlidAndOwner() throws Exception {
		Group group2 = GroupTestUtil.addGroup();

		Portlet group2Portlet = getTestPortlets(1)[0];

		PortletPreferences portletPreferencesGroup1 =
			PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
				_group, _layout, _portlet)[0];

		PortletPreferences portletPreferencesGroup2 =
			PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
				group2, _layout, group2Portlet)[0];

		PortletPreferences[] currentPortletPreferences =
			PortletPreferencesTestUtil.fetchPortletPreferences(
				portletPreferencesGroup1, portletPreferencesGroup2);

		Assert.assertNotNull(
			"After added porlet preferences for the portlet: " +
			_portlet.getPortletId() + " these should not be null",
			currentPortletPreferences[0]);

		Assert.assertNotNull(
			"After added porlet preferences for the portlet: " +
			group2Portlet.getPortletId() + " these should not be null",
			currentPortletPreferences[1]);

		PortletPreferencesLocalServiceUtil.deletePortletPreferences(
			_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
			_layout.getPlid());

		currentPortletPreferences =
			PortletPreferencesTestUtil.fetchPortletPreferences(
				portletPreferencesGroup1, portletPreferencesGroup2);

		Assert.assertNull(
			"After delete the porlet preferences owned by the group: " +
				_group.getGroupId() + " the preferences for the portlet " +
				_portlet.getPortletId() + " and the group " +
				_group.getGroupId() + " should not be defined ",
			currentPortletPreferences[0]);

		Assert.assertNotNull(
			"After delete the porlet preferences owned by the group: " +
				_group.getGroupId() + " the preferences for the portlet " +
				group2Portlet.getPortletId() +
				" should still be defined",
			currentPortletPreferences[1]);
	}

	@Test
	public void testDeletePreferencesByPortletId() throws Exception {
		Portlet portlet2 = getTestPortlets(1)[0];

		PortletPreferences[] portletPreferences =
			PortletPreferencesTestUtil.
				addPortletLayoutWithoutDefaultPreferences(
					_layout, _portlet, portlet2);

		PortletPreferences[] currentPortletPreferences =
			PortletPreferencesTestUtil.fetchPortletPreferences(
				portletPreferences);

		Assert.assertNotNull(
			"It has not been posible to fetch the portletPreferencesId " +
				portletPreferences[0].getPortletPreferencesId(),
			currentPortletPreferences[0]);

		Assert.assertNotNull(
			"It has not been posible to fetch the portletPreferencesId " +
				portletPreferences[1].getPortletPreferencesId(),
			currentPortletPreferences[1]);

		PortletPreferencesLocalServiceUtil.deletePortletPreferences(
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
			_portlet.getPortletId());

		currentPortletPreferences =
			PortletPreferencesTestUtil.fetchPortletPreferences(
				portletPreferences);

		Assert.assertNull(
			"It has been posible to fetch the portletPreferencesId " +
				portletPreferences[0].getPortletPreferencesId() +
				" after delete it",
			currentPortletPreferences[0]);

		Assert.assertNotNull(
			"It has not been posible to fetch the portletPreferencesId " +
				portletPreferences[1].getPortletPreferencesId(),
			currentPortletPreferences[1]);
	}

	@Test
	public void testDeletePreferencesByPortletPreferencesId() throws Exception {
		PortletPreferences portletPreferences =
			PortletPreferencesTestUtil.
				addPortletLayoutWithoutDefaultPreferences(_layout, _portlet)[0];

		PortletPreferences portletPreferencesFetched =
			PortletPreferencesLocalServiceUtil.fetchPortletPreferences(
				portletPreferences.getPortletPreferencesId());

		Assert.assertNotNull(
			"It has not been posible to fetch the portletPreferencesId " +
				portletPreferences.getPortletPreferencesId(),
			portletPreferencesFetched);

		PortletPreferencesLocalServiceUtil.deletePortletPreferences(
			portletPreferences.getPortletPreferencesId());

		portletPreferencesFetched =
			PortletPreferencesLocalServiceUtil.fetchPortletPreferences(
				portletPreferences.getPortletPreferencesId());

		Assert.assertNull(
			"It has been posible to fetch the portletPreferencesId " +
				portletPreferences.getPortletPreferencesId() +
				" after delete it",
			portletPreferencesFetched);
	}

	@Test
	public void testFetchPreferences() throws Exception {
		PortletPreferencesTestUtil.assertNullLayoutPreferences(
			_layout, _portlet);

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		PortletPreferencesTestUtil.addPortletLayoutPreferences(
			_layout, preferencesAsXML, _portlet);

		javax.portlet.PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.fetchPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		PortletPreferencesTestUtil.assertPortletPreferenceValues(
			portletPreferences, _PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);
	}

	@Test
	public void testFetchPreferencesByPortletPreferencesIds() throws Exception {
		PortletPreferencesTestUtil.assertNullLayoutPreferences(
			_layout, _portlet);

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		PortletPreferencesTestUtil.addPortletLayoutPreferences(
			_layout, preferencesAsXML, _portlet);

		PortletPreferencesIds portletPreferencesIds =
			new PortletPreferencesIds(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		javax.portlet.PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.fetchPreferences(
				portletPreferencesIds);

		PortletPreferencesTestUtil.assertPortletPreferenceValues(
			portletPreferences, _PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);
	}

	@Test
	public void testFetchPreferencesNull() throws Exception {
		PortletPreferencesTestUtil.assertNullLayoutPreferences(
			_layout, _portlet);

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		PortletPreferencesTestUtil.addPortletLayoutPreferences(
			_layout, preferencesAsXML, _portlet);

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

		Assert.assertNull(
			"The portlet preferences for (" + " companyId: " +
				TestPropsValues.getCompanyId() + " ownerId: " +
				PortletKeys.PREFS_OWNER_ID_DEFAULT + " ownerType: " +
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT + " PLID: " +
				_layout.getPlid() + " portletId: " + _portlet.getPortletId() +
				") should be null after delete it ", portletPreferences);
	}

	@Test
	public void testGetDefaultPreferences() throws Exception {
		javax.portlet.PortletPreferences defaultPreferences =
			PortletPreferencesLocalServiceUtil.getDefaultPreferences(
				TestPropsValues.getCompanyId(), _PORTEL_WITH_PREFERENCES_ID);

		Assert.assertFalse(
			"The default preferences for the portlet " +
				_PORTEL_WITH_PREFERENCES_ID + " should be defined",
			defaultPreferences.getMap().isEmpty());

		String[] symbols = {"GBP", "CNY", "EUR", "JPY", "USD"};

		Assert.assertArrayEquals(
			"The default value for the preference 'symbols' of the portlet " +
				_PORTEL_WITH_PREFERENCES_ID + "should be: " + symbols,
			symbols, defaultPreferences.getMap().get("symbols"));
	}

	@Test
	public void testGetPreferencesAll() throws Exception {
		List<PortletPreferences> initialPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences();

		int initialCount = initialPortletPreferences.size();

		PortletPreferencesTestUtil.addPortletLayoutWithoutDefaultPreferences(
			_layout, _portlet);

		List<PortletPreferences> currentPortletsPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences();

		Assert.assertEquals(
			"After add a new portlet preference, the total number "+
				"of portlet preferences should be increase in 1",
			initialCount + 1, currentPortletsPreferences.size());
	}

	@Test
	public void testGetPreferencesByCompanyGroupOwnerPortlet()
		throws Exception {

		Layout layout2 = LayoutTestUtil.addLayout(GroupTestUtil.addGroup());

		PortletPreferencesTestUtil.addPortletLayoutWithoutDefaultPreferences(
			_layout, _portlet);

		PortletPreferencesTestUtil.addPortletLayoutWithoutDefaultPreferences(
			layout2, _portlet);

		List<PortletPreferences> portletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				_layout.getCompanyId(), _layout.getGroupId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _portlet.getPortletId(),
				false);

		Assert.assertEquals(
			"The number of portlet preferences fetched by (" +
				" companyId " + _layout.getCompanyId() +
				" groupId " + _layout.getGroupId() +
				" ownerId " + PortletKeys.PREFS_OWNER_ID_DEFAULT +
				" ownerType " + PortletKeys.PREFS_OWNER_TYPE_LAYOUT +
				" portletId " + _portlet.getPortletId() + ") should be 1",
			1, portletPreferences.size());

		Assert.assertEquals(
			"The portlet preferences fetched by (" +
				" companyId " + _layout.getCompanyId() +
				" groupId " + _layout.getGroupId() +
				" ownerId " + PortletKeys.PREFS_OWNER_ID_DEFAULT +
				" ownerType " + PortletKeys.PREFS_OWNER_TYPE_LAYOUT +
				" portletId " + _portlet.getPortletId() +
				") should have the PLID " + _layout.getPlid(),
			_layout.getPlid(), portletPreferences.get(0).getPlid());
	}

	@Test
	public void testGetPreferencesByCompanyOwnerLayoutPortletAdded()
		throws Exception {

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		PortletPreferences portletPreferences =
			PortletPreferencesTestUtil.addPortletLayoutPreferences(
				_layout, preferencesAsXML, _portlet)[0];

		javax.portlet.PortletPreferences initialPortletPreferences =
			PortletPreferencesTestUtil.convert(portletPreferences);

		PortletPreferencesImpl currentPortletPreferences =
			(PortletPreferencesImpl)
				PortletPreferencesLocalServiceUtil.getPreferences(
					TestPropsValues.getCompanyId(),
					PortletKeys.PREFS_OWNER_ID_DEFAULT,
					PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
					_portlet.getPortletId(), preferencesAsXML);

		Assert.assertArrayEquals(
			"The values for the preference " + _PREFERENCE_NAME +
				" should be " + _PREFERENCE_VALUES_SINGLE,
			initialPortletPreferences.getMap().get(_PREFERENCE_NAME),
			currentPortletPreferences.getMap().get(_PREFERENCE_NAME));

		PortletPreferencesTestUtil.assertPortletPreferencesOwnedByLayout(
			_layout, currentPortletPreferences);
	}

	@Test
	public void testGetPreferencesByCompanyOwnerPortletRepeatedGroup()
		throws Exception {

		Layout[] layouts = LayoutTestUtil.addLayout(_group, 2);

		PortletPreferences portalPreferencesLayout =
			PortletPreferencesTestUtil.
				addPortletLayoutWithoutDefaultPreferences(
					layouts[0], _portlet)[0];

		PortletPreferencesTestUtil.addPortletLayoutWithoutDefaultPreferences(
			layouts[1], _portlet);

		List<PortletPreferences> listPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				layouts[0].getCompanyId(), _group.getGroupId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _portlet.getPortletId(),
				false);

		Assert.assertEquals(
			"2 portlet preferences shoud be returned by the search (" +
				" companyId " + layouts[0].getCompanyId() +
				" groupId " + _group.getGroupId() +
				" ownerId "+ PortletKeys.PREFS_OWNER_ID_DEFAULT +
				" ownerType " + PortletKeys.PREFS_OWNER_TYPE_LAYOUT +
				" portletId "+_portlet.getPortletId() + ") ",
			2, listPortletPreferences.size());

		for (PortletPreferences portletPreferences : listPortletPreferences) {
			if (portalPreferencesLayout.getPortletPreferencesId() ==
					portletPreferences.getPortletPreferencesId()) {
						Assert.assertEquals(
							"The PLID of the portlet preferences " +
								portletPreferences.getPortletPreferencesId() +
								" should be " + layouts[0].getPlid(),
							portletPreferences.getPlid(), layouts[0].getPlid());
			}
			else {
				Assert.assertEquals(
					"The PLID of the portlet preferences " +
						portletPreferences.getPortletPreferencesId() +
						" should be " + layouts[1].getPlid(),
					portletPreferences.getPlid(), layouts[1].getPlid());
			}
		}
	}

	@Test
	public void testGetPreferencesByOwnerAndLayout() throws Exception {
		Group group2 = GroupTestUtil.addGroup();

		Portlet[] portletsGroup2 = getTestPortlets(2);

		PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
			_group, _layout, _portlet);

		PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
			group2, _layout, portletsGroup2);

		List<PortletPreferences> portletPreferencesList =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid());

		Assert.assertEquals(
			"The portlet preferences for the group " + _group.getGroupId() +
				" should be 1", 1, portletPreferencesList.size());

		portletPreferencesList =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				group2.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid());

		Assert.assertEquals(
			"The portlet preferences for the group " + group2.getGroupId() +
				" should be 2", 2, portletPreferencesList.size());
	}

	@Test
	public void testGetPreferencesByOwnerLayoutPortlet() throws Exception {
		Group group2 = GroupTestUtil.addGroup();

		Portlet[] portletsGroup2 = getTestPortlets(2);

		PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
			_group, _layout, _portlet);

		PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
			group2, _layout, portletsGroup2);

		PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid(), _portlet.getPortletId());

		Assert.assertEquals(
			"The portletPreferences returned do not have the expected " +
				"portletid" + portletPreferences.getPortletId(),
			portletPreferences.getPortletId(), _portlet.getPortletId());

		PortletPreferencesTestUtil.assertPortletPreferencesOwnedByGroup(
			_layout, _group, portletPreferences);
	}

	@Test
	public void testGetPreferencesByOwnerLayoutPortletNotAdded()
		throws Exception {

		PortletPreferencesTestUtil.assertNullLayoutPreferences(
			_layout, _portlet);

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		PortletPreferencesImpl portletPreferences =
			(PortletPreferencesImpl)
				PortletPreferencesLocalServiceUtil.getPreferences(
					TestPropsValues.getCompanyId(),
					PortletKeys.PREFS_OWNER_ID_DEFAULT,
					PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
					_portlet.getPortletId(), preferencesAsXML);

		Assert.assertArrayEquals(
			"The values for the preference name: " + _PREFERENCE_NAME +
				" should be " + _PREFERENCE_VALUES_SINGLE,
			_PREFERENCE_VALUES_SINGLE,
			portletPreferences.getMap().get(_PREFERENCE_NAME));

		PortletPreferencesTestUtil.assertPortletPreferencesOwnedByLayout
			(_layout, portletPreferences);
	}

	@Test
	public void testGetPreferencesByOwnerLayoutPortletNotAddedWithoutDefault()
		throws Exception {

		PortletPreferencesTestUtil.assertNullLayoutPreferences(
			_layout, _portlet);

		PortletPreferencesImpl portletPreferences = (PortletPreferencesImpl )
			PortletPreferencesLocalServiceUtil.getPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		PortletPreferencesTestUtil.assertEmptyPortletPreferences(
			_layout, portletPreferences);

		PortletPreferencesTestUtil.assertPortletPreferencesOwnedByLayout
			(_layout, portletPreferences);
	}

	@Test
	public void testGetPreferencesByPlid() throws Exception {
		Layout layout2 = LayoutTestUtil.addLayout(_group);

		Portlet[] portlets = getTestPortlets(2);

		PortletPreferencesTestUtil.addPortletLayoutWithoutDefaultPreferences(
			_layout, _portlet);

		PortletPreferencesTestUtil.addPortletLayoutWithoutDefaultPreferences(
			layout2, portlets);

		List<PortletPreferences> listPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesByPlid(
				layout2.getPlid());

		Assert.assertEquals(
			"The number of portlet preferences for the PLID " +
				layout2.getPlid() + " should be 2", 2,
			listPortletPreferences.size());
	}

	@Test
	public void testGetPreferencesByPortletLayout() throws Exception {
		Layout layout2 = LayoutTestUtil.addLayout(_group);

		PortletPreferencesTestUtil.addPortletLayoutWithoutDefaultPreferences(
			_layout, _portlet);

		PortletPreferencesTestUtil.addPortletLayoutWithoutDefaultPreferences(
			layout2, _portlet);

		List<PortletPreferences> listPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				_layout.getPlid(), _portlet.getPortletId());

		Assert.assertEquals(
			"1 portlet preferences should be returned for the PLID " +
				_layout.getPlid() + " and the portletId " +
				_portlet.getPortletId(),
			1, listPortletPreferences.size());

		Assert.assertEquals(
			"The search returned a PLID diferent than the expected: ",
			_layout.getPlid(), listPortletPreferences.get(0).getPlid());

		Assert.assertEquals(
			"The search returned a portletId diferent than the expected: ",
			_portlet.getPortletId(),
			listPortletPreferences.get(0).getPortletId());
	}

	@Test
	public void testGetPreferencesByPortletOwnerTypeLayout() throws Exception {
		Group group2 = GroupTestUtil.addGroup();

		PortletPreferencesTestUtil.addPortletLayoutWithoutDefaultPreferences(
			_layout, _portlet);

		PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
				group2, _layout, _portlet);

		List<PortletPreferences> listPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(
			"The search of the portlet preferences by type layout only" +
				"has to return one value",
			1, listPortletPreferences.size());

		PortletPreferencesImpl portletPreferenesImpl =
			PortletPreferencesTestUtil.convert(listPortletPreferences.get(0));

		PortletPreferencesTestUtil.assertPortletPreferencesOwnedByLayout(
			_layout, portletPreferenesImpl);
	}

	@Test
	public void testGetPreferencesByPortletPreferencesIds() throws Exception {

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		PortletPreferencesTestUtil.addPortletLayoutPreferences(
			_layout, preferencesAsXML, _portlet);

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
	public void testGetPreferencesCountByOwnerLayoutPortlet() throws Exception {
		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid(), _portlet, false);

		Assert.assertEquals(
			"Before add any portlet preferences for (the portlet " +
				_portlet.getPortletId() + " The group " +_group.getGroupId() +
				" and the layout " + _layout.getPlid() +
				" )the count should be 0",
			0, initialCount);

		PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
			_group, _layout, _portlet);

		long currentCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid(), _portlet, false);

		Assert.assertEquals(
			"After add one portlet preferences for (the portlet " +
				_portlet.getPortletId() + " The group " +_group.getGroupId() +
				" and the layout " + _layout.getPlid() +
				" ) the count should be 1",
			1, currentCount);
	}

	@Test
	public void testGetPreferencesCountByOwnerLayoutPortletNotDefault()
		throws Exception {

		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid(), _portlet, true);

		Assert.assertEquals(
			"Before add any portlet preferences for (the portlet " +
				_portlet.getPortletId() + " The group " +_group.getGroupId() +
				" and the layout " + _layout.getPlid() +
				" )the count should be 0",
			0, initialCount);

		PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
			_group, _layout, _portlet);

		long currentCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_layout.getPlid(), _portlet, true);

		Assert.assertEquals(
			"After add one portlet preferences for (the portlet " +
				_portlet.getPortletId() + " The group " +_group.getGroupId() +
				" and the layout " + _layout.getPlid() +
				" ) without specify the default value, "+
				"the search excluding default preferences should not " +
				"return any value ",
			0, currentCount);
	}

	@Test
	public void testGetPreferencesCountByOwnerPortletWithoutExcludeDefault()
		throws Exception {

		Group group2 = GroupTestUtil.addGroup();

		Portlet portletGroup2 = getTestPortlets(1)[0];

		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_portlet.getPortletId(), false);

		Assert.assertEquals(
			"Before add any portlet preferences for (the portlet " +
				_portlet.getPortletId() + " and the group " +
				_group.getGroupId() + " )the count should be 0",
			0, initialCount);

		PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
			_group, _layout, _portlet);
		PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
			group2, _layout, portletGroup2);

		long currentCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_portlet.getPortletId(), false);

		Assert.assertEquals(
			"After add one portlet preferences for (the portlet " +
				_portlet.getPortletId() + " and the group " +
				_group.getGroupId() + " ) the count should be 1",
			1, currentCount);
	}

	@Test
		public void testGetPreferencesCountByOwnerPortletExcludeDefault()
			throws Exception {

		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_portlet.getPortletId(), false);

		Assert.assertEquals(
			"Before add any portlet preferences for (the portlet " +
				_portlet.getPortletId() + " and the group " +
				_group.getGroupId() + " )the count should be 0",
			0, initialCount);

		PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
			_group, _layout, _portlet);

		long currentCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				_group.getGroupId(), PortletKeys.PREFS_OWNER_TYPE_GROUP,
				_portlet.getPortletId(), true);

		Assert.assertEquals(
			"After add one portlet preferences for (the portlet " +
				_portlet.getPortletId() + " and the group " +
				_group.getGroupId() + " ) without specify the default value, " +
				"the search excluding default preferences should not " +
				"return any value ",
			0, currentCount);
	}

	@Test
	public void testGetPreferencesCountByPortletOwnerType() throws Exception {
		Layout layout2 = LayoutTestUtil.addLayout(_group);

		long initialCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _portlet.getPortletId());

		PortletPreferencesTestUtil.addPortletLayoutWithoutDefaultPreferences(
			_layout, _portlet);

		PortletPreferencesTestUtil.addPortletLayoutWithoutDefaultPreferences(
			layout2, _portlet);

		PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
			_group, _layout, _portlet);

		long currentCount =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _portlet.getPortletId());

		Assert.assertEquals("The search of the portlet preferences of the " +
			"portlet " +_portlet.getPortletId() + " and the ownertype layout " +
			"should return 2 results",initialCount + 2, currentCount);
	}

	@Test
	public void testGetPreferencesCountyPortletOwnerTypeLayout()
		throws Exception {

		Group group2 = GroupTestUtil.addGroup();

		long initialCountTypeLayout =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(
			"Before add preferences of type layout to the layout  " +
				_layout.getPlid() + " and the portlet " +
				_portlet.getPortletId() +
				" any portlet preferences should be found for" +
				" this portlet and layout" ,0, initialCountTypeLayout);

		long initialCountTypeGroup =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_GROUP, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(
			"Before add preferences of type group to the layout  " +
				_layout.getPlid() + " and the portlet " +
				_portlet.getPortletId() +
				" any portlet preferences should be found for" +
				" this portlet and layout" ,0, initialCountTypeGroup);

		long initialCountTypeCompany =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_COMPANY, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(
			"Before add preferences of type company to the layout  " +
				_layout.getPlid() + " and the portlet " +
				_portlet.getPortletId() +
				" any portlet preferences should be found for" +
				" this portlet and layout" ,0, initialCountTypeCompany);

		PortletPreferencesTestUtil.addPortletLayoutWithoutDefaultPreferences(
			_layout, _portlet);

		PortletPreferencesTestUtil.addPortletGroupWithoutDefaultPreferences(
			group2, _layout, _portlet);

		long currentCountTypeLayout =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(
			"After add preferences of type layout to the layout  " +
				_layout.getPlid() + " and the portlet " +
				_portlet.getPortletId() +
				"one portlet preference should by returned after search "+
				"by these two parameters and the ownertype layout", 1,
			currentCountTypeLayout);

		long currentCountTypeGroup =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_GROUP, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(
			"After add preferences of type group to the layout  " +
				_layout.getPlid() + " and the portlet " +
				_portlet.getPortletId() +
				"one portlet preference should by returned after search "+
				"by these two parameters and the ownertype group", 1,
			currentCountTypeGroup);

		long currentCountTypeCompany =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_COMPANY, _layout.getPlid(),
				_portlet.getPortletId());

		Assert.assertEquals(
			"Before add preferences of type company to the layout  " +
				_layout.getPlid() + " and the portlet " +
				_portlet.getPortletId() +
				" any portlet preferences should be found for" +
				" this portlet and layout" ,0, currentCountTypeCompany);
	}

	@Test
	public void testGetStrictPreferences() throws Exception {

		javax.portlet.PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.getStrictPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		PortletPreferencesTestUtil.assertStrictPortletPreferences(
			portletPreferences);
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

		PortletPreferencesTestUtil.assertStrictPortletPreferences(
			portletPreferences);
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

		PortletPreferencesTestUtil.assertStrictPortletPreferences(
			portletPreferences);
	}

	@Test
	public void testUpdatePreferencesMultipleValues() throws Exception {
		PortletPreferencesTestUtil.assertNullLayoutPreferences(
			_layout, _portlet);

		PortletPreferencesTestUtil.assertNullLayoutPreferences(
			_layout, _portlet);

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		PortletPreferencesTestUtil.addPortletLayoutPreferences(
			_layout, preferencesAsXML, _portlet);

		String multiplePreferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_MULTIPLE);

		PortletPreferencesLocalServiceUtil.updatePreferences(
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
			_portlet.getPortletId(), multiplePreferencesAsXML);

		javax.portlet.PortletPreferences preferences =
			PortletPreferencesLocalServiceUtil.getPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		PortletPreferencesTestUtil.assertPortletPreferenceValues(
			preferences, _PREFERENCE_NAME, _PREFERENCE_VALUES_MULTIPLE);
	}

	@Test
	public void testUpdatePreferencesMultipleValuesByPortletPreferencesImpl()
		throws Exception {

		PortletPreferencesTestUtil.assertNullLayoutPreferences(
			_layout, _portlet);

		String preferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_SINGLE);

		PortletPreferencesTestUtil.addPortletLayoutPreferences(
			_layout, preferencesAsXML, _portlet);

		String multiplePreferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_MULTIPLE);

		PortletPreferencesImpl portletPreferencesImpl =
			(PortletPreferencesImpl)PortletPreferencesFactoryUtil.fromXML(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId(), multiplePreferencesAsXML);

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

		PortletPreferencesTestUtil.assertPortletPreferenceValues(
			preferences, _PREFERENCE_NAME, _PREFERENCE_VALUES_MULTIPLE);
	}

	@Test
	public void testUpdatePreferencesMultipleValuesNoPreviouslyAdded()
		throws Exception {

		PortletPreferencesTestUtil.assertNullLayoutPreferences(
			_layout, _portlet);

		String multiplePreferencesAsXML =
			PortletPreferencesTestUtil.getPreferencesAsXMLString(
				_PREFERENCE_NAME, _PREFERENCE_VALUES_MULTIPLE);

		PortletPreferencesLocalServiceUtil.updatePreferences(
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
			_portlet.getPortletId(), multiplePreferencesAsXML);

		javax.portlet.PortletPreferences preferences =
			PortletPreferencesLocalServiceUtil.getPreferences(
				TestPropsValues.getCompanyId(),
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _layout.getPlid(),
				_portlet.getPortletId());

		PortletPreferencesTestUtil.assertPortletPreferenceValues(
			preferences, _PREFERENCE_NAME, _PREFERENCE_VALUES_MULTIPLE);
	}

	protected Portlet[] getTestPortlets(int numberPortlets) throws Exception {
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