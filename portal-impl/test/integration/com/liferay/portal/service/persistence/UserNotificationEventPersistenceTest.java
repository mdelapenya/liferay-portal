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

import com.liferay.portal.NoSuchUserNotificationEventException;
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
import com.liferay.portal.model.UserNotificationEvent;
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
public class UserNotificationEventPersistenceTest {
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

		UserNotificationEvent userNotificationEvent = _persistence.create(pk);

		Assert.assertNotNull(userNotificationEvent);

		Assert.assertEquals(userNotificationEvent.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		UserNotificationEvent newUserNotificationEvent = addUserNotificationEvent();

		_persistence.remove(newUserNotificationEvent);

		UserNotificationEvent existingUserNotificationEvent = _persistence.fetchByPrimaryKey(newUserNotificationEvent.getPrimaryKey());

		Assert.assertNull(existingUserNotificationEvent);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addUserNotificationEvent();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		UserNotificationEvent newUserNotificationEvent = _persistence.create(pk);

		newUserNotificationEvent.setUuid(ServiceTestUtil.randomString());

		newUserNotificationEvent.setCompanyId(ServiceTestUtil.nextLong());

		newUserNotificationEvent.setUserId(ServiceTestUtil.nextLong());

		newUserNotificationEvent.setType(ServiceTestUtil.randomString());

		newUserNotificationEvent.setTimestamp(ServiceTestUtil.nextLong());

		newUserNotificationEvent.setDeliverBy(ServiceTestUtil.nextLong());

		newUserNotificationEvent.setDelivered(ServiceTestUtil.randomBoolean());

		newUserNotificationEvent.setPayload(ServiceTestUtil.randomString());

		newUserNotificationEvent.setArchived(ServiceTestUtil.randomBoolean());

		_persistence.update(newUserNotificationEvent);

		UserNotificationEvent existingUserNotificationEvent = _persistence.findByPrimaryKey(newUserNotificationEvent.getPrimaryKey());

		Assert.assertEquals(existingUserNotificationEvent.getUuid(),
			newUserNotificationEvent.getUuid());
		Assert.assertEquals(existingUserNotificationEvent.getUserNotificationEventId(),
			newUserNotificationEvent.getUserNotificationEventId());
		Assert.assertEquals(existingUserNotificationEvent.getCompanyId(),
			newUserNotificationEvent.getCompanyId());
		Assert.assertEquals(existingUserNotificationEvent.getUserId(),
			newUserNotificationEvent.getUserId());
		Assert.assertEquals(existingUserNotificationEvent.getType(),
			newUserNotificationEvent.getType());
		Assert.assertEquals(existingUserNotificationEvent.getTimestamp(),
			newUserNotificationEvent.getTimestamp());
		Assert.assertEquals(existingUserNotificationEvent.getDeliverBy(),
			newUserNotificationEvent.getDeliverBy());
		Assert.assertEquals(existingUserNotificationEvent.getDelivered(),
			newUserNotificationEvent.getDelivered());
		Assert.assertEquals(existingUserNotificationEvent.getPayload(),
			newUserNotificationEvent.getPayload());
		Assert.assertEquals(existingUserNotificationEvent.getArchived(),
			newUserNotificationEvent.getArchived());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		UserNotificationEvent newUserNotificationEvent = addUserNotificationEvent();

		UserNotificationEvent existingUserNotificationEvent = _persistence.findByPrimaryKey(newUserNotificationEvent.getPrimaryKey());

		Assert.assertEquals(existingUserNotificationEvent,
			newUserNotificationEvent);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchUserNotificationEventException");
		}
		catch (NoSuchUserNotificationEventException nsee) {
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
		UserNotificationEvent userNotificationEvent = addUserNotificationEvent();

		String uuid = userNotificationEvent.getUuid();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, userNotificationEvents.size());

		Assert.assertEquals(userNotificationEvent.getPrimaryKey(),
			userNotificationEvents.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addUserNotificationEvent();

		String uuid = ServiceTestUtil.randomString();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, userNotificationEvents.size());
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
		UserNotificationEvent userNotificationEvent = addUserNotificationEvent();

		String uuid = userNotificationEvent.getUuid();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByUuid(uuid,
				start, end);

		Assert.assertEquals(expected, userNotificationEvents.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		UserNotificationEvent userNotificationEvent = addUserNotificationEvent();

		String uuid = userNotificationEvent.getUuid();

		long companyId = userNotificationEvent.getCompanyId();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(1, userNotificationEvents.size());

		Assert.assertEquals(userNotificationEvent.getPrimaryKey(),
			userNotificationEvents.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addUserNotificationEvent();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(0, userNotificationEvents.size());
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
		UserNotificationEvent userNotificationEvent = addUserNotificationEvent();

		String uuid = userNotificationEvent.getUuid();

		long companyId = userNotificationEvent.getCompanyId();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByUuid_C(uuid,
				companyId, start, end);

		Assert.assertEquals(expected, userNotificationEvents.size());
	}

	@Test
	public void testFindByUserId() throws Exception {
		UserNotificationEvent userNotificationEvent = addUserNotificationEvent();

		long userId = userNotificationEvent.getUserId();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByUserId(userId);

		Assert.assertEquals(1, userNotificationEvents.size());

		Assert.assertEquals(userNotificationEvent.getPrimaryKey(),
			userNotificationEvents.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUserIdNotFound() throws Exception {
		addUserNotificationEvent();

		long userId = ServiceTestUtil.nextLong();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByUserId(userId);

		Assert.assertEquals(0, userNotificationEvents.size());
	}

	@Test
	public void testFindByUserIdStartEnd() throws Exception {
		testFindByUserIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByUserIdStartEndWrongRange() throws Exception {
		testFindByUserIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByUserIdStartEndZeroZero() throws Exception {
		testFindByUserIdStartEnd(0, 0, 0);
	}

	protected void testFindByUserIdStartEnd(int start, int end, int expected)
		throws Exception {
		UserNotificationEvent userNotificationEvent = addUserNotificationEvent();

		long userId = userNotificationEvent.getUserId();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByUserId(userId,
				start, end);

		Assert.assertEquals(expected, userNotificationEvents.size());
	}

	@Test
	public void testFindByU_D() throws Exception {
		UserNotificationEvent userNotificationEvent = addUserNotificationEvent();

		long userId = userNotificationEvent.getUserId();

		boolean delivered = userNotificationEvent.getDelivered();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByU_D(userId,
				delivered);

		Assert.assertEquals(1, userNotificationEvents.size());

		Assert.assertEquals(userNotificationEvent.getPrimaryKey(),
			userNotificationEvents.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByU_DNotFound() throws Exception {
		addUserNotificationEvent();

		long userId = ServiceTestUtil.nextLong();

		boolean delivered = ServiceTestUtil.randomBoolean();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByU_D(userId,
				delivered);

		Assert.assertEquals(0, userNotificationEvents.size());
	}

	@Test
	public void testFindByU_DStartEnd() throws Exception {
		testFindByU_DStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByU_DStartEndWrongRange() throws Exception {
		testFindByU_DStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByU_DStartEndZeroZero() throws Exception {
		testFindByU_DStartEnd(0, 0, 0);
	}

	protected void testFindByU_DStartEnd(int start, int end, int expected)
		throws Exception {
		UserNotificationEvent userNotificationEvent = addUserNotificationEvent();

		long userId = userNotificationEvent.getUserId();

		boolean delivered = userNotificationEvent.getDelivered();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByU_D(userId,
				delivered, start, end);

		Assert.assertEquals(expected, userNotificationEvents.size());
	}

	@Test
	public void testFindByU_A() throws Exception {
		UserNotificationEvent userNotificationEvent = addUserNotificationEvent();

		long userId = userNotificationEvent.getUserId();

		boolean archived = userNotificationEvent.getArchived();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByU_A(userId,
				archived);

		Assert.assertEquals(1, userNotificationEvents.size());

		Assert.assertEquals(userNotificationEvent.getPrimaryKey(),
			userNotificationEvents.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByU_ANotFound() throws Exception {
		addUserNotificationEvent();

		long userId = ServiceTestUtil.nextLong();

		boolean archived = ServiceTestUtil.randomBoolean();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByU_A(userId,
				archived);

		Assert.assertEquals(0, userNotificationEvents.size());
	}

	@Test
	public void testFindByU_AStartEnd() throws Exception {
		testFindByU_AStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByU_AStartEndWrongRange() throws Exception {
		testFindByU_AStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByU_AStartEndZeroZero() throws Exception {
		testFindByU_AStartEnd(0, 0, 0);
	}

	protected void testFindByU_AStartEnd(int start, int end, int expected)
		throws Exception {
		UserNotificationEvent userNotificationEvent = addUserNotificationEvent();

		long userId = userNotificationEvent.getUserId();

		boolean archived = userNotificationEvent.getArchived();

		List<UserNotificationEvent> userNotificationEvents = _persistence.findByU_A(userId,
				archived, start, end);

		Assert.assertEquals(expected, userNotificationEvents.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("UserNotificationEvent",
			"uuid", true, "userNotificationEventId", true, "companyId", true,
			"userId", true, "type", true, "timestamp", true, "deliverBy", true,
			"delivered", true, "payload", true, "archived", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		UserNotificationEvent newUserNotificationEvent = addUserNotificationEvent();

		UserNotificationEvent existingUserNotificationEvent = _persistence.fetchByPrimaryKey(newUserNotificationEvent.getPrimaryKey());

		Assert.assertEquals(existingUserNotificationEvent,
			newUserNotificationEvent);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		UserNotificationEvent missingUserNotificationEvent = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingUserNotificationEvent);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new UserNotificationEventActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					UserNotificationEvent userNotificationEvent = (UserNotificationEvent)object;

					Assert.assertNotNull(userNotificationEvent);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		UserNotificationEvent newUserNotificationEvent = addUserNotificationEvent();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(UserNotificationEvent.class,
				UserNotificationEvent.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("userNotificationEventId",
				newUserNotificationEvent.getUserNotificationEventId()));

		List<UserNotificationEvent> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		UserNotificationEvent existingUserNotificationEvent = result.get(0);

		Assert.assertEquals(existingUserNotificationEvent,
			newUserNotificationEvent);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(UserNotificationEvent.class,
				UserNotificationEvent.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("userNotificationEventId",
				ServiceTestUtil.nextLong()));

		List<UserNotificationEvent> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		UserNotificationEvent newUserNotificationEvent = addUserNotificationEvent();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(UserNotificationEvent.class,
				UserNotificationEvent.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"userNotificationEventId"));

		Object newUserNotificationEventId = newUserNotificationEvent.getUserNotificationEventId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("userNotificationEventId",
				new Object[] { newUserNotificationEventId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingUserNotificationEventId = result.get(0);

		Assert.assertEquals(existingUserNotificationEventId,
			newUserNotificationEventId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(UserNotificationEvent.class,
				UserNotificationEvent.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"userNotificationEventId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("userNotificationEventId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected UserNotificationEvent addUserNotificationEvent()
		throws Exception {
		long pk = ServiceTestUtil.nextLong();

		UserNotificationEvent userNotificationEvent = _persistence.create(pk);

		userNotificationEvent.setUuid(ServiceTestUtil.randomString());

		userNotificationEvent.setCompanyId(ServiceTestUtil.nextLong());

		userNotificationEvent.setUserId(ServiceTestUtil.nextLong());

		userNotificationEvent.setType(ServiceTestUtil.randomString());

		userNotificationEvent.setTimestamp(ServiceTestUtil.nextLong());

		userNotificationEvent.setDeliverBy(ServiceTestUtil.nextLong());

		userNotificationEvent.setDelivered(ServiceTestUtil.randomBoolean());

		userNotificationEvent.setPayload(ServiceTestUtil.randomString());

		userNotificationEvent.setArchived(ServiceTestUtil.randomBoolean());

		_persistence.update(userNotificationEvent);

		return userNotificationEvent;
	}

	private static Log _log = LogFactoryUtil.getLog(UserNotificationEventPersistenceTest.class);
	private UserNotificationEventPersistence _persistence = (UserNotificationEventPersistence)PortalBeanLocatorUtil.locate(UserNotificationEventPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}