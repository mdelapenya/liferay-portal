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

package com.liferay.portlet.polls.service.persistence;

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

import com.liferay.portlet.polls.NoSuchChoiceException;
import com.liferay.portlet.polls.model.PollsChoice;
import com.liferay.portlet.polls.model.impl.PollsChoiceModelImpl;

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
public class PollsChoicePersistenceTest {
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

		PollsChoice pollsChoice = _persistence.create(pk);

		Assert.assertNotNull(pollsChoice);

		Assert.assertEquals(pollsChoice.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		PollsChoice newPollsChoice = addPollsChoice();

		_persistence.remove(newPollsChoice);

		PollsChoice existingPollsChoice = _persistence.fetchByPrimaryKey(newPollsChoice.getPrimaryKey());

		Assert.assertNull(existingPollsChoice);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addPollsChoice();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		PollsChoice newPollsChoice = _persistence.create(pk);

		newPollsChoice.setUuid(ServiceTestUtil.randomString());

		newPollsChoice.setGroupId(ServiceTestUtil.nextLong());

		newPollsChoice.setCompanyId(ServiceTestUtil.nextLong());

		newPollsChoice.setUserId(ServiceTestUtil.nextLong());

		newPollsChoice.setUserName(ServiceTestUtil.randomString());

		newPollsChoice.setCreateDate(ServiceTestUtil.nextDate());

		newPollsChoice.setModifiedDate(ServiceTestUtil.nextDate());

		newPollsChoice.setQuestionId(ServiceTestUtil.nextLong());

		newPollsChoice.setName(ServiceTestUtil.randomString());

		newPollsChoice.setDescription(ServiceTestUtil.randomString());

		_persistence.update(newPollsChoice);

		PollsChoice existingPollsChoice = _persistence.findByPrimaryKey(newPollsChoice.getPrimaryKey());

		Assert.assertEquals(existingPollsChoice.getUuid(),
			newPollsChoice.getUuid());
		Assert.assertEquals(existingPollsChoice.getChoiceId(),
			newPollsChoice.getChoiceId());
		Assert.assertEquals(existingPollsChoice.getGroupId(),
			newPollsChoice.getGroupId());
		Assert.assertEquals(existingPollsChoice.getCompanyId(),
			newPollsChoice.getCompanyId());
		Assert.assertEquals(existingPollsChoice.getUserId(),
			newPollsChoice.getUserId());
		Assert.assertEquals(existingPollsChoice.getUserName(),
			newPollsChoice.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingPollsChoice.getCreateDate()),
			Time.getShortTimestamp(newPollsChoice.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingPollsChoice.getModifiedDate()),
			Time.getShortTimestamp(newPollsChoice.getModifiedDate()));
		Assert.assertEquals(existingPollsChoice.getQuestionId(),
			newPollsChoice.getQuestionId());
		Assert.assertEquals(existingPollsChoice.getName(),
			newPollsChoice.getName());
		Assert.assertEquals(existingPollsChoice.getDescription(),
			newPollsChoice.getDescription());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		PollsChoice newPollsChoice = addPollsChoice();

		PollsChoice existingPollsChoice = _persistence.findByPrimaryKey(newPollsChoice.getPrimaryKey());

		Assert.assertEquals(existingPollsChoice, newPollsChoice);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchChoiceException");
		}
		catch (NoSuchChoiceException nsee) {
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
		PollsChoice pollsChoice = addPollsChoice();

		String uuid = pollsChoice.getUuid();

		List<PollsChoice> pollsChoices = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, pollsChoices.size());

		Assert.assertEquals(pollsChoice.getPrimaryKey(),
			pollsChoices.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addPollsChoice();

		String uuid = ServiceTestUtil.randomString();

		List<PollsChoice> pollsChoices = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, pollsChoices.size());
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
		PollsChoice pollsChoice = addPollsChoice();

		String uuid = pollsChoice.getUuid();

		List<PollsChoice> pollsChoices = _persistence.findByUuid(uuid, start,
				end);

		Assert.assertEquals(expected, pollsChoices.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		PollsChoice pollsChoice = addPollsChoice();

		String uuid = pollsChoice.getUuid();

		long companyId = pollsChoice.getCompanyId();

		List<PollsChoice> pollsChoices = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(1, pollsChoices.size());

		Assert.assertEquals(pollsChoice.getPrimaryKey(),
			pollsChoices.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addPollsChoice();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<PollsChoice> pollsChoices = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(0, pollsChoices.size());
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
		PollsChoice pollsChoice = addPollsChoice();

		String uuid = pollsChoice.getUuid();

		long companyId = pollsChoice.getCompanyId();

		List<PollsChoice> pollsChoices = _persistence.findByUuid_C(uuid,
				companyId, start, end);

		Assert.assertEquals(expected, pollsChoices.size());
	}

	@Test
	public void testFindByQuestionId() throws Exception {
		PollsChoice pollsChoice = addPollsChoice();

		long questionId = pollsChoice.getQuestionId();

		List<PollsChoice> pollsChoices = _persistence.findByQuestionId(questionId);

		Assert.assertEquals(1, pollsChoices.size());

		Assert.assertEquals(pollsChoice.getPrimaryKey(),
			pollsChoices.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByQuestionIdNotFound() throws Exception {
		addPollsChoice();

		long questionId = ServiceTestUtil.nextLong();

		List<PollsChoice> pollsChoices = _persistence.findByQuestionId(questionId);

		Assert.assertEquals(0, pollsChoices.size());
	}

	@Test
	public void testFindByQuestionIdStartEnd() throws Exception {
		testFindByQuestionIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByQuestionIdStartEndWrongRange()
		throws Exception {
		testFindByQuestionIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByQuestionIdStartEndZeroZero()
		throws Exception {
		testFindByQuestionIdStartEnd(0, 0, 0);
	}

	protected void testFindByQuestionIdStartEnd(int start, int end, int expected)
		throws Exception {
		PollsChoice pollsChoice = addPollsChoice();

		long questionId = pollsChoice.getQuestionId();

		List<PollsChoice> pollsChoices = _persistence.findByQuestionId(questionId,
				start, end);

		Assert.assertEquals(expected, pollsChoices.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("PollsChoice", "uuid", true,
			"choiceId", true, "groupId", true, "companyId", true, "userId",
			true, "userName", true, "createDate", true, "modifiedDate", true,
			"questionId", true, "name", true, "description", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		PollsChoice newPollsChoice = addPollsChoice();

		PollsChoice existingPollsChoice = _persistence.fetchByPrimaryKey(newPollsChoice.getPrimaryKey());

		Assert.assertEquals(existingPollsChoice, newPollsChoice);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		PollsChoice missingPollsChoice = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingPollsChoice);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new PollsChoiceActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					PollsChoice pollsChoice = (PollsChoice)object;

					Assert.assertNotNull(pollsChoice);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		PollsChoice newPollsChoice = addPollsChoice();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(PollsChoice.class,
				PollsChoice.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("choiceId",
				newPollsChoice.getChoiceId()));

		List<PollsChoice> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		PollsChoice existingPollsChoice = result.get(0);

		Assert.assertEquals(existingPollsChoice, newPollsChoice);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(PollsChoice.class,
				PollsChoice.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("choiceId",
				ServiceTestUtil.nextLong()));

		List<PollsChoice> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		PollsChoice newPollsChoice = addPollsChoice();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(PollsChoice.class,
				PollsChoice.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("choiceId"));

		Object newChoiceId = newPollsChoice.getChoiceId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("choiceId",
				new Object[] { newChoiceId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingChoiceId = result.get(0);

		Assert.assertEquals(existingChoiceId, newChoiceId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(PollsChoice.class,
				PollsChoice.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("choiceId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("choiceId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		PollsChoice newPollsChoice = addPollsChoice();

		_persistence.clearCache();

		PollsChoiceModelImpl existingPollsChoiceModelImpl = (PollsChoiceModelImpl)_persistence.findByPrimaryKey(newPollsChoice.getPrimaryKey());

		Assert.assertTrue(Validator.equals(
				existingPollsChoiceModelImpl.getUuid(),
				existingPollsChoiceModelImpl.getOriginalUuid()));
		Assert.assertEquals(existingPollsChoiceModelImpl.getGroupId(),
			existingPollsChoiceModelImpl.getOriginalGroupId());

		Assert.assertEquals(existingPollsChoiceModelImpl.getQuestionId(),
			existingPollsChoiceModelImpl.getOriginalQuestionId());
		Assert.assertTrue(Validator.equals(
				existingPollsChoiceModelImpl.getName(),
				existingPollsChoiceModelImpl.getOriginalName()));
	}

	protected PollsChoice addPollsChoice() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		PollsChoice pollsChoice = _persistence.create(pk);

		pollsChoice.setUuid(ServiceTestUtil.randomString());

		pollsChoice.setGroupId(ServiceTestUtil.nextLong());

		pollsChoice.setCompanyId(ServiceTestUtil.nextLong());

		pollsChoice.setUserId(ServiceTestUtil.nextLong());

		pollsChoice.setUserName(ServiceTestUtil.randomString());

		pollsChoice.setCreateDate(ServiceTestUtil.nextDate());

		pollsChoice.setModifiedDate(ServiceTestUtil.nextDate());

		pollsChoice.setQuestionId(ServiceTestUtil.nextLong());

		pollsChoice.setName(ServiceTestUtil.randomString());

		pollsChoice.setDescription(ServiceTestUtil.randomString());

		_persistence.update(pollsChoice);

		return pollsChoice;
	}

	private static Log _log = LogFactoryUtil.getLog(PollsChoicePersistenceTest.class);
	private PollsChoicePersistence _persistence = (PollsChoicePersistence)PortalBeanLocatorUtil.locate(PollsChoicePersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}