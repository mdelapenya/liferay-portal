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

package com.liferay.portal.upgrade.v6_2_0;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.UpgradeProcessExecutionTestListener;
import com.liferay.portal.upgrade.UpgradeProcessTestUtil;
import com.liferay.portal.upgrade.UpgradeProcess_6_2_0;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Manuel de la Peña
 */
@ExecutionTestListeners(
	listeners = {
		UpgradeProcessExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class UpgradeProcess_6_2_0_Test {

	@Test
	public void testDoUpgrade() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeProcess_6_2_0.class);
	}

	@Test
	public void testUpgradeAssetPublisher() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeAssetPublisher.class);
	}

	@Test
	public void testUpgradeBlogs() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeBlogs.class);
	}

	@Test
	public void testUpgradeBlogsAggregator() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeBlogsAggregator.class);
	}

	@Test
	public void testUpgradeCustomizablePortlets() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeCustomizablePortlets.class);
	}

	@Test
	public void testUpgradeDocumentLibrary() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeDocumentLibrary.class);
	}

	@Test
	public void testUpgradeDynamicDataListDisplay() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeDynamicDataListDisplay.class);
	}

	@Test
	public void testUpgradeDynamicDataMapping() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeDynamicDataMapping.class);
	}

	@Test
	public void testUpgradeJournal() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeJournal.class);
	}

	@Test
	public void testUpgradeMessageBoards() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeMessageBoards.class);
	}

	@Test
	public void testUpgradeMessageBoardsAttachments() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeMessageBoardsAttachments.class);
	}

	@Test
	public void testUpgradePortletPreferences() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradePortletPreferences.class);
	}

	@Test
	public void testUpgradeSchema() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeSchema.class);
	}

	@Test
	public void testUpgradeUser() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeUser.class);
	}

	@Test
	public void testUpgradeWikiAttachments() throws Exception {
		UpgradeProcessTestUtil.doUpgrade(UpgradeWikiAttachments.class);
	}

}