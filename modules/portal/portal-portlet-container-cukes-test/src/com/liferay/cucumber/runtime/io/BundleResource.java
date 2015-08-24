package com.liferay.cucumber.runtime.io;

import cucumber.runtime.io.Resource;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Example: bundleentry://587.fwk715858581:6/cucumber/io/BundleResource.class
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

		Pattern pattern = Pattern.compile("bundleentry://\\d+\\.\\w+(?::\\d+)?/(\\S+)");

		Matcher matcher = pattern.matcher(entryURLPath);

		if (matcher.matches()) {
			String fullyQualifiedClassName = matcher.group(1);

			return fullyQualifiedClassName.replace("/", ".");
		}

		throw new IllegalArgumentException(
			"The resource is not a valid bundle resource");
	}

}