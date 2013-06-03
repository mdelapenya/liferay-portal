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

package com.liferay.portal.verify;

import com.liferay.portal.events.StartupHelper;
import com.liferay.portal.events.StartupHelperUtil;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * @author Manuel de la Peña
 */
public class BaseVerifyTestCase {

	@BeforeClass
	public static void setUpClass() {
		_startupHelper = StartupHelperUtil.getStartupHelper();

		new StartupHelperUtil().setStartupHelper(new MockStartupHelper());
	}

	@AfterClass
	public static void tearDownClass() {
		new StartupHelperUtil().setStartupHelper(_startupHelper);
	}

	protected static class MockStartupHelper extends StartupHelper {

		public boolean isUpgraded() {
			return true;
		}

	}

	private static StartupHelper _startupHelper;

}