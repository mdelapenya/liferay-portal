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
		if ((_smtpServer != null) && !_smtpServer.isStopped()) {
			return;
		}

		ServerOptions opts = new ServerOptions();

		opts.port = GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.MAIL_SESSION_MAIL_SMTP_PORT));

		_smtpServer = SmtpServerFactory.startServer(opts);
	}

	public static void stop() {
		if ((_smtpServer == null) || _smtpServer.isStopped()) {
			return;
		}

		_smtpServer.stop();

		if (waitForClose()) {
			_smtpServer = null;
		}
		else {
			throw new IllegalStateException(
				"Couldn't close the mail service after retries");
		}
	}

	protected static boolean waitForClose() {
		int retryCount = GetterUtil.getInteger(PropsUtil.get(MAIL_RETRY_COUNT));
		int retrySleep = GetterUtil.getInteger(PropsUtil.get(MAIL_RETRY_SLEEP));

		int currentRetry = 0;

		while (!_smtpServer.isStopped() && currentRetry < retryCount) {
			if (_log.isWarnEnabled()) {
				_log.warn("Waiting for mail server close... " +
					"(retry #" + (currentRetry + 1) + ")");
			}

			try {
				Thread.sleep(retrySleep);
			}
			catch (InterruptedException e) {
				// nothing, retry again
			}

			currentRetry++;
		}

		return _smtpServer.isStopped();
	}

	private static final String MAIL_RETRY_COUNT = "mail.retry.count";

	private static final String MAIL_RETRY_SLEEP = "mail.retry.sleep";

	private static Log _log = LogFactoryUtil.getLog(MailServiceTestUtil.class);

	private static SmtpServer _smtpServer;

}