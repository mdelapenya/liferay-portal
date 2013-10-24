/*
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

package com.liferay.portlet.asset.service.persistence

import com.liferay.portal.kernel.dao.orm.QueryUtil
import com.liferay.portal.kernel.test.ExecutionTestListeners
import com.liferay.portal.kernel.transaction.Transactional
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil
import com.liferay.portal.kernel.util.StringPool

import com.liferay.portal.model.Group
import com.liferay.portal.model.GroupConstants
import com.liferay.portal.model.Layout
import com.liferay.portal.model.User

import com.liferay.portal.security.permission.PermissionChecker
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil
import com.liferay.portal.security.permission.PermissionThreadLocal
import com.liferay.portal.service.GroupLocalServiceUtil
import com.liferay.portal.service.ServiceContext
import com.liferay.portal.service.ServiceTestUtil

import com.liferay.portal.test.EnvironmentExecutionTestListener
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner
import com.liferay.portal.test.TransactionalExecutionTestListener

import com.liferay.portal.util.GroupTestUtil
import com.liferay.portal.util.LayoutTestUtil
import com.liferay.portal.util.PortalUtil
import com.liferay.portal.util.TestPropsValues

import com.liferay.portal.util.UserTestUtil

import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil

import com.liferay.portlet.blogs.model.BlogsEntry
import com.liferay.portlet.blogs.util.BlogsTestUtil

import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith

/**
 * @author Carlos Sierra Andrés
 */

@ExecutionTestListeners(
	listeners = [
		EnvironmentExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	]
	)
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
class AssetTagFinderTestGroovy {

	@Before
	public void setUp() {
		scopeGroup = addScopeGroup()
		siteGroup = scopeGroup.getParentGroup()

		assetTagName = ServiceTestUtil.randomString();
	}

	@Test public void testFilterCountByG_C_N() {
		long classNameId = PortalUtil.getClassNameId(BlogsEntry.class)

		testWithPermissions(
			that: [(scopeGroup): incrementsOne, (siteGroup): doesNotIncrement],
			when: {
					AssetTagFinderUtil.filterCountByG_C_N(
						probe.getGroupId(), classNameId, assetTagName)
			},
			doing: { addBlogsEntry(scopeGroup.getGroupId(), assetTagName) }
		)
	}

	@Test public void testFilterCountByG_N() {
		testWithPermissions(
			that: [(scopeGroup): incrementsOne, (siteGroup): doesNotIncrement],
			when: {
					AssetTagFinderUtil.filterCountByG_N(
						probe.getGroupId(), assetTagName)
			},
			doing: { addBlogsEntry(scopeGroup.getGroupId(), assetTagName) }
		)
	}

	@Test public void testFilterCountByG_N_P() {
		String[] assetTagProperties = ["key:value"] as String[]

		testWithPermissions(
			that: [(scopeGroup): doesNotIncrement, (siteGroup): incrementsOne],
			when: {
					AssetTagFinderUtil.filterCountByG_N_P(
					probe.getGroupId(), assetTagName, assetTagProperties)
			},
			doing: { addAssetTag(
				siteGroup.getGroupId(), assetTagName, assetTagProperties) }
		)
	}

	@Test public void testFilterFindByG_C_N() {
		long classNameId = PortalUtil.getClassNameId(BlogsEntry.class)

		testWithPermissions(
			that: [(scopeGroup): incrementsOne, (siteGroup): doesNotIncrement],
			when: {
					AssetTagFinderUtil.filterFindByG_C_N(
						probe.getGroupId(), classNameId, assetTagName,
						QueryUtil.ALL_POS, QueryUtil.ALL_POS, null).size()
			},
			doing: { addBlogsEntry(scopeGroup.getGroupId(), assetTagName) }
		)
	}

	@Test public void testFilterFindByG_N() {
		addAssetTag(siteGroup.getGroupId(), assetTagName, null)

		testWithPermissions(
			that: [(scopeGroup): throwsException ],
			when: {
				catchException {
					AssetTagFinderUtil.filterFindByG_N(
						probe.getGroupId(), assetTagName)
				}
			}
		)

		testWithPermissions(
			that: [(siteGroup): isEqualsLowerCase(assetTagName)],
			when: {
					AssetTagFinderUtil.filterFindByG_N(
					probe.getGroupId(), assetTagName).getName()
			}
		)
	}

	@Test public void testFilterFindByG_N_P() {
		String[] assetTagProperties = ["key:value"] as String[]

		testWithPermissions(
			that: [(scopeGroup): doesNotIncrement, (siteGroup): incrementsOne],
			when: { AssetTagFinderUtil.filterFindByG_N_P(
					[probe.getGroupId()] as long[], assetTagName,
					assetTagProperties, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null).size()
			},
			doing: { addAssetTag(
				siteGroup.getGroupId(), assetTagName, assetTagProperties)
			}
		)
	}

	protected String assetTagName;

	protected def isEqualsLowerCase =
		{ to -> { x, _ -> assert x == to.toLowerCase() } }

	protected def incrementsOne = { pre, post -> assert (pre + 1) == post }
	protected def doesNotIncrement = { pre, post -> assert pre == post }
	protected def throwsException =
		{ pre, _ -> assert pre instanceof Exception }

	protected Group scopeGroup;
	protected Group siteGroup;

	protected void addAssetTag(long groupId, String name, String[] properties)
		throws Exception {

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			groupId);

		AssetTagLocalServiceUtil.addTag(
			TestPropsValues.getUserId(), name, properties, serviceContext);
	}

	protected void addBlogsEntry(long groupId, String tagName)
		throws Exception {

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			groupId);

		serviceContext.setAssetTagNames([tagName] as String[]);

		BlogsTestUtil.addEntry(
			TestPropsValues.getUserId(), ServiceTestUtil.randomString(), true,
			serviceContext);
	}

	protected Group addScopeGroup() throws Exception {
		Group group = GroupTestUtil.addGroup();

		Layout layout = LayoutTestUtil.addLayout(
			group.getGroupId(), ServiceTestUtil.randomString());

		String name = ServiceTestUtil.randomString();
		String description = ServiceTestUtil.randomString();
		String friendlyURL =
			StringPool.SLASH + FriendlyURLNormalizerUtil.normalize(name);
		ServiceContext serviceContext = ServiceTestUtil.getServiceContext(
			group.getGroupId());

		Group scopeGroup = GroupLocalServiceUtil.addGroup(
			TestPropsValues.getUserId(), group.getParentGroupId(),
			Layout.class.getName(), layout.getPlid(),
			GroupConstants.DEFAULT_LIVE_GROUP_ID, name, description,
			GroupConstants.TYPE_SITE_OPEN, true,
			GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, friendlyURL, false,
			true, serviceContext);

		return scopeGroup;
	}

	private void testWithPermissions(Map m) {
		def previous = m.that.collect{ probe, assertion ->
				def localWhen = m.when.clone()
				localWhen.delegate = [probe: probe]
				[probe, assertion, localWhen()]
			}

		if (m.doing) {
			m.doing()
		}

		User user = UserTestUtil.addUser(null, 0);

		PermissionChecker originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		try {
			PermissionChecker permissionChecker =
				PermissionCheckerFactoryUtil.create(user);

			PermissionThreadLocal.setPermissionChecker(permissionChecker);

			previous.each{probe, assertion, prev ->
				def localWhen = m.when.clone()
				localWhen.delegate = [probe: probe]
				assertion(prev, localWhen())
			}
		}
		finally {
			PermissionThreadLocal.setPermissionChecker(
				originalPermissionChecker);
		}
	}

	private Exception catchException (Closure body) {
		try {
			body();

			assert fail;
		}
		catch (Exception e) {
			return e;
		}
	}

}