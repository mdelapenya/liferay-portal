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
import org.junit.Assert;

import java.util.Map;

/**
 * @author Cristina González
 */
public class PortletPreferencesTestUtil {

	public static PortletPreferencesImpl convert(
			PortletPreferences portletPreferences)
		throws Exception {

		return (PortletPreferencesImpl)PortletPreferencesFactoryUtil.fromXML(
			TestPropsValues.getCompanyId(), portletPreferences.getOwnerId(),
			portletPreferences.getOwnerType(), portletPreferences.getPlid(),
			portletPreferences.getPortletId(),
			portletPreferences.getPreferences());
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

	public static PortletPreferences[] addPortletGroupWithoutDefaultPreferences(
			Group group, Layout layout, Portlet... portlet)
		throws Exception {

		return addPortletsGroupPreferences(group, layout, null, portlet);
	}

	public static PortletPreferences[] addPortletsGroupPreferences(
			Group group, Layout layout, String defaultPreferences,
			Portlet ... portlets)
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

	public static PortletPreferences addPortletLayoutPreferences(
			Layout layout, String defaultPreferences, Portlet portlet)
		throws Exception {

		return PortletPreferencesLocalServiceUtil.addPortletPreferences(
			TestPropsValues.getCompanyId(), PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, layout.getPlid(),
			portlet.getPortletId(), portlet, defaultPreferences);
	}

	public static  PortletPreferences[] addPortletLayoutPreferences(
			Layout layout, String defaultPreferences, Portlet ... portlets)
		throws Exception {

		PortletPreferences[] results = new PortletPreferences[portlets.length];

		for (int i = 0; i < results.length; i++) {
			results[i] =  addPortletLayoutPreferences(
				layout, defaultPreferences, portlets[i]);
		}

		return results;
	}

	public static void assertPortletPreferenceValues(
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

	public static void assertPortletPreferenceValues(

		PortletPreferences portletPreferences, String preferenceName,
			String[] preferenceValues)
		throws Exception {

		PortletPreferencesImpl portletPreferencesImpl =
			PortletPreferencesTestUtil.convert(portletPreferences);

		assertPortletPreferenceValues(
			portletPreferencesImpl, preferenceName, preferenceValues);
	}


	public static PortletPreferences[]
		addPortletLayoutWithoutDefaultPreferences(
			Layout layout, Portlet... portlets)
		throws Exception {

		return addPortletLayoutPreferences(layout, null, portlets);
	}

	public static javax.portlet.PortletPreferences fetchLayoutPreferences(
			Layout layout, Portlet portlet)
		throws Exception {

		return PortletPreferencesLocalServiceUtil.fetchPreferences(
			TestPropsValues.getCompanyId(), PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, layout.getPlid(),
			portlet.getPortletId());
	}

	public static void assertEmptyPortletPreferences(
			Layout layout, javax.portlet.PortletPreferences portletPreferences)
		throws Exception {

		assertPortletPreferencesOwnedByLayout(
			layout, (PortletPreferencesImpl) portletPreferences);

		PortletPreferencesImpl portletPreferencesImpl =
			(PortletPreferencesImpl)portletPreferences;

		Assert.assertTrue(portletPreferencesImpl.getMap().isEmpty());
	}

	public static void assertNullLayoutPreferences(
			Layout layout, Portlet portlet)
		throws Exception {

		javax.portlet.PortletPreferences preferences =
			PortletPreferencesTestUtil.fetchLayoutPreferences(layout, portlet);
		Assert.assertNull(preferences);
	}

	public static void assertPortletPreferencesOwnedByLayout(
		Layout layout, PortletPreferencesImpl portletPreferences) {

		Assert.assertEquals(layout.getPlid(), portletPreferences.getPlid());

		Assert.assertEquals(
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
			portletPreferences.getOwnerType());

		Assert.assertEquals(
			PortletKeys.PREFS_OWNER_ID_DEFAULT,
			portletPreferences.getOwnerId());
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


}