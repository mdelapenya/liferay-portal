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

package com.liferay.portlet.wiki.service.persistence;

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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;
import com.liferay.portal.util.PropsValues;

import com.liferay.portlet.wiki.NoSuchPageException;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.model.impl.WikiPageModelImpl;

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
public class WikiPagePersistenceTest {
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

		WikiPage wikiPage = _persistence.create(pk);

		Assert.assertNotNull(wikiPage);

		Assert.assertEquals(wikiPage.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		WikiPage newWikiPage = addWikiPage();

		_persistence.remove(newWikiPage);

		WikiPage existingWikiPage = _persistence.fetchByPrimaryKey(newWikiPage.getPrimaryKey());

		Assert.assertNull(existingWikiPage);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addWikiPage();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		WikiPage newWikiPage = _persistence.create(pk);

		newWikiPage.setUuid(ServiceTestUtil.randomString());

		newWikiPage.setResourcePrimKey(ServiceTestUtil.nextLong());

		newWikiPage.setGroupId(ServiceTestUtil.nextLong());

		newWikiPage.setCompanyId(ServiceTestUtil.nextLong());

		newWikiPage.setUserId(ServiceTestUtil.nextLong());

		newWikiPage.setUserName(ServiceTestUtil.randomString());

		newWikiPage.setCreateDate(ServiceTestUtil.nextDate());

		newWikiPage.setModifiedDate(ServiceTestUtil.nextDate());

		newWikiPage.setNodeId(ServiceTestUtil.nextLong());

		newWikiPage.setTitle(ServiceTestUtil.randomString());

		newWikiPage.setVersion(ServiceTestUtil.nextDouble());

		newWikiPage.setMinorEdit(ServiceTestUtil.randomBoolean());

		newWikiPage.setContent(ServiceTestUtil.randomString());

		newWikiPage.setSummary(ServiceTestUtil.randomString());

		newWikiPage.setFormat(ServiceTestUtil.randomString());

		newWikiPage.setHead(ServiceTestUtil.randomBoolean());

		newWikiPage.setParentTitle(ServiceTestUtil.randomString());

		newWikiPage.setRedirectTitle(ServiceTestUtil.randomString());

		newWikiPage.setStatus(ServiceTestUtil.nextInt());

		newWikiPage.setStatusByUserId(ServiceTestUtil.nextLong());

		newWikiPage.setStatusByUserName(ServiceTestUtil.randomString());

		newWikiPage.setStatusDate(ServiceTestUtil.nextDate());

		_persistence.update(newWikiPage);

		WikiPage existingWikiPage = _persistence.findByPrimaryKey(newWikiPage.getPrimaryKey());

		Assert.assertEquals(existingWikiPage.getUuid(), newWikiPage.getUuid());
		Assert.assertEquals(existingWikiPage.getPageId(),
			newWikiPage.getPageId());
		Assert.assertEquals(existingWikiPage.getResourcePrimKey(),
			newWikiPage.getResourcePrimKey());
		Assert.assertEquals(existingWikiPage.getGroupId(),
			newWikiPage.getGroupId());
		Assert.assertEquals(existingWikiPage.getCompanyId(),
			newWikiPage.getCompanyId());
		Assert.assertEquals(existingWikiPage.getUserId(),
			newWikiPage.getUserId());
		Assert.assertEquals(existingWikiPage.getUserName(),
			newWikiPage.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingWikiPage.getCreateDate()),
			Time.getShortTimestamp(newWikiPage.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingWikiPage.getModifiedDate()),
			Time.getShortTimestamp(newWikiPage.getModifiedDate()));
		Assert.assertEquals(existingWikiPage.getNodeId(),
			newWikiPage.getNodeId());
		Assert.assertEquals(existingWikiPage.getTitle(), newWikiPage.getTitle());
		AssertUtils.assertEquals(existingWikiPage.getVersion(),
			newWikiPage.getVersion());
		Assert.assertEquals(existingWikiPage.getMinorEdit(),
			newWikiPage.getMinorEdit());
		Assert.assertEquals(existingWikiPage.getContent(),
			newWikiPage.getContent());
		Assert.assertEquals(existingWikiPage.getSummary(),
			newWikiPage.getSummary());
		Assert.assertEquals(existingWikiPage.getFormat(),
			newWikiPage.getFormat());
		Assert.assertEquals(existingWikiPage.getHead(), newWikiPage.getHead());
		Assert.assertEquals(existingWikiPage.getParentTitle(),
			newWikiPage.getParentTitle());
		Assert.assertEquals(existingWikiPage.getRedirectTitle(),
			newWikiPage.getRedirectTitle());
		Assert.assertEquals(existingWikiPage.getStatus(),
			newWikiPage.getStatus());
		Assert.assertEquals(existingWikiPage.getStatusByUserId(),
			newWikiPage.getStatusByUserId());
		Assert.assertEquals(existingWikiPage.getStatusByUserName(),
			newWikiPage.getStatusByUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingWikiPage.getStatusDate()),
			Time.getShortTimestamp(newWikiPage.getStatusDate()));
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		WikiPage newWikiPage = addWikiPage();

		WikiPage existingWikiPage = _persistence.findByPrimaryKey(newWikiPage.getPrimaryKey());

		Assert.assertEquals(existingWikiPage, newWikiPage);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchPageException");
		}
		catch (NoSuchPageException nsee) {
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
		WikiPage wikiPage = addWikiPage();

		String uuid = wikiPage.getUuid();

		List<WikiPage> wikiPages = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addWikiPage();

		String uuid = ServiceTestUtil.randomString();

		List<WikiPage> wikiPages = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, wikiPages.size());
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
		WikiPage wikiPage = addWikiPage();

		String uuid = wikiPage.getUuid();

		List<WikiPage> wikiPages = _persistence.findByUuid(uuid, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		WikiPage wikiPage = addWikiPage();

		String uuid = wikiPage.getUuid();

		long companyId = wikiPage.getCompanyId();

		List<WikiPage> wikiPages = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addWikiPage();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<WikiPage> wikiPages = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(0, wikiPages.size());
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
		WikiPage wikiPage = addWikiPage();

		String uuid = wikiPage.getUuid();

		long companyId = wikiPage.getCompanyId();

		List<WikiPage> wikiPages = _persistence.findByUuid_C(uuid, companyId,
				start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByResourcePrimKey() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long resourcePrimKey = wikiPage.getResourcePrimKey();

		List<WikiPage> wikiPages = _persistence.findByResourcePrimKey(resourcePrimKey);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByResourcePrimKeyNotFound() throws Exception {
		addWikiPage();

		long resourcePrimKey = ServiceTestUtil.nextLong();

		List<WikiPage> wikiPages = _persistence.findByResourcePrimKey(resourcePrimKey);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByResourcePrimKeyStartEnd() throws Exception {
		testFindByResourcePrimKeyStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByResourcePrimKeyStartEndWrongRange()
		throws Exception {
		testFindByResourcePrimKeyStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByResourcePrimKeyStartEndZeroZero()
		throws Exception {
		testFindByResourcePrimKeyStartEnd(0, 0, 0);
	}

	protected void testFindByResourcePrimKeyStartEnd(int start, int end,
		int expected) throws Exception {
		WikiPage wikiPage = addWikiPage();

		long resourcePrimKey = wikiPage.getResourcePrimKey();

		List<WikiPage> wikiPages = _persistence.findByResourcePrimKey(resourcePrimKey,
				start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByNodeId() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		List<WikiPage> wikiPages = _persistence.findByNodeId(nodeId);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByNodeIdNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		List<WikiPage> wikiPages = _persistence.findByNodeId(nodeId);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByNodeIdStartEnd() throws Exception {
		testFindByNodeIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByNodeIdStartEndWrongRange() throws Exception {
		testFindByNodeIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByNodeIdStartEndZeroZero() throws Exception {
		testFindByNodeIdStartEnd(0, 0, 0);
	}

	protected void testFindByNodeIdStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		List<WikiPage> wikiPages = _persistence.findByNodeId(nodeId, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByFormat() throws Exception {
		WikiPage wikiPage = addWikiPage();

		String format = wikiPage.getFormat();

		List<WikiPage> wikiPages = _persistence.findByFormat(format);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByFormatNotFound() throws Exception {
		addWikiPage();

		String format = ServiceTestUtil.randomString();

		List<WikiPage> wikiPages = _persistence.findByFormat(format);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByFormatStartEnd() throws Exception {
		testFindByFormatStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByFormatStartEndWrongRange() throws Exception {
		testFindByFormatStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByFormatStartEndZeroZero() throws Exception {
		testFindByFormatStartEnd(0, 0, 0);
	}

	protected void testFindByFormatStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		String format = wikiPage.getFormat();

		List<WikiPage> wikiPages = _persistence.findByFormat(format, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByR_N() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long resourcePrimKey = wikiPage.getResourcePrimKey();

		long nodeId = wikiPage.getNodeId();

		List<WikiPage> wikiPages = _persistence.findByR_N(resourcePrimKey,
				nodeId);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByR_NNotFound() throws Exception {
		addWikiPage();

		long resourcePrimKey = ServiceTestUtil.nextLong();

		long nodeId = ServiceTestUtil.nextLong();

		List<WikiPage> wikiPages = _persistence.findByR_N(resourcePrimKey,
				nodeId);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByR_NStartEnd() throws Exception {
		testFindByR_NStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByR_NStartEndWrongRange() throws Exception {
		testFindByR_NStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByR_NStartEndZeroZero() throws Exception {
		testFindByR_NStartEnd(0, 0, 0);
	}

	protected void testFindByR_NStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long resourcePrimKey = wikiPage.getResourcePrimKey();

		long nodeId = wikiPage.getNodeId();

		List<WikiPage> wikiPages = _persistence.findByR_N(resourcePrimKey,
				nodeId, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByR_S() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long resourcePrimKey = wikiPage.getResourcePrimKey();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByR_S(resourcePrimKey,
				status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByR_SNotFound() throws Exception {
		addWikiPage();

		long resourcePrimKey = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByR_S(resourcePrimKey,
				status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByR_SStartEnd() throws Exception {
		testFindByR_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByR_SStartEndWrongRange() throws Exception {
		testFindByR_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByR_SStartEndZeroZero() throws Exception {
		testFindByR_SStartEnd(0, 0, 0);
	}

	protected void testFindByR_SStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long resourcePrimKey = wikiPage.getResourcePrimKey();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByR_S(resourcePrimKey,
				status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_T() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		String title = wikiPage.getTitle();

		List<WikiPage> wikiPages = _persistence.findByN_T(nodeId, title);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_TNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		String title = ServiceTestUtil.randomString();

		List<WikiPage> wikiPages = _persistence.findByN_T(nodeId, title);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_TStartEnd() throws Exception {
		testFindByN_TStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_TStartEndWrongRange() throws Exception {
		testFindByN_TStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_TStartEndZeroZero() throws Exception {
		testFindByN_TStartEnd(0, 0, 0);
	}

	protected void testFindByN_TStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		String title = wikiPage.getTitle();

		List<WikiPage> wikiPages = _persistence.findByN_T(nodeId, title, start,
				end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_H() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		List<WikiPage> wikiPages = _persistence.findByN_H(nodeId, head);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_HNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		boolean head = ServiceTestUtil.randomBoolean();

		List<WikiPage> wikiPages = _persistence.findByN_H(nodeId, head);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_HStartEnd() throws Exception {
		testFindByN_HStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_HStartEndWrongRange() throws Exception {
		testFindByN_HStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_HStartEndZeroZero() throws Exception {
		testFindByN_HStartEnd(0, 0, 0);
	}

	protected void testFindByN_HStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		List<WikiPage> wikiPages = _persistence.findByN_H(nodeId, head, start,
				end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_P() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		String parentTitle = wikiPage.getParentTitle();

		List<WikiPage> wikiPages = _persistence.findByN_P(nodeId, parentTitle);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_PNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		String parentTitle = ServiceTestUtil.randomString();

		List<WikiPage> wikiPages = _persistence.findByN_P(nodeId, parentTitle);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_PStartEnd() throws Exception {
		testFindByN_PStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_PStartEndWrongRange() throws Exception {
		testFindByN_PStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_PStartEndZeroZero() throws Exception {
		testFindByN_PStartEnd(0, 0, 0);
	}

	protected void testFindByN_PStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		String parentTitle = wikiPage.getParentTitle();

		List<WikiPage> wikiPages = _persistence.findByN_P(nodeId, parentTitle,
				start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_R() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		String redirectTitle = wikiPage.getRedirectTitle();

		List<WikiPage> wikiPages = _persistence.findByN_R(nodeId, redirectTitle);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_RNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		String redirectTitle = ServiceTestUtil.randomString();

		List<WikiPage> wikiPages = _persistence.findByN_R(nodeId, redirectTitle);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_RStartEnd() throws Exception {
		testFindByN_RStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_RStartEndWrongRange() throws Exception {
		testFindByN_RStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_RStartEndZeroZero() throws Exception {
		testFindByN_RStartEnd(0, 0, 0);
	}

	protected void testFindByN_RStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		String redirectTitle = wikiPage.getRedirectTitle();

		List<WikiPage> wikiPages = _persistence.findByN_R(nodeId,
				redirectTitle, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_S() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_S(nodeId, status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_SNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByN_S(nodeId, status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_SStartEnd() throws Exception {
		testFindByN_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_SStartEndWrongRange() throws Exception {
		testFindByN_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_SStartEndZeroZero() throws Exception {
		testFindByN_SStartEnd(0, 0, 0);
	}

	protected void testFindByN_SStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_S(nodeId, status,
				start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByR_N_H() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long resourcePrimKey = wikiPage.getResourcePrimKey();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		List<WikiPage> wikiPages = _persistence.findByR_N_H(resourcePrimKey,
				nodeId, head);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByR_N_HNotFound() throws Exception {
		addWikiPage();

		long resourcePrimKey = ServiceTestUtil.nextLong();

		long nodeId = ServiceTestUtil.nextLong();

		boolean head = ServiceTestUtil.randomBoolean();

		List<WikiPage> wikiPages = _persistence.findByR_N_H(resourcePrimKey,
				nodeId, head);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByR_N_HStartEnd() throws Exception {
		testFindByR_N_HStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByR_N_HStartEndWrongRange() throws Exception {
		testFindByR_N_HStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByR_N_HStartEndZeroZero() throws Exception {
		testFindByR_N_HStartEnd(0, 0, 0);
	}

	protected void testFindByR_N_HStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long resourcePrimKey = wikiPage.getResourcePrimKey();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		List<WikiPage> wikiPages = _persistence.findByR_N_H(resourcePrimKey,
				nodeId, head, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByR_N_S() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long resourcePrimKey = wikiPage.getResourcePrimKey();

		long nodeId = wikiPage.getNodeId();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByR_N_S(resourcePrimKey,
				nodeId, status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByR_N_SNotFound() throws Exception {
		addWikiPage();

		long resourcePrimKey = ServiceTestUtil.nextLong();

		long nodeId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByR_N_S(resourcePrimKey,
				nodeId, status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByR_N_SStartEnd() throws Exception {
		testFindByR_N_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByR_N_SStartEndWrongRange() throws Exception {
		testFindByR_N_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByR_N_SStartEndZeroZero() throws Exception {
		testFindByR_N_SStartEnd(0, 0, 0);
	}

	protected void testFindByR_N_SStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long resourcePrimKey = wikiPage.getResourcePrimKey();

		long nodeId = wikiPage.getNodeId();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByR_N_S(resourcePrimKey,
				nodeId, status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByG_N_H() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long groupId = wikiPage.getGroupId();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		List<WikiPage> wikiPages = _persistence.findByG_N_H(groupId, nodeId,
				head);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_N_HNotFound() throws Exception {
		addWikiPage();

		long groupId = ServiceTestUtil.nextLong();

		long nodeId = ServiceTestUtil.nextLong();

		boolean head = ServiceTestUtil.randomBoolean();

		List<WikiPage> wikiPages = _persistence.findByG_N_H(groupId, nodeId,
				head);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByG_N_HStartEnd() throws Exception {
		testFindByG_N_HStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_N_HStartEndWrongRange() throws Exception {
		testFindByG_N_HStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_N_HStartEndZeroZero() throws Exception {
		testFindByG_N_HStartEnd(0, 0, 0);
	}

	protected void testFindByG_N_HStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long groupId = wikiPage.getGroupId();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		List<WikiPage> wikiPages = _persistence.findByG_N_H(groupId, nodeId,
				head, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByG_N_S() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long groupId = wikiPage.getGroupId();

		long nodeId = wikiPage.getNodeId();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByG_N_S(groupId, nodeId,
				status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_N_SNotFound() throws Exception {
		addWikiPage();

		long groupId = ServiceTestUtil.nextLong();

		long nodeId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByG_N_S(groupId, nodeId,
				status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByG_N_SStartEnd() throws Exception {
		testFindByG_N_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_N_SStartEndWrongRange() throws Exception {
		testFindByG_N_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_N_SStartEndZeroZero() throws Exception {
		testFindByG_N_SStartEnd(0, 0, 0);
	}

	protected void testFindByG_N_SStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long groupId = wikiPage.getGroupId();

		long nodeId = wikiPage.getNodeId();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByG_N_S(groupId, nodeId,
				status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByU_N_S() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long userId = wikiPage.getUserId();

		long nodeId = wikiPage.getNodeId();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByU_N_S(userId, nodeId,
				status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByU_N_SNotFound() throws Exception {
		addWikiPage();

		long userId = ServiceTestUtil.nextLong();

		long nodeId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByU_N_S(userId, nodeId,
				status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByU_N_SStartEnd() throws Exception {
		testFindByU_N_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByU_N_SStartEndWrongRange() throws Exception {
		testFindByU_N_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByU_N_SStartEndZeroZero() throws Exception {
		testFindByU_N_SStartEnd(0, 0, 0);
	}

	protected void testFindByU_N_SStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long userId = wikiPage.getUserId();

		long nodeId = wikiPage.getNodeId();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByU_N_S(userId, nodeId,
				status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_T_H() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		String title = wikiPage.getTitle();

		boolean head = wikiPage.getHead();

		List<WikiPage> wikiPages = _persistence.findByN_T_H(nodeId, title, head);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_T_HNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		String title = ServiceTestUtil.randomString();

		boolean head = ServiceTestUtil.randomBoolean();

		List<WikiPage> wikiPages = _persistence.findByN_T_H(nodeId, title, head);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_T_HStartEnd() throws Exception {
		testFindByN_T_HStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_T_HStartEndWrongRange() throws Exception {
		testFindByN_T_HStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_T_HStartEndZeroZero() throws Exception {
		testFindByN_T_HStartEnd(0, 0, 0);
	}

	protected void testFindByN_T_HStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		String title = wikiPage.getTitle();

		boolean head = wikiPage.getHead();

		List<WikiPage> wikiPages = _persistence.findByN_T_H(nodeId, title,
				head, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_T_S() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		String title = wikiPage.getTitle();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_T_S(nodeId, title,
				status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_T_SNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		String title = ServiceTestUtil.randomString();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByN_T_S(nodeId, title,
				status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_T_SStartEnd() throws Exception {
		testFindByN_T_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_T_SStartEndWrongRange() throws Exception {
		testFindByN_T_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_T_SStartEndZeroZero() throws Exception {
		testFindByN_T_SStartEnd(0, 0, 0);
	}

	protected void testFindByN_T_SStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		String title = wikiPage.getTitle();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_T_S(nodeId, title,
				status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_H_P() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		String parentTitle = wikiPage.getParentTitle();

		List<WikiPage> wikiPages = _persistence.findByN_H_P(nodeId, head,
				parentTitle);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_H_PNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		boolean head = ServiceTestUtil.randomBoolean();

		String parentTitle = ServiceTestUtil.randomString();

		List<WikiPage> wikiPages = _persistence.findByN_H_P(nodeId, head,
				parentTitle);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_H_PStartEnd() throws Exception {
		testFindByN_H_PStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_H_PStartEndWrongRange() throws Exception {
		testFindByN_H_PStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_H_PStartEndZeroZero() throws Exception {
		testFindByN_H_PStartEnd(0, 0, 0);
	}

	protected void testFindByN_H_PStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		String parentTitle = wikiPage.getParentTitle();

		List<WikiPage> wikiPages = _persistence.findByN_H_P(nodeId, head,
				parentTitle, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_H_S() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_H_S(nodeId, head, status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_H_SNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		boolean head = ServiceTestUtil.randomBoolean();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByN_H_S(nodeId, head, status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_H_SStartEnd() throws Exception {
		testFindByN_H_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_H_SStartEndWrongRange() throws Exception {
		testFindByN_H_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_H_SStartEndZeroZero() throws Exception {
		testFindByN_H_SStartEnd(0, 0, 0);
	}

	protected void testFindByN_H_SStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_H_S(nodeId, head,
				status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_H_NotS() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_H_NotS(nodeId, head,
				status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_H_NotSNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		boolean head = ServiceTestUtil.randomBoolean();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByN_H_NotS(nodeId, head,
				status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_H_NotSStartEnd() throws Exception {
		testFindByN_H_NotSStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_H_NotSStartEndWrongRange()
		throws Exception {
		testFindByN_H_NotSStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_H_NotSStartEndZeroZero() throws Exception {
		testFindByN_H_NotSStartEnd(0, 0, 0);
	}

	protected void testFindByN_H_NotSStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_H_NotS(nodeId, head,
				status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByG_U_N_S() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long groupId = wikiPage.getGroupId();

		long userId = wikiPage.getUserId();

		long nodeId = wikiPage.getNodeId();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByG_U_N_S(groupId, userId,
				nodeId, status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_U_N_SNotFound() throws Exception {
		addWikiPage();

		long groupId = ServiceTestUtil.nextLong();

		long userId = ServiceTestUtil.nextLong();

		long nodeId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByG_U_N_S(groupId, userId,
				nodeId, status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByG_U_N_SStartEnd() throws Exception {
		testFindByG_U_N_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_U_N_SStartEndWrongRange() throws Exception {
		testFindByG_U_N_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_U_N_SStartEndZeroZero() throws Exception {
		testFindByG_U_N_SStartEnd(0, 0, 0);
	}

	protected void testFindByG_U_N_SStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long groupId = wikiPage.getGroupId();

		long userId = wikiPage.getUserId();

		long nodeId = wikiPage.getNodeId();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByG_U_N_S(groupId, userId,
				nodeId, status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByG_N_T_H() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long groupId = wikiPage.getGroupId();

		long nodeId = wikiPage.getNodeId();

		String title = wikiPage.getTitle();

		boolean head = wikiPage.getHead();

		List<WikiPage> wikiPages = _persistence.findByG_N_T_H(groupId, nodeId,
				title, head);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_N_T_HNotFound() throws Exception {
		addWikiPage();

		long groupId = ServiceTestUtil.nextLong();

		long nodeId = ServiceTestUtil.nextLong();

		String title = ServiceTestUtil.randomString();

		boolean head = ServiceTestUtil.randomBoolean();

		List<WikiPage> wikiPages = _persistence.findByG_N_T_H(groupId, nodeId,
				title, head);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByG_N_T_HStartEnd() throws Exception {
		testFindByG_N_T_HStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_N_T_HStartEndWrongRange() throws Exception {
		testFindByG_N_T_HStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_N_T_HStartEndZeroZero() throws Exception {
		testFindByG_N_T_HStartEnd(0, 0, 0);
	}

	protected void testFindByG_N_T_HStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long groupId = wikiPage.getGroupId();

		long nodeId = wikiPage.getNodeId();

		String title = wikiPage.getTitle();

		boolean head = wikiPage.getHead();

		List<WikiPage> wikiPages = _persistence.findByG_N_T_H(groupId, nodeId,
				title, head, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByG_N_H_S() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long groupId = wikiPage.getGroupId();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByG_N_H_S(groupId, nodeId,
				head, status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_N_H_SNotFound() throws Exception {
		addWikiPage();

		long groupId = ServiceTestUtil.nextLong();

		long nodeId = ServiceTestUtil.nextLong();

		boolean head = ServiceTestUtil.randomBoolean();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByG_N_H_S(groupId, nodeId,
				head, status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByG_N_H_SStartEnd() throws Exception {
		testFindByG_N_H_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_N_H_SStartEndWrongRange() throws Exception {
		testFindByG_N_H_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_N_H_SStartEndZeroZero() throws Exception {
		testFindByG_N_H_SStartEnd(0, 0, 0);
	}

	protected void testFindByG_N_H_SStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long groupId = wikiPage.getGroupId();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByG_N_H_S(groupId, nodeId,
				head, status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_H_P_S() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		String parentTitle = wikiPage.getParentTitle();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_H_P_S(nodeId, head,
				parentTitle, status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_H_P_SNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		boolean head = ServiceTestUtil.randomBoolean();

		String parentTitle = ServiceTestUtil.randomString();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByN_H_P_S(nodeId, head,
				parentTitle, status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_H_P_SStartEnd() throws Exception {
		testFindByN_H_P_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_H_P_SStartEndWrongRange() throws Exception {
		testFindByN_H_P_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_H_P_SStartEndZeroZero() throws Exception {
		testFindByN_H_P_SStartEnd(0, 0, 0);
	}

	protected void testFindByN_H_P_SStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		String parentTitle = wikiPage.getParentTitle();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_H_P_S(nodeId, head,
				parentTitle, status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_H_P_NotS() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		String parentTitle = wikiPage.getParentTitle();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_H_P_NotS(nodeId, head,
				parentTitle, status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_H_P_NotSNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		boolean head = ServiceTestUtil.randomBoolean();

		String parentTitle = ServiceTestUtil.randomString();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByN_H_P_NotS(nodeId, head,
				parentTitle, status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_H_P_NotSStartEnd() throws Exception {
		testFindByN_H_P_NotSStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_H_P_NotSStartEndWrongRange()
		throws Exception {
		testFindByN_H_P_NotSStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_H_P_NotSStartEndZeroZero()
		throws Exception {
		testFindByN_H_P_NotSStartEnd(0, 0, 0);
	}

	protected void testFindByN_H_P_NotSStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		String parentTitle = wikiPage.getParentTitle();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_H_P_NotS(nodeId, head,
				parentTitle, status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_H_R_S() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		String redirectTitle = wikiPage.getRedirectTitle();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_H_R_S(nodeId, head,
				redirectTitle, status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_H_R_SNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		boolean head = ServiceTestUtil.randomBoolean();

		String redirectTitle = ServiceTestUtil.randomString();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByN_H_R_S(nodeId, head,
				redirectTitle, status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_H_R_SStartEnd() throws Exception {
		testFindByN_H_R_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_H_R_SStartEndWrongRange() throws Exception {
		testFindByN_H_R_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_H_R_SStartEndZeroZero() throws Exception {
		testFindByN_H_R_SStartEnd(0, 0, 0);
	}

	protected void testFindByN_H_R_SStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		String redirectTitle = wikiPage.getRedirectTitle();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_H_R_S(nodeId, head,
				redirectTitle, status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByN_H_R_NotS() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		String redirectTitle = wikiPage.getRedirectTitle();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_H_R_NotS(nodeId, head,
				redirectTitle, status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByN_H_R_NotSNotFound() throws Exception {
		addWikiPage();

		long nodeId = ServiceTestUtil.nextLong();

		boolean head = ServiceTestUtil.randomBoolean();

		String redirectTitle = ServiceTestUtil.randomString();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByN_H_R_NotS(nodeId, head,
				redirectTitle, status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByN_H_R_NotSStartEnd() throws Exception {
		testFindByN_H_R_NotSStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByN_H_R_NotSStartEndWrongRange()
		throws Exception {
		testFindByN_H_R_NotSStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByN_H_R_NotSStartEndZeroZero()
		throws Exception {
		testFindByN_H_R_NotSStartEnd(0, 0, 0);
	}

	protected void testFindByN_H_R_NotSStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		String redirectTitle = wikiPage.getRedirectTitle();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByN_H_R_NotS(nodeId, head,
				redirectTitle, status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	@Test
	public void testFindByG_N_H_P_S() throws Exception {
		WikiPage wikiPage = addWikiPage();

		long groupId = wikiPage.getGroupId();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		String parentTitle = wikiPage.getParentTitle();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByG_N_H_P_S(groupId,
				nodeId, head, parentTitle, status);

		Assert.assertEquals(1, wikiPages.size());

		Assert.assertEquals(wikiPage.getPrimaryKey(),
			wikiPages.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByG_N_H_P_SNotFound() throws Exception {
		addWikiPage();

		long groupId = ServiceTestUtil.nextLong();

		long nodeId = ServiceTestUtil.nextLong();

		boolean head = ServiceTestUtil.randomBoolean();

		String parentTitle = ServiceTestUtil.randomString();

		int status = ServiceTestUtil.nextInt();

		List<WikiPage> wikiPages = _persistence.findByG_N_H_P_S(groupId,
				nodeId, head, parentTitle, status);

		Assert.assertEquals(0, wikiPages.size());
	}

	@Test
	public void testFindByG_N_H_P_SStartEnd() throws Exception {
		testFindByG_N_H_P_SStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByG_N_H_P_SStartEndWrongRange()
		throws Exception {
		testFindByG_N_H_P_SStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByG_N_H_P_SStartEndZeroZero() throws Exception {
		testFindByG_N_H_P_SStartEnd(0, 0, 0);
	}

	protected void testFindByG_N_H_P_SStartEnd(int start, int end, int expected)
		throws Exception {
		WikiPage wikiPage = addWikiPage();

		long groupId = wikiPage.getGroupId();

		long nodeId = wikiPage.getNodeId();

		boolean head = wikiPage.getHead();

		String parentTitle = wikiPage.getParentTitle();

		int status = wikiPage.getStatus();

		List<WikiPage> wikiPages = _persistence.findByG_N_H_P_S(groupId,
				nodeId, head, parentTitle, status, start, end);

		Assert.assertEquals(expected, wikiPages.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("WikiPage", "uuid", true,
			"pageId", true, "resourcePrimKey", true, "groupId", true,
			"companyId", true, "userId", true, "userName", true, "createDate",
			true, "modifiedDate", true, "nodeId", true, "title", true,
			"version", true, "minorEdit", true, "content", true, "summary",
			true, "format", true, "head", true, "parentTitle", true,
			"redirectTitle", true, "status", true, "statusByUserId", true,
			"statusByUserName", true, "statusDate", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		WikiPage newWikiPage = addWikiPage();

		WikiPage existingWikiPage = _persistence.fetchByPrimaryKey(newWikiPage.getPrimaryKey());

		Assert.assertEquals(existingWikiPage, newWikiPage);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		WikiPage missingWikiPage = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingWikiPage);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new WikiPageActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					WikiPage wikiPage = (WikiPage)object;

					Assert.assertNotNull(wikiPage);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		WikiPage newWikiPage = addWikiPage();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(WikiPage.class,
				WikiPage.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("pageId",
				newWikiPage.getPageId()));

		List<WikiPage> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		WikiPage existingWikiPage = result.get(0);

		Assert.assertEquals(existingWikiPage, newWikiPage);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(WikiPage.class,
				WikiPage.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("pageId",
				ServiceTestUtil.nextLong()));

		List<WikiPage> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		WikiPage newWikiPage = addWikiPage();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(WikiPage.class,
				WikiPage.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("pageId"));

		Object newPageId = newWikiPage.getPageId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("pageId",
				new Object[] { newPageId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingPageId = result.get(0);

		Assert.assertEquals(existingPageId, newPageId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(WikiPage.class,
				WikiPage.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("pageId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("pageId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		WikiPage newWikiPage = addWikiPage();

		_persistence.clearCache();

		WikiPageModelImpl existingWikiPageModelImpl = (WikiPageModelImpl)_persistence.findByPrimaryKey(newWikiPage.getPrimaryKey());

		Assert.assertTrue(Validator.equals(
				existingWikiPageModelImpl.getUuid(),
				existingWikiPageModelImpl.getOriginalUuid()));
		Assert.assertEquals(existingWikiPageModelImpl.getGroupId(),
			existingWikiPageModelImpl.getOriginalGroupId());

		Assert.assertEquals(existingWikiPageModelImpl.getResourcePrimKey(),
			existingWikiPageModelImpl.getOriginalResourcePrimKey());
		Assert.assertEquals(existingWikiPageModelImpl.getNodeId(),
			existingWikiPageModelImpl.getOriginalNodeId());
		AssertUtils.assertEquals(existingWikiPageModelImpl.getVersion(),
			existingWikiPageModelImpl.getOriginalVersion());

		Assert.assertEquals(existingWikiPageModelImpl.getNodeId(),
			existingWikiPageModelImpl.getOriginalNodeId());
		Assert.assertTrue(Validator.equals(
				existingWikiPageModelImpl.getTitle(),
				existingWikiPageModelImpl.getOriginalTitle()));
		AssertUtils.assertEquals(existingWikiPageModelImpl.getVersion(),
			existingWikiPageModelImpl.getOriginalVersion());
	}

	protected WikiPage addWikiPage() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		WikiPage wikiPage = _persistence.create(pk);

		wikiPage.setUuid(ServiceTestUtil.randomString());

		wikiPage.setResourcePrimKey(ServiceTestUtil.nextLong());

		wikiPage.setGroupId(ServiceTestUtil.nextLong());

		wikiPage.setCompanyId(ServiceTestUtil.nextLong());

		wikiPage.setUserId(ServiceTestUtil.nextLong());

		wikiPage.setUserName(ServiceTestUtil.randomString());

		wikiPage.setCreateDate(ServiceTestUtil.nextDate());

		wikiPage.setModifiedDate(ServiceTestUtil.nextDate());

		wikiPage.setNodeId(ServiceTestUtil.nextLong());

		wikiPage.setTitle(ServiceTestUtil.randomString());

		wikiPage.setVersion(ServiceTestUtil.nextDouble());

		wikiPage.setMinorEdit(ServiceTestUtil.randomBoolean());

		wikiPage.setContent(ServiceTestUtil.randomString());

		wikiPage.setSummary(ServiceTestUtil.randomString());

		wikiPage.setFormat(ServiceTestUtil.randomString());

		wikiPage.setHead(ServiceTestUtil.randomBoolean());

		wikiPage.setParentTitle(ServiceTestUtil.randomString());

		wikiPage.setRedirectTitle(ServiceTestUtil.randomString());

		wikiPage.setStatus(ServiceTestUtil.nextInt());

		wikiPage.setStatusByUserId(ServiceTestUtil.nextLong());

		wikiPage.setStatusByUserName(ServiceTestUtil.randomString());

		wikiPage.setStatusDate(ServiceTestUtil.nextDate());

		_persistence.update(wikiPage);

		return wikiPage;
	}

	private static Log _log = LogFactoryUtil.getLog(WikiPagePersistenceTest.class);
	private WikiPagePersistence _persistence = (WikiPagePersistence)PortalBeanLocatorUtil.locate(WikiPagePersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}