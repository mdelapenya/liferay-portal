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

import com.liferay.portal.NoSuchPortletPreferencesException;
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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.model.impl.PortletPreferencesModelImpl;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;
import com.liferay.portal.util.PropsValues;

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
public class PortletPreferencesPersistenceTest {
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

		PortletPreferences portletPreferences = _persistence.create(pk);

		Assert.assertNotNull(portletPreferences);

		Assert.assertEquals(portletPreferences.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		PortletPreferences newPortletPreferences = addPortletPreferences();

		_persistence.remove(newPortletPreferences);

		PortletPreferences existingPortletPreferences = _persistence.fetchByPrimaryKey(newPortletPreferences.getPrimaryKey());

		Assert.assertNull(existingPortletPreferences);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addPortletPreferences();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		PortletPreferences newPortletPreferences = _persistence.create(pk);

		newPortletPreferences.setOwnerId(ServiceTestUtil.nextLong());

		newPortletPreferences.setOwnerType(ServiceTestUtil.nextInt());

		newPortletPreferences.setPlid(ServiceTestUtil.nextLong());

		newPortletPreferences.setPortletId(ServiceTestUtil.randomString());

		newPortletPreferences.setPreferences(ServiceTestUtil.randomString());

		_persistence.update(newPortletPreferences);

		PortletPreferences existingPortletPreferences = _persistence.findByPrimaryKey(newPortletPreferences.getPrimaryKey());

		Assert.assertEquals(existingPortletPreferences.getPortletPreferencesId(),
			newPortletPreferences.getPortletPreferencesId());
		Assert.assertEquals(existingPortletPreferences.getOwnerId(),
			newPortletPreferences.getOwnerId());
		Assert.assertEquals(existingPortletPreferences.getOwnerType(),
			newPortletPreferences.getOwnerType());
		Assert.assertEquals(existingPortletPreferences.getPlid(),
			newPortletPreferences.getPlid());
		Assert.assertEquals(existingPortletPreferences.getPortletId(),
			newPortletPreferences.getPortletId());
		Assert.assertEquals(existingPortletPreferences.getPreferences(),
			newPortletPreferences.getPreferences());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		PortletPreferences newPortletPreferences = addPortletPreferences();

		PortletPreferences existingPortletPreferences = _persistence.findByPrimaryKey(newPortletPreferences.getPrimaryKey());

		Assert.assertEquals(existingPortletPreferences, newPortletPreferences);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchPortletPreferencesException");
		}
		catch (NoSuchPortletPreferencesException nsee) {
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
	public void testFindByPlid() throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		long plid = portletPreferences.getPlid();

		List<PortletPreferences> portletPreferenceses = _persistence.findByPlid(plid);

		Assert.assertEquals(1, portletPreferenceses.size());

		Assert.assertEquals(portletPreferences.getPrimaryKey(),
			portletPreferenceses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByPlidNotFound() throws Exception {
		addPortletPreferences();

		long plid = ServiceTestUtil.nextLong();

		List<PortletPreferences> portletPreferenceses = _persistence.findByPlid(plid);

		Assert.assertEquals(0, portletPreferenceses.size());
	}

	@Test
	public void testFindByPlidStartEnd() throws Exception {
		testFindByPlidStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByPlidStartEndWrongRange() throws Exception {
		testFindByPlidStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByPlidStartEndZeroZero() throws Exception {
		testFindByPlidStartEnd(0, 0, 0);
	}

	protected void testFindByPlidStartEnd(int start, int end, int expected)
		throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		long plid = portletPreferences.getPlid();

		List<PortletPreferences> portletPreferenceses = _persistence.findByPlid(plid,
				start, end);

		Assert.assertEquals(expected, portletPreferenceses.size());
	}

	@Test
	public void testFindByPortletId() throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		String portletId = portletPreferences.getPortletId();

		List<PortletPreferences> portletPreferenceses = _persistence.findByPortletId(portletId);

		Assert.assertEquals(1, portletPreferenceses.size());

		Assert.assertEquals(portletPreferences.getPrimaryKey(),
			portletPreferenceses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByPortletIdNotFound() throws Exception {
		addPortletPreferences();

		String portletId = ServiceTestUtil.randomString();

		List<PortletPreferences> portletPreferenceses = _persistence.findByPortletId(portletId);

		Assert.assertEquals(0, portletPreferenceses.size());
	}

	@Test
	public void testFindByPortletIdStartEnd() throws Exception {
		testFindByPortletIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByPortletIdStartEndWrongRange()
		throws Exception {
		testFindByPortletIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByPortletIdStartEndZeroZero() throws Exception {
		testFindByPortletIdStartEnd(0, 0, 0);
	}

	protected void testFindByPortletIdStartEnd(int start, int end, int expected)
		throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		String portletId = portletPreferences.getPortletId();

		List<PortletPreferences> portletPreferenceses = _persistence.findByPortletId(portletId,
				start, end);

		Assert.assertEquals(expected, portletPreferenceses.size());
	}

	@Test
	public void testFindByO_P() throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		int ownerType = portletPreferences.getOwnerType();

		String portletId = portletPreferences.getPortletId();

		List<PortletPreferences> portletPreferenceses = _persistence.findByO_P(ownerType,
				portletId);

		Assert.assertEquals(1, portletPreferenceses.size());

		Assert.assertEquals(portletPreferences.getPrimaryKey(),
			portletPreferenceses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByO_PNotFound() throws Exception {
		addPortletPreferences();

		int ownerType = ServiceTestUtil.nextInt();

		String portletId = ServiceTestUtil.randomString();

		List<PortletPreferences> portletPreferenceses = _persistence.findByO_P(ownerType,
				portletId);

		Assert.assertEquals(0, portletPreferenceses.size());
	}

	@Test
	public void testFindByO_PStartEnd() throws Exception {
		testFindByO_PStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByO_PStartEndWrongRange() throws Exception {
		testFindByO_PStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByO_PStartEndZeroZero() throws Exception {
		testFindByO_PStartEnd(0, 0, 0);
	}

	protected void testFindByO_PStartEnd(int start, int end, int expected)
		throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		int ownerType = portletPreferences.getOwnerType();

		String portletId = portletPreferences.getPortletId();

		List<PortletPreferences> portletPreferenceses = _persistence.findByO_P(ownerType,
				portletId, start, end);

		Assert.assertEquals(expected, portletPreferenceses.size());
	}

	@Test
	public void testFindByP_P() throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		long plid = portletPreferences.getPlid();

		String portletId = portletPreferences.getPortletId();

		List<PortletPreferences> portletPreferenceses = _persistence.findByP_P(plid,
				portletId);

		Assert.assertEquals(1, portletPreferenceses.size());

		Assert.assertEquals(portletPreferences.getPrimaryKey(),
			portletPreferenceses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByP_PNotFound() throws Exception {
		addPortletPreferences();

		long plid = ServiceTestUtil.nextLong();

		String portletId = ServiceTestUtil.randomString();

		List<PortletPreferences> portletPreferenceses = _persistence.findByP_P(plid,
				portletId);

		Assert.assertEquals(0, portletPreferenceses.size());
	}

	@Test
	public void testFindByP_PStartEnd() throws Exception {
		testFindByP_PStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByP_PStartEndWrongRange() throws Exception {
		testFindByP_PStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByP_PStartEndZeroZero() throws Exception {
		testFindByP_PStartEnd(0, 0, 0);
	}

	protected void testFindByP_PStartEnd(int start, int end, int expected)
		throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		long plid = portletPreferences.getPlid();

		String portletId = portletPreferences.getPortletId();

		List<PortletPreferences> portletPreferenceses = _persistence.findByP_P(plid,
				portletId, start, end);

		Assert.assertEquals(expected, portletPreferenceses.size());
	}

	@Test
	public void testFindByO_O_P() throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		long ownerId = portletPreferences.getOwnerId();

		int ownerType = portletPreferences.getOwnerType();

		long plid = portletPreferences.getPlid();

		List<PortletPreferences> portletPreferenceses = _persistence.findByO_O_P(ownerId,
				ownerType, plid);

		Assert.assertEquals(1, portletPreferenceses.size());

		Assert.assertEquals(portletPreferences.getPrimaryKey(),
			portletPreferenceses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByO_O_PNotFound() throws Exception {
		addPortletPreferences();

		long ownerId = ServiceTestUtil.nextLong();

		int ownerType = ServiceTestUtil.nextInt();

		long plid = ServiceTestUtil.nextLong();

		List<PortletPreferences> portletPreferenceses = _persistence.findByO_O_P(ownerId,
				ownerType, plid);

		Assert.assertEquals(0, portletPreferenceses.size());
	}

	@Test
	public void testFindByO_O_PStartEnd() throws Exception {
		testFindByO_O_PStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByO_O_PStartEndWrongRange() throws Exception {
		testFindByO_O_PStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByO_O_PStartEndZeroZero() throws Exception {
		testFindByO_O_PStartEnd(0, 0, 0);
	}

	protected void testFindByO_O_PStartEnd(int start, int end, int expected)
		throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		long ownerId = portletPreferences.getOwnerId();

		int ownerType = portletPreferences.getOwnerType();

		long plid = portletPreferences.getPlid();

		List<PortletPreferences> portletPreferenceses = _persistence.findByO_O_P(ownerId,
				ownerType, plid, start, end);

		Assert.assertEquals(expected, portletPreferenceses.size());
	}

	@Test
	public void testFindByO_O_PI() throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		long ownerId = portletPreferences.getOwnerId();

		int ownerType = portletPreferences.getOwnerType();

		String portletId = portletPreferences.getPortletId();

		List<PortletPreferences> portletPreferenceses = _persistence.findByO_O_PI(ownerId,
				ownerType, portletId);

		Assert.assertEquals(1, portletPreferenceses.size());

		Assert.assertEquals(portletPreferences.getPrimaryKey(),
			portletPreferenceses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByO_O_PINotFound() throws Exception {
		addPortletPreferences();

		long ownerId = ServiceTestUtil.nextLong();

		int ownerType = ServiceTestUtil.nextInt();

		String portletId = ServiceTestUtil.randomString();

		List<PortletPreferences> portletPreferenceses = _persistence.findByO_O_PI(ownerId,
				ownerType, portletId);

		Assert.assertEquals(0, portletPreferenceses.size());
	}

	@Test
	public void testFindByO_O_PIStartEnd() throws Exception {
		testFindByO_O_PIStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByO_O_PIStartEndWrongRange() throws Exception {
		testFindByO_O_PIStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByO_O_PIStartEndZeroZero() throws Exception {
		testFindByO_O_PIStartEnd(0, 0, 0);
	}

	protected void testFindByO_O_PIStartEnd(int start, int end, int expected)
		throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		long ownerId = portletPreferences.getOwnerId();

		int ownerType = portletPreferences.getOwnerType();

		String portletId = portletPreferences.getPortletId();

		List<PortletPreferences> portletPreferenceses = _persistence.findByO_O_PI(ownerId,
				ownerType, portletId, start, end);

		Assert.assertEquals(expected, portletPreferenceses.size());
	}

	@Test
	public void testFindByO_P_P() throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		int ownerType = portletPreferences.getOwnerType();

		long plid = portletPreferences.getPlid();

		String portletId = portletPreferences.getPortletId();

		List<PortletPreferences> portletPreferenceses = _persistence.findByO_P_P(ownerType,
				plid, portletId);

		Assert.assertEquals(1, portletPreferenceses.size());

		Assert.assertEquals(portletPreferences.getPrimaryKey(),
			portletPreferenceses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByO_P_PNotFound() throws Exception {
		addPortletPreferences();

		int ownerType = ServiceTestUtil.nextInt();

		long plid = ServiceTestUtil.nextLong();

		String portletId = ServiceTestUtil.randomString();

		List<PortletPreferences> portletPreferenceses = _persistence.findByO_P_P(ownerType,
				plid, portletId);

		Assert.assertEquals(0, portletPreferenceses.size());
	}

	@Test
	public void testFindByO_P_PStartEnd() throws Exception {
		testFindByO_P_PStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByO_P_PStartEndWrongRange() throws Exception {
		testFindByO_P_PStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByO_P_PStartEndZeroZero() throws Exception {
		testFindByO_P_PStartEnd(0, 0, 0);
	}

	protected void testFindByO_P_PStartEnd(int start, int end, int expected)
		throws Exception {
		PortletPreferences portletPreferences = addPortletPreferences();

		int ownerType = portletPreferences.getOwnerType();

		long plid = portletPreferences.getPlid();

		String portletId = portletPreferences.getPortletId();

		List<PortletPreferences> portletPreferenceses = _persistence.findByO_P_P(ownerType,
				plid, portletId, start, end);

		Assert.assertEquals(expected, portletPreferenceses.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("PortletPreferences",
			"portletPreferencesId", true, "ownerId", true, "ownerType", true,
			"plid", true, "portletId", true, "preferences", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		PortletPreferences newPortletPreferences = addPortletPreferences();

		PortletPreferences existingPortletPreferences = _persistence.fetchByPrimaryKey(newPortletPreferences.getPrimaryKey());

		Assert.assertEquals(existingPortletPreferences, newPortletPreferences);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		PortletPreferences missingPortletPreferences = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingPortletPreferences);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new PortletPreferencesActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					PortletPreferences portletPreferences = (PortletPreferences)object;

					Assert.assertNotNull(portletPreferences);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		PortletPreferences newPortletPreferences = addPortletPreferences();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(PortletPreferences.class,
				PortletPreferences.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("portletPreferencesId",
				newPortletPreferences.getPortletPreferencesId()));

		List<PortletPreferences> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		PortletPreferences existingPortletPreferences = result.get(0);

		Assert.assertEquals(existingPortletPreferences, newPortletPreferences);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(PortletPreferences.class,
				PortletPreferences.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("portletPreferencesId",
				ServiceTestUtil.nextLong()));

		List<PortletPreferences> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		PortletPreferences newPortletPreferences = addPortletPreferences();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(PortletPreferences.class,
				PortletPreferences.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"portletPreferencesId"));

		Object newPortletPreferencesId = newPortletPreferences.getPortletPreferencesId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("portletPreferencesId",
				new Object[] { newPortletPreferencesId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingPortletPreferencesId = result.get(0);

		Assert.assertEquals(existingPortletPreferencesId,
			newPortletPreferencesId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(PortletPreferences.class,
				PortletPreferences.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"portletPreferencesId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("portletPreferencesId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		PortletPreferences newPortletPreferences = addPortletPreferences();

		_persistence.clearCache();

		PortletPreferencesModelImpl existingPortletPreferencesModelImpl = (PortletPreferencesModelImpl)_persistence.findByPrimaryKey(newPortletPreferences.getPrimaryKey());

		Assert.assertEquals(existingPortletPreferencesModelImpl.getOwnerId(),
			existingPortletPreferencesModelImpl.getOriginalOwnerId());
		Assert.assertEquals(existingPortletPreferencesModelImpl.getOwnerType(),
			existingPortletPreferencesModelImpl.getOriginalOwnerType());
		Assert.assertEquals(existingPortletPreferencesModelImpl.getPlid(),
			existingPortletPreferencesModelImpl.getOriginalPlid());
		Assert.assertTrue(Validator.equals(
				existingPortletPreferencesModelImpl.getPortletId(),
				existingPortletPreferencesModelImpl.getOriginalPortletId()));
	}

	protected PortletPreferences addPortletPreferences()
		throws Exception {
		long pk = ServiceTestUtil.nextLong();

		PortletPreferences portletPreferences = _persistence.create(pk);

		portletPreferences.setOwnerId(ServiceTestUtil.nextLong());

		portletPreferences.setOwnerType(ServiceTestUtil.nextInt());

		portletPreferences.setPlid(ServiceTestUtil.nextLong());

		portletPreferences.setPortletId(ServiceTestUtil.randomString());

		portletPreferences.setPreferences(ServiceTestUtil.randomString());

		_persistence.update(portletPreferences);

		return portletPreferences;
	}

	private static Log _log = LogFactoryUtil.getLog(PortletPreferencesPersistenceTest.class);
	private PortletPreferencesPersistence _persistence = (PortletPreferencesPersistence)PortalBeanLocatorUtil.locate(PortletPreferencesPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}