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

package com.liferay.portal.test;

import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.test.persistence.TransactionalMemory;
import com.liferay.portlet.blogs.model.BlogsEntry;

import java.io.Serializable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class TransactionalMemoryTest {

	@BeforeClass
	public static void setUpClass() {
		_memoryStorage = new TransactionalMemory();
	}

	@Before
	public void setUp() throws Exception {
		_key = ServiceTestUtil.randomString();

		_basePersistence = new BasePersistenceImpl<BlogsEntry>();
	}

	@Test
	public void testGetBasePersistences() {
		Assert.assertNotNull(_memoryStorage.getBasePersistences());
	}

	@Test
	public void testPut() {
		_memoryStorage.put(_key, _basePersistence);

		BasePersistence<?> inMemoryPersistence =
			_memoryStorage.getBasePersistences().get(_key);

		Assert.assertEquals(_basePersistence, inMemoryPersistence);
	}

	@Test
	public void testReset() {
		_memoryStorage.put(_key, _basePersistence);

		_memoryStorage.reset();

		Assert.assertEquals(0, _memoryStorage.getBasePersistences().size());
	}

	private static TransactionalMemory _memoryStorage;
	private BasePersistence<?> _basePersistence;

	private Serializable _key;


}