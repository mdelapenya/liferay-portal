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

package com.liferay.vulcan.jaxrs.writer.json.internal.exception.mapper;

import com.liferay.vulcan.error.VulcanDeveloperError.MustHaveExceptionConverter;
import com.liferay.vulcan.jaxrs.writer.json.internal.writer.WriterHelper;
import com.liferay.vulcan.logger.VulcanLogger;
import com.liferay.vulcan.message.json.ErrorMessageMapper;
import com.liferay.vulcan.result.APIError;
import com.liferay.vulcan.wiring.osgi.manager.ErrorMessageMapperManager;
import com.liferay.vulcan.wiring.osgi.manager.ExceptionConverterManager;

import java.util.Optional;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * This mapper will be responsible of capturing JAX-RS internal {@link
 * WebApplicationException}, converting them to its corresponding {@link
 * APIError} and writing the error to the response.
 *
 * @author Alejandro Hernández
 */
@Component(immediate = true, property = "liferay.vulcan.exception.mapper=true")
public class WebApplicationExceptionMapper
	implements ExceptionMapper<WebApplicationException> {

	@Override
	public Response toResponse(WebApplicationException exception) {
		Optional<APIError> optional = _exceptionConverterManager.convert(
			exception);

		APIError apiError = optional.orElseThrow(
			() -> new MustHaveExceptionConverter(exception.getClass()));

		_vulcanLogger.error(apiError);

		Response.ResponseBuilder responseBuilder = Response.status(
			apiError.getStatusCode());

		ErrorMessageMapper errorMessageMapper =
			_errorMessageMapperManager.getErrorMessageMapper(
				apiError, _httpHeaders);

		return responseBuilder.entity(
			WriterHelper.writeError(errorMessageMapper, apiError, _httpHeaders)
		).type(
			errorMessageMapper.getMediaType()
		).build();
	}

	@Reference
	private ErrorMessageMapperManager _errorMessageMapperManager;

	@Reference
	private ExceptionConverterManager _exceptionConverterManager;

	@Context
	private HttpHeaders _httpHeaders;

	@Reference
	private VulcanLogger _vulcanLogger;

}