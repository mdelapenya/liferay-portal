/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portalweb.socialofficehome.sites.privatesite;

import com.liferay.portalweb.portal.BaseTestSuite;
import com.liferay.portalweb.socialofficehome.sites.privatesite.addsitessitetypeprivate.AddSitesSiteTypePrivateTests;
import com.liferay.portalweb.socialofficehome.sites.privatesite.searchsitessitetypeprivate.SearchSitesSiteTypePrivateTests;
import com.liferay.portalweb.socialofficehome.sites.privatesite.soussearchsitessitetypeprivate.SOUs_SearchSitesSiteTypePrivateTests;
import com.liferay.portalweb.socialofficehome.sites.privatesite.viewsitessitetypeprivate.ViewSitesSiteTypePrivateTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Brian Wing Shun Chan
 */
public class PrivateSiteTestPlan extends BaseTestSuite {

	public static Test suite() {
		TestSuite testSuite = new TestSuite();

		testSuite.addTest(AddSitesSiteTypePrivateTests.suite());
		testSuite.addTest(SearchSitesSiteTypePrivateTests.suite());
		testSuite.addTest(SOUs_SearchSitesSiteTypePrivateTests.suite());
		testSuite.addTest(ViewSitesSiteTypePrivateTests.suite());

		return testSuite;
	}

}