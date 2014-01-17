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

package com.liferay.portlet.social.service.persistence;

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
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;
import com.liferay.portal.util.PropsValues;

import com.liferay.portlet.social.NoSuchRelationException;
import com.liferay.portlet.social.model.SocialRelation;
import com.liferay.portlet.social.model.impl.SocialRelationModelImpl;

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
public class SocialRelationPersistenceTest {
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

		SocialRelation socialRelation = _persistence.create(pk);

		Assert.assertNotNull(socialRelation);

		Assert.assertEquals(socialRelation.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		SocialRelation newSocialRelation = addSocialRelation();

		_persistence.remove(newSocialRelation);

		SocialRelation existingSocialRelation = _persistence.fetchByPrimaryKey(newSocialRelation.getPrimaryKey());

		Assert.assertNull(existingSocialRelation);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addSocialRelation();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SocialRelation newSocialRelation = _persistence.create(pk);

		newSocialRelation.setUuid(ServiceTestUtil.randomString());

		newSocialRelation.setCompanyId(ServiceTestUtil.nextLong());

		newSocialRelation.setCreateDate(ServiceTestUtil.nextLong());

		newSocialRelation.setUserId1(ServiceTestUtil.nextLong());

		newSocialRelation.setUserId2(ServiceTestUtil.nextLong());

		newSocialRelation.setType(ServiceTestUtil.nextInt());

		_persistence.update(newSocialRelation);

		SocialRelation existingSocialRelation = _persistence.findByPrimaryKey(newSocialRelation.getPrimaryKey());

		Assert.assertEquals(existingSocialRelation.getUuid(),
			newSocialRelation.getUuid());
		Assert.assertEquals(existingSocialRelation.getRelationId(),
			newSocialRelation.getRelationId());
		Assert.assertEquals(existingSocialRelation.getCompanyId(),
			newSocialRelation.getCompanyId());
		Assert.assertEquals(existingSocialRelation.getCreateDate(),
			newSocialRelation.getCreateDate());
		Assert.assertEquals(existingSocialRelation.getUserId1(),
			newSocialRelation.getUserId1());
		Assert.assertEquals(existingSocialRelation.getUserId2(),
			newSocialRelation.getUserId2());
		Assert.assertEquals(existingSocialRelation.getType(),
			newSocialRelation.getType());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		SocialRelation newSocialRelation = addSocialRelation();

		SocialRelation existingSocialRelation = _persistence.findByPrimaryKey(newSocialRelation.getPrimaryKey());

		Assert.assertEquals(existingSocialRelation, newSocialRelation);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchRelationException");
		}
		catch (NoSuchRelationException nsee) {
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
		SocialRelation socialRelation = addSocialRelation();

		String uuid = socialRelation.getUuid();

		List<SocialRelation> socialRelations = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, socialRelations.size());

		Assert.assertEquals(socialRelation.getPrimaryKey(),
			socialRelations.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addSocialRelation();

		String uuid = ServiceTestUtil.randomString();

		List<SocialRelation> socialRelations = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, socialRelations.size());
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
		SocialRelation socialRelation = addSocialRelation();

		String uuid = socialRelation.getUuid();

		List<SocialRelation> socialRelations = _persistence.findByUuid(uuid,
				start, end);

		Assert.assertEquals(expected, socialRelations.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		String uuid = socialRelation.getUuid();

		long companyId = socialRelation.getCompanyId();

		List<SocialRelation> socialRelations = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(1, socialRelations.size());

		Assert.assertEquals(socialRelation.getPrimaryKey(),
			socialRelations.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addSocialRelation();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<SocialRelation> socialRelations = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(0, socialRelations.size());
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
		SocialRelation socialRelation = addSocialRelation();

		String uuid = socialRelation.getUuid();

		long companyId = socialRelation.getCompanyId();

		List<SocialRelation> socialRelations = _persistence.findByUuid_C(uuid,
				companyId, start, end);

		Assert.assertEquals(expected, socialRelations.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		long companyId = socialRelation.getCompanyId();

		List<SocialRelation> socialRelations = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, socialRelations.size());

		Assert.assertEquals(socialRelation.getPrimaryKey(),
			socialRelations.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addSocialRelation();

		long companyId = ServiceTestUtil.nextLong();

		List<SocialRelation> socialRelations = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, socialRelations.size());
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
		SocialRelation socialRelation = addSocialRelation();

		long companyId = socialRelation.getCompanyId();

		List<SocialRelation> socialRelations = _persistence.findByCompanyId(companyId,
				start, end);

		Assert.assertEquals(expected, socialRelations.size());
	}

	@Test
	public void testFindByUserId1() throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		long userId1 = socialRelation.getUserId1();

		List<SocialRelation> socialRelations = _persistence.findByUserId1(userId1);

		Assert.assertEquals(1, socialRelations.size());

		Assert.assertEquals(socialRelation.getPrimaryKey(),
			socialRelations.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUserId1NotFound() throws Exception {
		addSocialRelation();

		long userId1 = ServiceTestUtil.nextLong();

		List<SocialRelation> socialRelations = _persistence.findByUserId1(userId1);

		Assert.assertEquals(0, socialRelations.size());
	}

	@Test
	public void testFindByUserId1StartEnd() throws Exception {
		testFindByUserId1StartEnd(0, 5, 1);
	}

	@Test
	public void testFindByUserId1StartEndWrongRange() throws Exception {
		testFindByUserId1StartEnd(5, 0, 0);
	}

	@Test
	public void testFindByUserId1StartEndZeroZero() throws Exception {
		testFindByUserId1StartEnd(0, 0, 0);
	}

	protected void testFindByUserId1StartEnd(int start, int end, int expected)
		throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		long userId1 = socialRelation.getUserId1();

		List<SocialRelation> socialRelations = _persistence.findByUserId1(userId1,
				start, end);

		Assert.assertEquals(expected, socialRelations.size());
	}

	@Test
	public void testFindByUserId2() throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		long userId2 = socialRelation.getUserId2();

		List<SocialRelation> socialRelations = _persistence.findByUserId2(userId2);

		Assert.assertEquals(1, socialRelations.size());

		Assert.assertEquals(socialRelation.getPrimaryKey(),
			socialRelations.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUserId2NotFound() throws Exception {
		addSocialRelation();

		long userId2 = ServiceTestUtil.nextLong();

		List<SocialRelation> socialRelations = _persistence.findByUserId2(userId2);

		Assert.assertEquals(0, socialRelations.size());
	}

	@Test
	public void testFindByUserId2StartEnd() throws Exception {
		testFindByUserId2StartEnd(0, 5, 1);
	}

	@Test
	public void testFindByUserId2StartEndWrongRange() throws Exception {
		testFindByUserId2StartEnd(5, 0, 0);
	}

	@Test
	public void testFindByUserId2StartEndZeroZero() throws Exception {
		testFindByUserId2StartEnd(0, 0, 0);
	}

	protected void testFindByUserId2StartEnd(int start, int end, int expected)
		throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		long userId2 = socialRelation.getUserId2();

		List<SocialRelation> socialRelations = _persistence.findByUserId2(userId2,
				start, end);

		Assert.assertEquals(expected, socialRelations.size());
	}

	@Test
	public void testFindByType() throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		int type = socialRelation.getType();

		List<SocialRelation> socialRelations = _persistence.findByType(type);

		Assert.assertEquals(1, socialRelations.size());

		Assert.assertEquals(socialRelation.getPrimaryKey(),
			socialRelations.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByTypeNotFound() throws Exception {
		addSocialRelation();

		int type = ServiceTestUtil.nextInt();

		List<SocialRelation> socialRelations = _persistence.findByType(type);

		Assert.assertEquals(0, socialRelations.size());
	}

	@Test
	public void testFindByTypeStartEnd() throws Exception {
		testFindByTypeStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByTypeStartEndWrongRange() throws Exception {
		testFindByTypeStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByTypeStartEndZeroZero() throws Exception {
		testFindByTypeStartEnd(0, 0, 0);
	}

	protected void testFindByTypeStartEnd(int start, int end, int expected)
		throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		int type = socialRelation.getType();

		List<SocialRelation> socialRelations = _persistence.findByType(type,
				start, end);

		Assert.assertEquals(expected, socialRelations.size());
	}

	@Test
	public void testFindByC_T() throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		long companyId = socialRelation.getCompanyId();

		int type = socialRelation.getType();

		List<SocialRelation> socialRelations = _persistence.findByC_T(companyId,
				type);

		Assert.assertEquals(1, socialRelations.size());

		Assert.assertEquals(socialRelation.getPrimaryKey(),
			socialRelations.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_TNotFound() throws Exception {
		addSocialRelation();

		long companyId = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		List<SocialRelation> socialRelations = _persistence.findByC_T(companyId,
				type);

		Assert.assertEquals(0, socialRelations.size());
	}

	@Test
	public void testFindByC_TStartEnd() throws Exception {
		testFindByC_TStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_TStartEndWrongRange() throws Exception {
		testFindByC_TStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_TStartEndZeroZero() throws Exception {
		testFindByC_TStartEnd(0, 0, 0);
	}

	protected void testFindByC_TStartEnd(int start, int end, int expected)
		throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		long companyId = socialRelation.getCompanyId();

		int type = socialRelation.getType();

		List<SocialRelation> socialRelations = _persistence.findByC_T(companyId,
				type, start, end);

		Assert.assertEquals(expected, socialRelations.size());
	}

	@Test
	public void testFindByU1_U2() throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		long userId1 = socialRelation.getUserId1();

		long userId2 = socialRelation.getUserId2();

		List<SocialRelation> socialRelations = _persistence.findByU1_U2(userId1,
				userId2);

		Assert.assertEquals(1, socialRelations.size());

		Assert.assertEquals(socialRelation.getPrimaryKey(),
			socialRelations.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByU1_U2NotFound() throws Exception {
		addSocialRelation();

		long userId1 = ServiceTestUtil.nextLong();

		long userId2 = ServiceTestUtil.nextLong();

		List<SocialRelation> socialRelations = _persistence.findByU1_U2(userId1,
				userId2);

		Assert.assertEquals(0, socialRelations.size());
	}

	@Test
	public void testFindByU1_U2StartEnd() throws Exception {
		testFindByU1_U2StartEnd(0, 5, 1);
	}

	@Test
	public void testFindByU1_U2StartEndWrongRange() throws Exception {
		testFindByU1_U2StartEnd(5, 0, 0);
	}

	@Test
	public void testFindByU1_U2StartEndZeroZero() throws Exception {
		testFindByU1_U2StartEnd(0, 0, 0);
	}

	protected void testFindByU1_U2StartEnd(int start, int end, int expected)
		throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		long userId1 = socialRelation.getUserId1();

		long userId2 = socialRelation.getUserId2();

		List<SocialRelation> socialRelations = _persistence.findByU1_U2(userId1,
				userId2, start, end);

		Assert.assertEquals(expected, socialRelations.size());
	}

	@Test
	public void testFindByU1_T() throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		long userId1 = socialRelation.getUserId1();

		int type = socialRelation.getType();

		List<SocialRelation> socialRelations = _persistence.findByU1_T(userId1,
				type);

		Assert.assertEquals(1, socialRelations.size());

		Assert.assertEquals(socialRelation.getPrimaryKey(),
			socialRelations.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByU1_TNotFound() throws Exception {
		addSocialRelation();

		long userId1 = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		List<SocialRelation> socialRelations = _persistence.findByU1_T(userId1,
				type);

		Assert.assertEquals(0, socialRelations.size());
	}

	@Test
	public void testFindByU1_TStartEnd() throws Exception {
		testFindByU1_TStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByU1_TStartEndWrongRange() throws Exception {
		testFindByU1_TStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByU1_TStartEndZeroZero() throws Exception {
		testFindByU1_TStartEnd(0, 0, 0);
	}

	protected void testFindByU1_TStartEnd(int start, int end, int expected)
		throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		long userId1 = socialRelation.getUserId1();

		int type = socialRelation.getType();

		List<SocialRelation> socialRelations = _persistence.findByU1_T(userId1,
				type, start, end);

		Assert.assertEquals(expected, socialRelations.size());
	}

	@Test
	public void testFindByU2_T() throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		long userId2 = socialRelation.getUserId2();

		int type = socialRelation.getType();

		List<SocialRelation> socialRelations = _persistence.findByU2_T(userId2,
				type);

		Assert.assertEquals(1, socialRelations.size());

		Assert.assertEquals(socialRelation.getPrimaryKey(),
			socialRelations.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByU2_TNotFound() throws Exception {
		addSocialRelation();

		long userId2 = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		List<SocialRelation> socialRelations = _persistence.findByU2_T(userId2,
				type);

		Assert.assertEquals(0, socialRelations.size());
	}

	@Test
	public void testFindByU2_TStartEnd() throws Exception {
		testFindByU2_TStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByU2_TStartEndWrongRange() throws Exception {
		testFindByU2_TStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByU2_TStartEndZeroZero() throws Exception {
		testFindByU2_TStartEnd(0, 0, 0);
	}

	protected void testFindByU2_TStartEnd(int start, int end, int expected)
		throws Exception {
		SocialRelation socialRelation = addSocialRelation();

		long userId2 = socialRelation.getUserId2();

		int type = socialRelation.getType();

		List<SocialRelation> socialRelations = _persistence.findByU2_T(userId2,
				type, start, end);

		Assert.assertEquals(expected, socialRelations.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("SocialRelation", "uuid",
			true, "relationId", true, "companyId", true, "createDate", true,
			"userId1", true, "userId2", true, "type", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		SocialRelation newSocialRelation = addSocialRelation();

		SocialRelation existingSocialRelation = _persistence.fetchByPrimaryKey(newSocialRelation.getPrimaryKey());

		Assert.assertEquals(existingSocialRelation, newSocialRelation);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SocialRelation missingSocialRelation = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingSocialRelation);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new SocialRelationActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					SocialRelation socialRelation = (SocialRelation)object;

					Assert.assertNotNull(socialRelation);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		SocialRelation newSocialRelation = addSocialRelation();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialRelation.class,
				SocialRelation.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("relationId",
				newSocialRelation.getRelationId()));

		List<SocialRelation> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		SocialRelation existingSocialRelation = result.get(0);

		Assert.assertEquals(existingSocialRelation, newSocialRelation);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialRelation.class,
				SocialRelation.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("relationId",
				ServiceTestUtil.nextLong()));

		List<SocialRelation> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		SocialRelation newSocialRelation = addSocialRelation();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialRelation.class,
				SocialRelation.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("relationId"));

		Object newRelationId = newSocialRelation.getRelationId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("relationId",
				new Object[] { newRelationId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingRelationId = result.get(0);

		Assert.assertEquals(existingRelationId, newRelationId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialRelation.class,
				SocialRelation.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("relationId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("relationId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		SocialRelation newSocialRelation = addSocialRelation();

		_persistence.clearCache();

		SocialRelationModelImpl existingSocialRelationModelImpl = (SocialRelationModelImpl)_persistence.findByPrimaryKey(newSocialRelation.getPrimaryKey());

		Assert.assertEquals(existingSocialRelationModelImpl.getUserId1(),
			existingSocialRelationModelImpl.getOriginalUserId1());
		Assert.assertEquals(existingSocialRelationModelImpl.getUserId2(),
			existingSocialRelationModelImpl.getOriginalUserId2());
		Assert.assertEquals(existingSocialRelationModelImpl.getType(),
			existingSocialRelationModelImpl.getOriginalType());
	}

	protected SocialRelation addSocialRelation() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SocialRelation socialRelation = _persistence.create(pk);

		socialRelation.setUuid(ServiceTestUtil.randomString());

		socialRelation.setCompanyId(ServiceTestUtil.nextLong());

		socialRelation.setCreateDate(ServiceTestUtil.nextLong());

		socialRelation.setUserId1(ServiceTestUtil.nextLong());

		socialRelation.setUserId2(ServiceTestUtil.nextLong());

		socialRelation.setType(ServiceTestUtil.nextInt());

		_persistence.update(socialRelation);

		return socialRelation;
	}

	private static Log _log = LogFactoryUtil.getLog(SocialRelationPersistenceTest.class);
	private SocialRelationPersistence _persistence = (SocialRelationPersistence)PortalBeanLocatorUtil.locate(SocialRelationPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}