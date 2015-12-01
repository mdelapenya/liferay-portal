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

import com.liferay.portal.tools.shard.builder.internal.algorithm.BaseDBProvider;

import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.Charset;

import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Properties;

import org.apache.commons.io.IOUtils;

/**
 * @author Manuel de la Peña
 */
public class OracleProvider extends BaseDBProvider {

	public OracleProvider(Properties dbProperties) {
		super(dbProperties);
	}

	@Override
	public String getControlTableNamesSQL(String schema) {
		StringBuilder sb = new StringBuilder(10);

		sb.append("select ");
		sb.append(getTableNameFieldName());
		sb.append(" from user_tables where ");
		sb.append(getTableNameFieldName());
		sb.append(" not in (");
		sb.append(getPartitionedTableNamesSQL(schema));
		sb.append(") group by ");
		sb.append(getTableNameFieldName());
		sb.append(" order by ");
		sb.append(getTableNameFieldName());

		return sb.toString();
	}

	@Override
	public String getDateTimeFormat() {
		return "yyyy-MM-dd HH:mm:ss.FFF";
	}

	@Override
	public String getPartitionedTableNamesSQL(String schema) {
		StringBuilder sb = new StringBuilder(4);

		sb.append("select ");
		sb.append(getTableNameFieldName());
		sb.append(" from cols where column_name='COMPANYID' group by ");
		sb.append(getTableNameFieldName());

		return sb.toString();
	}

	@Override
	public String getTableNameFieldName() {
		return "table_name";
	}

	public String serializeTableField(Object field) throws SQLException {
		StringBuilder sb = new StringBuilder();

		if (field == null) {
			sb.append("null");
		}
		else if (field instanceof Number) {
			sb.append(field);
		}
		else if ((field instanceof Date) || (field instanceof Timestamp) ||
				 (field instanceof oracle.sql.TIMESTAMP)) {

			sb.append("to_timestamp('");
			sb.append(formatDateTime(field));
			sb.append("', '");
			sb.append("yyyy-MM-dd HH24:MI:SS.FF");
			sb.append("')");
		}
		else if (field instanceof Clob) {
			sb.append("TO_CLOB('");

			try (InputStream inputStream = ((Clob)field).getAsciiStream()) {
				String clobString = IOUtils.toString(
					inputStream, Charset.forName("UTF-8"));

				sb.append(clobString);
			}
			catch (IOException | SQLException e) {
				e.printStackTrace();
			}

			sb.append("')");
		}
		else if (field instanceof String) {
			String value = (String)field;

			value = value.replace("'", "\\'");

			sb.append("'");
			sb.append(value);
			sb.append("'");
		}
		else {
			sb.append("'");
			sb.append(field);
			sb.append("'");
		}

		return sb.toString();
	}

	protected String formatDateTime(Object date) throws SQLException {
		if (date instanceof oracle.sql.TIMESTAMP) {
			Timestamp timestamp = ((oracle.sql.TIMESTAMP)date).timestampValue();

			return super.formatDateTime(timestamp);
		}

		return super.formatDateTime(date);
	}

}