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

package com.liferay.portlet.blogs.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.test.AssertUtils;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;
import com.liferay.portal.util.PropsValues;

import com.liferay.portlet.blogs.NoSuchStatsUserException;
import com.liferay.portlet.blogs.model.BlogsStatsUser;
import com.liferay.portlet.blogs.model.impl.BlogsStatsUserModelImpl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 */
@ExecutionTestListeners(listeners =  {
	PersistenceExecutionTestListener.class})
@RunWith(LiferayPersistenceIntegrationJUnitTestRunner.class)
public class BlogsStatsUserPersistenceTest {
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

		BlogsStatsUser blogsStatsUser = _persistence.create(pk);

		Assert.assertNotNull(blogsStatsUser);

		Assert.assertEquals(blogsStatsUser.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		BlogsStatsUser newBlogsStatsUser = addBlogsStatsUser();

		_persistence.remove(newBlogsStatsUser);

		BlogsStatsUser existingBlogsStatsUser = _persistence.fetchByPrimaryKey(newBlogsStatsUser.getPrimaryKey());

		Assert.assertNull(existingBlogsStatsUser);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addBlogsStatsUser();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		BlogsStatsUser newBlogsStatsUser = _persistence.create(pk);

		newBlogsStatsUser.setGroupId(ServiceTestUtil.nextLong());

		newBlogsStatsUser.setCompanyId(ServiceTestUtil.nextLong());

		newBlogsStatsUser.setUserId(ServiceTestUtil.nextLong());

		newBlogsStatsUser.setEntryCount(ServiceTestUtil.nextInt());

		newBlogsStatsUser.setLastPostDate(ServiceTestUtil.nextDate());

		newBlogsStatsUser.setRatingsTotalEntries(ServiceTestUtil.nextInt());

		newBlogsStatsUser.setRatingsTotalScore(ServiceTestUtil.nextDouble());

		newBlogsStatsUser.setRatingsAverageScore(ServiceTestUtil.nextDouble());

		_persistence.update(newBlogsStatsUser);

		BlogsStatsUser existingBlogsStatsUser = _persistence.findByPrimaryKey(newBlogsStatsUser.getPrimaryKey());

		Assert.assertEquals(existingBlogsStatsUser.getStatsUserId(),
			newBlogsStatsUser.getStatsUserId());
		Assert.assertEquals(existingBlogsStatsUser.getGroupId(),
			newBlogsStatsUser.getGroupId());
		Assert.assertEquals(existingBlogsStatsUser.getCompanyId(),
			newBlogsStatsUser.getCompanyId());
		Assert.assertEquals(existingBlogsStatsUser.getUserId(),
			newBlogsStatsUser.getUserId());
		Assert.assertEquals(existingBlogsStatsUser.getEntryCount(),
			newBlogsStatsUser.getEntryCount());
		Assert.assertEquals(Time.getShortTimestamp(
				existingBlogsStatsUser.getLastPostDate()),
			Time.getShortTimestamp(newBlogsStatsUser.getLastPostDate()));
		Assert.assertEquals(existingBlogsStatsUser.getRatingsTotalEntries(),
			newBlogsStatsUser.getRatingsTotalEntries());
		AssertUtils.assertEquals(existingBlogsStatsUser.getRatingsTotalScore(),
			newBlogsStatsUser.getRatingsTotalScore());
		AssertUtils.assertEquals(existingBlogsStatsUser.getRatingsAverageScore(),
			newBlogsStatsUser.getRatingsAverageScore());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		BlogsStatsUser newBlogsStatsUser = addBlogsStatsUser();

		BlogsStatsUser existingBlogsStatsUser = _persistence.findByPrimaryKey(newBlogsStatsUser.getPrimaryKey());

		Assert.assertEquals(existingBlogsStatsUser, newBlogsStatsUser);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchStatsUserException");
		}
		catch (NoSuchStatsUserException nsee) {
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
		BlogsStatsUser blogsStatsUser = addBlogsStatsUser();

		long groupId = blogsStatsUser.getGroupId();

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByGroupId(groupId);

		Assert.assertEquals(1, blogsStatsUsers.size());

		Assert.assertEquals(blogsStatsUser.getPrimaryKey(),
			blogsStatsUsers.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByGroupIdNotFound() throws Exception {
		addBlogsStatsUser();

		long groupId = ServiceTestUtil.nextLong();

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByGroupId(groupId);

		Assert.assertEquals(0, blogsStatsUsers.size());
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
		BlogsStatsUser blogsStatsUser = addBlogsStatsUser();

		long groupId = blogsStatsUser.getGroupId();

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByGroupId(groupId,
				start, end);

		Assert.assertEquals(expected, blogsStatsUsers.size());
	}

	@Test
	public void testFindByUserId() throws Exception {
		BlogsStatsUser blogsStatsUser = addBlogsStatsUser();

		long userId = blogsStatsUser.getUserId();

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByUserId(userId);

		Assert.assertEquals(1, blogsStatsUsers.size());

		Assert.assertEquals(blogsStatsUser.getPrimaryKey(),
			blogsStatsUsers.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUserIdNotFound() throws Exception {
		addBlogsStatsUser();

		long userId = ServiceTestUtil.nextLong();

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByUserId(userId);

		Assert.assertEquals(0, blogsStatsUsers.size());
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
		BlogsStatsUser blogsStatsUser = addBlogsStatsUser();

		long userId = blogsStatsUser.getUserId();

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByUserId(userId,
				start, end);

		Assert.assertEquals(expected, blogsStatsUsers.size());
	}

	@Test
	public void testFindByG_NotE() throws Exception {
		BlogsStatsUser blogsStatsUser = addBlogsStatsUser();

		long groupId = blogsStatsUser.getGroupId();

		int entryCount = 1;

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByG_NotE(groupId,
				entryCount);

		Assert.assertEquals(1, blogsStatsUsers.size());

		Assert.assertEquals(blogsStatsUser.getPrimaryKey(),
			blogsStatsUsers.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_NotENotFound() throws Exception {
		addBlogsStatsUser();

		long groupId = ServiceTestUtil.nextLong();

		int entryCount = ServiceTestUtil.nextInt();

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByG_NotE(groupId,
				entryCount);

		Assert.assertEquals(0, blogsStatsUsers.size());
	}

	@Test
	public void testFindByG_NotEStartEnd() throws Exception {
		testFindByG_NotEStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_NotEStartEndWrongRange() throws Exception {
		testFindByG_NotEStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_NotEStartEndZeroZero() throws Exception {
		testFindByG_NotEStartEnd(0, 0, 0);
	}

	protected void testFindByG_NotEStartEnd(int start, int end, int expected)
		throws Exception {
		BlogsStatsUser blogsStatsUser = addBlogsStatsUser();

		long groupId = blogsStatsUser.getGroupId();

		int entryCount = 1;

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByG_NotE(groupId,
				entryCount, start, end);

		Assert.assertEquals(expected, blogsStatsUsers.size());
	}

	@Test
	public void testFindByC_NotE() throws Exception {
		BlogsStatsUser blogsStatsUser = addBlogsStatsUser();

		long companyId = blogsStatsUser.getCompanyId();

		int entryCount = 1;

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByC_NotE(companyId,
				entryCount);

		Assert.assertEquals(1, blogsStatsUsers.size());

		Assert.assertEquals(blogsStatsUser.getPrimaryKey(),
			blogsStatsUsers.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_NotENotFound() throws Exception {
		addBlogsStatsUser();

		long companyId = ServiceTestUtil.nextLong();

		int entryCount = ServiceTestUtil.nextInt();

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByC_NotE(companyId,
				entryCount);

		Assert.assertEquals(0, blogsStatsUsers.size());
	}

	@Test
	public void testFindByC_NotEStartEnd() throws Exception {
		testFindByC_NotEStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_NotEStartEndWrongRange() throws Exception {
		testFindByC_NotEStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_NotEStartEndZeroZero() throws Exception {
		testFindByC_NotEStartEnd(0, 0, 0);
	}

	protected void testFindByC_NotEStartEnd(int start, int end, int expected)
		throws Exception {
		BlogsStatsUser blogsStatsUser = addBlogsStatsUser();

		long companyId = blogsStatsUser.getCompanyId();

		int entryCount = 1;

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByC_NotE(companyId,
				entryCount, start, end);

		Assert.assertEquals(expected, blogsStatsUsers.size());
	}

	@Test
	public void testFindByU_L() throws Exception {
		BlogsStatsUser blogsStatsUser = addBlogsStatsUser();

		long userId = blogsStatsUser.getUserId();

		Date lastPostDate = blogsStatsUser.getLastPostDate();

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByU_L(userId,
				lastPostDate);

		Assert.assertEquals(1, blogsStatsUsers.size());

		Assert.assertEquals(blogsStatsUser.getPrimaryKey(),
			blogsStatsUsers.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByU_LNotFound() throws Exception {
		addBlogsStatsUser();

		long userId = ServiceTestUtil.nextLong();

		Date lastPostDate = ServiceTestUtil.nextDate();

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByU_L(userId,
				lastPostDate);

		Assert.assertEquals(0, blogsStatsUsers.size());
	}

	@Test
	public void testFindByU_LStartEnd() throws Exception {
		testFindByU_LStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByU_LStartEndWrongRange() throws Exception {
		testFindByU_LStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByU_LStartEndZeroZero() throws Exception {
		testFindByU_LStartEnd(0, 0, 0);
	}

	protected void testFindByU_LStartEnd(int start, int end, int expected)
		throws Exception {
		BlogsStatsUser blogsStatsUser = addBlogsStatsUser();

		long userId = blogsStatsUser.getUserId();

		Date lastPostDate = blogsStatsUser.getLastPostDate();

		List<BlogsStatsUser> blogsStatsUsers = _persistence.findByU_L(userId,
				lastPostDate, start, end);

		Assert.assertEquals(expected, blogsStatsUsers.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("BlogsStatsUser",
			"statsUserId", true, "groupId", true, "companyId", true, "userId",
			true, "entryCount", true, "lastPostDate", true,
			"ratingsTotalEntries", true, "ratingsTotalScore", true,
			"ratingsAverageScore", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		BlogsStatsUser newBlogsStatsUser = addBlogsStatsUser();

		BlogsStatsUser existingBlogsStatsUser = _persistence.fetchByPrimaryKey(newBlogsStatsUser.getPrimaryKey());

		Assert.assertEquals(existingBlogsStatsUser, newBlogsStatsUser);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		BlogsStatsUser missingBlogsStatsUser = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingBlogsStatsUser);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new BlogsStatsUserActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					BlogsStatsUser blogsStatsUser = (BlogsStatsUser)object;

					Assert.assertNotNull(blogsStatsUser);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		BlogsStatsUser newBlogsStatsUser = addBlogsStatsUser();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(BlogsStatsUser.class,
				BlogsStatsUser.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("statsUserId",
				newBlogsStatsUser.getStatsUserId()));

		List<BlogsStatsUser> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		BlogsStatsUser existingBlogsStatsUser = result.get(0);

		Assert.assertEquals(existingBlogsStatsUser, newBlogsStatsUser);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(BlogsStatsUser.class,
				BlogsStatsUser.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("statsUserId",
				ServiceTestUtil.nextLong()));

		List<BlogsStatsUser> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		BlogsStatsUser newBlogsStatsUser = addBlogsStatsUser();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(BlogsStatsUser.class,
				BlogsStatsUser.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("statsUserId"));

		Object newStatsUserId = newBlogsStatsUser.getStatsUserId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("statsUserId",
				new Object[] { newStatsUserId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingStatsUserId = result.get(0);

		Assert.assertEquals(existingStatsUserId, newStatsUserId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(BlogsStatsUser.class,
				BlogsStatsUser.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("statsUserId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("statsUserId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		BlogsStatsUser newBlogsStatsUser = addBlogsStatsUser();

		_persistence.clearCache();

		BlogsStatsUserModelImpl existingBlogsStatsUserModelImpl = (BlogsStatsUserModelImpl)_persistence.findByPrimaryKey(newBlogsStatsUser.getPrimaryKey());

		Assert.assertEquals(existingBlogsStatsUserModelImpl.getGroupId(),
			existingBlogsStatsUserModelImpl.getOriginalGroupId());
		Assert.assertEquals(existingBlogsStatsUserModelImpl.getUserId(),
			existingBlogsStatsUserModelImpl.getOriginalUserId());
	}

	protected BlogsStatsUser addBlogsStatsUser() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		BlogsStatsUser blogsStatsUser = _persistence.create(pk);

		blogsStatsUser.setGroupId(ServiceTestUtil.nextLong());

		blogsStatsUser.setCompanyId(ServiceTestUtil.nextLong());

		blogsStatsUser.setUserId(ServiceTestUtil.nextLong());

		blogsStatsUser.setEntryCount(ServiceTestUtil.nextInt());

		blogsStatsUser.setLastPostDate(ServiceTestUtil.nextDate());

		blogsStatsUser.setRatingsTotalEntries(ServiceTestUtil.nextInt());

		blogsStatsUser.setRatingsTotalScore(ServiceTestUtil.nextDouble());

		blogsStatsUser.setRatingsAverageScore(ServiceTestUtil.nextDouble());

		_persistence.update(blogsStatsUser);

		return blogsStatsUser;
	}

	private static Log _log = LogFactoryUtil.getLog(BlogsStatsUserPersistenceTest.class);
	private BlogsStatsUserPersistence _persistence = (BlogsStatsUserPersistence)PortalBeanLocatorUtil.locate(BlogsStatsUserPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}