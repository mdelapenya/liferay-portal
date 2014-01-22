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

package com.liferay.portal.service;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.sender.DirectSynchronousMessageSender;
import com.liferay.portal.kernel.messaging.sender.SynchronousMessageSender;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.model.ClassName;
import com.liferay.portal.model.impl.ClassNameImpl;

import com.liferay.portal.service.persistence.ClassNamePersistence;
import org.junit.Test;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import org.junit.runner.RunWith;
import org.testng.Assert;

/**
 * @author Manuel de la Peña
 */
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class PortalServiceTest {

	@Test
	public void testAddClassName_Rollback()
		throws SystemException {

		String classNameValue = "";

		addClassName(classNameValue);

		throw new SystemException();
	}

	@Test
	public void testAddClassName_Success()
		throws SystemException {

		String classNameValue = "";

		addClassName(classNameValue);
	}

	@Test
	public void testAddClassNameAndTestTransactionPortletBar_PortalRollback()
		throws SystemException {

		String transactionPortletBarText = "";

		addClassName(PortalService.class.getName());

		addTransactionPortletBar(transactionPortletBarText, false);

		throw new SystemException();
	}

	@Test
	public void testAddClassNameAndTestTransactionPortletBar_PortletRollback()
		throws SystemException {

		String transactionPortletBarText = "";

		addClassName(PortalService.class.getName());

		addTransactionPortletBar(transactionPortletBarText, true);
	}

	@Test
	public void testAddClassNameAndTestTransactionPortletBar_Success()
		throws SystemException {

		String transactionPortletBarText = "";

		addClassName(PortalService.class.getName());

		addTransactionPortletBar(transactionPortletBarText, false);
	}

	@Test
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public void testAutoSyncHibernateSessionStateOnTxCreation()
		throws SystemException {

		// Add in new transaction

		ClassName className = ClassNameLocalServiceUtil.addClassName(
			"testAutoSyncHibernateSessionStateOnTxCreation1");

		try {

			// Fetch in current transaction

			// Clear entity cache to force Hibernate to populate its first level
			// cache

			EntityCacheUtil.clearCache();

			className = _persistence.fetchByPrimaryKey(
				className.getClassNameId());

			Session currentSession = _persistence.getCurrentSession();

			if (!currentSession.contains(className)) {
				throw new IllegalStateException(
					"Entities are not available in Hibernate's first level " +
						"cache");
			}

			ClassName newClassName = new ClassNameImpl();

			newClassName.setPrimaryKey(className.getClassNameId());

			String newValue = "testAutoSyncHibernateSessionStateOnTxCreation2";

			newClassName.setValue(newValue);

			// Update in new transaction

			ClassNameLocalServiceUtil.updateClassName(newClassName);

			if (currentSession.contains(className)) {
				throw new IllegalStateException(
					"Entities are still available in Hibernate's first level " +
						"cache");
			}

			// Refetch in current transaction

			// Clear entity cache to force Hibernate to populate its first level
			// cache

			EntityCacheUtil.clearCache();

			className = _persistence.fetchByPrimaryKey(
				className.getClassNameId());

			if (!newValue.equals(className.getValue())) {
				throw new IllegalStateException(
					"Expected " + newValue + " but found " +
						className.getClassName());
			}
		}
		finally {

			// Clean up

			ClassNameLocalServiceUtil.deleteClassName(className);
		}
	}

	@Test
	public void testDeleteClassName() throws PortalException, SystemException {
		_persistence.removeByValue(PortalService.class.getName());
	}

	@Test
	public void testGetBuildNumber() {
		int buildNumber = PortalServiceUtil.getBuildNumber();

		Assert.assertEquals(7000, buildNumber);
	}

	@Test
	public void testHasClassName() throws SystemException {
		int count = _persistence.countByValue(
			PortalService.class.getName());

		Assert.assertTrue(count > 0);
	}

	protected void addClassName(String classNameValue) throws SystemException {
		long classNameId = CounterLocalServiceUtil.increment();

		ClassName className = _persistence.create(classNameId);

		className.setValue(classNameValue);

		_persistence.update(className);
	}

	protected void addTransactionPortletBar(
			String transactionPortletBarText, boolean rollback)
		throws SystemException {

		try {
			Message message = new Message();

			message.put("rollback", rollback);
			message.put("text", transactionPortletBarText);

			SynchronousMessageSender synchronousMessageSender =
				(SynchronousMessageSender) PortalBeanLocatorUtil.locate(
					DirectSynchronousMessageSender.class.getName());

			synchronousMessageSender.send(
				DestinationNames.TEST_TRANSACTION, message);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	private ClassNamePersistence _persistence =
		(ClassNamePersistence)PortalBeanLocatorUtil.locate(
			ClassNamePersistence.class.getName());

	private static Log _log = LogFactoryUtil.getLog(PortalServiceTest.class);

}