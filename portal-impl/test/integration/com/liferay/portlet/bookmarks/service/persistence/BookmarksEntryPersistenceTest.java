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

package com.liferay.portlet.bookmarks.service.persistence;

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

import com.liferay.portlet.bookmarks.NoSuchEntryException;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.impl.BookmarksEntryModelImpl;

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
public class BookmarksEntryPersistenceTest {
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

		BookmarksEntry bookmarksEntry = _persistence.create(pk);

		Assert.assertNotNull(bookmarksEntry);

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		BookmarksEntry newBookmarksEntry = addBookmarksEntry();

		_persistence.remove(newBookmarksEntry);

		BookmarksEntry existingBookmarksEntry = _persistence.fetchByPrimaryKey(newBookmarksEntry.getPrimaryKey());

		Assert.assertNull(existingBookmarksEntry);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addBookmarksEntry();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		BookmarksEntry newBookmarksEntry = _persistence.create(pk);

		newBookmarksEntry.setUuid(ServiceTestUtil.randomString());

		newBookmarksEntry.setGroupId(ServiceTestUtil.nextLong());

		newBookmarksEntry.setCompanyId(ServiceTestUtil.nextLong());

		newBookmarksEntry.setUserId(ServiceTestUtil.nextLong());

		newBookmarksEntry.setUserName(ServiceTestUtil.randomString());

		newBookmarksEntry.setCreateDate(ServiceTestUtil.nextDate());

		newBookmarksEntry.setModifiedDate(ServiceTestUtil.nextDate());

		newBookmarksEntry.setResourceBlockId(ServiceTestUtil.nextLong());

		newBookmarksEntry.setFolderId(ServiceTestUtil.nextLong());

		newBookmarksEntry.setTreePath(ServiceTestUtil.randomString());

		newBookmarksEntry.setName(ServiceTestUtil.randomString());

		newBookmarksEntry.setUrl(ServiceTestUtil.randomString());

		newBookmarksEntry.setDescription(ServiceTestUtil.randomString());

		newBookmarksEntry.setVisits(ServiceTestUtil.nextInt());

		newBookmarksEntry.setPriority(ServiceTestUtil.nextInt());

		newBookmarksEntry.setStatus(ServiceTestUtil.nextInt());

		newBookmarksEntry.setStatusByUserId(ServiceTestUtil.nextLong());

		newBookmarksEntry.setStatusByUserName(ServiceTestUtil.randomString());

		newBookmarksEntry.setStatusDate(ServiceTestUtil.nextDate());

		_persistence.update(newBookmarksEntry);

		BookmarksEntry existingBookmarksEntry = _persistence.findByPrimaryKey(newBookmarksEntry.getPrimaryKey());

		Assert.assertEquals(existingBookmarksEntry.getUuid(),
			newBookmarksEntry.getUuid());
		Assert.assertEquals(existingBookmarksEntry.getEntryId(),
			newBookmarksEntry.getEntryId());
		Assert.assertEquals(existingBookmarksEntry.getGroupId(),
			newBookmarksEntry.getGroupId());
		Assert.assertEquals(existingBookmarksEntry.getCompanyId(),
			newBookmarksEntry.getCompanyId());
		Assert.assertEquals(existingBookmarksEntry.getUserId(),
			newBookmarksEntry.getUserId());
		Assert.assertEquals(existingBookmarksEntry.getUserName(),
			newBookmarksEntry.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingBookmarksEntry.getCreateDate()),
			Time.getShortTimestamp(newBookmarksEntry.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingBookmarksEntry.getModifiedDate()),
			Time.getShortTimestamp(newBookmarksEntry.getModifiedDate()));
		Assert.assertEquals(existingBookmarksEntry.getResourceBlockId(),
			newBookmarksEntry.getResourceBlockId());
		Assert.assertEquals(existingBookmarksEntry.getFolderId(),
			newBookmarksEntry.getFolderId());
		Assert.assertEquals(existingBookmarksEntry.getTreePath(),
			newBookmarksEntry.getTreePath());
		Assert.assertEquals(existingBookmarksEntry.getName(),
			newBookmarksEntry.getName());
		Assert.assertEquals(existingBookmarksEntry.getUrl(),
			newBookmarksEntry.getUrl());
		Assert.assertEquals(existingBookmarksEntry.getDescription(),
			newBookmarksEntry.getDescription());
		Assert.assertEquals(existingBookmarksEntry.getVisits(),
			newBookmarksEntry.getVisits());
		Assert.assertEquals(existingBookmarksEntry.getPriority(),
			newBookmarksEntry.getPriority());
		Assert.assertEquals(existingBookmarksEntry.getStatus(),
			newBookmarksEntry.getStatus());
		Assert.assertEquals(existingBookmarksEntry.getStatusByUserId(),
			newBookmarksEntry.getStatusByUserId());
		Assert.assertEquals(existingBookmarksEntry.getStatusByUserName(),
			newBookmarksEntry.getStatusByUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingBookmarksEntry.getStatusDate()),
			Time.getShortTimestamp(newBookmarksEntry.getStatusDate()));
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		BookmarksEntry newBookmarksEntry = addBookmarksEntry();

		BookmarksEntry existingBookmarksEntry = _persistence.findByPrimaryKey(newBookmarksEntry.getPrimaryKey());

		Assert.assertEquals(existingBookmarksEntry, newBookmarksEntry);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchEntryException");
		}
		catch (NoSuchEntryException nsee) {
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
	public void testFindByResourceBlockId() throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long resourceBlockId = bookmarksEntry.getResourceBlockId();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByResourceBlockId(resourceBlockId);

		Assert.assertEquals(1, bookmarksEntries.size());

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(),
			bookmarksEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByResourceBlockIdNotFound() throws Exception {
		addBookmarksEntry();

		long resourceBlockId = ServiceTestUtil.nextLong();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByResourceBlockId(resourceBlockId);

		Assert.assertEquals(0, bookmarksEntries.size());
	}

	@Test
	public void testFindByResourceBlockIdStartEnd() throws Exception {
		testFindByResourceBlockIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByResourceBlockIdStartEndWrongRange()
		throws Exception {
		testFindByResourceBlockIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByResourceBlockIdStartEndZeroZero()
		throws Exception {
		testFindByResourceBlockIdStartEnd(0, 0, 0);
	}

	protected void testFindByResourceBlockIdStartEnd(int start, int end,
		int expected) throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long resourceBlockId = bookmarksEntry.getResourceBlockId();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByResourceBlockId(resourceBlockId,
				start, end);

		Assert.assertEquals(expected, bookmarksEntries.size());
	}

	@Test
	public void testFindByUuid() throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		String uuid = bookmarksEntry.getUuid();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, bookmarksEntries.size());

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(),
			bookmarksEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addBookmarksEntry();

		String uuid = ServiceTestUtil.randomString();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, bookmarksEntries.size());
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
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		String uuid = bookmarksEntry.getUuid();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByUuid(uuid,
				start, end);

		Assert.assertEquals(expected, bookmarksEntries.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		String uuid = bookmarksEntry.getUuid();

		long companyId = bookmarksEntry.getCompanyId();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(1, bookmarksEntries.size());

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(),
			bookmarksEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addBookmarksEntry();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByUuid_C(uuid,
				companyId);

		Assert.assertEquals(0, bookmarksEntries.size());
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
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		String uuid = bookmarksEntry.getUuid();

		long companyId = bookmarksEntry.getCompanyId();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByUuid_C(uuid,
				companyId, start, end);

		Assert.assertEquals(expected, bookmarksEntries.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long companyId = bookmarksEntry.getCompanyId();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, bookmarksEntries.size());

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(),
			bookmarksEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addBookmarksEntry();

		long companyId = ServiceTestUtil.nextLong();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, bookmarksEntries.size());
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
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long companyId = bookmarksEntry.getCompanyId();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByCompanyId(companyId,
				start, end);

		Assert.assertEquals(expected, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_F() throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		long folderId = bookmarksEntry.getFolderId();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_F(groupId,
				folderId);

		Assert.assertEquals(1, bookmarksEntries.size());

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(),
			bookmarksEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_FNotFound() throws Exception {
		addBookmarksEntry();

		long groupId = ServiceTestUtil.nextLong();

		long folderId = ServiceTestUtil.nextLong();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_F(groupId,
				folderId);

		Assert.assertEquals(0, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_FStartEnd() throws Exception {
		testFindByG_FStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_FStartEndWrongRange() throws Exception {
		testFindByG_FStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_FStartEndZeroZero() throws Exception {
		testFindByG_FStartEnd(0, 0, 0);
	}

	protected void testFindByG_FStartEnd(int start, int end, int expected)
		throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		long folderId = bookmarksEntry.getFolderId();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_F(groupId,
				folderId, start, end);

		Assert.assertEquals(expected, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_S() throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_S(groupId,
				status);

		Assert.assertEquals(1, bookmarksEntries.size());

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(),
			bookmarksEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_SNotFound() throws Exception {
		addBookmarksEntry();

		long groupId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_S(groupId,
				status);

		Assert.assertEquals(0, bookmarksEntries.size());
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
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_S(groupId,
				status, start, end);

		Assert.assertEquals(expected, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_NotS() throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_NotS(groupId,
				status);

		Assert.assertEquals(1, bookmarksEntries.size());

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(),
			bookmarksEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_NotSNotFound() throws Exception {
		addBookmarksEntry();

		long groupId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_NotS(groupId,
				status);

		Assert.assertEquals(0, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_NotSStartEnd() throws Exception {
		testFindByG_NotSStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_NotSStartEndWrongRange() throws Exception {
		testFindByG_NotSStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_NotSStartEndZeroZero() throws Exception {
		testFindByG_NotSStartEnd(0, 0, 0);
	}

	protected void testFindByG_NotSStartEnd(int start, int end, int expected)
		throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_NotS(groupId,
				status, start, end);

		Assert.assertEquals(expected, bookmarksEntries.size());
	}

	@Test
	public void testFindByC_NotS() throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long companyId = bookmarksEntry.getCompanyId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByC_NotS(companyId,
				status);

		Assert.assertEquals(1, bookmarksEntries.size());

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(),
			bookmarksEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_NotSNotFound() throws Exception {
		addBookmarksEntry();

		long companyId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByC_NotS(companyId,
				status);

		Assert.assertEquals(0, bookmarksEntries.size());
	}

	@Test
	public void testFindByC_NotSStartEnd() throws Exception {
		testFindByC_NotSStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_NotSStartEndWrongRange() throws Exception {
		testFindByC_NotSStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_NotSStartEndZeroZero() throws Exception {
		testFindByC_NotSStartEnd(0, 0, 0);
	}

	protected void testFindByC_NotSStartEnd(int start, int end, int expected)
		throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long companyId = bookmarksEntry.getCompanyId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByC_NotS(companyId,
				status, start, end);

		Assert.assertEquals(expected, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_U_S() throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		long userId = bookmarksEntry.getUserId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_U_S(groupId,
				userId, status);

		Assert.assertEquals(1, bookmarksEntries.size());

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(),
			bookmarksEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_U_SNotFound() throws Exception {
		addBookmarksEntry();

		long groupId = ServiceTestUtil.nextLong();

		long userId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_U_S(groupId,
				userId, status);

		Assert.assertEquals(0, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_U_SStartEnd() throws Exception {
		testFindByG_U_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_U_SStartEndWrongRange() throws Exception {
		testFindByG_U_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_U_SStartEndZeroZero() throws Exception {
		testFindByG_U_SStartEnd(0, 0, 0);
	}

	protected void testFindByG_U_SStartEnd(int start, int end, int expected)
		throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		long userId = bookmarksEntry.getUserId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_U_S(groupId,
				userId, status, start, end);

		Assert.assertEquals(expected, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_U_NotS() throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		long userId = bookmarksEntry.getUserId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_U_NotS(groupId,
				userId, status);

		Assert.assertEquals(1, bookmarksEntries.size());

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(),
			bookmarksEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_U_NotSNotFound() throws Exception {
		addBookmarksEntry();

		long groupId = ServiceTestUtil.nextLong();

		long userId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_U_NotS(groupId,
				userId, status);

		Assert.assertEquals(0, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_U_NotSStartEnd() throws Exception {
		testFindByG_U_NotSStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_U_NotSStartEndWrongRange()
		throws Exception {
		testFindByG_U_NotSStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_U_NotSStartEndZeroZero() throws Exception {
		testFindByG_U_NotSStartEnd(0, 0, 0);
	}

	protected void testFindByG_U_NotSStartEnd(int start, int end, int expected)
		throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		long userId = bookmarksEntry.getUserId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_U_NotS(groupId,
				userId, status, start, end);

		Assert.assertEquals(expected, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_F_S() throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		long folderId = bookmarksEntry.getFolderId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_F_S(groupId,
				folderId, status);

		Assert.assertEquals(1, bookmarksEntries.size());

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(),
			bookmarksEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_F_SNotFound() throws Exception {
		addBookmarksEntry();

		long groupId = ServiceTestUtil.nextLong();

		long folderId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_F_S(groupId,
				folderId, status);

		Assert.assertEquals(0, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_F_SStartEnd() throws Exception {
		testFindByG_F_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_F_SStartEndWrongRange() throws Exception {
		testFindByG_F_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_F_SStartEndZeroZero() throws Exception {
		testFindByG_F_SStartEnd(0, 0, 0);
	}

	protected void testFindByG_F_SStartEnd(int start, int end, int expected)
		throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		long folderId = bookmarksEntry.getFolderId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_F_S(groupId,
				folderId, status, start, end);

		Assert.assertEquals(expected, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_F_NotS() throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		long folderId = bookmarksEntry.getFolderId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_F_NotS(groupId,
				folderId, status);

		Assert.assertEquals(1, bookmarksEntries.size());

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(),
			bookmarksEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_F_NotSNotFound() throws Exception {
		addBookmarksEntry();

		long groupId = ServiceTestUtil.nextLong();

		long folderId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_F_NotS(groupId,
				folderId, status);

		Assert.assertEquals(0, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_F_NotSStartEnd() throws Exception {
		testFindByG_F_NotSStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_F_NotSStartEndWrongRange()
		throws Exception {
		testFindByG_F_NotSStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_F_NotSStartEndZeroZero() throws Exception {
		testFindByG_F_NotSStartEnd(0, 0, 0);
	}

	protected void testFindByG_F_NotSStartEnd(int start, int end, int expected)
		throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		long folderId = bookmarksEntry.getFolderId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_F_NotS(groupId,
				folderId, status, start, end);

		Assert.assertEquals(expected, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_U_F_S() throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		long userId = bookmarksEntry.getUserId();

		long folderId = bookmarksEntry.getFolderId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_U_F_S(groupId,
				userId, folderId, status);

		Assert.assertEquals(1, bookmarksEntries.size());

		Assert.assertEquals(bookmarksEntry.getPrimaryKey(),
			bookmarksEntries.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_U_F_SNotFound() throws Exception {
		addBookmarksEntry();

		long groupId = ServiceTestUtil.nextLong();

		long userId = ServiceTestUtil.nextLong();

		long folderId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_U_F_S(groupId,
				userId, folderId, status);

		Assert.assertEquals(0, bookmarksEntries.size());
	}

	@Test
	public void testFindByG_U_F_SStartEnd() throws Exception {
		testFindByG_U_F_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_U_F_SStartEndWrongRange() throws Exception {
		testFindByG_U_F_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_U_F_SStartEndZeroZero() throws Exception {
		testFindByG_U_F_SStartEnd(0, 0, 0);
	}

	protected void testFindByG_U_F_SStartEnd(int start, int end, int expected)
		throws Exception {
		BookmarksEntry bookmarksEntry = addBookmarksEntry();

		long groupId = bookmarksEntry.getGroupId();

		long userId = bookmarksEntry.getUserId();

		long folderId = bookmarksEntry.getFolderId();

		int status = bookmarksEntry.getStatus();

		List<BookmarksEntry> bookmarksEntries = _persistence.findByG_U_F_S(groupId,
				userId, folderId, status, start, end);

		Assert.assertEquals(expected, bookmarksEntries.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("BookmarksEntry", "uuid",
			true, "entryId", true, "groupId", true, "companyId", true,
			"userId", true, "userName", true, "createDate", true,
			"modifiedDate", true, "resourceBlockId", true, "folderId", true,
			"treePath", true, "name", true, "url", true, "description", true,
			"visits", true, "priority", true, "status", true, "statusByUserId",
			true, "statusByUserName", true, "statusDate", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		BookmarksEntry newBookmarksEntry = addBookmarksEntry();

		BookmarksEntry existingBookmarksEntry = _persistence.fetchByPrimaryKey(newBookmarksEntry.getPrimaryKey());

		Assert.assertEquals(existingBookmarksEntry, newBookmarksEntry);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		BookmarksEntry missingBookmarksEntry = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingBookmarksEntry);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new BookmarksEntryActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					BookmarksEntry bookmarksEntry = (BookmarksEntry)object;

					Assert.assertNotNull(bookmarksEntry);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		BookmarksEntry newBookmarksEntry = addBookmarksEntry();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(BookmarksEntry.class,
				BookmarksEntry.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("entryId",
				newBookmarksEntry.getEntryId()));

		List<BookmarksEntry> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		BookmarksEntry existingBookmarksEntry = result.get(0);

		Assert.assertEquals(existingBookmarksEntry, newBookmarksEntry);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(BookmarksEntry.class,
				BookmarksEntry.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("entryId",
				ServiceTestUtil.nextLong()));

		List<BookmarksEntry> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		BookmarksEntry newBookmarksEntry = addBookmarksEntry();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(BookmarksEntry.class,
				BookmarksEntry.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("entryId"));

		Object newEntryId = newBookmarksEntry.getEntryId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("entryId",
				new Object[] { newEntryId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingEntryId = result.get(0);

		Assert.assertEquals(existingEntryId, newEntryId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(BookmarksEntry.class,
				BookmarksEntry.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("entryId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("entryId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		BookmarksEntry newBookmarksEntry = addBookmarksEntry();

		_persistence.clearCache();

		BookmarksEntryModelImpl existingBookmarksEntryModelImpl = (BookmarksEntryModelImpl)_persistence.findByPrimaryKey(newBookmarksEntry.getPrimaryKey());

		Assert.assertTrue(Validator.equals(
				existingBookmarksEntryModelImpl.getUuid(),
				existingBookmarksEntryModelImpl.getOriginalUuid()));
		Assert.assertEquals(existingBookmarksEntryModelImpl.getGroupId(),
			existingBookmarksEntryModelImpl.getOriginalGroupId());
	}

	protected BookmarksEntry addBookmarksEntry() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		BookmarksEntry bookmarksEntry = _persistence.create(pk);

		bookmarksEntry.setUuid(ServiceTestUtil.randomString());

		bookmarksEntry.setGroupId(ServiceTestUtil.nextLong());

		bookmarksEntry.setCompanyId(ServiceTestUtil.nextLong());

		bookmarksEntry.setUserId(ServiceTestUtil.nextLong());

		bookmarksEntry.setUserName(ServiceTestUtil.randomString());

		bookmarksEntry.setCreateDate(ServiceTestUtil.nextDate());

		bookmarksEntry.setModifiedDate(ServiceTestUtil.nextDate());

		bookmarksEntry.setResourceBlockId(ServiceTestUtil.nextLong());

		bookmarksEntry.setFolderId(ServiceTestUtil.nextLong());

		bookmarksEntry.setTreePath(ServiceTestUtil.randomString());

		bookmarksEntry.setName(ServiceTestUtil.randomString());

		bookmarksEntry.setUrl(ServiceTestUtil.randomString());

		bookmarksEntry.setDescription(ServiceTestUtil.randomString());

		bookmarksEntry.setVisits(ServiceTestUtil.nextInt());

		bookmarksEntry.setPriority(ServiceTestUtil.nextInt());

		bookmarksEntry.setStatus(ServiceTestUtil.nextInt());

		bookmarksEntry.setStatusByUserId(ServiceTestUtil.nextLong());

		bookmarksEntry.setStatusByUserName(ServiceTestUtil.randomString());

		bookmarksEntry.setStatusDate(ServiceTestUtil.nextDate());

		_persistence.update(bookmarksEntry);

		return bookmarksEntry;
	}

	private static Log _log = LogFactoryUtil.getLog(BookmarksEntryPersistenceTest.class);
	private BookmarksEntryPersistence _persistence = (BookmarksEntryPersistence)PortalBeanLocatorUtil.locate(BookmarksEntryPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}