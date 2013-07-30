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

package com.liferay.portal.dao.db;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.util.PropsImpl;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Miguel Pastor
 * @author Manuel de la Peña
 */
@PrepareForTest({PropsUtil.class})
@RunWith(PowerMockRunner.class)
public abstract class BaseDBTestCase extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		spy(PropsUtil.class);

		when(
			PropsUtil.getProps()
		).thenReturn(
			new PropsImpl()
		);
	}

	@Test
	public void testGetDatabaseName() throws Exception {
		mockJDBCDefaultURL();

		DB db = getDB();

		String databaseName = db.getDatabaseName(
			PropsUtil.get(PropsKeys.JDBC_DEFAULT_URL));

		verifyStatic();

		Assert.assertEquals(DATABASE_NAME, databaseName);
	}

	protected String buildSQL(String query) throws IOException {
		DB db = getDB();

		return db.buildSQL(query);
	}

	protected abstract DB getDB();

	protected abstract String getJDBCDefaultURL();

	protected void mockJDBCDefaultURL() {
		when(
			PropsUtil.get(PropsKeys.JDBC_DEFAULT_URL)
		).thenReturn(
			getJDBCDefaultURL()
		);
	}

	protected static final String DATABASE_NAME = "lportal_test";

	protected static final String RENAME_TABLE_QUERY = "alter_table_name a b";
}