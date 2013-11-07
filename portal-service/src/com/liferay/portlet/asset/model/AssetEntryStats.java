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

package com.liferay.portlet.asset.model;

import com.liferay.portal.kernel.util.Accessor;
import com.liferay.portal.model.PersistedModel;

/**
 * The extended model interface for the AssetEntryStats service. Represents a row in the &quot;AssetEntryStats&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see AssetEntryStatsModel
 * @see com.liferay.portlet.asset.model.impl.AssetEntryStatsImpl
 * @see com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl
 * @generated
 */
public interface AssetEntryStats extends AssetEntryStatsModel, PersistedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to {@link com.liferay.portlet.asset.model.impl.AssetEntryStatsImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<AssetEntryStats, Long> ENTRY_STATS_ID_ACCESSOR = new Accessor<AssetEntryStats, Long>() {
			@Override
			public Long get(AssetEntryStats assetEntryStats) {
				return assetEntryStats.getEntryStatsId();
			}
		};
}