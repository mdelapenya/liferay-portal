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

package com.liferay.portlet.documentlibrary.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;
import com.liferay.portal.util.PropsValues;

import com.liferay.portlet.documentlibrary.NoSuchFileVersionException;
import com.liferay.portlet.documentlibrary.model.DLFileVersion;
import com.liferay.portlet.documentlibrary.model.impl.DLFileVersionModelImpl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 */
@ExecutionTestListeners(listeners =  {
	PersistenceExecutionTestListener.class})
@RunWith(LiferayPersistenceIntegrationJUnitTestRunner.class)
public class DLFileVersionPersistenceTest {
	@After
	public void tearDown() throws Exception {
		Map<Serializable, BasePersistence<?>> basePersistences = _transactionalPersistenceAdvice.getBasePersistences();

		Set<Serializable> primaryKeys = basePersistences.keySet();

		for (Serializable primaryKey : primaryKeys) {
			BasePersistence<?> basePersistence = basePersistences.get(primaryKey);

			try {
				basePersistence.remove(primaryKey);
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug("The model with primary key " + primaryKey +
						" was already deleted");
				}
			}
		}

		_transactionalPersistenceAdvice.reset();
	}

	@Test
	public void testCreate() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		DLFileVersion dlFileVersion = _persistence.create(pk);

		Assert.assertNotNull(dlFileVersion);

		Assert.assertEquals(dlFileVersion.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		DLFileVersion newDLFileVersion = addDLFileVersion();

		_persistence.remove(newDLFileVersion);

		DLFileVersion existingDLFileVersion = _persistence.fetchByPrimaryKey(newDLFileVersion.getPrimaryKey());

		Assert.assertNull(existingDLFileVersion);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addDLFileVersion();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		DLFileVersion newDLFileVersion = _persistence.create(pk);

		newDLFileVersion.setUuid(ServiceTestUtil.randomString());

		newDLFileVersion.setGroupId(ServiceTestUtil.nextLong());

		newDLFileVersion.setCompanyId(ServiceTestUtil.nextLong());

		newDLFileVersion.setUserId(ServiceTestUtil.nextLong());

		newDLFileVersion.setUserName(ServiceTestUtil.randomString());

		newDLFileVersion.setCreateDate(ServiceTestUtil.nextDate());

		newDLFileVersion.setModifiedDate(ServiceTestUtil.nextDate());

		newDLFileVersion.setRepositoryId(ServiceTestUtil.nextLong());

		newDLFileVersion.setFolderId(ServiceTestUtil.nextLong());

		newDLFileVersion.setFileEntryId(ServiceTestUtil.nextLong());

		newDLFileVersion.setTreePath(ServiceTestUtil.randomString());

		newDLFileVersion.setExtension(ServiceTestUtil.randomString());

		newDLFileVersion.setMimeType(ServiceTestUtil.randomString());

		newDLFileVersion.setTitle(ServiceTestUtil.randomString());

		newDLFileVersion.setDescription(ServiceTestUtil.randomString());

		newDLFileVersion.setChangeLog(ServiceTestUtil.randomString());

		newDLFileVersion.setExtraSettings(ServiceTestUtil.randomString());

		newDLFileVersion.setFileEntryTypeId(ServiceTestUtil.nextLong());

		newDLFileVersion.setVersion(ServiceTestUtil.randomString());

		newDLFileVersion.setSize(ServiceTestUtil.nextLong());

		newDLFileVersion.setChecksum(ServiceTestUtil.randomString());

		newDLFileVersion.setStatus(ServiceTestUtil.nextInt());

		newDLFileVersion.setStatusByUserId(ServiceTestUtil.nextLong());

		newDLFileVersion.setStatusByUserName(ServiceTestUtil.randomString());

		newDLFileVersion.setStatusDate(ServiceTestUtil.nextDate());

		_persistence.update(newDLFileVersion);

		DLFileVersion existingDLFileVersion = _persistence.findByPrimaryKey(newDLFileVersion.getPrimaryKey());

		Assert.assertEquals(existingDLFileVersion.getUuid(),
			newDLFileVersion.getUuid());
		Assert.assertEquals(existingDLFileVersion.getFileVersionId(),
			newDLFileVersion.getFileVersionId());
		Assert.assertEquals(existingDLFileVersion.getGroupId(),
			newDLFileVersion.getGroupId());
		Assert.assertEquals(existingDLFileVersion.getCompanyId(),
			newDLFileVersion.getCompanyId());
		Assert.assertEquals(existingDLFileVersion.getUserId(),
			newDLFileVersion.getUserId());
		Assert.assertEquals(existingDLFileVersion.getUserName(),
			newDLFileVersion.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingDLFileVersion.getCreateDate()),
			Time.getShortTimestamp(newDLFileVersion.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingDLFileVersion.getModifiedDate()),
			Time.getShortTimestamp(newDLFileVersion.getModifiedDate()));
		Assert.assertEquals(existingDLFileVersion.getRepositoryId(),
			newDLFileVersion.getRepositoryId());
		Assert.assertEquals(existingDLFileVersion.getFolderId(),
			newDLFileVersion.getFolderId());
		Assert.assertEquals(existingDLFileVersion.getFileEntryId(),
			newDLFileVersion.getFileEntryId());
		Assert.assertEquals(existingDLFileVersion.getTreePath(),
			newDLFileVersion.getTreePath());
		Assert.assertEquals(existingDLFileVersion.getExtension(),
			newDLFileVersion.getExtension());
		Assert.assertEquals(existingDLFileVersion.getMimeType(),
			newDLFileVersion.getMimeType());
		Assert.assertEquals(existingDLFileVersion.getTitle(),
			newDLFileVersion.getTitle());
		Assert.assertEquals(existingDLFileVersion.getDescription(),
			newDLFileVersion.getDescription());
		Assert.assertEquals(existingDLFileVersion.getChangeLog(),
			newDLFileVersion.getChangeLog());
		Assert.assertEquals(existingDLFileVersion.getExtraSettings(),
			newDLFileVersion.getExtraSettings());
		Assert.assertEquals(existingDLFileVersion.getFileEntryTypeId(),
			newDLFileVersion.getFileEntryTypeId());
		Assert.assertEquals(existingDLFileVersion.getVersion(),
			newDLFileVersion.getVersion());
		Assert.assertEquals(existingDLFileVersion.getSize(),
			newDLFileVersion.getSize());
		Assert.assertEquals(existingDLFileVersion.getChecksum(),
			newDLFileVersion.getChecksum());
		Assert.assertEquals(existingDLFileVersion.getStatus(),
			newDLFileVersion.getStatus());
		Assert.assertEquals(existingDLFileVersion.getStatusByUserId(),
			newDLFileVersion.getStatusByUserId());
		Assert.assertEquals(existingDLFileVersion.getStatusByUserName(),
			newDLFileVersion.getStatusByUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingDLFileVersion.getStatusDate()),
			Time.getShortTimestamp(newDLFileVersion.getStatusDate()));
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		DLFileVersion newDLFileVersion = addDLFileVersion();

		DLFileVersion existingDLFileVersion = _persistence.findByPrimaryKey(newDLFileVersion.getPrimaryKey());

		Assert.assertEquals(existingDLFileVersion, newDLFileVersion);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchFileVersionException");
		}
		catch (NoSuchFileVersionException nsee) {
		}
	}

	@Test
	public void testFindAll() throws Exception {
		try {
			_persistence.findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS,
				getOrderByComparator());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testFindByUuid() throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		String uuid = dlFileVersion.getUuid();

		List<DLFileVersion> dlFileVersions = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, dlFileVersions.size());

		Assert.assertEquals(dlFileVersion.getPrimaryKey(),
			dlFileVersions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addDLFileVersion();

		String uuid = ServiceTestUtil.randomString();

		List<DLFileVersion> dlFileVersions = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, dlFileVersions.size());
	}

	@Test
	public void testFindByUuidStartEnd() throws Exception {
		testFindByUuidStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByUuidStartEndWrongRange() throws Exception {
		testFindByUuidStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByUuidStartEndZeroZero() throws Exception {
		testFindByUuidStartEnd(0, 0, 0);
	}

	protected void testFindByUuidStartEnd(int start, int end, int expected)
		throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		String uuid = dlFileVersion.getUuid();

		List<DLFileVersion> dlFileVersions = _persistence.findByUuid(uuid,
				start, end);

		Assert.assertEquals(expected, dlFileVersions.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		String uuid = dlFileVersion.getUuid();

		long companyId = dlFileVersion.getCompanyId();

		List<DLFileVersion> dlFileVersions = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(1, dlFileVersions.size());

		Assert.assertEquals(dlFileVersion.getPrimaryKey(),
			dlFileVersions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addDLFileVersion();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<DLFileVersion> dlFileVersions = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(0, dlFileVersions.size());
	}

	@Test
	public void testFindByUuid_CStartEnd() throws Exception {
		testFindByUuid_CStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByUuid_CStartEndWrongRange() throws Exception {
		testFindByUuid_CStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByUuid_CStartEndZeroZero() throws Exception {
		testFindByUuid_CStartEnd(0, 0, 0);
	}

	protected void testFindByUuid_CStartEnd(int start, int end, int expected)
		throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		String uuid = dlFileVersion.getUuid();

		long companyId = dlFileVersion.getCompanyId();

		List<DLFileVersion> dlFileVersions = _persistence.findByUuid_C(uuid,
				companyId, start, end);

		Assert.assertEquals(expected, dlFileVersions.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		long companyId = dlFileVersion.getCompanyId();

		List<DLFileVersion> dlFileVersions = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, dlFileVersions.size());

		Assert.assertEquals(dlFileVersion.getPrimaryKey(),
			dlFileVersions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addDLFileVersion();

		long companyId = ServiceTestUtil.nextLong();

		List<DLFileVersion> dlFileVersions = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, dlFileVersions.size());
	}

	@Test
	public void testFindByCompanyIdStartEnd() throws Exception {
		testFindByCompanyIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByCompanyIdStartEndWrongRange()
		throws Exception {
		testFindByCompanyIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByCompanyIdStartEndZeroZero() throws Exception {
		testFindByCompanyIdStartEnd(0, 0, 0);
	}

	protected void testFindByCompanyIdStartEnd(int start, int end, int expected)
		throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		long companyId = dlFileVersion.getCompanyId();

		List<DLFileVersion> dlFileVersions = _persistence.findByCompanyId(companyId,
				start, end);

		Assert.assertEquals(expected, dlFileVersions.size());
	}

	@Test
	public void testFindByFileEntryId() throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		long fileEntryId = dlFileVersion.getFileEntryId();

		List<DLFileVersion> dlFileVersions = _persistence.findByFileEntryId(fileEntryId);

		Assert.assertEquals(1, dlFileVersions.size());

		Assert.assertEquals(dlFileVersion.getPrimaryKey(),
			dlFileVersions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByFileEntryIdNotFound() throws Exception {
		addDLFileVersion();

		long fileEntryId = ServiceTestUtil.nextLong();

		List<DLFileVersion> dlFileVersions = _persistence.findByFileEntryId(fileEntryId);

		Assert.assertEquals(0, dlFileVersions.size());
	}

	@Test
	public void testFindByFileEntryIdStartEnd() throws Exception {
		testFindByFileEntryIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByFileEntryIdStartEndWrongRange()
		throws Exception {
		testFindByFileEntryIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByFileEntryIdStartEndZeroZero()
		throws Exception {
		testFindByFileEntryIdStartEnd(0, 0, 0);
	}

	protected void testFindByFileEntryIdStartEnd(int start, int end,
		int expected) throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		long fileEntryId = dlFileVersion.getFileEntryId();

		List<DLFileVersion> dlFileVersions = _persistence.findByFileEntryId(fileEntryId,
				start, end);

		Assert.assertEquals(expected, dlFileVersions.size());
	}

	@Test
	public void testFindByMimeType() throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		String mimeType = dlFileVersion.getMimeType();

		List<DLFileVersion> dlFileVersions = _persistence.findByMimeType(mimeType);

		Assert.assertEquals(1, dlFileVersions.size());

		Assert.assertEquals(dlFileVersion.getPrimaryKey(),
			dlFileVersions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByMimeTypeNotFound() throws Exception {
		addDLFileVersion();

		String mimeType = ServiceTestUtil.randomString();

		List<DLFileVersion> dlFileVersions = _persistence.findByMimeType(mimeType);

		Assert.assertEquals(0, dlFileVersions.size());
	}

	@Test
	public void testFindByMimeTypeStartEnd() throws Exception {
		testFindByMimeTypeStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByMimeTypeStartEndWrongRange()
		throws Exception {
		testFindByMimeTypeStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByMimeTypeStartEndZeroZero() throws Exception {
		testFindByMimeTypeStartEnd(0, 0, 0);
	}

	protected void testFindByMimeTypeStartEnd(int start, int end, int expected)
		throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		String mimeType = dlFileVersion.getMimeType();

		List<DLFileVersion> dlFileVersions = _persistence.findByMimeType(mimeType,
				start, end);

		Assert.assertEquals(expected, dlFileVersions.size());
	}

	@Test
	public void testFindByC_NotS() throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		long companyId = dlFileVersion.getCompanyId();

		int status = dlFileVersion.getStatus();

		List<DLFileVersion> dlFileVersions = _persistence.findByC_NotS(companyId,
				status);

		Assert.assertEquals(1, dlFileVersions.size());

		Assert.assertEquals(dlFileVersion.getPrimaryKey(),
			dlFileVersions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_NotSNotFound() throws Exception {
		addDLFileVersion();

		long companyId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<DLFileVersion> dlFileVersions = _persistence.findByC_NotS(companyId,
				status);

		Assert.assertEquals(0, dlFileVersions.size());
	}

	@Test
	public void testFindByC_NotSStartEnd() throws Exception {
		testFindByC_NotSStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_NotSStartEndWrongRange() throws Exception {
		testFindByC_NotSStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_NotSStartEndZeroZero() throws Exception {
		testFindByC_NotSStartEnd(0, 0, 0);
	}

	protected void testFindByC_NotSStartEnd(int start, int end, int expected)
		throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		long companyId = dlFileVersion.getCompanyId();

		int status = dlFileVersion.getStatus();

		List<DLFileVersion> dlFileVersions = _persistence.findByC_NotS(companyId,
				status, start, end);

		Assert.assertEquals(expected, dlFileVersions.size());
	}

	@Test
	public void testFindByF_S() throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		long fileEntryId = dlFileVersion.getFileEntryId();

		int status = dlFileVersion.getStatus();

		List<DLFileVersion> dlFileVersions = _persistence.findByF_S(fileEntryId,
				status);

		Assert.assertEquals(1, dlFileVersions.size());

		Assert.assertEquals(dlFileVersion.getPrimaryKey(),
			dlFileVersions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByF_SNotFound() throws Exception {
		addDLFileVersion();

		long fileEntryId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<DLFileVersion> dlFileVersions = _persistence.findByF_S(fileEntryId,
				status);

		Assert.assertEquals(0, dlFileVersions.size());
	}

	@Test
	public void testFindByF_SStartEnd() throws Exception {
		testFindByF_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByF_SStartEndWrongRange() throws Exception {
		testFindByF_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByF_SStartEndZeroZero() throws Exception {
		testFindByF_SStartEnd(0, 0, 0);
	}

	protected void testFindByF_SStartEnd(int start, int end, int expected)
		throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		long fileEntryId = dlFileVersion.getFileEntryId();

		int status = dlFileVersion.getStatus();

		List<DLFileVersion> dlFileVersions = _persistence.findByF_S(fileEntryId,
				status, start, end);

		Assert.assertEquals(expected, dlFileVersions.size());
	}

	@Test
	public void testFindByG_F_S() throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		long groupId = dlFileVersion.getGroupId();

		long folderId = dlFileVersion.getFolderId();

		int status = dlFileVersion.getStatus();

		List<DLFileVersion> dlFileVersions = _persistence.findByG_F_S(groupId,
				folderId, status);

		Assert.assertEquals(1, dlFileVersions.size());

		Assert.assertEquals(dlFileVersion.getPrimaryKey(),
			dlFileVersions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_F_SNotFound() throws Exception {
		addDLFileVersion();

		long groupId = ServiceTestUtil.nextLong();

		long folderId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<DLFileVersion> dlFileVersions = _persistence.findByG_F_S(groupId,
				folderId, status);

		Assert.assertEquals(0, dlFileVersions.size());
	}

	@Test
	public void testFindByG_F_SStartEnd() throws Exception {
		testFindByG_F_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_F_SStartEndWrongRange() throws Exception {
		testFindByG_F_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_F_SStartEndZeroZero() throws Exception {
		testFindByG_F_SStartEnd(0, 0, 0);
	}

	protected void testFindByG_F_SStartEnd(int start, int end, int expected)
		throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		long groupId = dlFileVersion.getGroupId();

		long folderId = dlFileVersion.getFolderId();

		int status = dlFileVersion.getStatus();

		List<DLFileVersion> dlFileVersions = _persistence.findByG_F_S(groupId,
				folderId, status, start, end);

		Assert.assertEquals(expected, dlFileVersions.size());
	}

	@Test
	public void testFindByG_F_T_V() throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		long groupId = dlFileVersion.getGroupId();

		long folderId = dlFileVersion.getFolderId();

		String title = dlFileVersion.getTitle();

		String version = dlFileVersion.getVersion();

		List<DLFileVersion> dlFileVersions = _persistence.findByG_F_T_V(groupId,
				folderId, title, version);

		Assert.assertEquals(1, dlFileVersions.size());

		Assert.assertEquals(dlFileVersion.getPrimaryKey(),
			dlFileVersions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_F_T_VNotFound() throws Exception {
		addDLFileVersion();

		long groupId = ServiceTestUtil.nextLong();

		long folderId = ServiceTestUtil.nextLong();

		String title = ServiceTestUtil.randomString();

		String version = ServiceTestUtil.randomString();

		List<DLFileVersion> dlFileVersions = _persistence.findByG_F_T_V(groupId,
				folderId, title, version);

		Assert.assertEquals(0, dlFileVersions.size());
	}

	@Test
	public void testFindByG_F_T_VStartEnd() throws Exception {
		testFindByG_F_T_VStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_F_T_VStartEndWrongRange() throws Exception {
		testFindByG_F_T_VStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_F_T_VStartEndZeroZero() throws Exception {
		testFindByG_F_T_VStartEnd(0, 0, 0);
	}

	protected void testFindByG_F_T_VStartEnd(int start, int end, int expected)
		throws Exception {
		DLFileVersion dlFileVersion = addDLFileVersion();

		long groupId = dlFileVersion.getGroupId();

		long folderId = dlFileVersion.getFolderId();

		String title = dlFileVersion.getTitle();

		String version = dlFileVersion.getVersion();

		List<DLFileVersion> dlFileVersions = _persistence.findByG_F_T_V(groupId,
				folderId, title, version, start, end);

		Assert.assertEquals(expected, dlFileVersions.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("DLFileVersion", "uuid",
			true, "fileVersionId", true, "groupId", true, "companyId", true,
			"userId", true, "userName", true, "createDate", true,
			"modifiedDate", true, "repositoryId", true, "folderId", true,
			"fileEntryId", true, "treePath", true, "extension", true,
			"mimeType", true, "title", true, "description", true, "changeLog",
			true, "extraSettings", true, "fileEntryTypeId", true, "version",
			true, "size", true, "checksum", true, "status", true,
			"statusByUserId", true, "statusByUserName", true, "statusDate", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		DLFileVersion newDLFileVersion = addDLFileVersion();

		DLFileVersion existingDLFileVersion = _persistence.fetchByPrimaryKey(newDLFileVersion.getPrimaryKey());

		Assert.assertEquals(existingDLFileVersion, newDLFileVersion);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		DLFileVersion missingDLFileVersion = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingDLFileVersion);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new DLFileVersionActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					DLFileVersion dlFileVersion = (DLFileVersion)object;

					Assert.assertNotNull(dlFileVersion);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		DLFileVersion newDLFileVersion = addDLFileVersion();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DLFileVersion.class,
				DLFileVersion.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("fileVersionId",
				newDLFileVersion.getFileVersionId()));

		List<DLFileVersion> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		DLFileVersion existingDLFileVersion = result.get(0);

		Assert.assertEquals(existingDLFileVersion, newDLFileVersion);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DLFileVersion.class,
				DLFileVersion.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("fileVersionId",
				ServiceTestUtil.nextLong()));

		List<DLFileVersion> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		DLFileVersion newDLFileVersion = addDLFileVersion();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DLFileVersion.class,
				DLFileVersion.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"fileVersionId"));

		Object newFileVersionId = newDLFileVersion.getFileVersionId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("fileVersionId",
				new Object[] { newFileVersionId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingFileVersionId = result.get(0);

		Assert.assertEquals(existingFileVersionId, newFileVersionId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DLFileVersion.class,
				DLFileVersion.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"fileVersionId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("fileVersionId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		DLFileVersion newDLFileVersion = addDLFileVersion();

		_persistence.clearCache();

		DLFileVersionModelImpl existingDLFileVersionModelImpl = (DLFileVersionModelImpl)_persistence.findByPrimaryKey(newDLFileVersion.getPrimaryKey());

		Assert.assertTrue(Validator.equals(
				existingDLFileVersionModelImpl.getUuid(),
				existingDLFileVersionModelImpl.getOriginalUuid()));
		Assert.assertEquals(existingDLFileVersionModelImpl.getGroupId(),
			existingDLFileVersionModelImpl.getOriginalGroupId());

		Assert.assertEquals(existingDLFileVersionModelImpl.getFileEntryId(),
			existingDLFileVersionModelImpl.getOriginalFileEntryId());
		Assert.assertTrue(Validator.equals(
				existingDLFileVersionModelImpl.getVersion(),
				existingDLFileVersionModelImpl.getOriginalVersion()));
	}

	protected DLFileVersion addDLFileVersion() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		DLFileVersion dlFileVersion = _persistence.create(pk);

		dlFileVersion.setUuid(ServiceTestUtil.randomString());

		dlFileVersion.setGroupId(ServiceTestUtil.nextLong());

		dlFileVersion.setCompanyId(ServiceTestUtil.nextLong());

		dlFileVersion.setUserId(ServiceTestUtil.nextLong());

		dlFileVersion.setUserName(ServiceTestUtil.randomString());

		dlFileVersion.setCreateDate(ServiceTestUtil.nextDate());

		dlFileVersion.setModifiedDate(ServiceTestUtil.nextDate());

		dlFileVersion.setRepositoryId(ServiceTestUtil.nextLong());

		dlFileVersion.setFolderId(ServiceTestUtil.nextLong());

		dlFileVersion.setFileEntryId(ServiceTestUtil.nextLong());

		dlFileVersion.setTreePath(ServiceTestUtil.randomString());

		dlFileVersion.setExtension(ServiceTestUtil.randomString());

		dlFileVersion.setMimeType(ServiceTestUtil.randomString());

		dlFileVersion.setTitle(ServiceTestUtil.randomString());

		dlFileVersion.setDescription(ServiceTestUtil.randomString());

		dlFileVersion.setChangeLog(ServiceTestUtil.randomString());

		dlFileVersion.setExtraSettings(ServiceTestUtil.randomString());

		dlFileVersion.setFileEntryTypeId(ServiceTestUtil.nextLong());

		dlFileVersion.setVersion(ServiceTestUtil.randomString());

		dlFileVersion.setSize(ServiceTestUtil.nextLong());

		dlFileVersion.setChecksum(ServiceTestUtil.randomString());

		dlFileVersion.setStatus(ServiceTestUtil.nextInt());

		dlFileVersion.setStatusByUserId(ServiceTestUtil.nextLong());

		dlFileVersion.setStatusByUserName(ServiceTestUtil.randomString());

		dlFileVersion.setStatusDate(ServiceTestUtil.nextDate());

		_persistence.update(dlFileVersion);

		return dlFileVersion;
	}

	private static Log _log = LogFactoryUtil.getLog(DLFileVersionPersistenceTest.class);
	private DLFileVersionPersistence _persistence = (DLFileVersionPersistence)PortalBeanLocatorUtil.locate(DLFileVersionPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}