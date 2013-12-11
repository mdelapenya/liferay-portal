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

package com.liferay.portal.tools.staticanalysis.examples;

import com.liferay.portal.tools.staticanalysis.LiferayAnalisysListener;

import java.lang.Exception;
import java.net.URL;

import org.junit.Before;

/**
 * @author Manuel de la Peña
 */
public abstract class BaseStaticAnalysisTestCase {

	@Before
	public void setUp() throws Exception {
		dependencies = getClazz().getResource("dependencies/");

		listener = new LiferayAnalisysListener();
	}

	protected URL getDependency(String fileName) throws Exception {
		return new URL(dependencies, fileName);
	}

	public abstract Class<?> getClazz();

	protected URL dependencies;
	protected LiferayAnalisysListener listener;

}