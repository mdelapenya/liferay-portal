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

import com.liferay.portlet.social.NoSuchActivityException;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.impl.SocialActivityModelImpl;

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
public class SocialActivityPersistenceTest {
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

		SocialActivity socialActivity = _persistence.create(pk);

		Assert.assertNotNull(socialActivity);

		Assert.assertEquals(socialActivity.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		SocialActivity newSocialActivity = addSocialActivity();

		_persistence.remove(newSocialActivity);

		SocialActivity existingSocialActivity = _persistence.fetchByPrimaryKey(newSocialActivity.getPrimaryKey());

		Assert.assertNull(existingSocialActivity);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addSocialActivity();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SocialActivity newSocialActivity = _persistence.create(pk);

		newSocialActivity.setGroupId(ServiceTestUtil.nextLong());

		newSocialActivity.setCompanyId(ServiceTestUtil.nextLong());

		newSocialActivity.setUserId(ServiceTestUtil.nextLong());

		newSocialActivity.setCreateDate(ServiceTestUtil.nextLong());

		newSocialActivity.setActivitySetId(ServiceTestUtil.nextLong());

		newSocialActivity.setMirrorActivityId(ServiceTestUtil.nextLong());

		newSocialActivity.setClassNameId(ServiceTestUtil.nextLong());

		newSocialActivity.setClassPK(ServiceTestUtil.nextLong());

		newSocialActivity.setParentClassNameId(ServiceTestUtil.nextLong());

		newSocialActivity.setParentClassPK(ServiceTestUtil.nextLong());

		newSocialActivity.setType(ServiceTestUtil.nextInt());

		newSocialActivity.setExtraData(ServiceTestUtil.randomString());

		newSocialActivity.setReceiverUserId(ServiceTestUtil.nextLong());

		_persistence.update(newSocialActivity);

		SocialActivity existingSocialActivity = _persistence.findByPrimaryKey(newSocialActivity.getPrimaryKey());

		Assert.assertEquals(existingSocialActivity.getActivityId(),
			newSocialActivity.getActivityId());
		Assert.assertEquals(existingSocialActivity.getGroupId(),
			newSocialActivity.getGroupId());
		Assert.assertEquals(existingSocialActivity.getCompanyId(),
			newSocialActivity.getCompanyId());
		Assert.assertEquals(existingSocialActivity.getUserId(),
			newSocialActivity.getUserId());
		Assert.assertEquals(existingSocialActivity.getCreateDate(),
			newSocialActivity.getCreateDate());
		Assert.assertEquals(existingSocialActivity.getActivitySetId(),
			newSocialActivity.getActivitySetId());
		Assert.assertEquals(existingSocialActivity.getMirrorActivityId(),
			newSocialActivity.getMirrorActivityId());
		Assert.assertEquals(existingSocialActivity.getClassNameId(),
			newSocialActivity.getClassNameId());
		Assert.assertEquals(existingSocialActivity.getClassPK(),
			newSocialActivity.getClassPK());
		Assert.assertEquals(existingSocialActivity.getParentClassNameId(),
			newSocialActivity.getParentClassNameId());
		Assert.assertEquals(existingSocialActivity.getParentClassPK(),
			newSocialActivity.getParentClassPK());
		Assert.assertEquals(existingSocialActivity.getType(),
			newSocialActivity.getType());
		Assert.assertEquals(existingSocialActivity.getExtraData(),
			newSocialActivity.getExtraData());
		Assert.assertEquals(existingSocialActivity.getReceiverUserId(),
			newSocialActivity.getReceiverUserId());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		SocialActivity newSocialActivity = addSocialActivity();

		SocialActivity existingSocialActivity = _persistence.findByPrimaryKey(newSocialActivity.getPrimaryKey());

		Assert.assertEquals(existingSocialActivity, newSocialActivity);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchActivityException");
		}
		catch (NoSuchActivityException nsee) {
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
		SocialActivity socialActivity = addSocialActivity();

		long groupId = socialActivity.getGroupId();

		List<SocialActivity> socialActivities = _persistence.findByGroupId(groupId);

		Assert.assertEquals(1, socialActivities.size());

		Assert.assertEquals(socialActivity.getPrimaryKey(),
			socialActivities.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByGroupIdNotFound() throws Exception {
		addSocialActivity();

		long groupId = ServiceTestUtil.nextLong();

		List<SocialActivity> socialActivities = _persistence.findByGroupId(groupId);

		Assert.assertEquals(0, socialActivities.size());
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
		SocialActivity socialActivity = addSocialActivity();

		long groupId = socialActivity.getGroupId();

		List<SocialActivity> socialActivities = _persistence.findByGroupId(groupId,
				start, end);

		Assert.assertEquals(expected, socialActivities.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long companyId = socialActivity.getCompanyId();

		List<SocialActivity> socialActivities = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, socialActivities.size());

		Assert.assertEquals(socialActivity.getPrimaryKey(),
			socialActivities.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addSocialActivity();

		long companyId = ServiceTestUtil.nextLong();

		List<SocialActivity> socialActivities = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, socialActivities.size());
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
		SocialActivity socialActivity = addSocialActivity();

		long companyId = socialActivity.getCompanyId();

		List<SocialActivity> socialActivities = _persistence.findByCompanyId(companyId,
				start, end);

		Assert.assertEquals(expected, socialActivities.size());
	}

	@Test
	public void testFindByUserId() throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long userId = socialActivity.getUserId();

		List<SocialActivity> socialActivities = _persistence.findByUserId(userId);

		Assert.assertEquals(1, socialActivities.size());

		Assert.assertEquals(socialActivity.getPrimaryKey(),
			socialActivities.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUserIdNotFound() throws Exception {
		addSocialActivity();

		long userId = ServiceTestUtil.nextLong();

		List<SocialActivity> socialActivities = _persistence.findByUserId(userId);

		Assert.assertEquals(0, socialActivities.size());
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
		SocialActivity socialActivity = addSocialActivity();

		long userId = socialActivity.getUserId();

		List<SocialActivity> socialActivities = _persistence.findByUserId(userId,
				start, end);

		Assert.assertEquals(expected, socialActivities.size());
	}

	@Test
	public void testFindByActivitySetId() throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long activitySetId = socialActivity.getActivitySetId();

		List<SocialActivity> socialActivities = _persistence.findByActivitySetId(activitySetId);

		Assert.assertEquals(1, socialActivities.size());

		Assert.assertEquals(socialActivity.getPrimaryKey(),
			socialActivities.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByActivitySetIdNotFound() throws Exception {
		addSocialActivity();

		long activitySetId = ServiceTestUtil.nextLong();

		List<SocialActivity> socialActivities = _persistence.findByActivitySetId(activitySetId);

		Assert.assertEquals(0, socialActivities.size());
	}

	@Test
	public void testFindByActivitySetIdStartEnd() throws Exception {
		testFindByActivitySetIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByActivitySetIdStartEndWrongRange()
		throws Exception {
		testFindByActivitySetIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByActivitySetIdStartEndZeroZero()
		throws Exception {
		testFindByActivitySetIdStartEnd(0, 0, 0);
	}

	protected void testFindByActivitySetIdStartEnd(int start, int end,
		int expected) throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long activitySetId = socialActivity.getActivitySetId();

		List<SocialActivity> socialActivities = _persistence.findByActivitySetId(activitySetId,
				start, end);

		Assert.assertEquals(expected, socialActivities.size());
	}

	@Test
	public void testFindByClassNameId() throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long classNameId = socialActivity.getClassNameId();

		List<SocialActivity> socialActivities = _persistence.findByClassNameId(classNameId);

		Assert.assertEquals(1, socialActivities.size());

		Assert.assertEquals(socialActivity.getPrimaryKey(),
			socialActivities.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByClassNameIdNotFound() throws Exception {
		addSocialActivity();

		long classNameId = ServiceTestUtil.nextLong();

		List<SocialActivity> socialActivities = _persistence.findByClassNameId(classNameId);

		Assert.assertEquals(0, socialActivities.size());
	}

	@Test
	public void testFindByClassNameIdStartEnd() throws Exception {
		testFindByClassNameIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByClassNameIdStartEndWrongRange()
		throws Exception {
		testFindByClassNameIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByClassNameIdStartEndZeroZero()
		throws Exception {
		testFindByClassNameIdStartEnd(0, 0, 0);
	}

	protected void testFindByClassNameIdStartEnd(int start, int end,
		int expected) throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long classNameId = socialActivity.getClassNameId();

		List<SocialActivity> socialActivities = _persistence.findByClassNameId(classNameId,
				start, end);

		Assert.assertEquals(expected, socialActivities.size());
	}

	@Test
	public void testFindByReceiverUserId() throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long receiverUserId = socialActivity.getReceiverUserId();

		List<SocialActivity> socialActivities = _persistence.findByReceiverUserId(receiverUserId);

		Assert.assertEquals(1, socialActivities.size());

		Assert.assertEquals(socialActivity.getPrimaryKey(),
			socialActivities.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByReceiverUserIdNotFound() throws Exception {
		addSocialActivity();

		long receiverUserId = ServiceTestUtil.nextLong();

		List<SocialActivity> socialActivities = _persistence.findByReceiverUserId(receiverUserId);

		Assert.assertEquals(0, socialActivities.size());
	}

	@Test
	public void testFindByReceiverUserIdStartEnd() throws Exception {
		testFindByReceiverUserIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByReceiverUserIdStartEndWrongRange()
		throws Exception {
		testFindByReceiverUserIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByReceiverUserIdStartEndZeroZero()
		throws Exception {
		testFindByReceiverUserIdStartEnd(0, 0, 0);
	}

	protected void testFindByReceiverUserIdStartEnd(int start, int end,
		int expected) throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long receiverUserId = socialActivity.getReceiverUserId();

		List<SocialActivity> socialActivities = _persistence.findByReceiverUserId(receiverUserId,
				start, end);

		Assert.assertEquals(expected, socialActivities.size());
	}

	@Test
	public void testFindByC_C() throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long classNameId = socialActivity.getClassNameId();

		long classPK = socialActivity.getClassPK();

		List<SocialActivity> socialActivities = _persistence.findByC_C(classNameId,
				classPK);

		Assert.assertEquals(1, socialActivities.size());

		Assert.assertEquals(socialActivity.getPrimaryKey(),
			socialActivities.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_CNotFound() throws Exception {
		addSocialActivity();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		List<SocialActivity> socialActivities = _persistence.findByC_C(classNameId,
				classPK);

		Assert.assertEquals(0, socialActivities.size());
	}

	@Test
	public void testFindByC_CStartEnd() throws Exception {
		testFindByC_CStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_CStartEndWrongRange() throws Exception {
		testFindByC_CStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_CStartEndZeroZero() throws Exception {
		testFindByC_CStartEnd(0, 0, 0);
	}

	protected void testFindByC_CStartEnd(int start, int end, int expected)
		throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long classNameId = socialActivity.getClassNameId();

		long classPK = socialActivity.getClassPK();

		List<SocialActivity> socialActivities = _persistence.findByC_C(classNameId,
				classPK, start, end);

		Assert.assertEquals(expected, socialActivities.size());
	}

	@Test
	public void testFindByM_C_C() throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long mirrorActivityId = socialActivity.getMirrorActivityId();

		long classNameId = socialActivity.getClassNameId();

		long classPK = socialActivity.getClassPK();

		List<SocialActivity> socialActivities = _persistence.findByM_C_C(mirrorActivityId,
				classNameId, classPK);

		Assert.assertEquals(1, socialActivities.size());

		Assert.assertEquals(socialActivity.getPrimaryKey(),
			socialActivities.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByM_C_CNotFound() throws Exception {
		addSocialActivity();

		long mirrorActivityId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		List<SocialActivity> socialActivities = _persistence.findByM_C_C(mirrorActivityId,
				classNameId, classPK);

		Assert.assertEquals(0, socialActivities.size());
	}

	@Test
	public void testFindByM_C_CStartEnd() throws Exception {
		testFindByM_C_CStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByM_C_CStartEndWrongRange() throws Exception {
		testFindByM_C_CStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByM_C_CStartEndZeroZero() throws Exception {
		testFindByM_C_CStartEnd(0, 0, 0);
	}

	protected void testFindByM_C_CStartEnd(int start, int end, int expected)
		throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long mirrorActivityId = socialActivity.getMirrorActivityId();

		long classNameId = socialActivity.getClassNameId();

		long classPK = socialActivity.getClassPK();

		List<SocialActivity> socialActivities = _persistence.findByM_C_C(mirrorActivityId,
				classNameId, classPK, start, end);

		Assert.assertEquals(expected, socialActivities.size());
	}

	@Test
	public void testFindByC_C_T() throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long classNameId = socialActivity.getClassNameId();

		long classPK = socialActivity.getClassPK();

		int type = socialActivity.getType();

		List<SocialActivity> socialActivities = _persistence.findByC_C_T(classNameId,
				classPK, type);

		Assert.assertEquals(1, socialActivities.size());

		Assert.assertEquals(socialActivity.getPrimaryKey(),
			socialActivities.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_C_TNotFound() throws Exception {
		addSocialActivity();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		List<SocialActivity> socialActivities = _persistence.findByC_C_T(classNameId,
				classPK, type);

		Assert.assertEquals(0, socialActivities.size());
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
		SocialActivity socialActivity = addSocialActivity();

		long classNameId = socialActivity.getClassNameId();

		long classPK = socialActivity.getClassPK();

		int type = socialActivity.getType();

		List<SocialActivity> socialActivities = _persistence.findByC_C_T(classNameId,
				classPK, type, start, end);

		Assert.assertEquals(expected, socialActivities.size());
	}

	@Test
	public void testFindByG_U_C_C_T_R() throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long groupId = socialActivity.getGroupId();

		long userId = socialActivity.getUserId();

		long classNameId = socialActivity.getClassNameId();

		long classPK = socialActivity.getClassPK();

		int type = socialActivity.getType();

		long receiverUserId = socialActivity.getReceiverUserId();

		List<SocialActivity> socialActivities = _persistence.findByG_U_C_C_T_R(groupId,
				userId, classNameId, classPK, type, receiverUserId);

		Assert.assertEquals(1, socialActivities.size());

		Assert.assertEquals(socialActivity.getPrimaryKey(),
			socialActivities.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_U_C_C_T_RNotFound() throws Exception {
		addSocialActivity();

		long groupId = ServiceTestUtil.nextLong();

		long userId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		long receiverUserId = ServiceTestUtil.nextLong();

		List<SocialActivity> socialActivities = _persistence.findByG_U_C_C_T_R(groupId,
				userId, classNameId, classPK, type, receiverUserId);

		Assert.assertEquals(0, socialActivities.size());
	}

	@Test
	public void testFindByG_U_C_C_T_RStartEnd() throws Exception {
		testFindByG_U_C_C_T_RStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_U_C_C_T_RStartEndWrongRange()
		throws Exception {
		testFindByG_U_C_C_T_RStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_U_C_C_T_RStartEndZeroZero()
		throws Exception {
		testFindByG_U_C_C_T_RStartEnd(0, 0, 0);
	}

	protected void testFindByG_U_C_C_T_RStartEnd(int start, int end,
		int expected) throws Exception {
		SocialActivity socialActivity = addSocialActivity();

		long groupId = socialActivity.getGroupId();

		long userId = socialActivity.getUserId();

		long classNameId = socialActivity.getClassNameId();

		long classPK = socialActivity.getClassPK();

		int type = socialActivity.getType();

		long receiverUserId = socialActivity.getReceiverUserId();

		List<SocialActivity> socialActivities = _persistence.findByG_U_C_C_T_R(groupId,
				userId, classNameId, classPK, type, receiverUserId, start, end);

		Assert.assertEquals(expected, socialActivities.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("SocialActivity",
			"activityId", true, "groupId", true, "companyId", true, "userId",
			true, "createDate", true, "activitySetId", true,
			"mirrorActivityId", true, "classNameId", true, "classPK", true,
			"parentClassNameId", true, "parentClassPK", true, "type", true,
			"extraData", true, "receiverUserId", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		SocialActivity newSocialActivity = addSocialActivity();

		SocialActivity existingSocialActivity = _persistence.fetchByPrimaryKey(newSocialActivity.getPrimaryKey());

		Assert.assertEquals(existingSocialActivity, newSocialActivity);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SocialActivity missingSocialActivity = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingSocialActivity);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new SocialActivityActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					SocialActivity socialActivity = (SocialActivity)object;

					Assert.assertNotNull(socialActivity);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		SocialActivity newSocialActivity = addSocialActivity();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialActivity.class,
				SocialActivity.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("activityId",
				newSocialActivity.getActivityId()));

		List<SocialActivity> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		SocialActivity existingSocialActivity = result.get(0);

		Assert.assertEquals(existingSocialActivity, newSocialActivity);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialActivity.class,
				SocialActivity.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("activityId",
				ServiceTestUtil.nextLong()));

		List<SocialActivity> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		SocialActivity newSocialActivity = addSocialActivity();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialActivity.class,
				SocialActivity.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("activityId"));

		Object newActivityId = newSocialActivity.getActivityId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("activityId",
				new Object[] { newActivityId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingActivityId = result.get(0);

		Assert.assertEquals(existingActivityId, newActivityId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialActivity.class,
				SocialActivity.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("activityId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("activityId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		SocialActivity newSocialActivity = addSocialActivity();

		_persistence.clearCache();

		SocialActivityModelImpl existingSocialActivityModelImpl = (SocialActivityModelImpl)_persistence.findByPrimaryKey(newSocialActivity.getPrimaryKey());

		Assert.assertEquals(existingSocialActivityModelImpl.getMirrorActivityId(),
			existingSocialActivityModelImpl.getOriginalMirrorActivityId());

		Assert.assertEquals(existingSocialActivityModelImpl.getGroupId(),
			existingSocialActivityModelImpl.getOriginalGroupId());
		Assert.assertEquals(existingSocialActivityModelImpl.getUserId(),
			existingSocialActivityModelImpl.getOriginalUserId());
		Assert.assertEquals(existingSocialActivityModelImpl.getCreateDate(),
			existingSocialActivityModelImpl.getOriginalCreateDate());
		Assert.assertEquals(existingSocialActivityModelImpl.getClassNameId(),
			existingSocialActivityModelImpl.getOriginalClassNameId());
		Assert.assertEquals(existingSocialActivityModelImpl.getClassPK(),
			existingSocialActivityModelImpl.getOriginalClassPK());
		Assert.assertEquals(existingSocialActivityModelImpl.getType(),
			existingSocialActivityModelImpl.getOriginalType());
		Assert.assertEquals(existingSocialActivityModelImpl.getReceiverUserId(),
			existingSocialActivityModelImpl.getOriginalReceiverUserId());
	}

	protected SocialActivity addSocialActivity() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SocialActivity socialActivity = _persistence.create(pk);

		socialActivity.setGroupId(ServiceTestUtil.nextLong());

		socialActivity.setCompanyId(ServiceTestUtil.nextLong());

		socialActivity.setUserId(ServiceTestUtil.nextLong());

		socialActivity.setCreateDate(ServiceTestUtil.nextLong());

		socialActivity.setActivitySetId(ServiceTestUtil.nextLong());

		socialActivity.setMirrorActivityId(ServiceTestUtil.nextLong());

		socialActivity.setClassNameId(ServiceTestUtil.nextLong());

		socialActivity.setClassPK(ServiceTestUtil.nextLong());

		socialActivity.setParentClassNameId(ServiceTestUtil.nextLong());

		socialActivity.setParentClassPK(ServiceTestUtil.nextLong());

		socialActivity.setType(ServiceTestUtil.nextInt());

		socialActivity.setExtraData(ServiceTestUtil.randomString());

		socialActivity.setReceiverUserId(ServiceTestUtil.nextLong());

		_persistence.update(socialActivity);

		return socialActivity;
	}

	private static Log _log = LogFactoryUtil.getLog(SocialActivityPersistenceTest.class);
	private SocialActivityPersistence _persistence = (SocialActivityPersistence)PortalBeanLocatorUtil.locate(SocialActivityPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}