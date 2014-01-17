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

import com.liferay.portal.NoSuchGroupException;
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
import com.liferay.portal.model.Group;
import com.liferay.portal.model.impl.GroupModelImpl;
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
public class GroupPersistenceTest {
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

		Group group = _persistence.create(pk);

		Assert.assertNotNull(group);

		Assert.assertEquals(group.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		Group newGroup = addGroup();

		_persistence.remove(newGroup);

		Group existingGroup = _persistence.fetchByPrimaryKey(newGroup.getPrimaryKey());

		Assert.assertNull(existingGroup);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addGroup();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Group newGroup = _persistence.create(pk);

		newGroup.setUuid(ServiceTestUtil.randomString());

		newGroup.setCompanyId(ServiceTestUtil.nextLong());

		newGroup.setCreatorUserId(ServiceTestUtil.nextLong());

		newGroup.setClassNameId(ServiceTestUtil.nextLong());

		newGroup.setClassPK(ServiceTestUtil.nextLong());

		newGroup.setParentGroupId(ServiceTestUtil.nextLong());

		newGroup.setLiveGroupId(ServiceTestUtil.nextLong());

		newGroup.setTreePath(ServiceTestUtil.randomString());

		newGroup.setName(ServiceTestUtil.randomString());

		newGroup.setDescription(ServiceTestUtil.randomString());

		newGroup.setType(ServiceTestUtil.nextInt());

		newGroup.setTypeSettings(ServiceTestUtil.randomString());

		newGroup.setManualMembership(ServiceTestUtil.randomBoolean());

		newGroup.setMembershipRestriction(ServiceTestUtil.nextInt());

		newGroup.setFriendlyURL(ServiceTestUtil.randomString());

		newGroup.setSite(ServiceTestUtil.randomBoolean());

		newGroup.setRemoteStagingGroupCount(ServiceTestUtil.nextInt());

		newGroup.setActive(ServiceTestUtil.randomBoolean());

		_persistence.update(newGroup);

		Group existingGroup = _persistence.findByPrimaryKey(newGroup.getPrimaryKey());

		Assert.assertEquals(existingGroup.getUuid(), newGroup.getUuid());
		Assert.assertEquals(existingGroup.getGroupId(), newGroup.getGroupId());
		Assert.assertEquals(existingGroup.getCompanyId(),
			newGroup.getCompanyId());
		Assert.assertEquals(existingGroup.getCreatorUserId(),
			newGroup.getCreatorUserId());
		Assert.assertEquals(existingGroup.getClassNameId(),
			newGroup.getClassNameId());
		Assert.assertEquals(existingGroup.getClassPK(), newGroup.getClassPK());
		Assert.assertEquals(existingGroup.getParentGroupId(),
			newGroup.getParentGroupId());
		Assert.assertEquals(existingGroup.getLiveGroupId(),
			newGroup.getLiveGroupId());
		Assert.assertEquals(existingGroup.getTreePath(), newGroup.getTreePath());
		Assert.assertEquals(existingGroup.getName(), newGroup.getName());
		Assert.assertEquals(existingGroup.getDescription(),
			newGroup.getDescription());
		Assert.assertEquals(existingGroup.getType(), newGroup.getType());
		Assert.assertEquals(existingGroup.getTypeSettings(),
			newGroup.getTypeSettings());
		Assert.assertEquals(existingGroup.getManualMembership(),
			newGroup.getManualMembership());
		Assert.assertEquals(existingGroup.getMembershipRestriction(),
			newGroup.getMembershipRestriction());
		Assert.assertEquals(existingGroup.getFriendlyURL(),
			newGroup.getFriendlyURL());
		Assert.assertEquals(existingGroup.getSite(), newGroup.getSite());
		Assert.assertEquals(existingGroup.getRemoteStagingGroupCount(),
			newGroup.getRemoteStagingGroupCount());
		Assert.assertEquals(existingGroup.getActive(), newGroup.getActive());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		Group newGroup = addGroup();

		Group existingGroup = _persistence.findByPrimaryKey(newGroup.getPrimaryKey());

		Assert.assertEquals(existingGroup, newGroup);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchGroupException");
		}
		catch (NoSuchGroupException nsee) {
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
		Group group = addGroup();

		String uuid = group.getUuid();

		List<Group> groups = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, groups.size());

		Assert.assertEquals(group.getPrimaryKey(), groups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addGroup();

		String uuid = ServiceTestUtil.randomString();

		List<Group> groups = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, groups.size());
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
		Group group = addGroup();

		String uuid = group.getUuid();

		List<Group> groups = _persistence.findByUuid(uuid, start, end);

		Assert.assertEquals(expected, groups.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		Group group = addGroup();

		String uuid = group.getUuid();

		long companyId = group.getCompanyId();

		List<Group> groups = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(1, groups.size());

		Assert.assertEquals(group.getPrimaryKey(), groups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addGroup();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<Group> groups = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(0, groups.size());
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
		Group group = addGroup();

		String uuid = group.getUuid();

		long companyId = group.getCompanyId();

		List<Group> groups = _persistence.findByUuid_C(uuid, companyId, start,
				end);

		Assert.assertEquals(expected, groups.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		Group group = addGroup();

		long companyId = group.getCompanyId();

		List<Group> groups = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, groups.size());

		Assert.assertEquals(group.getPrimaryKey(), groups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addGroup();

		long companyId = ServiceTestUtil.nextLong();

		List<Group> groups = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, groups.size());
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
		Group group = addGroup();

		long companyId = group.getCompanyId();

		List<Group> groups = _persistence.findByCompanyId(companyId, start, end);

		Assert.assertEquals(expected, groups.size());
	}

	@Test
	public void testFindByC_C() throws Exception {
		Group group = addGroup();

		long companyId = group.getCompanyId();

		long classNameId = group.getClassNameId();

		List<Group> groups = _persistence.findByC_C(companyId, classNameId);

		Assert.assertEquals(1, groups.size());

		Assert.assertEquals(group.getPrimaryKey(), groups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_CNotFound() throws Exception {
		addGroup();

		long companyId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		List<Group> groups = _persistence.findByC_C(companyId, classNameId);

		Assert.assertEquals(0, groups.size());
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
		Group group = addGroup();

		long companyId = group.getCompanyId();

		long classNameId = group.getClassNameId();

		List<Group> groups = _persistence.findByC_C(companyId, classNameId,
				start, end);

		Assert.assertEquals(expected, groups.size());
	}

	@Test
	public void testFindByC_P() throws Exception {
		Group group = addGroup();

		long companyId = group.getCompanyId();

		long parentGroupId = group.getParentGroupId();

		List<Group> groups = _persistence.findByC_P(companyId, parentGroupId);

		Assert.assertEquals(1, groups.size());

		Assert.assertEquals(group.getPrimaryKey(), groups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_PNotFound() throws Exception {
		addGroup();

		long companyId = ServiceTestUtil.nextLong();

		long parentGroupId = ServiceTestUtil.nextLong();

		List<Group> groups = _persistence.findByC_P(companyId, parentGroupId);

		Assert.assertEquals(0, groups.size());
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
		Group group = addGroup();

		long companyId = group.getCompanyId();

		long parentGroupId = group.getParentGroupId();

		List<Group> groups = _persistence.findByC_P(companyId, parentGroupId,
				start, end);

		Assert.assertEquals(expected, groups.size());
	}

	@Test
	public void testFindByC_S() throws Exception {
		Group group = addGroup();

		long companyId = group.getCompanyId();

		boolean site = group.getSite();

		List<Group> groups = _persistence.findByC_S(companyId, site);

		Assert.assertEquals(1, groups.size());

		Assert.assertEquals(group.getPrimaryKey(), groups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_SNotFound() throws Exception {
		addGroup();

		long companyId = ServiceTestUtil.nextLong();

		boolean site = ServiceTestUtil.randomBoolean();

		List<Group> groups = _persistence.findByC_S(companyId, site);

		Assert.assertEquals(0, groups.size());
	}

	@Test
	public void testFindByC_SStartEnd() throws Exception {
		testFindByC_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_SStartEndWrongRange() throws Exception {
		testFindByC_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_SStartEndZeroZero() throws Exception {
		testFindByC_SStartEnd(0, 0, 0);
	}

	protected void testFindByC_SStartEnd(int start, int end, int expected)
		throws Exception {
		Group group = addGroup();

		long companyId = group.getCompanyId();

		boolean site = group.getSite();

		List<Group> groups = _persistence.findByC_S(companyId, site, start, end);

		Assert.assertEquals(expected, groups.size());
	}

	@Test
	public void testFindByT_A() throws Exception {
		Group group = addGroup();

		int type = group.getType();

		boolean active = group.getActive();

		List<Group> groups = _persistence.findByT_A(type, active);

		Assert.assertEquals(1, groups.size());

		Assert.assertEquals(group.getPrimaryKey(), groups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByT_ANotFound() throws Exception {
		addGroup();

		int type = ServiceTestUtil.nextInt();

		boolean active = ServiceTestUtil.randomBoolean();

		List<Group> groups = _persistence.findByT_A(type, active);

		Assert.assertEquals(0, groups.size());
	}

	@Test
	public void testFindByT_AStartEnd() throws Exception {
		testFindByT_AStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByT_AStartEndWrongRange() throws Exception {
		testFindByT_AStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByT_AStartEndZeroZero() throws Exception {
		testFindByT_AStartEnd(0, 0, 0);
	}

	protected void testFindByT_AStartEnd(int start, int end, int expected)
		throws Exception {
		Group group = addGroup();

		int type = group.getType();

		boolean active = group.getActive();

		List<Group> groups = _persistence.findByT_A(type, active, start, end);

		Assert.assertEquals(expected, groups.size());
	}

	@Test
	public void testFindByG_C_P() throws Exception {
		Group group = addGroup();

		long groupId = group.getGroupId();

		long companyId = group.getCompanyId();

		long parentGroupId = group.getParentGroupId();

		List<Group> groups = _persistence.findByG_C_P(groupId, companyId,
				parentGroupId);

		Assert.assertEquals(1, groups.size());

		Assert.assertEquals(group.getPrimaryKey(), groups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_C_PNotFound() throws Exception {
		addGroup();

		long groupId = ServiceTestUtil.nextLong();

		long companyId = ServiceTestUtil.nextLong();

		long parentGroupId = ServiceTestUtil.nextLong();

		List<Group> groups = _persistence.findByG_C_P(groupId, companyId,
				parentGroupId);

		Assert.assertEquals(0, groups.size());
	}

	@Test
	public void testFindByG_C_PStartEnd() throws Exception {
		testFindByG_C_PStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_C_PStartEndWrongRange() throws Exception {
		testFindByG_C_PStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_C_PStartEndZeroZero() throws Exception {
		testFindByG_C_PStartEnd(0, 0, 0);
	}

	protected void testFindByG_C_PStartEnd(int start, int end, int expected)
		throws Exception {
		Group group = addGroup();

		long groupId = group.getGroupId();

		long companyId = group.getCompanyId();

		long parentGroupId = group.getParentGroupId();

		List<Group> groups = _persistence.findByG_C_P(groupId, companyId,
				parentGroupId, start, end);

		Assert.assertEquals(expected, groups.size());
	}

	@Test
	public void testFindByC_C_P() throws Exception {
		Group group = addGroup();

		long companyId = group.getCompanyId();

		long classNameId = group.getClassNameId();

		long parentGroupId = group.getParentGroupId();

		List<Group> groups = _persistence.findByC_C_P(companyId, classNameId,
				parentGroupId);

		Assert.assertEquals(1, groups.size());

		Assert.assertEquals(group.getPrimaryKey(), groups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_C_PNotFound() throws Exception {
		addGroup();

		long companyId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		long parentGroupId = ServiceTestUtil.nextLong();

		List<Group> groups = _persistence.findByC_C_P(companyId, classNameId,
				parentGroupId);

		Assert.assertEquals(0, groups.size());
	}

	@Test
	public void testFindByC_C_PStartEnd() throws Exception {
		testFindByC_C_PStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_C_PStartEndWrongRange() throws Exception {
		testFindByC_C_PStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_C_PStartEndZeroZero() throws Exception {
		testFindByC_C_PStartEnd(0, 0, 0);
	}

	protected void testFindByC_C_PStartEnd(int start, int end, int expected)
		throws Exception {
		Group group = addGroup();

		long companyId = group.getCompanyId();

		long classNameId = group.getClassNameId();

		long parentGroupId = group.getParentGroupId();

		List<Group> groups = _persistence.findByC_C_P(companyId, classNameId,
				parentGroupId, start, end);

		Assert.assertEquals(expected, groups.size());
	}

	@Test
	public void testFindByC_P_S() throws Exception {
		Group group = addGroup();

		long companyId = group.getCompanyId();

		long parentGroupId = group.getParentGroupId();

		boolean site = group.getSite();

		List<Group> groups = _persistence.findByC_P_S(companyId, parentGroupId,
				site);

		Assert.assertEquals(1, groups.size());

		Assert.assertEquals(group.getPrimaryKey(), groups.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_P_SNotFound() throws Exception {
		addGroup();

		long companyId = ServiceTestUtil.nextLong();

		long parentGroupId = ServiceTestUtil.nextLong();

		boolean site = ServiceTestUtil.randomBoolean();

		List<Group> groups = _persistence.findByC_P_S(companyId, parentGroupId,
				site);

		Assert.assertEquals(0, groups.size());
	}

	@Test
	public void testFindByC_P_SStartEnd() throws Exception {
		testFindByC_P_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_P_SStartEndWrongRange() throws Exception {
		testFindByC_P_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_P_SStartEndZeroZero() throws Exception {
		testFindByC_P_SStartEnd(0, 0, 0);
	}

	protected void testFindByC_P_SStartEnd(int start, int end, int expected)
		throws Exception {
		Group group = addGroup();

		long companyId = group.getCompanyId();

		long parentGroupId = group.getParentGroupId();

		boolean site = group.getSite();

		List<Group> groups = _persistence.findByC_P_S(companyId, parentGroupId,
				site, start, end);

		Assert.assertEquals(expected, groups.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("Group_", "uuid", true,
			"groupId", true, "companyId", true, "creatorUserId", true,
			"classNameId", true, "classPK", true, "parentGroupId", true,
			"liveGroupId", true, "treePath", true, "name", true, "description",
			true, "type", true, "typeSettings", true, "manualMembership", true,
			"membershipRestriction", true, "friendlyURL", true, "site", true,
			"remoteStagingGroupCount", true, "active", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		Group newGroup = addGroup();

		Group existingGroup = _persistence.fetchByPrimaryKey(newGroup.getPrimaryKey());

		Assert.assertEquals(existingGroup, newGroup);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Group missingGroup = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingGroup);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new GroupActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					Group group = (Group)object;

					Assert.assertNotNull(group);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		Group newGroup = addGroup();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Group.class,
				Group.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("groupId",
				newGroup.getGroupId()));

		List<Group> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Group existingGroup = result.get(0);

		Assert.assertEquals(existingGroup, newGroup);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Group.class,
				Group.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("groupId",
				ServiceTestUtil.nextLong()));

		List<Group> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		Group newGroup = addGroup();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Group.class,
				Group.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("groupId"));

		Object newGroupId = newGroup.getGroupId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("groupId",
				new Object[] { newGroupId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingGroupId = result.get(0);

		Assert.assertEquals(existingGroupId, newGroupId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Group.class,
				Group.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("groupId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("groupId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		Group newGroup = addGroup();

		_persistence.clearCache();

		GroupModelImpl existingGroupModelImpl = (GroupModelImpl)_persistence.findByPrimaryKey(newGroup.getPrimaryKey());

		Assert.assertTrue(Validator.equals(existingGroupModelImpl.getUuid(),
				existingGroupModelImpl.getOriginalUuid()));
		Assert.assertEquals(existingGroupModelImpl.getGroupId(),
			existingGroupModelImpl.getOriginalGroupId());

		Assert.assertEquals(existingGroupModelImpl.getLiveGroupId(),
			existingGroupModelImpl.getOriginalLiveGroupId());

		Assert.assertEquals(existingGroupModelImpl.getCompanyId(),
			existingGroupModelImpl.getOriginalCompanyId());
		Assert.assertTrue(Validator.equals(existingGroupModelImpl.getName(),
				existingGroupModelImpl.getOriginalName()));

		Assert.assertEquals(existingGroupModelImpl.getCompanyId(),
			existingGroupModelImpl.getOriginalCompanyId());
		Assert.assertTrue(Validator.equals(
				existingGroupModelImpl.getFriendlyURL(),
				existingGroupModelImpl.getOriginalFriendlyURL()));

		Assert.assertEquals(existingGroupModelImpl.getCompanyId(),
			existingGroupModelImpl.getOriginalCompanyId());
		Assert.assertEquals(existingGroupModelImpl.getClassNameId(),
			existingGroupModelImpl.getOriginalClassNameId());
		Assert.assertEquals(existingGroupModelImpl.getClassPK(),
			existingGroupModelImpl.getOriginalClassPK());

		Assert.assertEquals(existingGroupModelImpl.getCompanyId(),
			existingGroupModelImpl.getOriginalCompanyId());
		Assert.assertEquals(existingGroupModelImpl.getLiveGroupId(),
			existingGroupModelImpl.getOriginalLiveGroupId());
		Assert.assertTrue(Validator.equals(existingGroupModelImpl.getName(),
				existingGroupModelImpl.getOriginalName()));

		Assert.assertEquals(existingGroupModelImpl.getCompanyId(),
			existingGroupModelImpl.getOriginalCompanyId());
		Assert.assertEquals(existingGroupModelImpl.getClassNameId(),
			existingGroupModelImpl.getOriginalClassNameId());
		Assert.assertEquals(existingGroupModelImpl.getLiveGroupId(),
			existingGroupModelImpl.getOriginalLiveGroupId());
		Assert.assertTrue(Validator.equals(existingGroupModelImpl.getName(),
				existingGroupModelImpl.getOriginalName()));
	}

	protected Group addGroup() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Group group = _persistence.create(pk);

		group.setUuid(ServiceTestUtil.randomString());

		group.setCompanyId(ServiceTestUtil.nextLong());

		group.setCreatorUserId(ServiceTestUtil.nextLong());

		group.setClassNameId(ServiceTestUtil.nextLong());

		group.setClassPK(ServiceTestUtil.nextLong());

		group.setParentGroupId(ServiceTestUtil.nextLong());

		group.setLiveGroupId(ServiceTestUtil.nextLong());

		group.setTreePath(ServiceTestUtil.randomString());

		group.setName(ServiceTestUtil.randomString());

		group.setDescription(ServiceTestUtil.randomString());

		group.setType(ServiceTestUtil.nextInt());

		group.setTypeSettings(ServiceTestUtil.randomString());

		group.setManualMembership(ServiceTestUtil.randomBoolean());

		group.setMembershipRestriction(ServiceTestUtil.nextInt());

		group.setFriendlyURL(ServiceTestUtil.randomString());

		group.setSite(ServiceTestUtil.randomBoolean());

		group.setRemoteStagingGroupCount(ServiceTestUtil.nextInt());

		group.setActive(ServiceTestUtil.randomBoolean());

		_persistence.update(group);

		return group;
	}

	private static Log _log = LogFactoryUtil.getLog(GroupPersistenceTest.class);
	private GroupPersistence _persistence = (GroupPersistence)PortalBeanLocatorUtil.locate(GroupPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}