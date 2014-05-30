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

package com.liferay.portal.util.test;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.ServerOptions;
import com.dumbster.smtp.SmtpServer;
import com.dumbster.smtp.SmtpServerFactory;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel de la Peña
 * @author José Manuel Navarro
 */
public class MailServiceTestUtil {

	public static int getInboxSize() {
		return _smtpServer.getEmailCount();
	}

	public static List<MailMessage> getMailMessages(
		String headerName, String headerValue) {

		List<MailMessage> mailMessages = new ArrayList<MailMessage>();

		for (int i = 0; i < _smtpServer.getEmailCount(); ++i) {
			MailMessage message = _smtpServer.getMessage(i);

			if (headerName.equals("Body")) {
				String body = message.getBody();

				if (body.equals(headerValue)) {
					mailMessages.add(message);
				}
			}
			else {
				String messageHeaderValue = message.getFirstHeaderValue(
					headerName);

				if (messageHeaderValue.equals(headerValue)) {
					mailMessages.add(message);
				}
			}
		}

		return mailMessages;
	}

	public static void start() {
		int retryCount = GetterUtil.getInteger(PropsUtil.get(MAIL_RETRY_COUNT));
		int retrySleep = GetterUtil.getInteger(PropsUtil.get(MAIL_RETRY_SLEEP));

		_retryStart(retryCount, retrySleep);
	}

	public static void stop() {
		if ((_smtpServer != null) && _smtpServer.isStopped()) {
			throw new IllegalStateException("Server is already stopped");
		}

		_smtpServer.stop();

		_smtpServer = null;
	}

	private static void _retryStart(int retryCount, int retrySleep) {
		if (retryCount == 0) {
			throw new IllegalStateException("Server is already running");
		}
		else {
			if (_smtpServer != null) {
				try {
					int initialRetryCount =
						GetterUtil.getInteger(PropsUtil.get(MAIL_RETRY_COUNT));

					if (_log.isWarnEnabled()) {
						_log.warn(
							"Retrying for " +
								(initialRetryCount - retryCount + 1) + "time");
					}

					Thread.sleep(retrySleep);
				}
				catch (InterruptedException e) {
					// nothing, retry again
				}

				_retryStart(retryCount--, retrySleep);
			}

			ServerOptions opts = new ServerOptions();
			opts.port = PropsValues.MAIL_SESSION_MAIL_SMTP_PORT;

			_smtpServer = SmtpServerFactory.startServer(opts);
		}
	}

	private static final String MAIL_RETRY_COUNT = "mail.retry.count";

	private static final String MAIL_RETRY_SLEEP = "mail.retry.sleep";

	private static Log _log = LogFactoryUtil.getLog(MailServiceTestUtil.class);

	private static SmtpServer _smtpServer;

}