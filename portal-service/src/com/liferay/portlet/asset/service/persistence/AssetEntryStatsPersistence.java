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

import com.liferay.portal.service.persistence.BasePersistence;

import com.liferay.portlet.asset.model.AssetEntryStats;

/**
 * The persistence interface for the asset entry stats service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetEntryStatsPersistenceImpl
 * @see AssetEntryStatsUtil
 * @generated
 */
public interface AssetEntryStatsPersistence extends BasePersistence<AssetEntryStats> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link AssetEntryStatsUtil} to access the asset entry stats persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

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
	public com.liferay.portlet.asset.model.AssetEntryStats findByC_C_D_M_Y(
		long classNameId, long classPK, int day, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portlet.asset.NoSuchEntryStatsException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats fetchByC_C_D_M_Y(
		long classNameId, long classPK, int day, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats fetchByC_C_D_M_Y(
		long classNameId, long classPK, int day, int month, int year,
		boolean retrieveFromCache)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats removeByC_C_D_M_Y(
		long classNameId, long classPK, int day, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portlet.asset.NoSuchEntryStatsException;

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
	public int countByC_C_D_M_Y(long classNameId, long classPK, int day,
		int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> findByG_Date(
		long groupId, int day, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> findByG_Date(
		long groupId, int day, int month, int year, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> findByG_Date(
		long groupId, int day, int month, int year, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats findByG_Date_First(
		long groupId, int day, int month, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portlet.asset.NoSuchEntryStatsException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats fetchByG_Date_First(
		long groupId, int day, int month, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats findByG_Date_Last(
		long groupId, int day, int month, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portlet.asset.NoSuchEntryStatsException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats fetchByG_Date_Last(
		long groupId, int day, int month, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats[] findByG_Date_PrevAndNext(
		long entryStatsId, long groupId, int day, int month, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portlet.asset.NoSuchEntryStatsException;

	/**
	* Removes all the asset entry statses where groupId = &#63; and day = &#63; and month = &#63; and year = &#63; from the database.
	*
	* @param groupId the group ID
	* @param day the day
	* @param month the month
	* @param year the year
	* @throws SystemException if a system exception occurred
	*/
	public void removeByG_Date(long groupId, int day, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public int countByG_Date(long groupId, int day, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns all the asset entry statses where groupId = &#63; and month = &#63; and year = &#63;.
	*
	* @param groupId the group ID
	* @param month the month
	* @param year the year
	* @return the matching asset entry statses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> findByG_Month(
		long groupId, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> findByG_Month(
		long groupId, int month, int year, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> findByG_Month(
		long groupId, int month, int year, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats findByG_Month_First(
		long groupId, int month, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portlet.asset.NoSuchEntryStatsException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats fetchByG_Month_First(
		long groupId, int month, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats findByG_Month_Last(
		long groupId, int month, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portlet.asset.NoSuchEntryStatsException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats fetchByG_Month_Last(
		long groupId, int month, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats[] findByG_Month_PrevAndNext(
		long entryStatsId, long groupId, int month, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portlet.asset.NoSuchEntryStatsException;

	/**
	* Removes all the asset entry statses where groupId = &#63; and month = &#63; and year = &#63; from the database.
	*
	* @param groupId the group ID
	* @param month the month
	* @param year the year
	* @throws SystemException if a system exception occurred
	*/
	public void removeByG_Month(long groupId, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of asset entry statses where groupId = &#63; and month = &#63; and year = &#63;.
	*
	* @param groupId the group ID
	* @param month the month
	* @param year the year
	* @return the number of matching asset entry statses
	* @throws SystemException if a system exception occurred
	*/
	public int countByG_Month(long groupId, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns all the asset entry statses where groupId = &#63; and year = &#63;.
	*
	* @param groupId the group ID
	* @param year the year
	* @return the matching asset entry statses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> findByG_Year(
		long groupId, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> findByG_Year(
		long groupId, int year, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> findByG_Year(
		long groupId, int year, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats findByG_Year_First(
		long groupId, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portlet.asset.NoSuchEntryStatsException;

	/**
	* Returns the first asset entry stats in the ordered set where groupId = &#63; and year = &#63;.
	*
	* @param groupId the group ID
	* @param year the year
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching asset entry stats, or <code>null</code> if a matching asset entry stats could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.asset.model.AssetEntryStats fetchByG_Year_First(
		long groupId, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats findByG_Year_Last(
		long groupId, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portlet.asset.NoSuchEntryStatsException;

	/**
	* Returns the last asset entry stats in the ordered set where groupId = &#63; and year = &#63;.
	*
	* @param groupId the group ID
	* @param year the year
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching asset entry stats, or <code>null</code> if a matching asset entry stats could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.asset.model.AssetEntryStats fetchByG_Year_Last(
		long groupId, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.liferay.portlet.asset.model.AssetEntryStats[] findByG_Year_PrevAndNext(
		long entryStatsId, long groupId, int year,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portlet.asset.NoSuchEntryStatsException;

	/**
	* Removes all the asset entry statses where groupId = &#63; and year = &#63; from the database.
	*
	* @param groupId the group ID
	* @param year the year
	* @throws SystemException if a system exception occurred
	*/
	public void removeByG_Year(long groupId, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of asset entry statses where groupId = &#63; and year = &#63;.
	*
	* @param groupId the group ID
	* @param year the year
	* @return the number of matching asset entry statses
	* @throws SystemException if a system exception occurred
	*/
	public int countByG_Year(long groupId, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Caches the asset entry stats in the entity cache if it is enabled.
	*
	* @param assetEntryStats the asset entry stats
	*/
	public void cacheResult(
		com.liferay.portlet.asset.model.AssetEntryStats assetEntryStats);

	/**
	* Caches the asset entry statses in the entity cache if it is enabled.
	*
	* @param assetEntryStatses the asset entry statses
	*/
	public void cacheResult(
		java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> assetEntryStatses);

	/**
	* Creates a new asset entry stats with the primary key. Does not add the asset entry stats to the database.
	*
	* @param entryStatsId the primary key for the new asset entry stats
	* @return the new asset entry stats
	*/
	public com.liferay.portlet.asset.model.AssetEntryStats create(
		long entryStatsId);

	/**
	* Removes the asset entry stats with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param entryStatsId the primary key of the asset entry stats
	* @return the asset entry stats that was removed
	* @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a asset entry stats with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.asset.model.AssetEntryStats remove(
		long entryStatsId)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portlet.asset.NoSuchEntryStatsException;

	public com.liferay.portlet.asset.model.AssetEntryStats updateImpl(
		com.liferay.portlet.asset.model.AssetEntryStats assetEntryStats)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the asset entry stats with the primary key or throws a {@link com.liferay.portlet.asset.NoSuchEntryStatsException} if it could not be found.
	*
	* @param entryStatsId the primary key of the asset entry stats
	* @return the asset entry stats
	* @throws com.liferay.portlet.asset.NoSuchEntryStatsException if a asset entry stats with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.asset.model.AssetEntryStats findByPrimaryKey(
		long entryStatsId)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portlet.asset.NoSuchEntryStatsException;

	/**
	* Returns the asset entry stats with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param entryStatsId the primary key of the asset entry stats
	* @return the asset entry stats, or <code>null</code> if a asset entry stats with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portlet.asset.model.AssetEntryStats fetchByPrimaryKey(
		long entryStatsId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns all the asset entry statses.
	*
	* @return the asset entry statses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> findAll()
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> findAll(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the asset entry statses from the database.
	*
	* @throws SystemException if a system exception occurred
	*/
	public void removeAll()
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of asset entry statses.
	*
	* @return the number of asset entry statses
	* @throws SystemException if a system exception occurred
	*/
	public int countAll()
		throws com.liferay.portal.kernel.exception.SystemException;
}