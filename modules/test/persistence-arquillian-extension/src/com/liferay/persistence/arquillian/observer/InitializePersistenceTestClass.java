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

package com.liferay.persistence.arquillian.observer;

import com.liferay.persistence.arquillian.annotation.PersistenceTest;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.test.jdbc.ResetDatabaseUtilDataSource;
import com.liferay.portal.tools.DBUpgrader;
import com.liferay.portal.util.InitUtil;
import com.liferay.portal.util.PropsUtil;

import java.util.List;

import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;

/**
 * @author Cristina González
 */
public class InitializePersistenceTestClass {

	public void method(@Observes BeforeClass beforeClass)
		throws TemplateException {

		TestClass testClass = beforeClass.getTestClass();

		if (testClass.getAnnotation(PersistenceTest.class) != null) {
			System.setProperty("catalina.base", ".");

			ResetDatabaseUtilDataSource.initialize();

			List<String> configLocations = ListUtil.fromArray(
				PropsUtil.getArray(PropsKeys.SPRING_CONFIGS));

			InitUtil.initWithSpringAndModuleFramework(
				false, processConfigLocations(configLocations), false);

			if (System.getProperty("external-properties") == null) {
				System.setProperty(
					"external-properties", "portal-test.properties");
			}

			try {
				DBUpgrader.upgrade();
			}
			catch (Exception e) {
				_log.error(e, e);
			}

			TemplateManagerUtil.init();
		}
	}

	protected List<String> processConfigLocations(
		List<String> configLocations) {

		configLocations.remove("META-INF/model-listener-spring.xml");

		return configLocations;
	}

	private static Log _log = LogFactoryUtil.getLog(
		InitializePersistenceTestClass.class);

}