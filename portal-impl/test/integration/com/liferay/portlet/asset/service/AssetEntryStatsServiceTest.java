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

package com.liferay.portlet.asset.service;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.Sync;
import com.liferay.portal.test.SynchronousDestinationExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.asset.model.AssetEntryStats;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.util.BlogsTestUtil;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Manuel de la Peña
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		SynchronousDestinationExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Sync
public class AssetEntryStatsServiceTest {

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_entry = BlogsTestUtil.addEntry(
			TestPropsValues.getUserId(), _group, true);
	}

	@After
	public void tearDown() throws Exception {
		GroupLocalServiceUtil.deleteGroup(_group);
	}

	@Test
	public void testGetAssetEntryStats() throws Exception {
		AssetEntryLocalServiceUtil.incrementViewCounter(
			TestPropsValues.getUserId(), _entry.getModelClassName(),
			_entry.getPrimaryKey());

		AssetEntryStats assetEntryStats = getAssetEntryStats(
			_entry.getModelClassName(), _entry.getPrimaryKey());

		Assert.assertEquals(1, assetEntryStats.getViewCount());
	}

	@Test
	public void testGetNotExistingAssetEntryStats() throws Exception {
		AssetEntryStats assetEntryStats = getAssetEntryStats(
			_entry.getModelClassName(), _entry.getPrimaryKey());

		Assert.assertNull(assetEntryStats);
	}

	protected AssetEntryStats getAssetEntryStats(String className, long classPK)
		throws Exception {

		return getAssetEntryStats(className, classPK, 0);
	}

	protected AssetEntryStats getAssetEntryStats(
			String className, long classPK, int increment)
		throws Exception {

		Date now = new Date();

		Calendar calendar = CalendarFactoryUtil.getCalendar();
		calendar.setTime(now);

		if (increment > 0) {
			calendar.add(Calendar.YEAR, increment);
		}

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);

		long classNameId = ClassNameLocalServiceUtil.getClassNameId(className);

		Thread.sleep(100);

		return AssetEntryStatsLocalServiceUtil.fetchByC_C_Date(
			classNameId, classPK, day, month, year);
	}

	private BlogsEntry _entry;
	private Group _group;

}