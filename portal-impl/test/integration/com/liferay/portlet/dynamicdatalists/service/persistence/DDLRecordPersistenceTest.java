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

package com.liferay.portlet.dynamicdatalists.service.persistence;

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

import com.liferay.portlet.dynamicdatalists.NoSuchRecordException;
import com.liferay.portlet.dynamicdatalists.model.DDLRecord;
import com.liferay.portlet.dynamicdatalists.model.impl.DDLRecordModelImpl;

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
public class DDLRecordPersistenceTest {
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

		DDLRecord ddlRecord = _persistence.create(pk);

		Assert.assertNotNull(ddlRecord);

		Assert.assertEquals(ddlRecord.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		DDLRecord newDDLRecord = addDDLRecord();

		_persistence.remove(newDDLRecord);

		DDLRecord existingDDLRecord = _persistence.fetchByPrimaryKey(newDDLRecord.getPrimaryKey());

		Assert.assertNull(existingDDLRecord);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addDDLRecord();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		DDLRecord newDDLRecord = _persistence.create(pk);

		newDDLRecord.setUuid(ServiceTestUtil.randomString());

		newDDLRecord.setGroupId(ServiceTestUtil.nextLong());

		newDDLRecord.setCompanyId(ServiceTestUtil.nextLong());

		newDDLRecord.setUserId(ServiceTestUtil.nextLong());

		newDDLRecord.setUserName(ServiceTestUtil.randomString());

		newDDLRecord.setVersionUserId(ServiceTestUtil.nextLong());

		newDDLRecord.setVersionUserName(ServiceTestUtil.randomString());

		newDDLRecord.setCreateDate(ServiceTestUtil.nextDate());

		newDDLRecord.setModifiedDate(ServiceTestUtil.nextDate());

		newDDLRecord.setDDMStorageId(ServiceTestUtil.nextLong());

		newDDLRecord.setRecordSetId(ServiceTestUtil.nextLong());

		newDDLRecord.setVersion(ServiceTestUtil.randomString());

		newDDLRecord.setDisplayIndex(ServiceTestUtil.nextInt());

		_persistence.update(newDDLRecord);

		DDLRecord existingDDLRecord = _persistence.findByPrimaryKey(newDDLRecord.getPrimaryKey());

		Assert.assertEquals(existingDDLRecord.getUuid(), newDDLRecord.getUuid());
		Assert.assertEquals(existingDDLRecord.getRecordId(),
			newDDLRecord.getRecordId());
		Assert.assertEquals(existingDDLRecord.getGroupId(),
			newDDLRecord.getGroupId());
		Assert.assertEquals(existingDDLRecord.getCompanyId(),
			newDDLRecord.getCompanyId());
		Assert.assertEquals(existingDDLRecord.getUserId(),
			newDDLRecord.getUserId());
		Assert.assertEquals(existingDDLRecord.getUserName(),
			newDDLRecord.getUserName());
		Assert.assertEquals(existingDDLRecord.getVersionUserId(),
			newDDLRecord.getVersionUserId());
		Assert.assertEquals(existingDDLRecord.getVersionUserName(),
			newDDLRecord.getVersionUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingDDLRecord.getCreateDate()),
			Time.getShortTimestamp(newDDLRecord.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingDDLRecord.getModifiedDate()),
			Time.getShortTimestamp(newDDLRecord.getModifiedDate()));
		Assert.assertEquals(existingDDLRecord.getDDMStorageId(),
			newDDLRecord.getDDMStorageId());
		Assert.assertEquals(existingDDLRecord.getRecordSetId(),
			newDDLRecord.getRecordSetId());
		Assert.assertEquals(existingDDLRecord.getVersion(),
			newDDLRecord.getVersion());
		Assert.assertEquals(existingDDLRecord.getDisplayIndex(),
			newDDLRecord.getDisplayIndex());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		DDLRecord newDDLRecord = addDDLRecord();

		DDLRecord existingDDLRecord = _persistence.findByPrimaryKey(newDDLRecord.getPrimaryKey());

		Assert.assertEquals(existingDDLRecord, newDDLRecord);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchRecordException");
		}
		catch (NoSuchRecordException nsee) {
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
		DDLRecord ddlRecord = addDDLRecord();

		String uuid = ddlRecord.getUuid();

		List<DDLRecord> ddlRecords = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, ddlRecords.size());

		Assert.assertEquals(ddlRecord.getPrimaryKey(),
			ddlRecords.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addDDLRecord();

		String uuid = ServiceTestUtil.randomString();

		List<DDLRecord> ddlRecords = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, ddlRecords.size());
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
		DDLRecord ddlRecord = addDDLRecord();

		String uuid = ddlRecord.getUuid();

		List<DDLRecord> ddlRecords = _persistence.findByUuid(uuid, start, end);

		Assert.assertEquals(expected, ddlRecords.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		DDLRecord ddlRecord = addDDLRecord();

		String uuid = ddlRecord.getUuid();

		long companyId = ddlRecord.getCompanyId();

		List<DDLRecord> ddlRecords = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(1, ddlRecords.size());

		Assert.assertEquals(ddlRecord.getPrimaryKey(),
			ddlRecords.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addDDLRecord();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<DDLRecord> ddlRecords = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(0, ddlRecords.size());
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
		DDLRecord ddlRecord = addDDLRecord();

		String uuid = ddlRecord.getUuid();

		long companyId = ddlRecord.getCompanyId();

		List<DDLRecord> ddlRecords = _persistence.findByUuid_C(uuid, companyId,
				start, end);

		Assert.assertEquals(expected, ddlRecords.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		DDLRecord ddlRecord = addDDLRecord();

		long companyId = ddlRecord.getCompanyId();

		List<DDLRecord> ddlRecords = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, ddlRecords.size());

		Assert.assertEquals(ddlRecord.getPrimaryKey(),
			ddlRecords.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addDDLRecord();

		long companyId = ServiceTestUtil.nextLong();

		List<DDLRecord> ddlRecords = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, ddlRecords.size());
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
		DDLRecord ddlRecord = addDDLRecord();

		long companyId = ddlRecord.getCompanyId();

		List<DDLRecord> ddlRecords = _persistence.findByCompanyId(companyId,
				start, end);

		Assert.assertEquals(expected, ddlRecords.size());
	}

	@Test
	public void testFindByRecordSetId() throws Exception {
		DDLRecord ddlRecord = addDDLRecord();

		long recordSetId = ddlRecord.getRecordSetId();

		List<DDLRecord> ddlRecords = _persistence.findByRecordSetId(recordSetId);

		Assert.assertEquals(1, ddlRecords.size());

		Assert.assertEquals(ddlRecord.getPrimaryKey(),
			ddlRecords.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByRecordSetIdNotFound() throws Exception {
		addDDLRecord();

		long recordSetId = ServiceTestUtil.nextLong();

		List<DDLRecord> ddlRecords = _persistence.findByRecordSetId(recordSetId);

		Assert.assertEquals(0, ddlRecords.size());
	}

	@Test
	public void testFindByRecordSetIdStartEnd() throws Exception {
		testFindByRecordSetIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByRecordSetIdStartEndWrongRange()
		throws Exception {
		testFindByRecordSetIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByRecordSetIdStartEndZeroZero()
		throws Exception {
		testFindByRecordSetIdStartEnd(0, 0, 0);
	}

	protected void testFindByRecordSetIdStartEnd(int start, int end,
		int expected) throws Exception {
		DDLRecord ddlRecord = addDDLRecord();

		long recordSetId = ddlRecord.getRecordSetId();

		List<DDLRecord> ddlRecords = _persistence.findByRecordSetId(recordSetId,
				start, end);

		Assert.assertEquals(expected, ddlRecords.size());
	}

	@Test
	public void testFindByR_U() throws Exception {
		DDLRecord ddlRecord = addDDLRecord();

		long recordSetId = ddlRecord.getRecordSetId();

		long userId = ddlRecord.getUserId();

		List<DDLRecord> ddlRecords = _persistence.findByR_U(recordSetId, userId);

		Assert.assertEquals(1, ddlRecords.size());

		Assert.assertEquals(ddlRecord.getPrimaryKey(),
			ddlRecords.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByR_UNotFound() throws Exception {
		addDDLRecord();

		long recordSetId = ServiceTestUtil.nextLong();

		long userId = ServiceTestUtil.nextLong();

		List<DDLRecord> ddlRecords = _persistence.findByR_U(recordSetId, userId);

		Assert.assertEquals(0, ddlRecords.size());
	}

	@Test
	public void testFindByR_UStartEnd() throws Exception {
		testFindByR_UStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByR_UStartEndWrongRange() throws Exception {
		testFindByR_UStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByR_UStartEndZeroZero() throws Exception {
		testFindByR_UStartEnd(0, 0, 0);
	}

	protected void testFindByR_UStartEnd(int start, int end, int expected)
		throws Exception {
		DDLRecord ddlRecord = addDDLRecord();

		long recordSetId = ddlRecord.getRecordSetId();

		long userId = ddlRecord.getUserId();

		List<DDLRecord> ddlRecords = _persistence.findByR_U(recordSetId,
				userId, start, end);

		Assert.assertEquals(expected, ddlRecords.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("DDLRecord", "uuid", true,
			"recordId", true, "groupId", true, "companyId", true, "userId",
			true, "userName", true, "versionUserId", true, "versionUserName",
			true, "createDate", true, "modifiedDate", true, "DDMStorageId",
			true, "recordSetId", true, "version", true, "displayIndex", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		DDLRecord newDDLRecord = addDDLRecord();

		DDLRecord existingDDLRecord = _persistence.fetchByPrimaryKey(newDDLRecord.getPrimaryKey());

		Assert.assertEquals(existingDDLRecord, newDDLRecord);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		DDLRecord missingDDLRecord = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingDDLRecord);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new DDLRecordActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					DDLRecord ddlRecord = (DDLRecord)object;

					Assert.assertNotNull(ddlRecord);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		DDLRecord newDDLRecord = addDDLRecord();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DDLRecord.class,
				DDLRecord.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("recordId",
				newDDLRecord.getRecordId()));

		List<DDLRecord> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		DDLRecord existingDDLRecord = result.get(0);

		Assert.assertEquals(existingDDLRecord, newDDLRecord);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DDLRecord.class,
				DDLRecord.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("recordId",
				ServiceTestUtil.nextLong()));

		List<DDLRecord> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		DDLRecord newDDLRecord = addDDLRecord();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DDLRecord.class,
				DDLRecord.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("recordId"));

		Object newRecordId = newDDLRecord.getRecordId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("recordId",
				new Object[] { newRecordId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingRecordId = result.get(0);

		Assert.assertEquals(existingRecordId, newRecordId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DDLRecord.class,
				DDLRecord.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("recordId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("recordId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		DDLRecord newDDLRecord = addDDLRecord();

		_persistence.clearCache();

		DDLRecordModelImpl existingDDLRecordModelImpl = (DDLRecordModelImpl)_persistence.findByPrimaryKey(newDDLRecord.getPrimaryKey());

		Assert.assertTrue(Validator.equals(
				existingDDLRecordModelImpl.getUuid(),
				existingDDLRecordModelImpl.getOriginalUuid()));
		Assert.assertEquals(existingDDLRecordModelImpl.getGroupId(),
			existingDDLRecordModelImpl.getOriginalGroupId());
	}

	protected DDLRecord addDDLRecord() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		DDLRecord ddlRecord = _persistence.create(pk);

		ddlRecord.setUuid(ServiceTestUtil.randomString());

		ddlRecord.setGroupId(ServiceTestUtil.nextLong());

		ddlRecord.setCompanyId(ServiceTestUtil.nextLong());

		ddlRecord.setUserId(ServiceTestUtil.nextLong());

		ddlRecord.setUserName(ServiceTestUtil.randomString());

		ddlRecord.setVersionUserId(ServiceTestUtil.nextLong());

		ddlRecord.setVersionUserName(ServiceTestUtil.randomString());

		ddlRecord.setCreateDate(ServiceTestUtil.nextDate());

		ddlRecord.setModifiedDate(ServiceTestUtil.nextDate());

		ddlRecord.setDDMStorageId(ServiceTestUtil.nextLong());

		ddlRecord.setRecordSetId(ServiceTestUtil.nextLong());

		ddlRecord.setVersion(ServiceTestUtil.randomString());

		ddlRecord.setDisplayIndex(ServiceTestUtil.nextInt());

		_persistence.update(ddlRecord);

		return ddlRecord;
	}

	private static Log _log = LogFactoryUtil.getLog(DDLRecordPersistenceTest.class);
	private DDLRecordPersistence _persistence = (DDLRecordPersistence)PortalBeanLocatorUtil.locate(DDLRecordPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}