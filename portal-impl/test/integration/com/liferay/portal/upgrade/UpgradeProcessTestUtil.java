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

package com.liferay.portal.upgrade;

import com.liferay.portal.dao.db.DBFactoryImpl;
import com.liferay.portal.dao.jdbc.util.DataSourceSwapper;
import com.liferay.portal.events.StartupAction;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.CentralizedThreadLocal;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.webcache.WebCachePoolUtil;
import com.liferay.portal.service.QuartzLocalServiceUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.util.InitUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import java.io.InputStream;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.util.Map;
import java.util.Properties;

/**
 * @author Manuel de la Peña
 */
public class UpgradeProcessTestUtil {

	public static void doUpgrade(Class<? extends UpgradeProcess> clazz)
		throws Exception {

		UpgradeProcess upgradeProcess = clazz.newInstance();

		upgradeProcess.upgrade();
	}

	public static void setUpOriginDatabase(Class<?> classUnderTest) {
		readTestUpgradeProcessProperties();

		DBFactoryUtil.setDBFactory(new DBFactoryImpl());

		try {
			runSQL(buildPermissionsSQL());

			runSQL(readOriginDatabaseFile(classUnderTest));

			if (_initialized) {
				return;
			}

			Properties jdbcProperties = new Properties();

			jdbcProperties.putAll(readTestUpgradeProcessProperties());

			reloadSpringDatasources(jdbcProperties);

			InitUtil.initWithSpring(true);

			_initialized = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void tearDownOriginDatabase() {
		if (_databaseType == null) {
			return;
		}

		StringBundler sb = new StringBundler(5);

		sb.append("DROP database ");
		sb.append(_DATABASE_NAME);
		sb.append(";");

		try {
			runSQL(sb.toString());

			// at the end, current spring configuration is reloaded

			reloadSpringDatasources(_currentJDBCProperties);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void verifyUpgradedDatabase() {
		try {
			QuartzLocalServiceUtil.checkQuartzTables();

			StartupAction startupAction = new StartupAction();

			startupAction.run(null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static String buildPermissionsSQL() {
		StringBundler sb = new StringBundler(8);

		sb.append("GRANT ALL PRIVILEGES ON ");
		sb.append(_DATABASE_NAME);
		sb.append(".* TO '");
		sb.append(_user);
		sb.append("'@'localhost' IDENTIFIED BY '");
		sb.append(_password);
		sb.append("' WITH GRANT OPTION;");

		return sb.toString();
	}

	protected static String readOriginDatabaseFile(Class<?> classUnderTest)
		throws Exception {

		StringBundler sb = new StringBundler(3);

		sb.append("dependencies/create-");
		sb.append(_databaseType);
		sb.append(".sql");

		InputStream inputStream = classUnderTest.getResourceAsStream(
			sb.toString());

		String sql = StringUtil.read(inputStream);

		return StringUtil.replace(sql, "lportal", _DATABASE_NAME);
	}

	protected static UnicodeProperties readTestUpgradeProcessProperties() {
		_rootPassword = PropsUtil.get("jdbc.root.password");
		_rootUser = PropsUtil.get("jdbc.root.username");

		_password = PropsUtil.get("upgrade.process.jdbc.origin.password");
		_user = PropsUtil.get("upgrade.process.jdbc.origin.username");

		_databaseType = PropsUtil.get("upgrade.process.jdbc.origin.type");

		_currentJDBCProperties = new Properties();

		_currentJDBCProperties.put(
			"jdbc.default.driverClassName",_driverClassName);
		_currentJDBCProperties.put("jdbc.default.url", _url);
		_currentJDBCProperties.put("jdbc.default.username", _user);
		_currentJDBCProperties.put("jdbc.default.password", _password);

		Filter filter = new Filter(_databaseType);

		_driverClassName = PropsUtil.get(
			"upgrade.process.jdbc.origin.driverClassName", filter);

		_url = PropsUtil.get("upgrade.process.jdbc.origin.url", filter);

		UnicodeProperties unicodeProperties = new UnicodeProperties();

		unicodeProperties.put("jdbc.default.driverClassName",_driverClassName);
		unicodeProperties.put("jdbc.default.url", _url);
		unicodeProperties.put("jdbc.default.username", _user);
		unicodeProperties.put("jdbc.default.password", _password);

		return unicodeProperties;
	}

	protected static void reloadSpringDatasources(Properties jdbcProperties) {
		try {

			// Data sources

			jdbcProperties = PropertiesUtil.getProperties(
				jdbcProperties, "jdbc.default.", true);

			DataSourceSwapper.swapCounterDataSource(jdbcProperties);
			DataSourceSwapper.swapLiferayDataSource(jdbcProperties);

			// Caches

			CacheRegistryUtil.clear();
			MultiVMPoolUtil.clear();
			WebCachePoolUtil.clear();
			CentralizedThreadLocal.clearShortLivedThreadLocals();

			// Persistence beans

			_reconfigurePersistenceBeans();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static void runSQL(String sql) throws Exception {
		Connection con = null;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			con = DriverManager.getConnection(
				PropsValues.JDBC_DEFAULT_URL, _rootUser, _rootPassword);

			Statement st = con.createStatement();

			String[] sqls = sql.split(";");

			for (String singelSql : sqls) {
				st.executeUpdate(singelSql);
			}
		}
		finally {
			DataAccess.cleanUp(con);
		}
	}

	private static void _reconfigurePersistenceBeans() throws Exception {
		@SuppressWarnings("rawtypes")
		Map<String, BasePersistenceImpl> beanPersistenceImpls =
			PortalBeanLocatorUtil.locate(BasePersistenceImpl.class);

		SessionFactory sessionFactory =
			(SessionFactory)PortalBeanLocatorUtil.locate(
				"liferaySessionFactory");

		for (String name : beanPersistenceImpls.keySet()) {
			BasePersistenceImpl<?> beanPersistenceImpl =
				beanPersistenceImpls.get(name);

			beanPersistenceImpl.setSessionFactory(sessionFactory);
		}
	}

	private static final String _DATABASE_NAME = "lportal_origin";

	private static Properties _currentJDBCProperties;

	private static String _databaseType;

	private static String _driverClassName;
	private static boolean _initialized;
	private static String _password;
	private static String _rootPassword;
	private static String _rootUser;
	private static String _url;
	private static String _user;

}