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

import com.liferay.portal.NoSuchWebsiteException;
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
import com.liferay.portal.model.Website;
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
public class WebsitePersistenceTest {
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

		Website website = _persistence.create(pk);

		Assert.assertNotNull(website);

		Assert.assertEquals(website.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		Website newWebsite = addWebsite();

		_persistence.remove(newWebsite);

		Website existingWebsite = _persistence.fetchByPrimaryKey(newWebsite.getPrimaryKey());

		Assert.assertNull(existingWebsite);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addWebsite();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Website newWebsite = _persistence.create(pk);

		newWebsite.setUuid(ServiceTestUtil.randomString());

		newWebsite.setCompanyId(ServiceTestUtil.nextLong());

		newWebsite.setUserId(ServiceTestUtil.nextLong());

		newWebsite.setUserName(ServiceTestUtil.randomString());

		newWebsite.setCreateDate(ServiceTestUtil.nextDate());

		newWebsite.setModifiedDate(ServiceTestUtil.nextDate());

		newWebsite.setClassNameId(ServiceTestUtil.nextLong());

		newWebsite.setClassPK(ServiceTestUtil.nextLong());

		newWebsite.setUrl(ServiceTestUtil.randomString());

		newWebsite.setTypeId(ServiceTestUtil.nextInt());

		newWebsite.setPrimary(ServiceTestUtil.randomBoolean());

		_persistence.update(newWebsite);

		Website existingWebsite = _persistence.findByPrimaryKey(newWebsite.getPrimaryKey());

		Assert.assertEquals(existingWebsite.getUuid(), newWebsite.getUuid());
		Assert.assertEquals(existingWebsite.getWebsiteId(),
			newWebsite.getWebsiteId());
		Assert.assertEquals(existingWebsite.getCompanyId(),
			newWebsite.getCompanyId());
		Assert.assertEquals(existingWebsite.getUserId(), newWebsite.getUserId());
		Assert.assertEquals(existingWebsite.getUserName(),
			newWebsite.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingWebsite.getCreateDate()),
			Time.getShortTimestamp(newWebsite.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingWebsite.getModifiedDate()),
			Time.getShortTimestamp(newWebsite.getModifiedDate()));
		Assert.assertEquals(existingWebsite.getClassNameId(),
			newWebsite.getClassNameId());
		Assert.assertEquals(existingWebsite.getClassPK(),
			newWebsite.getClassPK());
		Assert.assertEquals(existingWebsite.getUrl(), newWebsite.getUrl());
		Assert.assertEquals(existingWebsite.getTypeId(), newWebsite.getTypeId());
		Assert.assertEquals(existingWebsite.getPrimary(),
			newWebsite.getPrimary());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		Website newWebsite = addWebsite();

		Website existingWebsite = _persistence.findByPrimaryKey(newWebsite.getPrimaryKey());

		Assert.assertEquals(existingWebsite, newWebsite);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchWebsiteException");
		}
		catch (NoSuchWebsiteException nsee) {
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
		Website website = addWebsite();

		String uuid = website.getUuid();

		List<Website> websites = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, websites.size());

		Assert.assertEquals(website.getPrimaryKey(),
			websites.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addWebsite();

		String uuid = ServiceTestUtil.randomString();

		List<Website> websites = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, websites.size());
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
		Website website = addWebsite();

		String uuid = website.getUuid();

		List<Website> websites = _persistence.findByUuid(uuid, start, end);

		Assert.assertEquals(expected, websites.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		Website website = addWebsite();

		String uuid = website.getUuid();

		long companyId = website.getCompanyId();

		List<Website> websites = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(1, websites.size());

		Assert.assertEquals(website.getPrimaryKey(),
			websites.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addWebsite();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<Website> websites = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(0, websites.size());
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
		Website website = addWebsite();

		String uuid = website.getUuid();

		long companyId = website.getCompanyId();

		List<Website> websites = _persistence.findByUuid_C(uuid, companyId,
				start, end);

		Assert.assertEquals(expected, websites.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		Website website = addWebsite();

		long companyId = website.getCompanyId();

		List<Website> websites = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, websites.size());

		Assert.assertEquals(website.getPrimaryKey(),
			websites.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addWebsite();

		long companyId = ServiceTestUtil.nextLong();

		List<Website> websites = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, websites.size());
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
		Website website = addWebsite();

		long companyId = website.getCompanyId();

		List<Website> websites = _persistence.findByCompanyId(companyId, start,
				end);

		Assert.assertEquals(expected, websites.size());
	}

	@Test
	public void testFindByUserId() throws Exception {
		Website website = addWebsite();

		long userId = website.getUserId();

		List<Website> websites = _persistence.findByUserId(userId);

		Assert.assertEquals(1, websites.size());

		Assert.assertEquals(website.getPrimaryKey(),
			websites.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUserIdNotFound() throws Exception {
		addWebsite();

		long userId = ServiceTestUtil.nextLong();

		List<Website> websites = _persistence.findByUserId(userId);

		Assert.assertEquals(0, websites.size());
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
		Website website = addWebsite();

		long userId = website.getUserId();

		List<Website> websites = _persistence.findByUserId(userId, start, end);

		Assert.assertEquals(expected, websites.size());
	}

	@Test
	public void testFindByC_C() throws Exception {
		Website website = addWebsite();

		long companyId = website.getCompanyId();

		long classNameId = website.getClassNameId();

		List<Website> websites = _persistence.findByC_C(companyId, classNameId);

		Assert.assertEquals(1, websites.size());

		Assert.assertEquals(website.getPrimaryKey(),
			websites.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_CNotFound() throws Exception {
		addWebsite();

		long companyId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		List<Website> websites = _persistence.findByC_C(companyId, classNameId);

		Assert.assertEquals(0, websites.size());
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
		Website website = addWebsite();

		long companyId = website.getCompanyId();

		long classNameId = website.getClassNameId();

		List<Website> websites = _persistence.findByC_C(companyId, classNameId,
				start, end);

		Assert.assertEquals(expected, websites.size());
	}

	@Test
	public void testFindByC_C_C() throws Exception {
		Website website = addWebsite();

		long companyId = website.getCompanyId();

		long classNameId = website.getClassNameId();

		long classPK = website.getClassPK();

		List<Website> websites = _persistence.findByC_C_C(companyId,
				classNameId, classPK);

		Assert.assertEquals(1, websites.size());

		Assert.assertEquals(website.getPrimaryKey(),
			websites.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_C_CNotFound() throws Exception {
		addWebsite();

		long companyId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		List<Website> websites = _persistence.findByC_C_C(companyId,
				classNameId, classPK);

		Assert.assertEquals(0, websites.size());
	}

	@Test
	public void testFindByC_C_CStartEnd() throws Exception {
		testFindByC_C_CStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_C_CStartEndWrongRange() throws Exception {
		testFindByC_C_CStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_C_CStartEndZeroZero() throws Exception {
		testFindByC_C_CStartEnd(0, 0, 0);
	}

	protected void testFindByC_C_CStartEnd(int start, int end, int expected)
		throws Exception {
		Website website = addWebsite();

		long companyId = website.getCompanyId();

		long classNameId = website.getClassNameId();

		long classPK = website.getClassPK();

		List<Website> websites = _persistence.findByC_C_C(companyId,
				classNameId, classPK, start, end);

		Assert.assertEquals(expected, websites.size());
	}

	@Test
	public void testFindByC_C_C_P() throws Exception {
		Website website = addWebsite();

		long companyId = website.getCompanyId();

		long classNameId = website.getClassNameId();

		long classPK = website.getClassPK();

		boolean primary = website.getPrimary();

		List<Website> websites = _persistence.findByC_C_C_P(companyId,
				classNameId, classPK, primary);

		Assert.assertEquals(1, websites.size());

		Assert.assertEquals(website.getPrimaryKey(),
			websites.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_C_C_PNotFound() throws Exception {
		addWebsite();

		long companyId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		boolean primary = ServiceTestUtil.randomBoolean();

		List<Website> websites = _persistence.findByC_C_C_P(companyId,
				classNameId, classPK, primary);

		Assert.assertEquals(0, websites.size());
	}

	@Test
	public void testFindByC_C_C_PStartEnd() throws Exception {
		testFindByC_C_C_PStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_C_C_PStartEndWrongRange() throws Exception {
		testFindByC_C_C_PStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_C_C_PStartEndZeroZero() throws Exception {
		testFindByC_C_C_PStartEnd(0, 0, 0);
	}

	protected void testFindByC_C_C_PStartEnd(int start, int end, int expected)
		throws Exception {
		Website website = addWebsite();

		long companyId = website.getCompanyId();

		long classNameId = website.getClassNameId();

		long classPK = website.getClassPK();

		boolean primary = website.getPrimary();

		List<Website> websites = _persistence.findByC_C_C_P(companyId,
				classNameId, classPK, primary, start, end);

		Assert.assertEquals(expected, websites.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("Website", "uuid", true,
			"websiteId", true, "companyId", true, "userId", true, "userName",
			true, "createDate", true, "modifiedDate", true, "classNameId",
			true, "classPK", true, "url", true, "typeId", true, "primary", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		Website newWebsite = addWebsite();

		Website existingWebsite = _persistence.fetchByPrimaryKey(newWebsite.getPrimaryKey());

		Assert.assertEquals(existingWebsite, newWebsite);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Website missingWebsite = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingWebsite);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new WebsiteActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					Website website = (Website)object;

					Assert.assertNotNull(website);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		Website newWebsite = addWebsite();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Website.class,
				Website.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("websiteId",
				newWebsite.getWebsiteId()));

		List<Website> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Website existingWebsite = result.get(0);

		Assert.assertEquals(existingWebsite, newWebsite);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Website.class,
				Website.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("websiteId",
				ServiceTestUtil.nextLong()));

		List<Website> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		Website newWebsite = addWebsite();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Website.class,
				Website.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("websiteId"));

		Object newWebsiteId = newWebsite.getWebsiteId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("websiteId",
				new Object[] { newWebsiteId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingWebsiteId = result.get(0);

		Assert.assertEquals(existingWebsiteId, newWebsiteId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Website.class,
				Website.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("websiteId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("websiteId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected Website addWebsite() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Website website = _persistence.create(pk);

		website.setUuid(ServiceTestUtil.randomString());

		website.setCompanyId(ServiceTestUtil.nextLong());

		website.setUserId(ServiceTestUtil.nextLong());

		website.setUserName(ServiceTestUtil.randomString());

		website.setCreateDate(ServiceTestUtil.nextDate());

		website.setModifiedDate(ServiceTestUtil.nextDate());

		website.setClassNameId(ServiceTestUtil.nextLong());

		website.setClassPK(ServiceTestUtil.nextLong());

		website.setUrl(ServiceTestUtil.randomString());

		website.setTypeId(ServiceTestUtil.nextInt());

		website.setPrimary(ServiceTestUtil.randomBoolean());

		_persistence.update(website);

		return website;
	}

	private static Log _log = LogFactoryUtil.getLog(WebsitePersistenceTest.class);
	private WebsitePersistence _persistence = (WebsitePersistence)PortalBeanLocatorUtil.locate(WebsitePersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}