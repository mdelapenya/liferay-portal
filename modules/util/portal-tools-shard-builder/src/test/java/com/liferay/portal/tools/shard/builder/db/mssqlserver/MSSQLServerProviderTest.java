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

package com.liferay.portal.tools.shard.builder.db.mssqlserver;

import com.liferay.portal.tools.shard.builder.internal.algorithm.BaseDBProviderTestCase;

import java.sql.Date;
import java.sql.Timestamp;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class MSSQLServerProviderTest extends BaseDBProviderTestCase {

	@Override
	@Test
	public void testSerializeTableFieldDate() throws Exception {
		String serializeTableField = dbProvider.serializeTableField(
			new Date(0L));

		Assert.assertEquals("'1970-01-01 00:00:00.000'", serializeTableField);
	}

	@Override
	@Test
	public void testSerializeTableFieldTimestamp() throws Exception {
		String serializeTableField = dbProvider.serializeTableField(
			new Timestamp(0L));

		Assert.assertEquals("'1970-01-01 00:00:00.0'", serializeTableField);
	}

	@Override
	protected String getCreateTableSQL() {
		return
			"create table foo (i INT, f FLOAT, s VARCHAR(75), d DATETIME2(3))";
	}

	@Override
	protected Object[] getDefaultArguments(
			int expectedInteger, Timestamp expectedTimestamp)
		throws Exception {

		double expectedDouble = 99.99d;
		String expectedString = "expectedString";

		return new Object[] {
			expectedInteger, expectedDouble, expectedString, expectedTimestamp
		};
	}

	@Override
	protected String getTestPropertiesFileName() {
		return "mssql-server";
	}

}