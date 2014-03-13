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

package com.liferay.portal.util.rules.subscriptionstestrules;

import com.liferay.portal.kernel.annotation.AnnotationLocator;
import com.liferay.portal.util.rules.IgnoreCondition;

/**
 * @author Roberto Díaz
 */
public class RootContainerModelSubscriptionNotSupportedCondition
	implements IgnoreCondition {

	@Override
	public boolean ignoreTest(Class clazz) {
		RootContainerModelSubscriptionNotSupported annotation =
			AnnotationLocator.locate(
				clazz, RootContainerModelSubscriptionNotSupported.class);

		if (annotation == null) {
			return false;
		}

		return true;
	}

}