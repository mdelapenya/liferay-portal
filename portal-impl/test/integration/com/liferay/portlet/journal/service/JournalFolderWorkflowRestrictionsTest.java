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

package com.liferay.portlet.journal.service;

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowEngineManager;
import com.liferay.portal.kernel.workflow.WorkflowEngineManagerUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.WorkflowDefinitionLink;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.WorkflowDefinitionLinkLocalServiceUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.Sync;
import com.liferay.portal.test.SynchronousDestinationExecutionTestListener;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.test.GroupTestUtil;
import com.liferay.portal.util.test.ServiceContextTestUtil;
import com.liferay.portal.util.test.TestPropsValues;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.util.test.DDMStructureTestUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleConstants;
import com.liferay.portlet.journal.model.JournalFolder;
import com.liferay.portlet.journal.model.JournalFolderConstants;
import com.liferay.portlet.journal.util.test.JournalTestUtil;

import java.lang.reflect.Field;

import java.util.Collections;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eudaldo Alonso
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		SynchronousDestinationExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Sync
@Transactional
public class JournalFolderWorkflowRestrictionsTest {

	@Before
	public void setUp() throws Exception {
		FinderCacheUtil.clearCache();

		_group = GroupTestUtil.addGroup();

		Field field = ReflectionUtil.getDeclaredField(
			WorkflowEngineManagerUtil.class, "_workflowEngineManager");

		_value = field.get(null);

		field.set(null, new WorkflowEngineManagerImpl());
	}

	@After
	public void tearDown() throws Exception {
		GroupLocalServiceUtil.deleteGroup(_group);

		Field field = ReflectionUtil.getDeclaredField(
			WorkflowEngineManagerUtil.class, "_workflowEngineManager");

		field.set(null, _value);
	}

	@Test
	public void testChangeWorkflowTypeFromDDMStructuresAndWorkflowToInherit()
		throws Exception {

		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			_group.getGroupId(), JournalArticle.class.getName());

		long[] ddmStructureIds = new long[]{ddmStructure.getStructureId()};

		testWorkflowRestrictions(
			ddmStructure.getStructureId(), ddmStructureIds,
			JournalFolderConstants.
				RESTRICTION_TYPE_DDM_STRUCTURES_AND_WORKFLOW);
	}

	@Test
	public void testChangeWorkflowTypeFromDDMStructuresAndWorkflowToWorkflow()
		throws Exception {

		JournalFolder folder = JournalTestUtil.addFolder(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, "Test 1");

		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			_group.getGroupId(), JournalArticle.class.getName());

		ServiceContext serviceContext1 =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		serviceContext1.setAttribute(
			"workflowDefinition" + ddmStructure.getStructureId(),
			"Single Approver@1");

		long[] ddmStructureIds = new long[]{ddmStructure.getStructureId()};

		JournalFolderLocalServiceUtil.updateFolder(
			TestPropsValues.getUserId(), folder.getFolderId(),
			folder.getParentFolderId(), folder.getName(),
			folder.getDescription(), ddmStructureIds,
			JournalFolderConstants.
				RESTRICTION_TYPE_DDM_STRUCTURES_AND_WORKFLOW,
			false, serviceContext1);

		WorkflowDefinitionLink workflowDefinitionLink =
			WorkflowDefinitionLinkLocalServiceUtil.fetchWorkflowDefinitionLink(
				folder.getCompanyId(), _group.getGroupId(),
				JournalFolder.class.getName(), folder.getFolderId(),
				ddmStructure.getStructureId());

		Assert.assertNotNull(workflowDefinitionLink);

		ServiceContext serviceContext2 =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		serviceContext2.setAttribute("workflowDefinition-1", StringPool.BLANK);
		serviceContext2.setAttribute(
			"workflowDefinition" + ddmStructure.getStructureId(),
			"Single Approver@1");

		JournalFolderLocalServiceUtil.updateFolder(
			TestPropsValues.getUserId(), folder.getFolderId(),
			folder.getParentFolderId(), folder.getName(),
			folder.getDescription(), ddmStructureIds,
			JournalFolderConstants.RESTRICTION_TYPE_WORKFLOW, false,
			serviceContext2);

		workflowDefinitionLink =
			WorkflowDefinitionLinkLocalServiceUtil.fetchWorkflowDefinitionLink(
				folder.getCompanyId(), _group.getGroupId(),
				JournalFolder.class.getName(), folder.getFolderId(),
				ddmStructure.getStructureId());

		Assert.assertNull(workflowDefinitionLink);
	}

	@Test
	public void testChangeWorkflowTypeFromWorkflowToInherit() throws Exception {
		testWorkflowRestrictions(
			JournalArticleConstants.DDM_STRUCTURE_ID_ALL, new long[]{},
			JournalFolderConstants.RESTRICTION_TYPE_WORKFLOW);
	}

	@Test
	public void testWorkflowRestrictionsAfterRemoveFolder() throws Exception {
		JournalFolder folder = JournalTestUtil.addFolder(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, "Test 1");

		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			_group.getGroupId(), JournalArticle.class.getName());

		long[] ddmStructureIds = new long[]{ddmStructure.getStructureId()};

		ServiceContext serviceContext1 =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		serviceContext1.setAttribute(
			"workflowDefinition" + ddmStructure.getStructureId(),
			"Single Approver@1");

		JournalFolderLocalServiceUtil.updateFolder(
			TestPropsValues.getUserId(), folder.getFolderId(),
			folder.getParentFolderId(), folder.getName(),
			folder.getDescription(), ddmStructureIds,
			JournalFolderConstants.RESTRICTION_TYPE_DDM_STRUCTURES_AND_WORKFLOW,
			false, serviceContext1);

		WorkflowDefinitionLink workflowDefinitionLink =
			WorkflowDefinitionLinkLocalServiceUtil.fetchWorkflowDefinitionLink(
				folder.getCompanyId(), _group.getGroupId(),
				JournalFolder.class.getName(), folder.getFolderId(),
				ddmStructure.getStructureId());

		Assert.assertNotNull(workflowDefinitionLink);

		JournalFolderLocalServiceUtil.deleteFolder(folder.getFolderId());

		workflowDefinitionLink =
			WorkflowDefinitionLinkLocalServiceUtil.fetchWorkflowDefinitionLink(
				folder.getCompanyId(), _group.getGroupId(),
				JournalFolder.class.getName(), folder.getFolderId(),
				ddmStructure.getStructureId());

		Assert.assertNull(workflowDefinitionLink);
	}

	protected void testWorkflowRestrictions(
			long ddmStructureId, long[] ddmStructureIds, int restrictionType)
		throws Exception {

		JournalFolder folder = JournalTestUtil.addFolder(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, "Test 1");

		ServiceContext serviceContext1 =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		serviceContext1.setAttribute(
			"workflowDefinition" + ddmStructureId, "Single Approver@1");

		JournalFolderLocalServiceUtil.updateFolder(
			TestPropsValues.getUserId(), folder.getFolderId(),
			folder.getParentFolderId(), folder.getName(),
			folder.getDescription(), ddmStructureIds, restrictionType, false,
			serviceContext1);

		WorkflowDefinitionLink workflowDefinitionLink =
			WorkflowDefinitionLinkLocalServiceUtil.fetchWorkflowDefinitionLink(
				folder.getCompanyId(), _group.getGroupId(),
				JournalFolder.class.getName(), folder.getFolderId(),
				ddmStructureId);

		Assert.assertNotNull(workflowDefinitionLink);

		ServiceContext serviceContext2 =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		JournalFolderLocalServiceUtil.updateFolder(
			TestPropsValues.getUserId(), folder.getFolderId(),
			folder.getParentFolderId(), folder.getName(),
			folder.getDescription(), ddmStructureIds,
			JournalFolderConstants.RESTRICTION_TYPE_INHERIT, false,
			serviceContext2);

		workflowDefinitionLink =
			WorkflowDefinitionLinkLocalServiceUtil.fetchWorkflowDefinitionLink(
				folder.getCompanyId(), _group.getGroupId(),
				JournalFolder.class.getName(), folder.getFolderId(),
				ddmStructureId);

		Assert.assertNull(workflowDefinitionLink);
	}

	private Group _group;
	private Object _value;

	private class WorkflowEngineManagerImpl implements WorkflowEngineManager {

		@Override
		public String getKey() {
			return "liferay";
		}

		@Override
		public String getName() {
			return "liferay test workflow engine";
		}

		@Override
		public Map<String, Object> getOptionalAttributes() {
			return Collections.emptyMap();
		}

		@Override
		public String getVersion() {
			return "7.0.0";
		}

		@Override
		public boolean isDeployed() {
			return true;
		}
	}

}