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

import com.liferay.portal.NoSuchLayoutSetPrototypeException;
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
import com.liferay.portal.model.LayoutSetPrototype;
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
public class LayoutSetPrototypePersistenceTest {
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

		LayoutSetPrototype layoutSetPrototype = _persistence.create(pk);

		Assert.assertNotNull(layoutSetPrototype);

		Assert.assertEquals(layoutSetPrototype.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		LayoutSetPrototype newLayoutSetPrototype = addLayoutSetPrototype();

		_persistence.remove(newLayoutSetPrototype);

		LayoutSetPrototype existingLayoutSetPrototype = _persistence.fetchByPrimaryKey(newLayoutSetPrototype.getPrimaryKey());

		Assert.assertNull(existingLayoutSetPrototype);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addLayoutSetPrototype();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		LayoutSetPrototype newLayoutSetPrototype = _persistence.create(pk);

		newLayoutSetPrototype.setUuid(ServiceTestUtil.randomString());

		newLayoutSetPrototype.setCompanyId(ServiceTestUtil.nextLong());

		newLayoutSetPrototype.setUserId(ServiceTestUtil.nextLong());

		newLayoutSetPrototype.setUserName(ServiceTestUtil.randomString());

		newLayoutSetPrototype.setCreateDate(ServiceTestUtil.nextDate());

		newLayoutSetPrototype.setModifiedDate(ServiceTestUtil.nextDate());

		newLayoutSetPrototype.setName(ServiceTestUtil.randomString());

		newLayoutSetPrototype.setDescription(ServiceTestUtil.randomString());

		newLayoutSetPrototype.setSettings(ServiceTestUtil.randomString());

		newLayoutSetPrototype.setActive(ServiceTestUtil.randomBoolean());

		_persistence.update(newLayoutSetPrototype);

		LayoutSetPrototype existingLayoutSetPrototype = _persistence.findByPrimaryKey(newLayoutSetPrototype.getPrimaryKey());

		Assert.assertEquals(existingLayoutSetPrototype.getUuid(),
			newLayoutSetPrototype.getUuid());
		Assert.assertEquals(existingLayoutSetPrototype.getLayoutSetPrototypeId(),
			newLayoutSetPrototype.getLayoutSetPrototypeId());
		Assert.assertEquals(existingLayoutSetPrototype.getCompanyId(),
			newLayoutSetPrototype.getCompanyId());
		Assert.assertEquals(existingLayoutSetPrototype.getUserId(),
			newLayoutSetPrototype.getUserId());
		Assert.assertEquals(existingLayoutSetPrototype.getUserName(),
			newLayoutSetPrototype.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingLayoutSetPrototype.getCreateDate()),
			Time.getShortTimestamp(newLayoutSetPrototype.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingLayoutSetPrototype.getModifiedDate()),
			Time.getShortTimestamp(newLayoutSetPrototype.getModifiedDate()));
		Assert.assertEquals(existingLayoutSetPrototype.getName(),
			newLayoutSetPrototype.getName());
		Assert.assertEquals(existingLayoutSetPrototype.getDescription(),
			newLayoutSetPrototype.getDescription());
		Assert.assertEquals(existingLayoutSetPrototype.getSettings(),
			newLayoutSetPrototype.getSettings());
		Assert.assertEquals(existingLayoutSetPrototype.getActive(),
			newLayoutSetPrototype.getActive());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		LayoutSetPrototype newLayoutSetPrototype = addLayoutSetPrototype();

		LayoutSetPrototype existingLayoutSetPrototype = _persistence.findByPrimaryKey(newLayoutSetPrototype.getPrimaryKey());

		Assert.assertEquals(existingLayoutSetPrototype, newLayoutSetPrototype);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchLayoutSetPrototypeException");
		}
		catch (NoSuchLayoutSetPrototypeException nsee) {
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
		LayoutSetPrototype layoutSetPrototype = addLayoutSetPrototype();

		String uuid = layoutSetPrototype.getUuid();

		List<LayoutSetPrototype> layoutSetPrototypes = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, layoutSetPrototypes.size());

		Assert.assertEquals(layoutSetPrototype.getPrimaryKey(),
			layoutSetPrototypes.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addLayoutSetPrototype();

		String uuid = ServiceTestUtil.randomString();

		List<LayoutSetPrototype> layoutSetPrototypes = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, layoutSetPrototypes.size());
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
		LayoutSetPrototype layoutSetPrototype = addLayoutSetPrototype();

		String uuid = layoutSetPrototype.getUuid();

		List<LayoutSetPrototype> layoutSetPrototypes = _persistence.findByUuid(uuid,
				start, end);

		Assert.assertEquals(expected, layoutSetPrototypes.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		LayoutSetPrototype layoutSetPrototype = addLayoutSetPrototype();

		String uuid = layoutSetPrototype.getUuid();

		long companyId = layoutSetPrototype.getCompanyId();

		List<LayoutSetPrototype> layoutSetPrototypes = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(1, layoutSetPrototypes.size());

		Assert.assertEquals(layoutSetPrototype.getPrimaryKey(),
			layoutSetPrototypes.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addLayoutSetPrototype();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<LayoutSetPrototype> layoutSetPrototypes = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(0, layoutSetPrototypes.size());
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
		LayoutSetPrototype layoutSetPrototype = addLayoutSetPrototype();

		String uuid = layoutSetPrototype.getUuid();

		long companyId = layoutSetPrototype.getCompanyId();

		List<LayoutSetPrototype> layoutSetPrototypes = _persistence.findByUuid_C(uuid,
				companyId, start, end);

		Assert.assertEquals(expected, layoutSetPrototypes.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		LayoutSetPrototype layoutSetPrototype = addLayoutSetPrototype();

		long companyId = layoutSetPrototype.getCompanyId();

		List<LayoutSetPrototype> layoutSetPrototypes = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, layoutSetPrototypes.size());

		Assert.assertEquals(layoutSetPrototype.getPrimaryKey(),
			layoutSetPrototypes.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addLayoutSetPrototype();

		long companyId = ServiceTestUtil.nextLong();

		List<LayoutSetPrototype> layoutSetPrototypes = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, layoutSetPrototypes.size());
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
		LayoutSetPrototype layoutSetPrototype = addLayoutSetPrototype();

		long companyId = layoutSetPrototype.getCompanyId();

		List<LayoutSetPrototype> layoutSetPrototypes = _persistence.findByCompanyId(companyId,
				start, end);

		Assert.assertEquals(expected, layoutSetPrototypes.size());
	}

	@Test
	public void testFindByC_A() throws Exception {
		LayoutSetPrototype layoutSetPrototype = addLayoutSetPrototype();

		long companyId = layoutSetPrototype.getCompanyId();

		boolean active = layoutSetPrototype.getActive();

		List<LayoutSetPrototype> layoutSetPrototypes = _persistence.findByC_A(companyId,
				active);

		Assert.assertEquals(1, layoutSetPrototypes.size());

		Assert.assertEquals(layoutSetPrototype.getPrimaryKey(),
			layoutSetPrototypes.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_ANotFound() throws Exception {
		addLayoutSetPrototype();

		long companyId = ServiceTestUtil.nextLong();

		boolean active = ServiceTestUtil.randomBoolean();

		List<LayoutSetPrototype> layoutSetPrototypes = _persistence.findByC_A(companyId,
				active);

		Assert.assertEquals(0, layoutSetPrototypes.size());
	}

	@Test
	public void testFindByC_AStartEnd() throws Exception {
		testFindByC_AStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_AStartEndWrongRange() throws Exception {
		testFindByC_AStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_AStartEndZeroZero() throws Exception {
		testFindByC_AStartEnd(0, 0, 0);
	}

	protected void testFindByC_AStartEnd(int start, int end, int expected)
		throws Exception {
		LayoutSetPrototype layoutSetPrototype = addLayoutSetPrototype();

		long companyId = layoutSetPrototype.getCompanyId();

		boolean active = layoutSetPrototype.getActive();

		List<LayoutSetPrototype> layoutSetPrototypes = _persistence.findByC_A(companyId,
				active, start, end);

		Assert.assertEquals(expected, layoutSetPrototypes.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("LayoutSetPrototype",
			"uuid", true, "layoutSetPrototypeId", true, "companyId", true,
			"userId", true, "userName", true, "createDate", true,
			"modifiedDate", true, "name", true, "description", true,
			"settings", true, "active", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		LayoutSetPrototype newLayoutSetPrototype = addLayoutSetPrototype();

		LayoutSetPrototype existingLayoutSetPrototype = _persistence.fetchByPrimaryKey(newLayoutSetPrototype.getPrimaryKey());

		Assert.assertEquals(existingLayoutSetPrototype, newLayoutSetPrototype);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		LayoutSetPrototype missingLayoutSetPrototype = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingLayoutSetPrototype);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new LayoutSetPrototypeActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					LayoutSetPrototype layoutSetPrototype = (LayoutSetPrototype)object;

					Assert.assertNotNull(layoutSetPrototype);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		LayoutSetPrototype newLayoutSetPrototype = addLayoutSetPrototype();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(LayoutSetPrototype.class,
				LayoutSetPrototype.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("layoutSetPrototypeId",
				newLayoutSetPrototype.getLayoutSetPrototypeId()));

		List<LayoutSetPrototype> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		LayoutSetPrototype existingLayoutSetPrototype = result.get(0);

		Assert.assertEquals(existingLayoutSetPrototype, newLayoutSetPrototype);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(LayoutSetPrototype.class,
				LayoutSetPrototype.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("layoutSetPrototypeId",
				ServiceTestUtil.nextLong()));

		List<LayoutSetPrototype> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		LayoutSetPrototype newLayoutSetPrototype = addLayoutSetPrototype();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(LayoutSetPrototype.class,
				LayoutSetPrototype.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"layoutSetPrototypeId"));

		Object newLayoutSetPrototypeId = newLayoutSetPrototype.getLayoutSetPrototypeId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("layoutSetPrototypeId",
				new Object[] { newLayoutSetPrototypeId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingLayoutSetPrototypeId = result.get(0);

		Assert.assertEquals(existingLayoutSetPrototypeId,
			newLayoutSetPrototypeId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(LayoutSetPrototype.class,
				LayoutSetPrototype.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"layoutSetPrototypeId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("layoutSetPrototypeId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected LayoutSetPrototype addLayoutSetPrototype()
		throws Exception {
		long pk = ServiceTestUtil.nextLong();

		LayoutSetPrototype layoutSetPrototype = _persistence.create(pk);

		layoutSetPrototype.setUuid(ServiceTestUtil.randomString());

		layoutSetPrototype.setCompanyId(ServiceTestUtil.nextLong());

		layoutSetPrototype.setUserId(ServiceTestUtil.nextLong());

		layoutSetPrototype.setUserName(ServiceTestUtil.randomString());

		layoutSetPrototype.setCreateDate(ServiceTestUtil.nextDate());

		layoutSetPrototype.setModifiedDate(ServiceTestUtil.nextDate());

		layoutSetPrototype.setName(ServiceTestUtil.randomString());

		layoutSetPrototype.setDescription(ServiceTestUtil.randomString());

		layoutSetPrototype.setSettings(ServiceTestUtil.randomString());

		layoutSetPrototype.setActive(ServiceTestUtil.randomBoolean());

		_persistence.update(layoutSetPrototype);

		return layoutSetPrototype;
	}

	private static Log _log = LogFactoryUtil.getLog(LayoutSetPrototypePersistenceTest.class);
	private LayoutSetPrototypePersistence _persistence = (LayoutSetPrototypePersistence)PortalBeanLocatorUtil.locate(LayoutSetPrototypePersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}