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
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ListType;
import com.liferay.portal.model.ListTypeModel;
import com.liferay.portal.model.ListTypeSoap;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the ListType service. Represents a row in the &quot;ListType&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portal.model.ListTypeModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link ListTypeImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ListTypeImpl
 * @see com.liferay.portal.model.ListType
 * @see com.liferay.portal.model.ListTypeModel
 * @generated
 */
@JSON(strict = true)
public class ListTypeModelImpl extends BaseModelImpl<ListType>
	implements ListTypeModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a list type model instance should use the {@link com.liferay.portal.model.ListType} interface instead.
	 */
	public static final String TABLE_NAME = "ListType";
	public static final Object[][] TABLE_COLUMNS = {
			{ "listTypeId", Types.INTEGER },
			{ "name", Types.VARCHAR },
			{ "type_", Types.VARCHAR }
		};
	public static final String TABLE_SQL_CREATE = "create table ListType (listTypeId INTEGER not null primary key,name VARCHAR(75) null,type_ VARCHAR(75) null)";
	public static final String TABLE_SQL_DROP = "drop table ListType";
	public static final String ORDER_BY_ENTITY_ALIAS = " ORDER BY listType.name ASC";
	public static final String ORDER_BY_ENTITY_TABLE = " ORDER BY ListType.name ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portal.model.ListType"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portal.model.ListType"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.model.ListType"),
			true);
	public static long TYPE_COLUMN_BITMASK = 1L;
	public static long NAME_COLUMN_BITMASK = 2L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static ListType toModel(ListTypeSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		ListType model = new ListTypeImpl();

		model.setListTypeId(soapModel.getListTypeId());
		model.setName(soapModel.getName());
		model.setType(soapModel.getType());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<ListType> toModels(ListTypeSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<ListType> models = new ArrayList<ListType>(soapModels.length);

		for (ListTypeSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portal.model.ListType"));

	public ListTypeModelImpl() {
	}

	public int getPrimaryKey() {
		return _listTypeId;
	}

	public void setPrimaryKey(int primaryKey) {
		setListTypeId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return _listTypeId;
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Integer)primaryKeyObj).intValue());
	}

	public Class<?> getModelClass() {
		return ListType.class;
	}

	public String getModelClassName() {
		return ListType.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("listTypeId", getListTypeId());
		attributes.put("name", getName());
		attributes.put("type", getType());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Integer listTypeId = (Integer)attributes.get("listTypeId");

		if (listTypeId != null) {
			setListTypeId(listTypeId);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String type = (String)attributes.get("type");

		if (type != null) {
			setType(type);
		}
	}

	@JSON
	public int getListTypeId() {
		return _listTypeId;
	}

	public void setListTypeId(int listTypeId) {
		_listTypeId = listTypeId;
	}

	@JSON
	public String getName() {
		if (_name == null) {
			return StringPool.BLANK;
		}
		else {
			return _name;
		}
	}

	public void setName(String name) {
		_columnBitmask = -1L;

		_name = name;
	}

	@JSON
	public String getType() {
		if (_type == null) {
			return StringPool.BLANK;
		}
		else {
			return _type;
		}
	}

	public void setType(String type) {
		_columnBitmask |= TYPE_COLUMN_BITMASK;

		if (_originalType == null) {
			_originalType = _type;
		}

		_type = type;
	}

	public String getOriginalType() {
		return GetterUtil.getString(_originalType);
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ListType toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (ListType)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		ListTypeImpl listTypeImpl = new ListTypeImpl();

		listTypeImpl.setListTypeId(getListTypeId());
		listTypeImpl.setName(getName());
		listTypeImpl.setType(getType());

		listTypeImpl.resetOriginalValues();

		return listTypeImpl;
	}

	public int compareTo(ListType listType) {
		int value = 0;

		value = getName().compareToIgnoreCase(listType.getName());

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		ListType listType = null;

		try {
			listType = (ListType)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		int primaryKey = listType.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getPrimaryKey();
	}

	@Override
	public void resetOriginalValues() {
		ListTypeModelImpl listTypeModelImpl = this;

		listTypeModelImpl._originalType = listTypeModelImpl._type;

		listTypeModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<ListType> toCacheModel() {
		ListTypeCacheModel listTypeCacheModel = new ListTypeCacheModel();

		listTypeCacheModel.listTypeId = getListTypeId();

		listTypeCacheModel.name = getName();

		String name = listTypeCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			listTypeCacheModel.name = null;
		}

		listTypeCacheModel.type = getType();

		String type = listTypeCacheModel.type;

		if ((type != null) && (type.length() == 0)) {
			listTypeCacheModel.type = null;
		}

		return listTypeCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(7);

		sb.append("{listTypeId=");
		sb.append(getListTypeId());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", type=");
		sb.append(getType());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(13);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portal.model.ListType");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>listTypeId</column-name><column-value><![CDATA[");
		sb.append(getListTypeId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>name</column-name><column-value><![CDATA[");
		sb.append(getName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>type</column-name><column-value><![CDATA[");
		sb.append(getType());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = ListType.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			ListType.class
		};
	private int _listTypeId;
	private String _name;
	private String _type;
	private String _originalType;
	private long _columnBitmask;
	private ListType _escapedModel;
}