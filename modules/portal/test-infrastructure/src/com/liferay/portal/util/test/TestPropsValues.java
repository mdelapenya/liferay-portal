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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @author Alexander Chow
 * @author Raymond Augé
 */
public class TestPropsValues {

	public static long getCompanyId() throws PortalException, SystemException {
		if (_companyId > 0) {
			return _companyId;
		}

		_companyId = PortalUtil.getDefaultCompanyId();

		return _companyId;
	}

	public static long getGroupId() throws PortalException, SystemException {
		if (_groupId > 0) {
			return _groupId;
		}

		Group group = GroupLocalServiceUtil.getGroup(
			getCompanyId(), GroupConstants.GUEST);

		_groupId = group.getGroupId();

		return _groupId;
	}

	public static String getLogin() throws Exception {
		return getLogin(true);
	}

	public static String getLogin(boolean encodeLogin) throws Exception {
		String login = null;

		String authType = PropsValues.COMPANY_SECURITY_AUTH_TYPE;

		if (authType.equals(CompanyConstants.AUTH_TYPE_EA)) {
			login = getUser().getEmailAddress();

			if (encodeLogin) {
				login = HttpUtil.encodeURL(login);
			}
		}
		else if (authType.equals(CompanyConstants.AUTH_TYPE_SN)) {
			login = getUser().getScreenName();
		}
		else if (authType.equals(CompanyConstants.AUTH_TYPE_ID)) {
			login = Long.toString(getUserId());
		}

		return login;
	}

	public static long getPlid() throws Exception {
		return getPlid(getGroupId());
	}

	public static long getPlid(long groupId) throws Exception {
		if (_plid > 0) {
			return _plid;
		}

		_plid = LayoutLocalServiceUtil.getDefaultPlid(groupId);

		return _plid;
	}

	public static User getUser() throws PortalException, SystemException {
		if (_user == null) {
			Role role = RoleLocalServiceUtil.getRole(
				getCompanyId(), RoleConstants.ADMINISTRATOR);

			List<User> users = UserLocalServiceUtil.getRoleUsers(
				role.getRoleId(), 0, 2);

			if (!users.isEmpty()) {
				_user = users.get(0);

				_userId = _user.getUserId();
			}
		}

		return _user;
	}

	public static long getUserId() throws PortalException, SystemException {
		if (_userId == 0) {
			User user = getUser();

			if (user != null) {
				_userId = user.getUserId();
			}
		}

		return _userId;
	}

	private static Log _log = LogFactoryUtil.getLog(TestPropsValues.class);

	private static long _companyId;
	private static long _groupId;
	private static long _plid;
	private static User _user;
	private static long _userId;

}