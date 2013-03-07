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

package com.liferay.portal.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.UserGroupRole;
import com.liferay.portal.model.UserGroupRoleModel;
import com.liferay.portal.model.UserGroupRoleSoap;
import com.liferay.portal.service.persistence.UserGroupRolePK;
import com.liferay.portal.util.PortalUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the UserGroupRole service. Represents a row in the &quot;UserGroupRole&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portal.model.UserGroupRoleModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link UserGroupRoleImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see UserGroupRoleImpl
 * @see com.liferay.portal.model.UserGroupRole
 * @see com.liferay.portal.model.UserGroupRoleModel
 * @generated
 */
@JSON(strict = true)
public class UserGroupRoleModelImpl extends BaseModelImpl<UserGroupRole>
	implements UserGroupRoleModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a user group role model instance should use the {@link com.liferay.portal.model.UserGroupRole} interface instead.
	 */
	public static final String TABLE_NAME = "UserGroupRole";
	public static final Object[][] TABLE_COLUMNS = {
			{ "userId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "roleId", Types.BIGINT }
		};
	public static final String TABLE_SQL_CREATE = "create table UserGroupRole (userId LONG not null,groupId LONG not null,roleId LONG not null,primary key (userId, groupId, roleId))";
	public static final String TABLE_SQL_DROP = "drop table UserGroupRole";
	public static final String ORDER_BY_ENTITY_ALIAS = " ORDER BY userGroupRole.userId ASC, userGroupRole.groupId ASC, userGroupRole.roleId ASC";
	public static final String ORDER_BY_ENTITY_TABLE = " ORDER BY UserGroupRole.userId ASC, UserGroupRole.groupId ASC, UserGroupRole.roleId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portal.model.UserGroupRole"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portal.model.UserGroupRole"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.model.UserGroupRole"),
			true);
	public static long GROUPID_COLUMN_BITMASK = 1L;
	public static long ROLEID_COLUMN_BITMASK = 2L;
	public static long USERID_COLUMN_BITMASK = 4L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static UserGroupRole toModel(UserGroupRoleSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		UserGroupRole model = new UserGroupRoleImpl();

		model.setUserId(soapModel.getUserId());
		model.setGroupId(soapModel.getGroupId());
		model.setRoleId(soapModel.getRoleId());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<UserGroupRole> toModels(UserGroupRoleSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<UserGroupRole> models = new ArrayList<UserGroupRole>(soapModels.length);

		for (UserGroupRoleSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portal.model.UserGroupRole"));

	public UserGroupRoleModelImpl() {
	}

	public UserGroupRolePK getPrimaryKey() {
		return new UserGroupRolePK(_userId, _groupId, _roleId);
	}

	public void setPrimaryKey(UserGroupRolePK primaryKey) {
		setUserId(primaryKey.userId);
		setGroupId(primaryKey.groupId);
		setRoleId(primaryKey.roleId);
	}

	public Serializable getPrimaryKeyObj() {
		return new UserGroupRolePK(_userId, _groupId, _roleId);
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey((UserGroupRolePK)primaryKeyObj);
	}

	public Class<?> getModelClass() {
		return UserGroupRole.class;
	}

	public String getModelClassName() {
		return UserGroupRole.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("userId", getUserId());
		attributes.put("groupId", getGroupId());
		attributes.put("roleId", getRoleId());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long roleId = (Long)attributes.get("roleId");

		if (roleId != null) {
			setRoleId(roleId);
		}
	}

	@JSON
	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_columnBitmask |= USERID_COLUMN_BITMASK;

		if (!_setOriginalUserId) {
			_setOriginalUserId = true;

			_originalUserId = _userId;
		}

		_userId = userId;
	}

	public String getUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
	}

	public void setUserUuid(String userUuid) {
		_userUuid = userUuid;
	}

	public long getOriginalUserId() {
		return _originalUserId;
	}

	@JSON
	public long getGroupId() {
		return _groupId;
	}

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

	@JSON
	public long getRoleId() {
		return _roleId;
	}

	public void setRoleId(long roleId) {
		_columnBitmask |= ROLEID_COLUMN_BITMASK;

		if (!_setOriginalRoleId) {
			_setOriginalRoleId = true;

			_originalRoleId = _roleId;
		}

		_roleId = roleId;
	}

	public long getOriginalRoleId() {
		return _originalRoleId;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public UserGroupRole toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (UserGroupRole)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		UserGroupRoleImpl userGroupRoleImpl = new UserGroupRoleImpl();

		userGroupRoleImpl.setUserId(getUserId());
		userGroupRoleImpl.setGroupId(getGroupId());
		userGroupRoleImpl.setRoleId(getRoleId());

		userGroupRoleImpl.resetOriginalValues();

		return userGroupRoleImpl;
	}

	public int compareTo(UserGroupRole userGroupRole) {
		UserGroupRolePK primaryKey = userGroupRole.getPrimaryKey();

		return getPrimaryKey().compareTo(primaryKey);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		UserGroupRole userGroupRole = null;

		try {
			userGroupRole = (UserGroupRole)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		UserGroupRolePK primaryKey = userGroupRole.getPrimaryKey();

		if (getPrimaryKey().equals(primaryKey)) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getPrimaryKey().hashCode();
	}

	@Override
	public void resetOriginalValues() {
		UserGroupRoleModelImpl userGroupRoleModelImpl = this;

		userGroupRoleModelImpl._originalUserId = userGroupRoleModelImpl._userId;

		userGroupRoleModelImpl._setOriginalUserId = false;

		userGroupRoleModelImpl._originalGroupId = userGroupRoleModelImpl._groupId;

		userGroupRoleModelImpl._setOriginalGroupId = false;

		userGroupRoleModelImpl._originalRoleId = userGroupRoleModelImpl._roleId;

		userGroupRoleModelImpl._setOriginalRoleId = false;

		userGroupRoleModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<UserGroupRole> toCacheModel() {
		UserGroupRoleCacheModel userGroupRoleCacheModel = new UserGroupRoleCacheModel();

		userGroupRoleCacheModel.userId = getUserId();

		userGroupRoleCacheModel.groupId = getGroupId();

		userGroupRoleCacheModel.roleId = getRoleId();

		return userGroupRoleCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(7);

		sb.append("{userId=");
		sb.append(getUserId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", roleId=");
		sb.append(getRoleId());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(13);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portal.model.UserGroupRole");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>groupId</column-name><column-value><![CDATA[");
		sb.append(getGroupId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>roleId</column-name><column-value><![CDATA[");
		sb.append(getRoleId());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = UserGroupRole.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			UserGroupRole.class
		};
	private long _userId;
	private String _userUuid;
	private long _originalUserId;
	private boolean _setOriginalUserId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _roleId;
	private long _originalRoleId;
	private boolean _setOriginalRoleId;
	private long _columnBitmask;
	private UserGroupRole _escapedModel;
}