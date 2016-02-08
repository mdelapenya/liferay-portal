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

package com.liferay.portal.tools.shard.builder.db.db2;

import com.liferay.portal.tools.shard.builder.internal.algorithm.BaseDBProviderTestCase;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class DB2ProviderTest extends BaseDBProviderTestCase {

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
	protected String getCreateTableSQL() {
		return "create table foo (i INT, f FLOAT, s VARCHAR(75), d TIMESTAMP)";
	}

	@Override
	protected String getTestPropertiesFileName() {
		return "db2";
	}

}