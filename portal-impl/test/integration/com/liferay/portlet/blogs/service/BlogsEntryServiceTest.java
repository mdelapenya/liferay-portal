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

package com.liferay.portlet.blogs.service;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.Sync;
import com.liferay.portal.test.SynchronousDestinationExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.util.BlogsTestUtil;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.util.MBTestUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Manuel de la Peña
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		SynchronousDestinationExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Sync
public class BlogsEntryServiceTest {

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_entry = BlogsTestUtil.addEntry(
			TestPropsValues.getUserId(), _group, true);
	}

	@After
	public void tearDown() throws Exception {
		GroupLocalServiceUtil.deleteGroup(_group);
	}

	@Test
	public void testAddCommentAny() throws Exception {
		testAddComment(true, WorkflowConstants.STATUS_ANY);
	}

	@Test
	public void testAddCommentApproved() throws Exception {
		testAddComment(true, WorkflowConstants.STATUS_APPROVED);
	}

	protected void testAddComment(boolean workflowEnabled, int status)
		throws Exception {

		int expectedCount =
			MBMessageLocalServiceUtil.getDiscussionMessagesCount(
				BlogsEntry.class.getName(), _entry.getEntryId(), status);

		boolean approved = false;

		if (status == WorkflowConstants.STATUS_APPROVED) {
			approved = true;
		}

		MBTestUtil.addDiscussion(
			_group.getGroupId(), _entry.getModelClassName(),
			_entry.getPrimaryKey(), workflowEnabled, approved);

		int actualCount =
			MBMessageLocalServiceUtil.getDiscussionMessagesCount(
				BlogsEntry.class.getName(), _entry.getEntryId(), status);

		Assert.assertEquals(expectedCount + 1, actualCount);
	}

	private BlogsEntry _entry;
	private Group _group;

}