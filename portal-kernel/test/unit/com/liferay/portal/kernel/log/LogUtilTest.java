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

package com.liferay.portal.kernel.log;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class LogUtilTest {

	@Test
	public void testLogCountBatchDecimalQuotient() {
		int batch = 100;
		int total = 333;

		Assert.assertEquals("0 of 333", LogUtil.logCountBatch(0, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(1, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(150, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(299, total, batch));
		Assert.assertEquals(
			"300 of 333", LogUtil.logCountBatch(300, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(301, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(332, total, batch));
		Assert.assertEquals(
			"333 of 333", LogUtil.logCountBatch(333, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(334, total, batch));
	}

	@Test
	public void testLogCountBatchTotalZero() {
		int batch = 100;
		int total = 0;

		Assert.assertEquals("0 of 0", LogUtil.logCountBatch(0, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(1, total, batch));
	}

	@Test
	public void testLogCountBatchWholeQuotient() {
		int batch = 100;
		int total = 1000;

		Assert.assertEquals(
			"0 of 1000", LogUtil.logCountBatch(0, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(1, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(250, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(499, total, batch));
		Assert.assertEquals(
			"500 of 1000", LogUtil.logCountBatch(500, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(501, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(999, total, batch));
		Assert.assertEquals(
			"1000 of 1000", LogUtil.logCountBatch(1000, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(1001, total, batch));
	}

	@Test
	public void testLogCountBatchWholeQuotient2() {
		int batch = 62;
		int total = 1240;

		Assert.assertEquals(
			"0 of 1240", LogUtil.logCountBatch(0, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(1, total, batch));
		Assert.assertEquals(
			"62 of 1240", LogUtil.logCountBatch(62, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(100, total, batch));
		Assert.assertEquals(
			"124 of 1240", LogUtil.logCountBatch(124, total, batch));
		Assert.assertEquals(
			"620 of 1240", LogUtil.logCountBatch(620, total, batch));
		Assert.assertNull(LogUtil.logCountBatch(1000, total, batch));
		Assert.assertEquals(
			"1178 of 1240", LogUtil.logCountBatch(1178, total, batch));
		Assert.assertEquals(
			"1240 of 1240", LogUtil.logCountBatch(1240, total, batch));
	}

}