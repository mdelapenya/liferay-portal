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

package com.liferay.portal.util;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.Sync;
import com.liferay.portal.test.SynchronousDestinationExecutionTestListener;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Sergio González
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		SynchronousDestinationExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Sync
public abstract class BaseSubscriptionTestCase {

	protected User _subscriptionUser;

	@Before
	public void setUp() throws Exception {
		MailServiceTestUtil.start();

		addSusbscriptionUser();

		group = GroupTestUtil.addGroup();
	}

	@After
	public void tearDown() throws Exception {
		UserLocalServiceUtil.deleteUser(_subscriptionUser);

		GroupLocalServiceUtil.deleteGroup(group);

		MailServiceTestUtil.stop();
	}

	@Test
	public void testSubscriptionBaseModelWhenInContainerModel()
		throws Exception {

		int initialInboxSize = MailServiceTestUtil.getInboxSize();

		long containerModelId = addContainerModel(
			DEFAULT_PARENT_CONTAINER_MODEL_ID);

		long baseModelId = addBaseModel(containerModelId);

		addSubscriptionBaseModel(baseModelId);

		updateEntry(baseModelId);

		Assert.assertEquals(
			initialInboxSize + 1, MailServiceTestUtil.getInboxSize());
	}

	@Test
	public void testSubscriptionBaseModelWhenInRootContainerModel()
		throws Exception {

		long baseModelId = addBaseModel(DEFAULT_PARENT_CONTAINER_MODEL_ID);

		addSubscriptionBaseModel(baseModelId);

		updateEntry(baseModelId);
	}

	@Test
	public void testSubscriptionContainerModelWhenInContainerModel()
		throws Exception {

		long containerModelId = addContainerModel(
			DEFAULT_PARENT_CONTAINER_MODEL_ID);

		addSubscriptionContainerModel(containerModelId);

		addBaseModel(containerModelId);
	}

	@Test
	public void testSubscriptionContainerModelWhenInRootContainerModel()
		throws Exception {

		long containerModelId = addContainerModel(
			DEFAULT_PARENT_CONTAINER_MODEL_ID);

		addSubscriptionContainerModel(containerModelId);

		addBaseModel(DEFAULT_PARENT_CONTAINER_MODEL_ID);
	}

	@Test
	public void testSubscriptionContainerModelWhenInSubcontainerModel()
		throws Exception {

		long containerModelId = addContainerModel(
			DEFAULT_PARENT_CONTAINER_MODEL_ID);

		addSubscriptionContainerModel(containerModelId);

		long subcontainerModelId = addContainerModel(containerModelId);

		addBaseModel(subcontainerModelId);
	}

	@Test
	public void testSubscriptionRootContainerModelWhenInContainerModel()
		throws Exception {

		addSubscriptionContainerModel(DEFAULT_PARENT_CONTAINER_MODEL_ID);

		long containerModelId = addContainerModel(
			DEFAULT_PARENT_CONTAINER_MODEL_ID);

		addBaseModel(containerModelId);
	}

	@Test
	public void testSubscriptionRootContainerModelWhenInRootContainerModel()
		throws Exception {

		addSubscriptionContainerModel(DEFAULT_PARENT_CONTAINER_MODEL_ID);

		addBaseModel(DEFAULT_PARENT_CONTAINER_MODEL_ID);
	}

	@Test
	public void testSubscriptionRootContainerModelWhenInSubcontainerModel()
		throws Exception {

		addSubscriptionContainerModel(DEFAULT_PARENT_CONTAINER_MODEL_ID);

		long containerModelId = addContainerModel(
			DEFAULT_PARENT_CONTAINER_MODEL_ID);

		long subcontainerModelId = addContainerModel(containerModelId);

		addBaseModel(subcontainerModelId);
	}

	protected void addSusbscriptionUser() throws Exception {
		_subscriptionUser = UserTestUtil.addUser();

		_subscriptionUser.setEmailAddress("to@test.com");

		_subscriptionUser = UserLocalServiceUtil.updateUser(_subscriptionUser);
	}

	public abstract long updateEntry(long baseModelId) throws Exception;

	protected abstract long addBaseModel(long containerModelId) throws Exception;

	protected abstract long addContainerModel(long containerModelId)
		throws Exception;

	protected abstract void addSubscriptionBaseModel(long baseModelId)
		throws Exception;

	protected abstract void addSubscriptionContainerModel(long containerModelId)
		throws Exception;

	protected static final long DEFAULT_PARENT_CONTAINER_MODEL_ID = 0;

	protected Group group;

}