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

import com.liferay.portal.NoSuchRoleException;
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
import com.liferay.portal.model.Role;
import com.liferay.portal.model.impl.RoleModelImpl;
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
public class RolePersistenceTest {
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

		Role role = _persistence.create(pk);

		Assert.assertNotNull(role);

		Assert.assertEquals(role.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		Role newRole = addRole();

		_persistence.remove(newRole);

		Role existingRole = _persistence.fetchByPrimaryKey(newRole.getPrimaryKey());

		Assert.assertNull(existingRole);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addRole();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Role newRole = _persistence.create(pk);

		newRole.setUuid(ServiceTestUtil.randomString());

		newRole.setCompanyId(ServiceTestUtil.nextLong());

		newRole.setUserId(ServiceTestUtil.nextLong());

		newRole.setUserName(ServiceTestUtil.randomString());

		newRole.setCreateDate(ServiceTestUtil.nextDate());

		newRole.setModifiedDate(ServiceTestUtil.nextDate());

		newRole.setClassNameId(ServiceTestUtil.nextLong());

		newRole.setClassPK(ServiceTestUtil.nextLong());

		newRole.setName(ServiceTestUtil.randomString());

		newRole.setTitle(ServiceTestUtil.randomString());

		newRole.setDescription(ServiceTestUtil.randomString());

		newRole.setType(ServiceTestUtil.nextInt());

		newRole.setSubtype(ServiceTestUtil.randomString());

		_persistence.update(newRole);

		Role existingRole = _persistence.findByPrimaryKey(newRole.getPrimaryKey());

		Assert.assertEquals(existingRole.getUuid(), newRole.getUuid());
		Assert.assertEquals(existingRole.getRoleId(), newRole.getRoleId());
		Assert.assertEquals(existingRole.getCompanyId(), newRole.getCompanyId());
		Assert.assertEquals(existingRole.getUserId(), newRole.getUserId());
		Assert.assertEquals(existingRole.getUserName(), newRole.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(existingRole.getCreateDate()),
			Time.getShortTimestamp(newRole.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingRole.getModifiedDate()),
			Time.getShortTimestamp(newRole.getModifiedDate()));
		Assert.assertEquals(existingRole.getClassNameId(),
			newRole.getClassNameId());
		Assert.assertEquals(existingRole.getClassPK(), newRole.getClassPK());
		Assert.assertEquals(existingRole.getName(), newRole.getName());
		Assert.assertEquals(existingRole.getTitle(), newRole.getTitle());
		Assert.assertEquals(existingRole.getDescription(),
			newRole.getDescription());
		Assert.assertEquals(existingRole.getType(), newRole.getType());
		Assert.assertEquals(existingRole.getSubtype(), newRole.getSubtype());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		Role newRole = addRole();

		Role existingRole = _persistence.findByPrimaryKey(newRole.getPrimaryKey());

		Assert.assertEquals(existingRole, newRole);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchRoleException");
		}
		catch (NoSuchRoleException nsee) {
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
		Role role = addRole();

		String uuid = role.getUuid();

		List<Role> roles = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, roles.size());

		Assert.assertEquals(role.getPrimaryKey(), roles.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addRole();

		String uuid = ServiceTestUtil.randomString();

		List<Role> roles = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, roles.size());
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
		Role role = addRole();

		String uuid = role.getUuid();

		List<Role> roles = _persistence.findByUuid(uuid, start, end);

		Assert.assertEquals(expected, roles.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		Role role = addRole();

		String uuid = role.getUuid();

		long companyId = role.getCompanyId();

		List<Role> roles = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(1, roles.size());

		Assert.assertEquals(role.getPrimaryKey(), roles.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addRole();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<Role> roles = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(0, roles.size());
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
		Role role = addRole();

		String uuid = role.getUuid();

		long companyId = role.getCompanyId();

		List<Role> roles = _persistence.findByUuid_C(uuid, companyId, start, end);

		Assert.assertEquals(expected, roles.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		Role role = addRole();

		long companyId = role.getCompanyId();

		List<Role> roles = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, roles.size());

		Assert.assertEquals(role.getPrimaryKey(), roles.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addRole();

		long companyId = ServiceTestUtil.nextLong();

		List<Role> roles = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, roles.size());
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
		Role role = addRole();

		long companyId = role.getCompanyId();

		List<Role> roles = _persistence.findByCompanyId(companyId, start, end);

		Assert.assertEquals(expected, roles.size());
	}

	@Test
	public void testFindByName() throws Exception {
		Role role = addRole();

		String name = role.getName();

		List<Role> roles = _persistence.findByName(name);

		Assert.assertEquals(1, roles.size());

		Assert.assertEquals(role.getPrimaryKey(), roles.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByNameNotFound() throws Exception {
		addRole();

		String name = ServiceTestUtil.randomString();

		List<Role> roles = _persistence.findByName(name);

		Assert.assertEquals(0, roles.size());
	}

	@Test
	public void testFindByNameStartEnd() throws Exception {
		testFindByNameStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByNameStartEndWrongRange() throws Exception {
		testFindByNameStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByNameStartEndZeroZero() throws Exception {
		testFindByNameStartEnd(0, 0, 0);
	}

	protected void testFindByNameStartEnd(int start, int end, int expected)
		throws Exception {
		Role role = addRole();

		String name = role.getName();

		List<Role> roles = _persistence.findByName(name, start, end);

		Assert.assertEquals(expected, roles.size());
	}

	@Test
	public void testFindByType() throws Exception {
		Role role = addRole();

		int type = role.getType();

		List<Role> roles = _persistence.findByType(type);

		Assert.assertEquals(1, roles.size());

		Assert.assertEquals(role.getPrimaryKey(), roles.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByTypeNotFound() throws Exception {
		addRole();

		int type = ServiceTestUtil.nextInt();

		List<Role> roles = _persistence.findByType(type);

		Assert.assertEquals(0, roles.size());
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
		Role role = addRole();

		int type = role.getType();

		List<Role> roles = _persistence.findByType(type, start, end);

		Assert.assertEquals(expected, roles.size());
	}

	@Test
	public void testFindBySubtype() throws Exception {
		Role role = addRole();

		String subtype = role.getSubtype();

		List<Role> roles = _persistence.findBySubtype(subtype);

		Assert.assertEquals(1, roles.size());

		Assert.assertEquals(role.getPrimaryKey(), roles.get(0).getPrimaryKey());
	}

	@Test
	public void testFindBySubtypeNotFound() throws Exception {
		addRole();

		String subtype = ServiceTestUtil.randomString();

		List<Role> roles = _persistence.findBySubtype(subtype);

		Assert.assertEquals(0, roles.size());
	}

	@Test
	public void testFindBySubtypeStartEnd() throws Exception {
		testFindBySubtypeStartEnd(0, 5, 1);
	}

	@Test
	public void testFindBySubtypeStartEndWrongRange() throws Exception {
		testFindBySubtypeStartEnd(5, 0, 0);
	}

	@Test
	public void testFindBySubtypeStartEndZeroZero() throws Exception {
		testFindBySubtypeStartEnd(0, 0, 0);
	}

	protected void testFindBySubtypeStartEnd(int start, int end, int expected)
		throws Exception {
		Role role = addRole();

		String subtype = role.getSubtype();

		List<Role> roles = _persistence.findBySubtype(subtype, start, end);

		Assert.assertEquals(expected, roles.size());
	}

	@Test
	public void testFindByC_T() throws Exception {
		Role role = addRole();

		long companyId = role.getCompanyId();

		int type = role.getType();

		List<Role> roles = _persistence.findByC_T(companyId, type);

		Assert.assertEquals(1, roles.size());

		Assert.assertEquals(role.getPrimaryKey(), roles.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_TNotFound() throws Exception {
		addRole();

		long companyId = ServiceTestUtil.nextLong();

		int type = ServiceTestUtil.nextInt();

		List<Role> roles = _persistence.findByC_T(companyId, type);

		Assert.assertEquals(0, roles.size());
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
		Role role = addRole();

		long companyId = role.getCompanyId();

		int type = role.getType();

		List<Role> roles = _persistence.findByC_T(companyId, type, start, end);

		Assert.assertEquals(expected, roles.size());
	}

	@Test
	public void testFindByT_S() throws Exception {
		Role role = addRole();

		int type = role.getType();

		String subtype = role.getSubtype();

		List<Role> roles = _persistence.findByT_S(type, subtype);

		Assert.assertEquals(1, roles.size());

		Assert.assertEquals(role.getPrimaryKey(), roles.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByT_SNotFound() throws Exception {
		addRole();

		int type = ServiceTestUtil.nextInt();

		String subtype = ServiceTestUtil.randomString();

		List<Role> roles = _persistence.findByT_S(type, subtype);

		Assert.assertEquals(0, roles.size());
	}

	@Test
	public void testFindByT_SStartEnd() throws Exception {
		testFindByT_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByT_SStartEndWrongRange() throws Exception {
		testFindByT_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByT_SStartEndZeroZero() throws Exception {
		testFindByT_SStartEnd(0, 0, 0);
	}

	protected void testFindByT_SStartEnd(int start, int end, int expected)
		throws Exception {
		Role role = addRole();

		int type = role.getType();

		String subtype = role.getSubtype();

		List<Role> roles = _persistence.findByT_S(type, subtype, start, end);

		Assert.assertEquals(expected, roles.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("Role_", "uuid", true,
			"roleId", true, "companyId", true, "userId", true, "userName",
			true, "createDate", true, "modifiedDate", true, "classNameId",
			true, "classPK", true, "name", true, "title", true, "description",
			true, "type", true, "subtype", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		Role newRole = addRole();

		Role existingRole = _persistence.fetchByPrimaryKey(newRole.getPrimaryKey());

		Assert.assertEquals(existingRole, newRole);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Role missingRole = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingRole);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new RoleActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					Role role = (Role)object;

					Assert.assertNotNull(role);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		Role newRole = addRole();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Role.class,
				Role.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("roleId",
				newRole.getRoleId()));

		List<Role> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Role existingRole = result.get(0);

		Assert.assertEquals(existingRole, newRole);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Role.class,
				Role.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("roleId",
				ServiceTestUtil.nextLong()));

		List<Role> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		Role newRole = addRole();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Role.class,
				Role.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("roleId"));

		Object newRoleId = newRole.getRoleId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("roleId",
				new Object[] { newRoleId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingRoleId = result.get(0);

		Assert.assertEquals(existingRoleId, newRoleId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Role.class,
				Role.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("roleId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("roleId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		Role newRole = addRole();

		_persistence.clearCache();

		RoleModelImpl existingRoleModelImpl = (RoleModelImpl)_persistence.findByPrimaryKey(newRole.getPrimaryKey());

		Assert.assertEquals(existingRoleModelImpl.getCompanyId(),
			existingRoleModelImpl.getOriginalCompanyId());
		Assert.assertTrue(Validator.equals(existingRoleModelImpl.getName(),
				existingRoleModelImpl.getOriginalName()));

		Assert.assertEquals(existingRoleModelImpl.getCompanyId(),
			existingRoleModelImpl.getOriginalCompanyId());
		Assert.assertEquals(existingRoleModelImpl.getClassNameId(),
			existingRoleModelImpl.getOriginalClassNameId());
		Assert.assertEquals(existingRoleModelImpl.getClassPK(),
			existingRoleModelImpl.getOriginalClassPK());
	}

	protected Role addRole() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Role role = _persistence.create(pk);

		role.setUuid(ServiceTestUtil.randomString());

		role.setCompanyId(ServiceTestUtil.nextLong());

		role.setUserId(ServiceTestUtil.nextLong());

		role.setUserName(ServiceTestUtil.randomString());

		role.setCreateDate(ServiceTestUtil.nextDate());

		role.setModifiedDate(ServiceTestUtil.nextDate());

		role.setClassNameId(ServiceTestUtil.nextLong());

		role.setClassPK(ServiceTestUtil.nextLong());

		role.setName(ServiceTestUtil.randomString());

		role.setTitle(ServiceTestUtil.randomString());

		role.setDescription(ServiceTestUtil.randomString());

		role.setType(ServiceTestUtil.nextInt());

		role.setSubtype(ServiceTestUtil.randomString());

		_persistence.update(role);

		return role;
	}

	private static Log _log = LogFactoryUtil.getLog(RolePersistenceTest.class);
	private RolePersistence _persistence = (RolePersistence)PortalBeanLocatorUtil.locate(RolePersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}