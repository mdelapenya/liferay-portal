/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.asset.service.persistence;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portlet.asset.model.AssetEntryStats;
import com.liferay.util.dao.orm.CustomSQLUtil;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Manuel de la Peña
 */
public class AssetEntryStatsFinderImpl
	extends BasePersistenceImpl<AssetEntryStats>
	implements AssetEntryStatsFinder {

	public static final String SUM_BY_G_DATE =
		AssetEntryStatsFinder.class.getName() + ".sumByG_Date";

	public static final String SUM_BY_G_MONTH =
		AssetEntryStatsFinder.class.getName() + ".sumByG_Month";

	public static final String SUM_BY_G_YEAR =
		AssetEntryStatsFinder.class.getName() + ".sumByG_Year";

	public long sumByG_C_C_Date(
			long groupId, long classNameId, long classPK, int day, int month,
			int year)
		throws SystemException {

		return sumByG(
			groupId, classNameId, classPK, SUM_BY_G_DATE, day, month, year);
	}

	public long sumByG_C_C_Month(
			long groupId, long classNameId, long classPK, int month, int year)
		throws SystemException {

		return sumByG(
			groupId, classNameId, classPK, SUM_BY_G_MONTH, month, year);
	}

	public long sumByG_C_C_Year(
			long groupId, long classNameId, long classPK, int year)
		throws SystemException {

		return sumByG(groupId, classNameId, classPK, SUM_BY_G_YEAR, year);
	}

	public long sumByG_C_Date(
			long groupId, long classNameId, int day, int month, int year)
		throws SystemException {

		return sumByG(groupId, classNameId, SUM_BY_G_DATE, day, month, year);
	}

	public long sumByG_C_Month(
			long groupId, long classNameId, int month, int year)
		throws SystemException {

		return sumByG(groupId, classNameId, SUM_BY_G_MONTH, month, year);
	}

	public long sumByG_C_Year(long groupId, long classNameId, int year)
		throws SystemException {

		return sumByG(groupId, classNameId, SUM_BY_G_YEAR, year);
	}

	public long sumByG_Date(long groupId, int day, int month, int year)
		throws SystemException {

		return sumByG(groupId, SUM_BY_G_DATE, day, month, year);
	}

	public long sumByG_Month(long groupId, int month, int year)
		throws SystemException {

		return sumByG(groupId, SUM_BY_G_MONTH, month, year);
	}

	public long sumByG_Year(long groupId, int year) throws SystemException {
		return sumByG(groupId, SUM_BY_G_YEAR, year);
	}

	protected String getWhere(LinkedHashMap<String, Object> params) {
		if ((params == null) || params.isEmpty()) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(params.size());

		for (Map.Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();

			if (key.equals("classNameId")) {
				sb.append(StringPool.OPEN_PARENTHESIS);
				sb.append("(AssetEntryStats.classNameId = ?)");
				sb.append(") AND ");
			}
			else if (key.equals("classPK")) {
				sb.append(StringPool.OPEN_PARENTHESIS);
				sb.append("(Group_.treePath LIKE ?) ");
				sb.append(") AND ");
			}
		}

		return sb.toString();
	}

	protected String replaceWhere(
		String sql, LinkedHashMap<String, Object> params) {

		if (params.isEmpty()) {
			return StringUtil.replace(
				sql,
				new String[] {
					"[$WHERE$]"
				},
				new String[] {
					StringPool.BLANK
				});
		}

		String cacheKey = _getCacheKey(sql, params);

		String resultSQL = _replaceWhereSQLCache.get(cacheKey);

		if (resultSQL == null) {
			resultSQL = StringUtil.replace(sql, "[$WHERE$]", getWhere(params));

			_replaceWhereSQLCache.put(cacheKey, resultSQL);
		}

		return resultSQL;
	}

	protected long sumByG(
			long groupId, long classNameId, long classPK, String customSQL,
			int... args)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(customSQL);

			LinkedHashMap<String, Object> params =
				new LinkedHashMap<String, Object>();

			if (classNameId > 0) {
				params.put("classNameId", classNameId);
			}

			if (classPK > 0) {
				params.put("classPK", classPK);
			}

			sql = replaceWhere(sql, params);

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar("SUM", Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			if (classNameId > 0) {
				qPos.add(classNameId);
			}

			if (classPK > 0) {
				qPos.add(classPK);
			}

			for (int param : args) {
				qPos.add(param);
			}

			Iterator<Long> itr = q.iterate();

			if (itr.hasNext()) {
				Long sum = itr.next();

				if (sum != null) {
					return sum.intValue();
				}
			}

			return 0;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected long sumByG(
			long groupId, long classNameId, String customSQL, int... args)
		throws SystemException {

		return sumByG(groupId, classNameId, 0, customSQL, args);
	}

	protected long sumByG(long groupId, String customSQL, int... args)
		throws SystemException {

		return sumByG(groupId, 0, 0, customSQL, args);
	}

	private String _getCacheKey(
		String sql, LinkedHashMap<String, Object> params) {

		StringBundler sb = new StringBundler();

		sb.append(sql);

		for (Map.Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();

			sb.append(key);
		}

		return sb.toString();
	}

	private Map<String, String> _replaceWhereSQLCache =
		new ConcurrentHashMap<String, String>();

}