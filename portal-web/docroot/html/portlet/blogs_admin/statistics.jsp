<%--
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
--%>

<%@ include file="/html/portlet/blogs_admin/init.jsp" %>

<liferay-util:include page="/html/portlet/blogs_admin/toolbar.jsp" />

<div id="<portlet:namespace/>statisticsWrapper">
	<h3 class="header"><liferay-ui:message key="number-of-total-visits-per-period-for-all-blog-entries-in-a-site" /></h3>
	<div class="content">
		<%@ include file="/html/portlet/blogs_admin/total_visits.jspf" %>
	</div>

	<h3 class="header"><liferay-ui:message key="ranking-of-the-most-read-blog-entries" /></h3>
	<div class="content">
		<%@ include file="/html/portlet/blogs_admin/most_read.jspf" %>
	</div>

	<h3 class="header"><liferay-ui:message key="ranking-of-the-most-commented-blog-entries" /></h3>
	<div class="content">
		<%@ include file="/html/portlet/blogs_admin/most_commented.jspf" %>
	</div>

	<h3 class="header"><liferay-ui:message key="ranking-of-the-most-active-users-commenting-blog-entries" /></h3>
	<div class="content">
		<%@ include file="/html/portlet/blogs_admin/most_active_users.jspf" %>
	</div>
</div>

<aui:script use="aui-toggler">
	new A.TogglerDelegate(
		{
			animated: true,
			closeAllOnExpand: true,
			container: '#<portlet:namespace />statisticsWrapper',
			content: '#<portlet:namespace />statisticsWrapper div .content',
			expanded: false,
			header: '#<portlet:namespace />statisticsWrapper h2 .header'
		}
	);
</aui:script>