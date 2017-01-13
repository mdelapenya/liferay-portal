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
import com.liferay.portal.kernel.util.StringUtil;

import java.util.function.Function;
import java.util.regex.Matcher;

/**
 * @author Manuel de la Peña
 */
public class OracleTransformer extends SQLTransformerBuilder {

	public OracleTransformer(DB db) {
		super(db);

		register(
			bitwiseCheckDefaultTransformation, booleanTransformation,
			_castClobTextTransformation, castLongTransformation,
			castTextTransformation, crossJoinDefaultTransformation,
			inStrDefaultTransformation, _integerDivisionTransformation,
			nullDateTransformation, substrDefaultTransformation,
			_escapeTransformation, _notEqualsBlankStringTransformation);
	}

	@Override
	protected String replaceCastText(Matcher matcher) {
		return matcher.replaceAll("CAST($1 AS VARCHAR(4000))");
	}

	private final Function<String, String> _castClobTextTransformation =
		(String sql) -> {
			Matcher matcher = castClobTextPattern.matcher(sql);

			return matcher.replaceAll("DBMS_LOB.SUBSTR($1, 4000, 1)");
		};

	private final Function<String, String> _escapeTransformation =
		(String sql) -> StringUtil.replace(sql, "LIKE ?", "LIKE ? ESCAPE '\\'");

	private final Function<String, String> _integerDivisionTransformation =
		(String sql) -> {
			Matcher matcher = integerDivisionPattern.matcher(sql);

			return matcher.replaceAll("TRUNC($1 / $2)");
		};

	private final Function<String, String> _notEqualsBlankStringTransformation =
		(String sql) -> StringUtil.replace(sql, " != ''", " IS NOT NULL");

}