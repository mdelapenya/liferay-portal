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

package com.liferay.portal.test;

import com.liferay.portal.cache.transactional.TransactionalPortalCacheHelper;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.test.AbstractExecutionTestListener;
import com.liferay.portal.kernel.test.TestContext;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.test.AbstractExecutionTestListener;
import com.liferay.portal.kernel.test.TestContext;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.test.persistence.BasePersistenceWrapper;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;
import com.liferay.portal.util.PropsValues;
import org.junit.Assert;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Miguel Pastor
<<<<<<< HEAD
 * @author Shuyang Zhou
=======
 * @author Manuel de la Peña
>>>>>>> LPS-35990 Create a transactional memory that holds updated base persistence entities in tests
 */
public class TransactionalExecutionTestListener
	extends AbstractExecutionTestListener {

	public void rollback() {
		List<BasePersistenceWrapper> basePersistencesList =
			_transactionalPersistenceAdvice.getBasePersistencesList();

		for (int i = basePersistencesList.size() - 1; i >= 0; i--) {
			BasePersistenceWrapper basePersistenceWrapper =
				basePersistencesList.get(i);

			BasePersistence<?> basePersistence =
				basePersistenceWrapper.getBasePersistence();

			Serializable key = basePersistenceWrapper.getKey();

			try {
				basePersistence.remove(key);
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug("The model with primary key " + key +
						" was already deleted");
				}
			}
		}

		_transactionalPersistenceAdvice.reset();
	}

	@Override
	public void runBeforeClass(TestContext testContext) {
		PropsValues.SPRING_HIBERNATE_SESSION_DELEGATED = false;

		try {
			TemplateManagerUtil.init();
		}
		catch (TemplateException te) {
			Assert.fail("The template manager can not been initialized");
		}
	}

	@Override
	public void runBeforeTest(TestContext testContext) {
		PropsValues.SPRING_HIBERNATE_SESSION_DELEGATED = false;

		try {
			TemplateManagerUtil.init();
		}
		catch (TemplateException te) {
			Assert.fail("The template manager can not been initialized");
		}
	}

	@Override
	public void runAfterClass(TestContext testContext) {
		rollback();

		PropsValues.SPRING_HIBERNATE_SESSION_DELEGATED = true;
	}

	@Override
	public void runAfterTest(TestContext testContext) {
		/*rollback();

		PropsValues.SPRING_HIBERNATE_SESSION_DELEGATED = true;*/
	}

	private static Log _log = LogFactoryUtil.getLog(
		TransactionalExecutionTestListener.class);
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice =
		(TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(
			TransactionalPersistenceAdvice.class.getName());

}