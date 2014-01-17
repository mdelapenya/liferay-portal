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

import com.liferay.portlet.social.NoSuchActivitySetException;
import com.liferay.portlet.social.model.SocialActivitySet;

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
public class SocialActivitySetPersistenceTest {
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

		SocialActivitySet socialActivitySet = _persistence.create(pk);

		Assert.assertNotNull(socialActivitySet);

		Assert.assertEquals(socialActivitySet.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		SocialActivitySet newSocialActivitySet = addSocialActivitySet();

		_persistence.remove(newSocialActivitySet);

		SocialActivitySet existingSocialActivitySet = _persistence.fetchByPrimaryKey(newSocialActivitySet.getPrimaryKey());

		Assert.assertNull(existingSocialActivitySet);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addSocialActivitySet();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SocialActivitySet newSocialActivitySet = _persistence.create(pk);

		newSocialActivitySet.setGroupId(ServiceTestUtil.nextLong());

		newSocialActivitySet.setCompanyId(ServiceTestUtil.nextLong());

		newSocialActivitySet.setUserId(ServiceTestUtil.nextLong());

		newSocialActivitySet.setCreateDate(ServiceTestUtil.nextLong());

		newSocialActivitySet.setModifiedDate(ServiceTestUtil.nextLong());

		newSocialActivitySet.setClassNameId(ServiceTestUtil.nextLong());

		newSocialActivitySet.setClassPK(ServiceTestUtil.nextLong());

		newSocialActivitySet.setType(ServiceTestUtil.nextInt());

		newSocialActivitySet.setExtraData(ServiceTestUtil.randomString());

		newSocialActivitySet.setActivityCount(ServiceTestUtil.nextInt());

		_persistence.update(newSocialActivitySet);

		SocialActivitySet existingSocialActivitySet = _persistence.findByPrimaryKey(newSocialActivitySet.getPrimaryKey());

		Assert.assertEquals(existingSocialActivitySet.getActivitySetId(),
			newSocialActivitySet.getActivitySetId());
		Assert.assertEquals(existingSocialActivitySet.getGroupId(),
			newSocialActivitySet.getGroupId());
		Assert.assertEquals(existingSocialActivitySet.getCompanyId(),
			newSocialActivitySet.getCompanyId());
		Assert.assertEquals(existingSocialActivitySet.getUserId(),
			newSocialActivitySet.getUserId());
		Assert.assertEquals(existingSocialActivitySet.getCreateDate(),
			newSocialActivitySet.getCreateDate());
		Assert.assertEquals(existingSocialActivitySet.getModifiedDate(),
			newSocialActivitySet.getModifiedDate());
		Assert.assertEquals(existingSocialActivitySet.getClassNameId(),
			newSocialActivitySet.getClassNameId());
		Assert.assertEquals(existingSocialActivitySet.getClassPK(),
			newSocialActivitySet.getClassPK());
		Assert.assertEquals(existingSocialActivitySet.getType(),
			newSocialActivitySet.getType());
		Assert.assertEquals(existingSocialActivitySet.getExtraData(),
			newSocialActivitySet.getExtraData());
		Assert.assertEquals(existingSocialActivitySet.getActivityCount(),
			newSocialActivitySet.getActivityCount());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		SocialActivitySet newSocialActivitySet = addSocialActivitySet();

		SocialActivitySet existingSocialActivitySet = _persistence.findByPrimaryKey(newSocialActivitySet.getPrimaryKey());

		Assert.assertEquals(existingSocialActivitySet, newSocialActivitySet);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchActivitySetException");
		}
		catch (NoSuchActivitySetException nsee) {
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
		SocialActivitySet socialActivitySet = addSocialActivitySet();

		long groupId = socialActivitySet.getGroupId();

		List<SocialActivitySet> socialActivitySets = _persistence.findByGroupId(groupId);

		Assert.assertEquals(1, socialActivitySets.size());

		Assert.assertEquals(socialActivitySet.getPrimaryKey(),
			socialActivitySets.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByGroupIdNotFound() throws Exception {
		addSocialActivitySet();

		long groupId = ServiceTestUtil.nextLong();

		List<SocialActivitySet> socialActivitySets = _persistence.findByGroupId(groupId);

		Assert.assertEquals(0, socialActivitySets.size());
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
		SocialActivitySet socialActivitySet = addSocialActivitySet();

		long groupId = socialActivitySet.getGroupId();

		List<SocialActivitySet> socialActivitySets = _persistence.findByGroupId(groupId,
				start, end);

		Assert.assertEquals(expected, socialActivitySets.size());
	}

	@Test
	public void testFindByUserId() throws Exception {
		SocialActivitySet socialActivitySet = addSocialActivitySet();

		long userId = socialActivitySet.getUserId();

		List<SocialActivitySet> socialActivitySets = _persistence.findByUserId(userId);

		Assert.assertEquals(1, socialActivitySets.size());

		Assert.assertEquals(socialActivitySet.getPrimaryKey(),
			socialActivitySets.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUserIdNotFound() throws Exception {
		addSocialActivitySet();

		long userId = ServiceTestUtil.nextLong();

		List<SocialActivitySet> socialActivitySets = _persistence.findByUserId(userId);

		Assert.assertEquals(0, socialActivitySets.size());
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
		SocialActivitySet socialActivitySet = addSocialActivitySet();

		long userId = socialActivitySet.getUserId();

		List<SocialActivitySet> socialActivitySets = _persistence.findByUserId(userId,
				start, end);

		Assert.assertEquals(expected, socialActivitySets.size());
	}

	@Test
	public void testFindByG_U_T() throws Exception {
		SocialActivitySet socialActivitySet = addSocialActivitySet();

		long groupId = socialActivitySet.getGroupId();

		long userId = socialActivitySet.getUserId();

		int type = socialActivitySet.getType();

		List<SocialActivitySet> socialActivitySets = _persistence.findByG_U_T(groupId,
				userId, type);

		Assert.assertEquals(1, socialActivitySets.size());

		Assert.assertEquals(socialActivitySet.getPrimaryKey(),
			socialActivitySets.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_U_TNotFound() throws Exception {
		addSocialActivitySet();

		long groupId = ServiceTestUtil.nextLong();

		long userId = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		List<SocialActivitySet> socialActivitySets = _persistence.findByG_U_T(groupId,
				userId, type);

		Assert.assertEquals(0, socialActivitySets.size());
	}

	@Test
	public void testFindByG_U_TStartEnd() throws Exception {
		testFindByG_U_TStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_U_TStartEndWrongRange() throws Exception {
		testFindByG_U_TStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_U_TStartEndZeroZero() throws Exception {
		testFindByG_U_TStartEnd(0, 0, 0);
	}

	protected void testFindByG_U_TStartEnd(int start, int end, int expected)
		throws Exception {
		SocialActivitySet socialActivitySet = addSocialActivitySet();

		long groupId = socialActivitySet.getGroupId();

		long userId = socialActivitySet.getUserId();

		int type = socialActivitySet.getType();

		List<SocialActivitySet> socialActivitySets = _persistence.findByG_U_T(groupId,
				userId, type, start, end);

		Assert.assertEquals(expected, socialActivitySets.size());
	}

	@Test
	public void testFindByC_C_T() throws Exception {
		SocialActivitySet socialActivitySet = addSocialActivitySet();

		long classNameId = socialActivitySet.getClassNameId();

		long classPK = socialActivitySet.getClassPK();

		int type = socialActivitySet.getType();

		List<SocialActivitySet> socialActivitySets = _persistence.findByC_C_T(classNameId,
				classPK, type);

		Assert.assertEquals(1, socialActivitySets.size());

		Assert.assertEquals(socialActivitySet.getPrimaryKey(),
			socialActivitySets.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_C_TNotFound() throws Exception {
		addSocialActivitySet();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		List<SocialActivitySet> socialActivitySets = _persistence.findByC_C_T(classNameId,
				classPK, type);

		Assert.assertEquals(0, socialActivitySets.size());
	}

	@Test
	public void testFindByC_C_TStartEnd() throws Exception {
		testFindByC_C_TStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_C_TStartEndWrongRange() throws Exception {
		testFindByC_C_TStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_C_TStartEndZeroZero() throws Exception {
		testFindByC_C_TStartEnd(0, 0, 0);
	}

	protected void testFindByC_C_TStartEnd(int start, int end, int expected)
		throws Exception {
		SocialActivitySet socialActivitySet = addSocialActivitySet();

		long classNameId = socialActivitySet.getClassNameId();

		long classPK = socialActivitySet.getClassPK();

		int type = socialActivitySet.getType();

		List<SocialActivitySet> socialActivitySets = _persistence.findByC_C_T(classNameId,
				classPK, type, start, end);

		Assert.assertEquals(expected, socialActivitySets.size());
	}

	@Test
	public void testFindByG_U_C_T() throws Exception {
		SocialActivitySet socialActivitySet = addSocialActivitySet();

		long groupId = socialActivitySet.getGroupId();

		long userId = socialActivitySet.getUserId();

		long classNameId = socialActivitySet.getClassNameId();

		int type = socialActivitySet.getType();

		List<SocialActivitySet> socialActivitySets = _persistence.findByG_U_C_T(groupId,
				userId, classNameId, type);

		Assert.assertEquals(1, socialActivitySets.size());

		Assert.assertEquals(socialActivitySet.getPrimaryKey(),
			socialActivitySets.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_U_C_TNotFound() throws Exception {
		addSocialActivitySet();

		long groupId = ServiceTestUtil.nextLong();

		long userId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		List<SocialActivitySet> socialActivitySets = _persistence.findByG_U_C_T(groupId,
				userId, classNameId, type);

		Assert.assertEquals(0, socialActivitySets.size());
	}

	@Test
	public void testFindByG_U_C_TStartEnd() throws Exception {
		testFindByG_U_C_TStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_U_C_TStartEndWrongRange() throws Exception {
		testFindByG_U_C_TStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_U_C_TStartEndZeroZero() throws Exception {
		testFindByG_U_C_TStartEnd(0, 0, 0);
	}

	protected void testFindByG_U_C_TStartEnd(int start, int end, int expected)
		throws Exception {
		SocialActivitySet socialActivitySet = addSocialActivitySet();

		long groupId = socialActivitySet.getGroupId();

		long userId = socialActivitySet.getUserId();

		long classNameId = socialActivitySet.getClassNameId();

		int type = socialActivitySet.getType();

		List<SocialActivitySet> socialActivitySets = _persistence.findByG_U_C_T(groupId,
				userId, classNameId, type, start, end);

		Assert.assertEquals(expected, socialActivitySets.size());
	}

	@Test
	public void testFindByU_C_C_T() throws Exception {
		SocialActivitySet socialActivitySet = addSocialActivitySet();

		long userId = socialActivitySet.getUserId();

		long classNameId = socialActivitySet.getClassNameId();

		long classPK = socialActivitySet.getClassPK();

		int type = socialActivitySet.getType();

		List<SocialActivitySet> socialActivitySets = _persistence.findByU_C_C_T(userId,
				classNameId, classPK, type);

		Assert.assertEquals(1, socialActivitySets.size());

		Assert.assertEquals(socialActivitySet.getPrimaryKey(),
			socialActivitySets.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByU_C_C_TNotFound() throws Exception {
		addSocialActivitySet();

		long userId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		List<SocialActivitySet> socialActivitySets = _persistence.findByU_C_C_T(userId,
				classNameId, classPK, type);

		Assert.assertEquals(0, socialActivitySets.size());
	}

	@Test
	public void testFindByU_C_C_TStartEnd() throws Exception {
		testFindByU_C_C_TStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByU_C_C_TStartEndWrongRange() throws Exception {
		testFindByU_C_C_TStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByU_C_C_TStartEndZeroZero() throws Exception {
		testFindByU_C_C_TStartEnd(0, 0, 0);
	}

	protected void testFindByU_C_C_TStartEnd(int start, int end, int expected)
		throws Exception {
		SocialActivitySet socialActivitySet = addSocialActivitySet();

		long userId = socialActivitySet.getUserId();

		long classNameId = socialActivitySet.getClassNameId();

		long classPK = socialActivitySet.getClassPK();

		int type = socialActivitySet.getType();

		List<SocialActivitySet> socialActivitySets = _persistence.findByU_C_C_T(userId,
				classNameId, classPK, type, start, end);

		Assert.assertEquals(expected, socialActivitySets.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("SocialActivitySet",
			"activitySetId", true, "groupId", true, "companyId", true,
			"userId", true, "createDate", true, "modifiedDate", true,
			"classNameId", true, "classPK", true, "type", true, "extraData",
			true, "activityCount", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		SocialActivitySet newSocialActivitySet = addSocialActivitySet();

		SocialActivitySet existingSocialActivitySet = _persistence.fetchByPrimaryKey(newSocialActivitySet.getPrimaryKey());

		Assert.assertEquals(existingSocialActivitySet, newSocialActivitySet);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SocialActivitySet missingSocialActivitySet = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingSocialActivitySet);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new SocialActivitySetActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					SocialActivitySet socialActivitySet = (SocialActivitySet)object;

					Assert.assertNotNull(socialActivitySet);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		SocialActivitySet newSocialActivitySet = addSocialActivitySet();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialActivitySet.class,
				SocialActivitySet.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("activitySetId",
				newSocialActivitySet.getActivitySetId()));

		List<SocialActivitySet> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		SocialActivitySet existingSocialActivitySet = result.get(0);

		Assert.assertEquals(existingSocialActivitySet, newSocialActivitySet);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialActivitySet.class,
				SocialActivitySet.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("activitySetId",
				ServiceTestUtil.nextLong()));

		List<SocialActivitySet> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		SocialActivitySet newSocialActivitySet = addSocialActivitySet();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialActivitySet.class,
				SocialActivitySet.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"activitySetId"));

		Object newActivitySetId = newSocialActivitySet.getActivitySetId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("activitySetId",
				new Object[] { newActivitySetId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingActivitySetId = result.get(0);

		Assert.assertEquals(existingActivitySetId, newActivitySetId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialActivitySet.class,
				SocialActivitySet.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"activitySetId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("activitySetId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected SocialActivitySet addSocialActivitySet()
		throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SocialActivitySet socialActivitySet = _persistence.create(pk);

		socialActivitySet.setGroupId(ServiceTestUtil.nextLong());

		socialActivitySet.setCompanyId(ServiceTestUtil.nextLong());

		socialActivitySet.setUserId(ServiceTestUtil.nextLong());

		socialActivitySet.setCreateDate(ServiceTestUtil.nextLong());

		socialActivitySet.setModifiedDate(ServiceTestUtil.nextLong());

		socialActivitySet.setClassNameId(ServiceTestUtil.nextLong());

		socialActivitySet.setClassPK(ServiceTestUtil.nextLong());

		socialActivitySet.setType(ServiceTestUtil.nextInt());

		socialActivitySet.setExtraData(ServiceTestUtil.randomString());

		socialActivitySet.setActivityCount(ServiceTestUtil.nextInt());

		_persistence.update(socialActivitySet);

		return socialActivitySet;
	}

	private static Log _log = LogFactoryUtil.getLog(SocialActivitySetPersistenceTest.class);
	private SocialActivitySetPersistence _persistence = (SocialActivitySetPersistence)PortalBeanLocatorUtil.locate(SocialActivitySetPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}