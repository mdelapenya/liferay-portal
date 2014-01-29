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
		Group group, Layout layout, String defaultPreferences, Portlet ... portlets)
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



}