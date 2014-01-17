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

import com.liferay.portal.NoSuchUserGroupException;
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
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.model.impl.UserGroupModelImpl;
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
public class UserGroupPersistenceTest {
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

		UserGroup userGroup = _persistence.create(pk);

		Assert.assertNotNull(userGroup);

		Assert.assertEquals(userGroup.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		UserGroup newUserGroup = addUserGroup();

		_persistence.remove(newUserGroup);

		UserGroup existingUserGroup = _persistence.fetchByPrimaryKey(newUserGroup.getPrimaryKey());

		Assert.assertNull(existingUserGroup);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addUserGroup();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		UserGroup newUserGroup = _persistence.create(pk);

		newUserGroup.setUuid(ServiceTestUtil.randomString());

		newUserGroup.setCompanyId(ServiceTestUtil.nextLong());

		newUserGroup.setUserId(ServiceTestUtil.nextLong());

		newUserGroup.setUserName(ServiceTestUtil.randomString());

		newUserGroup.setCreateDate(ServiceTestUtil.nextDate());

		newUserGroup.setModifiedDate(ServiceTestUtil.nextDate());

		newUserGroup.setParentUserGroupId(ServiceTestUtil.nextLong());

		newUserGroup.setName(ServiceTestUtil.randomString());

		newUserGroup.setDescription(ServiceTestUtil.randomString());

		newUserGroup.setAddedByLDAPImport(ServiceTestUtil.randomBoolean());

		_persistence.update(newUserGroup);

		UserGroup existingUserGroup = _persistence.findByPrimaryKey(newUserGroup.getPrimaryKey());

		Assert.assertEquals(existingUserGroup.getUuid(), newUserGroup.getUuid());
		Assert.assertEquals(existingUserGroup.getUserGroupId(),
			newUserGroup.getUserGroupId());
		Assert.assertEquals(existingUserGroup.getCompanyId(),
			newUserGroup.getCompanyId());
		Assert.assertEquals(existingUserGroup.getUserId(),
			newUserGroup.getUserId());
		Assert.assertEquals(existingUserGroup.getUserName(),
			newUserGroup.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingUserGroup.getCreateDate()),
			Time.getShortTimestamp(newUserGroup.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingUserGroup.getModifiedDate()),
			Time.getShortTimestamp(newUserGroup.getModifiedDate()));
		Assert.assertEquals(existingUserGroup.getParentUserGroupId(),
			newUserGroup.getParentUserGroupId());
		Assert.assertEquals(existingUserGroup.getName(), newUserGroup.getName());
		Assert.assertEquals(existingUserGroup.getDescription(),
			newUserGroup.getDescription());
		Assert.assertEquals(existingUserGroup.getAddedByLDAPImport(),
			newUserGroup.getAddedByLDAPImport());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		UserGroup newUserGroup = addUserGroup();

		UserGroup existingUserGroup = _persistence.findByPrimaryKey(newUserGroup.getPrimaryKey());

		Assert.assertEquals(existingUserGroup, newUserGroup);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchUserGroupException");
		}
		catch (NoSuchUserGroupException nsee) {
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
		UserGroup userGroup = addUserGroup();

		String uuid = userGroup.getUuid();

		List<UserGroup> userGroups = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, userGroups.size());

		Assert.assertEquals(userGroup.getPrimaryKey(),
			userGroups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addUserGroup();

		String uuid = ServiceTestUtil.randomString();

		List<UserGroup> userGroups = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, userGroups.size());
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
		UserGroup userGroup = addUserGroup();

		String uuid = userGroup.getUuid();

		List<UserGroup> userGroups = _persistence.findByUuid(uuid, start, end);

		Assert.assertEquals(expected, userGroups.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		UserGroup userGroup = addUserGroup();

		String uuid = userGroup.getUuid();

		long companyId = userGroup.getCompanyId();

		List<UserGroup> userGroups = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(1, userGroups.size());

		Assert.assertEquals(userGroup.getPrimaryKey(),
			userGroups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addUserGroup();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<UserGroup> userGroups = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(0, userGroups.size());
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
		UserGroup userGroup = addUserGroup();

		String uuid = userGroup.getUuid();

		long companyId = userGroup.getCompanyId();

		List<UserGroup> userGroups = _persistence.findByUuid_C(uuid, companyId,
				start, end);

		Assert.assertEquals(expected, userGroups.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		UserGroup userGroup = addUserGroup();

		long companyId = userGroup.getCompanyId();

		List<UserGroup> userGroups = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, userGroups.size());

		Assert.assertEquals(userGroup.getPrimaryKey(),
			userGroups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addUserGroup();

		long companyId = ServiceTestUtil.nextLong();

		List<UserGroup> userGroups = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, userGroups.size());
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
		UserGroup userGroup = addUserGroup();

		long companyId = userGroup.getCompanyId();

		List<UserGroup> userGroups = _persistence.findByCompanyId(companyId,
				start, end);

		Assert.assertEquals(expected, userGroups.size());
	}

	@Test
	public void testFindByC_P() throws Exception {
		UserGroup userGroup = addUserGroup();

		long companyId = userGroup.getCompanyId();

		long parentUserGroupId = userGroup.getParentUserGroupId();

		List<UserGroup> userGroups = _persistence.findByC_P(companyId,
				parentUserGroupId);

		Assert.assertEquals(1, userGroups.size());

		Assert.assertEquals(userGroup.getPrimaryKey(),
			userGroups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_PNotFound() throws Exception {
		addUserGroup();

		long companyId = ServiceTestUtil.nextLong();

		long parentUserGroupId = ServiceTestUtil.nextLong();

		List<UserGroup> userGroups = _persistence.findByC_P(companyId,
				parentUserGroupId);

		Assert.assertEquals(0, userGroups.size());
	}

	@Test
	public void testFindByC_PStartEnd() throws Exception {
		testFindByC_PStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_PStartEndWrongRange() throws Exception {
		testFindByC_PStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_PStartEndZeroZero() throws Exception {
		testFindByC_PStartEnd(0, 0, 0);
	}

	protected void testFindByC_PStartEnd(int start, int end, int expected)
		throws Exception {
		UserGroup userGroup = addUserGroup();

		long companyId = userGroup.getCompanyId();

		long parentUserGroupId = userGroup.getParentUserGroupId();

		List<UserGroup> userGroups = _persistence.findByC_P(companyId,
				parentUserGroupId, start, end);

		Assert.assertEquals(expected, userGroups.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("UserGroup", "uuid", true,
			"userGroupId", true, "companyId", true, "userId", true, "userName",
			true, "createDate", true, "modifiedDate", true,
			"parentUserGroupId", true, "name", true, "description", true,
			"addedByLDAPImport", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		UserGroup newUserGroup = addUserGroup();

		UserGroup existingUserGroup = _persistence.fetchByPrimaryKey(newUserGroup.getPrimaryKey());

		Assert.assertEquals(existingUserGroup, newUserGroup);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		UserGroup missingUserGroup = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingUserGroup);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new UserGroupActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					UserGroup userGroup = (UserGroup)object;

					Assert.assertNotNull(userGroup);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		UserGroup newUserGroup = addUserGroup();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(UserGroup.class,
				UserGroup.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("userGroupId",
				newUserGroup.getUserGroupId()));

		List<UserGroup> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		UserGroup existingUserGroup = result.get(0);

		Assert.assertEquals(existingUserGroup, newUserGroup);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(UserGroup.class,
				UserGroup.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("userGroupId",
				ServiceTestUtil.nextLong()));

		List<UserGroup> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		UserGroup newUserGroup = addUserGroup();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(UserGroup.class,
				UserGroup.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("userGroupId"));

		Object newUserGroupId = newUserGroup.getUserGroupId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("userGroupId",
				new Object[] { newUserGroupId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingUserGroupId = result.get(0);

		Assert.assertEquals(existingUserGroupId, newUserGroupId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(UserGroup.class,
				UserGroup.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("userGroupId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("userGroupId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		UserGroup newUserGroup = addUserGroup();

		_persistence.clearCache();

		UserGroupModelImpl existingUserGroupModelImpl = (UserGroupModelImpl)_persistence.findByPrimaryKey(newUserGroup.getPrimaryKey());

		Assert.assertEquals(existingUserGroupModelImpl.getCompanyId(),
			existingUserGroupModelImpl.getOriginalCompanyId());
		Assert.assertTrue(Validator.equals(
				existingUserGroupModelImpl.getName(),
				existingUserGroupModelImpl.getOriginalName()));
	}

	protected UserGroup addUserGroup() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		UserGroup userGroup = _persistence.create(pk);

		userGroup.setUuid(ServiceTestUtil.randomString());

		userGroup.setCompanyId(ServiceTestUtil.nextLong());

		userGroup.setUserId(ServiceTestUtil.nextLong());

		userGroup.setUserName(ServiceTestUtil.randomString());

		userGroup.setCreateDate(ServiceTestUtil.nextDate());

		userGroup.setModifiedDate(ServiceTestUtil.nextDate());

		userGroup.setParentUserGroupId(ServiceTestUtil.nextLong());

		userGroup.setName(ServiceTestUtil.randomString());

		userGroup.setDescription(ServiceTestUtil.randomString());

		userGroup.setAddedByLDAPImport(ServiceTestUtil.randomBoolean());

		_persistence.update(userGroup);

		return userGroup;
	}

	private static Log _log = LogFactoryUtil.getLog(UserGroupPersistenceTest.class);
	private UserGroupPersistence _persistence = (UserGroupPersistence)PortalBeanLocatorUtil.locate(UserGroupPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}