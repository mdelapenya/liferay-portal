/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.persistence.arquillian.transactional.util;

import com.liferay.persistence.arquillian.annotation.PersistenceTest;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionAttribute;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;

import java.util.concurrent.Callable;

import org.jboss.arquillian.core.spi.EventContext;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.ClassEvent;

/**
 * @author Cristina González
 */
public class TransactionalUtilImpl implements TransactionalUtil {

	public void invokeTransaction(final EventContext eventContext)
		throws Throwable {

		PersistenceTest persistenceTestAnnotation = getAnnotation(
			(ClassEvent)eventContext.getEvent());

		if (persistenceTestAnnotation != null) {
			TransactionAttribute.Builder builder =
				new TransactionAttribute.Builder();

			builder.setPropagation(Propagation.REQUIRED);
			builder.setRollbackForClasses(
				PortalException.class, SystemException.class);

			TransactionInvokerUtil.invoke(
				builder.build(), new Callable<Object>() {
					@Override
					public Object call() throws Exception {
						eventContext.proceed();
						return null;
					}
				}
			);
		}
		else {
			eventContext.proceed();
		}
	}

	protected PersistenceTest getAnnotation(ClassEvent testEvent) {
		TestClass testClass = testEvent.getTestClass();

		return testClass.getAnnotation(PersistenceTest.class);
	}

}