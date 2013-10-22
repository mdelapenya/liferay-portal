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

import com.liferay.portal.kernel.exception.SystemException;
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

	public long sumByC_C_Date(
			long groupId, long classNameId, long classPK, int day, int month,
			int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_C_C_Date(
			groupId, classNameId, classPK, day, month, year);
	}

	public long sumByC_C_Month(
			long groupId, long classNameId, long classPK, int month, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_C_C_Month(
			groupId, classNameId, classPK, month, year);
	}

	public long sumByC_C_Year(
			long groupId, long classNameId, long classPK, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_C_C_Year(
			groupId, classNameId, classPK, year);
	}

	public long sumByC_Date(
			long groupId, long classNameId, int day, int month, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_C_Date(
			groupId, classNameId, day, month, year);
	}

	public long sumByC_Month(long groupId, long classNameId, int month, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_C_Month(
			groupId, classNameId, month, year);
	}

	public long sumByC_Year(long groupId, long classNameId, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_C_Year(groupId, classNameId, year);
	}

	public long sumByDate(long groupId, int day, int month, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_Date(groupId, day, month, year);
	}

	public long sumByMonth(long groupId, int month, int year)
		throws SystemException {

		return assetEntryStatsFinder.sumByG_Month(groupId, month, year);
	}

	public long sumByYear(long groupId, int year) throws SystemException {
		return assetEntryStatsFinder.sumByG_Year(groupId, year);
	}

}