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

import com.liferay.portal.NoSuchLayoutPrototypeException;
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
import com.liferay.portal.model.LayoutPrototype;
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
public class LayoutPrototypePersistenceTest {
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

		LayoutPrototype layoutPrototype = _persistence.create(pk);

		Assert.assertNotNull(layoutPrototype);

		Assert.assertEquals(layoutPrototype.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		LayoutPrototype newLayoutPrototype = addLayoutPrototype();

		_persistence.remove(newLayoutPrototype);

		LayoutPrototype existingLayoutPrototype = _persistence.fetchByPrimaryKey(newLayoutPrototype.getPrimaryKey());

		Assert.assertNull(existingLayoutPrototype);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addLayoutPrototype();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		LayoutPrototype newLayoutPrototype = _persistence.create(pk);

		newLayoutPrototype.setUuid(ServiceTestUtil.randomString());

		newLayoutPrototype.setCompanyId(ServiceTestUtil.nextLong());

		newLayoutPrototype.setUserId(ServiceTestUtil.nextLong());

		newLayoutPrototype.setUserName(ServiceTestUtil.randomString());

		newLayoutPrototype.setCreateDate(ServiceTestUtil.nextDate());

		newLayoutPrototype.setModifiedDate(ServiceTestUtil.nextDate());

		newLayoutPrototype.setName(ServiceTestUtil.randomString());

		newLayoutPrototype.setDescription(ServiceTestUtil.randomString());

		newLayoutPrototype.setSettings(ServiceTestUtil.randomString());

		newLayoutPrototype.setActive(ServiceTestUtil.randomBoolean());

		_persistence.update(newLayoutPrototype);

		LayoutPrototype existingLayoutPrototype = _persistence.findByPrimaryKey(newLayoutPrototype.getPrimaryKey());

		Assert.assertEquals(existingLayoutPrototype.getUuid(),
			newLayoutPrototype.getUuid());
		Assert.assertEquals(existingLayoutPrototype.getLayoutPrototypeId(),
			newLayoutPrototype.getLayoutPrototypeId());
		Assert.assertEquals(existingLayoutPrototype.getCompanyId(),
			newLayoutPrototype.getCompanyId());
		Assert.assertEquals(existingLayoutPrototype.getUserId(),
			newLayoutPrototype.getUserId());
		Assert.assertEquals(existingLayoutPrototype.getUserName(),
			newLayoutPrototype.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingLayoutPrototype.getCreateDate()),
			Time.getShortTimestamp(newLayoutPrototype.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingLayoutPrototype.getModifiedDate()),
			Time.getShortTimestamp(newLayoutPrototype.getModifiedDate()));
		Assert.assertEquals(existingLayoutPrototype.getName(),
			newLayoutPrototype.getName());
		Assert.assertEquals(existingLayoutPrototype.getDescription(),
			newLayoutPrototype.getDescription());
		Assert.assertEquals(existingLayoutPrototype.getSettings(),
			newLayoutPrototype.getSettings());
		Assert.assertEquals(existingLayoutPrototype.getActive(),
			newLayoutPrototype.getActive());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		LayoutPrototype newLayoutPrototype = addLayoutPrototype();

		LayoutPrototype existingLayoutPrototype = _persistence.findByPrimaryKey(newLayoutPrototype.getPrimaryKey());

		Assert.assertEquals(existingLayoutPrototype, newLayoutPrototype);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchLayoutPrototypeException");
		}
		catch (NoSuchLayoutPrototypeException nsee) {
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
		LayoutPrototype layoutPrototype = addLayoutPrototype();

		String uuid = layoutPrototype.getUuid();

		List<LayoutPrototype> layoutPrototypes = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, layoutPrototypes.size());

		Assert.assertEquals(layoutPrototype.getPrimaryKey(),
			layoutPrototypes.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addLayoutPrototype();

		String uuid = ServiceTestUtil.randomString();

		List<LayoutPrototype> layoutPrototypes = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, layoutPrototypes.size());
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
		LayoutPrototype layoutPrototype = addLayoutPrototype();

		String uuid = layoutPrototype.getUuid();

		List<LayoutPrototype> layoutPrototypes = _persistence.findByUuid(uuid,
				start, end);

		Assert.assertEquals(expected, layoutPrototypes.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		LayoutPrototype layoutPrototype = addLayoutPrototype();

		String uuid = layoutPrototype.getUuid();

		long companyId = layoutPrototype.getCompanyId();

		List<LayoutPrototype> layoutPrototypes = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(1, layoutPrototypes.size());

		Assert.assertEquals(layoutPrototype.getPrimaryKey(),
			layoutPrototypes.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addLayoutPrototype();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<LayoutPrototype> layoutPrototypes = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(0, layoutPrototypes.size());
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
		LayoutPrototype layoutPrototype = addLayoutPrototype();

		String uuid = layoutPrototype.getUuid();

		long companyId = layoutPrototype.getCompanyId();

		List<LayoutPrototype> layoutPrototypes = _persistence.findByUuid_C(uuid,
				companyId, start, end);

		Assert.assertEquals(expected, layoutPrototypes.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		LayoutPrototype layoutPrototype = addLayoutPrototype();

		long companyId = layoutPrototype.getCompanyId();

		List<LayoutPrototype> layoutPrototypes = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, layoutPrototypes.size());

		Assert.assertEquals(layoutPrototype.getPrimaryKey(),
			layoutPrototypes.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addLayoutPrototype();

		long companyId = ServiceTestUtil.nextLong();

		List<LayoutPrototype> layoutPrototypes = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, layoutPrototypes.size());
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
		LayoutPrototype layoutPrototype = addLayoutPrototype();

		long companyId = layoutPrototype.getCompanyId();

		List<LayoutPrototype> layoutPrototypes = _persistence.findByCompanyId(companyId,
				start, end);

		Assert.assertEquals(expected, layoutPrototypes.size());
	}

	@Test
	public void testFindByC_A() throws Exception {
		LayoutPrototype layoutPrototype = addLayoutPrototype();

		long companyId = layoutPrototype.getCompanyId();

		boolean active = layoutPrototype.getActive();

		List<LayoutPrototype> layoutPrototypes = _persistence.findByC_A(companyId,
				active);

		Assert.assertEquals(1, layoutPrototypes.size());

		Assert.assertEquals(layoutPrototype.getPrimaryKey(),
			layoutPrototypes.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_ANotFound() throws Exception {
		addLayoutPrototype();

		long companyId = ServiceTestUtil.nextLong();

		boolean active = ServiceTestUtil.randomBoolean();

		List<LayoutPrototype> layoutPrototypes = _persistence.findByC_A(companyId,
				active);

		Assert.assertEquals(0, layoutPrototypes.size());
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
		LayoutPrototype layoutPrototype = addLayoutPrototype();

		long companyId = layoutPrototype.getCompanyId();

		boolean active = layoutPrototype.getActive();

		List<LayoutPrototype> layoutPrototypes = _persistence.findByC_A(companyId,
				active, start, end);

		Assert.assertEquals(expected, layoutPrototypes.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("LayoutPrototype", "uuid",
			true, "layoutPrototypeId", true, "companyId", true, "userId", true,
			"userName", true, "createDate", true, "modifiedDate", true, "name",
			true, "description", true, "settings", true, "active", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		LayoutPrototype newLayoutPrototype = addLayoutPrototype();

		LayoutPrototype existingLayoutPrototype = _persistence.fetchByPrimaryKey(newLayoutPrototype.getPrimaryKey());

		Assert.assertEquals(existingLayoutPrototype, newLayoutPrototype);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		LayoutPrototype missingLayoutPrototype = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingLayoutPrototype);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new LayoutPrototypeActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					LayoutPrototype layoutPrototype = (LayoutPrototype)object;

					Assert.assertNotNull(layoutPrototype);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		LayoutPrototype newLayoutPrototype = addLayoutPrototype();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(LayoutPrototype.class,
				LayoutPrototype.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("layoutPrototypeId",
				newLayoutPrototype.getLayoutPrototypeId()));

		List<LayoutPrototype> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		LayoutPrototype existingLayoutPrototype = result.get(0);

		Assert.assertEquals(existingLayoutPrototype, newLayoutPrototype);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(LayoutPrototype.class,
				LayoutPrototype.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("layoutPrototypeId",
				ServiceTestUtil.nextLong()));

		List<LayoutPrototype> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		LayoutPrototype newLayoutPrototype = addLayoutPrototype();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(LayoutPrototype.class,
				LayoutPrototype.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"layoutPrototypeId"));

		Object newLayoutPrototypeId = newLayoutPrototype.getLayoutPrototypeId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("layoutPrototypeId",
				new Object[] { newLayoutPrototypeId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingLayoutPrototypeId = result.get(0);

		Assert.assertEquals(existingLayoutPrototypeId, newLayoutPrototypeId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(LayoutPrototype.class,
				LayoutPrototype.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"layoutPrototypeId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("layoutPrototypeId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected LayoutPrototype addLayoutPrototype() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		LayoutPrototype layoutPrototype = _persistence.create(pk);

		layoutPrototype.setUuid(ServiceTestUtil.randomString());

		layoutPrototype.setCompanyId(ServiceTestUtil.nextLong());

		layoutPrototype.setUserId(ServiceTestUtil.nextLong());

		layoutPrototype.setUserName(ServiceTestUtil.randomString());

		layoutPrototype.setCreateDate(ServiceTestUtil.nextDate());

		layoutPrototype.setModifiedDate(ServiceTestUtil.nextDate());

		layoutPrototype.setName(ServiceTestUtil.randomString());

		layoutPrototype.setDescription(ServiceTestUtil.randomString());

		layoutPrototype.setSettings(ServiceTestUtil.randomString());

		layoutPrototype.setActive(ServiceTestUtil.randomBoolean());

		_persistence.update(layoutPrototype);

		return layoutPrototype;
	}

	private static Log _log = LogFactoryUtil.getLog(LayoutPrototypePersistenceTest.class);
	private LayoutPrototypePersistence _persistence = (LayoutPrototypePersistence)PortalBeanLocatorUtil.locate(LayoutPrototypePersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}