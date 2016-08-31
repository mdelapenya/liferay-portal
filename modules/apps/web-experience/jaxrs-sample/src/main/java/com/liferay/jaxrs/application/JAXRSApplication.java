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

package com.liferay.jaxrs.application;

import com.liferay.jaxrs.readers.NameTextMessageBodyReader;
import com.liferay.jaxrs.resources.NameResource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.liferay.jaxrs.writers.NameTextMessageBodyWriter;
import org.osgi.service.component.annotations.Component;

/**
 * @author Manuel de la Peña
 */
@ApplicationPath("/api")
@Component(immediate = true, service = Application.class)
public class JAXRSApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		return new HashSet<>(
			Arrays.asList(
				NameResource.class, NameTextMessageBodyWriter.class,
				NameTextMessageBodyReader.class));
	}

	@Override
	public Set<Object> getSingletons() {
		return super.getSingletons();
	}

}