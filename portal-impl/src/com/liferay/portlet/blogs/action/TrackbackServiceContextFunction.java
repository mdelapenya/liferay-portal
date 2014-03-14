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

package com.liferay.portlet.blogs.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Function;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;

import javax.portlet.PortletRequest;

/**
 * @author André de Oliveira
 */
public class TrackbackServiceContextFunction
	implements Function<String, ServiceContext> {

	public TrackbackServiceContextFunction(PortletRequest portletRequest) {
		_portletRequest = portletRequest;
	}

	@Override
	public ServiceContext apply(String className) {

		try {
			return ServiceContextFactory.getInstance(
				className, _portletRequest);
		}
		catch (PortalException e) {
			throw new RuntimeException(e);
		}
		catch (SystemException e) {
			throw new RuntimeException(e);
		}
	}

	private PortletRequest _portletRequest;

}