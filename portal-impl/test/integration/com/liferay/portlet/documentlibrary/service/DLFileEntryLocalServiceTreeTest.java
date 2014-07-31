/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.documentlibrary.service;

import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.model.Group;
import com.liferay.portal.test.DeleteAfterTestRun;
import com.liferay.portal.test.rule.MainServletTestRule;
import com.liferay.portal.test.runners.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.util.test.GroupTestUtil;
import com.liferay.portal.util.test.TestPropsValues;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.util.test.DLAppTestUtil;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.testng.Assert;

/**
 * @author Shinn Lok
 */
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class DLFileEntryLocalServiceTreeTest {

	@ClassRule
	public static MainServletTestRule mainServletTestRule =
		new MainServletTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testRebuildTree() throws Exception {
		createTree();

		for (FileEntry fileEntry : _fileEntries) {
			DLFileEntry dlFileEntry = DLFileEntryLocalServiceUtil.getFileEntry(
				fileEntry.getFileEntryId());

			dlFileEntry.setTreePath(null);

			DLFileEntryLocalServiceUtil.updateDLFileEntry(dlFileEntry);
		}

		DLFileEntryLocalServiceUtil.rebuildTree(TestPropsValues.getCompanyId());

		for (FileEntry fileEntry : _fileEntries) {
			DLFileEntry dlFileEntry = DLFileEntryLocalServiceUtil.getFileEntry(
				fileEntry.getFileEntryId());

			Assert.assertEquals(
				dlFileEntry.buildTreePath(), dlFileEntry.getTreePath());
		}
	}

	protected void createTree() throws Exception {
		FileEntry fileEntryA = DLAppTestUtil.addFileEntry(
			_group.getGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			"Entry A.txt");

		_fileEntries.add(fileEntryA);

		_folder = DLAppTestUtil.addFolder(
			_group.getGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			"Folder A");

		FileEntry fileEntryAA = DLAppTestUtil.addFileEntry(
			_group.getGroupId(), _folder.getFolderId(), "Entry A.txt");

		_fileEntries.add(fileEntryAA);
	}

	private List<FileEntry> _fileEntries = new ArrayList<FileEntry>();
	private Folder _folder;

	@DeleteAfterTestRun
	private Group _group;

}