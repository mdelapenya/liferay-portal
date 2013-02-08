/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.documentlibrary.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.GroupedModel;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the DLFileRank service. Represents a row in the &quot;DLFileRank&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.documentlibrary.model.impl.DLFileRankModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.documentlibrary.model.impl.DLFileRankImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DLFileRank
 * @see com.liferay.portlet.documentlibrary.model.impl.DLFileRankImpl
 * @see com.liferay.portlet.documentlibrary.model.impl.DLFileRankModelImpl
 * @generated
 */
public interface DLFileRankModel extends BaseModel<DLFileRank>, GroupedModel,
	StagedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a document library file rank model instance should use the {@link DLFileRank} interface instead.
	 */

	/**
	 * Returns the primary key of this document library file rank.
	 *
	 * @return the primary key of this document library file rank
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this document library file rank.
	 *
	 * @param primaryKey the primary key of this document library file rank
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this document library file rank.
	 *
	 * @return the uuid of this document library file rank
	 */
	@AutoEscape
	public String getUuid();

	/**
	 * Sets the uuid of this document library file rank.
	 *
	 * @param uuid the uuid of this document library file rank
	 */
	public void setUuid(String uuid);

	/**
	 * Returns the file rank ID of this document library file rank.
	 *
	 * @return the file rank ID of this document library file rank
	 */
	public long getFileRankId();

	/**
	 * Sets the file rank ID of this document library file rank.
	 *
	 * @param fileRankId the file rank ID of this document library file rank
	 */
	public void setFileRankId(long fileRankId);

	/**
	 * Returns the group ID of this document library file rank.
	 *
	 * @return the group ID of this document library file rank
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this document library file rank.
	 *
	 * @param groupId the group ID of this document library file rank
	 */
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this document library file rank.
	 *
	 * @return the company ID of this document library file rank
	 */
	public long getCompanyId();

	/**
	 * Sets the company ID of this document library file rank.
	 *
	 * @param companyId the company ID of this document library file rank
	 */
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this document library file rank.
	 *
	 * @return the user ID of this document library file rank
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this document library file rank.
	 *
	 * @param userId the user ID of this document library file rank
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this document library file rank.
	 *
	 * @return the user uuid of this document library file rank
	 * @throws SystemException if a system exception occurred
	 */
	public String getUserUuid() throws SystemException;

	/**
	 * Sets the user uuid of this document library file rank.
	 *
	 * @param userUuid the user uuid of this document library file rank
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this document library file rank.
	 *
	 * @return the user name of this document library file rank
	 */
	@AutoEscape
	public String getUserName();

	/**
	 * Sets the user name of this document library file rank.
	 *
	 * @param userName the user name of this document library file rank
	 */
	public void setUserName(String userName);

	/**
	 * Returns the create date of this document library file rank.
	 *
	 * @return the create date of this document library file rank
	 */
	public Date getCreateDate();

	/**
	 * Sets the create date of this document library file rank.
	 *
	 * @param createDate the create date of this document library file rank
	 */
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this document library file rank.
	 *
	 * @return the modified date of this document library file rank
	 */
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this document library file rank.
	 *
	 * @param modifiedDate the modified date of this document library file rank
	 */
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the file entry ID of this document library file rank.
	 *
	 * @return the file entry ID of this document library file rank
	 */
	public long getFileEntryId();

	/**
	 * Sets the file entry ID of this document library file rank.
	 *
	 * @param fileEntryId the file entry ID of this document library file rank
	 */
	public void setFileEntryId(long fileEntryId);

	/**
	 * Returns the active of this document library file rank.
	 *
	 * @return the active of this document library file rank
	 */
	public boolean getActive();

	/**
	 * Returns <code>true</code> if this document library file rank is active.
	 *
	 * @return <code>true</code> if this document library file rank is active; <code>false</code> otherwise
	 */
	public boolean isActive();

	/**
	 * Sets whether this document library file rank is active.
	 *
	 * @param active the active of this document library file rank
	 */
	public void setActive(boolean active);

	public boolean isNew();

	public void setNew(boolean n);

	public boolean isCachedModel();

	public void setCachedModel(boolean cachedModel);

	public boolean isEscapedModel();

	public Serializable getPrimaryKeyObj();

	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	public ExpandoBridge getExpandoBridge();

	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	public Object clone();

	public int compareTo(DLFileRank dlFileRank);

	public int hashCode();

	public CacheModel<DLFileRank> toCacheModel();

	public DLFileRank toEscapedModel();

	public DLFileRank toUnescapedModel();

	public String toString();

	public String toXmlString();
}