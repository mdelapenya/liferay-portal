/**
 * Copyright (c) 2000-2008 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.sun.com/cddl/cddl.html and
 * legal/CDDLv1.0.txt. See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at legal/CDDLv1.0.txt.
 *
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Copyright 2008 Sun Microsystems Inc. All rights reserved.
 **/

package com.liferay.taglib.ruon;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <a href="UserPresenceTag.java.html"><b><i>View Source</i></b></a>
 *
 * @author Murali Krishna Reddy
 *
 */
public class UserPresenceTag extends TagSupport{

	public int doStartTag() throws JspException {
		try {
			JSONObject ruonPresenceJSON = JSONFactoryUtil.createJSONObject();

			JSONObject getPresenceStatusRequestJSON =
				JSONFactoryUtil.createJSONObject();

			getPresenceStatusRequestJSON.put("userId",_userId);

			ruonPresenceJSON.put(
				"getPresenceStatusRequest", getPresenceStatusRequestJSON);

			String ruonPresenceResponse =
				MessageBusUtil.sendSynchronizedMessage(
					DestinationNames.RUON, ruonPresenceJSON.toString());

			String presenceStatus = "";

			if (ruonPresenceResponse != null){
				JSONObject ruonPresenceResponseJSON =
					JSONFactoryUtil.createJSONObject(ruonPresenceResponse);

				JSONObject presenceStatusResponseJSON =
					ruonPresenceResponseJSON.getJSONObject(
						"getPresenceStatusResponse");

				if (presenceStatusResponseJSON != null){
					presenceStatus =
						presenceStatusResponseJSON.getString("presenceStatus");
				}
			}

				pageContext.getOut().print(presenceStatus);

		}
		catch (Exception e) {
			throw new JspException(e);
		}

		return SKIP_BODY;
	}

	public void setUserId(Long userId) {
		_userId = userId;
	}

	private Long _userId;

}