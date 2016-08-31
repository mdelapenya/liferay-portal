package com.liferay.jaxrs.readers;
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import com.liferay.jaxrs.resources.NameResource;
import com.liferay.portal.kernel.util.StringUtil;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import java.io.IOException;
import java.io.InputStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author Manuel de la Peña
 */
public class NameTextMessageBodyReader
	implements MessageBodyReader<NameResource.Name> {

	@Override
	public boolean isReadable(
		Class<?> type, Type genericType, Annotation[] annotations,
		MediaType mediaType) {

		return (type.isAssignableFrom(NameResource.Name.class) &&
			mediaType.isCompatible(MediaType.TEXT_PLAIN_TYPE));
	}

	@Override
	public NameResource.Name readFrom(
			Class<NameResource.Name> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream)
		throws IOException, WebApplicationException {

		return new NameResource.Name(StringUtil.read(entityStream));
	}

}