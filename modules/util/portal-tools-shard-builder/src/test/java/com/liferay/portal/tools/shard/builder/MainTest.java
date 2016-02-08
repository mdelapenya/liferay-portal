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

package com.liferay.portal.tools.shard.builder;

import com.beust.jcommander.ParameterException;

import com.liferay.portal.tools.shard.builder.internal.util.PropsReader;

import java.io.File;

import java.net.URL;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author Manuel de la Peña
 */
public class MainTest {

	@Test
	public void testMainOnDB2() throws Exception {
		URL url = getClass().getResource("/db2.properties");

		Properties properties = PropsReader.read(url.getFile());

		String schemaName = properties.getProperty("dataSource.user");

		_testMain(url, schemaName);
	}

	@Test
	public void testMainOnMSSQLServer() throws Exception {
		URL url = getClass().getResource("/mssql-server.properties");

		Properties properties = PropsReader.read(url.getFile());

		String schemaName = properties.getProperty("dataSource.databaseName");

		_testMain(url, schemaName);
	}

	@Test
	public void testMainOnMySQL() throws Exception {
		URL url = getClass().getResource("/mysql.properties");

		Properties properties = PropsReader.read(url.getFile());

		String schemaName = properties.getProperty("dataSource.databaseName");

		_testMain(url, schemaName);
	}

	@Test
	public void testMainOnOracle() throws Exception {
		URL url = getClass().getResource("/oracle.properties");

		Properties properties = PropsReader.read(url.getFile());

		String schemaName = properties.getProperty("dataSource.user");

		_testMain(url, schemaName);
	}

	@Test
	public void testMainOnPostgreSQL() throws Exception {
		URL url = getClass().getResource("/postgresql.properties");

		Properties properties = PropsReader.read(url.getFile());

		String schemaName = properties.getProperty("dataSource.databaseName");

		_testMain(url, schemaName);
	}

	@Test
	public void testMainOnSybase() throws Exception {
		URL url = getClass().getResource("/sybase.properties");

		Properties properties = PropsReader.read(url.getFile());

		String schemaName = properties.getProperty("dataSource.databaseName");

		_testMain(url, schemaName);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testValidateEmptyArguments() throws Exception {
		Main.main(new String[0]);
	}

	@Test(expected = ParameterException.class)
	public void testValidateInvalidCompanyId() throws Exception {
		String[] arguments = {
			"-C", "foo", "-O", "bar", "-P", "foo.properties", "-S", _SCHEMA_NAME
		};

		Main.main(arguments);
	}

	@Test(expected = ParameterException.class)
	public void testValidateInvalidOptionArguments() throws Exception {
		Main.main(new String[] {"-X", "foo"});
	}

	@Test(expected = ParameterException.class)
	public void testValidateNonexistingOutputDirectory() throws Exception {
		String[] arguments = {
			"-C", _COMPANY_ID, "-O", "foo", "-P", "foo.properties", "-S",
			_SCHEMA_NAME
		};

		Main.main(arguments);
	}

	@Test(expected = ParameterException.class)
	public void testValidateNonexistingPropertiesFile() throws Exception {
		String[] arguments = {
			"-C", _COMPANY_ID, "-O", "bar", "-P", "foo.properties", "-S",
			_SCHEMA_NAME
		};

		Main.main(arguments);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testValidateNullArguments() throws Exception {
		Main.main(null);
	}

	@Test(expected = ParameterException.class)
	public void testValidateReadOnlyOutputDirectory() throws Exception {
		File readOnlyDir = temporaryFolder.newFolder();

		readOnlyDir.setReadable(false);
		readOnlyDir.setWritable(false);

		String[] arguments = {
			"-C", _COMPANY_ID, "-O", readOnlyDir.getAbsolutePath(), "-P",
			"foo.properties", "-S", _SCHEMA_NAME
		};

		Main.main(arguments);
	}

	@Test
	public void testValidateRequiredArguments() throws Exception {
		String[][] requiredArguments = {
			{"-C", ""}, {"-O", ""}, {"-P", ""}, {"-S", ""}
		};

		for (String[] requiredArgument : requiredArguments) {
			try {
				Main.main(requiredArgument);
			}
			catch (ParameterException pe) {
			}
		}
	}

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private void _testMain(URL propertiesUrl, String schema) throws Exception {
		Assume.assumeNotNull(propertiesUrl);

		File outputFolder = temporaryFolder.getRoot();

		String[] args = new String[] {
			"-P", propertiesUrl.getFile(), "-S", schema, "-C", _COMPANY_ID,
			"-O", outputFolder.getAbsolutePath()
		};

		Main.main(args);

		String[] outputFiles = outputFolder.list();

		Assert.assertTrue(outputFiles.length > 0);
	}

	private static final String _COMPANY_ID = "20156";

	private static final String _SCHEMA_NAME = "lportal";

}