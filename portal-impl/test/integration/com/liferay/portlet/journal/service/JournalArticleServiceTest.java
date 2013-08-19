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

package com.liferay.portlet.journal.service;

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalFolderConstants;
import com.liferay.portlet.journal.util.JournalTestUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Juan Fernández
 */
@ExecutionTestListeners(
	listeners = {
		EnvironmentExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class JournalArticleServiceTest {

	@Before
	public void setUp() throws Exception {
		FinderCacheUtil.clearCache();

		_group = GroupTestUtil.addGroup();

		_article = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, "Version 1",
			"This is a test article.");
	}

	@After
	public void tearDown() throws Exception {
		JournalArticleLocalServiceUtil.deleteArticle(
			_group.getGroupId(), _article.getArticleId(), new ServiceContext());

		GroupLocalServiceUtil.deleteGroup(_group);
	}

	@Test
	public void testAddArticle() throws Exception {
		Assert.assertTrue(_article.isApproved());
		Assert.assertEquals(1.0, _article.getVersion(), 0);
	}

	@Test
	public void testFetchLatestArticleExpiredWithStatusAny() throws Exception {
		updateArticleAndExpireLastVersion("Version 2");

		fetchLatestArticle(WorkflowConstants.STATUS_ANY, false);

		Assert.assertTrue(_latestArticle.isExpired());
		Assert.assertEquals(
			"Version 2", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertEquals(1.1, _latestArticle.getVersion(), 0);
	}

	@Test
	public void testFetchLatestArticleExpiredWithStatusApproved()
		throws Exception {

		updateArticleAndExpireLastVersion("Version 2");

		fetchLatestArticle(WorkflowConstants.STATUS_APPROVED, true);

		Assert.assertTrue(_latestArticle.isApproved());
		Assert.assertEquals(
			"Version 1", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertEquals(1.0, _latestArticle.getVersion(), 0);
	}

	@Test
	public void testFetchLatestArticleExpiredWithStatusExpired()
		throws Exception {

		updateArticleAndExpireLastVersion("Version 2");

		fetchLatestArticle(WorkflowConstants.STATUS_EXPIRED, false);

		Assert.assertTrue(_latestArticle.isExpired());
		Assert.assertEquals(
			"Version 2", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertEquals(1.1, _latestArticle.getVersion(), 0);
	}

	@Test
	public void testFetchLatestArticleNotExpiredWithStatusAny()
		throws Exception {

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		fetchLatestArticle(WorkflowConstants.STATUS_ANY, false);

		Assert.assertTrue(_latestArticle.isApproved());
		Assert.assertEquals(
			"Version 2", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertEquals(1.1, _latestArticle.getVersion(), 0);
	}

	@Test
	public void testFetchLatestArticleNotExpiredWithStatusApproved()
		throws Exception {

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		fetchLatestArticle(WorkflowConstants.STATUS_APPROVED, true);

		Assert.assertTrue(_latestArticle.isApproved());
		Assert.assertEquals(
			"Version 2", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertEquals(1.1, _latestArticle.getVersion(), 0);
	}

	@Test
	public void testFetchLatestArticleNotExpiredWithStatusExpired()
		throws Exception {

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		fetchLatestArticle(WorkflowConstants.STATUS_EXPIRED, false);

		Assert.assertNull(_latestArticle);
	}

	@Test
	public void testFetchLatestArticleWithApprovedStatusWhenArticleIsExpired()
		throws Exception {

		updateAndExpireArticle();

		fetchLatestArticle(WorkflowConstants.STATUS_APPROVED);

		Assert.assertNull(_latestArticle);
	}

	@Test
	public void testFetchLatestArticleWithApprovedStatusWithApprovedVersion()
		throws Exception {

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		fetchLatestArticle(WorkflowConstants.STATUS_APPROVED);

		Assert.assertTrue(_latestArticle.isApproved());
		Assert.assertTrue(1.1 == _latestArticle.getVersion());
	}

	@Test
	public void testFetchLatestArticleWithDraftStatusWhenNoDraftVersion()
		throws Exception {

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		fetchLatestArticle(WorkflowConstants.STATUS_DRAFT);

		Assert.assertNull(_latestArticle);
	}

	@Test
	public void testUpdateArticle() throws Exception {
		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		Assert.assertTrue(_article.isApproved());
		Assert.assertEquals(1.1, _article.getVersion(), 0);
	}

	protected void fetchLatestArticle(int status) throws Exception {
		_latestArticle = JournalArticleLocalServiceUtil.fetchLatestArticle(
			_group.getGroupId(), _article.getArticleId(), status);
	}

	protected void fetchLatestArticle(int status, boolean preferApproved)
		throws Exception {

		_latestArticle = JournalArticleLocalServiceUtil.fetchLatestArticle(
			_article.getResourcePrimKey(), status, preferApproved);
	}

	protected void updateAndExpireArticle() throws Exception {
		JournalTestUtil.updateArticle(_article, "Version 2");

		JournalArticleLocalServiceUtil.expireArticle(
			_article.getUserId(), _article.getGroupId(),
			_article.getArticleId(), null,
			ServiceTestUtil.getServiceContext(_group.getGroupId())
		);
	}

	protected void updateArticleAndExpireLastVersion(String title)
		throws Exception {

		JournalTestUtil.updateArticle(_article, title);

		_article = JournalArticleLocalServiceUtil.expireArticle(
			_article.getUserId(), _article.getGroupId(),
			_article.getArticleId(), 1.1, null,
			ServiceTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertTrue(_article.isExpired());
		Assert.assertEquals(1.1, _article.getVersion(), 0);
	}

	private JournalArticle _article;
	private Group _group;
	private JournalArticle _latestArticle;

}