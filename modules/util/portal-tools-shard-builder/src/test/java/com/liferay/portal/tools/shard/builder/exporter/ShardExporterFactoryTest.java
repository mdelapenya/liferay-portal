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

package com.liferay.portal.tools.shard.builder.exporter;

import com.liferay.portal.tools.shard.builder.db.db2.DB2Provider;
import com.liferay.portal.tools.shard.builder.db.mssqlserver.MSSQLServerProvider;
import com.liferay.portal.tools.shard.builder.db.mysql.MySQLProvider;
import com.liferay.portal.tools.shard.builder.db.oracle.OracleProvider;
import com.liferay.portal.tools.shard.builder.db.postgresql.PostgreSQLProvider;
import com.liferay.portal.tools.shard.builder.db.sybase.SybaseProvider;
import com.liferay.portal.tools.shard.builder.exporter.exception.DBProviderNotAvailableException;
import com.liferay.portal.tools.shard.builder.internal.util.PropsReader;

import java.net.URL;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class ShardExporterFactoryTest {

	@Test(expected = DBProviderNotAvailableException.class)
	public void testGetShardExporter() throws Exception {
		ShardExporterFactory.getShardExporter(new Properties());
	}

	@Test
	public void testGetShardExporterReturnsDB2Provider() throws Exception {
		testGetShardExporter("db2", DB2Provider.class);
	}

	@Test
	public void testGetShardExporterReturnsMSSQLServerProvider()
		throws Exception {

		testGetShardExporter("mssql-server", MSSQLServerProvider.class);
	}

	@Test
	public void testGetShardExporterReturnsMysqlProvider() throws Exception {
		testGetShardExporter("mysql", MySQLProvider.class);
	}

	@Test
	public void testGetShardExporterReturnsOracleProvider() throws Exception {
		testGetShardExporter("oracle", OracleProvider.class);
	}

	@Test
	public void testGetShardExporterReturnsPostgreSqlProvider()
		throws Exception {

		testGetShardExporter("postgresql", PostgreSQLProvider.class);
	}

	@Test
	public void testGetShardExporterReturnsSybaseProvider() throws Exception {
		testGetShardExporter("sybase", SybaseProvider.class);
	}

	protected void testGetShardExporter(
			String databaseType, Class<?> providerClass)
		throws Exception {

		Class<?> clazz = getClass();

		URL url = clazz.getResource("/" + databaseType +".properties");

		Assume.assumeNotNull(url);

		Properties properties = PropsReader.read(url.getPath());

		ShardExporter shardExporter = ShardExporterFactory.getShardExporter(
			properties);

		Assert.assertTrue(providerClass.isInstance(shardExporter));
	}

}