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

package com.liferay.portlet.asset.model;

import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link AssetEntryStats}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetEntryStats
 * @generated
 */
public class AssetEntryStatsWrapper implements AssetEntryStats,
	ModelWrapper<AssetEntryStats> {
	public AssetEntryStatsWrapper(AssetEntryStats assetEntryStats) {
		_assetEntryStats = assetEntryStats;
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

	/**
	* Returns the primary key of this asset entry stats.
	*
	* @return the primary key of this asset entry stats
	*/
	@Override
	public long getPrimaryKey() {
		return _assetEntryStats.getPrimaryKey();
	}

	/**
	* Sets the primary key of this asset entry stats.
	*
	* @param primaryKey the primary key of this asset entry stats
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_assetEntryStats.setPrimaryKey(primaryKey);
	}

	/**
	* Returns the entry stats ID of this asset entry stats.
	*
	* @return the entry stats ID of this asset entry stats
	*/
	@Override
	public long getEntryStatsId() {
		return _assetEntryStats.getEntryStatsId();
	}

	/**
	* Sets the entry stats ID of this asset entry stats.
	*
	* @param entryStatsId the entry stats ID of this asset entry stats
	*/
	@Override
	public void setEntryStatsId(long entryStatsId) {
		_assetEntryStats.setEntryStatsId(entryStatsId);
	}

	/**
	* Returns the group ID of this asset entry stats.
	*
	* @return the group ID of this asset entry stats
	*/
	@Override
	public long getGroupId() {
		return _assetEntryStats.getGroupId();
	}

	/**
	* Sets the group ID of this asset entry stats.
	*
	* @param groupId the group ID of this asset entry stats
	*/
	@Override
	public void setGroupId(long groupId) {
		_assetEntryStats.setGroupId(groupId);
	}

	/**
	* Returns the company ID of this asset entry stats.
	*
	* @return the company ID of this asset entry stats
	*/
	@Override
	public long getCompanyId() {
		return _assetEntryStats.getCompanyId();
	}

	/**
	* Sets the company ID of this asset entry stats.
	*
	* @param companyId the company ID of this asset entry stats
	*/
	@Override
	public void setCompanyId(long companyId) {
		_assetEntryStats.setCompanyId(companyId);
	}

	/**
	* Returns the user ID of this asset entry stats.
	*
	* @return the user ID of this asset entry stats
	*/
	@Override
	public long getUserId() {
		return _assetEntryStats.getUserId();
	}

	/**
	* Sets the user ID of this asset entry stats.
	*
	* @param userId the user ID of this asset entry stats
	*/
	@Override
	public void setUserId(long userId) {
		_assetEntryStats.setUserId(userId);
	}

	/**
	* Returns the user uuid of this asset entry stats.
	*
	* @return the user uuid of this asset entry stats
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public java.lang.String getUserUuid()
		throws com.liferay.portal.kernel.exception.SystemException {
		return _assetEntryStats.getUserUuid();
	}

	/**
	* Sets the user uuid of this asset entry stats.
	*
	* @param userUuid the user uuid of this asset entry stats
	*/
	@Override
	public void setUserUuid(java.lang.String userUuid) {
		_assetEntryStats.setUserUuid(userUuid);
	}

	/**
	* Returns the user name of this asset entry stats.
	*
	* @return the user name of this asset entry stats
	*/
	@Override
	public java.lang.String getUserName() {
		return _assetEntryStats.getUserName();
	}

	/**
	* Sets the user name of this asset entry stats.
	*
	* @param userName the user name of this asset entry stats
	*/
	@Override
	public void setUserName(java.lang.String userName) {
		_assetEntryStats.setUserName(userName);
	}

	/**
	* Returns the create date of this asset entry stats.
	*
	* @return the create date of this asset entry stats
	*/
	@Override
	public java.util.Date getCreateDate() {
		return _assetEntryStats.getCreateDate();
	}

	/**
	* Sets the create date of this asset entry stats.
	*
	* @param createDate the create date of this asset entry stats
	*/
	@Override
	public void setCreateDate(java.util.Date createDate) {
		_assetEntryStats.setCreateDate(createDate);
	}

	/**
	* Returns the modified date of this asset entry stats.
	*
	* @return the modified date of this asset entry stats
	*/
	@Override
	public java.util.Date getModifiedDate() {
		return _assetEntryStats.getModifiedDate();
	}

	/**
	* Sets the modified date of this asset entry stats.
	*
	* @param modifiedDate the modified date of this asset entry stats
	*/
	@Override
	public void setModifiedDate(java.util.Date modifiedDate) {
		_assetEntryStats.setModifiedDate(modifiedDate);
	}

	/**
	* Returns the fully qualified class name of this asset entry stats.
	*
	* @return the fully qualified class name of this asset entry stats
	*/
	@Override
	public java.lang.String getClassName() {
		return _assetEntryStats.getClassName();
	}

	@Override
	public void setClassName(java.lang.String className) {
		_assetEntryStats.setClassName(className);
	}

	/**
	* Returns the class name ID of this asset entry stats.
	*
	* @return the class name ID of this asset entry stats
	*/
	@Override
	public long getClassNameId() {
		return _assetEntryStats.getClassNameId();
	}

	/**
	* Sets the class name ID of this asset entry stats.
	*
	* @param classNameId the class name ID of this asset entry stats
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_assetEntryStats.setClassNameId(classNameId);
	}

	/**
	* Returns the class p k of this asset entry stats.
	*
	* @return the class p k of this asset entry stats
	*/
	@Override
	public long getClassPK() {
		return _assetEntryStats.getClassPK();
	}

	/**
	* Sets the class p k of this asset entry stats.
	*
	* @param classPK the class p k of this asset entry stats
	*/
	@Override
	public void setClassPK(long classPK) {
		_assetEntryStats.setClassPK(classPK);
	}

	/**
	* Returns the day of this asset entry stats.
	*
	* @return the day of this asset entry stats
	*/
	@Override
	public int getDay() {
		return _assetEntryStats.getDay();
	}

	/**
	* Sets the day of this asset entry stats.
	*
	* @param day the day of this asset entry stats
	*/
	@Override
	public void setDay(int day) {
		_assetEntryStats.setDay(day);
	}

	/**
	* Returns the month of this asset entry stats.
	*
	* @return the month of this asset entry stats
	*/
	@Override
	public int getMonth() {
		return _assetEntryStats.getMonth();
	}

	/**
	* Sets the month of this asset entry stats.
	*
	* @param month the month of this asset entry stats
	*/
	@Override
	public void setMonth(int month) {
		_assetEntryStats.setMonth(month);
	}

	/**
	* Returns the year of this asset entry stats.
	*
	* @return the year of this asset entry stats
	*/
	@Override
	public int getYear() {
		return _assetEntryStats.getYear();
	}

	/**
	* Sets the year of this asset entry stats.
	*
	* @param year the year of this asset entry stats
	*/
	@Override
	public void setYear(int year) {
		_assetEntryStats.setYear(year);
	}

	/**
	* Returns the view count of this asset entry stats.
	*
	* @return the view count of this asset entry stats
	*/
	@Override
	public int getViewCount() {
		return _assetEntryStats.getViewCount();
	}

	/**
	* Sets the view count of this asset entry stats.
	*
	* @param viewCount the view count of this asset entry stats
	*/
	@Override
	public void setViewCount(int viewCount) {
		_assetEntryStats.setViewCount(viewCount);
	}

	@Override
	public boolean isNew() {
		return _assetEntryStats.isNew();
	}

	@Override
	public void setNew(boolean n) {
		_assetEntryStats.setNew(n);
	}

	@Override
	public boolean isCachedModel() {
		return _assetEntryStats.isCachedModel();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_assetEntryStats.setCachedModel(cachedModel);
	}

	@Override
	public boolean isEscapedModel() {
		return _assetEntryStats.isEscapedModel();
	}

	@Override
	public java.io.Serializable getPrimaryKeyObj() {
		return _assetEntryStats.getPrimaryKeyObj();
	}

	@Override
	public void setPrimaryKeyObj(java.io.Serializable primaryKeyObj) {
		_assetEntryStats.setPrimaryKeyObj(primaryKeyObj);
	}

	@Override
	public com.liferay.portlet.expando.model.ExpandoBridge getExpandoBridge() {
		return _assetEntryStats.getExpandoBridge();
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.model.BaseModel<?> baseModel) {
		_assetEntryStats.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portlet.expando.model.ExpandoBridge expandoBridge) {
		_assetEntryStats.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.service.ServiceContext serviceContext) {
		_assetEntryStats.setExpandoBridgeAttributes(serviceContext);
	}

	@Override
	public java.lang.Object clone() {
		return new AssetEntryStatsWrapper((AssetEntryStats)_assetEntryStats.clone());
	}

	@Override
	public int compareTo(
		com.liferay.portlet.asset.model.AssetEntryStats assetEntryStats) {
		return _assetEntryStats.compareTo(assetEntryStats);
	}

	@Override
	public int hashCode() {
		return _assetEntryStats.hashCode();
	}

	@Override
	public com.liferay.portal.model.CacheModel<com.liferay.portlet.asset.model.AssetEntryStats> toCacheModel() {
		return _assetEntryStats.toCacheModel();
	}

	@Override
	public com.liferay.portlet.asset.model.AssetEntryStats toEscapedModel() {
		return new AssetEntryStatsWrapper(_assetEntryStats.toEscapedModel());
	}

	@Override
	public com.liferay.portlet.asset.model.AssetEntryStats toUnescapedModel() {
		return new AssetEntryStatsWrapper(_assetEntryStats.toUnescapedModel());
	}

	@Override
	public java.lang.String toString() {
		return _assetEntryStats.toString();
	}

	@Override
	public java.lang.String toXmlString() {
		return _assetEntryStats.toXmlString();
	}

	@Override
	public void persist()
		throws com.liferay.portal.kernel.exception.SystemException {
		_assetEntryStats.persist();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AssetEntryStatsWrapper)) {
			return false;
		}

		AssetEntryStatsWrapper assetEntryStatsWrapper = (AssetEntryStatsWrapper)obj;

		if (Validator.equals(_assetEntryStats,
					assetEntryStatsWrapper._assetEntryStats)) {
			return true;
		}

		return false;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedModel}
	 */
	public AssetEntryStats getWrappedAssetEntryStats() {
		return _assetEntryStats;
	}

	@Override
	public AssetEntryStats getWrappedModel() {
		return _assetEntryStats;
	}

	@Override
	public void resetOriginalValues() {
		_assetEntryStats.resetOriginalValues();
	}

	private AssetEntryStats _assetEntryStats;
}