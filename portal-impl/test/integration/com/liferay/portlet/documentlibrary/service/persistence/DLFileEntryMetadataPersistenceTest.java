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
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;
import com.liferay.portal.util.PropsValues;

import com.liferay.portlet.documentlibrary.NoSuchFileEntryMetadataException;
import com.liferay.portlet.documentlibrary.model.DLFileEntryMetadata;
import com.liferay.portlet.documentlibrary.model.impl.DLFileEntryMetadataModelImpl;

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
public class DLFileEntryMetadataPersistenceTest {
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

		DLFileEntryMetadata dlFileEntryMetadata = _persistence.create(pk);

		Assert.assertNotNull(dlFileEntryMetadata);

		Assert.assertEquals(dlFileEntryMetadata.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		DLFileEntryMetadata newDLFileEntryMetadata = addDLFileEntryMetadata();

		_persistence.remove(newDLFileEntryMetadata);

		DLFileEntryMetadata existingDLFileEntryMetadata = _persistence.fetchByPrimaryKey(newDLFileEntryMetadata.getPrimaryKey());

		Assert.assertNull(existingDLFileEntryMetadata);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addDLFileEntryMetadata();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		DLFileEntryMetadata newDLFileEntryMetadata = _persistence.create(pk);

		newDLFileEntryMetadata.setUuid(ServiceTestUtil.randomString());

		newDLFileEntryMetadata.setDDMStorageId(ServiceTestUtil.nextLong());

		newDLFileEntryMetadata.setDDMStructureId(ServiceTestUtil.nextLong());

		newDLFileEntryMetadata.setFileEntryTypeId(ServiceTestUtil.nextLong());

		newDLFileEntryMetadata.setFileEntryId(ServiceTestUtil.nextLong());

		newDLFileEntryMetadata.setFileVersionId(ServiceTestUtil.nextLong());

		_persistence.update(newDLFileEntryMetadata);

		DLFileEntryMetadata existingDLFileEntryMetadata = _persistence.findByPrimaryKey(newDLFileEntryMetadata.getPrimaryKey());

		Assert.assertEquals(existingDLFileEntryMetadata.getUuid(),
			newDLFileEntryMetadata.getUuid());
		Assert.assertEquals(existingDLFileEntryMetadata.getFileEntryMetadataId(),
			newDLFileEntryMetadata.getFileEntryMetadataId());
		Assert.assertEquals(existingDLFileEntryMetadata.getDDMStorageId(),
			newDLFileEntryMetadata.getDDMStorageId());
		Assert.assertEquals(existingDLFileEntryMetadata.getDDMStructureId(),
			newDLFileEntryMetadata.getDDMStructureId());
		Assert.assertEquals(existingDLFileEntryMetadata.getFileEntryTypeId(),
			newDLFileEntryMetadata.getFileEntryTypeId());
		Assert.assertEquals(existingDLFileEntryMetadata.getFileEntryId(),
			newDLFileEntryMetadata.getFileEntryId());
		Assert.assertEquals(existingDLFileEntryMetadata.getFileVersionId(),
			newDLFileEntryMetadata.getFileVersionId());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		DLFileEntryMetadata newDLFileEntryMetadata = addDLFileEntryMetadata();

		DLFileEntryMetadata existingDLFileEntryMetadata = _persistence.findByPrimaryKey(newDLFileEntryMetadata.getPrimaryKey());

		Assert.assertEquals(existingDLFileEntryMetadata, newDLFileEntryMetadata);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchFileEntryMetadataException");
		}
		catch (NoSuchFileEntryMetadataException nsee) {
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
		DLFileEntryMetadata dlFileEntryMetadata = addDLFileEntryMetadata();

		String uuid = dlFileEntryMetadata.getUuid();

		List<DLFileEntryMetadata> dlFileEntryMetadatas = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, dlFileEntryMetadatas.size());

		Assert.assertEquals(dlFileEntryMetadata.getPrimaryKey(),
			dlFileEntryMetadatas.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addDLFileEntryMetadata();

		String uuid = ServiceTestUtil.randomString();

		List<DLFileEntryMetadata> dlFileEntryMetadatas = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, dlFileEntryMetadatas.size());
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
		DLFileEntryMetadata dlFileEntryMetadata = addDLFileEntryMetadata();

		String uuid = dlFileEntryMetadata.getUuid();

		List<DLFileEntryMetadata> dlFileEntryMetadatas = _persistence.findByUuid(uuid,
				start, end);

		Assert.assertEquals(expected, dlFileEntryMetadatas.size());
	}

	@Test
	public void testFindByFileEntryTypeId() throws Exception {
		DLFileEntryMetadata dlFileEntryMetadata = addDLFileEntryMetadata();

		long fileEntryTypeId = dlFileEntryMetadata.getFileEntryTypeId();

		List<DLFileEntryMetadata> dlFileEntryMetadatas = _persistence.findByFileEntryTypeId(fileEntryTypeId);

		Assert.assertEquals(1, dlFileEntryMetadatas.size());

		Assert.assertEquals(dlFileEntryMetadata.getPrimaryKey(),
			dlFileEntryMetadatas.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByFileEntryTypeIdNotFound() throws Exception {
		addDLFileEntryMetadata();

		long fileEntryTypeId = ServiceTestUtil.nextLong();

		List<DLFileEntryMetadata> dlFileEntryMetadatas = _persistence.findByFileEntryTypeId(fileEntryTypeId);

		Assert.assertEquals(0, dlFileEntryMetadatas.size());
	}

	@Test
	public void testFindByFileEntryTypeIdStartEnd() throws Exception {
		testFindByFileEntryTypeIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByFileEntryTypeIdStartEndWrongRange()
		throws Exception {
		testFindByFileEntryTypeIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByFileEntryTypeIdStartEndZeroZero()
		throws Exception {
		testFindByFileEntryTypeIdStartEnd(0, 0, 0);
	}

	protected void testFindByFileEntryTypeIdStartEnd(int start, int end,
		int expected) throws Exception {
		DLFileEntryMetadata dlFileEntryMetadata = addDLFileEntryMetadata();

		long fileEntryTypeId = dlFileEntryMetadata.getFileEntryTypeId();

		List<DLFileEntryMetadata> dlFileEntryMetadatas = _persistence.findByFileEntryTypeId(fileEntryTypeId,
				start, end);

		Assert.assertEquals(expected, dlFileEntryMetadatas.size());
	}

	@Test
	public void testFindByFileEntryId() throws Exception {
		DLFileEntryMetadata dlFileEntryMetadata = addDLFileEntryMetadata();

		long fileEntryId = dlFileEntryMetadata.getFileEntryId();

		List<DLFileEntryMetadata> dlFileEntryMetadatas = _persistence.findByFileEntryId(fileEntryId);

		Assert.assertEquals(1, dlFileEntryMetadatas.size());

		Assert.assertEquals(dlFileEntryMetadata.getPrimaryKey(),
			dlFileEntryMetadatas.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByFileEntryIdNotFound() throws Exception {
		addDLFileEntryMetadata();

		long fileEntryId = ServiceTestUtil.nextLong();

		List<DLFileEntryMetadata> dlFileEntryMetadatas = _persistence.findByFileEntryId(fileEntryId);

		Assert.assertEquals(0, dlFileEntryMetadatas.size());
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
		DLFileEntryMetadata dlFileEntryMetadata = addDLFileEntryMetadata();

		long fileEntryId = dlFileEntryMetadata.getFileEntryId();

		List<DLFileEntryMetadata> dlFileEntryMetadatas = _persistence.findByFileEntryId(fileEntryId,
				start, end);

		Assert.assertEquals(expected, dlFileEntryMetadatas.size());
	}

	@Test
	public void testFindByFileVersionId() throws Exception {
		DLFileEntryMetadata dlFileEntryMetadata = addDLFileEntryMetadata();

		long fileVersionId = dlFileEntryMetadata.getFileVersionId();

		List<DLFileEntryMetadata> dlFileEntryMetadatas = _persistence.findByFileVersionId(fileVersionId);

		Assert.assertEquals(1, dlFileEntryMetadatas.size());

		Assert.assertEquals(dlFileEntryMetadata.getPrimaryKey(),
			dlFileEntryMetadatas.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByFileVersionIdNotFound() throws Exception {
		addDLFileEntryMetadata();

		long fileVersionId = ServiceTestUtil.nextLong();

		List<DLFileEntryMetadata> dlFileEntryMetadatas = _persistence.findByFileVersionId(fileVersionId);

		Assert.assertEquals(0, dlFileEntryMetadatas.size());
	}

	@Test
	public void testFindByFileVersionIdStartEnd() throws Exception {
		testFindByFileVersionIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByFileVersionIdStartEndWrongRange()
		throws Exception {
		testFindByFileVersionIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByFileVersionIdStartEndZeroZero()
		throws Exception {
		testFindByFileVersionIdStartEnd(0, 0, 0);
	}

	protected void testFindByFileVersionIdStartEnd(int start, int end,
		int expected) throws Exception {
		DLFileEntryMetadata dlFileEntryMetadata = addDLFileEntryMetadata();

		long fileVersionId = dlFileEntryMetadata.getFileVersionId();

		List<DLFileEntryMetadata> dlFileEntryMetadatas = _persistence.findByFileVersionId(fileVersionId,
				start, end);

		Assert.assertEquals(expected, dlFileEntryMetadatas.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("DLFileEntryMetadata",
			"uuid", true, "fileEntryMetadataId", true, "DDMStorageId", true,
			"DDMStructureId", true, "fileEntryTypeId", true, "fileEntryId",
			true, "fileVersionId", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		DLFileEntryMetadata newDLFileEntryMetadata = addDLFileEntryMetadata();

		DLFileEntryMetadata existingDLFileEntryMetadata = _persistence.fetchByPrimaryKey(newDLFileEntryMetadata.getPrimaryKey());

		Assert.assertEquals(existingDLFileEntryMetadata, newDLFileEntryMetadata);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		DLFileEntryMetadata missingDLFileEntryMetadata = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingDLFileEntryMetadata);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new DLFileEntryMetadataActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					DLFileEntryMetadata dlFileEntryMetadata = (DLFileEntryMetadata)object;

					Assert.assertNotNull(dlFileEntryMetadata);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		DLFileEntryMetadata newDLFileEntryMetadata = addDLFileEntryMetadata();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DLFileEntryMetadata.class,
				DLFileEntryMetadata.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("fileEntryMetadataId",
				newDLFileEntryMetadata.getFileEntryMetadataId()));

		List<DLFileEntryMetadata> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		DLFileEntryMetadata existingDLFileEntryMetadata = result.get(0);

		Assert.assertEquals(existingDLFileEntryMetadata, newDLFileEntryMetadata);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DLFileEntryMetadata.class,
				DLFileEntryMetadata.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("fileEntryMetadataId",
				ServiceTestUtil.nextLong()));

		List<DLFileEntryMetadata> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		DLFileEntryMetadata newDLFileEntryMetadata = addDLFileEntryMetadata();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DLFileEntryMetadata.class,
				DLFileEntryMetadata.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"fileEntryMetadataId"));

		Object newFileEntryMetadataId = newDLFileEntryMetadata.getFileEntryMetadataId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("fileEntryMetadataId",
				new Object[] { newFileEntryMetadataId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingFileEntryMetadataId = result.get(0);

		Assert.assertEquals(existingFileEntryMetadataId, newFileEntryMetadataId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DLFileEntryMetadata.class,
				DLFileEntryMetadata.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"fileEntryMetadataId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("fileEntryMetadataId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		DLFileEntryMetadata newDLFileEntryMetadata = addDLFileEntryMetadata();

		_persistence.clearCache();

		DLFileEntryMetadataModelImpl existingDLFileEntryMetadataModelImpl = (DLFileEntryMetadataModelImpl)_persistence.findByPrimaryKey(newDLFileEntryMetadata.getPrimaryKey());

		Assert.assertEquals(existingDLFileEntryMetadataModelImpl.getDDMStructureId(),
			existingDLFileEntryMetadataModelImpl.getOriginalDDMStructureId());
		Assert.assertEquals(existingDLFileEntryMetadataModelImpl.getFileVersionId(),
			existingDLFileEntryMetadataModelImpl.getOriginalFileVersionId());
	}

	protected DLFileEntryMetadata addDLFileEntryMetadata()
		throws Exception {
		long pk = ServiceTestUtil.nextLong();

		DLFileEntryMetadata dlFileEntryMetadata = _persistence.create(pk);

		dlFileEntryMetadata.setUuid(ServiceTestUtil.randomString());

		dlFileEntryMetadata.setDDMStorageId(ServiceTestUtil.nextLong());

		dlFileEntryMetadata.setDDMStructureId(ServiceTestUtil.nextLong());

		dlFileEntryMetadata.setFileEntryTypeId(ServiceTestUtil.nextLong());

		dlFileEntryMetadata.setFileEntryId(ServiceTestUtil.nextLong());

		dlFileEntryMetadata.setFileVersionId(ServiceTestUtil.nextLong());

		_persistence.update(dlFileEntryMetadata);

		return dlFileEntryMetadata;
	}

	private static Log _log = LogFactoryUtil.getLog(DLFileEntryMetadataPersistenceTest.class);
	private DLFileEntryMetadataPersistence _persistence = (DLFileEntryMetadataPersistence)PortalBeanLocatorUtil.locate(DLFileEntryMetadataPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}