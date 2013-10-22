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

/**
 * @author Brian Wing Shun Chan
 */
public interface AssetEntryStatsFinder {
	public long sumByG_C_C_Date(long groupId, long classNameId, long classPK,
		int day, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

	public long sumByG_C_C_Month(long groupId, long classNameId, long classPK,
		int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

	public long sumByG_C_C_Year(long groupId, long classNameId, long classPK,
		int year) throws com.liferay.portal.kernel.exception.SystemException;

	public long sumByG_C_Date(long groupId, long classNameId, int day,
		int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

	public long sumByG_C_Month(long groupId, long classNameId, int month,
		int year) throws com.liferay.portal.kernel.exception.SystemException;

	public long sumByG_C_Year(long groupId, long classNameId, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

	public long sumByG_Date(long groupId, int day, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

	public long sumByG_Month(long groupId, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException;

	public long sumByG_Year(long groupId, int year)
		throws com.liferay.portal.kernel.exception.SystemException;
}