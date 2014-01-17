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

import com.liferay.portal.NoSuchWorkflowDefinitionLinkException;
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
import com.liferay.portal.model.WorkflowDefinitionLink;
import com.liferay.portal.model.impl.WorkflowDefinitionLinkModelImpl;
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
public class WorkflowDefinitionLinkPersistenceTest {
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

		WorkflowDefinitionLink workflowDefinitionLink = _persistence.create(pk);

		Assert.assertNotNull(workflowDefinitionLink);

		Assert.assertEquals(workflowDefinitionLink.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		WorkflowDefinitionLink newWorkflowDefinitionLink = addWorkflowDefinitionLink();

		_persistence.remove(newWorkflowDefinitionLink);

		WorkflowDefinitionLink existingWorkflowDefinitionLink = _persistence.fetchByPrimaryKey(newWorkflowDefinitionLink.getPrimaryKey());

		Assert.assertNull(existingWorkflowDefinitionLink);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addWorkflowDefinitionLink();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		WorkflowDefinitionLink newWorkflowDefinitionLink = _persistence.create(pk);

		newWorkflowDefinitionLink.setGroupId(ServiceTestUtil.nextLong());

		newWorkflowDefinitionLink.setCompanyId(ServiceTestUtil.nextLong());

		newWorkflowDefinitionLink.setUserId(ServiceTestUtil.nextLong());

		newWorkflowDefinitionLink.setUserName(ServiceTestUtil.randomString());

		newWorkflowDefinitionLink.setCreateDate(ServiceTestUtil.nextDate());

		newWorkflowDefinitionLink.setModifiedDate(ServiceTestUtil.nextDate());

		newWorkflowDefinitionLink.setClassNameId(ServiceTestUtil.nextLong());

		newWorkflowDefinitionLink.setClassPK(ServiceTestUtil.nextLong());

		newWorkflowDefinitionLink.setTypePK(ServiceTestUtil.nextLong());

		newWorkflowDefinitionLink.setWorkflowDefinitionName(ServiceTestUtil.randomString());

		newWorkflowDefinitionLink.setWorkflowDefinitionVersion(ServiceTestUtil.nextInt());

		_persistence.update(newWorkflowDefinitionLink);

		WorkflowDefinitionLink existingWorkflowDefinitionLink = _persistence.findByPrimaryKey(newWorkflowDefinitionLink.getPrimaryKey());

		Assert.assertEquals(existingWorkflowDefinitionLink.getWorkflowDefinitionLinkId(),
			newWorkflowDefinitionLink.getWorkflowDefinitionLinkId());
		Assert.assertEquals(existingWorkflowDefinitionLink.getGroupId(),
			newWorkflowDefinitionLink.getGroupId());
		Assert.assertEquals(existingWorkflowDefinitionLink.getCompanyId(),
			newWorkflowDefinitionLink.getCompanyId());
		Assert.assertEquals(existingWorkflowDefinitionLink.getUserId(),
			newWorkflowDefinitionLink.getUserId());
		Assert.assertEquals(existingWorkflowDefinitionLink.getUserName(),
			newWorkflowDefinitionLink.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingWorkflowDefinitionLink.getCreateDate()),
			Time.getShortTimestamp(newWorkflowDefinitionLink.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingWorkflowDefinitionLink.getModifiedDate()),
			Time.getShortTimestamp(newWorkflowDefinitionLink.getModifiedDate()));
		Assert.assertEquals(existingWorkflowDefinitionLink.getClassNameId(),
			newWorkflowDefinitionLink.getClassNameId());
		Assert.assertEquals(existingWorkflowDefinitionLink.getClassPK(),
			newWorkflowDefinitionLink.getClassPK());
		Assert.assertEquals(existingWorkflowDefinitionLink.getTypePK(),
			newWorkflowDefinitionLink.getTypePK());
		Assert.assertEquals(existingWorkflowDefinitionLink.getWorkflowDefinitionName(),
			newWorkflowDefinitionLink.getWorkflowDefinitionName());
		Assert.assertEquals(existingWorkflowDefinitionLink.getWorkflowDefinitionVersion(),
			newWorkflowDefinitionLink.getWorkflowDefinitionVersion());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		WorkflowDefinitionLink newWorkflowDefinitionLink = addWorkflowDefinitionLink();

		WorkflowDefinitionLink existingWorkflowDefinitionLink = _persistence.findByPrimaryKey(newWorkflowDefinitionLink.getPrimaryKey());

		Assert.assertEquals(existingWorkflowDefinitionLink,
			newWorkflowDefinitionLink);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchWorkflowDefinitionLinkException");
		}
		catch (NoSuchWorkflowDefinitionLinkException nsee) {
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
	public void testFindByCompanyId() throws Exception {
		WorkflowDefinitionLink workflowDefinitionLink = addWorkflowDefinitionLink();

		long companyId = workflowDefinitionLink.getCompanyId();

		List<WorkflowDefinitionLink> workflowDefinitionLinks = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, workflowDefinitionLinks.size());

		Assert.assertEquals(workflowDefinitionLink.getPrimaryKey(),
			workflowDefinitionLinks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addWorkflowDefinitionLink();

		long companyId = ServiceTestUtil.nextLong();

		List<WorkflowDefinitionLink> workflowDefinitionLinks = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, workflowDefinitionLinks.size());
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
		WorkflowDefinitionLink workflowDefinitionLink = addWorkflowDefinitionLink();

		long companyId = workflowDefinitionLink.getCompanyId();

		List<WorkflowDefinitionLink> workflowDefinitionLinks = _persistence.findByCompanyId(companyId,
				start, end);

		Assert.assertEquals(expected, workflowDefinitionLinks.size());
	}

	@Test
	public void testFindByC_W_W() throws Exception {
		WorkflowDefinitionLink workflowDefinitionLink = addWorkflowDefinitionLink();

		long companyId = workflowDefinitionLink.getCompanyId();

		String workflowDefinitionName = workflowDefinitionLink.getWorkflowDefinitionName();

		int workflowDefinitionVersion = workflowDefinitionLink.getWorkflowDefinitionVersion();

		List<WorkflowDefinitionLink> workflowDefinitionLinks = _persistence.findByC_W_W(companyId,
				workflowDefinitionName, workflowDefinitionVersion);

		Assert.assertEquals(1, workflowDefinitionLinks.size());

		Assert.assertEquals(workflowDefinitionLink.getPrimaryKey(),
			workflowDefinitionLinks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_W_WNotFound() throws Exception {
		addWorkflowDefinitionLink();

		long companyId = ServiceTestUtil.nextLong();

		String workflowDefinitionName = ServiceTestUtil.randomString();

		int workflowDefinitionVersion = ServiceTestUtil.nextInt();

		List<WorkflowDefinitionLink> workflowDefinitionLinks = _persistence.findByC_W_W(companyId,
				workflowDefinitionName, workflowDefinitionVersion);

		Assert.assertEquals(0, workflowDefinitionLinks.size());
	}

	@Test
	public void testFindByC_W_WStartEnd() throws Exception {
		testFindByC_W_WStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_W_WStartEndWrongRange() throws Exception {
		testFindByC_W_WStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_W_WStartEndZeroZero() throws Exception {
		testFindByC_W_WStartEnd(0, 0, 0);
	}

	protected void testFindByC_W_WStartEnd(int start, int end, int expected)
		throws Exception {
		WorkflowDefinitionLink workflowDefinitionLink = addWorkflowDefinitionLink();

		long companyId = workflowDefinitionLink.getCompanyId();

		String workflowDefinitionName = workflowDefinitionLink.getWorkflowDefinitionName();

		int workflowDefinitionVersion = workflowDefinitionLink.getWorkflowDefinitionVersion();

		List<WorkflowDefinitionLink> workflowDefinitionLinks = _persistence.findByC_W_W(companyId,
				workflowDefinitionName, workflowDefinitionVersion, start, end);

		Assert.assertEquals(expected, workflowDefinitionLinks.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("WorkflowDefinitionLink",
			"workflowDefinitionLinkId", true, "groupId", true, "companyId",
			true, "userId", true, "userName", true, "createDate", true,
			"modifiedDate", true, "classNameId", true, "classPK", true,
			"typePK", true, "workflowDefinitionName", true,
			"workflowDefinitionVersion", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		WorkflowDefinitionLink newWorkflowDefinitionLink = addWorkflowDefinitionLink();

		WorkflowDefinitionLink existingWorkflowDefinitionLink = _persistence.fetchByPrimaryKey(newWorkflowDefinitionLink.getPrimaryKey());

		Assert.assertEquals(existingWorkflowDefinitionLink,
			newWorkflowDefinitionLink);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		WorkflowDefinitionLink missingWorkflowDefinitionLink = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingWorkflowDefinitionLink);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new WorkflowDefinitionLinkActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					WorkflowDefinitionLink workflowDefinitionLink = (WorkflowDefinitionLink)object;

					Assert.assertNotNull(workflowDefinitionLink);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		WorkflowDefinitionLink newWorkflowDefinitionLink = addWorkflowDefinitionLink();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(WorkflowDefinitionLink.class,
				WorkflowDefinitionLink.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq(
				"workflowDefinitionLinkId",
				newWorkflowDefinitionLink.getWorkflowDefinitionLinkId()));

		List<WorkflowDefinitionLink> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		WorkflowDefinitionLink existingWorkflowDefinitionLink = result.get(0);

		Assert.assertEquals(existingWorkflowDefinitionLink,
			newWorkflowDefinitionLink);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(WorkflowDefinitionLink.class,
				WorkflowDefinitionLink.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq(
				"workflowDefinitionLinkId", ServiceTestUtil.nextLong()));

		List<WorkflowDefinitionLink> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		WorkflowDefinitionLink newWorkflowDefinitionLink = addWorkflowDefinitionLink();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(WorkflowDefinitionLink.class,
				WorkflowDefinitionLink.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"workflowDefinitionLinkId"));

		Object newWorkflowDefinitionLinkId = newWorkflowDefinitionLink.getWorkflowDefinitionLinkId();

		dynamicQuery.add(RestrictionsFactoryUtil.in(
				"workflowDefinitionLinkId",
				new Object[] { newWorkflowDefinitionLinkId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingWorkflowDefinitionLinkId = result.get(0);

		Assert.assertEquals(existingWorkflowDefinitionLinkId,
			newWorkflowDefinitionLinkId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(WorkflowDefinitionLink.class,
				WorkflowDefinitionLink.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"workflowDefinitionLinkId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in(
				"workflowDefinitionLinkId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		WorkflowDefinitionLink newWorkflowDefinitionLink = addWorkflowDefinitionLink();

		_persistence.clearCache();

		WorkflowDefinitionLinkModelImpl existingWorkflowDefinitionLinkModelImpl = (WorkflowDefinitionLinkModelImpl)_persistence.findByPrimaryKey(newWorkflowDefinitionLink.getPrimaryKey());

		Assert.assertEquals(existingWorkflowDefinitionLinkModelImpl.getGroupId(),
			existingWorkflowDefinitionLinkModelImpl.getOriginalGroupId());
		Assert.assertEquals(existingWorkflowDefinitionLinkModelImpl.getCompanyId(),
			existingWorkflowDefinitionLinkModelImpl.getOriginalCompanyId());
		Assert.assertEquals(existingWorkflowDefinitionLinkModelImpl.getClassNameId(),
			existingWorkflowDefinitionLinkModelImpl.getOriginalClassNameId());
		Assert.assertEquals(existingWorkflowDefinitionLinkModelImpl.getClassPK(),
			existingWorkflowDefinitionLinkModelImpl.getOriginalClassPK());
		Assert.assertEquals(existingWorkflowDefinitionLinkModelImpl.getTypePK(),
			existingWorkflowDefinitionLinkModelImpl.getOriginalTypePK());
	}

	protected WorkflowDefinitionLink addWorkflowDefinitionLink()
		throws Exception {
		long pk = ServiceTestUtil.nextLong();

		WorkflowDefinitionLink workflowDefinitionLink = _persistence.create(pk);

		workflowDefinitionLink.setGroupId(ServiceTestUtil.nextLong());

		workflowDefinitionLink.setCompanyId(ServiceTestUtil.nextLong());

		workflowDefinitionLink.setUserId(ServiceTestUtil.nextLong());

		workflowDefinitionLink.setUserName(ServiceTestUtil.randomString());

		workflowDefinitionLink.setCreateDate(ServiceTestUtil.nextDate());

		workflowDefinitionLink.setModifiedDate(ServiceTestUtil.nextDate());

		workflowDefinitionLink.setClassNameId(ServiceTestUtil.nextLong());

		workflowDefinitionLink.setClassPK(ServiceTestUtil.nextLong());

		workflowDefinitionLink.setTypePK(ServiceTestUtil.nextLong());

		workflowDefinitionLink.setWorkflowDefinitionName(ServiceTestUtil.randomString());

		workflowDefinitionLink.setWorkflowDefinitionVersion(ServiceTestUtil.nextInt());

		_persistence.update(workflowDefinitionLink);

		return workflowDefinitionLink;
	}

	private static Log _log = LogFactoryUtil.getLog(WorkflowDefinitionLinkPersistenceTest.class);
	private WorkflowDefinitionLinkPersistence _persistence = (WorkflowDefinitionLinkPersistence)PortalBeanLocatorUtil.locate(WorkflowDefinitionLinkPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}