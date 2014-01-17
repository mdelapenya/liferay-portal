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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;
import com.liferay.portal.util.PropsValues;

import com.liferay.portlet.social.NoSuchRequestException;
import com.liferay.portlet.social.model.SocialRequest;
import com.liferay.portlet.social.model.impl.SocialRequestModelImpl;

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
public class SocialRequestPersistenceTest {
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

		SocialRequest socialRequest = _persistence.create(pk);

		Assert.assertNotNull(socialRequest);

		Assert.assertEquals(socialRequest.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		SocialRequest newSocialRequest = addSocialRequest();

		_persistence.remove(newSocialRequest);

		SocialRequest existingSocialRequest = _persistence.fetchByPrimaryKey(newSocialRequest.getPrimaryKey());

		Assert.assertNull(existingSocialRequest);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addSocialRequest();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SocialRequest newSocialRequest = _persistence.create(pk);

		newSocialRequest.setUuid(ServiceTestUtil.randomString());

		newSocialRequest.setGroupId(ServiceTestUtil.nextLong());

		newSocialRequest.setCompanyId(ServiceTestUtil.nextLong());

		newSocialRequest.setUserId(ServiceTestUtil.nextLong());

		newSocialRequest.setCreateDate(ServiceTestUtil.nextLong());

		newSocialRequest.setModifiedDate(ServiceTestUtil.nextLong());

		newSocialRequest.setClassNameId(ServiceTestUtil.nextLong());

		newSocialRequest.setClassPK(ServiceTestUtil.nextLong());

		newSocialRequest.setType(ServiceTestUtil.nextInt());

		newSocialRequest.setExtraData(ServiceTestUtil.randomString());

		newSocialRequest.setReceiverUserId(ServiceTestUtil.nextLong());

		newSocialRequest.setStatus(ServiceTestUtil.nextInt());

		_persistence.update(newSocialRequest);

		SocialRequest existingSocialRequest = _persistence.findByPrimaryKey(newSocialRequest.getPrimaryKey());

		Assert.assertEquals(existingSocialRequest.getUuid(),
			newSocialRequest.getUuid());
		Assert.assertEquals(existingSocialRequest.getRequestId(),
			newSocialRequest.getRequestId());
		Assert.assertEquals(existingSocialRequest.getGroupId(),
			newSocialRequest.getGroupId());
		Assert.assertEquals(existingSocialRequest.getCompanyId(),
			newSocialRequest.getCompanyId());
		Assert.assertEquals(existingSocialRequest.getUserId(),
			newSocialRequest.getUserId());
		Assert.assertEquals(existingSocialRequest.getCreateDate(),
			newSocialRequest.getCreateDate());
		Assert.assertEquals(existingSocialRequest.getModifiedDate(),
			newSocialRequest.getModifiedDate());
		Assert.assertEquals(existingSocialRequest.getClassNameId(),
			newSocialRequest.getClassNameId());
		Assert.assertEquals(existingSocialRequest.getClassPK(),
			newSocialRequest.getClassPK());
		Assert.assertEquals(existingSocialRequest.getType(),
			newSocialRequest.getType());
		Assert.assertEquals(existingSocialRequest.getExtraData(),
			newSocialRequest.getExtraData());
		Assert.assertEquals(existingSocialRequest.getReceiverUserId(),
			newSocialRequest.getReceiverUserId());
		Assert.assertEquals(existingSocialRequest.getStatus(),
			newSocialRequest.getStatus());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		SocialRequest newSocialRequest = addSocialRequest();

		SocialRequest existingSocialRequest = _persistence.findByPrimaryKey(newSocialRequest.getPrimaryKey());

		Assert.assertEquals(existingSocialRequest, newSocialRequest);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchRequestException");
		}
		catch (NoSuchRequestException nsee) {
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
		SocialRequest socialRequest = addSocialRequest();

		String uuid = socialRequest.getUuid();

		List<SocialRequest> socialRequests = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, socialRequests.size());

		Assert.assertEquals(socialRequest.getPrimaryKey(),
			socialRequests.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addSocialRequest();

		String uuid = ServiceTestUtil.randomString();

		List<SocialRequest> socialRequests = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, socialRequests.size());
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
		SocialRequest socialRequest = addSocialRequest();

		String uuid = socialRequest.getUuid();

		List<SocialRequest> socialRequests = _persistence.findByUuid(uuid,
				start, end);

		Assert.assertEquals(expected, socialRequests.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		SocialRequest socialRequest = addSocialRequest();

		String uuid = socialRequest.getUuid();

		long companyId = socialRequest.getCompanyId();

		List<SocialRequest> socialRequests = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(1, socialRequests.size());

		Assert.assertEquals(socialRequest.getPrimaryKey(),
			socialRequests.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addSocialRequest();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<SocialRequest> socialRequests = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(0, socialRequests.size());
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
		SocialRequest socialRequest = addSocialRequest();

		String uuid = socialRequest.getUuid();

		long companyId = socialRequest.getCompanyId();

		List<SocialRequest> socialRequests = _persistence.findByUuid_C(uuid,
				companyId, start, end);

		Assert.assertEquals(expected, socialRequests.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		SocialRequest socialRequest = addSocialRequest();

		long companyId = socialRequest.getCompanyId();

		List<SocialRequest> socialRequests = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, socialRequests.size());

		Assert.assertEquals(socialRequest.getPrimaryKey(),
			socialRequests.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addSocialRequest();

		long companyId = ServiceTestUtil.nextLong();

		List<SocialRequest> socialRequests = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, socialRequests.size());
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
		SocialRequest socialRequest = addSocialRequest();

		long companyId = socialRequest.getCompanyId();

		List<SocialRequest> socialRequests = _persistence.findByCompanyId(companyId,
				start, end);

		Assert.assertEquals(expected, socialRequests.size());
	}

	@Test
	public void testFindByUserId() throws Exception {
		SocialRequest socialRequest = addSocialRequest();

		long userId = socialRequest.getUserId();

		List<SocialRequest> socialRequests = _persistence.findByUserId(userId);

		Assert.assertEquals(1, socialRequests.size());

		Assert.assertEquals(socialRequest.getPrimaryKey(),
			socialRequests.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUserIdNotFound() throws Exception {
		addSocialRequest();

		long userId = ServiceTestUtil.nextLong();

		List<SocialRequest> socialRequests = _persistence.findByUserId(userId);

		Assert.assertEquals(0, socialRequests.size());
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
		SocialRequest socialRequest = addSocialRequest();

		long userId = socialRequest.getUserId();

		List<SocialRequest> socialRequests = _persistence.findByUserId(userId,
				start, end);

		Assert.assertEquals(expected, socialRequests.size());
	}

	@Test
	public void testFindByReceiverUserId() throws Exception {
		SocialRequest socialRequest = addSocialRequest();

		long receiverUserId = socialRequest.getReceiverUserId();

		List<SocialRequest> socialRequests = _persistence.findByReceiverUserId(receiverUserId);

		Assert.assertEquals(1, socialRequests.size());

		Assert.assertEquals(socialRequest.getPrimaryKey(),
			socialRequests.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByReceiverUserIdNotFound() throws Exception {
		addSocialRequest();

		long receiverUserId = ServiceTestUtil.nextLong();

		List<SocialRequest> socialRequests = _persistence.findByReceiverUserId(receiverUserId);

		Assert.assertEquals(0, socialRequests.size());
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
		SocialRequest socialRequest = addSocialRequest();

		long receiverUserId = socialRequest.getReceiverUserId();

		List<SocialRequest> socialRequests = _persistence.findByReceiverUserId(receiverUserId,
				start, end);

		Assert.assertEquals(expected, socialRequests.size());
	}

	@Test
	public void testFindByU_S() throws Exception {
		SocialRequest socialRequest = addSocialRequest();

		long userId = socialRequest.getUserId();

		int status = socialRequest.getStatus();

		List<SocialRequest> socialRequests = _persistence.findByU_S(userId,
				status);

		Assert.assertEquals(1, socialRequests.size());

		Assert.assertEquals(socialRequest.getPrimaryKey(),
			socialRequests.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByU_SNotFound() throws Exception {
		addSocialRequest();

		long userId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<SocialRequest> socialRequests = _persistence.findByU_S(userId,
				status);

		Assert.assertEquals(0, socialRequests.size());
	}

	@Test
	public void testFindByU_SStartEnd() throws Exception {
		testFindByU_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByU_SStartEndWrongRange() throws Exception {
		testFindByU_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByU_SStartEndZeroZero() throws Exception {
		testFindByU_SStartEnd(0, 0, 0);
	}

	protected void testFindByU_SStartEnd(int start, int end, int expected)
		throws Exception {
		SocialRequest socialRequest = addSocialRequest();

		long userId = socialRequest.getUserId();

		int status = socialRequest.getStatus();

		List<SocialRequest> socialRequests = _persistence.findByU_S(userId,
				status, start, end);

		Assert.assertEquals(expected, socialRequests.size());
	}

	@Test
	public void testFindByC_C() throws Exception {
		SocialRequest socialRequest = addSocialRequest();

		long classNameId = socialRequest.getClassNameId();

		long classPK = socialRequest.getClassPK();

		List<SocialRequest> socialRequests = _persistence.findByC_C(classNameId,
				classPK);

		Assert.assertEquals(1, socialRequests.size());

		Assert.assertEquals(socialRequest.getPrimaryKey(),
			socialRequests.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_CNotFound() throws Exception {
		addSocialRequest();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		List<SocialRequest> socialRequests = _persistence.findByC_C(classNameId,
				classPK);

		Assert.assertEquals(0, socialRequests.size());
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
		SocialRequest socialRequest = addSocialRequest();

		long classNameId = socialRequest.getClassNameId();

		long classPK = socialRequest.getClassPK();

		List<SocialRequest> socialRequests = _persistence.findByC_C(classNameId,
				classPK, start, end);

		Assert.assertEquals(expected, socialRequests.size());
	}

	@Test
	public void testFindByR_S() throws Exception {
		SocialRequest socialRequest = addSocialRequest();

		long receiverUserId = socialRequest.getReceiverUserId();

		int status = socialRequest.getStatus();

		List<SocialRequest> socialRequests = _persistence.findByR_S(receiverUserId,
				status);

		Assert.assertEquals(1, socialRequests.size());

		Assert.assertEquals(socialRequest.getPrimaryKey(),
			socialRequests.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByR_SNotFound() throws Exception {
		addSocialRequest();

		long receiverUserId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<SocialRequest> socialRequests = _persistence.findByR_S(receiverUserId,
				status);

		Assert.assertEquals(0, socialRequests.size());
	}

	@Test
	public void testFindByR_SStartEnd() throws Exception {
		testFindByR_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByR_SStartEndWrongRange() throws Exception {
		testFindByR_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByR_SStartEndZeroZero() throws Exception {
		testFindByR_SStartEnd(0, 0, 0);
	}

	protected void testFindByR_SStartEnd(int start, int end, int expected)
		throws Exception {
		SocialRequest socialRequest = addSocialRequest();

		long receiverUserId = socialRequest.getReceiverUserId();

		int status = socialRequest.getStatus();

		List<SocialRequest> socialRequests = _persistence.findByR_S(receiverUserId,
				status, start, end);

		Assert.assertEquals(expected, socialRequests.size());
	}

	@Test
	public void testFindByU_C_C_T_S() throws Exception {
		SocialRequest socialRequest = addSocialRequest();

		long userId = socialRequest.getUserId();

		long classNameId = socialRequest.getClassNameId();

		long classPK = socialRequest.getClassPK();

		int type = socialRequest.getType();

		int status = socialRequest.getStatus();

		List<SocialRequest> socialRequests = _persistence.findByU_C_C_T_S(userId,
				classNameId, classPK, type, status);

		Assert.assertEquals(1, socialRequests.size());

		Assert.assertEquals(socialRequest.getPrimaryKey(),
			socialRequests.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByU_C_C_T_SNotFound() throws Exception {
		addSocialRequest();

		long userId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		int status = ServiceTestUtil.nextInt();

		List<SocialRequest> socialRequests = _persistence.findByU_C_C_T_S(userId,
				classNameId, classPK, type, status);

		Assert.assertEquals(0, socialRequests.size());
	}

	@Test
	public void testFindByU_C_C_T_SStartEnd() throws Exception {
		testFindByU_C_C_T_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByU_C_C_T_SStartEndWrongRange()
		throws Exception {
		testFindByU_C_C_T_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByU_C_C_T_SStartEndZeroZero() throws Exception {
		testFindByU_C_C_T_SStartEnd(0, 0, 0);
	}

	protected void testFindByU_C_C_T_SStartEnd(int start, int end, int expected)
		throws Exception {
		SocialRequest socialRequest = addSocialRequest();

		long userId = socialRequest.getUserId();

		long classNameId = socialRequest.getClassNameId();

		long classPK = socialRequest.getClassPK();

		int type = socialRequest.getType();

		int status = socialRequest.getStatus();

		List<SocialRequest> socialRequests = _persistence.findByU_C_C_T_S(userId,
				classNameId, classPK, type, status, start, end);

		Assert.assertEquals(expected, socialRequests.size());
	}

	@Test
	public void testFindByC_C_T_R_S() throws Exception {
		SocialRequest socialRequest = addSocialRequest();

		long classNameId = socialRequest.getClassNameId();

		long classPK = socialRequest.getClassPK();

		int type = socialRequest.getType();

		long receiverUserId = socialRequest.getReceiverUserId();

		int status = socialRequest.getStatus();

		List<SocialRequest> socialRequests = _persistence.findByC_C_T_R_S(classNameId,
				classPK, type, receiverUserId, status);

		Assert.assertEquals(1, socialRequests.size());

		Assert.assertEquals(socialRequest.getPrimaryKey(),
			socialRequests.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_C_T_R_SNotFound() throws Exception {
		addSocialRequest();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		long receiverUserId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<SocialRequest> socialRequests = _persistence.findByC_C_T_R_S(classNameId,
				classPK, type, receiverUserId, status);

		Assert.assertEquals(0, socialRequests.size());
	}

	@Test
	public void testFindByC_C_T_R_SStartEnd() throws Exception {
		testFindByC_C_T_R_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_C_T_R_SStartEndWrongRange()
		throws Exception {
		testFindByC_C_T_R_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_C_T_R_SStartEndZeroZero() throws Exception {
		testFindByC_C_T_R_SStartEnd(0, 0, 0);
	}

	protected void testFindByC_C_T_R_SStartEnd(int start, int end, int expected)
		throws Exception {
		SocialRequest socialRequest = addSocialRequest();

		long classNameId = socialRequest.getClassNameId();

		long classPK = socialRequest.getClassPK();

		int type = socialRequest.getType();

		long receiverUserId = socialRequest.getReceiverUserId();

		int status = socialRequest.getStatus();

		List<SocialRequest> socialRequests = _persistence.findByC_C_T_R_S(classNameId,
				classPK, type, receiverUserId, status, start, end);

		Assert.assertEquals(expected, socialRequests.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("SocialRequest", "uuid",
			true, "requestId", true, "groupId", true, "companyId", true,
			"userId", true, "createDate", true, "modifiedDate", true,
			"classNameId", true, "classPK", true, "type", true, "extraData",
			true, "receiverUserId", true, "status", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		SocialRequest newSocialRequest = addSocialRequest();

		SocialRequest existingSocialRequest = _persistence.fetchByPrimaryKey(newSocialRequest.getPrimaryKey());

		Assert.assertEquals(existingSocialRequest, newSocialRequest);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SocialRequest missingSocialRequest = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingSocialRequest);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new SocialRequestActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					SocialRequest socialRequest = (SocialRequest)object;

					Assert.assertNotNull(socialRequest);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		SocialRequest newSocialRequest = addSocialRequest();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialRequest.class,
				SocialRequest.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("requestId",
				newSocialRequest.getRequestId()));

		List<SocialRequest> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		SocialRequest existingSocialRequest = result.get(0);

		Assert.assertEquals(existingSocialRequest, newSocialRequest);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialRequest.class,
				SocialRequest.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("requestId",
				ServiceTestUtil.nextLong()));

		List<SocialRequest> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		SocialRequest newSocialRequest = addSocialRequest();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialRequest.class,
				SocialRequest.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("requestId"));

		Object newRequestId = newSocialRequest.getRequestId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("requestId",
				new Object[] { newRequestId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingRequestId = result.get(0);

		Assert.assertEquals(existingRequestId, newRequestId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(SocialRequest.class,
				SocialRequest.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("requestId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("requestId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		SocialRequest newSocialRequest = addSocialRequest();

		_persistence.clearCache();

		SocialRequestModelImpl existingSocialRequestModelImpl = (SocialRequestModelImpl)_persistence.findByPrimaryKey(newSocialRequest.getPrimaryKey());

		Assert.assertTrue(Validator.equals(
				existingSocialRequestModelImpl.getUuid(),
				existingSocialRequestModelImpl.getOriginalUuid()));
		Assert.assertEquals(existingSocialRequestModelImpl.getGroupId(),
			existingSocialRequestModelImpl.getOriginalGroupId());

		Assert.assertEquals(existingSocialRequestModelImpl.getUserId(),
			existingSocialRequestModelImpl.getOriginalUserId());
		Assert.assertEquals(existingSocialRequestModelImpl.getClassNameId(),
			existingSocialRequestModelImpl.getOriginalClassNameId());
		Assert.assertEquals(existingSocialRequestModelImpl.getClassPK(),
			existingSocialRequestModelImpl.getOriginalClassPK());
		Assert.assertEquals(existingSocialRequestModelImpl.getType(),
			existingSocialRequestModelImpl.getOriginalType());
		Assert.assertEquals(existingSocialRequestModelImpl.getReceiverUserId(),
			existingSocialRequestModelImpl.getOriginalReceiverUserId());
	}

	protected SocialRequest addSocialRequest() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		SocialRequest socialRequest = _persistence.create(pk);

		socialRequest.setUuid(ServiceTestUtil.randomString());

		socialRequest.setGroupId(ServiceTestUtil.nextLong());

		socialRequest.setCompanyId(ServiceTestUtil.nextLong());

		socialRequest.setUserId(ServiceTestUtil.nextLong());

		socialRequest.setCreateDate(ServiceTestUtil.nextLong());

		socialRequest.setModifiedDate(ServiceTestUtil.nextLong());

		socialRequest.setClassNameId(ServiceTestUtil.nextLong());

		socialRequest.setClassPK(ServiceTestUtil.nextLong());

		socialRequest.setType(ServiceTestUtil.nextInt());

		socialRequest.setExtraData(ServiceTestUtil.randomString());

		socialRequest.setReceiverUserId(ServiceTestUtil.nextLong());

		socialRequest.setStatus(ServiceTestUtil.nextInt());

		_persistence.update(socialRequest);

		return socialRequest;
	}

	private static Log _log = LogFactoryUtil.getLog(SocialRequestPersistenceTest.class);
	private SocialRequestPersistence _persistence = (SocialRequestPersistence)PortalBeanLocatorUtil.locate(SocialRequestPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}