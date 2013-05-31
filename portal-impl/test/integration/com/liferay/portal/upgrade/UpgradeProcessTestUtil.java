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

import com.liferay.portal.dao.db.DBFactoryImpl;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.tools.DBUpgrader;
import com.liferay.portal.util.InitUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.util.Properties;

/**
 * @author Manuel de la Peña
 */
public class UpgradeProcessTestUtil {

	public static String DATABASE_NAME;

	public static void doUpgrade(Class<? extends UpgradeProcess> clazz)
		throws Exception {

		UpgradeProcess upgradeProcess = clazz.newInstance();

		upgradeProcess.upgrade();
	}

	public static void reloadCurrentSpringDatasources() {
		Properties jdbcProperties = getDefaultDatabaseProperties();

		InitUtil.reloadSpringDatasources(jdbcProperties);
	}

	public static void setUpOriginDatabase(String sql) throws Exception {
		DBFactoryUtil.setDBFactory(new DBFactoryImpl());

		runSQL(sql);

		runSQL(buildPermissionsSQL());

		Properties jdbcProperties = getDefaultDatabaseProperties();

		String url = jdbcProperties.getProperty(PropsKeys.JDBC_DEFAULT_URL);

		url = url.replace("lportal", DATABASE_NAME);

		jdbcProperties.put(PropsKeys.JDBC_DEFAULT_URL, url);

		InitUtil.reloadSpringDatasources(jdbcProperties);
	}

	public static void tearDownOriginDatabase() throws Exception {
		StringBundler sb = new StringBundler(3);

		sb.append("DROP database ");
		sb.append(DATABASE_NAME);
		sb.append(StringPool.SEMICOLON);

		runSQL(sb.toString());
	}

	public static void verifyUpgradedDatabase() throws Exception {
		DBUpgrader.verify();
	}

	protected static String buildPermissionsSQL() {
		StringBundler sb = new StringBundler(9);

		sb.append("GRANT ALL PRIVILEGES ON ");
		sb.append(DATABASE_NAME);
		sb.append(".* TO '");
		sb.append(PropsValues.JDBC_DEFAULT_USERNAME);
		sb.append("'@'");
		sb.append(StringPool.PERCENT);
		sb.append("' IDENTIFIED BY '");
		sb.append(PropsValues.JDBC_DEFAULT_PASSWORD);
		sb.append("' WITH GRANT OPTION;");
		

		return sb.toString();
	}

	protected static Properties getDefaultDatabaseProperties() {
		Properties defaultJdbcProperties = PropsUtil.getProperties(
			"jdbc.default.", false);

		return defaultJdbcProperties;
	}

	protected static void runSQL(String sql) throws Exception {
		Connection con = null;

		try {
			Class.forName(
				PropsValues.JDBC_DEFAULT_DRIVER_CLASS_NAME).newInstance();

			con = DriverManager.getConnection(
				PropsValues.JDBC_DEFAULT_URL, PropsValues.JDBC_DEFAULT_USERNAME,
				PropsValues.JDBC_DEFAULT_PASSWORD);

			Statement st = con.createStatement();

			String[] sqls = sql.split(StringPool.SEMICOLON);

			for (String singleSql : sqls) {
				st.executeUpdate(singleSql);
			}
		}
		finally {
			DataAccess.cleanUp(con);
		}
	}

	static {
		try {
			DATABASE_NAME = "lportal_" + ServiceTestUtil.randomString();
		}
		catch (Exception e) {
			DATABASE_NAME = "lportal_origin";
		}
	}

}