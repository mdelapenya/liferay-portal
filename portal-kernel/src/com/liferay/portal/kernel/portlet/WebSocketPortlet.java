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

package com.liferay.portal.kernel.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.websockets.WebSocketEndpoint;
import com.liferay.portal.kernel.websockets.WebSocketEndpointCache;

import javax.portlet.PortletException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * @author Manuel de la Peña
 */
public class WebSocketPortlet extends MVCPortlet {

	@OnClose
	public void close(Session session) {
		WebSocketEndpoint webSocketEndpoint =
			_webSocketEndpointCache.getWebSocketEndpoint(session.getRequestURI().getPath());

		webSocketEndpoint.close(session);
	}

	@Override
	public void destroy() {
		super.destroy();

		_webSocketEndpointCache.close();
	}

	@Override
	public void init() throws PortletException {
		super.init();

		_webSocketEndpointCache = new WebSocketEndpointCache(
			WebSocketEndpoint.EMPTY, getPortletName(), WebSocketEndpoint.class);
	}

	@OnMessage
	public void handleMessage(String message, Session session) {
		WebSocketEndpoint webSocketEndpoint =
			_webSocketEndpointCache.getWebSocketEndpoint(session.getRequestURI().getPath());

		webSocketEndpoint.handleMessage(message, session);
	}

	@OnError
	public void onError(Throwable error) {
		System.err.println("Error on websocket: " + error.getMessage());

		error.printStackTrace();
		/*
		WebSocketEndpoint webSocketEndpoint =
			_webSocketEndpointCache.getWebSocketEndpoint(session.getRequestURI().getPath());

		webSocketEndpoint.onError(error);
		*/
	}

	@OnOpen
	public void open(Session session) {
		WebSocketEndpoint webSocketEndpoint =
			_webSocketEndpointCache.getWebSocketEndpoint(session.getRequestURI().getPath());

		webSocketEndpoint.open(session);
	}

	private WebSocketEndpointCache _webSocketEndpointCache;

}