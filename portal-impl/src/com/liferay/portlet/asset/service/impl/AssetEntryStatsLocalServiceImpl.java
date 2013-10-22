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

package com.liferay.portlet.asset.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.asset.model.AssetEntryStats;
import com.liferay.portlet.asset.service.base.AssetEntryStatsLocalServiceBaseImpl;

/**
 * Provides the local service for accessing, deleting, updating, and validating
 * asset entries stats.
 *
 * @author Brian Wing Shun Chan
 * @author Manuel de la Peña
 */
public class AssetEntryStatsLocalServiceImpl
	extends AssetEntryStatsLocalServiceBaseImpl {

	@Override
	public AssetEntryStats fetchByC_C_Date(
			long classNameId, long classPK, int day, int month, int year)
		throws SystemException {

		return assetEntryStatsPersistence.fetchByC_C_D_M_Y(
			classNameId, classPK, day, month, year);
	}

	@Override
	public AssetEntryStats findByC_C_Date(
			long classNameId, long classPK, int day, int month, int year)
		throws PortalException, SystemException {

		return assetEntryStatsPersistence.findByC_C_D_M_Y(
			classNameId, classPK, day, month, year);
	}

	/**
	 * Remove the stats for an asset.
	 *
	 * @param classNameId the primary key of the class to remove its stats
	 * @param classPK the primary of the entity to remove its stats
	 * @throws SystemException
	 */
	@Override
	public void removeByC_C(long classNameId, long classPK)
		throws SystemException {

		assetEntryStatsPersistence.removeByC_C(classNameId, classPK);
	}

	/**
	 * Retrieves the sum of view counts for an asset, in an specific day of a
	 * year.
	 *
	 * @param  groupId the primary key of the web content article's group
	 * @param  classNameId the primary key of the class to search for
	 * @param  classPK the primary key of the entity to search for
	 * @param  day the day to search for
	 * @param  month the month to search for
	 * @param  year the year to search for
	 * @return the sum of all view counts for a classNameId, classPK, in an
	 *         specific day of a year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long sumByC_C_Date(
			long groupId, long classNameId, long classPK, int day, int month,
			int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_C_C_Date(
			groupId, classNameId, classPK, day, month, year);
	}

	/**
	 * Retrieves the sum of view counts for an asset, in an specific month of a
	 * year.
	 *
	 * @param  groupId the primary key of the web content article's group
	 * @param  classNameId the primary key of the class to search for
	 * @param  classPK the primary key of the entity to search for
	 * @param  month the month to search for
	 * @param  year the year to search for
	 * @return the sum of all view counts for a classNameId, classPK, in an
	 *         specific month of a year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long sumByC_C_Month(
			long groupId, long classNameId, long classPK, int month, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_C_C_Month(
			groupId, classNameId, classPK, month, year);
	}

	/**
	 * Retrieves the sum of view counts for an asset, in an specific year.
	 *
	 * @param  groupId the primary key of the web content article's group
	 * @param  classNameId the primary key of the class to search for
	 * @param  classPK the primary key of the entity to search for
	 * @param  year the year to search for
	 * @return the sum of all view counts for a classNameId, classPK, in an
	 *         specific year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long sumByC_C_Year(
			long groupId, long classNameId, long classPK, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_C_C_Year(
			groupId, classNameId, classPK, year);
	}

	/**
	 * Retrieves the sum of view counts for a type of assets, in an specific
	 * day of a year.
	 *
	 * @param  groupId the primary key of the web content article's group
	 * @param  classNameId the primary key of the class to search for
	 * @param  day the day to search for
	 * @param  month the month to search for
	 * @param  year the year to search for
	 * @return the sum of all view counts for a classNameId, in an specific day
	 *         of a year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long sumByC_Date(
			long groupId, long classNameId, int day, int month, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_C_Date(
			groupId, classNameId, day, month, year);
	}

	/**
	 * Retrieves the sum of view counts for a type of assets, in an specific
	 * month of a year.
	 *
	 * @param  groupId the primary key of the web content article's group
	 * @param  classNameId the primary key of the class to search for
	 * @param  month the month to search for
	 * @param  year the year to search for
	 * @return the sum of all view counts for a classNameId in an specific month
	 *         of a year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long sumByC_Month(long groupId, long classNameId, int month, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_C_Month(
			groupId, classNameId, month, year);
	}

	/**
	 * Retrieves the sum of view counts for a type of assets, in an specific
	 * year.
	 *
	 * @param  groupId the primary key of the web content article's group
	 * @param  classNameId the primary key of the class to search for
	 * @param  year the year to search for
	 * @return the sum of all view counts for a classNameId, in an specific year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long sumByC_Year(long groupId, long classNameId, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_C_Year(groupId, classNameId, year);
	}

	/**
	 * Retrieves the sum of view counts for all assets in a Group, in an
	 * specific day of a year.
	 *
	 * @param  groupId the primary key of the web content article's group
	 * @param  day the day to search for
	 * @param  month the month to search for
	 * @param  year the year to search for
	 * @return the sum of all view counts for a groupId, in an specific
	 *         day of a year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long sumByDate(long groupId, int day, int month, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_Date(groupId, day, month, year);
	}

	/**
	 * Retrieves the sum of view counts for all assets in a Group, in an
	 * specific month of a year.
	 *
	 * @param  groupId the primary key of the web content article's group
	 * @param  month the month to search for
	 * @param  year the year to search for
	 * @return the sum of all view counts for a groupId, in an specific
	 *         month of a year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long sumByMonth(long groupId, int month, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_Month(groupId, month, year);
	}

	/**
	 * Retrieves the sum of view counts for all assets in a Group, in an
	 * specific year.
	 *
	 * @param  groupId the primary key of the web content article's group
	 * @param  year the year to search for
	 * @return the sum of all view counts for a groupId, in an specific year
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long sumByYear(long groupId, int year) throws SystemException {
		return assetEntryStatsFinder.sumByG_Year(groupId, year);
	}

}