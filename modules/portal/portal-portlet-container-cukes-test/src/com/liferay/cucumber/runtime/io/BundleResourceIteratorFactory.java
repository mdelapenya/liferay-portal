package com.liferay.cucumber.runtime.io;

import cucumber.runtime.io.Resource;
import cucumber.runtime.io.ResourceIteratorFactory;

import java.net.URL;

import java.util.Iterator;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Manuel de la Peña
 */
public class BundleResourceIteratorFactory implements ResourceIteratorFactory {

	@Override
	public boolean isFactoryFor(URL url) {
		return url.getProtocol().equals("bundleresource");
	}

	@Override
	public Iterator<Resource> createIterator(
		URL url, String path, String suffix) {

		Bundle bundle = FrameworkUtil.getBundle(this.getClass());

		return new BundleResourceIterator(bundle, suffix);
	}

}