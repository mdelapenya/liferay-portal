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

import com.liferay.portal.NoSuchUserException;
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
import com.liferay.portal.model.User;
import com.liferay.portal.model.impl.UserModelImpl;
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
public class UserPersistenceTest {
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

		User user = _persistence.create(pk);

		Assert.assertNotNull(user);

		Assert.assertEquals(user.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		User newUser = addUser();

		_persistence.remove(newUser);

		User existingUser = _persistence.fetchByPrimaryKey(newUser.getPrimaryKey());

		Assert.assertNull(existingUser);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addUser();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		User newUser = _persistence.create(pk);

		newUser.setUuid(ServiceTestUtil.randomString());

		newUser.setCompanyId(ServiceTestUtil.nextLong());

		newUser.setCreateDate(ServiceTestUtil.nextDate());

		newUser.setModifiedDate(ServiceTestUtil.nextDate());

		newUser.setDefaultUser(ServiceTestUtil.randomBoolean());

		newUser.setContactId(ServiceTestUtil.nextLong());

		newUser.setPassword(ServiceTestUtil.randomString());

		newUser.setPasswordEncrypted(ServiceTestUtil.randomBoolean());

		newUser.setPasswordReset(ServiceTestUtil.randomBoolean());

		newUser.setPasswordModifiedDate(ServiceTestUtil.nextDate());

		newUser.setDigest(ServiceTestUtil.randomString());

		newUser.setReminderQueryQuestion(ServiceTestUtil.randomString());

		newUser.setReminderQueryAnswer(ServiceTestUtil.randomString());

		newUser.setGraceLoginCount(ServiceTestUtil.nextInt());

		newUser.setScreenName(ServiceTestUtil.randomString());

		newUser.setEmailAddress(ServiceTestUtil.randomString());

		newUser.setFacebookId(ServiceTestUtil.nextLong());

		newUser.setLdapServerId(ServiceTestUtil.nextLong());

		newUser.setOpenId(ServiceTestUtil.randomString());

		newUser.setPortraitId(ServiceTestUtil.nextLong());

		newUser.setLanguageId(ServiceTestUtil.randomString());

		newUser.setTimeZoneId(ServiceTestUtil.randomString());

		newUser.setGreeting(ServiceTestUtil.randomString());

		newUser.setComments(ServiceTestUtil.randomString());

		newUser.setFirstName(ServiceTestUtil.randomString());

		newUser.setMiddleName(ServiceTestUtil.randomString());

		newUser.setLastName(ServiceTestUtil.randomString());

		newUser.setJobTitle(ServiceTestUtil.randomString());

		newUser.setLoginDate(ServiceTestUtil.nextDate());

		newUser.setLoginIP(ServiceTestUtil.randomString());

		newUser.setLastLoginDate(ServiceTestUtil.nextDate());

		newUser.setLastLoginIP(ServiceTestUtil.randomString());

		newUser.setLastFailedLoginDate(ServiceTestUtil.nextDate());

		newUser.setFailedLoginAttempts(ServiceTestUtil.nextInt());

		newUser.setLockout(ServiceTestUtil.randomBoolean());

		newUser.setLockoutDate(ServiceTestUtil.nextDate());

		newUser.setAgreedToTermsOfUse(ServiceTestUtil.randomBoolean());

		newUser.setEmailAddressVerified(ServiceTestUtil.randomBoolean());

		newUser.setStatus(ServiceTestUtil.nextInt());

		_persistence.update(newUser);

		User existingUser = _persistence.findByPrimaryKey(newUser.getPrimaryKey());

		Assert.assertEquals(existingUser.getUuid(), newUser.getUuid());
		Assert.assertEquals(existingUser.getUserId(), newUser.getUserId());
		Assert.assertEquals(existingUser.getCompanyId(), newUser.getCompanyId());
		Assert.assertEquals(Time.getShortTimestamp(existingUser.getCreateDate()),
			Time.getShortTimestamp(newUser.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingUser.getModifiedDate()),
			Time.getShortTimestamp(newUser.getModifiedDate()));
		Assert.assertEquals(existingUser.getDefaultUser(),
			newUser.getDefaultUser());
		Assert.assertEquals(existingUser.getContactId(), newUser.getContactId());
		Assert.assertEquals(existingUser.getPassword(), newUser.getPassword());
		Assert.assertEquals(existingUser.getPasswordEncrypted(),
			newUser.getPasswordEncrypted());
		Assert.assertEquals(existingUser.getPasswordReset(),
			newUser.getPasswordReset());
		Assert.assertEquals(Time.getShortTimestamp(
				existingUser.getPasswordModifiedDate()),
			Time.getShortTimestamp(newUser.getPasswordModifiedDate()));
		Assert.assertEquals(existingUser.getDigest(), newUser.getDigest());
		Assert.assertEquals(existingUser.getReminderQueryQuestion(),
			newUser.getReminderQueryQuestion());
		Assert.assertEquals(existingUser.getReminderQueryAnswer(),
			newUser.getReminderQueryAnswer());
		Assert.assertEquals(existingUser.getGraceLoginCount(),
			newUser.getGraceLoginCount());
		Assert.assertEquals(existingUser.getScreenName(),
			newUser.getScreenName());
		Assert.assertEquals(existingUser.getEmailAddress(),
			newUser.getEmailAddress());
		Assert.assertEquals(existingUser.getFacebookId(),
			newUser.getFacebookId());
		Assert.assertEquals(existingUser.getLdapServerId(),
			newUser.getLdapServerId());
		Assert.assertEquals(existingUser.getOpenId(), newUser.getOpenId());
		Assert.assertEquals(existingUser.getPortraitId(),
			newUser.getPortraitId());
		Assert.assertEquals(existingUser.getLanguageId(),
			newUser.getLanguageId());
		Assert.assertEquals(existingUser.getTimeZoneId(),
			newUser.getTimeZoneId());
		Assert.assertEquals(existingUser.getGreeting(), newUser.getGreeting());
		Assert.assertEquals(existingUser.getComments(), newUser.getComments());
		Assert.assertEquals(existingUser.getFirstName(), newUser.getFirstName());
		Assert.assertEquals(existingUser.getMiddleName(),
			newUser.getMiddleName());
		Assert.assertEquals(existingUser.getLastName(), newUser.getLastName());
		Assert.assertEquals(existingUser.getJobTitle(), newUser.getJobTitle());
		Assert.assertEquals(Time.getShortTimestamp(existingUser.getLoginDate()),
			Time.getShortTimestamp(newUser.getLoginDate()));
		Assert.assertEquals(existingUser.getLoginIP(), newUser.getLoginIP());
		Assert.assertEquals(Time.getShortTimestamp(
				existingUser.getLastLoginDate()),
			Time.getShortTimestamp(newUser.getLastLoginDate()));
		Assert.assertEquals(existingUser.getLastLoginIP(),
			newUser.getLastLoginIP());
		Assert.assertEquals(Time.getShortTimestamp(
				existingUser.getLastFailedLoginDate()),
			Time.getShortTimestamp(newUser.getLastFailedLoginDate()));
		Assert.assertEquals(existingUser.getFailedLoginAttempts(),
			newUser.getFailedLoginAttempts());
		Assert.assertEquals(existingUser.getLockout(), newUser.getLockout());
		Assert.assertEquals(Time.getShortTimestamp(
				existingUser.getLockoutDate()),
			Time.getShortTimestamp(newUser.getLockoutDate()));
		Assert.assertEquals(existingUser.getAgreedToTermsOfUse(),
			newUser.getAgreedToTermsOfUse());
		Assert.assertEquals(existingUser.getEmailAddressVerified(),
			newUser.getEmailAddressVerified());
		Assert.assertEquals(existingUser.getStatus(), newUser.getStatus());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		User newUser = addUser();

		User existingUser = _persistence.findByPrimaryKey(newUser.getPrimaryKey());

		Assert.assertEquals(existingUser, newUser);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchUserException");
		}
		catch (NoSuchUserException nsee) {
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
		User user = addUser();

		String uuid = user.getUuid();

		List<User> users = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, users.size());

		Assert.assertEquals(user.getPrimaryKey(), users.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addUser();

		String uuid = ServiceTestUtil.randomString();

		List<User> users = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, users.size());
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
		User user = addUser();

		String uuid = user.getUuid();

		List<User> users = _persistence.findByUuid(uuid, start, end);

		Assert.assertEquals(expected, users.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		User user = addUser();

		String uuid = user.getUuid();

		long companyId = user.getCompanyId();

		List<User> users = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(1, users.size());

		Assert.assertEquals(user.getPrimaryKey(), users.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addUser();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<User> users = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(0, users.size());
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
		User user = addUser();

		String uuid = user.getUuid();

		long companyId = user.getCompanyId();

		List<User> users = _persistence.findByUuid_C(uuid, companyId, start, end);

		Assert.assertEquals(expected, users.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		User user = addUser();

		long companyId = user.getCompanyId();

		List<User> users = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, users.size());

		Assert.assertEquals(user.getPrimaryKey(), users.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addUser();

		long companyId = ServiceTestUtil.nextLong();

		List<User> users = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, users.size());
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
		User user = addUser();

		long companyId = user.getCompanyId();

		List<User> users = _persistence.findByCompanyId(companyId, start, end);

		Assert.assertEquals(expected, users.size());
	}

	@Test
	public void testFindByEmailAddress() throws Exception {
		User user = addUser();

		String emailAddress = user.getEmailAddress();

		List<User> users = _persistence.findByEmailAddress(emailAddress);

		Assert.assertEquals(1, users.size());

		Assert.assertEquals(user.getPrimaryKey(), users.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByEmailAddressNotFound() throws Exception {
		addUser();

		String emailAddress = ServiceTestUtil.randomString();

		List<User> users = _persistence.findByEmailAddress(emailAddress);

		Assert.assertEquals(0, users.size());
	}

	@Test
	public void testFindByEmailAddressStartEnd() throws Exception {
		testFindByEmailAddressStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByEmailAddressStartEndWrongRange()
		throws Exception {
		testFindByEmailAddressStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByEmailAddressStartEndZeroZero()
		throws Exception {
		testFindByEmailAddressStartEnd(0, 0, 0);
	}

	protected void testFindByEmailAddressStartEnd(int start, int end,
		int expected) throws Exception {
		User user = addUser();

		String emailAddress = user.getEmailAddress();

		List<User> users = _persistence.findByEmailAddress(emailAddress, start,
				end);

		Assert.assertEquals(expected, users.size());
	}

	@Test
	public void testFindByC_CD() throws Exception {
		User user = addUser();

		long companyId = user.getCompanyId();

		Date createDate = user.getCreateDate();

		List<User> users = _persistence.findByC_CD(companyId, createDate);

		Assert.assertEquals(1, users.size());

		Assert.assertEquals(user.getPrimaryKey(), users.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_CDNotFound() throws Exception {
		addUser();

		long companyId = ServiceTestUtil.nextLong();

		Date createDate = ServiceTestUtil.nextDate();

		List<User> users = _persistence.findByC_CD(companyId, createDate);

		Assert.assertEquals(0, users.size());
	}

	@Test
	public void testFindByC_CDStartEnd() throws Exception {
		testFindByC_CDStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_CDStartEndWrongRange() throws Exception {
		testFindByC_CDStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_CDStartEndZeroZero() throws Exception {
		testFindByC_CDStartEnd(0, 0, 0);
	}

	protected void testFindByC_CDStartEnd(int start, int end, int expected)
		throws Exception {
		User user = addUser();

		long companyId = user.getCompanyId();

		Date createDate = user.getCreateDate();

		List<User> users = _persistence.findByC_CD(companyId, createDate,
				start, end);

		Assert.assertEquals(expected, users.size());
	}

	@Test
	public void testFindByC_MD() throws Exception {
		User user = addUser();

		long companyId = user.getCompanyId();

		Date modifiedDate = user.getModifiedDate();

		List<User> users = _persistence.findByC_MD(companyId, modifiedDate);

		Assert.assertEquals(1, users.size());

		Assert.assertEquals(user.getPrimaryKey(), users.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_MDNotFound() throws Exception {
		addUser();

		long companyId = ServiceTestUtil.nextLong();

		Date modifiedDate = ServiceTestUtil.nextDate();

		List<User> users = _persistence.findByC_MD(companyId, modifiedDate);

		Assert.assertEquals(0, users.size());
	}

	@Test
	public void testFindByC_MDStartEnd() throws Exception {
		testFindByC_MDStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_MDStartEndWrongRange() throws Exception {
		testFindByC_MDStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_MDStartEndZeroZero() throws Exception {
		testFindByC_MDStartEnd(0, 0, 0);
	}

	protected void testFindByC_MDStartEnd(int start, int end, int expected)
		throws Exception {
		User user = addUser();

		long companyId = user.getCompanyId();

		Date modifiedDate = user.getModifiedDate();

		List<User> users = _persistence.findByC_MD(companyId, modifiedDate,
				start, end);

		Assert.assertEquals(expected, users.size());
	}

	@Test
	public void testFindByC_S() throws Exception {
		User user = addUser();

		long companyId = user.getCompanyId();

		int status = user.getStatus();

		List<User> users = _persistence.findByC_S(companyId, status);

		Assert.assertEquals(1, users.size());

		Assert.assertEquals(user.getPrimaryKey(), users.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_SNotFound() throws Exception {
		addUser();

		long companyId = ServiceTestUtil.nextLong();

		int status = ServiceTestUtil.nextInt();

		List<User> users = _persistence.findByC_S(companyId, status);

		Assert.assertEquals(0, users.size());
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
		User user = addUser();

		long companyId = user.getCompanyId();

		int status = user.getStatus();

		List<User> users = _persistence.findByC_S(companyId, status, start, end);

		Assert.assertEquals(expected, users.size());
	}

	@Test
	public void testFindByC_CD_MD() throws Exception {
		User user = addUser();

		long companyId = user.getCompanyId();

		Date createDate = user.getCreateDate();

		Date modifiedDate = user.getModifiedDate();

		List<User> users = _persistence.findByC_CD_MD(companyId, createDate,
				modifiedDate);

		Assert.assertEquals(1, users.size());

		Assert.assertEquals(user.getPrimaryKey(), users.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_CD_MDNotFound() throws Exception {
		addUser();

		long companyId = ServiceTestUtil.nextLong();

		Date createDate = ServiceTestUtil.nextDate();

		Date modifiedDate = ServiceTestUtil.nextDate();

		List<User> users = _persistence.findByC_CD_MD(companyId, createDate,
				modifiedDate);

		Assert.assertEquals(0, users.size());
	}

	@Test
	public void testFindByC_CD_MDStartEnd() throws Exception {
		testFindByC_CD_MDStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_CD_MDStartEndWrongRange() throws Exception {
		testFindByC_CD_MDStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_CD_MDStartEndZeroZero() throws Exception {
		testFindByC_CD_MDStartEnd(0, 0, 0);
	}

	protected void testFindByC_CD_MDStartEnd(int start, int end, int expected)
		throws Exception {
		User user = addUser();

		long companyId = user.getCompanyId();

		Date createDate = user.getCreateDate();

		Date modifiedDate = user.getModifiedDate();

		List<User> users = _persistence.findByC_CD_MD(companyId, createDate,
				modifiedDate, start, end);

		Assert.assertEquals(expected, users.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("User_", "uuid", true,
			"userId", true, "companyId", true, "createDate", true,
			"modifiedDate", true, "defaultUser", true, "contactId", true,
			"password", true, "passwordEncrypted", true, "passwordReset", true,
			"passwordModifiedDate", true, "digest", true,
			"reminderQueryQuestion", true, "reminderQueryAnswer", true,
			"graceLoginCount", true, "screenName", true, "emailAddress", true,
			"facebookId", true, "ldapServerId", true, "openId", true,
			"portraitId", true, "languageId", true, "timeZoneId", true,
			"greeting", true, "comments", true, "firstName", true,
			"middleName", true, "lastName", true, "jobTitle", true,
			"loginDate", true, "loginIP", true, "lastLoginDate", true,
			"lastLoginIP", true, "lastFailedLoginDate", true,
			"failedLoginAttempts", true, "lockout", true, "lockoutDate", true,
			"agreedToTermsOfUse", true, "emailAddressVerified", true, "status",
			true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		User newUser = addUser();

		User existingUser = _persistence.fetchByPrimaryKey(newUser.getPrimaryKey());

		Assert.assertEquals(existingUser, newUser);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		User missingUser = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingUser);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new UserActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					User user = (User)object;

					Assert.assertNotNull(user);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		User newUser = addUser();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(User.class,
				User.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("userId",
				newUser.getUserId()));

		List<User> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		User existingUser = result.get(0);

		Assert.assertEquals(existingUser, newUser);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(User.class,
				User.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("userId",
				ServiceTestUtil.nextLong()));

		List<User> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		User newUser = addUser();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(User.class,
				User.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("userId"));

		Object newUserId = newUser.getUserId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("userId",
				new Object[] { newUserId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingUserId = result.get(0);

		Assert.assertEquals(existingUserId, newUserId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(User.class,
				User.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("userId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("userId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		User newUser = addUser();

		_persistence.clearCache();

		UserModelImpl existingUserModelImpl = (UserModelImpl)_persistence.findByPrimaryKey(newUser.getPrimaryKey());

		Assert.assertEquals(existingUserModelImpl.getContactId(),
			existingUserModelImpl.getOriginalContactId());

		Assert.assertEquals(existingUserModelImpl.getPortraitId(),
			existingUserModelImpl.getOriginalPortraitId());

		Assert.assertEquals(existingUserModelImpl.getCompanyId(),
			existingUserModelImpl.getOriginalCompanyId());
		Assert.assertEquals(existingUserModelImpl.getUserId(),
			existingUserModelImpl.getOriginalUserId());

		Assert.assertEquals(existingUserModelImpl.getCompanyId(),
			existingUserModelImpl.getOriginalCompanyId());
		Assert.assertEquals(existingUserModelImpl.getDefaultUser(),
			existingUserModelImpl.getOriginalDefaultUser());

		Assert.assertEquals(existingUserModelImpl.getCompanyId(),
			existingUserModelImpl.getOriginalCompanyId());
		Assert.assertTrue(Validator.equals(
				existingUserModelImpl.getScreenName(),
				existingUserModelImpl.getOriginalScreenName()));

		Assert.assertEquals(existingUserModelImpl.getCompanyId(),
			existingUserModelImpl.getOriginalCompanyId());
		Assert.assertTrue(Validator.equals(
				existingUserModelImpl.getEmailAddress(),
				existingUserModelImpl.getOriginalEmailAddress()));

		Assert.assertEquals(existingUserModelImpl.getCompanyId(),
			existingUserModelImpl.getOriginalCompanyId());
		Assert.assertEquals(existingUserModelImpl.getFacebookId(),
			existingUserModelImpl.getOriginalFacebookId());

		Assert.assertEquals(existingUserModelImpl.getCompanyId(),
			existingUserModelImpl.getOriginalCompanyId());
		Assert.assertTrue(Validator.equals(existingUserModelImpl.getOpenId(),
				existingUserModelImpl.getOriginalOpenId()));
	}

	protected User addUser() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		User user = _persistence.create(pk);

		user.setUuid(ServiceTestUtil.randomString());

		user.setCompanyId(ServiceTestUtil.nextLong());

		user.setCreateDate(ServiceTestUtil.nextDate());

		user.setModifiedDate(ServiceTestUtil.nextDate());

		user.setDefaultUser(ServiceTestUtil.randomBoolean());

		user.setContactId(ServiceTestUtil.nextLong());

		user.setPassword(ServiceTestUtil.randomString());

		user.setPasswordEncrypted(ServiceTestUtil.randomBoolean());

		user.setPasswordReset(ServiceTestUtil.randomBoolean());

		user.setPasswordModifiedDate(ServiceTestUtil.nextDate());

		user.setDigest(ServiceTestUtil.randomString());

		user.setReminderQueryQuestion(ServiceTestUtil.randomString());

		user.setReminderQueryAnswer(ServiceTestUtil.randomString());

		user.setGraceLoginCount(ServiceTestUtil.nextInt());

		user.setScreenName(ServiceTestUtil.randomString());

		user.setEmailAddress(ServiceTestUtil.randomString());

		user.setFacebookId(ServiceTestUtil.nextLong());

		user.setLdapServerId(ServiceTestUtil.nextLong());

		user.setOpenId(ServiceTestUtil.randomString());

		user.setPortraitId(ServiceTestUtil.nextLong());

		user.setLanguageId(ServiceTestUtil.randomString());

		user.setTimeZoneId(ServiceTestUtil.randomString());

		user.setGreeting(ServiceTestUtil.randomString());

		user.setComments(ServiceTestUtil.randomString());

		user.setFirstName(ServiceTestUtil.randomString());

		user.setMiddleName(ServiceTestUtil.randomString());

		user.setLastName(ServiceTestUtil.randomString());

		user.setJobTitle(ServiceTestUtil.randomString());

		user.setLoginDate(ServiceTestUtil.nextDate());

		user.setLoginIP(ServiceTestUtil.randomString());

		user.setLastLoginDate(ServiceTestUtil.nextDate());

		user.setLastLoginIP(ServiceTestUtil.randomString());

		user.setLastFailedLoginDate(ServiceTestUtil.nextDate());

		user.setFailedLoginAttempts(ServiceTestUtil.nextInt());

		user.setLockout(ServiceTestUtil.randomBoolean());

		user.setLockoutDate(ServiceTestUtil.nextDate());

		user.setAgreedToTermsOfUse(ServiceTestUtil.randomBoolean());

		user.setEmailAddressVerified(ServiceTestUtil.randomBoolean());

		user.setStatus(ServiceTestUtil.nextInt());

		_persistence.update(user);

		return user;
	}

	private static Log _log = LogFactoryUtil.getLog(UserPersistenceTest.class);
	private UserPersistence _persistence = (UserPersistence)PortalBeanLocatorUtil.locate(UserPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}