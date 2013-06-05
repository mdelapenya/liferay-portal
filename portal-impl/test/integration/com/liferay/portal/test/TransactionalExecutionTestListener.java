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
import com.liferay.portal.kernel.test.AbstractExecutionTestListener;
import com.liferay.portal.kernel.test.TestContext;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;

import java.io.Serializable;

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
		Map<Serializable, BasePersistence<?>> basePersistences =
			_transactionalPersistenceAdvice.getBasePersistences();

		Set<Serializable> primaryKeys = basePersistences.keySet();

		for (Serializable primaryKey : primaryKeys) {
			BasePersistence<?> basePersistence = basePersistences.get(
				primaryKey);

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

	@Override
	public void runAfterClass(TestContext testContext) {
		rollback();
	}

	private static Log _log = LogFactoryUtil.getLog(
		TransactionalExecutionTestListener.class);
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice =
		(TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(
			TransactionalPersistenceAdvice.class.getName());
}