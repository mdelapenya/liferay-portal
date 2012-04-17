<%--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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
--%>

<%@ include file="/html/portlet/trash/init.jsp" %>

<%
int trashEntriesCount = TrashEntryLocalServiceUtil.getTrashEntriesCount(themeDisplay.getScopeGroupId());
%>

<aui:layout>
	<c:if test="<%= trashEntriesCount > 0 %>">
		<aui:button-row>
			<aui:button name="emptyTrashButton" value="empty-trash" />
		</aui:button-row>
	</c:if>

	<liferay-ui:search-container
		emptyResultsMessage="the-recycle-bin-is-empty"
		headerNames="primaryKey,type, status"
		rowChecker="<%= new RowChecker(renderResponse) %>"
	>
		<liferay-ui:search-container-results
			results="<%= TrashEntryLocalServiceUtil.getEntries(themeDisplay.getScopeGroupId(), searchContainer.getStart(), searchContainer.getEnd()) %>"
			total="<%= trashEntriesCount %>"
		/>

		<liferay-ui:search-container-row
			className="com.liferay.portlet.trash.model.TrashEntry"
			keyProperty="entryId"
			modelVar="trashEntry"
		>

			<liferay-ui:search-container-column-text
				name="primaryKey"
				property="primaryKey"
			/>

			<liferay-ui:search-container-column-text
				name="type"
				property="className"
			/>

			<liferay-ui:search-container-column-text
				name="status"
				value="status"
			/>

		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator paginate="<%= false %>" />
	</liferay-ui:search-container>
</aui:layout>