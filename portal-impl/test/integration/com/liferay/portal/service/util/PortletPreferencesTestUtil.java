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

package com.liferay.portal.service.util;

import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.PortletPreferencesImpl;
import com.liferay.portlet.StrictPortletPreferencesImpl;

import java.util.Map;

import org.junit.Assert;

/**
 * @author Cristina González
 */
public class PortletPreferencesTestUtil {

	public static PortletPreferences[] addPortletGroupWithoutDefaultPreferences(
			Group group, Layout layout, Portlet... portlet)
		throws Exception {

		return addPortletsGroupPreferences(group, layout, null, portlet);
	}

	public static  PortletPreferences[] addPortletLayoutPreferences(
			Layout layout, String defaultPreferences, Portlet ... portlets)
		throws Exception {

		PortletPreferences[] results = new PortletPreferences[portlets.length];

		for (int i = 0; i < results.length; i++) {
			results[i] =
				PortletPreferencesLocalServiceUtil.addPortletPreferences(
					TestPropsValues.getCompanyId(),
					PortletKeys.PREFS_OWNER_ID_DEFAULT,
					PortletKeys.PREFS_OWNER_TYPE_LAYOUT, layout.getPlid(),
					portlets[i].getPortletId(), portlets[i],
					defaultPreferences);
		}

		return results;
	}

	public static PortletPreferences[]
		addPortletLayoutWithoutDefaultPreferences(
			Layout layout, Portlet... portlets)
		throws Exception {

		return addPortletLayoutPreferences(layout, null, portlets);
	}

	public static PortletPreferences[] addPortletsGroupPreferences(
			Group group, Layout layout, String defaultPreferences,
			Portlet... portlets)
		throws Exception {

		PortletPreferences[] results = new PortletPreferences[portlets.length];

		for (int i = 0; i < results.length; i++) {
			results[i] =
				PortletPreferencesLocalServiceUtil.addPortletPreferences(
					TestPropsValues.getCompanyId(), group.getGroupId(),
					PortletKeys.PREFS_OWNER_TYPE_GROUP, layout.getPlid(),
					portlets[i].getPortletId(), portlets[i],
					defaultPreferences);
		}

		return results;
	}

	public static void assertEmptyPortletPreferences(
			Layout layout, javax.portlet.PortletPreferences portletPreferences)
		throws Exception {

		assertPortletPreferencesOwnedByLayout(
			layout, (PortletPreferencesImpl)portletPreferences);

		PortletPreferencesImpl portletPreferencesImpl =
			(PortletPreferencesImpl)portletPreferences;

		Assert.assertTrue(
			"The portlet preferences defined by (ownerType: " +
			portletPreferencesImpl.getOwnerType() + " ownerId: " +
			portletPreferencesImpl.getOwnerId() + " and plid: " +
			portletPreferencesImpl.getPlid() +
			") has defined the preferences: "
			+portletPreferencesImpl.getMap().keySet() +
			" and was expected to be empty",
			portletPreferencesImpl.getMap().isEmpty());
	}

	public static void assertNullLayoutPreferences(
			Layout layout, Portlet portlet)
		throws Exception {

		PortletPreferencesImpl preferences =
			(PortletPreferencesImpl)PortletPreferencesTestUtil.
				fetchLayoutPreferences(layout, portlet);

		Assert.assertNull(
			"Any portlet preferences was expected to be defined for " +
				"(layoutId: " + layout.getPlid() + " and portletId: " +
				portlet.getPortletId() +
				")",
			preferences);
	}

	public static void assertPortletPreferencesOwnedByLayout(
		Layout layout, PortletPreferencesImpl portletPreferences) {

		Assert.assertEquals(
			"The portlet preferences PLID is not the same as the layout PLID, ",
			layout.getPlid(), portletPreferences.getPlid());

		Assert.assertEquals(
			"The portlet preferences owner type is not layout, ",
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
			portletPreferences.getOwnerType());

		Assert.assertEquals(
			"The portlet preferences owner is is not the default owner, ",
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			portletPreferences.getOwnerId());
	}

	public static void assertPortletPreferenceValues(
			javax.portlet.PortletPreferences portletPreferences,
			String preferenceName, String[] preferenceValues)
		throws Exception {

		PortletPreferencesImpl portletPreferencesImpl =
			(PortletPreferencesImpl)portletPreferences;

		Map<String, String[]> portletPreferencesMap =
			portletPreferencesImpl.getMap();

		Assert.assertFalse(
			"There are not portlet preferences defined for "+
			"(ownerType: " + portletPreferencesImpl.getOwnerType() +
			" ownerId: " + portletPreferencesImpl.getOwnerId() + " and plid: " +
			portletPreferencesImpl.getPlid() +
			")",portletPreferencesMap.isEmpty());

		Assert.assertArrayEquals(
			"The value of the portlet preference " +
			preferenceName + " defined for (ownerType: " +
			portletPreferencesImpl.getOwnerType() + " ownerId: " +
			portletPreferencesImpl.getOwnerId() + " and plid: " +
			portletPreferencesImpl.getPlid() +
			") is not the expected", preferenceValues,
			portletPreferencesMap.get(preferenceName));
	}

	public static void assertPortletPreferenceValues(
			PortletPreferences portletPreferences, String preferenceName,
			String[] preferenceValues)
		throws Exception {

		PortletPreferencesImpl portletPreferencesImpl =
			PortletPreferencesTestUtil.convert(portletPreferences);

		assertPortletPreferenceValues(
			portletPreferencesImpl, preferenceName, preferenceValues);
	}

	public static void assertStrictPortletPreferences(
		javax.portlet.PortletPreferences portletPreferences) {

		StrictPortletPreferencesImpl strictPortletPreferences =
			(StrictPortletPreferencesImpl)portletPreferences;

		Assert.assertEquals(
			"The PLID of a StrictPortletPreference always should by 0: ", 0,
			strictPortletPreferences.getPlid());

		Assert.assertEquals(
			"The OwnerType of a StrictPortletPreference always should by 0: ",
			0, strictPortletPreferences.getOwnerType());

		Assert.assertEquals(
			"The OwnerId of a StrictPortletPreference always should by 0: ", 0,
			strictPortletPreferences.getOwnerId());

		Assert.assertTrue(
			"The map of preferences of a StrictPortletPreference "+
				"always should by empty",
			strictPortletPreferences.getMap().isEmpty());
	}

	public static PortletPreferencesImpl convert(
			PortletPreferences portletPreferences)
		throws Exception {

		return (PortletPreferencesImpl)PortletPreferencesFactoryUtil.fromXML(
			TestPropsValues.getCompanyId(), portletPreferences.getOwnerId(),
			portletPreferences.getOwnerType(), portletPreferences.getPlid(),
			portletPreferences.getPortletId(),
			portletPreferences.getPreferences());
	}

	public static javax.portlet.PortletPreferences fetchLayoutPreferences(
			Layout layout, Portlet portlet)
		throws Exception {

		return PortletPreferencesLocalServiceUtil.fetchPreferences(
			TestPropsValues.getCompanyId(), PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, layout.getPlid(),
			portlet.getPortletId());
	}

	public static PortletPreferences[] fetchPortletPreferences(
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

	public static String getPreferencesAsXMLString(
		String name, String[] values) {

		String preferencesAsXml = "<portlet-preferences><preference>";
		preferencesAsXml+= "<name>" + name + "</name>";

		for (String value : values) {
			preferencesAsXml+= "<value>" + value + "</value>";
		}

		preferencesAsXml+="</preference></portlet-preferences>";
		return preferencesAsXml;
	}

}