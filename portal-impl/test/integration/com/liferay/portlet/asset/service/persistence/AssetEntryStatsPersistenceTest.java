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

package com.liferay.portlet.asset.service.persistence;

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

import com.liferay.portlet.asset.NoSuchEntryStatsException;
import com.liferay.portlet.asset.model.AssetEntryStats;
import com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl;

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
public class AssetEntryStatsPersistenceTest {
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

		AssetEntryStats assetEntryStats = _persistence.create(pk);

		Assert.assertNotNull(assetEntryStats);

		Assert.assertEquals(assetEntryStats.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		AssetEntryStats newAssetEntryStats = addAssetEntryStats();

		_persistence.remove(newAssetEntryStats);

		AssetEntryStats existingAssetEntryStats = _persistence.fetchByPrimaryKey(newAssetEntryStats.getPrimaryKey());

		Assert.assertNull(existingAssetEntryStats);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addAssetEntryStats();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetEntryStats newAssetEntryStats = _persistence.create(pk);

		newAssetEntryStats.setGroupId(ServiceTestUtil.nextLong());

		newAssetEntryStats.setCompanyId(ServiceTestUtil.nextLong());

		newAssetEntryStats.setUserId(ServiceTestUtil.nextLong());

		newAssetEntryStats.setUserName(ServiceTestUtil.randomString());

		newAssetEntryStats.setCreateDate(ServiceTestUtil.nextDate());

		newAssetEntryStats.setModifiedDate(ServiceTestUtil.nextDate());

		newAssetEntryStats.setClassNameId(ServiceTestUtil.nextLong());

		newAssetEntryStats.setClassPK(ServiceTestUtil.nextLong());

		newAssetEntryStats.setDay(ServiceTestUtil.nextInt());

		newAssetEntryStats.setMonth(ServiceTestUtil.nextInt());

		newAssetEntryStats.setYear(ServiceTestUtil.nextInt());

		newAssetEntryStats.setViewCount(ServiceTestUtil.nextInt());

		_persistence.update(newAssetEntryStats);

		AssetEntryStats existingAssetEntryStats = _persistence.findByPrimaryKey(newAssetEntryStats.getPrimaryKey());

		Assert.assertEquals(existingAssetEntryStats.getEntryStatsId(),
			newAssetEntryStats.getEntryStatsId());
		Assert.assertEquals(existingAssetEntryStats.getGroupId(),
			newAssetEntryStats.getGroupId());
		Assert.assertEquals(existingAssetEntryStats.getCompanyId(),
			newAssetEntryStats.getCompanyId());
		Assert.assertEquals(existingAssetEntryStats.getUserId(),
			newAssetEntryStats.getUserId());
		Assert.assertEquals(existingAssetEntryStats.getUserName(),
			newAssetEntryStats.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingAssetEntryStats.getCreateDate()),
			Time.getShortTimestamp(newAssetEntryStats.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingAssetEntryStats.getModifiedDate()),
			Time.getShortTimestamp(newAssetEntryStats.getModifiedDate()));
		Assert.assertEquals(existingAssetEntryStats.getClassNameId(),
			newAssetEntryStats.getClassNameId());
		Assert.assertEquals(existingAssetEntryStats.getClassPK(),
			newAssetEntryStats.getClassPK());
		Assert.assertEquals(existingAssetEntryStats.getDay(),
			newAssetEntryStats.getDay());
		Assert.assertEquals(existingAssetEntryStats.getMonth(),
			newAssetEntryStats.getMonth());
		Assert.assertEquals(existingAssetEntryStats.getYear(),
			newAssetEntryStats.getYear());
		Assert.assertEquals(existingAssetEntryStats.getViewCount(),
			newAssetEntryStats.getViewCount());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		AssetEntryStats newAssetEntryStats = addAssetEntryStats();

		AssetEntryStats existingAssetEntryStats = _persistence.findByPrimaryKey(newAssetEntryStats.getPrimaryKey());

		Assert.assertEquals(existingAssetEntryStats, newAssetEntryStats);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchEntryStatsException");
		}
		catch (NoSuchEntryStatsException nsee) {
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

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("AssetEntryStats",
			"entryStatsId", true, "groupId", true, "companyId", true, "userId",
			true, "userName", true, "createDate", true, "modifiedDate", true,
			"classNameId", true, "classPK", true, "day", true, "month", true,
			"year", true, "viewCount", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		AssetEntryStats newAssetEntryStats = addAssetEntryStats();

		AssetEntryStats existingAssetEntryStats = _persistence.fetchByPrimaryKey(newAssetEntryStats.getPrimaryKey());

		Assert.assertEquals(existingAssetEntryStats, newAssetEntryStats);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetEntryStats missingAssetEntryStats = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingAssetEntryStats);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new AssetEntryStatsActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					AssetEntryStats assetEntryStats = (AssetEntryStats)object;

					Assert.assertNotNull(assetEntryStats);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		AssetEntryStats newAssetEntryStats = addAssetEntryStats();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetEntryStats.class,
				AssetEntryStats.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("entryStatsId",
				newAssetEntryStats.getEntryStatsId()));

		List<AssetEntryStats> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		AssetEntryStats existingAssetEntryStats = result.get(0);

		Assert.assertEquals(existingAssetEntryStats, newAssetEntryStats);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetEntryStats.class,
				AssetEntryStats.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("entryStatsId",
				ServiceTestUtil.nextLong()));

		List<AssetEntryStats> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		AssetEntryStats newAssetEntryStats = addAssetEntryStats();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetEntryStats.class,
				AssetEntryStats.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"entryStatsId"));

		Object newEntryStatsId = newAssetEntryStats.getEntryStatsId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("entryStatsId",
				new Object[] { newEntryStatsId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingEntryStatsId = result.get(0);

		Assert.assertEquals(existingEntryStatsId, newEntryStatsId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetEntryStats.class,
				AssetEntryStats.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"entryStatsId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("entryStatsId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		AssetEntryStats newAssetEntryStats = addAssetEntryStats();

		_persistence.clearCache();

		AssetEntryStatsModelImpl existingAssetEntryStatsModelImpl = (AssetEntryStatsModelImpl)_persistence.findByPrimaryKey(newAssetEntryStats.getPrimaryKey());

		Assert.assertEquals(existingAssetEntryStatsModelImpl.getClassNameId(),
			existingAssetEntryStatsModelImpl.getOriginalClassNameId());
		Assert.assertEquals(existingAssetEntryStatsModelImpl.getClassPK(),
			existingAssetEntryStatsModelImpl.getOriginalClassPK());
		Assert.assertEquals(existingAssetEntryStatsModelImpl.getDay(),
			existingAssetEntryStatsModelImpl.getOriginalDay());
		Assert.assertEquals(existingAssetEntryStatsModelImpl.getMonth(),
			existingAssetEntryStatsModelImpl.getOriginalMonth());
		Assert.assertEquals(existingAssetEntryStatsModelImpl.getYear(),
			existingAssetEntryStatsModelImpl.getOriginalYear());
	}

	protected AssetEntryStats addAssetEntryStats() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetEntryStats assetEntryStats = _persistence.create(pk);

		assetEntryStats.setGroupId(ServiceTestUtil.nextLong());

		assetEntryStats.setCompanyId(ServiceTestUtil.nextLong());

		assetEntryStats.setUserId(ServiceTestUtil.nextLong());

		assetEntryStats.setUserName(ServiceTestUtil.randomString());

		assetEntryStats.setCreateDate(ServiceTestUtil.nextDate());

		assetEntryStats.setModifiedDate(ServiceTestUtil.nextDate());

		assetEntryStats.setClassNameId(ServiceTestUtil.nextLong());

		assetEntryStats.setClassPK(ServiceTestUtil.nextLong());

		assetEntryStats.setDay(ServiceTestUtil.nextInt());

		assetEntryStats.setMonth(ServiceTestUtil.nextInt());

		assetEntryStats.setYear(ServiceTestUtil.nextInt());

		assetEntryStats.setViewCount(ServiceTestUtil.nextInt());

		_persistence.update(assetEntryStats);

		return assetEntryStats;
	}

	private static Log _log = LogFactoryUtil.getLog(AssetEntryStatsPersistenceTest.class);
	private AssetEntryStatsPersistence _persistence = (AssetEntryStatsPersistence)PortalBeanLocatorUtil.locate(AssetEntryStatsPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}