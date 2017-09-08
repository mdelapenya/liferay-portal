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

package com.liferay.modern.site.building.fragment.util.comparator;

import com.liferay.modern.site.building.fragment.model.MSBFragmentCollection;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * @author Jürgen Kappler
 */
public class MSBFragmentCollectionCreateDateComparator
	extends OrderByComparator<MSBFragmentCollection> {

	public static final String ORDER_BY_ASC =
		"MSBFragmentCollection.createDate ASC";

	public static final String ORDER_BY_DESC =
		"MSBFragmentCollection.createDate DESC";

	public static final String[] ORDER_BY_FIELDS = {"createDate"};

	public MSBFragmentCollectionCreateDateComparator() {
		this(true);
	}

	public MSBFragmentCollectionCreateDateComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(
		MSBFragmentCollection msbFragmentCollection1,
		MSBFragmentCollection msbFragmentCollection2) {

		int value = DateUtil.compareTo(
			msbFragmentCollection1.getCreateDate(),
			msbFragmentCollection2.getCreateDate());

		if (_ascending) {
			return value;
		}
		else {
			return -value;
		}
	}

	@Override
	public String getOrderBy() {
		if (_ascending) {
			return ORDER_BY_ASC;
		}
		else {
			return ORDER_BY_DESC;
		}
	}

	@Override
	public String[] getOrderByFields() {
		return ORDER_BY_FIELDS;
	}

	@Override
	public boolean isAscending() {
		return _ascending;
	}

	private final boolean _ascending;

}