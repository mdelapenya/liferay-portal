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

package com.liferay.portlet.trash.service.persistence;

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
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;
import com.liferay.portal.util.PropsValues;

import com.liferay.portlet.trash.NoSuchEntryException;
import com.liferay.portlet.trash.model.TrashEntry;
import com.liferay.portlet.trash.model.impl.TrashEntryModelImpl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 */
@ExecutionTestListeners(listeners =  {
	PersistenceExecutionTestListener.class})
@RunWith(LiferayPersistenceIntegrationJUnitTestRunner.class)
public class TrashEntryPersistenceTest {
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

		TrashEntry trashEntry = _persistence.create(pk);

		Assert.assertNotNull(trashEntry);

		Assert.assertEquals(trashEntry.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		TrashEntry newTrashEntry = addTrashEntry();

		_persistence.remove(newTrashEntry);

		TrashEntry existingTrashEntry = _persistence.fetchByPrimaryKey(newTrashEntry.getPrimaryKey());

		Assert.assertNull(existingTrashEntry);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addTrashEntry();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		TrashEntry newTrashEntry = _persistence.create(pk);

		newTrashEntry.setGroupId(ServiceTestUtil.nextLong());

		newTrashEntry.setCompanyId(ServiceTestUtil.nextLong());

		newTrashEntry.setUserId(ServiceTestUtil.nextLong());

		newTrashEntry.setUserName(ServiceTestUtil.randomString());

		newTrashEntry.setCreateDate(ServiceTestUtil.nextDate());

		newTrashEntry.setClassNameId(ServiceTestUtil.nextLong());

		newTrashEntry.setClassPK(ServiceTestUtil.nextLong());

		newTrashEntry.setSystemEventSetKey(ServiceTestUtil.nextLong());

		newTrashEntry.setTypeSettings(ServiceTestUtil.randomString());

		newTrashEntry.setStatus(ServiceTestUtil.nextInt());

		_persistence.update(newTrashEntry);

		TrashEntry existingTrashEntry = _persistence.findByPrimaryKey(newTrashEntry.getPrimaryKey());

		Assert.assertEquals(existingTrashEntry.getEntryId(),
			newTrashEntry.getEntryId());
		Assert.assertEquals(existingTrashEntry.getGroupId(),
			newTrashEntry.getGroupId());
		Assert.assertEquals(existingTrashEntry.getCompanyId(),
			newTrashEntry.getCompanyId());
		Assert.assertEquals(existingTrashEntry.getUserId(),
			newTrashEntry.getUserId());
		Assert.assertEquals(existingTrashEntry.getUserName(),
			newTrashEntry.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingTrashEntry.getCreateDate()),
			Time.getShortTimestamp(newTrashEntry.getCreateDate()));
		Assert.assertEquals(existingTrashEntry.getClassNameId(),
			newTrashEntry.getClassNameId());
		Assert.assertEquals(existingTrashEntry.getClassPK(),
			newTrashEntry.getClassPK());
		Assert.assertEquals(existingTrashEntry.getSystemEventSetKey(),
			newTrashEntry.getSystemEventSetKey());
		Assert.assertEquals(existingTrashEntry.getTypeSettings(),
			newTrashEntry.getTypeSettings());
		Assert.assertEquals(existingTrashEntry.getStatus(),
			newTrashEntry.getStatus());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		TrashEntry newTrashEntry = addTrashEntry();

		TrashEntry existingTrashEntry = _persistence.findByPrimaryKey(newTrashEntry.getPrimaryKey());

		Assert.assertEquals(existingTrashEntry, newTrashEntry);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchEntryException");
		}
		catch (NoSuchEntryException nsee) {
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
	public void testFindByGroupId() throws Exception {
		TrashEntry trashEntry = addTrashEntry();

		long groupId = trashEntry.getGroupId();

		List<TrashEntry> trashEntries = _persistence.findByGroupId(groupId);

		Assert.assertEquals(1, trashEntries.size());

		Assert.assertEquals(trashEntry.getPrimaryKey(),
			trashEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByGroupIdNotFound() throws Exception {
		addTrashEntry();

		long groupId = ServiceTestUtil.nextLong();

		List<TrashEntry> trashEntries = _persistence.findByGroupId(groupId);

		Assert.assertEquals(0, trashEntries.size());
	}

	@Test
	public void testFindByGroupIdStartEnd() throws Exception {
		testFindByGroupIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByGroupIdStartEndWrongRange() throws Exception {
		testFindByGroupIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByGroupIdStartEndZeroZero() throws Exception {
		testFindByGroupIdStartEnd(0, 0, 0);
	}

	protected void testFindByGroupIdStartEnd(int start, int end, int expected)
		throws Exception {
		TrashEntry trashEntry = addTrashEntry();

		long groupId = trashEntry.getGroupId();

		List<TrashEntry> trashEntries = _persistence.findByGroupId(groupId,
				start, end);

		Assert.assertEquals(expected, trashEntries.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		TrashEntry trashEntry = addTrashEntry();

		long companyId = trashEntry.getCompanyId();

		List<TrashEntry> trashEntries = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, trashEntries.size());

		Assert.assertEquals(trashEntry.getPrimaryKey(),
			trashEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addTrashEntry();

		long companyId = ServiceTestUtil.nextLong();

		List<TrashEntry> trashEntries = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, trashEntries.size());
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
		TrashEntry trashEntry = addTrashEntry();

		long companyId = trashEntry.getCompanyId();

		List<TrashEntry> trashEntries = _persistence.findByCompanyId(companyId,
				start, end);

		Assert.assertEquals(expected, trashEntries.size());
	}

	@Test
	public void testFindByG_LtCD() throws Exception {
		TrashEntry trashEntry = addTrashEntry();

		long groupId = trashEntry.getGroupId();

		Date createDate = trashEntry.getCreateDate();

		List<TrashEntry> trashEntries = _persistence.findByG_LtCD(groupId,
				createDate);

		Assert.assertEquals(1, trashEntries.size());

		Assert.assertEquals(trashEntry.getPrimaryKey(),
			trashEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_LtCDNotFound() throws Exception {
		addTrashEntry();

		long groupId = ServiceTestUtil.nextLong();

		Date createDate = ServiceTestUtil.nextDate();

		List<TrashEntry> trashEntries = _persistence.findByG_LtCD(groupId,
				createDate);

		Assert.assertEquals(0, trashEntries.size());
	}

	@Test
	public void testFindByG_LtCDStartEnd() throws Exception {
		testFindByG_LtCDStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_LtCDStartEndWrongRange() throws Exception {
		testFindByG_LtCDStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_LtCDStartEndZeroZero() throws Exception {
		testFindByG_LtCDStartEnd(0, 0, 0);
	}

	protected void testFindByG_LtCDStartEnd(int start, int end, int expected)
		throws Exception {
		TrashEntry trashEntry = addTrashEntry();

		long groupId = trashEntry.getGroupId();

		Date createDate = trashEntry.getCreateDate();

		List<TrashEntry> trashEntries = _persistence.findByG_LtCD(groupId,
				createDate, start, end);

		Assert.assertEquals(expected, trashEntries.size());
	}

	@Test
	public void testFindByG_C() throws Exception {
		TrashEntry trashEntry = addTrashEntry();

		long groupId = trashEntry.getGroupId();

		long classNameId = trashEntry.getClassNameId();

		List<TrashEntry> trashEntries = _persistence.findByG_C(groupId,
				classNameId);

		Assert.assertEquals(1, trashEntries.size());

		Assert.assertEquals(trashEntry.getPrimaryKey(),
			trashEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_CNotFound() throws Exception {
		addTrashEntry();

		long groupId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		List<TrashEntry> trashEntries = _persistence.findByG_C(groupId,
				classNameId);

		Assert.assertEquals(0, trashEntries.size());
	}

	@Test
	public void testFindByG_CStartEnd() throws Exception {
		testFindByG_CStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_CStartEndWrongRange() throws Exception {
		testFindByG_CStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_CStartEndZeroZero() throws Exception {
		testFindByG_CStartEnd(0, 0, 0);
	}

	protected void testFindByG_CStartEnd(int start, int end, int expected)
		throws Exception {
		TrashEntry trashEntry = addTrashEntry();

		long groupId = trashEntry.getGroupId();

		long classNameId = trashEntry.getClassNameId();

		List<TrashEntry> trashEntries = _persistence.findByG_C(groupId,
				classNameId, start, end);

		Assert.assertEquals(expected, trashEntries.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("TrashEntry", "entryId",
			true, "groupId", true, "companyId", true, "userId", true,
			"userName", true, "createDate", true, "classNameId", true,
			"classPK", true, "systemEventSetKey", true, "typeSettings", true,
			"status", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		TrashEntry newTrashEntry = addTrashEntry();

		TrashEntry existingTrashEntry = _persistence.fetchByPrimaryKey(newTrashEntry.getPrimaryKey());

		Assert.assertEquals(existingTrashEntry, newTrashEntry);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		TrashEntry missingTrashEntry = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingTrashEntry);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new TrashEntryActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					TrashEntry trashEntry = (TrashEntry)object;

					Assert.assertNotNull(trashEntry);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		TrashEntry newTrashEntry = addTrashEntry();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(TrashEntry.class,
				TrashEntry.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("entryId",
				newTrashEntry.getEntryId()));

		List<TrashEntry> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		TrashEntry existingTrashEntry = result.get(0);

		Assert.assertEquals(existingTrashEntry, newTrashEntry);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(TrashEntry.class,
				TrashEntry.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("entryId",
				ServiceTestUtil.nextLong()));

		List<TrashEntry> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		TrashEntry newTrashEntry = addTrashEntry();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(TrashEntry.class,
				TrashEntry.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("entryId"));

		Object newEntryId = newTrashEntry.getEntryId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("entryId",
				new Object[] { newEntryId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingEntryId = result.get(0);

		Assert.assertEquals(existingEntryId, newEntryId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(TrashEntry.class,
				TrashEntry.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("entryId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("entryId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		TrashEntry newTrashEntry = addTrashEntry();

		_persistence.clearCache();

		TrashEntryModelImpl existingTrashEntryModelImpl = (TrashEntryModelImpl)_persistence.findByPrimaryKey(newTrashEntry.getPrimaryKey());

		Assert.assertEquals(existingTrashEntryModelImpl.getClassNameId(),
			existingTrashEntryModelImpl.getOriginalClassNameId());
		Assert.assertEquals(existingTrashEntryModelImpl.getClassPK(),
			existingTrashEntryModelImpl.getOriginalClassPK());
	}

	protected TrashEntry addTrashEntry() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		TrashEntry trashEntry = _persistence.create(pk);

		trashEntry.setGroupId(ServiceTestUtil.nextLong());

		trashEntry.setCompanyId(ServiceTestUtil.nextLong());

		trashEntry.setUserId(ServiceTestUtil.nextLong());

		trashEntry.setUserName(ServiceTestUtil.randomString());

		trashEntry.setCreateDate(ServiceTestUtil.nextDate());

		trashEntry.setClassNameId(ServiceTestUtil.nextLong());

		trashEntry.setClassPK(ServiceTestUtil.nextLong());

		trashEntry.setSystemEventSetKey(ServiceTestUtil.nextLong());

		trashEntry.setTypeSettings(ServiceTestUtil.randomString());

		trashEntry.setStatus(ServiceTestUtil.nextInt());

		_persistence.update(trashEntry);

		return trashEntry;
	}

	private static Log _log = LogFactoryUtil.getLog(TrashEntryPersistenceTest.class);
	private TrashEntryPersistence _persistence = (TrashEntryPersistence)PortalBeanLocatorUtil.locate(TrashEntryPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}