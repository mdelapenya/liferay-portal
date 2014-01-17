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

import com.liferay.portal.NoSuchBackgroundTaskException;
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
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;

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
public class BackgroundTaskPersistenceTest {
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

		BackgroundTask backgroundTask = _persistence.create(pk);

		Assert.assertNotNull(backgroundTask);

		Assert.assertEquals(backgroundTask.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		BackgroundTask newBackgroundTask = addBackgroundTask();

		_persistence.remove(newBackgroundTask);

		BackgroundTask existingBackgroundTask = _persistence.fetchByPrimaryKey(newBackgroundTask.getPrimaryKey());

		Assert.assertNull(existingBackgroundTask);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addBackgroundTask();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		BackgroundTask newBackgroundTask = _persistence.create(pk);

		newBackgroundTask.setGroupId(ServiceTestUtil.nextLong());

		newBackgroundTask.setCompanyId(ServiceTestUtil.nextLong());

		newBackgroundTask.setUserId(ServiceTestUtil.nextLong());

		newBackgroundTask.setUserName(ServiceTestUtil.randomString());

		newBackgroundTask.setCreateDate(ServiceTestUtil.nextDate());

		newBackgroundTask.setModifiedDate(ServiceTestUtil.nextDate());

		newBackgroundTask.setName(ServiceTestUtil.randomString());

		newBackgroundTask.setServletContextNames(ServiceTestUtil.randomString());

		newBackgroundTask.setTaskExecutorClassName(ServiceTestUtil.randomString());

		newBackgroundTask.setTaskContext(ServiceTestUtil.randomString());

		newBackgroundTask.setCompleted(ServiceTestUtil.randomBoolean());

		newBackgroundTask.setCompletionDate(ServiceTestUtil.nextDate());

		newBackgroundTask.setStatus(ServiceTestUtil.nextInt());

		newBackgroundTask.setStatusMessage(ServiceTestUtil.randomString());

		_persistence.update(newBackgroundTask);

		BackgroundTask existingBackgroundTask = _persistence.findByPrimaryKey(newBackgroundTask.getPrimaryKey());

		Assert.assertEquals(existingBackgroundTask.getBackgroundTaskId(),
			newBackgroundTask.getBackgroundTaskId());
		Assert.assertEquals(existingBackgroundTask.getGroupId(),
			newBackgroundTask.getGroupId());
		Assert.assertEquals(existingBackgroundTask.getCompanyId(),
			newBackgroundTask.getCompanyId());
		Assert.assertEquals(existingBackgroundTask.getUserId(),
			newBackgroundTask.getUserId());
		Assert.assertEquals(existingBackgroundTask.getUserName(),
			newBackgroundTask.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingBackgroundTask.getCreateDate()),
			Time.getShortTimestamp(newBackgroundTask.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingBackgroundTask.getModifiedDate()),
			Time.getShortTimestamp(newBackgroundTask.getModifiedDate()));
		Assert.assertEquals(existingBackgroundTask.getName(),
			newBackgroundTask.getName());
		Assert.assertEquals(existingBackgroundTask.getServletContextNames(),
			newBackgroundTask.getServletContextNames());
		Assert.assertEquals(existingBackgroundTask.getTaskExecutorClassName(),
			newBackgroundTask.getTaskExecutorClassName());
		Assert.assertEquals(existingBackgroundTask.getTaskContext(),
			newBackgroundTask.getTaskContext());
		Assert.assertEquals(existingBackgroundTask.getCompleted(),
			newBackgroundTask.getCompleted());
		Assert.assertEquals(Time.getShortTimestamp(
				existingBackgroundTask.getCompletionDate()),
			Time.getShortTimestamp(newBackgroundTask.getCompletionDate()));
		Assert.assertEquals(existingBackgroundTask.getStatus(),
			newBackgroundTask.getStatus());
		Assert.assertEquals(existingBackgroundTask.getStatusMessage(),
			newBackgroundTask.getStatusMessage());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		BackgroundTask newBackgroundTask = addBackgroundTask();

		BackgroundTask existingBackgroundTask = _persistence.findByPrimaryKey(newBackgroundTask.getPrimaryKey());

		Assert.assertEquals(existingBackgroundTask, newBackgroundTask);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchBackgroundTaskException");
		}
		catch (NoSuchBackgroundTaskException nsee) {
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
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		List<BackgroundTask> backgroundTasks = _persistence.findByGroupId(groupId);

		Assert.assertEquals(1, backgroundTasks.size());

		Assert.assertEquals(backgroundTask.getPrimaryKey(),
			backgroundTasks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByGroupIdNotFound() throws Exception {
		addBackgroundTask();

		long groupId = ServiceTestUtil.nextLong();

		List<BackgroundTask> backgroundTasks = _persistence.findByGroupId(groupId);

		Assert.assertEquals(0, backgroundTasks.size());
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
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		List<BackgroundTask> backgroundTasks = _persistence.findByGroupId(groupId,
				start, end);

		Assert.assertEquals(expected, backgroundTasks.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		long companyId = backgroundTask.getCompanyId();

		List<BackgroundTask> backgroundTasks = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, backgroundTasks.size());

		Assert.assertEquals(backgroundTask.getPrimaryKey(),
			backgroundTasks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addBackgroundTask();

		long companyId = ServiceTestUtil.nextLong();

		List<BackgroundTask> backgroundTasks = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, backgroundTasks.size());
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
		BackgroundTask backgroundTask = addBackgroundTask();

		long companyId = backgroundTask.getCompanyId();

		List<BackgroundTask> backgroundTasks = _persistence.findByCompanyId(companyId,
				start, end);

		Assert.assertEquals(expected, backgroundTasks.size());
	}

	@Test
	public void testFindByStatus() throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		int status = backgroundTask.getStatus();

		List<BackgroundTask> backgroundTasks = _persistence.findByStatus(status);

		Assert.assertEquals(1, backgroundTasks.size());

		Assert.assertEquals(backgroundTask.getPrimaryKey(),
			backgroundTasks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByStatusNotFound() throws Exception {
		addBackgroundTask();

		int status = ServiceTestUtil.nextInt();

		List<BackgroundTask> backgroundTasks = _persistence.findByStatus(status);

		Assert.assertEquals(0, backgroundTasks.size());
	}

	@Test
	public void testFindByStatusStartEnd() throws Exception {
		testFindByStatusStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByStatusStartEndWrongRange() throws Exception {
		testFindByStatusStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByStatusStartEndZeroZero() throws Exception {
		testFindByStatusStartEnd(0, 0, 0);
	}

	protected void testFindByStatusStartEnd(int start, int end, int expected)
		throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		int status = backgroundTask.getStatus();

		List<BackgroundTask> backgroundTasks = _persistence.findByStatus(status,
				start, end);

		Assert.assertEquals(expected, backgroundTasks.size());
	}

	@Test
	public void testFindByG_T() throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		String taskExecutorClassName = backgroundTask.getTaskExecutorClassName();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_T(groupId,
				taskExecutorClassName);

		Assert.assertEquals(1, backgroundTasks.size());

		Assert.assertEquals(backgroundTask.getPrimaryKey(),
			backgroundTasks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_TNotFound() throws Exception {
		addBackgroundTask();

		long groupId = ServiceTestUtil.nextLong();

		String taskExecutorClassName = ServiceTestUtil.randomString();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_T(groupId,
				taskExecutorClassName);

		Assert.assertEquals(0, backgroundTasks.size());
	}

	@Test
	public void testFindByG_TStartEnd() throws Exception {
		testFindByG_TStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_TStartEndWrongRange() throws Exception {
		testFindByG_TStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_TStartEndZeroZero() throws Exception {
		testFindByG_TStartEnd(0, 0, 0);
	}

	protected void testFindByG_TStartEnd(int start, int end, int expected)
		throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		String taskExecutorClassName = backgroundTask.getTaskExecutorClassName();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_T(groupId,
				taskExecutorClassName, start, end);

		Assert.assertEquals(expected, backgroundTasks.size());
	}

	@Test
	public void testFindByG_S() throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		int status = backgroundTask.getStatus();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_S(groupId,
				status);

		Assert.assertEquals(1, backgroundTasks.size());

		Assert.assertEquals(backgroundTask.getPrimaryKey(),
			backgroundTasks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_SNotFound() throws Exception {
		addBackgroundTask();

		long groupId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_S(groupId,
				status);

		Assert.assertEquals(0, backgroundTasks.size());
	}

	@Test
	public void testFindByG_SStartEnd() throws Exception {
		testFindByG_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_SStartEndWrongRange() throws Exception {
		testFindByG_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_SStartEndZeroZero() throws Exception {
		testFindByG_SStartEnd(0, 0, 0);
	}

	protected void testFindByG_SStartEnd(int start, int end, int expected)
		throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		int status = backgroundTask.getStatus();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_S(groupId,
				status, start, end);

		Assert.assertEquals(expected, backgroundTasks.size());
	}

	@Test
	public void testFindByT_S() throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		String taskExecutorClassName = backgroundTask.getTaskExecutorClassName();

		int status = backgroundTask.getStatus();

		List<BackgroundTask> backgroundTasks = _persistence.findByT_S(taskExecutorClassName,
				status);

		Assert.assertEquals(1, backgroundTasks.size());

		Assert.assertEquals(backgroundTask.getPrimaryKey(),
			backgroundTasks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByT_SNotFound() throws Exception {
		addBackgroundTask();

		String taskExecutorClassName = ServiceTestUtil.randomString();

		int status = ServiceTestUtil.nextInt();

		List<BackgroundTask> backgroundTasks = _persistence.findByT_S(taskExecutorClassName,
				status);

		Assert.assertEquals(0, backgroundTasks.size());
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
		BackgroundTask backgroundTask = addBackgroundTask();

		String taskExecutorClassName = backgroundTask.getTaskExecutorClassName();

		int status = backgroundTask.getStatus();

		List<BackgroundTask> backgroundTasks = _persistence.findByT_S(taskExecutorClassName,
				status, start, end);

		Assert.assertEquals(expected, backgroundTasks.size());
	}

	@Test
	public void testFindByG_N_T() throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		String name = backgroundTask.getName();

		String taskExecutorClassName = backgroundTask.getTaskExecutorClassName();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_N_T(groupId,
				name, taskExecutorClassName);

		Assert.assertEquals(1, backgroundTasks.size());

		Assert.assertEquals(backgroundTask.getPrimaryKey(),
			backgroundTasks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_N_TNotFound() throws Exception {
		addBackgroundTask();

		long groupId = ServiceTestUtil.nextLong();

		String name = ServiceTestUtil.randomString();

		String taskExecutorClassName = ServiceTestUtil.randomString();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_N_T(groupId,
				name, taskExecutorClassName);

		Assert.assertEquals(0, backgroundTasks.size());
	}

	@Test
	public void testFindByG_N_TStartEnd() throws Exception {
		testFindByG_N_TStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_N_TStartEndWrongRange() throws Exception {
		testFindByG_N_TStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_N_TStartEndZeroZero() throws Exception {
		testFindByG_N_TStartEnd(0, 0, 0);
	}

	protected void testFindByG_N_TStartEnd(int start, int end, int expected)
		throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		String name = backgroundTask.getName();

		String taskExecutorClassName = backgroundTask.getTaskExecutorClassName();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_N_T(groupId,
				name, taskExecutorClassName, start, end);

		Assert.assertEquals(expected, backgroundTasks.size());
	}

	@Test
	public void testFindByG_T_C() throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		String taskExecutorClassName = backgroundTask.getTaskExecutorClassName();

		boolean completed = backgroundTask.getCompleted();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_T_C(groupId,
				taskExecutorClassName, completed);

		Assert.assertEquals(1, backgroundTasks.size());

		Assert.assertEquals(backgroundTask.getPrimaryKey(),
			backgroundTasks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_T_CNotFound() throws Exception {
		addBackgroundTask();

		long groupId = ServiceTestUtil.nextLong();

		String taskExecutorClassName = ServiceTestUtil.randomString();

		boolean completed = ServiceTestUtil.randomBoolean();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_T_C(groupId,
				taskExecutorClassName, completed);

		Assert.assertEquals(0, backgroundTasks.size());
	}

	@Test
	public void testFindByG_T_CStartEnd() throws Exception {
		testFindByG_T_CStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_T_CStartEndWrongRange() throws Exception {
		testFindByG_T_CStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_T_CStartEndZeroZero() throws Exception {
		testFindByG_T_CStartEnd(0, 0, 0);
	}

	protected void testFindByG_T_CStartEnd(int start, int end, int expected)
		throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		String taskExecutorClassName = backgroundTask.getTaskExecutorClassName();

		boolean completed = backgroundTask.getCompleted();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_T_C(groupId,
				taskExecutorClassName, completed, start, end);

		Assert.assertEquals(expected, backgroundTasks.size());
	}

	@Test
	public void testFindByG_T_S() throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		String taskExecutorClassName = backgroundTask.getTaskExecutorClassName();

		int status = backgroundTask.getStatus();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_T_S(groupId,
				taskExecutorClassName, status);

		Assert.assertEquals(1, backgroundTasks.size());

		Assert.assertEquals(backgroundTask.getPrimaryKey(),
			backgroundTasks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_T_SNotFound() throws Exception {
		addBackgroundTask();

		long groupId = ServiceTestUtil.nextLong();

		String taskExecutorClassName = ServiceTestUtil.randomString();

		int status = ServiceTestUtil.nextInt();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_T_S(groupId,
				taskExecutorClassName, status);

		Assert.assertEquals(0, backgroundTasks.size());
	}

	@Test
	public void testFindByG_T_SStartEnd() throws Exception {
		testFindByG_T_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_T_SStartEndWrongRange() throws Exception {
		testFindByG_T_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_T_SStartEndZeroZero() throws Exception {
		testFindByG_T_SStartEnd(0, 0, 0);
	}

	protected void testFindByG_T_SStartEnd(int start, int end, int expected)
		throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		String taskExecutorClassName = backgroundTask.getTaskExecutorClassName();

		int status = backgroundTask.getStatus();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_T_S(groupId,
				taskExecutorClassName, status, start, end);

		Assert.assertEquals(expected, backgroundTasks.size());
	}

	@Test
	public void testFindByG_N_T_C() throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		String name = backgroundTask.getName();

		String taskExecutorClassName = backgroundTask.getTaskExecutorClassName();

		boolean completed = backgroundTask.getCompleted();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_N_T_C(groupId,
				name, taskExecutorClassName, completed);

		Assert.assertEquals(1, backgroundTasks.size());

		Assert.assertEquals(backgroundTask.getPrimaryKey(),
			backgroundTasks.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_N_T_CNotFound() throws Exception {
		addBackgroundTask();

		long groupId = ServiceTestUtil.nextLong();

		String name = ServiceTestUtil.randomString();

		String taskExecutorClassName = ServiceTestUtil.randomString();

		boolean completed = ServiceTestUtil.randomBoolean();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_N_T_C(groupId,
				name, taskExecutorClassName, completed);

		Assert.assertEquals(0, backgroundTasks.size());
	}

	@Test
	public void testFindByG_N_T_CStartEnd() throws Exception {
		testFindByG_N_T_CStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_N_T_CStartEndWrongRange() throws Exception {
		testFindByG_N_T_CStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_N_T_CStartEndZeroZero() throws Exception {
		testFindByG_N_T_CStartEnd(0, 0, 0);
	}

	protected void testFindByG_N_T_CStartEnd(int start, int end, int expected)
		throws Exception {
		BackgroundTask backgroundTask = addBackgroundTask();

		long groupId = backgroundTask.getGroupId();

		String name = backgroundTask.getName();

		String taskExecutorClassName = backgroundTask.getTaskExecutorClassName();

		boolean completed = backgroundTask.getCompleted();

		List<BackgroundTask> backgroundTasks = _persistence.findByG_N_T_C(groupId,
				name, taskExecutorClassName, completed, start, end);

		Assert.assertEquals(expected, backgroundTasks.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("BackgroundTask",
			"backgroundTaskId", true, "groupId", true, "companyId", true,
			"userId", true, "userName", true, "createDate", true,
			"modifiedDate", true, "name", true, "servletContextNames", true,
			"taskExecutorClassName", true, "taskContext", true, "completed",
			true, "completionDate", true, "status", true, "statusMessage", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		BackgroundTask newBackgroundTask = addBackgroundTask();

		BackgroundTask existingBackgroundTask = _persistence.fetchByPrimaryKey(newBackgroundTask.getPrimaryKey());

		Assert.assertEquals(existingBackgroundTask, newBackgroundTask);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		BackgroundTask missingBackgroundTask = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingBackgroundTask);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new BackgroundTaskActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					BackgroundTask backgroundTask = (BackgroundTask)object;

					Assert.assertNotNull(backgroundTask);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		BackgroundTask newBackgroundTask = addBackgroundTask();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(BackgroundTask.class,
				BackgroundTask.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("backgroundTaskId",
				newBackgroundTask.getBackgroundTaskId()));

		List<BackgroundTask> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		BackgroundTask existingBackgroundTask = result.get(0);

		Assert.assertEquals(existingBackgroundTask, newBackgroundTask);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(BackgroundTask.class,
				BackgroundTask.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("backgroundTaskId",
				ServiceTestUtil.nextLong()));

		List<BackgroundTask> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		BackgroundTask newBackgroundTask = addBackgroundTask();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(BackgroundTask.class,
				BackgroundTask.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"backgroundTaskId"));

		Object newBackgroundTaskId = newBackgroundTask.getBackgroundTaskId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("backgroundTaskId",
				new Object[] { newBackgroundTaskId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingBackgroundTaskId = result.get(0);

		Assert.assertEquals(existingBackgroundTaskId, newBackgroundTaskId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(BackgroundTask.class,
				BackgroundTask.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"backgroundTaskId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("backgroundTaskId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected BackgroundTask addBackgroundTask() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		BackgroundTask backgroundTask = _persistence.create(pk);

		backgroundTask.setGroupId(ServiceTestUtil.nextLong());

		backgroundTask.setCompanyId(ServiceTestUtil.nextLong());

		backgroundTask.setUserId(ServiceTestUtil.nextLong());

		backgroundTask.setUserName(ServiceTestUtil.randomString());

		backgroundTask.setCreateDate(ServiceTestUtil.nextDate());

		backgroundTask.setModifiedDate(ServiceTestUtil.nextDate());

		backgroundTask.setName(ServiceTestUtil.randomString());

		backgroundTask.setServletContextNames(ServiceTestUtil.randomString());

		backgroundTask.setTaskExecutorClassName(ServiceTestUtil.randomString());

		backgroundTask.setTaskContext(ServiceTestUtil.randomString());

		backgroundTask.setCompleted(ServiceTestUtil.randomBoolean());

		backgroundTask.setCompletionDate(ServiceTestUtil.nextDate());

		backgroundTask.setStatus(ServiceTestUtil.nextInt());

		backgroundTask.setStatusMessage(ServiceTestUtil.randomString());

		_persistence.update(backgroundTask);

		return backgroundTask;
	}

	private static Log _log = LogFactoryUtil.getLog(BackgroundTaskPersistenceTest.class);
	private BackgroundTaskPersistence _persistence = (BackgroundTaskPersistence)PortalBeanLocatorUtil.locate(BackgroundTaskPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}