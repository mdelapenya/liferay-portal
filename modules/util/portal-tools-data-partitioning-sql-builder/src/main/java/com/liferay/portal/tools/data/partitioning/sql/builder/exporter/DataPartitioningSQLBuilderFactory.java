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

package com.liferay.portal.tools.data.partitioning.sql.builder.exporter;

import com.liferay.portal.tools.data.partitioning.sql.builder.exporter.exception.SQLBuilderNotAvailableException;
import com.liferay.portal.tools.data.partitioning.sql.builder.internal.exporter.SQLBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Manuel de la Peña
 */
public class DataPartitioningSQLBuilderFactory {

	public static SQLBuilder getSQLBuilder() {
		ServiceLoader<SQLBuilder> serviceLoader = ServiceLoader.load(
			SQLBuilder.class);

		List<SQLBuilder> sqlBuilders = new ArrayList<>();

		for (SQLBuilder sqlBuilder : serviceLoader) {
			sqlBuilders.add(sqlBuilder);
		}

		_logger.info(sqlBuilders.size() + " SQL builders available");

		for (SQLBuilder sqlBuilder : sqlBuilders) {
			_logger.info("SQL builder " + sqlBuilder);
		}

		if (sqlBuilders.isEmpty() || (sqlBuilders.size() > 1)) {
			throw new SQLBuilderNotAvailableException(
				sqlBuilders.size() + " SQL builder available");
		}

		return sqlBuilders.get(0);
	}

	private static final Logger _logger = LoggerFactory.getLogger(
		DataPartitioningSQLBuilderFactory.class);

}