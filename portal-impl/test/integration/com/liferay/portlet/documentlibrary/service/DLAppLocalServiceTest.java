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

import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.test.DeleteAfterTestRun;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.Sync;
import com.liferay.portal.test.SynchronousDestinationExecutionTestListener;
import com.liferay.portal.util.test.GroupTestUtil;
import com.liferay.portal.util.test.RandomTestUtil;
import com.liferay.portal.util.test.ServiceContextTestUtil;
import com.liferay.portal.util.test.TestPropsValues;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.NoSuchFolderException;
import com.liferay.portlet.documentlibrary.model.DLFileEntryConstants;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.model.DLSyncConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Manuel de la Peña
 */
@RunWith(Enclosed.class)
public class DLAppLocalServiceTest {

	@ExecutionTestListeners(
		listeners = {
			MainServletExecutionTestListener.class,
			SynchronousDestinationExecutionTestListener.class
		})
	@RunWith(LiferayIntegrationJUnitTestRunner.class)
	@Sync
	public static final class WhenAddingAFolder {

		@Before
		public void setUp() throws Exception {
			_group = GroupTestUtil.addGroup();

			_serviceContext = ServiceContextTestUtil.getServiceContext(
				_group.getGroupId());
		}

		@Test
		public void shouldAddAnAsset() throws Exception {
			Folder folder = addFolder(false, _serviceContext);

			AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(
				DLFolderConstants.getClassName(), folder.getFolderId());

			Assert.assertNotNull(assetEntry);
		}

		@Test
		public void shouldAddFolder() throws Exception {
			Folder folder = addFolder(true, _serviceContext);

			Assert.assertTrue(folder != null);
		}

		@Test
		public void shouldAddRootFolder() throws Exception {
			Folder folder = addFolder(false, _serviceContext);

			Assert.assertTrue(folder != null);
		}

		@Test
		public void shouldFireASyncEvent() throws Exception {
			AtomicInteger counter = registerDLSyncEventProcessorMessageListener(
				DLSyncConstants.EVENT_ADD);

			addFolder(true, _serviceContext);

			Assert.assertEquals(1, counter.get());
		}

		@DeleteAfterTestRun
		private Group _group;
		private ServiceContext _serviceContext;

	}

	@ExecutionTestListeners(
		listeners = {
			MainServletExecutionTestListener.class,
			SynchronousDestinationExecutionTestListener.class
		})
	@RunWith(LiferayIntegrationJUnitTestRunner.class)
	@Sync
	public static final class WhenUpdatingAFileEntry {

		@Before
		public void setUp() throws Exception {
			_group = GroupTestUtil.addGroup();

			_serviceContext = ServiceContextTestUtil.getServiceContext(
				_group.getGroupId());
		}

		@Test
		public void shouldFireASyncEvent() throws Throwable {
			AtomicInteger counter = registerDLSyncEventProcessorMessageListener(
				DLSyncConstants.EVENT_UPDATE);

			ServiceContext serviceContext =
				ServiceContextTestUtil.getServiceContext(_group.getGroupId());

			FileEntry fileEntry = addFileEntry(serviceContext);

			updateFileEntry(serviceContext, fileEntry);

			Assert.assertEquals(2, counter.get());
		}

		@Test
		public void shouldUpdateItsAssetEntry() throws Exception {
			FileEntry fileEntry = addFileEntry(_serviceContext);

			updateFileEntry(_serviceContext, fileEntry);

			AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(
				DLFileEntryConstants.getClassName(),
				fileEntry.getFileEntryId());

			Assert.assertEquals("New Title", assetEntry.getTitle());
		}

		@DeleteAfterTestRun
		private Group _group;
		private ServiceContext _serviceContext;

	}

	@ExecutionTestListeners(
		listeners = {
			MainServletExecutionTestListener.class,
			SynchronousDestinationExecutionTestListener.class
		})
	@RunWith(LiferayIntegrationJUnitTestRunner.class)
	@Sync
	public static final class WhenUpdatingAFolder {

		@Before
		public void setUp() throws Exception {
			_group = GroupTestUtil.addGroup();

			_serviceContext = ServiceContextTestUtil.getServiceContext(
				_group.getGroupId());
		}

		@Test
		public void shouldUpdateItsAssetEntry() throws Exception {
			Folder folder = addFolder(false, "Old Name", _serviceContext);

			DLAppLocalServiceUtil.updateFolder(
				folder.getFolderId(), folder.getParentFolderId(), "New Name",
				RandomTestUtil.randomString(), _serviceContext);

			AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(
				DLFolderConstants.getClassName(), folder.getFolderId());

			Assert.assertEquals("New Name", assetEntry.getTitle());
		}

		@Test(expected = NoSuchFolderException.class)
		public void shouldFailOnDefaultParentFolder() throws Exception {
			ServiceContext serviceContext =
				ServiceContextTestUtil.getServiceContext(_group.getGroupId());

			DLAppLocalServiceUtil.updateFolder(
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(), StringPool.BLANK, serviceContext);
		}

		@DeleteAfterTestRun
		private Group _group;
		private ServiceContext _serviceContext;

	}

	protected static FileEntry addFileEntry(ServiceContext serviceContext)
		throws Exception {

		return DLAppLocalServiceUtil.addFileEntry(
			TestPropsValues.getUserId(), serviceContext.getScopeGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			RandomTestUtil.randomString(), ContentTypes.TEXT_PLAIN, "Old Title",
			RandomTestUtil.randomString(), null, RandomTestUtil.randomBytes(),
			serviceContext);
	}

	protected static Folder addFolder(
			boolean rootFolder, ServiceContext serviceContext)
		throws Exception {

		return addFolder(
			rootFolder, RandomTestUtil.randomString(), serviceContext);
	}

	protected static Folder addFolder(
			boolean rootFolder, String name, ServiceContext serviceContext)
		throws Exception {

		long parentFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;

		if (!rootFolder) {
			Folder parentFolder = addFolder(
				parentFolderId, "Test Folder", true, serviceContext);

			parentFolderId = parentFolder.getFolderId();
		}

		return addFolder(parentFolderId, name, serviceContext);
	}

	protected static Folder addFolder(
			long parentFolderId, String name, boolean deleteExisting,
			ServiceContext serviceContext)
		throws Exception {

		if (deleteExisting) {
			try {
				Folder folder = DLAppLocalServiceUtil.getFolder(
					serviceContext.getScopeGroupId(), parentFolderId, name);

				DLAppLocalServiceUtil.deleteFolder(folder.getFolderId());
			}
			catch (NoSuchFolderException nsfe) {
			}
		}

		return DLAppLocalServiceUtil.addFolder(
			TestPropsValues.getUserId(), serviceContext.getScopeGroupId(),
			parentFolderId, name, StringPool.BLANK, serviceContext);
	}

	protected static Folder addFolder(
			long parentFolderId, String name, ServiceContext serviceContext)
		throws Exception {

		return addFolder(parentFolderId, name, false, serviceContext);
	}

	protected static AtomicInteger registerDLSyncEventProcessorMessageListener(
		final String targetEvent) {

		final AtomicInteger counter = new AtomicInteger();

		MessageBusUtil.registerMessageListener(
			DestinationNames.DOCUMENT_LIBRARY_SYNC_EVENT_PROCESSOR,
			new MessageListener() {

				@Override
				public void receive(Message message) {
					Object event = message.get("event");

					if (targetEvent.equals(event)) {
						counter.incrementAndGet();
					}
				}

			});

		return counter;
	}

	protected static FileEntry updateFileEntry(
			ServiceContext serviceContext, FileEntry fileEntry)
		throws Exception {

		return DLAppLocalServiceUtil.updateFileEntry(
			TestPropsValues.getUserId(), fileEntry.getFileEntryId(),
			RandomTestUtil.randomString(), ContentTypes.TEXT_PLAIN, "New Title",
			RandomTestUtil.randomString(), null, true,
			RandomTestUtil.randomBytes(), serviceContext);
	}

}