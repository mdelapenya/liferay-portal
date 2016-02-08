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

package com.liferay.portal.tools.shard.builder.db.oracle;

import com.liferay.portal.tools.shard.builder.internal.algorithm.BaseDBProviderTestCase;

import java.sql.Date;
import java.sql.Timestamp;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class OracleProviderTest extends BaseDBProviderTestCase {

	@Override
	@Test
	public void testSerializeTableFieldDate() throws Exception {
		String serializeTableField = dbProvider.serializeTableField(
			new Date(0L));

		String expectedDateField =
			"to_timestamp('1970-01-01 00:00:00.001', " +
				"'yyyy-MM-dd HH24:MI:SS.FF')";

		Assert.assertEquals(expectedDateField, serializeTableField);
	}

	@Override
	@Test
	public void testSerializeTableFieldTimestamp() throws Exception {
		String serializeTableField = dbProvider.serializeTableField(
			new Timestamp(0L));

		String expectedTimestampField =
			"to_timestamp('1970-01-01 00:00:00.001', " +
				"'yyyy-MM-dd HH24:MI:SS.FF')";

		Assert.assertEquals(expectedTimestampField, serializeTableField);
	}

	@Override
	protected String getCreateTableSQL() {
		return
			"create table foo (i INT, f NUMBER(30,20), s VARCHAR(75)," +
				"d TIMESTAMP)";
	}

	@Override
	protected String getTestPropertiesFileName() {
		return "oracle";
	}

}