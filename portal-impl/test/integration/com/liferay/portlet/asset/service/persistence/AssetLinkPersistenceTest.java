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

import com.liferay.portlet.asset.NoSuchLinkException;
import com.liferay.portlet.asset.model.AssetLink;
import com.liferay.portlet.asset.model.impl.AssetLinkModelImpl;

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
public class AssetLinkPersistenceTest {
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

		AssetLink assetLink = _persistence.create(pk);

		Assert.assertNotNull(assetLink);

		Assert.assertEquals(assetLink.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		AssetLink newAssetLink = addAssetLink();

		_persistence.remove(newAssetLink);

		AssetLink existingAssetLink = _persistence.fetchByPrimaryKey(newAssetLink.getPrimaryKey());

		Assert.assertNull(existingAssetLink);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addAssetLink();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetLink newAssetLink = _persistence.create(pk);

		newAssetLink.setCompanyId(ServiceTestUtil.nextLong());

		newAssetLink.setUserId(ServiceTestUtil.nextLong());

		newAssetLink.setUserName(ServiceTestUtil.randomString());

		newAssetLink.setCreateDate(ServiceTestUtil.nextDate());

		newAssetLink.setEntryId1(ServiceTestUtil.nextLong());

		newAssetLink.setEntryId2(ServiceTestUtil.nextLong());

		newAssetLink.setType(ServiceTestUtil.nextInt());

		newAssetLink.setWeight(ServiceTestUtil.nextInt());

		_persistence.update(newAssetLink);

		AssetLink existingAssetLink = _persistence.findByPrimaryKey(newAssetLink.getPrimaryKey());

		Assert.assertEquals(existingAssetLink.getLinkId(),
			newAssetLink.getLinkId());
		Assert.assertEquals(existingAssetLink.getCompanyId(),
			newAssetLink.getCompanyId());
		Assert.assertEquals(existingAssetLink.getUserId(),
			newAssetLink.getUserId());
		Assert.assertEquals(existingAssetLink.getUserName(),
			newAssetLink.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingAssetLink.getCreateDate()),
			Time.getShortTimestamp(newAssetLink.getCreateDate()));
		Assert.assertEquals(existingAssetLink.getEntryId1(),
			newAssetLink.getEntryId1());
		Assert.assertEquals(existingAssetLink.getEntryId2(),
			newAssetLink.getEntryId2());
		Assert.assertEquals(existingAssetLink.getType(), newAssetLink.getType());
		Assert.assertEquals(existingAssetLink.getWeight(),
			newAssetLink.getWeight());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		AssetLink newAssetLink = addAssetLink();

		AssetLink existingAssetLink = _persistence.findByPrimaryKey(newAssetLink.getPrimaryKey());

		Assert.assertEquals(existingAssetLink, newAssetLink);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchLinkException");
		}
		catch (NoSuchLinkException nsee) {
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
	public void testFindByE1() throws Exception {
		AssetLink assetLink = addAssetLink();

		long entryId1 = assetLink.getEntryId1();

		List<AssetLink> assetLinks = _persistence.findByE1(entryId1);

		Assert.assertEquals(1, assetLinks.size());

		Assert.assertEquals(assetLink.getPrimaryKey(),
			assetLinks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByE1NotFound() throws Exception {
		addAssetLink();

		long entryId1 = ServiceTestUtil.nextLong();

		List<AssetLink> assetLinks = _persistence.findByE1(entryId1);

		Assert.assertEquals(0, assetLinks.size());
	}

	@Test
	public void testFindByE1StartEnd() throws Exception {
		testFindByE1StartEnd(0, 5, 1);
	}

	@Test
	public void testFindByE1StartEndWrongRange() throws Exception {
		testFindByE1StartEnd(5, 0, 0);
	}

	@Test
	public void testFindByE1StartEndZeroZero() throws Exception {
		testFindByE1StartEnd(0, 0, 0);
	}

	protected void testFindByE1StartEnd(int start, int end, int expected)
		throws Exception {
		AssetLink assetLink = addAssetLink();

		long entryId1 = assetLink.getEntryId1();

		List<AssetLink> assetLinks = _persistence.findByE1(entryId1, start, end);

		Assert.assertEquals(expected, assetLinks.size());
	}

	@Test
	public void testFindByE2() throws Exception {
		AssetLink assetLink = addAssetLink();

		long entryId2 = assetLink.getEntryId2();

		List<AssetLink> assetLinks = _persistence.findByE2(entryId2);

		Assert.assertEquals(1, assetLinks.size());

		Assert.assertEquals(assetLink.getPrimaryKey(),
			assetLinks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByE2NotFound() throws Exception {
		addAssetLink();

		long entryId2 = ServiceTestUtil.nextLong();

		List<AssetLink> assetLinks = _persistence.findByE2(entryId2);

		Assert.assertEquals(0, assetLinks.size());
	}

	@Test
	public void testFindByE2StartEnd() throws Exception {
		testFindByE2StartEnd(0, 5, 1);
	}

	@Test
	public void testFindByE2StartEndWrongRange() throws Exception {
		testFindByE2StartEnd(5, 0, 0);
	}

	@Test
	public void testFindByE2StartEndZeroZero() throws Exception {
		testFindByE2StartEnd(0, 0, 0);
	}

	protected void testFindByE2StartEnd(int start, int end, int expected)
		throws Exception {
		AssetLink assetLink = addAssetLink();

		long entryId2 = assetLink.getEntryId2();

		List<AssetLink> assetLinks = _persistence.findByE2(entryId2, start, end);

		Assert.assertEquals(expected, assetLinks.size());
	}

	@Test
	public void testFindByE_E() throws Exception {
		AssetLink assetLink = addAssetLink();

		long entryId1 = assetLink.getEntryId1();

		long entryId2 = assetLink.getEntryId2();

		List<AssetLink> assetLinks = _persistence.findByE_E(entryId1, entryId2);

		Assert.assertEquals(1, assetLinks.size());

		Assert.assertEquals(assetLink.getPrimaryKey(),
			assetLinks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByE_ENotFound() throws Exception {
		addAssetLink();

		long entryId1 = ServiceTestUtil.nextLong();

		long entryId2 = ServiceTestUtil.nextLong();

		List<AssetLink> assetLinks = _persistence.findByE_E(entryId1, entryId2);

		Assert.assertEquals(0, assetLinks.size());
	}

	@Test
	public void testFindByE_EStartEnd() throws Exception {
		testFindByE_EStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByE_EStartEndWrongRange() throws Exception {
		testFindByE_EStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByE_EStartEndZeroZero() throws Exception {
		testFindByE_EStartEnd(0, 0, 0);
	}

	protected void testFindByE_EStartEnd(int start, int end, int expected)
		throws Exception {
		AssetLink assetLink = addAssetLink();

		long entryId1 = assetLink.getEntryId1();

		long entryId2 = assetLink.getEntryId2();

		List<AssetLink> assetLinks = _persistence.findByE_E(entryId1, entryId2,
				start, end);

		Assert.assertEquals(expected, assetLinks.size());
	}

	@Test
	public void testFindByE1_T() throws Exception {
		AssetLink assetLink = addAssetLink();

		long entryId1 = assetLink.getEntryId1();

		int type = assetLink.getType();

		List<AssetLink> assetLinks = _persistence.findByE1_T(entryId1, type);

		Assert.assertEquals(1, assetLinks.size());

		Assert.assertEquals(assetLink.getPrimaryKey(),
			assetLinks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByE1_TNotFound() throws Exception {
		addAssetLink();

		long entryId1 = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		List<AssetLink> assetLinks = _persistence.findByE1_T(entryId1, type);

		Assert.assertEquals(0, assetLinks.size());
	}

	@Test
	public void testFindByE1_TStartEnd() throws Exception {
		testFindByE1_TStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByE1_TStartEndWrongRange() throws Exception {
		testFindByE1_TStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByE1_TStartEndZeroZero() throws Exception {
		testFindByE1_TStartEnd(0, 0, 0);
	}

	protected void testFindByE1_TStartEnd(int start, int end, int expected)
		throws Exception {
		AssetLink assetLink = addAssetLink();

		long entryId1 = assetLink.getEntryId1();

		int type = assetLink.getType();

		List<AssetLink> assetLinks = _persistence.findByE1_T(entryId1, type,
				start, end);

		Assert.assertEquals(expected, assetLinks.size());
	}

	@Test
	public void testFindByE2_T() throws Exception {
		AssetLink assetLink = addAssetLink();

		long entryId2 = assetLink.getEntryId2();

		int type = assetLink.getType();

		List<AssetLink> assetLinks = _persistence.findByE2_T(entryId2, type);

		Assert.assertEquals(1, assetLinks.size());

		Assert.assertEquals(assetLink.getPrimaryKey(),
			assetLinks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByE2_TNotFound() throws Exception {
		addAssetLink();

		long entryId2 = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		List<AssetLink> assetLinks = _persistence.findByE2_T(entryId2, type);

		Assert.assertEquals(0, assetLinks.size());
	}

	@Test
	public void testFindByE2_TStartEnd() throws Exception {
		testFindByE2_TStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByE2_TStartEndWrongRange() throws Exception {
		testFindByE2_TStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByE2_TStartEndZeroZero() throws Exception {
		testFindByE2_TStartEnd(0, 0, 0);
	}

	protected void testFindByE2_TStartEnd(int start, int end, int expected)
		throws Exception {
		AssetLink assetLink = addAssetLink();

		long entryId2 = assetLink.getEntryId2();

		int type = assetLink.getType();

		List<AssetLink> assetLinks = _persistence.findByE2_T(entryId2, type,
				start, end);

		Assert.assertEquals(expected, assetLinks.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("AssetLink", "linkId", true,
			"companyId", true, "userId", true, "userName", true, "createDate",
			true, "entryId1", true, "entryId2", true, "type", true, "weight",
			true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		AssetLink newAssetLink = addAssetLink();

		AssetLink existingAssetLink = _persistence.fetchByPrimaryKey(newAssetLink.getPrimaryKey());

		Assert.assertEquals(existingAssetLink, newAssetLink);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetLink missingAssetLink = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingAssetLink);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new AssetLinkActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					AssetLink assetLink = (AssetLink)object;

					Assert.assertNotNull(assetLink);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		AssetLink newAssetLink = addAssetLink();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetLink.class,
				AssetLink.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("linkId",
				newAssetLink.getLinkId()));

		List<AssetLink> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		AssetLink existingAssetLink = result.get(0);

		Assert.assertEquals(existingAssetLink, newAssetLink);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetLink.class,
				AssetLink.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("linkId",
				ServiceTestUtil.nextLong()));

		List<AssetLink> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		AssetLink newAssetLink = addAssetLink();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetLink.class,
				AssetLink.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("linkId"));

		Object newLinkId = newAssetLink.getLinkId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("linkId",
				new Object[] { newLinkId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingLinkId = result.get(0);

		Assert.assertEquals(existingLinkId, newLinkId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetLink.class,
				AssetLink.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("linkId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("linkId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		AssetLink newAssetLink = addAssetLink();

		_persistence.clearCache();

		AssetLinkModelImpl existingAssetLinkModelImpl = (AssetLinkModelImpl)_persistence.findByPrimaryKey(newAssetLink.getPrimaryKey());

		Assert.assertEquals(existingAssetLinkModelImpl.getEntryId1(),
			existingAssetLinkModelImpl.getOriginalEntryId1());
		Assert.assertEquals(existingAssetLinkModelImpl.getEntryId2(),
			existingAssetLinkModelImpl.getOriginalEntryId2());
		Assert.assertEquals(existingAssetLinkModelImpl.getType(),
			existingAssetLinkModelImpl.getOriginalType());
	}

	protected AssetLink addAssetLink() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetLink assetLink = _persistence.create(pk);

		assetLink.setCompanyId(ServiceTestUtil.nextLong());

		assetLink.setUserId(ServiceTestUtil.nextLong());

		assetLink.setUserName(ServiceTestUtil.randomString());

		assetLink.setCreateDate(ServiceTestUtil.nextDate());

		assetLink.setEntryId1(ServiceTestUtil.nextLong());

		assetLink.setEntryId2(ServiceTestUtil.nextLong());

		assetLink.setType(ServiceTestUtil.nextInt());

		assetLink.setWeight(ServiceTestUtil.nextInt());

		_persistence.update(assetLink);

		return assetLink;
	}

	private static Log _log = LogFactoryUtil.getLog(AssetLinkPersistenceTest.class);
	private AssetLinkPersistence _persistence = (AssetLinkPersistence)PortalBeanLocatorUtil.locate(AssetLinkPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}