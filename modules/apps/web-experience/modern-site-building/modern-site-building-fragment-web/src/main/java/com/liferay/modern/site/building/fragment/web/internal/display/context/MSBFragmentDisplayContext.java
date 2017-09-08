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

package com.liferay.modern.site.building.fragment.web.internal.display.context;

import com.liferay.modern.site.building.fragment.constants.MSBFragmentPortletKeys;
import com.liferay.modern.site.building.fragment.model.MSBFragmentCollection;
import com.liferay.modern.site.building.fragment.model.MSBFragmentEntry;
import com.liferay.modern.site.building.fragment.service.MSBFragmentCollectionServiceUtil;
import com.liferay.modern.site.building.fragment.service.MSBFragmentEntryServiceUtil;
import com.liferay.modern.site.building.fragment.service.permission.MSBFragmentPermission;
import com.liferay.modern.site.building.fragment.web.util.MSBFragmentPortletUtil;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jürgen Kappler
 */
public class MSBFragmentDisplayContext {

	public MSBFragmentDisplayContext(
		RenderRequest renderRequest, RenderResponse renderResponse,
		HttpServletRequest request) {

		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_request = request;
	}

	public String getDisplayStyle() {
		if (Validator.isNotNull(_displayStyle)) {
			return _displayStyle;
		}

		PortalPreferences portalPreferences =
			PortletPreferencesFactoryUtil.getPortalPreferences(_request);

		_displayStyle = portalPreferences.getValue(
			MSBFragmentPortletKeys.MODERN_SITE_BUILDING_FRAGMENT,
			"display-style", "icon");

		return _displayStyle;
	}

	public String getEditMSBFragmentCollectionRedirect()
		throws PortalException {

		String redirect = ParamUtil.getString(_request, "redirect");

		if (Validator.isNull(redirect)) {
			PortletURL portletURL = _renderResponse.createRenderURL();

			redirect = portletURL.toString();
		}

		return redirect;
	}

	public String getEditMSBFragmentEntryRedirect() throws PortalException {
		PortletURL portletURL = _renderResponse.createRenderURL();

		portletURL.setParameter("mvcPath", "/view_msb_fragment_entries.jsp");

		if (getMSBFragmentCollectionId() > 0) {
			portletURL.setParameter(
				"msbFragmentCollectionId",
				String.valueOf(getMSBFragmentCollectionId()));
		}

		return portletURL.toString();
	}

	public String getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_request, "keywords");

		return _keywords;
	}

	public MSBFragmentCollection getMSBFragmentCollection()
		throws PortalException {

		if (_msbFragmentCollection != null) {
			return _msbFragmentCollection;
		}

		_msbFragmentCollection =
			MSBFragmentCollectionServiceUtil.fetchMSBFragmentCollection(
				getMSBFragmentCollectionId());

		return _msbFragmentCollection;
	}

	public long getMSBFragmentCollectionId() {
		if (Validator.isNotNull(_msbFragmentCollectionId)) {
			return _msbFragmentCollectionId;
		}

		_msbFragmentCollectionId = ParamUtil.getLong(
			_request, "msbFragmentCollectionId");

		return _msbFragmentCollectionId;
	}

	public String getMSBFragmentCollectionsRedirect() throws PortalException {
		String redirect = ParamUtil.getString(_request, "redirect");

		if (Validator.isNull(redirect)) {
			PortletURL backURL = _renderResponse.createRenderURL();

			backURL.setParameter("mvcPath", "/view.jsp");

			redirect = backURL.toString();
		}

		return redirect;
	}

	public SearchContainer getMSBFragmentCollectionsSearchContainer()
		throws PortalException {

		if (_msbFragmentCollectionsSearchContainer != null) {
			return _msbFragmentCollectionsSearchContainer;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		SearchContainer msbFragmentCollectionsSearchContainer =
			new SearchContainer(
				_renderRequest, _renderResponse.createRenderURL(), null,
				"there-are-no-collections");

		if (!isSearch()) {
			msbFragmentCollectionsSearchContainer.setEmptyResultsMessage(
				"there-are-no-collections.-you-can-add-a-collection-by-" +
					"clicking-the-plus-button-on-the-bottom-right-corner");

			msbFragmentCollectionsSearchContainer.
				setEmptyResultsMessageCssClass(
					"taglib-empty-result-message-header-has-plus-btn");
		}
		else {
			msbFragmentCollectionsSearchContainer.setSearch(true);
		}

		msbFragmentCollectionsSearchContainer.setRowChecker(
			new EmptyOnClickRowChecker(_renderResponse));

		OrderByComparator<MSBFragmentCollection> orderByComparator =
			MSBFragmentPortletUtil.getMSBFragmentCollectionOrderByComparator(
				getOrderByCol(), getOrderByType());

		msbFragmentCollectionsSearchContainer.setOrderByCol(getOrderByCol());
		msbFragmentCollectionsSearchContainer.setOrderByComparator(
			orderByComparator);
		msbFragmentCollectionsSearchContainer.setOrderByType(getOrderByType());
		msbFragmentCollectionsSearchContainer.setRowChecker(
			new EmptyOnClickRowChecker(_renderResponse));

		List<MSBFragmentCollection> msbFragmentCollections = null;
		int msbFragmentCollectionsCount = 0;

		if (isSearch()) {
			msbFragmentCollections =
				MSBFragmentCollectionServiceUtil.getMSBFragmentCollections(
					themeDisplay.getScopeGroupId(), getKeywords(),
					msbFragmentCollectionsSearchContainer.getStart(),
					msbFragmentCollectionsSearchContainer.getEnd(),
					orderByComparator);

			msbFragmentCollectionsCount =
				MSBFragmentCollectionServiceUtil.getMSBFragmentCollectionsCount(
					themeDisplay.getScopeGroupId(), getKeywords());
		}
		else {
			msbFragmentCollections =
				MSBFragmentCollectionServiceUtil.getMSBFragmentCollections(
					themeDisplay.getScopeGroupId(),
					msbFragmentCollectionsSearchContainer.getStart(),
					msbFragmentCollectionsSearchContainer.getEnd(),
					orderByComparator);

			msbFragmentCollectionsCount =
				MSBFragmentCollectionServiceUtil.getMSBFragmentCollectionsCount(
					themeDisplay.getScopeGroupId());
		}

		msbFragmentCollectionsSearchContainer.setTotal(
			msbFragmentCollectionsCount);
		msbFragmentCollectionsSearchContainer.setResults(
			msbFragmentCollections);

		_msbFragmentCollectionsSearchContainer =
			msbFragmentCollectionsSearchContainer;

		return _msbFragmentCollectionsSearchContainer;
	}

	public String getMSBFragmentCollectionTitle() throws PortalException {
		MSBFragmentCollection msbFragmentCollection =
			getMSBFragmentCollection();

		if (msbFragmentCollection == null) {
			return LanguageUtil.get(_request, "add-collection");
		}

		return msbFragmentCollection.getName();
	}

	public SearchContainer getMSBFragmentEntriesSearchContainer()
		throws PortalException {

		if (_msbFragmentEntriesSearchContainer != null) {
			return _msbFragmentEntriesSearchContainer;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		SearchContainer msbFragmentEntriesSearchContainer = new SearchContainer(
			_renderRequest, _renderResponse.createRenderURL(), null,
			"there-are-no-fragments");

		if (!isSearch()) {
			msbFragmentEntriesSearchContainer.setEmptyResultsMessage(
				"there-are-no-fragments.-you-can-add-a-fragment-by-clicking-" +
					"the-plus-button-on-the-bottom-right-corner");
			msbFragmentEntriesSearchContainer.setEmptyResultsMessageCssClass(
				"taglib-empty-result-message-header-has-plus-btn");
		}
		else {
			msbFragmentEntriesSearchContainer.setSearch(true);
		}

		msbFragmentEntriesSearchContainer.setRowChecker(
			new EmptyOnClickRowChecker(_renderResponse));

		OrderByComparator<MSBFragmentEntry> orderByComparator =
			MSBFragmentPortletUtil.getMSBFragmentEntryOrderByComparator(
				getOrderByCol(), getOrderByType());

		msbFragmentEntriesSearchContainer.setOrderByCol(getOrderByCol());
		msbFragmentEntriesSearchContainer.setOrderByComparator(
			orderByComparator);
		msbFragmentEntriesSearchContainer.setOrderByType(getOrderByType());

		List<MSBFragmentEntry> msbFragmentEntries = null;
		int msbFragmentEntriesCount = 0;

		if (isSearch()) {
			msbFragmentEntries =
				MSBFragmentEntryServiceUtil.getMSBFragmentEntries(
					themeDisplay.getScopeGroupId(),
					getMSBFragmentCollectionId(), getKeywords(),
					msbFragmentEntriesSearchContainer.getStart(),
					msbFragmentEntriesSearchContainer.getEnd(),
					orderByComparator);

			msbFragmentEntriesCount =
				MSBFragmentEntryServiceUtil.getMSBFragmentCollectionsCount(
					themeDisplay.getScopeGroupId(),
					getMSBFragmentCollectionId(), getKeywords());
		}
		else {
			msbFragmentEntries =
				MSBFragmentEntryServiceUtil.getMSBFragmentEntries(
					themeDisplay.getScopeGroupId(),
					getMSBFragmentCollectionId(),
					msbFragmentEntriesSearchContainer.getStart(),
					msbFragmentEntriesSearchContainer.getEnd(),
					orderByComparator);

			msbFragmentEntriesCount =
				MSBFragmentEntryServiceUtil.getMSBFragmentCollectionsCount(
					themeDisplay.getScopeGroupId(),
					getMSBFragmentCollectionId());
		}

		msbFragmentEntriesSearchContainer.setResults(msbFragmentEntries);
		msbFragmentEntriesSearchContainer.setTotal(msbFragmentEntriesCount);

		_msbFragmentEntriesSearchContainer = msbFragmentEntriesSearchContainer;

		return _msbFragmentEntriesSearchContainer;
	}

	public MSBFragmentEntry getMSBFragmentEntry() throws PortalException {
		if (_msbFragmentEntry != null) {
			return _msbFragmentEntry;
		}

		_msbFragmentEntry = MSBFragmentEntryServiceUtil.fetchMSBFragmentEntry(
			getMSBFragmentEntryId());

		return _msbFragmentEntry;
	}

	public long getMSBFragmentEntryId() {
		if (Validator.isNotNull(_msbFragmentEntryId)) {
			return _msbFragmentEntryId;
		}

		_msbFragmentEntryId = ParamUtil.getLong(_request, "msbFragmentEntryId");

		return _msbFragmentEntryId;
	}

	public String getMSBFragmentEntryTitle() throws PortalException {
		MSBFragmentEntry msbFragmentEntry = getMSBFragmentEntry();

		if (msbFragmentEntry == null) {
			return LanguageUtil.get(_request, "add-fragment");
		}

		return msbFragmentEntry.getName();
	}

	public String getOrderByCol() {
		if (Validator.isNotNull(_orderByCol)) {
			return _orderByCol;
		}

		_orderByCol = ParamUtil.getString(
			_request, "orderByCol", "create-date");

		return _orderByCol;
	}

	public String getOrderByType() {
		if (Validator.isNotNull(_orderByType)) {
			return _orderByType;
		}

		_orderByType = ParamUtil.getString(_request, "orderByType", "asc");

		return _orderByType;
	}

	public String[] getOrderColumns() {
		return new String[] {"create-date", "name"};
	}

	public boolean isDisabledMSBFragmentCollectionsManagementBar()
		throws PortalException {

		if (_hasMSBFragmentCollectionsResults()) {
			return false;
		}

		if (isSearch()) {
			return false;
		}

		return true;
	}

	public boolean isDisabledMSBFragmentEntriesManagementBar()
		throws PortalException {

		if (_hasMSBFragmentEntriesResults()) {
			return false;
		}

		if (isSearch()) {
			return false;
		}

		return true;
	}

	public boolean isSearch() {
		if (Validator.isNotNull(getKeywords())) {
			return true;
		}

		return false;
	}

	public boolean isShowAddButton(String actionId) {
		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (MSBFragmentPermission.contains(
				themeDisplay.getPermissionChecker(),
				MSBFragmentPermission.RESOURCE_NAME,
				MSBFragmentPortletKeys.MODERN_SITE_BUILDING_FRAGMENT,
				themeDisplay.getSiteGroupId(), actionId)) {

			return true;
		}

		return false;
	}

	public boolean isShowMSBFragmentCollectionsSearch() throws PortalException {
		if (_hasMSBFragmentCollectionsResults()) {
			return true;
		}

		if (isSearch()) {
			return true;
		}

		return false;
	}

	public boolean isShowMSBFragmentEntriesSearch() throws PortalException {
		if (_hasMSBFragmentEntriesResults()) {
			return true;
		}

		if (isSearch()) {
			return true;
		}

		return false;
	}

	private boolean _hasMSBFragmentCollectionsResults() throws PortalException {
		SearchContainer searchContainer =
			getMSBFragmentCollectionsSearchContainer();

		if (searchContainer.getTotal() > 0) {
			return true;
		}

		return false;
	}

	private boolean _hasMSBFragmentEntriesResults() throws PortalException {
		SearchContainer searchContainer =
			getMSBFragmentEntriesSearchContainer();

		if (searchContainer.getTotal() > 0) {
			return true;
		}

		return false;
	}

	private String _displayStyle;
	private String _keywords;
	private MSBFragmentCollection _msbFragmentCollection;
	private Long _msbFragmentCollectionId;
	private SearchContainer _msbFragmentCollectionsSearchContainer;
	private SearchContainer _msbFragmentEntriesSearchContainer;
	private MSBFragmentEntry _msbFragmentEntry;
	private Long _msbFragmentEntryId;
	private String _orderByCol;
	private String _orderByType;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final HttpServletRequest _request;

}