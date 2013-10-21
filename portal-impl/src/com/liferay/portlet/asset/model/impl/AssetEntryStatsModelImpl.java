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

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.asset.model.AssetEntryStats;
import com.liferay.portlet.asset.model.AssetEntryStatsModel;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the AssetEntryStats service. Represents a row in the &quot;AssetEntryStats&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portlet.asset.model.AssetEntryStatsModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link AssetEntryStatsImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetEntryStatsImpl
 * @see com.liferay.portlet.asset.model.AssetEntryStats
 * @see com.liferay.portlet.asset.model.AssetEntryStatsModel
 * @generated
 */
public class AssetEntryStatsModelImpl extends BaseModelImpl<AssetEntryStats>
	implements AssetEntryStatsModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a asset entry stats model instance should use the {@link com.liferay.portlet.asset.model.AssetEntryStats} interface instead.
	 */
	public static final String TABLE_NAME = "AssetEntryStats";
	public static final Object[][] TABLE_COLUMNS = {
			{ "entryStatsId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "classNameId", Types.BIGINT },
			{ "classPK", Types.BIGINT },
			{ "day", Types.INTEGER },
			{ "month", Types.INTEGER },
			{ "year", Types.INTEGER },
			{ "viewCount", Types.INTEGER }
		};
	public static final String TABLE_SQL_CREATE = "create table AssetEntryStats (entryStatsId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,classNameId LONG,classPK LONG,day INTEGER,month INTEGER,year INTEGER,viewCount INTEGER)";
	public static final String TABLE_SQL_DROP = "drop table AssetEntryStats";
	public static final String ORDER_BY_JPQL = " ORDER BY assetEntryStats.entryStatsId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY AssetEntryStats.entryStatsId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portlet.asset.model.AssetEntryStats"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portlet.asset.model.AssetEntryStats"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portlet.asset.model.AssetEntryStats"),
			true);
	public static long CLASSNAMEID_COLUMN_BITMASK = 1L;
	public static long CLASSPK_COLUMN_BITMASK = 2L;
	public static long DAY_COLUMN_BITMASK = 4L;
	public static long GROUPID_COLUMN_BITMASK = 8L;
	public static long MONTH_COLUMN_BITMASK = 16L;
	public static long YEAR_COLUMN_BITMASK = 32L;
	public static long ENTRYSTATSID_COLUMN_BITMASK = 64L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portlet.asset.model.AssetEntryStats"));

	public AssetEntryStatsModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _entryStatsId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setEntryStatsId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _entryStatsId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return AssetEntryStats.class;
	}

	@Override
	public String getModelClassName() {
		return AssetEntryStats.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("entryStatsId", getEntryStatsId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("classNameId", getClassNameId());
		attributes.put("classPK", getClassPK());
		attributes.put("day", getDay());
		attributes.put("month", getMonth());
		attributes.put("year", getYear());
		attributes.put("viewCount", getViewCount());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long entryStatsId = (Long)attributes.get("entryStatsId");

		if (entryStatsId != null) {
			setEntryStatsId(entryStatsId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long classNameId = (Long)attributes.get("classNameId");

		if (classNameId != null) {
			setClassNameId(classNameId);
		}

		Long classPK = (Long)attributes.get("classPK");

		if (classPK != null) {
			setClassPK(classPK);
		}

		Integer day = (Integer)attributes.get("day");

		if (day != null) {
			setDay(day);
		}

		Integer month = (Integer)attributes.get("month");

		if (month != null) {
			setMonth(month);
		}

		Integer year = (Integer)attributes.get("year");

		if (year != null) {
			setYear(year);
		}

		Integer viewCount = (Integer)attributes.get("viewCount");

		if (viewCount != null) {
			setViewCount(viewCount);
		}
	}

	@Override
	public long getEntryStatsId() {
		return _entryStatsId;
	}

	@Override
	public void setEntryStatsId(long entryStatsId) {
		_entryStatsId = entryStatsId;
	}

	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		_columnBitmask |= GROUPID_COLUMN_BITMASK;

		if (!_setOriginalGroupId) {
			_setOriginalGroupId = true;

			_originalGroupId = _groupId;
		}

		_groupId = groupId;
	}

	public long getOriginalGroupId() {
		return _originalGroupId;
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	public String getUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
	}

	@Override
	public void setUserUuid(String userUuid) {
		_userUuid = userUuid;
	}

	@Override
	public String getUserName() {
		if (_userName == null) {
			return StringPool.BLANK;
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
	}

	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	@Override
	public String getClassName() {
		if (getClassNameId() <= 0) {
			return StringPool.BLANK;
		}

		return PortalUtil.getClassName(getClassNameId());
	}

	@Override
	public void setClassName(String className) {
		long classNameId = 0;

		if (Validator.isNotNull(className)) {
			classNameId = PortalUtil.getClassNameId(className);
		}

		setClassNameId(classNameId);
	}

	@Override
	public long getClassNameId() {
		return _classNameId;
	}

	@Override
	public void setClassNameId(long classNameId) {
		_columnBitmask |= CLASSNAMEID_COLUMN_BITMASK;

		if (!_setOriginalClassNameId) {
			_setOriginalClassNameId = true;

			_originalClassNameId = _classNameId;
		}

		_classNameId = classNameId;
	}

	public long getOriginalClassNameId() {
		return _originalClassNameId;
	}

	@Override
	public long getClassPK() {
		return _classPK;
	}

	@Override
	public void setClassPK(long classPK) {
		_columnBitmask |= CLASSPK_COLUMN_BITMASK;

		if (!_setOriginalClassPK) {
			_setOriginalClassPK = true;

			_originalClassPK = _classPK;
		}

		_classPK = classPK;
	}

	public long getOriginalClassPK() {
		return _originalClassPK;
	}

	@Override
	public int getDay() {
		return _day;
	}

	@Override
	public void setDay(int day) {
		_columnBitmask |= DAY_COLUMN_BITMASK;

		if (!_setOriginalDay) {
			_setOriginalDay = true;

			_originalDay = _day;
		}

		_day = day;
	}

	public int getOriginalDay() {
		return _originalDay;
	}

	@Override
	public int getMonth() {
		return _month;
	}

	@Override
	public void setMonth(int month) {
		_columnBitmask |= MONTH_COLUMN_BITMASK;

		if (!_setOriginalMonth) {
			_setOriginalMonth = true;

			_originalMonth = _month;
		}

		_month = month;
	}

	public int getOriginalMonth() {
		return _originalMonth;
	}

	@Override
	public int getYear() {
		return _year;
	}

	@Override
	public void setYear(int year) {
		_columnBitmask |= YEAR_COLUMN_BITMASK;

		if (!_setOriginalYear) {
			_setOriginalYear = true;

			_originalYear = _year;
		}

		_year = year;
	}

	public int getOriginalYear() {
		return _originalYear;
	}

	@Override
	public int getViewCount() {
		return _viewCount;
	}

	@Override
	public void setViewCount(int viewCount) {
		_viewCount = viewCount;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			AssetEntryStats.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public AssetEntryStats toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (AssetEntryStats)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		AssetEntryStatsImpl assetEntryStatsImpl = new AssetEntryStatsImpl();

		assetEntryStatsImpl.setEntryStatsId(getEntryStatsId());
		assetEntryStatsImpl.setGroupId(getGroupId());
		assetEntryStatsImpl.setCompanyId(getCompanyId());
		assetEntryStatsImpl.setUserId(getUserId());
		assetEntryStatsImpl.setUserName(getUserName());
		assetEntryStatsImpl.setCreateDate(getCreateDate());
		assetEntryStatsImpl.setModifiedDate(getModifiedDate());
		assetEntryStatsImpl.setClassNameId(getClassNameId());
		assetEntryStatsImpl.setClassPK(getClassPK());
		assetEntryStatsImpl.setDay(getDay());
		assetEntryStatsImpl.setMonth(getMonth());
		assetEntryStatsImpl.setYear(getYear());
		assetEntryStatsImpl.setViewCount(getViewCount());

		assetEntryStatsImpl.resetOriginalValues();

		return assetEntryStatsImpl;
	}

	@Override
	public int compareTo(AssetEntryStats assetEntryStats) {
		long primaryKey = assetEntryStats.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AssetEntryStats)) {
			return false;
		}

		AssetEntryStats assetEntryStats = (AssetEntryStats)obj;

		long primaryKey = assetEntryStats.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public void resetOriginalValues() {
		AssetEntryStatsModelImpl assetEntryStatsModelImpl = this;

		assetEntryStatsModelImpl._originalGroupId = assetEntryStatsModelImpl._groupId;

		assetEntryStatsModelImpl._setOriginalGroupId = false;

		assetEntryStatsModelImpl._originalClassNameId = assetEntryStatsModelImpl._classNameId;

		assetEntryStatsModelImpl._setOriginalClassNameId = false;

		assetEntryStatsModelImpl._originalClassPK = assetEntryStatsModelImpl._classPK;

		assetEntryStatsModelImpl._setOriginalClassPK = false;

		assetEntryStatsModelImpl._originalDay = assetEntryStatsModelImpl._day;

		assetEntryStatsModelImpl._setOriginalDay = false;

		assetEntryStatsModelImpl._originalMonth = assetEntryStatsModelImpl._month;

		assetEntryStatsModelImpl._setOriginalMonth = false;

		assetEntryStatsModelImpl._originalYear = assetEntryStatsModelImpl._year;

		assetEntryStatsModelImpl._setOriginalYear = false;

		assetEntryStatsModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<AssetEntryStats> toCacheModel() {
		AssetEntryStatsCacheModel assetEntryStatsCacheModel = new AssetEntryStatsCacheModel();

		assetEntryStatsCacheModel.entryStatsId = getEntryStatsId();

		assetEntryStatsCacheModel.groupId = getGroupId();

		assetEntryStatsCacheModel.companyId = getCompanyId();

		assetEntryStatsCacheModel.userId = getUserId();

		assetEntryStatsCacheModel.userName = getUserName();

		String userName = assetEntryStatsCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			assetEntryStatsCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			assetEntryStatsCacheModel.createDate = createDate.getTime();
		}
		else {
			assetEntryStatsCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			assetEntryStatsCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			assetEntryStatsCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		assetEntryStatsCacheModel.classNameId = getClassNameId();

		assetEntryStatsCacheModel.classPK = getClassPK();

		assetEntryStatsCacheModel.day = getDay();

		assetEntryStatsCacheModel.month = getMonth();

		assetEntryStatsCacheModel.year = getYear();

		assetEntryStatsCacheModel.viewCount = getViewCount();

		return assetEntryStatsCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(27);

		sb.append("{entryStatsId=");
		sb.append(getEntryStatsId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append(", classNameId=");
		sb.append(getClassNameId());
		sb.append(", classPK=");
		sb.append(getClassPK());
		sb.append(", day=");
		sb.append(getDay());
		sb.append(", month=");
		sb.append(getMonth());
		sb.append(", year=");
		sb.append(getYear());
		sb.append(", viewCount=");
		sb.append(getViewCount());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(43);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portlet.asset.model.AssetEntryStats");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>entryStatsId</column-name><column-value><![CDATA[");
		sb.append(getEntryStatsId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>groupId</column-name><column-value><![CDATA[");
		sb.append(getGroupId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userName</column-name><column-value><![CDATA[");
		sb.append(getUserName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
		sb.append(getModifiedDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>classNameId</column-name><column-value><![CDATA[");
		sb.append(getClassNameId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>classPK</column-name><column-value><![CDATA[");
		sb.append(getClassPK());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>day</column-name><column-value><![CDATA[");
		sb.append(getDay());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>month</column-name><column-value><![CDATA[");
		sb.append(getMonth());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>year</column-name><column-value><![CDATA[");
		sb.append(getYear());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>viewCount</column-name><column-value><![CDATA[");
		sb.append(getViewCount());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = AssetEntryStats.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			AssetEntryStats.class
		};
	private long _entryStatsId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _userId;
	private String _userUuid;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private long _classNameId;
	private long _originalClassNameId;
	private boolean _setOriginalClassNameId;
	private long _classPK;
	private long _originalClassPK;
	private boolean _setOriginalClassPK;
	private int _day;
	private int _originalDay;
	private boolean _setOriginalDay;
	private int _month;
	private int _originalMonth;
	private boolean _setOriginalMonth;
	private int _year;
	private int _originalYear;
	private boolean _setOriginalYear;
	private int _viewCount;
	private long _columnBitmask;
	private AssetEntryStats _escapedModel;
}