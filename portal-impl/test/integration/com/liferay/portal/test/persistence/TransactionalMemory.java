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

package com.liferay.portal.test.persistence;

import com.liferay.portal.service.persistence.BasePersistence;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Manuel de la Peña
 */
public class TransactionalMemory {

	public Map<Serializable, BasePersistence<?>> getBasePersistences() {
		return _basePersistences;
	}

	public void put(Serializable key, BasePersistence<?> value) {
		_basePersistences.put(key, value);
	}

	public void reset() {
		_basePersistences.clear();
	}

	private Map<Serializable, BasePersistence<?>> _basePersistences =
		new HashMap<Serializable, BasePersistence<?>>();

}