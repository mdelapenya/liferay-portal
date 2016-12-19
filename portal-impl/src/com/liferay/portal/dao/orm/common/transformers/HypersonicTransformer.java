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

package com.liferay.portal.dao.orm.common.transformers;

import com.liferay.portal.kernel.dao.db.DB;

import java.util.function.Function;
import java.util.regex.Matcher;

/**
 * @author Manuel de la Peña
 */
public class HypersonicTransformer extends SQLTransformerBuilder {

	public HypersonicTransformer(DB db) {
		super(db);

		register(
			bitwiseCheckDefaultTransformation, booleanTransformation,
			castClobTextTransformation, _castLongTransformation,
			castTextTransformation, crossJoinDefaultTransformation,
			inStrDefaultTransformation, integerDivisionTransformation,
			nullDateTransformation, substrDefaultTransformation);
	}

	@Override
	protected String replaceCastText(Matcher matcher) {
		return matcher.replaceAll("CONVERT($1, SQL_VARCHAR)");
	}

	private final Function<String, String> _castLongTransformation =
		(String sql) -> {
			Matcher matcher = castLongPattern.matcher(sql);

			return matcher.replaceAll("CONVERT($1, SQL_BIGINT)");
		};

}