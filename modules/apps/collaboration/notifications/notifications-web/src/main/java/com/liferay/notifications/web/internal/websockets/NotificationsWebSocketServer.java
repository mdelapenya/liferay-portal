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

package com.liferay.notifications.web.internal.websockets;

import com.liferay.notifications.web.internal.constants.NotificationsPortletKeys;
import com.liferay.portal.kernel.websockets.WebSocketEndpoint;

import org.osgi.service.component.annotations.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * @author Manuel de la Peña
 */
@Component(
	property = {
		"javax.portlet.name=" + NotificationsPortletKeys.NOTIFICATIONS,
		"websocket.endpoint.name=/ws-notifications"
	},
	service = WebSocketEndpoint.class
)
public class NotificationsWebSocketServer implements WebSocketEndpoint {

	@OnClose
	@Override
	public void close(Session session) {
		System.out.println("Closing WebSocket");
	}

	@OnMessage
	@Override
	public void handleMessage(String message, Session session) {
		System.out.println("Handling message");
	}

	@OnError
	@Override
	public void onError(Throwable error) {
		System.err.println("Error");
	}

	@OnOpen
	@Override
	public void open(Session session) {
		System.out.println("Opening WebSocket");
	}

}