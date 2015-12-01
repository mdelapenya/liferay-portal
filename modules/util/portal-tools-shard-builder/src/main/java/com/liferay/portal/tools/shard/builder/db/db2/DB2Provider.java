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

import com.liferay.portal.tools.shard.builder.internal.algorithm.BaseDBProvider;

import java.util.Properties;

/**
 * @author Manuel de la Peña
 */
public class DB2Provider extends BaseDBProvider {

	public DB2Provider(Properties dbProperties) {
		super(dbProperties);
	}

	@Override
	public String getControlTableNamesSQL(String schema) {
		StringBuilder sb = new StringBuilder(12);

		sb.append("select c1.");
		sb.append(getTableNameFieldName());
		sb.append(
			" from syscat.columns c1, syscat.tables t1 where t1.tabschema = '");
		sb.append(schema);
		sb.append("' and t.tabschema = c1.tabschema and c1.");
		sb.append(getTableNameFieldName());
		sb.append(" not in (");
		sb.append(getPartitionedTableNamesSQL(schema));
		sb.append(") group by c1.");
		sb.append(getTableNameFieldName());
		sb.append(" order by c1.");
		sb.append(getTableNameFieldName());

		return sb.toString();
	}

	@Override
	public String getPartitionedTableNamesSQL(String schema) {
		StringBuilder sb = new StringBuilder(9);

		sb.append("select c2.");
		sb.append(getTableNameFieldName());
		sb.append(" from syscat.columns c2, syscat.tables t2 where ");
		sb.append("t2.tabschema = c2.tabschema and t2.tabschema = '");
		sb.append(schema);
		sb.append("' and c2.colname = 'COMPANYID' group by c2.");
		sb.append(getTableNameFieldName());
		sb.append(" order by c2.");
		sb.append(getTableNameFieldName());

		return sb.toString();
	}

	@Override
	public String getTableNameFieldName() {
		return "tabname";
	}

}