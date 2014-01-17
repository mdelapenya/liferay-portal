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

package com.liferay.portal.service.persistence;

import com.liferay.portal.NoSuchSystemEventException;
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
import com.liferay.portal.model.SystemEvent;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;

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
public class SystemEventPersistenceTest {
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

		SystemEvent systemEvent = _persistence.create(pk);

		Assert.assertNotNull(systemEvent);

		Assert.assertEquals(systemEvent.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		SystemEvent newSystemEvent = addSystemEvent();

		_persistence.remove(newSystemEvent);

		SystemEvent existingSystemEvent = _persistence.fetchByPrimaryKey(newSystemEvent.getPrimaryKey());

		Assert.assertNull(existingSystemEvent);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addSystemEvent();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SystemEvent newSystemEvent = _persistence.create(pk);

		newSystemEvent.setGroupId(ServiceTestUtil.nextLong());

		newSystemEvent.setCompanyId(ServiceTestUtil.nextLong());

		newSystemEvent.setUserId(ServiceTestUtil.nextLong());

		newSystemEvent.setUserName(ServiceTestUtil.randomString());

		newSystemEvent.setCreateDate(ServiceTestUtil.nextDate());

		newSystemEvent.setClassNameId(ServiceTestUtil.nextLong());

		newSystemEvent.setClassPK(ServiceTestUtil.nextLong());

		newSystemEvent.setClassUuid(ServiceTestUtil.randomString());

		newSystemEvent.setReferrerClassNameId(ServiceTestUtil.nextLong());

		newSystemEvent.setParentSystemEventId(ServiceTestUtil.nextLong());

		newSystemEvent.setSystemEventSetKey(ServiceTestUtil.nextLong());

		newSystemEvent.setType(ServiceTestUtil.nextInt());

		newSystemEvent.setExtraData(ServiceTestUtil.randomString());

		_persistence.update(newSystemEvent);

		SystemEvent existingSystemEvent = _persistence.findByPrimaryKey(newSystemEvent.getPrimaryKey());

		Assert.assertEquals(existingSystemEvent.getSystemEventId(),
			newSystemEvent.getSystemEventId());
		Assert.assertEquals(existingSystemEvent.getGroupId(),
			newSystemEvent.getGroupId());
		Assert.assertEquals(existingSystemEvent.getCompanyId(),
			newSystemEvent.getCompanyId());
		Assert.assertEquals(existingSystemEvent.getUserId(),
			newSystemEvent.getUserId());
		Assert.assertEquals(existingSystemEvent.getUserName(),
			newSystemEvent.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingSystemEvent.getCreateDate()),
			Time.getShortTimestamp(newSystemEvent.getCreateDate()));
		Assert.assertEquals(existingSystemEvent.getClassNameId(),
			newSystemEvent.getClassNameId());
		Assert.assertEquals(existingSystemEvent.getClassPK(),
			newSystemEvent.getClassPK());
		Assert.assertEquals(existingSystemEvent.getClassUuid(),
			newSystemEvent.getClassUuid());
		Assert.assertEquals(existingSystemEvent.getReferrerClassNameId(),
			newSystemEvent.getReferrerClassNameId());
		Assert.assertEquals(existingSystemEvent.getParentSystemEventId(),
			newSystemEvent.getParentSystemEventId());
		Assert.assertEquals(existingSystemEvent.getSystemEventSetKey(),
			newSystemEvent.getSystemEventSetKey());
		Assert.assertEquals(existingSystemEvent.getType(),
			newSystemEvent.getType());
		Assert.assertEquals(existingSystemEvent.getExtraData(),
			newSystemEvent.getExtraData());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		SystemEvent newSystemEvent = addSystemEvent();

		SystemEvent existingSystemEvent = _persistence.findByPrimaryKey(newSystemEvent.getPrimaryKey());

		Assert.assertEquals(existingSystemEvent, newSystemEvent);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchSystemEventException");
		}
		catch (NoSuchSystemEventException nsee) {
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
		SystemEvent systemEvent = addSystemEvent();

		long groupId = systemEvent.getGroupId();

		List<SystemEvent> systemEvents = _persistence.findByGroupId(groupId);

		Assert.assertEquals(1, systemEvents.size());

		Assert.assertEquals(systemEvent.getPrimaryKey(),
			systemEvents.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByGroupIdNotFound() throws Exception {
		addSystemEvent();

		long groupId = ServiceTestUtil.nextLong();

		List<SystemEvent> systemEvents = _persistence.findByGroupId(groupId);

		Assert.assertEquals(0, systemEvents.size());
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
		SystemEvent systemEvent = addSystemEvent();

		long groupId = systemEvent.getGroupId();

		List<SystemEvent> systemEvents = _persistence.findByGroupId(groupId,
				start, end);

		Assert.assertEquals(expected, systemEvents.size());
	}

	@Test
	public void testFindByG_S() throws Exception {
		SystemEvent systemEvent = addSystemEvent();

		long groupId = systemEvent.getGroupId();

		long systemEventSetKey = systemEvent.getSystemEventSetKey();

		List<SystemEvent> systemEvents = _persistence.findByG_S(groupId,
				systemEventSetKey);

		Assert.assertEquals(1, systemEvents.size());

		Assert.assertEquals(systemEvent.getPrimaryKey(),
			systemEvents.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_SNotFound() throws Exception {
		addSystemEvent();

		long groupId = ServiceTestUtil.nextLong();

		long systemEventSetKey = ServiceTestUtil.nextLong();

		List<SystemEvent> systemEvents = _persistence.findByG_S(groupId,
				systemEventSetKey);

		Assert.assertEquals(0, systemEvents.size());
	}

	@Test
	public void testFindByG_SStartEnd() throws Exception {
		testFindByG_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_SStartEndWrongRange() throws Exception {
		testFindByG_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_SStartEndZeroZero() throws Exception {
		testFindByG_SStartEnd(0, 0, 0);
	}

	protected void testFindByG_SStartEnd(int start, int end, int expected)
		throws Exception {
		SystemEvent systemEvent = addSystemEvent();

		long groupId = systemEvent.getGroupId();

		long systemEventSetKey = systemEvent.getSystemEventSetKey();

		List<SystemEvent> systemEvents = _persistence.findByG_S(groupId,
				systemEventSetKey, start, end);

		Assert.assertEquals(expected, systemEvents.size());
	}

	@Test
	public void testFindByG_C_C() throws Exception {
		SystemEvent systemEvent = addSystemEvent();

		long groupId = systemEvent.getGroupId();

		long classNameId = systemEvent.getClassNameId();

		long classPK = systemEvent.getClassPK();

		List<SystemEvent> systemEvents = _persistence.findByG_C_C(groupId,
				classNameId, classPK);

		Assert.assertEquals(1, systemEvents.size());

		Assert.assertEquals(systemEvent.getPrimaryKey(),
			systemEvents.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_C_CNotFound() throws Exception {
		addSystemEvent();

		long groupId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		List<SystemEvent> systemEvents = _persistence.findByG_C_C(groupId,
				classNameId, classPK);

		Assert.assertEquals(0, systemEvents.size());
	}

	@Test
	public void testFindByG_C_CStartEnd() throws Exception {
		testFindByG_C_CStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_C_CStartEndWrongRange() throws Exception {
		testFindByG_C_CStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_C_CStartEndZeroZero() throws Exception {
		testFindByG_C_CStartEnd(0, 0, 0);
	}

	protected void testFindByG_C_CStartEnd(int start, int end, int expected)
		throws Exception {
		SystemEvent systemEvent = addSystemEvent();

		long groupId = systemEvent.getGroupId();

		long classNameId = systemEvent.getClassNameId();

		long classPK = systemEvent.getClassPK();

		List<SystemEvent> systemEvents = _persistence.findByG_C_C(groupId,
				classNameId, classPK, start, end);

		Assert.assertEquals(expected, systemEvents.size());
	}

	@Test
	public void testFindByG_C_C_T() throws Exception {
		SystemEvent systemEvent = addSystemEvent();

		long groupId = systemEvent.getGroupId();

		long classNameId = systemEvent.getClassNameId();

		long classPK = systemEvent.getClassPK();

		int type = systemEvent.getType();

		List<SystemEvent> systemEvents = _persistence.findByG_C_C_T(groupId,
				classNameId, classPK, type);

		Assert.assertEquals(1, systemEvents.size());

		Assert.assertEquals(systemEvent.getPrimaryKey(),
			systemEvents.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_C_C_TNotFound() throws Exception {
		addSystemEvent();

		long groupId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		List<SystemEvent> systemEvents = _persistence.findByG_C_C_T(groupId,
				classNameId, classPK, type);

		Assert.assertEquals(0, systemEvents.size());
	}

	@Test
	public void testFindByG_C_C_TStartEnd() throws Exception {
		testFindByG_C_C_TStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_C_C_TStartEndWrongRange() throws Exception {
		testFindByG_C_C_TStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_C_C_TStartEndZeroZero() throws Exception {
		testFindByG_C_C_TStartEnd(0, 0, 0);
	}

	protected void testFindByG_C_C_TStartEnd(int start, int end, int expected)
		throws Exception {
		SystemEvent systemEvent = addSystemEvent();

		long groupId = systemEvent.getGroupId();

		long classNameId = systemEvent.getClassNameId();

		long classPK = systemEvent.getClassPK();

		int type = systemEvent.getType();

		List<SystemEvent> systemEvents = _persistence.findByG_C_C_T(groupId,
				classNameId, classPK, type, start, end);

		Assert.assertEquals(expected, systemEvents.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("SystemEvent",
			"systemEventId", true, "groupId", true, "companyId", true,
			"userId", true, "userName", true, "createDate", true,
			"classNameId", true, "classPK", true, "classUuid", true,
			"referrerClassNameId", true, "parentSystemEventId", true,
			"systemEventSetKey", true, "type", true, "extraData", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		SystemEvent newSystemEvent = addSystemEvent();

		SystemEvent existingSystemEvent = _persistence.fetchByPrimaryKey(newSystemEvent.getPrimaryKey());

		Assert.assertEquals(existingSystemEvent, newSystemEvent);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SystemEvent missingSystemEvent = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingSystemEvent);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new SystemEventActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					SystemEvent systemEvent = (SystemEvent)object;

					Assert.assertNotNull(systemEvent);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		SystemEvent newSystemEvent = addSystemEvent();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SystemEvent.class,
				SystemEvent.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("systemEventId",
				newSystemEvent.getSystemEventId()));

		List<SystemEvent> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		SystemEvent existingSystemEvent = result.get(0);

		Assert.assertEquals(existingSystemEvent, newSystemEvent);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SystemEvent.class,
				SystemEvent.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("systemEventId",
				ServiceTestUtil.nextLong()));

		List<SystemEvent> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		SystemEvent newSystemEvent = addSystemEvent();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SystemEvent.class,
				SystemEvent.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"systemEventId"));

		Object newSystemEventId = newSystemEvent.getSystemEventId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("systemEventId",
				new Object[] { newSystemEventId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingSystemEventId = result.get(0);

		Assert.assertEquals(existingSystemEventId, newSystemEventId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SystemEvent.class,
				SystemEvent.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"systemEventId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("systemEventId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected SystemEvent addSystemEvent() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SystemEvent systemEvent = _persistence.create(pk);

		systemEvent.setGroupId(ServiceTestUtil.nextLong());

		systemEvent.setCompanyId(ServiceTestUtil.nextLong());

		systemEvent.setUserId(ServiceTestUtil.nextLong());

		systemEvent.setUserName(ServiceTestUtil.randomString());

		systemEvent.setCreateDate(ServiceTestUtil.nextDate());

		systemEvent.setClassNameId(ServiceTestUtil.nextLong());

		systemEvent.setClassPK(ServiceTestUtil.nextLong());

		systemEvent.setClassUuid(ServiceTestUtil.randomString());

		systemEvent.setReferrerClassNameId(ServiceTestUtil.nextLong());

		systemEvent.setParentSystemEventId(ServiceTestUtil.nextLong());

		systemEvent.setSystemEventSetKey(ServiceTestUtil.nextLong());

		systemEvent.setType(ServiceTestUtil.nextInt());

		systemEvent.setExtraData(ServiceTestUtil.randomString());

		_persistence.update(systemEvent);

		return systemEvent;
	}

	private static Log _log = LogFactoryUtil.getLog(SystemEventPersistenceTest.class);
	private SystemEventPersistence _persistence = (SystemEventPersistence)PortalBeanLocatorUtil.locate(SystemEventPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}