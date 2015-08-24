package com.liferay.cucumber.runtime.io;

import cucumber.runtime.io.Resource;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Example: bundleresource://587.fwk715858581:6/cucumber/api/
 * 
 * @author Manuel de la Peña
 */
public class BundleResource implements Resource {

	private final URL bundleURL;
	private final URL entryURL;

	public BundleResource(URL bundleURL, URL entryURL) {
		this.bundleURL = bundleURL;
		this.entryURL = entryURL;
	}

	@Override
	public String getPath() {
		return entryURL.getPath();
	}

	@Override
	public String getAbsolutePath() {
		return bundleURL.getPath() + getPath();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return bundleURL.openStream();
	}

	@Override
	public String getClassName(String extension) {
		String entryURLPath = entryURL.toString();

		if (entryURLPath.contains("$")) {
			return entryURLPath.substring(entryURLPath.indexOf("$") + 1);
		}
		else if (entryURLPath.indexOf("/") != -1) {
			return entryURLPath.substring(entryURLPath.lastIndexOf("/") + 1);
		}

		return "";
	}

}