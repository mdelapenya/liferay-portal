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

package com.liferay.portal.util.rules;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import org.junit.Assume;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * @author Roberto Díaz
 */
public class BaseIgnoreMethodRule implements MethodRule {

	public BaseIgnoreMethodRule(Class clazz) {
		_clazz = clazz;
	}

	public Statement apply(
		final Statement base, final FrameworkMethod frameworkMethod,
		final Object target ) {

		Ignorable ignorable = frameworkMethod.getAnnotation(Ignorable.class);

		if (ignorable == null) {
			return base;
		}

		IgnoreCondition condition = null;

		Class<? extends IgnoreCondition>[] ignoreConditions =
				ignorable.conditions();

		for (Class<? extends IgnoreCondition> ignoreCondition :
				ignoreConditions) {

			try {
				condition = ignoreCondition.newInstance();
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn("Unable to get instance", e);
				}
			}

			if (condition.ignoreTest(_clazz)) {
				return new Statement() {
					@Override
					public void evaluate() throws Throwable {
						Assume.assumeTrue(
							"Ignoring " + frameworkMethod.getName(), false);
					}
				};
			}
		}

		return base;
	}

	private static Log _log = LogFactoryUtil.getLog(BaseIgnoreMethodRule.class);

	private final Class _clazz;

}