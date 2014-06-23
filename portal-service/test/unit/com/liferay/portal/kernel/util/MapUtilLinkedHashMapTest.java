/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.util;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Peter Borkuti
 */
public class MapUtilLinkedHashMapTest {

	@Test
	public void testCustomDelimiter() throws Exception {
		Map<String, String> map = MapUtil.toLinkedHashMap(
			new String[] {"one,1"}, ",");

		Assert.assertTrue(map.size() == 1);

		Assert.assertTrue(map.containsKey("one"));
		Assert.assertTrue(map.containsValue("1"));
	}

	@Test
	public void testDefaultDelimiter() throws Exception {
		Map<String, String> map = MapUtil.toLinkedHashMap(
			new String[] {"one:1"});

		Assert.assertTrue(map.size() == 1);

		Assert.assertTrue(map.containsKey("one"));
		Assert.assertTrue(map.containsValue("1"));
	}

	@Test
	public void testTypeBoolean() throws Exception {
		Map<String, Object> map = MapUtil.toLinkedHashMap(
			new String[] {"one:true:boolean"});

		Assert.assertTrue(map.size() == 1);

		Assert.assertTrue(map.containsKey("one"));
		Assert.assertTrue(map.containsValue(true));

		Assert.assertTrue(map.get("one") instanceof Boolean);
	}

	@Test
	public void testTypeByte() throws Exception {
		Map<String, Object> map = MapUtil.toLinkedHashMap(
			new String[] {"one:1:" + Byte.class.getName()});

		Assert.assertTrue(map.size() == 1);

		Assert.assertTrue(map.containsKey("one"));
		Assert.assertTrue(map.containsValue((byte)1));

		Assert.assertTrue(map.get("one") instanceof Byte);
	}

	@Test
	public void testTypeDouble() throws Exception {
		Map<String, Object> map = MapUtil.toLinkedHashMap(
			new String[] {"one:1.0:double"});

		Assert.assertTrue(map.size() == 1);

		Assert.assertTrue(map.containsKey("one"));
		Assert.assertTrue(map.containsValue(1.0d));

		Assert.assertTrue(map.get("one") instanceof Double);
	}

	@Test
	public void testTypeInteger() throws Exception {
		Map<String, Object> map = MapUtil.toLinkedHashMap(
			new String[] {"one:1:int"});

		Assert.assertTrue(map.size() == 1);

		Assert.assertTrue(map.containsKey("one"));
		Assert.assertTrue(map.containsValue(1));

		Assert.assertTrue(map.get("one") instanceof Integer);
	}

	@Test
	public void testTypeLong() throws Exception {
		Map<String, Object> map = MapUtil.toLinkedHashMap(
			new String[] {"one:1:long"});

		Assert.assertTrue(map.size() == 1);

		Assert.assertTrue(map.containsKey("one"));
		Assert.assertTrue(map.containsValue(1l));

		Assert.assertTrue(map.get("one") instanceof Long);
	}

	@Test
	public void testTypeShort() throws Exception {
		Map<String, Object> map = MapUtil.toLinkedHashMap(
			new String[] {"one:1:short"});

		Assert.assertTrue(map.size() == 1);

		Assert.assertTrue(map.containsKey("one"));
		Assert.assertTrue(map.containsValue((short)1));

		Assert.assertTrue(map.get("one") instanceof Short);
	}

	@Test
	public void testTypeString() throws Exception {
		Map<String, Object> map = MapUtil.toLinkedHashMap(
			new String[] {"one:X:" + String.class.getName()});

		Assert.assertTrue(map.size() == 1);

		Assert.assertTrue(map.containsKey("one"));
		Assert.assertTrue(map.containsValue("X"));

		Assert.assertTrue(map.get("one") instanceof String);
	}

	@Test
	public void testZeroLength() throws Exception {
		Map<String, String> map = MapUtil.toLinkedHashMap(new String[] {});

		Assert.assertTrue(map.isEmpty());
	}

}