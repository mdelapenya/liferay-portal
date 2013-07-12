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

package com.liferay.portal.upgrade;

import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.ServiceTestUtil;

import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public abstract class BaseUpgradeProcessTestCase {

	@Before
	public void setUp() throws Exception {
		databaseName = "lportal_" + ServiceTestUtil.randomString();

		String createSql = readOriginDatabaseFile();

		UpgradeProcessTestUtil.setUpOriginDatabase(createSql, databaseName);

		preConditions();
	}

	@After
	public void tearDown() throws Exception {
		try {
			UpgradeProcessTestUtil.verifyUpgradedDatabase();

			postConditions();

			UpgradeProcessTestUtil.tearDownOriginDatabase(databaseName);
		}
		finally {
			UpgradeProcessTestUtil.reloadDatasources();
		}
	}

	@Test
	public void testUpgrade() throws Exception {
		Class<? extends UpgradeProcess> upgradeClass = getUpgradeClass();

		UpgradeProcessTestUtil.doUpgrade(upgradeClass);
	}

	protected abstract String getOriginVersion();

	protected abstract Class<? extends UpgradeProcess> getUpgradeClass();

	protected void postConditions() {
	}

	protected void preConditions() {
	}

	protected String readOriginDatabaseFile() throws Exception {
		StringBundler sb = new StringBundler(7);

		String type = DBFactoryUtil.getDB().getType();

		sb.append("dependencies/portal-");
		sb.append(type.toLowerCase());
		sb.append(StringPool.DASH);
		sb.append(getOriginVersion());
		sb.append(".sql");

		Class<?> clazz = this.getClass();

		InputStream inputStream = clazz.getResourceAsStream(sb.toString());

		String sql = StringUtil.read(inputStream);

		return StringUtil.replace(sql, "lportal", databaseName);
	}

	protected String databaseName;

}