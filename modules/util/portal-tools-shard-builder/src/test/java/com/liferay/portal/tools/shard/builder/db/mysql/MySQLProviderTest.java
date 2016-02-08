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

package com.liferay.portal.tools.shard.builder.db.mysql;

import com.liferay.portal.tools.shard.builder.internal.algorithm.BaseDBProviderTestCase;

import java.sql.Date;
import java.sql.Timestamp;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class MySQLProviderTest extends BaseDBProviderTestCase {

	@Test
	public void testSerializeNull() throws Exception {
		String serializeTableField = dbProvider.serializeTableField(null);

		Assert.assertEquals("null", serializeTableField);
	}

	@Override
	@Test
	public void testSerializeTableFieldDate() throws Exception {
		String serializeTableField = dbProvider.serializeTableField(
			new Date(0L));

		Assert.assertEquals("'1970-01-01 00:00:00'", serializeTableField);
	}

	@Override
	@Test
	public void testSerializeTableFieldDouble() throws Exception {
		String serializeTableField = dbProvider.serializeTableField(
			Double.valueOf(99.99));

		Assert.assertEquals("'99.99'", serializeTableField);
	}

	@Override
	@Test
	public void testSerializeTableFieldFloat() throws Exception {
		String serializeTableField = dbProvider.serializeTableField(
			Float.valueOf(1));

		Assert.assertEquals("'1.0'", serializeTableField);
	}

	@Override
	@Test
	public void testSerializeTableFieldInteger() throws Exception {
		String serializeTableField = dbProvider.serializeTableField(
			Integer.valueOf(1));

		Assert.assertEquals("'1'", serializeTableField);
	}

	@Override
	@Test
	public void testSerializeTableFieldTimestamp() throws Exception {
		String serializeTableField = dbProvider.serializeTableField(
			new Timestamp(0L));

		Assert.assertEquals("'1970-01-01 00:00:00'", serializeTableField);
	}

	@Override
	protected String getCreateTableSQL() {
		return "create table foo (i INT, f DOUBLE, s VARCHAR(75), d TIMESTAMP)";
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
		return "mysql";
	}

}