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

package com.liferay.portlet.mobiledevicerules.service.persistence;

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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;
import com.liferay.portal.util.PropsValues;

import com.liferay.portlet.mobiledevicerules.NoSuchActionException;
import com.liferay.portlet.mobiledevicerules.model.MDRAction;
import com.liferay.portlet.mobiledevicerules.model.impl.MDRActionModelImpl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Edward C. Han
 */
@ExecutionTestListeners(listeners =  {
	PersistenceExecutionTestListener.class})
@RunWith(LiferayPersistenceIntegrationJUnitTestRunner.class)
public class MDRActionPersistenceTest {
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

		MDRAction mdrAction = _persistence.create(pk);

		Assert.assertNotNull(mdrAction);

		Assert.assertEquals(mdrAction.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		MDRAction newMDRAction = addMDRAction();

		_persistence.remove(newMDRAction);

		MDRAction existingMDRAction = _persistence.fetchByPrimaryKey(newMDRAction.getPrimaryKey());

		Assert.assertNull(existingMDRAction);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addMDRAction();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		MDRAction newMDRAction = _persistence.create(pk);

		newMDRAction.setUuid(ServiceTestUtil.randomString());

		newMDRAction.setGroupId(ServiceTestUtil.nextLong());

		newMDRAction.setCompanyId(ServiceTestUtil.nextLong());

		newMDRAction.setUserId(ServiceTestUtil.nextLong());

		newMDRAction.setUserName(ServiceTestUtil.randomString());

		newMDRAction.setCreateDate(ServiceTestUtil.nextDate());

		newMDRAction.setModifiedDate(ServiceTestUtil.nextDate());

		newMDRAction.setClassNameId(ServiceTestUtil.nextLong());

		newMDRAction.setClassPK(ServiceTestUtil.nextLong());

		newMDRAction.setRuleGroupInstanceId(ServiceTestUtil.nextLong());

		newMDRAction.setName(ServiceTestUtil.randomString());

		newMDRAction.setDescription(ServiceTestUtil.randomString());

		newMDRAction.setType(ServiceTestUtil.randomString());

		newMDRAction.setTypeSettings(ServiceTestUtil.randomString());

		_persistence.update(newMDRAction);

		MDRAction existingMDRAction = _persistence.findByPrimaryKey(newMDRAction.getPrimaryKey());

		Assert.assertEquals(existingMDRAction.getUuid(), newMDRAction.getUuid());
		Assert.assertEquals(existingMDRAction.getActionId(),
			newMDRAction.getActionId());
		Assert.assertEquals(existingMDRAction.getGroupId(),
			newMDRAction.getGroupId());
		Assert.assertEquals(existingMDRAction.getCompanyId(),
			newMDRAction.getCompanyId());
		Assert.assertEquals(existingMDRAction.getUserId(),
			newMDRAction.getUserId());
		Assert.assertEquals(existingMDRAction.getUserName(),
			newMDRAction.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingMDRAction.getCreateDate()),
			Time.getShortTimestamp(newMDRAction.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingMDRAction.getModifiedDate()),
			Time.getShortTimestamp(newMDRAction.getModifiedDate()));
		Assert.assertEquals(existingMDRAction.getClassNameId(),
			newMDRAction.getClassNameId());
		Assert.assertEquals(existingMDRAction.getClassPK(),
			newMDRAction.getClassPK());
		Assert.assertEquals(existingMDRAction.getRuleGroupInstanceId(),
			newMDRAction.getRuleGroupInstanceId());
		Assert.assertEquals(existingMDRAction.getName(), newMDRAction.getName());
		Assert.assertEquals(existingMDRAction.getDescription(),
			newMDRAction.getDescription());
		Assert.assertEquals(existingMDRAction.getType(), newMDRAction.getType());
		Assert.assertEquals(existingMDRAction.getTypeSettings(),
			newMDRAction.getTypeSettings());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		MDRAction newMDRAction = addMDRAction();

		MDRAction existingMDRAction = _persistence.findByPrimaryKey(newMDRAction.getPrimaryKey());

		Assert.assertEquals(existingMDRAction, newMDRAction);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchActionException");
		}
		catch (NoSuchActionException nsee) {
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
		MDRAction mdrAction = addMDRAction();

		String uuid = mdrAction.getUuid();

		List<MDRAction> mdrActions = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, mdrActions.size());

		Assert.assertEquals(mdrAction.getPrimaryKey(),
			mdrActions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addMDRAction();

		String uuid = ServiceTestUtil.randomString();

		List<MDRAction> mdrActions = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, mdrActions.size());
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
		MDRAction mdrAction = addMDRAction();

		String uuid = mdrAction.getUuid();

		List<MDRAction> mdrActions = _persistence.findByUuid(uuid, start, end);

		Assert.assertEquals(expected, mdrActions.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		MDRAction mdrAction = addMDRAction();

		String uuid = mdrAction.getUuid();

		long companyId = mdrAction.getCompanyId();

		List<MDRAction> mdrActions = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(1, mdrActions.size());

		Assert.assertEquals(mdrAction.getPrimaryKey(),
			mdrActions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addMDRAction();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<MDRAction> mdrActions = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(0, mdrActions.size());
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
		MDRAction mdrAction = addMDRAction();

		String uuid = mdrAction.getUuid();

		long companyId = mdrAction.getCompanyId();

		List<MDRAction> mdrActions = _persistence.findByUuid_C(uuid, companyId,
				start, end);

		Assert.assertEquals(expected, mdrActions.size());
	}

	@Test
	public void testFindByRuleGroupInstanceId() throws Exception {
		MDRAction mdrAction = addMDRAction();

		long ruleGroupInstanceId = mdrAction.getRuleGroupInstanceId();

		List<MDRAction> mdrActions = _persistence.findByRuleGroupInstanceId(ruleGroupInstanceId);

		Assert.assertEquals(1, mdrActions.size());

		Assert.assertEquals(mdrAction.getPrimaryKey(),
			mdrActions.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByRuleGroupInstanceIdNotFound()
		throws Exception {
		addMDRAction();

		long ruleGroupInstanceId = ServiceTestUtil.nextLong();

		List<MDRAction> mdrActions = _persistence.findByRuleGroupInstanceId(ruleGroupInstanceId);

		Assert.assertEquals(0, mdrActions.size());
	}

	@Test
	public void testFindByRuleGroupInstanceIdStartEnd()
		throws Exception {
		testFindByRuleGroupInstanceIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByRuleGroupInstanceIdStartEndWrongRange()
		throws Exception {
		testFindByRuleGroupInstanceIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByRuleGroupInstanceIdStartEndZeroZero()
		throws Exception {
		testFindByRuleGroupInstanceIdStartEnd(0, 0, 0);
	}

	protected void testFindByRuleGroupInstanceIdStartEnd(int start, int end,
		int expected) throws Exception {
		MDRAction mdrAction = addMDRAction();

		long ruleGroupInstanceId = mdrAction.getRuleGroupInstanceId();

		List<MDRAction> mdrActions = _persistence.findByRuleGroupInstanceId(ruleGroupInstanceId,
				start, end);

		Assert.assertEquals(expected, mdrActions.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("MDRAction", "uuid", true,
			"actionId", true, "groupId", true, "companyId", true, "userId",
			true, "userName", true, "createDate", true, "modifiedDate", true,
			"classNameId", true, "classPK", true, "ruleGroupInstanceId", true,
			"name", true, "description", true, "type", true, "typeSettings",
			true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		MDRAction newMDRAction = addMDRAction();

		MDRAction existingMDRAction = _persistence.fetchByPrimaryKey(newMDRAction.getPrimaryKey());

		Assert.assertEquals(existingMDRAction, newMDRAction);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		MDRAction missingMDRAction = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingMDRAction);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new MDRActionActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					MDRAction mdrAction = (MDRAction)object;

					Assert.assertNotNull(mdrAction);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		MDRAction newMDRAction = addMDRAction();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(MDRAction.class,
				MDRAction.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("actionId",
				newMDRAction.getActionId()));

		List<MDRAction> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		MDRAction existingMDRAction = result.get(0);

		Assert.assertEquals(existingMDRAction, newMDRAction);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(MDRAction.class,
				MDRAction.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("actionId",
				ServiceTestUtil.nextLong()));

		List<MDRAction> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		MDRAction newMDRAction = addMDRAction();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(MDRAction.class,
				MDRAction.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("actionId"));

		Object newActionId = newMDRAction.getActionId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("actionId",
				new Object[] { newActionId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingActionId = result.get(0);

		Assert.assertEquals(existingActionId, newActionId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(MDRAction.class,
				MDRAction.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("actionId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("actionId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		MDRAction newMDRAction = addMDRAction();

		_persistence.clearCache();

		MDRActionModelImpl existingMDRActionModelImpl = (MDRActionModelImpl)_persistence.findByPrimaryKey(newMDRAction.getPrimaryKey());

		Assert.assertTrue(Validator.equals(
				existingMDRActionModelImpl.getUuid(),
				existingMDRActionModelImpl.getOriginalUuid()));
		Assert.assertEquals(existingMDRActionModelImpl.getGroupId(),
			existingMDRActionModelImpl.getOriginalGroupId());
	}

	protected MDRAction addMDRAction() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		MDRAction mdrAction = _persistence.create(pk);

		mdrAction.setUuid(ServiceTestUtil.randomString());

		mdrAction.setGroupId(ServiceTestUtil.nextLong());

		mdrAction.setCompanyId(ServiceTestUtil.nextLong());

		mdrAction.setUserId(ServiceTestUtil.nextLong());

		mdrAction.setUserName(ServiceTestUtil.randomString());

		mdrAction.setCreateDate(ServiceTestUtil.nextDate());

		mdrAction.setModifiedDate(ServiceTestUtil.nextDate());

		mdrAction.setClassNameId(ServiceTestUtil.nextLong());

		mdrAction.setClassPK(ServiceTestUtil.nextLong());

		mdrAction.setRuleGroupInstanceId(ServiceTestUtil.nextLong());

		mdrAction.setName(ServiceTestUtil.randomString());

		mdrAction.setDescription(ServiceTestUtil.randomString());

		mdrAction.setType(ServiceTestUtil.randomString());

		mdrAction.setTypeSettings(ServiceTestUtil.randomString());

		_persistence.update(mdrAction);

		return mdrAction;
	}

	private static Log _log = LogFactoryUtil.getLog(MDRActionPersistenceTest.class);
	private MDRActionPersistence _persistence = (MDRActionPersistence)PortalBeanLocatorUtil.locate(MDRActionPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}