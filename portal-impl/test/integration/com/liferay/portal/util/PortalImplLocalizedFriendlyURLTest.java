/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.util;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.model.VirtualLayoutConstants;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.UserGroupLocalServiceUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.TransactionalExecutionTestListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Sergio González
 */
@ExecutionTestListeners(
	listeners = {
		EnvironmentExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class PortalImplLocalizedFriendlyURLTest {

	@BeforeClass
	public static void setUpClass() {
		_nameMap = new HashMap<Locale, String>();

		_nameMap.put(_enLocale, "Home");
		_nameMap.put(_esLocale, "Inicio");
		_nameMap.put(_frLocale, "Accueil");

		_friendlyURLMap = new HashMap<Locale, String>();

		_friendlyURLMap.put(_enLocale, "/home");
		_friendlyURLMap.put(_esLocale, "/inicio");
		_friendlyURLMap.put(_frLocale, "/accueil");
	}

	@Test
	public void testLocalizedURLSitePrivateLayoutFriendlyURL()
		throws Exception {

		testLocalizedURLSiteLayoutFriendlyURL(true);
	}

	@Test
	public void testLocalizedURLSitePublicLayoutFriendlyURL() throws Exception {
		testLocalizedURLSiteLayoutFriendlyURL(false);
	}

	@Test
	public void testLocalizedURLVirtualPrivateLayoutFriendlyURL()
		throws Exception {

		testLocalizedURLVirtualLayoutFriendlyURL(true);
	}

	@Test
	public void testLocalizedURLVirtualPublicLayoutFriendlyURL()
		throws Exception {

		testLocalizedURLVirtualLayoutFriendlyURL(false);
	}

	@Test
	public void testNonexistentLocalizedSitePrivateLayoutFriendlyURL()
		throws Exception {

		testNonexistentLocalizedSiteLayoutFriendlyURL(true);
	}

	@Test
	public void testNonexistentLocalizedSitePublicLayoutFriendlyURL()
		throws Exception {

		testNonexistentLocalizedSiteLayoutFriendlyURL(false);
	}

	@Test
	public void testNonexistentLocalizedVirtualPrivateLayoutFriendlyURL()
		throws Exception {

		testNonexistentLocalizedVirtualLayoutFriendlyURL(true);
	}

	@Test
	public void testNonexistentLocalizedVirtualPublicLayoutFriendlyURL()
		throws Exception {

		testNonexistentLocalizedVirtualLayoutFriendlyURL(false);
	}

	@Test
	public void testNonexistentWronglyLocalizedSiteLayoutPrivateFriendlyURL()
		throws Exception {

		testNonexistentWronglyLocalizedSiteLayoutFriendlyURL(true);
	}

	@Test
	public void testNonexistentWronglyLocalizedSiteLayoutPublicFriendlyURL()
		throws Exception {

		testNonexistentWronglyLocalizedSiteLayoutFriendlyURL(false);
	}

	@Test
	public void testNonexistentWronglyLocalizedVirtualLayoutPrivateFriendlyURL()
		throws Exception {

		testNonexistentWronglyLocalizedVirtualLayoutFriendlyURL(true);
	}

	@Test
	public void testNonexistentWronglyLocalizedVirtualLayoutPublicFriendlyURL()
		throws Exception {

		testNonexistentWronglyLocalizedVirtualLayoutFriendlyURL(false);
	}

	@Test
	public void testWronglyLocalizedURLSiteLayoutPrivateFriendlyURL1()
		throws Exception {

		testWronglyLocalizedURLSiteLayoutFriendlyURL(true, _enLocale, "/home");
	}

	@Test
	public void testWronglyLocalizedURLSiteLayoutPrivateFriendlyURL2()
		throws Exception {

		testWronglyLocalizedURLSiteLayoutFriendlyURL(
			true, _frLocale, "/accueil");
	}

	@Test
	public void testWronglyLocalizedURLSiteLayoutPublicFriendlyURL1()
		throws Exception {

		testWronglyLocalizedURLSiteLayoutFriendlyURL(false, _enLocale, "/home");
	}

	@Test
	public void testWronglyLocalizedURLSiteLayoutPublicFriendlyURL2()
		throws Exception {

		testWronglyLocalizedURLSiteLayoutFriendlyURL(
			false, _frLocale, "/accueil");
	}

	@Test
	public void testWronglyLocalizedURLVirtualPrivateLayoutFriendlyURL1()
		throws Exception {

		testWronglyLocalizedURLVirtualLayoutFriendlyURL(
			true, _enLocale, "/home");
	}

	@Test
	public void testWronglyLocalizedURLVirtualPrivateLayoutFriendlyURL2()
		throws Exception {

		testWronglyLocalizedURLVirtualLayoutFriendlyURL(
			true, _frLocale, "/accueil");
	}

	@Test
	public void testWronglyLocalizedURLVirtualPublicLayoutFriendlyURL1()
		throws Exception {

		testWronglyLocalizedURLVirtualLayoutFriendlyURL(
			false, _enLocale, "/home");
	}

	@Test
	public void testWronglyLocalizedURLVirtualPublicLayoutFriendlyURL2()
		throws Exception {

		testWronglyLocalizedURLVirtualLayoutFriendlyURL(
			false, _frLocale, "/accueil");
	}

	protected void assertLocalizedURLSiteLayoutFriendlyURL(
			long groupId, Layout layout, String layoutFriendlyURL,
			Locale locale, String expectedLayoutFriendlyURL)
		throws Exception {

		Group group = GroupLocalServiceUtil.getGroup(groupId);

		String groupFriendlyURL = group.getFriendlyURL();

		String groupServletMapping = _PUBLIC_GROUP_SERVLET_MAPPING;

		if (layout.isPrivateLayout()) {
			groupServletMapping = _PRIVATE_GROUP_SERVLET_MAPPING;
		}

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setRequestURI(
			groupServletMapping + groupFriendlyURL + layoutFriendlyURL);
		mockHttpServletRequest.setPathInfo(
			groupFriendlyURL + layoutFriendlyURL);

		String localizedFriendlyURL = PortalUtil.getLocalizedFriendlyURL(
			mockHttpServletRequest, layout, locale);

		StringBundler sb = new StringBundler();

		sb.append(StringPool.SLASH);
		sb.append(PortalUtil.getI18nPathLanguageId(locale, StringPool.BLANK));
		sb.append(groupServletMapping);
		sb.append(group.getFriendlyURL());
		sb.append(expectedLayoutFriendlyURL);

		Assert.assertEquals(sb.toString(), localizedFriendlyURL);
	}

	protected void assertLocalizedURLVirtualLayoutFriendlyURL(
			long userGroupGroupId, Layout layout, String layoutFriendlyURL,
			Locale locale, String expectedLayoutFriendlyURL)
		throws Exception {

		User user = TestPropsValues.getUser();

		Group groupUser = user.getGroup();

		Group userGroupGroup = GroupLocalServiceUtil.getGroup(userGroupGroupId);

		String groupServletMapping = _PUBLIC_GROUP_SERVLET_MAPPING;

		if (layout.isPrivateLayout()) {
			groupServletMapping = _PRIVATE_GROUP_SERVLET_MAPPING;
		}

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		StringBundler sb = new StringBundler(5);

		sb.append(groupServletMapping);
		sb.append(groupUser.getFriendlyURL());
		sb.append(VirtualLayoutConstants.CANONICAL_URL_SEPARATOR);
		sb.append(userGroupGroup.getFriendlyURL());
		sb.append(layoutFriendlyURL);

		mockHttpServletRequest.setRequestURI(sb.toString());

		sb = new StringBundler(4);

		sb.append(groupUser.getFriendlyURL());
		sb.append(VirtualLayoutConstants.CANONICAL_URL_SEPARATOR);
		sb.append(userGroupGroup.getFriendlyURL());
		sb.append(layoutFriendlyURL);

		mockHttpServletRequest.setPathInfo(sb.toString());

		String localizedFriendlyURL = PortalUtil.getLocalizedFriendlyURL(
			mockHttpServletRequest, layout, locale);

		sb = new StringBundler(7);

		sb.append(StringPool.SLASH);
		sb.append(PortalUtil.getI18nPathLanguageId(locale, StringPool.BLANK));
		sb.append(groupServletMapping);
		sb.append(groupUser.getFriendlyURL());
		sb.append(VirtualLayoutConstants.CANONICAL_URL_SEPARATOR);
		sb.append(userGroupGroup.getFriendlyURL());
		sb.append(expectedLayoutFriendlyURL);

		Assert.assertEquals(sb.toString(), localizedFriendlyURL);
	}

	protected void testLocalizedURLSiteLayoutFriendlyURL(boolean privateLayout)
		throws Exception {

		Group group = GroupTestUtil.addGroup();

		Layout layout = LayoutTestUtil.addLayout(
			group.getGroupId(), privateLayout, _nameMap, _friendlyURLMap);

		assertLocalizedURLSiteLayoutFriendlyURL(
			group.getGroupId(), layout, "/inicio", _esLocale, "/inicio");
	}

	protected void testLocalizedURLVirtualLayoutFriendlyURL(
			boolean privateLayout)
		throws Exception {

		Group group = GroupTestUtil.addGroup();

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			group.getGroupId());

		UserGroup userGroup = UserGroupTestUtil.addUserGroup(
			group.getGroupId());

		Group userGroupGroup = userGroup.getGroup();

		Layout layout = LayoutTestUtil.addLayout(
			userGroupGroup.getGroupId(), privateLayout, _nameMap, _friendlyURLMap);

		UserGroupLocalServiceUtil.addUserUserGroup(
			serviceContext.getUserId(), userGroup.getUserGroupId());

		assertLocalizedURLVirtualLayoutFriendlyURL(
			userGroupGroup.getGroupId(), layout, "/inicio", _esLocale,
			"/inicio");
	}

	protected void testNonexistentLocalizedSiteLayoutFriendlyURL(
			boolean privateLayout)
		throws Exception {

		Group group = GroupTestUtil.addGroup();

		Layout layout = LayoutTestUtil.addLayout(
			group.getGroupId(), privateLayout, _nameMap, _friendlyURLMap);

		assertLocalizedURLSiteLayoutFriendlyURL(
			group.getGroupId(), layout, "/home", _deLocale, "/home");
	}

	protected void testNonexistentLocalizedVirtualLayoutFriendlyURL(
			boolean privateLayout)
		throws Exception {

		Group group = GroupTestUtil.addGroup();

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			group.getGroupId());

		UserGroup userGroup = UserGroupTestUtil.addUserGroup(
			group.getGroupId());

		Group userGroupGroup = userGroup.getGroup();

		Layout layout = LayoutTestUtil.addLayout(
			userGroupGroup.getGroupId(), privateLayout, _nameMap,
			_friendlyURLMap);

		UserGroupLocalServiceUtil.addUserUserGroup(
			serviceContext.getUserId(), userGroup.getUserGroupId());

		assertLocalizedURLVirtualLayoutFriendlyURL(
			userGroupGroup.getGroupId(), layout, "/home", _deLocale, "/home");
	}

	protected void testNonexistentWronglyLocalizedSiteLayoutFriendlyURL(
			boolean privateLayout)
		throws Exception {

		Group group = GroupTestUtil.addGroup();

		Layout layout = LayoutTestUtil.addLayout(
			group.getGroupId(), privateLayout, _nameMap, _friendlyURLMap);

		assertLocalizedURLSiteLayoutFriendlyURL(
			group.getGroupId(), layout, "/inicio", _deLocale, "/home");
	}

	protected void testNonexistentWronglyLocalizedVirtualLayoutFriendlyURL(
			boolean privateLayout)
		throws Exception {

		Group group = GroupTestUtil.addGroup();

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			group.getGroupId());

		UserGroup userGroup = UserGroupTestUtil.addUserGroup(
			group.getGroupId());

		Group userGroupGroup = userGroup.getGroup();

		Layout layout = LayoutTestUtil.addLayout(
			userGroupGroup.getGroupId(), privateLayout, _nameMap,
			_friendlyURLMap);

		UserGroupLocalServiceUtil.addUserUserGroup(
			serviceContext.getUserId(), userGroup.getUserGroupId());

		assertLocalizedURLVirtualLayoutFriendlyURL(
			userGroupGroup.getGroupId(), layout, "/inicio", _enLocale, "/home");
	}

	protected void testWronglyLocalizedURLSiteLayoutFriendlyURL(
			boolean privateLayout, Locale locale,
			String expectedLayoutFriendlyURL)
		throws Exception {

		Group group = GroupTestUtil.addGroup();

		Layout layout = LayoutTestUtil.addLayout(
			group.getGroupId(), privateLayout, _nameMap, _friendlyURLMap);

		assertLocalizedURLSiteLayoutFriendlyURL(
			group.getGroupId(), layout, "/inicio", locale,
			expectedLayoutFriendlyURL);
	}

	protected void testWronglyLocalizedURLVirtualLayoutFriendlyURL(
			boolean privateLayout, Locale locale,
			String expectedLayoutFriendlyURL)
		throws Exception {

		Group group = GroupTestUtil.addGroup();

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			group.getGroupId());

		UserGroup userGroup = UserGroupTestUtil.addUserGroup(
			group.getGroupId());

		Group userGroupGroup = userGroup.getGroup();

		Layout layout = LayoutTestUtil.addLayout(
			userGroupGroup.getGroupId(), privateLayout, _nameMap,
			_friendlyURLMap);

		UserGroupLocalServiceUtil.addUserUserGroup(
			serviceContext.getUserId(), userGroup.getUserGroupId());

		assertLocalizedURLVirtualLayoutFriendlyURL(
			userGroupGroup.getGroupId(), layout, "/inicio", locale,
			expectedLayoutFriendlyURL);
	}

	private static final String _PRIVATE_GROUP_SERVLET_MAPPING =
		PropsValues.LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING;

	private static final String _PUBLIC_GROUP_SERVLET_MAPPING =
		PropsValues.LAYOUT_FRIENDLY_URL_PUBLIC_SERVLET_MAPPING;

	private static Locale _deLocale = new Locale("de", "DE");
	private static Locale _enLocale = new Locale("en", "US");
	private static Locale _esLocale = new Locale("es", "ES");
	private static Map<Locale, String> _friendlyURLMap;
	private static Locale _frLocale = new Locale("fr", "FR");
	private static Map<Locale, String> _nameMap;

}