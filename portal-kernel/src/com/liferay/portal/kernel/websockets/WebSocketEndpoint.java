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

package com.liferay.portal.kernel.websockets;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * @author Manuel de la Peña
 */
public interface WebSocketEndpoint {

	public static final WebSocketEndpoint EMPTY = new WebSocketEndpoint() {

		@OnClose
		@Override
		public void close(Session session) {
		}

		@OnMessage
		@Override
		public void handleMessage(String message, Session session) {
		}

		@OnError
		@Override
		public void onError(Throwable error) {
		}

		@OnOpen
		@Override
		public void open(Session session) {
		}

	};

	@OnClose
	public void close(Session session);

	@OnMessage
	public void handleMessage(String message, Session session);

	@OnError
	public void onError(Throwable error);

	@OnOpen
	public void open(Session session);

}