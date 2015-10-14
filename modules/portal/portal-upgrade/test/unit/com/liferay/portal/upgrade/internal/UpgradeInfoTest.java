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

package com.liferay.portal.upgrade.internal;

import com.liferay.portal.kernel.dao.db.DBProcessContext;
import com.liferay.portal.kernel.upgrade.UpgradeStep;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class UpgradeInfoTest {

	@Test
	public void testEqualsReturnsFalseComparingToDifferentInstance() {
		UpgradeStep upgradeStep = new UpgradeStepStub();

		UpgradeInfo upgradeInfo1 = new UpgradeInfo(
			"1.0.0", "2.0.0", upgradeStep);

		Assert.assertFalse(upgradeInfo1.equals(new String()));
	}

	@Test
	public void testEqualsReturnsFalseComparingToDifferentSchemaVersion() {
		UpgradeInfo upgradeInfo1 = new UpgradeInfo(
			"1.0.0", "2.0.0", new UpgradeStepStub());

		UpgradeInfo upgradeInfo2 = new UpgradeInfo(
			"2.0.0", "3.0.0", new UpgradeStepStub());

		Assert.assertFalse(upgradeInfo1.equals(upgradeInfo2));
	}

	@Test
	public void testEqualsReturnsFalseComparingToSameSchemaVersion() {
		UpgradeInfo upgradeInfo1 = new UpgradeInfo(
			"1.0.0", "2.0.0", new UpgradeStepStub());

		UpgradeInfo upgradeInfo2 = new UpgradeInfo(
			"1.0.0", "2.0.0", new UpgradeStepStub());

		Assert.assertFalse(upgradeInfo1.equals(upgradeInfo2));
	}

	@Test
	public void testEqualsReturnsTrue() {
		UpgradeStep upgradeStep = new UpgradeStepStub();

		UpgradeInfo upgradeInfo1 = new UpgradeInfo(
			"1.0.0", "2.0.0", upgradeStep);

		UpgradeInfo upgradeInfo2 = new UpgradeInfo(
			"1.0.0", "2.0.0", upgradeStep);

		Assert.assertTrue(upgradeInfo1.equals(upgradeInfo2));
	}

	@Test
	public void testEqualsReturnsTrueComparingToSameObject() {
		UpgradeInfo upgradeInfo1 = new UpgradeInfo(
			"1.0.0", "2.0.0", new UpgradeStepStub());

		Assert.assertTrue(upgradeInfo1.equals(upgradeInfo1));
	}

	private class UpgradeStepStub implements UpgradeStep {

		@Override
		public void upgrade(DBProcessContext dbProcessContext) {
		}

	}

}