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

package com.liferay.portlet.asset.service;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;

/**
 * Provides the local service utility for AssetEntryStats. This utility wraps
 * {@link com.liferay.portlet.asset.service.impl.AssetEntryStatsLocalServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see AssetEntryStatsLocalService
 * @see com.liferay.portlet.asset.service.base.AssetEntryStatsLocalServiceBaseImpl
 * @see com.liferay.portlet.asset.service.impl.AssetEntryStatsLocalServiceImpl
 * @generated
 */
public class AssetEntryStatsLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.portlet.asset.service.impl.AssetEntryStatsLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* Adds the asset entry stats to the database. Also notifies the appropriate model listeners.
	*
	* @param assetEntryStats the asset entry stats
	* @return the asset entry stats that was added
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portlet.asset.model.AssetEntryStats addAssetEntryStats(
		com.liferay.portlet.asset.model.AssetEntryStats assetEntryStats)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().addAssetEntryStats(assetEntryStats);
	}

	/**
	* Creates a new asset entry stats with the primary key. Does not add the asset entry stats to the database.
	*
	* @param entryStatsId the primary key for the new asset entry stats
	* @return the new asset entry stats
	*/
	public static com.liferay.portlet.asset.model.AssetEntryStats createAssetEntryStats(
		long entryStatsId) {
		return getService().createAssetEntryStats(entryStatsId);
	}

	/**
	* Deletes the asset entry stats with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param entryStatsId the primary key of the asset entry stats
	* @return the asset entry stats that was removed
	* @throws PortalException if a asset entry stats with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portlet.asset.model.AssetEntryStats deleteAssetEntryStats(
		long entryStatsId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().deleteAssetEntryStats(entryStatsId);
	}

	/**
	* Deletes the asset entry stats from the database. Also notifies the appropriate model listeners.
	*
	* @param assetEntryStats the asset entry stats
	* @return the asset entry stats that was removed
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portlet.asset.model.AssetEntryStats deleteAssetEntryStats(
		com.liferay.portlet.asset.model.AssetEntryStats assetEntryStats)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().deleteAssetEntryStats(assetEntryStats);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public static java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public static java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public static java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService()
				   .dynamicQuery(dynamicQuery, start, end, orderByComparator);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows that match the dynamic query
	* @throws SystemException if a system exception occurred
	*/
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows that match the dynamic query
	* @throws SystemException if a system exception occurred
	*/
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static com.liferay.portlet.asset.model.AssetEntryStats fetchAssetEntryStats(
		long entryStatsId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().fetchAssetEntryStats(entryStatsId);
	}

	/**
	* Returns the asset entry stats with the primary key.
	*
	* @param entryStatsId the primary key of the asset entry stats
	* @return the asset entry stats
	* @throws PortalException if a asset entry stats with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portlet.asset.model.AssetEntryStats getAssetEntryStats(
		long entryStatsId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getAssetEntryStats(entryStatsId);
	}

	public static com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	* Returns a range of all the asset entry statses.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.asset.model.impl.AssetEntryStatsModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of asset entry statses
	* @param end the upper bound of the range of asset entry statses (not inclusive)
	* @return the range of asset entry statses
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.liferay.portlet.asset.model.AssetEntryStats> getAssetEntryStatses(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getAssetEntryStatses(start, end);
	}

	/**
	* Returns the number of asset entry statses.
	*
	* @return the number of asset entry statses
	* @throws SystemException if a system exception occurred
	*/
	public static int getAssetEntryStatsesCount()
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getAssetEntryStatsesCount();
	}

	/**
	* Updates the asset entry stats in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param assetEntryStats the asset entry stats
	* @return the asset entry stats that was updated
	* @throws SystemException if a system exception occurred
	*/
	public static com.liferay.portlet.asset.model.AssetEntryStats updateAssetEntryStats(
		com.liferay.portlet.asset.model.AssetEntryStats assetEntryStats)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().updateAssetEntryStats(assetEntryStats);
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public static java.lang.String getBeanIdentifier() {
		return getService().getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public static void setBeanIdentifier(java.lang.String beanIdentifier) {
		getService().setBeanIdentifier(beanIdentifier);
	}

	/**
	* Remove the stats for an asset.
	*
	* @param classNameId the primary key of the class to remove its stats
	* @param classPK the primary of the entity to remove its stats
	* @throws SystemException
	*/
	public static void removeByC_C(long classNameId, long classPK)
		throws com.liferay.portal.kernel.exception.SystemException {
		getService().removeByC_C(classNameId, classPK);
	}

	/**
	* Retrieves the sum of view counts for an asset, in an specific day of a
	* year.
	*
	* @param groupId the primary key of the web content article's group
	* @param classNameId the primary key of the class to search for
	* @param classPK the primary key of the entity to search for
	* @param day the day to search for
	* @param month the month to search for
	* @param year the year to search for
	* @return the sum of all view counts for a classNameId, classPK, in an
	specific day of a year
	* @throws SystemException if a system exception occurred
	*/
	public static long sumByC_C_Date(long groupId, long classNameId,
		long classPK, int day, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService()
				   .sumByC_C_Date(groupId, classNameId, classPK, day, month,
			year);
	}

	/**
	* Retrieves the sum of view counts for an asset, in an specific month of a
	* year.
	*
	* @param groupId the primary key of the web content article's group
	* @param classNameId the primary key of the class to search for
	* @param classPK the primary key of the entity to search for
	* @param month the month to search for
	* @param year the year to search for
	* @return the sum of all view counts for a classNameId, classPK, in an
	specific month of a year
	* @throws SystemException if a system exception occurred
	*/
	public static long sumByC_C_Month(long groupId, long classNameId,
		long classPK, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService()
				   .sumByC_C_Month(groupId, classNameId, classPK, month, year);
	}

	/**
	* Retrieves the sum of view counts for an asset, in an specific year.
	*
	* @param groupId the primary key of the web content article's group
	* @param classNameId the primary key of the class to search for
	* @param classPK the primary key of the entity to search for
	* @param year the year to search for
	* @return the sum of all view counts for a classNameId, classPK, in an
	specific year
	* @throws SystemException if a system exception occurred
	*/
	public static long sumByC_C_Year(long groupId, long classNameId,
		long classPK, int year)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().sumByC_C_Year(groupId, classNameId, classPK, year);
	}

	/**
	* Retrieves the sum of view counts for a type of assets, in an specific
	* day of a year.
	*
	* @param groupId the primary key of the web content article's group
	* @param classNameId the primary key of the class to search for
	* @param day the day to search for
	* @param month the month to search for
	* @param year the year to search for
	* @return the sum of all view counts for a classNameId, in an specific day
	of a year
	* @throws SystemException if a system exception occurred
	*/
	public static long sumByC_Date(long groupId, long classNameId, int day,
		int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().sumByC_Date(groupId, classNameId, day, month, year);
	}

	/**
	* Retrieves the sum of view counts for a type of assets, in an specific
	* month of a year.
	*
	* @param groupId the primary key of the web content article's group
	* @param classNameId the primary key of the class to search for
	* @param month the month to search for
	* @param year the year to search for
	* @return the sum of all view counts for a classNameId in an specific month
	of a year
	* @throws SystemException if a system exception occurred
	*/
	public static long sumByC_Month(long groupId, long classNameId, int month,
		int year) throws com.liferay.portal.kernel.exception.SystemException {
		return getService().sumByC_Month(groupId, classNameId, month, year);
	}

	/**
	* Retrieves the sum of view counts for a type of assets, in an specific
	* year.
	*
	* @param groupId the primary key of the web content article's group
	* @param classNameId the primary key of the class to search for
	* @param year the year to search for
	* @return the sum of all view counts for a classNameId, in an specific year
	* @throws SystemException if a system exception occurred
	*/
	public static long sumByC_Year(long groupId, long classNameId, int year)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().sumByC_Year(groupId, classNameId, year);
	}

	/**
	* Retrieves the sum of view counts for all assets in a Group, in an
	* specific day of a year.
	*
	* @param groupId the primary key of the web content article's group
	* @param day the day to search for
	* @param month the month to search for
	* @param year the year to search for
	* @return the sum of all view counts for a groupId, in an specific
	day of a year
	* @throws SystemException if a system exception occurred
	*/
	public static long sumByDate(long groupId, int day, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().sumByDate(groupId, day, month, year);
	}

	/**
	* Retrieves the sum of view counts for all assets in a Group, in an
	* specific month of a year.
	*
	* @param groupId the primary key of the web content article's group
	* @param month the month to search for
	* @param year the year to search for
	* @return the sum of all view counts for a groupId, in an specific
	month of a year
	* @throws SystemException if a system exception occurred
	*/
	public static long sumByMonth(long groupId, int month, int year)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().sumByMonth(groupId, month, year);
	}

	/**
	* Retrieves the sum of view counts for all assets in a Group, in an
	* specific year.
	*
	* @param groupId the primary key of the web content article's group
	* @param year the year to search for
	* @return the sum of all view counts for a groupId, in an specific year
	* @throws SystemException if a system exception occurred
	*/
	public static long sumByYear(long groupId, int year)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().sumByYear(groupId, year);
	}

	public static AssetEntryStatsLocalService getService() {
		if (_service == null) {
			_service = (AssetEntryStatsLocalService)PortalBeanLocatorUtil.locate(AssetEntryStatsLocalService.class.getName());

			ReferenceRegistry.registerReference(AssetEntryStatsLocalServiceUtil.class,
				"_service");
		}

		return _service;
	}

	/**
	 * @deprecated As of 6.2.0
	 */
	public void setService(AssetEntryStatsLocalService service) {
	}

	private static AssetEntryStatsLocalService _service;
}