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

import com.liferay.portal.NoSuchRegionException;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Region;
import com.liferay.portal.model.impl.RegionModelImpl;
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
public class RegionPersistenceTest {
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

		Region region = _persistence.create(pk);

		Assert.assertNotNull(region);

		Assert.assertEquals(region.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		Region newRegion = addRegion();

		_persistence.remove(newRegion);

		Region existingRegion = _persistence.fetchByPrimaryKey(newRegion.getPrimaryKey());

		Assert.assertNull(existingRegion);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addRegion();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Region newRegion = _persistence.create(pk);

		newRegion.setCountryId(ServiceTestUtil.nextLong());

		newRegion.setRegionCode(ServiceTestUtil.randomString());

		newRegion.setName(ServiceTestUtil.randomString());

		newRegion.setActive(ServiceTestUtil.randomBoolean());

		_persistence.update(newRegion);

		Region existingRegion = _persistence.findByPrimaryKey(newRegion.getPrimaryKey());

		Assert.assertEquals(existingRegion.getRegionId(),
			newRegion.getRegionId());
		Assert.assertEquals(existingRegion.getCountryId(),
			newRegion.getCountryId());
		Assert.assertEquals(existingRegion.getRegionCode(),
			newRegion.getRegionCode());
		Assert.assertEquals(existingRegion.getName(), newRegion.getName());
		Assert.assertEquals(existingRegion.getActive(), newRegion.getActive());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		Region newRegion = addRegion();

		Region existingRegion = _persistence.findByPrimaryKey(newRegion.getPrimaryKey());

		Assert.assertEquals(existingRegion, newRegion);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchRegionException");
		}
		catch (NoSuchRegionException nsee) {
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
	public void testFindByCountryId() throws Exception {
		Region region = addRegion();

		long countryId = region.getCountryId();

		List<Region> regions = _persistence.findByCountryId(countryId);

		Assert.assertEquals(1, regions.size());

		Assert.assertEquals(region.getPrimaryKey(),
			regions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCountryIdNotFound() throws Exception {
		addRegion();

		long countryId = ServiceTestUtil.nextLong();

		List<Region> regions = _persistence.findByCountryId(countryId);

		Assert.assertEquals(0, regions.size());
	}

	@Test
	public void testFindByCountryIdStartEnd() throws Exception {
		testFindByCountryIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByCountryIdStartEndWrongRange()
		throws Exception {
		testFindByCountryIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByCountryIdStartEndZeroZero() throws Exception {
		testFindByCountryIdStartEnd(0, 0, 0);
	}

	protected void testFindByCountryIdStartEnd(int start, int end, int expected)
		throws Exception {
		Region region = addRegion();

		long countryId = region.getCountryId();

		List<Region> regions = _persistence.findByCountryId(countryId, start,
				end);

		Assert.assertEquals(expected, regions.size());
	}

	@Test
	public void testFindByActive() throws Exception {
		Region region = addRegion();

		boolean active = region.getActive();

		List<Region> regions = _persistence.findByActive(active);

		Assert.assertEquals(1, regions.size());

		Assert.assertEquals(region.getPrimaryKey(),
			regions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByActiveNotFound() throws Exception {
		addRegion();

		boolean active = ServiceTestUtil.randomBoolean();

		List<Region> regions = _persistence.findByActive(active);

		Assert.assertEquals(0, regions.size());
	}

	@Test
	public void testFindByActiveStartEnd() throws Exception {
		testFindByActiveStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByActiveStartEndWrongRange() throws Exception {
		testFindByActiveStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByActiveStartEndZeroZero() throws Exception {
		testFindByActiveStartEnd(0, 0, 0);
	}

	protected void testFindByActiveStartEnd(int start, int end, int expected)
		throws Exception {
		Region region = addRegion();

		boolean active = region.getActive();

		List<Region> regions = _persistence.findByActive(active, start, end);

		Assert.assertEquals(expected, regions.size());
	}

	@Test
	public void testFindByC_A() throws Exception {
		Region region = addRegion();

		long countryId = region.getCountryId();

		boolean active = region.getActive();

		List<Region> regions = _persistence.findByC_A(countryId, active);

		Assert.assertEquals(1, regions.size());

		Assert.assertEquals(region.getPrimaryKey(),
			regions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_ANotFound() throws Exception {
		addRegion();

		long countryId = ServiceTestUtil.nextLong();

		boolean active = ServiceTestUtil.randomBoolean();

		List<Region> regions = _persistence.findByC_A(countryId, active);

		Assert.assertEquals(0, regions.size());
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
		Region region = addRegion();

		long countryId = region.getCountryId();

		boolean active = region.getActive();

		List<Region> regions = _persistence.findByC_A(countryId, active, start,
				end);

		Assert.assertEquals(expected, regions.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("Region", "regionId", true,
			"countryId", true, "regionCode", true, "name", true, "active", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		Region newRegion = addRegion();

		Region existingRegion = _persistence.fetchByPrimaryKey(newRegion.getPrimaryKey());

		Assert.assertEquals(existingRegion, newRegion);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Region missingRegion = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingRegion);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		Region newRegion = addRegion();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Region.class,
				Region.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("regionId",
				newRegion.getRegionId()));

		List<Region> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Region existingRegion = result.get(0);

		Assert.assertEquals(existingRegion, newRegion);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Region.class,
				Region.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("regionId",
				ServiceTestUtil.nextLong()));

		List<Region> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		Region newRegion = addRegion();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Region.class,
				Region.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("regionId"));

		Object newRegionId = newRegion.getRegionId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("regionId",
				new Object[] { newRegionId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingRegionId = result.get(0);

		Assert.assertEquals(existingRegionId, newRegionId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Region.class,
				Region.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("regionId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("regionId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		Region newRegion = addRegion();

		_persistence.clearCache();

		RegionModelImpl existingRegionModelImpl = (RegionModelImpl)_persistence.findByPrimaryKey(newRegion.getPrimaryKey());

		Assert.assertEquals(existingRegionModelImpl.getCountryId(),
			existingRegionModelImpl.getOriginalCountryId());
		Assert.assertTrue(Validator.equals(
				existingRegionModelImpl.getRegionCode(),
				existingRegionModelImpl.getOriginalRegionCode()));
	}

	protected Region addRegion() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Region region = _persistence.create(pk);

		region.setCountryId(ServiceTestUtil.nextLong());

		region.setRegionCode(ServiceTestUtil.randomString());

		region.setName(ServiceTestUtil.randomString());

		region.setActive(ServiceTestUtil.randomBoolean());

		_persistence.update(region);

		return region;
	}

	private static Log _log = LogFactoryUtil.getLog(RegionPersistenceTest.class);
	private RegionPersistence _persistence = (RegionPersistence)PortalBeanLocatorUtil.locate(RegionPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}