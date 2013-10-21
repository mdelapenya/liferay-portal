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

import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnmodifiableList;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import com.liferay.portlet.asset.NoSuchEntryStatsException;
import com.liferay.portlet.asset.model.AssetEntryStats;
import com.liferay.portlet.asset.model.impl.AssetEntryStatsImpl;
import com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The persistence implementation for the asset entry stats service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetEntryStatsPersistence
 * @see AssetEntryStatsUtil
 * @generated
 */
public class AssetEntryStatsPersistenceImpl extends BasePersistenceImpl<AssetEntryStats>
	implements AssetEntryStatsPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link AssetEntryStatsUtil} to access the asset entry stats persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = AssetEntryStatsImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED,
			AssetEntryStatsImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED,
			AssetEntryStatsImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_FETCH_BY_C_C_D_M_Y = new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED,
			AssetEntryStatsImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByC_C_D_M_Y",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				Integer.class.getName()
			},
			AssetEntryStatsModelImpl.CLASSNAMEID_COLUMN_BITMASK |
			AssetEntryStatsModelImpl.CLASSPK_COLUMN_BITMASK |
			AssetEntryStatsModelImpl.DAY_COLUMN_BITMASK |
			AssetEntryStatsModelImpl.MONTH_COLUMN_BITMASK |
			AssetEntryStatsModelImpl.YEAR_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_C_C_D_M_Y = new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_C_D_M_Y",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				Integer.class.getName()
			});

	/**
	 * Returns the asset entry stats where classNameId = &#63; and classPK = &#63; and day = &#63; and month = &#63; and year = &#63; or throws a {@link com.liferay.portlet.asset.NoSuchEntryStatsException} if it could not be found.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @return the matching asset entry stats
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats findByC_C_D_M_Y(long classNameId, long classPK,
		int day, int month, int year)
		throws NoSuchEntryStatsException, SystemException {
		AssetEntryStats assetEntryStats = fetchByC_C_D_M_Y(classNameId,
				classPK, day, month, year);

		if (assetEntryStats == null) {
			StringBundler msg = new StringBundler(12);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("classNameId=");
			msg.append(classNameId);

			msg.append(", classPK=");
			msg.append(classPK);

			msg.append(", day=");
			msg.append(day);

			msg.append(", month=");
			msg.append(month);

			msg.append(", year=");
			msg.append(year);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchEntryStatsException(msg.toString());
		}

		return assetEntryStats;
	}

	/**
	 * Returns the asset entry stats where classNameId = &#63; and classPK = &#63; and day = &#63; and month = &#63; and year = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @return the matching asset entry stats, or <code>null</code> if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats fetchByC_C_D_M_Y(long classNameId, long classPK,
		int day, int month, int year) throws SystemException {
		return fetchByC_C_D_M_Y(classNameId, classPK, day, month, year, true);
	}

	/**
	 * Returns the asset entry stats where classNameId = &#63; and classPK = &#63; and day = &#63; and month = &#63; and year = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching asset entry stats, or <code>null</code> if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats fetchByC_C_D_M_Y(long classNameId, long classPK,
		int day, int month, int year, boolean retrieveFromCache)
		throws SystemException {
		Object[] finderArgs = new Object[] {
				classNameId, classPK, day, month, year
			};

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_C_C_D_M_Y,
					finderArgs, this);
		}

		if (result instanceof AssetEntryStats) {
			AssetEntryStats assetEntryStats = (AssetEntryStats)result;

			if ((classNameId != assetEntryStats.getClassNameId()) ||
					(classPK != assetEntryStats.getClassPK()) ||
					(day != assetEntryStats.getDay()) ||
					(month != assetEntryStats.getMonth()) ||
					(year != assetEntryStats.getYear())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(7);

			query.append(_SQL_SELECT_ASSETENTRYSTATS_WHERE);

			query.append(_FINDER_COLUMN_C_C_D_M_Y_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_C_C_D_M_Y_CLASSPK_2);

			query.append(_FINDER_COLUMN_C_C_D_M_Y_DAY_2);

			query.append(_FINDER_COLUMN_C_C_D_M_Y_MONTH_2);

			query.append(_FINDER_COLUMN_C_C_D_M_Y_YEAR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(classNameId);

				qPos.add(classPK);

				qPos.add(day);

				qPos.add(month);

				qPos.add(year);

				List<AssetEntryStats> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_C_C_D_M_Y,
						finderArgs, list);
				}
				else {
					AssetEntryStats assetEntryStats = list.get(0);

					result = assetEntryStats;

					cacheResult(assetEntryStats);

					if ((assetEntryStats.getClassNameId() != classNameId) ||
							(assetEntryStats.getClassPK() != classPK) ||
							(assetEntryStats.getDay() != day) ||
							(assetEntryStats.getMonth() != month) ||
							(assetEntryStats.getYear() != year)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_C_C_D_M_Y,
							finderArgs, assetEntryStats);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_C_C_D_M_Y,
					finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (AssetEntryStats)result;
		}
	}

	/**
	 * Removes the asset entry stats where classNameId = &#63; and classPK = &#63; and day = &#63; and month = &#63; and year = &#63; from the database.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @return the asset entry stats that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats removeByC_C_D_M_Y(long classNameId, long classPK,
		int day, int month, int year)
		throws NoSuchEntryStatsException, SystemException {
		AssetEntryStats assetEntryStats = findByC_C_D_M_Y(classNameId, classPK,
				day, month, year);

		return remove(assetEntryStats);
	}

	/**
	 * Returns the number of asset entry statses where classNameId = &#63; and classPK = &#63; and day = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class p k
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @return the number of matching asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByC_C_D_M_Y(long classNameId, long classPK, int day,
		int month, int year) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_C_C_D_M_Y;

		Object[] finderArgs = new Object[] {
				classNameId, classPK, day, month, year
			};

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(6);

			query.append(_SQL_COUNT_ASSETENTRYSTATS_WHERE);

			query.append(_FINDER_COLUMN_C_C_D_M_Y_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_C_C_D_M_Y_CLASSPK_2);

			query.append(_FINDER_COLUMN_C_C_D_M_Y_DAY_2);

			query.append(_FINDER_COLUMN_C_C_D_M_Y_MONTH_2);

			query.append(_FINDER_COLUMN_C_C_D_M_Y_YEAR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(classNameId);

				qPos.add(classPK);

				qPos.add(day);

				qPos.add(month);

				qPos.add(year);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_C_C_D_M_Y_CLASSNAMEID_2 = "assetEntryStats.classNameId = ? AND ";
	private static final String _FINDER_COLUMN_C_C_D_M_Y_CLASSPK_2 = "assetEntryStats.classPK = ? AND ";
	private static final String _FINDER_COLUMN_C_C_D_M_Y_DAY_2 = "assetEntryStats.day = ? AND ";
	private static final String _FINDER_COLUMN_C_C_D_M_Y_MONTH_2 = "assetEntryStats.month = ? AND ";
	private static final String _FINDER_COLUMN_C_C_D_M_Y_YEAR_2 = "assetEntryStats.year = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_DATE = new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED,
			AssetEntryStatsImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByG_Date",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_DATE =
		new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED,
			AssetEntryStatsImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_Date",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), Integer.class.getName()
			},
			AssetEntryStatsModelImpl.GROUPID_COLUMN_BITMASK |
			AssetEntryStatsModelImpl.DAY_COLUMN_BITMASK |
			AssetEntryStatsModelImpl.MONTH_COLUMN_BITMASK |
			AssetEntryStatsModelImpl.YEAR_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_DATE = new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_Date",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), Integer.class.getName()
			});

	/**
	 * Returns all the asset entry statses where groupId = &#63; and day = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @return the matching asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<AssetEntryStats> findByG_Date(long groupId, int day, int month,
		int year) throws SystemException {
		return findByG_Date(groupId, day, month, year, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the asset entry statses where groupId = &#63; and day = &#63; and month = &#63; and year = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @param start the lower bound of the range of asset entry statses
	 * @param end the upper bound of the range of asset entry statses (not inclusive)
	 * @return the range of matching asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<AssetEntryStats> findByG_Date(long groupId, int day, int month,
		int year, int start, int end) throws SystemException {
		return findByG_Date(groupId, day, month, year, start, end, null);
	}

	/**
	 * Returns an ordered range of all the asset entry statses where groupId = &#63; and day = &#63; and month = &#63; and year = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @param start the lower bound of the range of asset entry statses
	 * @param end the upper bound of the range of asset entry statses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<AssetEntryStats> findByG_Date(long groupId, int day, int month,
		int year, int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_DATE;
			finderArgs = new Object[] { groupId, day, month, year };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_DATE;
			finderArgs = new Object[] {
					groupId, day, month, year,
					
					start, end, orderByComparator
				};
		}

		List<AssetEntryStats> list = (List<AssetEntryStats>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (AssetEntryStats assetEntryStats : list) {
				if ((groupId != assetEntryStats.getGroupId()) ||
						(day != assetEntryStats.getDay()) ||
						(month != assetEntryStats.getMonth()) ||
						(year != assetEntryStats.getYear())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(6 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(6);
			}

			query.append(_SQL_SELECT_ASSETENTRYSTATS_WHERE);

			query.append(_FINDER_COLUMN_G_DATE_GROUPID_2);

			query.append(_FINDER_COLUMN_G_DATE_DAY_2);

			query.append(_FINDER_COLUMN_G_DATE_MONTH_2);

			query.append(_FINDER_COLUMN_G_DATE_YEAR_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AssetEntryStatsModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(day);

				qPos.add(month);

				qPos.add(year);

				if (!pagination) {
					list = (List<AssetEntryStats>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<AssetEntryStats>(list);
				}
				else {
					list = (List<AssetEntryStats>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first asset entry stats in the ordered set where groupId = &#63; and day = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset entry stats
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats findByG_Date_First(long groupId, int day, int month,
		int year, OrderByComparator orderByComparator)
		throws NoSuchEntryStatsException, SystemException {
		AssetEntryStats assetEntryStats = fetchByG_Date_First(groupId, day,
				month, year, orderByComparator);

		if (assetEntryStats != null) {
			return assetEntryStats;
		}

		StringBundler msg = new StringBundler(10);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", day=");
		msg.append(day);

		msg.append(", month=");
		msg.append(month);

		msg.append(", year=");
		msg.append(year);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryStatsException(msg.toString());
	}

	/**
	 * Returns the first asset entry stats in the ordered set where groupId = &#63; and day = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset entry stats, or <code>null</code> if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats fetchByG_Date_First(long groupId, int day,
		int month, int year, OrderByComparator orderByComparator)
		throws SystemException {
		List<AssetEntryStats> list = findByG_Date(groupId, day, month, year, 0,
				1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last asset entry stats in the ordered set where groupId = &#63; and day = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset entry stats
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats findByG_Date_Last(long groupId, int day, int month,
		int year, OrderByComparator orderByComparator)
		throws NoSuchEntryStatsException, SystemException {
		AssetEntryStats assetEntryStats = fetchByG_Date_Last(groupId, day,
				month, year, orderByComparator);

		if (assetEntryStats != null) {
			return assetEntryStats;
		}

		StringBundler msg = new StringBundler(10);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", day=");
		msg.append(day);

		msg.append(", month=");
		msg.append(month);

		msg.append(", year=");
		msg.append(year);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryStatsException(msg.toString());
	}

	/**
	 * Returns the last asset entry stats in the ordered set where groupId = &#63; and day = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset entry stats, or <code>null</code> if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats fetchByG_Date_Last(long groupId, int day, int month,
		int year, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByG_Date(groupId, day, month, year);

		if (count == 0) {
			return null;
		}

		List<AssetEntryStats> list = findByG_Date(groupId, day, month, year,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the asset entry statses before and after the current asset entry stats in the ordered set where groupId = &#63; and day = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param entryStatsId the primary key of the current asset entry stats
	 * @param groupId the group ID
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next asset entry stats
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a asset entry stats with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats[] findByG_Date_PrevAndNext(long entryStatsId,
		long groupId, int day, int month, int year,
		OrderByComparator orderByComparator)
		throws NoSuchEntryStatsException, SystemException {
		AssetEntryStats assetEntryStats = findByPrimaryKey(entryStatsId);

		Session session = null;

		try {
			session = openSession();

			AssetEntryStats[] array = new AssetEntryStatsImpl[3];

			array[0] = getByG_Date_PrevAndNext(session, assetEntryStats,
					groupId, day, month, year, orderByComparator, true);

			array[1] = assetEntryStats;

			array[2] = getByG_Date_PrevAndNext(session, assetEntryStats,
					groupId, day, month, year, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AssetEntryStats getByG_Date_PrevAndNext(Session session,
		AssetEntryStats assetEntryStats, long groupId, int day, int month,
		int year, OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ASSETENTRYSTATS_WHERE);

		query.append(_FINDER_COLUMN_G_DATE_GROUPID_2);

		query.append(_FINDER_COLUMN_G_DATE_DAY_2);

		query.append(_FINDER_COLUMN_G_DATE_MONTH_2);

		query.append(_FINDER_COLUMN_G_DATE_YEAR_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(AssetEntryStatsModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		qPos.add(day);

		qPos.add(month);

		qPos.add(year);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(assetEntryStats);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AssetEntryStats> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the asset entry statses where groupId = &#63; and day = &#63; and month = &#63; and year = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByG_Date(long groupId, int day, int month, int year)
		throws SystemException {
		for (AssetEntryStats assetEntryStats : findByG_Date(groupId, day,
				month, year, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(assetEntryStats);
		}
	}

	/**
	 * Returns the number of asset entry statses where groupId = &#63; and day = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param day the day
	 * @param month the month
	 * @param year the year
	 * @return the number of matching asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByG_Date(long groupId, int day, int month, int year)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_DATE;

		Object[] finderArgs = new Object[] { groupId, day, month, year };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(5);

			query.append(_SQL_COUNT_ASSETENTRYSTATS_WHERE);

			query.append(_FINDER_COLUMN_G_DATE_GROUPID_2);

			query.append(_FINDER_COLUMN_G_DATE_DAY_2);

			query.append(_FINDER_COLUMN_G_DATE_MONTH_2);

			query.append(_FINDER_COLUMN_G_DATE_YEAR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(day);

				qPos.add(month);

				qPos.add(year);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_DATE_GROUPID_2 = "assetEntryStats.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_DATE_DAY_2 = "assetEntryStats.day = ? AND ";
	private static final String _FINDER_COLUMN_G_DATE_MONTH_2 = "assetEntryStats.month = ? AND ";
	private static final String _FINDER_COLUMN_G_DATE_YEAR_2 = "assetEntryStats.year = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_MONTH = new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED,
			AssetEntryStatsImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByG_Month",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_MONTH =
		new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED,
			AssetEntryStatsImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_Month",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName()
			},
			AssetEntryStatsModelImpl.GROUPID_COLUMN_BITMASK |
			AssetEntryStatsModelImpl.MONTH_COLUMN_BITMASK |
			AssetEntryStatsModelImpl.YEAR_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_MONTH = new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_Month",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName()
			});

	/**
	 * Returns all the asset entry statses where groupId = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @return the matching asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<AssetEntryStats> findByG_Month(long groupId, int month, int year)
		throws SystemException {
		return findByG_Month(groupId, month, year, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the asset entry statses where groupId = &#63; and month = &#63; and year = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @param start the lower bound of the range of asset entry statses
	 * @param end the upper bound of the range of asset entry statses (not inclusive)
	 * @return the range of matching asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<AssetEntryStats> findByG_Month(long groupId, int month,
		int year, int start, int end) throws SystemException {
		return findByG_Month(groupId, month, year, start, end, null);
	}

	/**
	 * Returns an ordered range of all the asset entry statses where groupId = &#63; and month = &#63; and year = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @param start the lower bound of the range of asset entry statses
	 * @param end the upper bound of the range of asset entry statses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<AssetEntryStats> findByG_Month(long groupId, int month,
		int year, int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_MONTH;
			finderArgs = new Object[] { groupId, month, year };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_MONTH;
			finderArgs = new Object[] {
					groupId, month, year,
					
					start, end, orderByComparator
				};
		}

		List<AssetEntryStats> list = (List<AssetEntryStats>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (AssetEntryStats assetEntryStats : list) {
				if ((groupId != assetEntryStats.getGroupId()) ||
						(month != assetEntryStats.getMonth()) ||
						(year != assetEntryStats.getYear())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(5 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_ASSETENTRYSTATS_WHERE);

			query.append(_FINDER_COLUMN_G_MONTH_GROUPID_2);

			query.append(_FINDER_COLUMN_G_MONTH_MONTH_2);

			query.append(_FINDER_COLUMN_G_MONTH_YEAR_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AssetEntryStatsModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(month);

				qPos.add(year);

				if (!pagination) {
					list = (List<AssetEntryStats>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<AssetEntryStats>(list);
				}
				else {
					list = (List<AssetEntryStats>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first asset entry stats in the ordered set where groupId = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset entry stats
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats findByG_Month_First(long groupId, int month,
		int year, OrderByComparator orderByComparator)
		throws NoSuchEntryStatsException, SystemException {
		AssetEntryStats assetEntryStats = fetchByG_Month_First(groupId, month,
				year, orderByComparator);

		if (assetEntryStats != null) {
			return assetEntryStats;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", month=");
		msg.append(month);

		msg.append(", year=");
		msg.append(year);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryStatsException(msg.toString());
	}

	/**
	 * Returns the first asset entry stats in the ordered set where groupId = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset entry stats, or <code>null</code> if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats fetchByG_Month_First(long groupId, int month,
		int year, OrderByComparator orderByComparator)
		throws SystemException {
		List<AssetEntryStats> list = findByG_Month(groupId, month, year, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last asset entry stats in the ordered set where groupId = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset entry stats
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats findByG_Month_Last(long groupId, int month,
		int year, OrderByComparator orderByComparator)
		throws NoSuchEntryStatsException, SystemException {
		AssetEntryStats assetEntryStats = fetchByG_Month_Last(groupId, month,
				year, orderByComparator);

		if (assetEntryStats != null) {
			return assetEntryStats;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", month=");
		msg.append(month);

		msg.append(", year=");
		msg.append(year);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryStatsException(msg.toString());
	}

	/**
	 * Returns the last asset entry stats in the ordered set where groupId = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset entry stats, or <code>null</code> if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats fetchByG_Month_Last(long groupId, int month,
		int year, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByG_Month(groupId, month, year);

		if (count == 0) {
			return null;
		}

		List<AssetEntryStats> list = findByG_Month(groupId, month, year,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the asset entry statses before and after the current asset entry stats in the ordered set where groupId = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param entryStatsId the primary key of the current asset entry stats
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next asset entry stats
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a asset entry stats with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats[] findByG_Month_PrevAndNext(long entryStatsId,
		long groupId, int month, int year, OrderByComparator orderByComparator)
		throws NoSuchEntryStatsException, SystemException {
		AssetEntryStats assetEntryStats = findByPrimaryKey(entryStatsId);

		Session session = null;

		try {
			session = openSession();

			AssetEntryStats[] array = new AssetEntryStatsImpl[3];

			array[0] = getByG_Month_PrevAndNext(session, assetEntryStats,
					groupId, month, year, orderByComparator, true);

			array[1] = assetEntryStats;

			array[2] = getByG_Month_PrevAndNext(session, assetEntryStats,
					groupId, month, year, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AssetEntryStats getByG_Month_PrevAndNext(Session session,
		AssetEntryStats assetEntryStats, long groupId, int month, int year,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ASSETENTRYSTATS_WHERE);

		query.append(_FINDER_COLUMN_G_MONTH_GROUPID_2);

		query.append(_FINDER_COLUMN_G_MONTH_MONTH_2);

		query.append(_FINDER_COLUMN_G_MONTH_YEAR_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(AssetEntryStatsModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		qPos.add(month);

		qPos.add(year);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(assetEntryStats);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AssetEntryStats> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the asset entry statses where groupId = &#63; and month = &#63; and year = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByG_Month(long groupId, int month, int year)
		throws SystemException {
		for (AssetEntryStats assetEntryStats : findByG_Month(groupId, month,
				year, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(assetEntryStats);
		}
	}

	/**
	 * Returns the number of asset entry statses where groupId = &#63; and month = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param month the month
	 * @param year the year
	 * @return the number of matching asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByG_Month(long groupId, int month, int year)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_MONTH;

		Object[] finderArgs = new Object[] { groupId, month, year };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_ASSETENTRYSTATS_WHERE);

			query.append(_FINDER_COLUMN_G_MONTH_GROUPID_2);

			query.append(_FINDER_COLUMN_G_MONTH_MONTH_2);

			query.append(_FINDER_COLUMN_G_MONTH_YEAR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(month);

				qPos.add(year);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_MONTH_GROUPID_2 = "assetEntryStats.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_MONTH_MONTH_2 = "assetEntryStats.month = ? AND ";
	private static final String _FINDER_COLUMN_G_MONTH_YEAR_2 = "assetEntryStats.year = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_G_YEAR = new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED,
			AssetEntryStatsImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByG_Year",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_YEAR =
		new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED,
			AssetEntryStatsImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_Year",
			new String[] { Long.class.getName(), Integer.class.getName() },
			AssetEntryStatsModelImpl.GROUPID_COLUMN_BITMASK |
			AssetEntryStatsModelImpl.YEAR_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_G_YEAR = new FinderPath(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_Year",
			new String[] { Long.class.getName(), Integer.class.getName() });

	/**
	 * Returns all the asset entry statses where groupId = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param year the year
	 * @return the matching asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<AssetEntryStats> findByG_Year(long groupId, int year)
		throws SystemException {
		return findByG_Year(groupId, year, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the asset entry statses where groupId = &#63; and year = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param year the year
	 * @param start the lower bound of the range of asset entry statses
	 * @param end the upper bound of the range of asset entry statses (not inclusive)
	 * @return the range of matching asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<AssetEntryStats> findByG_Year(long groupId, int year,
		int start, int end) throws SystemException {
		return findByG_Year(groupId, year, start, end, null);
	}

	/**
	 * Returns an ordered range of all the asset entry statses where groupId = &#63; and year = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param year the year
	 * @param start the lower bound of the range of asset entry statses
	 * @param end the upper bound of the range of asset entry statses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<AssetEntryStats> findByG_Year(long groupId, int year,
		int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_YEAR;
			finderArgs = new Object[] { groupId, year };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_G_YEAR;
			finderArgs = new Object[] {
					groupId, year,
					
					start, end, orderByComparator
				};
		}

		List<AssetEntryStats> list = (List<AssetEntryStats>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (AssetEntryStats assetEntryStats : list) {
				if ((groupId != assetEntryStats.getGroupId()) ||
						(year != assetEntryStats.getYear())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_ASSETENTRYSTATS_WHERE);

			query.append(_FINDER_COLUMN_G_YEAR_GROUPID_2);

			query.append(_FINDER_COLUMN_G_YEAR_YEAR_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(AssetEntryStatsModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(year);

				if (!pagination) {
					list = (List<AssetEntryStats>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<AssetEntryStats>(list);
				}
				else {
					list = (List<AssetEntryStats>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first asset entry stats in the ordered set where groupId = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset entry stats
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats findByG_Year_First(long groupId, int year,
		OrderByComparator orderByComparator)
		throws NoSuchEntryStatsException, SystemException {
		AssetEntryStats assetEntryStats = fetchByG_Year_First(groupId, year,
				orderByComparator);

		if (assetEntryStats != null) {
			return assetEntryStats;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", year=");
		msg.append(year);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryStatsException(msg.toString());
	}

	/**
	 * Returns the first asset entry stats in the ordered set where groupId = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching asset entry stats, or <code>null</code> if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats fetchByG_Year_First(long groupId, int year,
		OrderByComparator orderByComparator) throws SystemException {
		List<AssetEntryStats> list = findByG_Year(groupId, year, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last asset entry stats in the ordered set where groupId = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset entry stats
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats findByG_Year_Last(long groupId, int year,
		OrderByComparator orderByComparator)
		throws NoSuchEntryStatsException, SystemException {
		AssetEntryStats assetEntryStats = fetchByG_Year_Last(groupId, year,
				orderByComparator);

		if (assetEntryStats != null) {
			return assetEntryStats;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", year=");
		msg.append(year);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryStatsException(msg.toString());
	}

	/**
	 * Returns the last asset entry stats in the ordered set where groupId = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching asset entry stats, or <code>null</code> if a matching asset entry stats could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats fetchByG_Year_Last(long groupId, int year,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByG_Year(groupId, year);

		if (count == 0) {
			return null;
		}

		List<AssetEntryStats> list = findByG_Year(groupId, year, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the asset entry statses before and after the current asset entry stats in the ordered set where groupId = &#63; and year = &#63;.
	 *
	 * @param entryStatsId the primary key of the current asset entry stats
	 * @param groupId the group ID
	 * @param year the year
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next asset entry stats
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a asset entry stats with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats[] findByG_Year_PrevAndNext(long entryStatsId,
		long groupId, int year, OrderByComparator orderByComparator)
		throws NoSuchEntryStatsException, SystemException {
		AssetEntryStats assetEntryStats = findByPrimaryKey(entryStatsId);

		Session session = null;

		try {
			session = openSession();

			AssetEntryStats[] array = new AssetEntryStatsImpl[3];

			array[0] = getByG_Year_PrevAndNext(session, assetEntryStats,
					groupId, year, orderByComparator, true);

			array[1] = assetEntryStats;

			array[2] = getByG_Year_PrevAndNext(session, assetEntryStats,
					groupId, year, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AssetEntryStats getByG_Year_PrevAndNext(Session session,
		AssetEntryStats assetEntryStats, long groupId, int year,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_ASSETENTRYSTATS_WHERE);

		query.append(_FINDER_COLUMN_G_YEAR_GROUPID_2);

		query.append(_FINDER_COLUMN_G_YEAR_YEAR_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(AssetEntryStatsModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		qPos.add(year);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(assetEntryStats);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AssetEntryStats> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the asset entry statses where groupId = &#63; and year = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param year the year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByG_Year(long groupId, int year)
		throws SystemException {
		for (AssetEntryStats assetEntryStats : findByG_Year(groupId, year,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(assetEntryStats);
		}
	}

	/**
	 * Returns the number of asset entry statses where groupId = &#63; and year = &#63;.
	 *
	 * @param groupId the group ID
	 * @param year the year
	 * @return the number of matching asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByG_Year(long groupId, int year) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_G_YEAR;

		Object[] finderArgs = new Object[] { groupId, year };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_ASSETENTRYSTATS_WHERE);

			query.append(_FINDER_COLUMN_G_YEAR_GROUPID_2);

			query.append(_FINDER_COLUMN_G_YEAR_YEAR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				qPos.add(year);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_G_YEAR_GROUPID_2 = "assetEntryStats.groupId = ? AND ";
	private static final String _FINDER_COLUMN_G_YEAR_YEAR_2 = "assetEntryStats.year = ?";

	public AssetEntryStatsPersistenceImpl() {
		setModelClass(AssetEntryStats.class);
	}

	/**
	 * Caches the asset entry stats in the entity cache if it is enabled.
	 *
	 * @param assetEntryStats the asset entry stats
	 */
	@Override
	public void cacheResult(AssetEntryStats assetEntryStats) {
		EntityCacheUtil.putResult(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsImpl.class, assetEntryStats.getPrimaryKey(),
			assetEntryStats);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_C_C_D_M_Y,
			new Object[] {
				assetEntryStats.getClassNameId(), assetEntryStats.getClassPK(),
				assetEntryStats.getDay(), assetEntryStats.getMonth(),
				assetEntryStats.getYear()
			}, assetEntryStats);

		assetEntryStats.resetOriginalValues();
	}

	/**
	 * Caches the asset entry statses in the entity cache if it is enabled.
	 *
	 * @param assetEntryStatses the asset entry statses
	 */
	@Override
	public void cacheResult(List<AssetEntryStats> assetEntryStatses) {
		for (AssetEntryStats assetEntryStats : assetEntryStatses) {
			if (EntityCacheUtil.getResult(
						AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
						AssetEntryStatsImpl.class,
						assetEntryStats.getPrimaryKey()) == null) {
				cacheResult(assetEntryStats);
			}
			else {
				assetEntryStats.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all asset entry statses.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(AssetEntryStatsImpl.class.getName());
		}

		EntityCacheUtil.clearCache(AssetEntryStatsImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the asset entry stats.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(AssetEntryStats assetEntryStats) {
		EntityCacheUtil.removeResult(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsImpl.class, assetEntryStats.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(assetEntryStats);
	}

	@Override
	public void clearCache(List<AssetEntryStats> assetEntryStatses) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (AssetEntryStats assetEntryStats : assetEntryStatses) {
			EntityCacheUtil.removeResult(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
				AssetEntryStatsImpl.class, assetEntryStats.getPrimaryKey());

			clearUniqueFindersCache(assetEntryStats);
		}
	}

	protected void cacheUniqueFindersCache(AssetEntryStats assetEntryStats) {
		if (assetEntryStats.isNew()) {
			Object[] args = new Object[] {
					assetEntryStats.getClassNameId(),
					assetEntryStats.getClassPK(), assetEntryStats.getDay(),
					assetEntryStats.getMonth(), assetEntryStats.getYear()
				};

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_C_C_D_M_Y, args,
				Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_C_C_D_M_Y, args,
				assetEntryStats);
		}
		else {
			AssetEntryStatsModelImpl assetEntryStatsModelImpl = (AssetEntryStatsModelImpl)assetEntryStats;

			if ((assetEntryStatsModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_C_C_D_M_Y.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						assetEntryStats.getClassNameId(),
						assetEntryStats.getClassPK(), assetEntryStats.getDay(),
						assetEntryStats.getMonth(), assetEntryStats.getYear()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_C_C_D_M_Y, args,
					Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_C_C_D_M_Y, args,
					assetEntryStats);
			}
		}
	}

	protected void clearUniqueFindersCache(AssetEntryStats assetEntryStats) {
		AssetEntryStatsModelImpl assetEntryStatsModelImpl = (AssetEntryStatsModelImpl)assetEntryStats;

		Object[] args = new Object[] {
				assetEntryStats.getClassNameId(), assetEntryStats.getClassPK(),
				assetEntryStats.getDay(), assetEntryStats.getMonth(),
				assetEntryStats.getYear()
			};

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_C_C_D_M_Y, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_C_C_D_M_Y, args);

		if ((assetEntryStatsModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_C_C_D_M_Y.getColumnBitmask()) != 0) {
			args = new Object[] {
					assetEntryStatsModelImpl.getOriginalClassNameId(),
					assetEntryStatsModelImpl.getOriginalClassPK(),
					assetEntryStatsModelImpl.getOriginalDay(),
					assetEntryStatsModelImpl.getOriginalMonth(),
					assetEntryStatsModelImpl.getOriginalYear()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_C_C_D_M_Y, args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_C_C_D_M_Y, args);
		}
	}

	/**
	 * Creates a new asset entry stats with the primary key. Does not add the asset entry stats to the database.
	 *
	 * @param entryStatsId the primary key for the new asset entry stats
	 * @return the new asset entry stats
	 */
	@Override
	public AssetEntryStats create(long entryStatsId) {
		AssetEntryStats assetEntryStats = new AssetEntryStatsImpl();

		assetEntryStats.setNew(true);
		assetEntryStats.setPrimaryKey(entryStatsId);

		return assetEntryStats;
	}

	/**
	 * Removes the asset entry stats with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryStatsId the primary key of the asset entry stats
	 * @return the asset entry stats that was removed
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a asset entry stats with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats remove(long entryStatsId)
		throws NoSuchEntryStatsException, SystemException {
		return remove((Serializable)entryStatsId);
	}

	/**
	 * Removes the asset entry stats with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the asset entry stats
	 * @return the asset entry stats that was removed
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a asset entry stats with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats remove(Serializable primaryKey)
		throws NoSuchEntryStatsException, SystemException {
		Session session = null;

		try {
			session = openSession();

			AssetEntryStats assetEntryStats = (AssetEntryStats)session.get(AssetEntryStatsImpl.class,
					primaryKey);

			if (assetEntryStats == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchEntryStatsException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(assetEntryStats);
		}
		catch (NoSuchEntryStatsException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected AssetEntryStats removeImpl(AssetEntryStats assetEntryStats)
		throws SystemException {
		assetEntryStats = toUnwrappedModel(assetEntryStats);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(assetEntryStats)) {
				assetEntryStats = (AssetEntryStats)session.get(AssetEntryStatsImpl.class,
						assetEntryStats.getPrimaryKeyObj());
			}

			if (assetEntryStats != null) {
				session.delete(assetEntryStats);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (assetEntryStats != null) {
			clearCache(assetEntryStats);
		}

		return assetEntryStats;
	}

	@Override
	public AssetEntryStats updateImpl(
		com.liferay.portlet.asset.model.AssetEntryStats assetEntryStats)
		throws SystemException {
		assetEntryStats = toUnwrappedModel(assetEntryStats);

		boolean isNew = assetEntryStats.isNew();

		AssetEntryStatsModelImpl assetEntryStatsModelImpl = (AssetEntryStatsModelImpl)assetEntryStats;

		Session session = null;

		try {
			session = openSession();

			if (assetEntryStats.isNew()) {
				session.save(assetEntryStats);

				assetEntryStats.setNew(false);
			}
			else {
				session.merge(assetEntryStats);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !AssetEntryStatsModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((assetEntryStatsModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_DATE.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						assetEntryStatsModelImpl.getOriginalGroupId(),
						assetEntryStatsModelImpl.getOriginalDay(),
						assetEntryStatsModelImpl.getOriginalMonth(),
						assetEntryStatsModelImpl.getOriginalYear()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_DATE, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_DATE,
					args);

				args = new Object[] {
						assetEntryStatsModelImpl.getGroupId(),
						assetEntryStatsModelImpl.getDay(),
						assetEntryStatsModelImpl.getMonth(),
						assetEntryStatsModelImpl.getYear()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_DATE, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_DATE,
					args);
			}

			if ((assetEntryStatsModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_MONTH.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						assetEntryStatsModelImpl.getOriginalGroupId(),
						assetEntryStatsModelImpl.getOriginalMonth(),
						assetEntryStatsModelImpl.getOriginalYear()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_MONTH, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_MONTH,
					args);

				args = new Object[] {
						assetEntryStatsModelImpl.getGroupId(),
						assetEntryStatsModelImpl.getMonth(),
						assetEntryStatsModelImpl.getYear()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_MONTH, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_MONTH,
					args);
			}

			if ((assetEntryStatsModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_YEAR.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						assetEntryStatsModelImpl.getOriginalGroupId(),
						assetEntryStatsModelImpl.getOriginalYear()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_YEAR, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_YEAR,
					args);

				args = new Object[] {
						assetEntryStatsModelImpl.getGroupId(),
						assetEntryStatsModelImpl.getYear()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_G_YEAR, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_G_YEAR,
					args);
			}
		}

		EntityCacheUtil.putResult(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
			AssetEntryStatsImpl.class, assetEntryStats.getPrimaryKey(),
			assetEntryStats);

		clearUniqueFindersCache(assetEntryStats);
		cacheUniqueFindersCache(assetEntryStats);

		return assetEntryStats;
	}

	protected AssetEntryStats toUnwrappedModel(AssetEntryStats assetEntryStats) {
		if (assetEntryStats instanceof AssetEntryStatsImpl) {
			return assetEntryStats;
		}

		AssetEntryStatsImpl assetEntryStatsImpl = new AssetEntryStatsImpl();

		assetEntryStatsImpl.setNew(assetEntryStats.isNew());
		assetEntryStatsImpl.setPrimaryKey(assetEntryStats.getPrimaryKey());

		assetEntryStatsImpl.setEntryStatsId(assetEntryStats.getEntryStatsId());
		assetEntryStatsImpl.setGroupId(assetEntryStats.getGroupId());
		assetEntryStatsImpl.setCompanyId(assetEntryStats.getCompanyId());
		assetEntryStatsImpl.setUserId(assetEntryStats.getUserId());
		assetEntryStatsImpl.setUserName(assetEntryStats.getUserName());
		assetEntryStatsImpl.setCreateDate(assetEntryStats.getCreateDate());
		assetEntryStatsImpl.setModifiedDate(assetEntryStats.getModifiedDate());
		assetEntryStatsImpl.setClassNameId(assetEntryStats.getClassNameId());
		assetEntryStatsImpl.setClassPK(assetEntryStats.getClassPK());
		assetEntryStatsImpl.setDay(assetEntryStats.getDay());
		assetEntryStatsImpl.setMonth(assetEntryStats.getMonth());
		assetEntryStatsImpl.setYear(assetEntryStats.getYear());
		assetEntryStatsImpl.setViewCount(assetEntryStats.getViewCount());

		return assetEntryStatsImpl;
	}

	/**
	 * Returns the asset entry stats with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the asset entry stats
	 * @return the asset entry stats
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a asset entry stats with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats findByPrimaryKey(Serializable primaryKey)
		throws NoSuchEntryStatsException, SystemException {
		AssetEntryStats assetEntryStats = fetchByPrimaryKey(primaryKey);

		if (assetEntryStats == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchEntryStatsException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return assetEntryStats;
	}

	/**
	 * Returns the asset entry stats with the primary key or throws a {@link com.liferay.portlet.asset.NoSuchEntryStatsException} if it could not be found.
	 *
	 * @param entryStatsId the primary key of the asset entry stats
	 * @return the asset entry stats
	 * @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a asset entry stats with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats findByPrimaryKey(long entryStatsId)
		throws NoSuchEntryStatsException, SystemException {
		return findByPrimaryKey((Serializable)entryStatsId);
	}

	/**
	 * Returns the asset entry stats with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the asset entry stats
	 * @return the asset entry stats, or <code>null</code> if a asset entry stats with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		AssetEntryStats assetEntryStats = (AssetEntryStats)EntityCacheUtil.getResult(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
				AssetEntryStatsImpl.class, primaryKey);

		if (assetEntryStats == _nullAssetEntryStats) {
			return null;
		}

		if (assetEntryStats == null) {
			Session session = null;

			try {
				session = openSession();

				assetEntryStats = (AssetEntryStats)session.get(AssetEntryStatsImpl.class,
						primaryKey);

				if (assetEntryStats != null) {
					cacheResult(assetEntryStats);
				}
				else {
					EntityCacheUtil.putResult(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
						AssetEntryStatsImpl.class, primaryKey,
						_nullAssetEntryStats);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(AssetEntryStatsModelImpl.ENTITY_CACHE_ENABLED,
					AssetEntryStatsImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return assetEntryStats;
	}

	/**
	 * Returns the asset entry stats with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param entryStatsId the primary key of the asset entry stats
	 * @return the asset entry stats, or <code>null</code> if a asset entry stats with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AssetEntryStats fetchByPrimaryKey(long entryStatsId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)entryStatsId);
	}

	/**
	 * Returns all the asset entry statses.
	 *
	 * @return the asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<AssetEntryStats> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the asset entry statses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of asset entry statses
	 * @param end the upper bound of the range of asset entry statses (not inclusive)
	 * @return the range of asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<AssetEntryStats> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the asset entry statses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of asset entry statses
	 * @param end the upper bound of the range of asset entry statses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<AssetEntryStats> findAll(int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<AssetEntryStats> list = (List<AssetEntryStats>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_ASSETENTRYSTATS);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_ASSETENTRYSTATS;

				if (pagination) {
					sql = sql.concat(AssetEntryStatsModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<AssetEntryStats>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<AssetEntryStats>(list);
				}
				else {
					list = (List<AssetEntryStats>)QueryUtil.list(q,
							getDialect(), start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the asset entry statses from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (AssetEntryStats assetEntryStats : findAll()) {
			remove(assetEntryStats);
		}
	}

	/**
	 * Returns the number of asset entry statses.
	 *
	 * @return the number of asset entry statses
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countAll() throws SystemException {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_ASSETENTRYSTATS);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Initializes the asset entry stats persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.portal.util.PropsUtil.get(
						"value.object.listener.com.liferay.portlet.asset.model.AssetEntryStats")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<AssetEntryStats>> listenersList = new ArrayList<ModelListener<AssetEntryStats>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<AssetEntryStats>)InstanceFactory.newInstance(
							getClassLoader(), listenerClassName));
				}

				listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}
	}

	public void destroy() {
		EntityCacheUtil.removeCache(AssetEntryStatsImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_ASSETENTRYSTATS = "SELECT assetEntryStats FROM AssetEntryStats assetEntryStats";
	private static final String _SQL_SELECT_ASSETENTRYSTATS_WHERE = "SELECT assetEntryStats FROM AssetEntryStats assetEntryStats WHERE ";
	private static final String _SQL_COUNT_ASSETENTRYSTATS = "SELECT COUNT(assetEntryStats) FROM AssetEntryStats assetEntryStats";
	private static final String _SQL_COUNT_ASSETENTRYSTATS_WHERE = "SELECT COUNT(assetEntryStats) FROM AssetEntryStats assetEntryStats WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "assetEntryStats.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No AssetEntryStats exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No AssetEntryStats exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = com.liferay.portal.util.PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE;
	private static Log _log = LogFactoryUtil.getLog(AssetEntryStatsPersistenceImpl.class);
	private static AssetEntryStats _nullAssetEntryStats = new AssetEntryStatsImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<AssetEntryStats> toCacheModel() {
				return _nullAssetEntryStatsCacheModel;
			}
		};

	private static CacheModel<AssetEntryStats> _nullAssetEntryStatsCacheModel = new CacheModel<AssetEntryStats>() {
			@Override
			public AssetEntryStats toEntityModel() {
				return _nullAssetEntryStats;
			}
		};
}