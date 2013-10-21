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

package com.liferay.portlet.asset.model.impl;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import com.liferay.portlet.asset.model.AssetEntryStats;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing AssetEntryStats in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see AssetEntryStats
 * @generated
 */
public class AssetEntryStatsCacheModel implements CacheModel<AssetEntryStats>,
	Externalizable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(27);

		sb.append("{entryStatsId=");
		sb.append(entryStatsId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", classNameId=");
		sb.append(classNameId);
		sb.append(", classPK=");
		sb.append(classPK);
		sb.append(", day=");
		sb.append(day);
		sb.append(", month=");
		sb.append(month);
		sb.append(", year=");
		sb.append(year);
		sb.append(", viewCount=");
		sb.append(viewCount);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public AssetEntryStats toEntityModel() {
		AssetEntryStatsImpl assetEntryStatsImpl = new AssetEntryStatsImpl();

		assetEntryStatsImpl.setEntryStatsId(entryStatsId);
		assetEntryStatsImpl.setGroupId(groupId);
		assetEntryStatsImpl.setCompanyId(companyId);
		assetEntryStatsImpl.setUserId(userId);

		if (userName == null) {
			assetEntryStatsImpl.setUserName(StringPool.BLANK);
		}
		else {
			assetEntryStatsImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			assetEntryStatsImpl.setCreateDate(null);
		}
		else {
			assetEntryStatsImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			assetEntryStatsImpl.setModifiedDate(null);
		}
		else {
			assetEntryStatsImpl.setModifiedDate(new Date(modifiedDate));
		}

		assetEntryStatsImpl.setClassNameId(classNameId);
		assetEntryStatsImpl.setClassPK(classPK);
		assetEntryStatsImpl.setDay(day);
		assetEntryStatsImpl.setMonth(month);
		assetEntryStatsImpl.setYear(year);
		assetEntryStatsImpl.setViewCount(viewCount);

		assetEntryStatsImpl.resetOriginalValues();

		return assetEntryStatsImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		entryStatsId = objectInput.readLong();
		groupId = objectInput.readLong();
		companyId = objectInput.readLong();
		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		classNameId = objectInput.readLong();
		classPK = objectInput.readLong();
		day = objectInput.readInt();
		month = objectInput.readInt();
		year = objectInput.readInt();
		viewCount = objectInput.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(entryStatsId);
		objectOutput.writeLong(groupId);
		objectOutput.writeLong(companyId);
		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);
		objectOutput.writeLong(classNameId);
		objectOutput.writeLong(classPK);
		objectOutput.writeInt(day);
		objectOutput.writeInt(month);
		objectOutput.writeInt(year);
		objectOutput.writeInt(viewCount);
	}

	public long entryStatsId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public long classNameId;
	public long classPK;
	public int day;
	public int month;
	public int year;
	public int viewCount;
}