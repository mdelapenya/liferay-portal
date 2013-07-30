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
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;

import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Miguel Pastor
 */
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public abstract class BaseDBIntegrationTestCase {

	public abstract String buildCreateDatabaseSQL();

	public abstract String getDatabaseType();

	@Test
	public void testDropDatabase() throws Exception {
		DB db = DBFactoryUtil.getDB();

		db.runSQL(buildCreateDatabaseSQL());

		String currentDatabaseType = db.getType();

		String testDatabaseType = getDatabaseType();

		Assume.assumeTrue(currentDatabaseType.equals(testDatabaseType));

		db.dropDatabase(DATABASE_TO_DROP);

	}

	protected static final String DATABASE_TO_DROP = "database_to_drop";

}