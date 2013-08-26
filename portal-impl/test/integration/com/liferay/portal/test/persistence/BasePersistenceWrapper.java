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

package com.liferay.portal.test.persistence;

import com.liferay.portal.service.persistence.BasePersistence;

import java.io.Serializable;

/**
 * @author Manuel de la Peña
 */
public class BasePersistenceWrapper {

	public BasePersistenceWrapper(
		Serializable key, BasePersistence<?> basePersistence) {

		_key = key;
		_basePersistence = basePersistence;
	}

	public BasePersistence<?> getBasePersistence() {
		return _basePersistence;
	}

	public Serializable getKey() {
		return _key;
	}

	private BasePersistence<?> _basePersistence;
	private Serializable _key;

}