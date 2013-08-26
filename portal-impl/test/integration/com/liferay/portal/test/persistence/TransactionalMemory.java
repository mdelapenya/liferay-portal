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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Manuel de la Peña
 */
public class TransactionalMemory {

	public Map<Serializable, BasePersistence<?>> getBasePersistences() {
		return _basePersistences;
	}

	public List<BasePersistenceWrapper> getBasePersistencesList() {
		return _basePersistencesList;
	}

	public void put(Serializable key, BasePersistence<?> value) {
		_basePersistences.put(key, value);

		_basePersistencesList.add(new BasePersistenceWrapper(key, value));
	}

	public void reset() {
		_basePersistences.clear();
		_basePersistencesList.clear();
	}

	private Map<Serializable, BasePersistence<?>> _basePersistences =
		new HashMap<Serializable, BasePersistence<?>>();

	private List<BasePersistenceWrapper> _basePersistencesList =
			new ArrayList<BasePersistenceWrapper>();

}