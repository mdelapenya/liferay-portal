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

package com.liferay.portal.util.comparator;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Portlet;

import java.util.Comparator;

/**
 * @author Carlos Sierra Andrés
 */
public class PortletNameComparator implements Comparator<Portlet> {

	@Override
	public int compare(Portlet portlet1, Portlet portlet2) {
		String portletName1;
		String portletName2;

		if (portlet1 == null) {
			portletName1 = StringPool.BLANK;
		}
		else {
			portletName1 = portlet1.getPortletName();

			if (portletName1 == null) {
				portletName1 = StringPool.BLANK;
			}
		}

		if (portlet2 == null) {
			portletName2 = StringPool.BLANK;
		}
		else {
			portletName2 = portlet2.getPortletName();

			if (portletName2 == null) {
				portletName2 = StringPool.BLANK;
			}
		}

		return portletName1.compareTo(portletName2);
	}

}