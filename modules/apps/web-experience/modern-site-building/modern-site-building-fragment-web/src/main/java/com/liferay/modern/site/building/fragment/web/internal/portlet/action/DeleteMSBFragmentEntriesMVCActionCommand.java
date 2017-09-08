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

package com.liferay.modern.site.building.fragment.web.internal.portlet.action;

import com.liferay.modern.site.building.fragment.constants.MSBFragmentPortletKeys;
import com.liferay.modern.site.building.fragment.service.MSBFragmentEntryService;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + MSBFragmentPortletKeys.MODERN_SITE_BUILDING_FRAGMENT,
		"mvc.command.name=deleteMSBFragmentEntries"
	},
	service = MVCActionCommand.class
)
public class DeleteMSBFragmentEntriesMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long[] deleteMSBFragmentEntryIds = null;

		long msbFragmentEntryId = ParamUtil.getLong(
			actionRequest, "msbFragmentEntryId");

		if (msbFragmentEntryId > 0) {
			deleteMSBFragmentEntryIds = new long[] {msbFragmentEntryId};
		}
		else {
			deleteMSBFragmentEntryIds = ParamUtil.getLongValues(
				actionRequest, "rowIds");
		}

		_msbFragmentEntryService.deleteMSBFragmentEntries(
			deleteMSBFragmentEntryIds);
	}

	@Reference
	private MSBFragmentEntryService _msbFragmentEntryService;

}