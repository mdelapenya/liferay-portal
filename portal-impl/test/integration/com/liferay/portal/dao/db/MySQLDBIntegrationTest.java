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
import com.liferay.portal.kernel.util.StringBundler;

/**
 * @author Miguel Pastor
 */
public class MySQLDBIntegrationTest extends BaseDBIntegrationTestCase {

	@Override
	public String buildCreateDatabaseSQL() {
		StringBundler sb = new StringBundler(3);

		sb.append("create database ");
		sb.append(DATABASE_TO_DROP);
		sb.append(" character set utf8;");

		return sb.toString();
	}

	@Override
	public String getDatabaseType() {
		return DB.TYPE_MYSQL;
	}

}