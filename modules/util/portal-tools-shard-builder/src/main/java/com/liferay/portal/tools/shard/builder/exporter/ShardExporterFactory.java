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

import java.util.Properties;

/**
 * @author Manuel de la Peña
 */
public class ShardExporterFactory {

	public static ShardExporter getShardExporter(Properties properties) {
		String dataSourceClassName = properties.getProperty(
			"dataSourceClassName");

		if (_DATASOURCE_CLASS_NAME_DB2.equals(dataSourceClassName)) {
			return new DB2Provider(properties);
		}
		else if (_DATASOURCE_CLASS_NAME_MS_SQL_SERVER.equals(
					dataSourceClassName)) {

			return new MSSQLServerProvider(properties);
		}
		else if (_DATASOURCE_CLASS_NAME_MYSQL.equals(dataSourceClassName)) {
			return new MySQLProvider(properties);
		}
		else if (_DATASOURCE_CLASS_NAME_ORACLE.equals(dataSourceClassName)) {
			return new OracleProvider(properties);
		}
		else if (_DATASOURCE_CLASS_NAME_POSTGRESQL.equals(
					dataSourceClassName) ||
				 _DATASOURCE_CLASS_NAME_POSTGRESQL_SIMPLE.equals(
					 dataSourceClassName)) {

			return new PostgreSQLProvider(properties);
		}
		else if (_DATASOURCE_CLASS_NAME_SYBASE.equals(dataSourceClassName)) {
			return new SybaseProvider(properties);
		}

		throw new DBProviderNotAvailableException();
	}

	private static final String _DATASOURCE_CLASS_NAME_DB2 =
		"com.ibm.db2.jcc.DB2SimpleDataSource";

	private static final String _DATASOURCE_CLASS_NAME_MS_SQL_SERVER =
		"com.microsoft.sqlserver.jdbc.SQLServerDataSource";

	private static final String _DATASOURCE_CLASS_NAME_MYSQL =
		"com.mysql.jdbc.jdbc2.optional.MysqlDataSource";

	private static final String _DATASOURCE_CLASS_NAME_ORACLE =
		"oracle.jdbc.pool.OracleDataSource";

	private static final String _DATASOURCE_CLASS_NAME_POSTGRESQL =
		"com.impossibl.postgres.jdbc.PGDataSource";

	private static final String _DATASOURCE_CLASS_NAME_POSTGRESQL_SIMPLE =
		"org.postgresql.ds.PGSimpleDataSource";

	private static final String _DATASOURCE_CLASS_NAME_SYBASE =
		"com.sybase.jdbc4.jdbc.SybDataSource";

}