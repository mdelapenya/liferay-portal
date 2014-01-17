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

package com.liferay.portlet.messageboards.service.persistence;

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

import com.liferay.portlet.messageboards.NoSuchBanException;
import com.liferay.portlet.messageboards.model.MBBan;
import com.liferay.portlet.messageboards.model.impl.MBBanModelImpl;

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
public class MBBanPersistenceTest {
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

		MBBan mbBan = _persistence.create(pk);

		Assert.assertNotNull(mbBan);

		Assert.assertEquals(mbBan.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		MBBan newMBBan = addMBBan();

		_persistence.remove(newMBBan);

		MBBan existingMBBan = _persistence.fetchByPrimaryKey(newMBBan.getPrimaryKey());

		Assert.assertNull(existingMBBan);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addMBBan();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		MBBan newMBBan = _persistence.create(pk);

		newMBBan.setUuid(ServiceTestUtil.randomString());

		newMBBan.setGroupId(ServiceTestUtil.nextLong());

		newMBBan.setCompanyId(ServiceTestUtil.nextLong());

		newMBBan.setUserId(ServiceTestUtil.nextLong());

		newMBBan.setUserName(ServiceTestUtil.randomString());

		newMBBan.setCreateDate(ServiceTestUtil.nextDate());

		newMBBan.setModifiedDate(ServiceTestUtil.nextDate());

		newMBBan.setBanUserId(ServiceTestUtil.nextLong());

		_persistence.update(newMBBan);

		MBBan existingMBBan = _persistence.findByPrimaryKey(newMBBan.getPrimaryKey());

		Assert.assertEquals(existingMBBan.getUuid(), newMBBan.getUuid());
		Assert.assertEquals(existingMBBan.getBanId(), newMBBan.getBanId());
		Assert.assertEquals(existingMBBan.getGroupId(), newMBBan.getGroupId());
		Assert.assertEquals(existingMBBan.getCompanyId(),
			newMBBan.getCompanyId());
		Assert.assertEquals(existingMBBan.getUserId(), newMBBan.getUserId());
		Assert.assertEquals(existingMBBan.getUserName(), newMBBan.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingMBBan.getCreateDate()),
			Time.getShortTimestamp(newMBBan.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingMBBan.getModifiedDate()),
			Time.getShortTimestamp(newMBBan.getModifiedDate()));
		Assert.assertEquals(existingMBBan.getBanUserId(),
			newMBBan.getBanUserId());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		MBBan newMBBan = addMBBan();

		MBBan existingMBBan = _persistence.findByPrimaryKey(newMBBan.getPrimaryKey());

		Assert.assertEquals(existingMBBan, newMBBan);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchBanException");
		}
		catch (NoSuchBanException nsee) {
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
		MBBan mbBan = addMBBan();

		String uuid = mbBan.getUuid();

		List<MBBan> mbBans = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, mbBans.size());

		Assert.assertEquals(mbBan.getPrimaryKey(), mbBans.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addMBBan();

		String uuid = ServiceTestUtil.randomString();

		List<MBBan> mbBans = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, mbBans.size());
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
		MBBan mbBan = addMBBan();

		String uuid = mbBan.getUuid();

		List<MBBan> mbBans = _persistence.findByUuid(uuid, start, end);

		Assert.assertEquals(expected, mbBans.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		MBBan mbBan = addMBBan();

		String uuid = mbBan.getUuid();

		long companyId = mbBan.getCompanyId();

		List<MBBan> mbBans = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(1, mbBans.size());

		Assert.assertEquals(mbBan.getPrimaryKey(), mbBans.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addMBBan();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<MBBan> mbBans = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(0, mbBans.size());
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
		MBBan mbBan = addMBBan();

		String uuid = mbBan.getUuid();

		long companyId = mbBan.getCompanyId();

		List<MBBan> mbBans = _persistence.findByUuid_C(uuid, companyId, start,
				end);

		Assert.assertEquals(expected, mbBans.size());
	}

	@Test
	public void testFindByGroupId() throws Exception {
		MBBan mbBan = addMBBan();

		long groupId = mbBan.getGroupId();

		List<MBBan> mbBans = _persistence.findByGroupId(groupId);

		Assert.assertEquals(1, mbBans.size());

		Assert.assertEquals(mbBan.getPrimaryKey(), mbBans.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByGroupIdNotFound() throws Exception {
		addMBBan();

		long groupId = ServiceTestUtil.nextLong();

		List<MBBan> mbBans = _persistence.findByGroupId(groupId);

		Assert.assertEquals(0, mbBans.size());
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
		MBBan mbBan = addMBBan();

		long groupId = mbBan.getGroupId();

		List<MBBan> mbBans = _persistence.findByGroupId(groupId, start, end);

		Assert.assertEquals(expected, mbBans.size());
	}

	@Test
	public void testFindByUserId() throws Exception {
		MBBan mbBan = addMBBan();

		long userId = mbBan.getUserId();

		List<MBBan> mbBans = _persistence.findByUserId(userId);

		Assert.assertEquals(1, mbBans.size());

		Assert.assertEquals(mbBan.getPrimaryKey(), mbBans.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUserIdNotFound() throws Exception {
		addMBBan();

		long userId = ServiceTestUtil.nextLong();

		List<MBBan> mbBans = _persistence.findByUserId(userId);

		Assert.assertEquals(0, mbBans.size());
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
		MBBan mbBan = addMBBan();

		long userId = mbBan.getUserId();

		List<MBBan> mbBans = _persistence.findByUserId(userId, start, end);

		Assert.assertEquals(expected, mbBans.size());
	}

	@Test
	public void testFindByBanUserId() throws Exception {
		MBBan mbBan = addMBBan();

		long banUserId = mbBan.getBanUserId();

		List<MBBan> mbBans = _persistence.findByBanUserId(banUserId);

		Assert.assertEquals(1, mbBans.size());

		Assert.assertEquals(mbBan.getPrimaryKey(), mbBans.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByBanUserIdNotFound() throws Exception {
		addMBBan();

		long banUserId = ServiceTestUtil.nextLong();

		List<MBBan> mbBans = _persistence.findByBanUserId(banUserId);

		Assert.assertEquals(0, mbBans.size());
	}

	@Test
	public void testFindByBanUserIdStartEnd() throws Exception {
		testFindByBanUserIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByBanUserIdStartEndWrongRange()
		throws Exception {
		testFindByBanUserIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByBanUserIdStartEndZeroZero() throws Exception {
		testFindByBanUserIdStartEnd(0, 0, 0);
	}

	protected void testFindByBanUserIdStartEnd(int start, int end, int expected)
		throws Exception {
		MBBan mbBan = addMBBan();

		long banUserId = mbBan.getBanUserId();

		List<MBBan> mbBans = _persistence.findByBanUserId(banUserId, start, end);

		Assert.assertEquals(expected, mbBans.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("MBBan", "uuid", true,
			"banId", true, "groupId", true, "companyId", true, "userId", true,
			"userName", true, "createDate", true, "modifiedDate", true,
			"banUserId", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		MBBan newMBBan = addMBBan();

		MBBan existingMBBan = _persistence.fetchByPrimaryKey(newMBBan.getPrimaryKey());

		Assert.assertEquals(existingMBBan, newMBBan);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		MBBan missingMBBan = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingMBBan);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new MBBanActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					MBBan mbBan = (MBBan)object;

					Assert.assertNotNull(mbBan);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		MBBan newMBBan = addMBBan();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(MBBan.class,
				MBBan.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("banId", newMBBan.getBanId()));

		List<MBBan> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		MBBan existingMBBan = result.get(0);

		Assert.assertEquals(existingMBBan, newMBBan);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(MBBan.class,
				MBBan.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("banId",
				ServiceTestUtil.nextLong()));

		List<MBBan> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		MBBan newMBBan = addMBBan();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(MBBan.class,
				MBBan.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("banId"));

		Object newBanId = newMBBan.getBanId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("banId",
				new Object[] { newBanId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingBanId = result.get(0);

		Assert.assertEquals(existingBanId, newBanId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(MBBan.class,
				MBBan.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("banId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("banId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		MBBan newMBBan = addMBBan();

		_persistence.clearCache();

		MBBanModelImpl existingMBBanModelImpl = (MBBanModelImpl)_persistence.findByPrimaryKey(newMBBan.getPrimaryKey());

		Assert.assertTrue(Validator.equals(existingMBBanModelImpl.getUuid(),
				existingMBBanModelImpl.getOriginalUuid()));
		Assert.assertEquals(existingMBBanModelImpl.getGroupId(),
			existingMBBanModelImpl.getOriginalGroupId());

		Assert.assertEquals(existingMBBanModelImpl.getGroupId(),
			existingMBBanModelImpl.getOriginalGroupId());
		Assert.assertEquals(existingMBBanModelImpl.getBanUserId(),
			existingMBBanModelImpl.getOriginalBanUserId());
	}

	protected MBBan addMBBan() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		MBBan mbBan = _persistence.create(pk);

		mbBan.setUuid(ServiceTestUtil.randomString());

		mbBan.setGroupId(ServiceTestUtil.nextLong());

		mbBan.setCompanyId(ServiceTestUtil.nextLong());

		mbBan.setUserId(ServiceTestUtil.nextLong());

		mbBan.setUserName(ServiceTestUtil.randomString());

		mbBan.setCreateDate(ServiceTestUtil.nextDate());

		mbBan.setModifiedDate(ServiceTestUtil.nextDate());

		mbBan.setBanUserId(ServiceTestUtil.nextLong());

		_persistence.update(mbBan);

		return mbBan;
	}

	private static Log _log = LogFactoryUtil.getLog(MBBanPersistenceTest.class);
	private MBBanPersistence _persistence = (MBBanPersistence)PortalBeanLocatorUtil.locate(MBBanPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}