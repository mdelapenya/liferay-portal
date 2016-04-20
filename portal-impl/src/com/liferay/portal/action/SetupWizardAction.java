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

package com.liferay.portal.action;

import com.liferay.portal.kernel.deploy.hot.HotDeployUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalLifecycleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.setup.SetupWizardUtil;
import com.liferay.portal.util.PropsValues;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Manuel de la Peña
 * @author Brian Wing Shun Chan
 */
public class SetupWizardAction extends Action {

	@Override
	public ActionForward execute(
			ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (!PropsValues.SETUP_WIZARD_ENABLED) {
			response.sendRedirect(themeDisplay.getPathMain());
		}

		String cmd = ParamUtil.getString(request, Constants.CMD);

		try {
			if (Validator.isNull(cmd)) {
				return actionMapping.findForward("portal.setup_wizard");
			}
			else if (cmd.equals(Constants.TRANSLATE)) {
				SetupWizardUtil.updateLanguage(request, response);

				return actionMapping.findForward("portal.setup_wizard");
			}
			else if (cmd.equals(Constants.UPDATE)) {
				SetupWizardUtil.updateSetup(request, response);

				PropsValues.SETUP_WIZARD_ENABLED = false;

				HotDeployUtil.setCapturePrematureEvents(false);

				PortalLifecycleUtil.flushInits();
			}

			response.sendRedirect(
				themeDisplay.getPathMain() + "/portal/setup_wizard");

			return null;
		}
		catch (Exception e) {
			if (e instanceof PrincipalException) {
				SessionErrors.add(request, e.getClass());

				return actionMapping.findForward("portal.setup_wizard");
			}

			PortalUtil.sendError(e, request, response);

			return null;
		}
	}

	protected void putMessage(
		HttpServletRequest request, JSONObject jsonObject, String key,
		Object... arguments) {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		String message = themeDisplay.translate(key, arguments);

		jsonObject.put("message", message);
	}

}