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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.collections.ServiceReferenceMapper;
import com.liferay.registry.collections.ServiceTrackerCollections;
import com.liferay.registry.collections.ServiceTrackerMap;
import com.liferay.registry.util.StringPlus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Manuel de la Peña
 */
public class WebSocketEndpointCache {

	public WebSocketEndpointCache(
		WebSocketEndpoint emptyWebSocketEndpoint, String portletName,
		Class<? extends WebSocketEndpoint> webSocketEndpointClass) {

		_emptyWebSocketEndpoint = emptyWebSocketEndpoint;
		_portletName = portletName;

		_serviceTrackerMap = ServiceTrackerCollections.openSingleValueMap(
			webSocketEndpointClass,
			"(&(javax.portlet.name=" + _portletName +
				")(websocket.endpoint.name=*))",
			new ServiceReferenceMapper<String, WebSocketEndpoint>() {

				@Override
				public void map(
					ServiceReference<WebSocketEndpoint> serviceReference,
					Emitter<String> emitter) {

					List<String> webSocketEndpointNames = StringPlus.asList(
						serviceReference.getProperty(
							"websocket.endpoint.name"));

					for (String webSocketEndpointName :
							webSocketEndpointNames) {

						emitter.emit(webSocketEndpointName);
					}
				}

			});
	}

	public void close() {
		_serviceTrackerMap.close();
	}

	public WebSocketEndpoint getWebSocketEndpoint(
		String webSocketEndpointName) {

		WebSocketEndpoint webSocketEndpoint = _serviceTrackerMap.getService(
			webSocketEndpointName);

		if (webSocketEndpoint != null) {
			return webSocketEndpoint;
		}

		try {
			webSocketEndpoint = _webSocketEndpointCache.get(
				webSocketEndpointName);

			if (webSocketEndpoint != null) {
				return webSocketEndpoint;
			}

			return _emptyWebSocketEndpoint;
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to instantiate a WebSocket endpoint for " +
						webSocketEndpointName);
			}

			_webSocketEndpointCache.put(
				webSocketEndpointName, _emptyWebSocketEndpoint);

			return _emptyWebSocketEndpoint;
		}
	}

	public boolean isEmpty() {
		return _webSocketEndpointCache.isEmpty();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		WebSocketEndpointCache.class);

	private final WebSocketEndpoint _emptyWebSocketEndpoint;
	private final String _portletName;
	private final Map<String, WebSocketEndpoint> _webSocketEndpointCache =
		new ConcurrentHashMap<>();
	private final ServiceTrackerMap<String, ? extends WebSocketEndpoint>
		_serviceTrackerMap;

}