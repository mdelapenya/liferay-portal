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

package com.liferay.portlet.asset.service.base;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.BaseServiceImpl;
import com.liferay.portal.service.persistence.UserFinder;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.asset.model.AssetCategoryProperty;
import com.liferay.portlet.asset.service.AssetCategoryPropertyService;
import com.liferay.portlet.asset.service.persistence.AssetCategoryFinder;
import com.liferay.portlet.asset.service.persistence.AssetCategoryPersistence;
import com.liferay.portlet.asset.service.persistence.AssetCategoryPropertyFinder;
import com.liferay.portlet.asset.service.persistence.AssetCategoryPropertyPersistence;
import com.liferay.portlet.asset.service.persistence.AssetEntryFinder;
import com.liferay.portlet.asset.service.persistence.AssetEntryPersistence;
import com.liferay.portlet.asset.service.persistence.AssetEntryStatsFinder;
import com.liferay.portlet.asset.service.persistence.AssetEntryStatsPersistence;
import com.liferay.portlet.asset.service.persistence.AssetLinkPersistence;
import com.liferay.portlet.asset.service.persistence.AssetTagFinder;
import com.liferay.portlet.asset.service.persistence.AssetTagPersistence;
import com.liferay.portlet.asset.service.persistence.AssetTagPropertyFinder;
import com.liferay.portlet.asset.service.persistence.AssetTagPropertyKeyFinder;
import com.liferay.portlet.asset.service.persistence.AssetTagPropertyPersistence;
import com.liferay.portlet.asset.service.persistence.AssetTagStatsPersistence;
import com.liferay.portlet.asset.service.persistence.AssetVocabularyFinder;
import com.liferay.portlet.asset.service.persistence.AssetVocabularyPersistence;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the asset category property remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portlet.asset.service.impl.AssetCategoryPropertyServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portlet.asset.service.impl.AssetCategoryPropertyServiceImpl
 * @see com.liferay.portlet.asset.service.AssetCategoryPropertyServiceUtil
 * @generated
 */
public abstract class AssetCategoryPropertyServiceBaseImpl
	extends BaseServiceImpl implements AssetCategoryPropertyService,
		IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.portlet.asset.service.AssetCategoryPropertyServiceUtil} to access the asset category property remote service.
	 */

	/**
	 * Returns the asset category local service.
	 *
	 * @return the asset category local service
	 */
	public com.liferay.portlet.asset.service.AssetCategoryLocalService getAssetCategoryLocalService() {
		return assetCategoryLocalService;
	}

	/**
	 * Sets the asset category local service.
	 *
	 * @param assetCategoryLocalService the asset category local service
	 */
	public void setAssetCategoryLocalService(
		com.liferay.portlet.asset.service.AssetCategoryLocalService assetCategoryLocalService) {
		this.assetCategoryLocalService = assetCategoryLocalService;
	}

	/**
	 * Returns the asset category remote service.
	 *
	 * @return the asset category remote service
	 */
	public com.liferay.portlet.asset.service.AssetCategoryService getAssetCategoryService() {
		return assetCategoryService;
	}

	/**
	 * Sets the asset category remote service.
	 *
	 * @param assetCategoryService the asset category remote service
	 */
	public void setAssetCategoryService(
		com.liferay.portlet.asset.service.AssetCategoryService assetCategoryService) {
		this.assetCategoryService = assetCategoryService;
	}

	/**
	 * Returns the asset category persistence.
	 *
	 * @return the asset category persistence
	 */
	public AssetCategoryPersistence getAssetCategoryPersistence() {
		return assetCategoryPersistence;
	}

	/**
	 * Sets the asset category persistence.
	 *
	 * @param assetCategoryPersistence the asset category persistence
	 */
	public void setAssetCategoryPersistence(
		AssetCategoryPersistence assetCategoryPersistence) {
		this.assetCategoryPersistence = assetCategoryPersistence;
	}

	/**
	 * Returns the asset category finder.
	 *
	 * @return the asset category finder
	 */
	public AssetCategoryFinder getAssetCategoryFinder() {
		return assetCategoryFinder;
	}

	/**
	 * Sets the asset category finder.
	 *
	 * @param assetCategoryFinder the asset category finder
	 */
	public void setAssetCategoryFinder(AssetCategoryFinder assetCategoryFinder) {
		this.assetCategoryFinder = assetCategoryFinder;
	}

	/**
	 * Returns the asset category property local service.
	 *
	 * @return the asset category property local service
	 */
	public com.liferay.portlet.asset.service.AssetCategoryPropertyLocalService getAssetCategoryPropertyLocalService() {
		return assetCategoryPropertyLocalService;
	}

	/**
	 * Sets the asset category property local service.
	 *
	 * @param assetCategoryPropertyLocalService the asset category property local service
	 */
	public void setAssetCategoryPropertyLocalService(
		com.liferay.portlet.asset.service.AssetCategoryPropertyLocalService assetCategoryPropertyLocalService) {
		this.assetCategoryPropertyLocalService = assetCategoryPropertyLocalService;
	}

	/**
	 * Returns the asset category property remote service.
	 *
	 * @return the asset category property remote service
	 */
	public com.liferay.portlet.asset.service.AssetCategoryPropertyService getAssetCategoryPropertyService() {
		return assetCategoryPropertyService;
	}

	/**
	 * Sets the asset category property remote service.
	 *
	 * @param assetCategoryPropertyService the asset category property remote service
	 */
	public void setAssetCategoryPropertyService(
		com.liferay.portlet.asset.service.AssetCategoryPropertyService assetCategoryPropertyService) {
		this.assetCategoryPropertyService = assetCategoryPropertyService;
	}

	/**
	 * Returns the asset category property persistence.
	 *
	 * @return the asset category property persistence
	 */
	public AssetCategoryPropertyPersistence getAssetCategoryPropertyPersistence() {
		return assetCategoryPropertyPersistence;
	}

	/**
	 * Sets the asset category property persistence.
	 *
	 * @param assetCategoryPropertyPersistence the asset category property persistence
	 */
	public void setAssetCategoryPropertyPersistence(
		AssetCategoryPropertyPersistence assetCategoryPropertyPersistence) {
		this.assetCategoryPropertyPersistence = assetCategoryPropertyPersistence;
	}

	/**
	 * Returns the asset category property finder.
	 *
	 * @return the asset category property finder
	 */
	public AssetCategoryPropertyFinder getAssetCategoryPropertyFinder() {
		return assetCategoryPropertyFinder;
	}

	/**
	 * Sets the asset category property finder.
	 *
	 * @param assetCategoryPropertyFinder the asset category property finder
	 */
	public void setAssetCategoryPropertyFinder(
		AssetCategoryPropertyFinder assetCategoryPropertyFinder) {
		this.assetCategoryPropertyFinder = assetCategoryPropertyFinder;
	}

	/**
	 * Returns the asset entry local service.
	 *
	 * @return the asset entry local service
	 */
	public com.liferay.portlet.asset.service.AssetEntryLocalService getAssetEntryLocalService() {
		return assetEntryLocalService;
	}

	/**
	 * Sets the asset entry local service.
	 *
	 * @param assetEntryLocalService the asset entry local service
	 */
	public void setAssetEntryLocalService(
		com.liferay.portlet.asset.service.AssetEntryLocalService assetEntryLocalService) {
		this.assetEntryLocalService = assetEntryLocalService;
	}

	/**
	 * Returns the asset entry remote service.
	 *
	 * @return the asset entry remote service
	 */
	public com.liferay.portlet.asset.service.AssetEntryService getAssetEntryService() {
		return assetEntryService;
	}

	/**
	 * Sets the asset entry remote service.
	 *
	 * @param assetEntryService the asset entry remote service
	 */
	public void setAssetEntryService(
		com.liferay.portlet.asset.service.AssetEntryService assetEntryService) {
		this.assetEntryService = assetEntryService;
	}

	/**
	 * Returns the asset entry persistence.
	 *
	 * @return the asset entry persistence
	 */
	public AssetEntryPersistence getAssetEntryPersistence() {
		return assetEntryPersistence;
	}

	/**
	 * Sets the asset entry persistence.
	 *
	 * @param assetEntryPersistence the asset entry persistence
	 */
	public void setAssetEntryPersistence(
		AssetEntryPersistence assetEntryPersistence) {
		this.assetEntryPersistence = assetEntryPersistence;
	}

	/**
	 * Returns the asset entry finder.
	 *
	 * @return the asset entry finder
	 */
	public AssetEntryFinder getAssetEntryFinder() {
		return assetEntryFinder;
	}

	/**
	 * Sets the asset entry finder.
	 *
	 * @param assetEntryFinder the asset entry finder
	 */
	public void setAssetEntryFinder(AssetEntryFinder assetEntryFinder) {
		this.assetEntryFinder = assetEntryFinder;
	}

	/**
	 * Returns the asset entry stats local service.
	 *
	 * @return the asset entry stats local service
	 */
	public com.liferay.portlet.asset.service.AssetEntryStatsLocalService getAssetEntryStatsLocalService() {
		return assetEntryStatsLocalService;
	}

	/**
	 * Sets the asset entry stats local service.
	 *
	 * @param assetEntryStatsLocalService the asset entry stats local service
	 */
	public void setAssetEntryStatsLocalService(
		com.liferay.portlet.asset.service.AssetEntryStatsLocalService assetEntryStatsLocalService) {
		this.assetEntryStatsLocalService = assetEntryStatsLocalService;
	}

	/**
	 * Returns the asset entry stats persistence.
	 *
	 * @return the asset entry stats persistence
	 */
	public AssetEntryStatsPersistence getAssetEntryStatsPersistence() {
		return assetEntryStatsPersistence;
	}

	/**
	 * Sets the asset entry stats persistence.
	 *
	 * @param assetEntryStatsPersistence the asset entry stats persistence
	 */
	public void setAssetEntryStatsPersistence(
		AssetEntryStatsPersistence assetEntryStatsPersistence) {
		this.assetEntryStatsPersistence = assetEntryStatsPersistence;
	}

	/**
	 * Returns the asset entry stats finder.
	 *
	 * @return the asset entry stats finder
	 */
	public AssetEntryStatsFinder getAssetEntryStatsFinder() {
		return assetEntryStatsFinder;
	}

	/**
	 * Sets the asset entry stats finder.
	 *
	 * @param assetEntryStatsFinder the asset entry stats finder
	 */
	public void setAssetEntryStatsFinder(
		AssetEntryStatsFinder assetEntryStatsFinder) {
		this.assetEntryStatsFinder = assetEntryStatsFinder;
	}

	/**
	 * Returns the asset link local service.
	 *
	 * @return the asset link local service
	 */
	public com.liferay.portlet.asset.service.AssetLinkLocalService getAssetLinkLocalService() {
		return assetLinkLocalService;
	}

	/**
	 * Sets the asset link local service.
	 *
	 * @param assetLinkLocalService the asset link local service
	 */
	public void setAssetLinkLocalService(
		com.liferay.portlet.asset.service.AssetLinkLocalService assetLinkLocalService) {
		this.assetLinkLocalService = assetLinkLocalService;
	}

	/**
	 * Returns the asset link persistence.
	 *
	 * @return the asset link persistence
	 */
	public AssetLinkPersistence getAssetLinkPersistence() {
		return assetLinkPersistence;
	}

	/**
	 * Sets the asset link persistence.
	 *
	 * @param assetLinkPersistence the asset link persistence
	 */
	public void setAssetLinkPersistence(
		AssetLinkPersistence assetLinkPersistence) {
		this.assetLinkPersistence = assetLinkPersistence;
	}

	/**
	 * Returns the asset tag local service.
	 *
	 * @return the asset tag local service
	 */
	public com.liferay.portlet.asset.service.AssetTagLocalService getAssetTagLocalService() {
		return assetTagLocalService;
	}

	/**
	 * Sets the asset tag local service.
	 *
	 * @param assetTagLocalService the asset tag local service
	 */
	public void setAssetTagLocalService(
		com.liferay.portlet.asset.service.AssetTagLocalService assetTagLocalService) {
		this.assetTagLocalService = assetTagLocalService;
	}

	/**
	 * Returns the asset tag remote service.
	 *
	 * @return the asset tag remote service
	 */
	public com.liferay.portlet.asset.service.AssetTagService getAssetTagService() {
		return assetTagService;
	}

	/**
	 * Sets the asset tag remote service.
	 *
	 * @param assetTagService the asset tag remote service
	 */
	public void setAssetTagService(
		com.liferay.portlet.asset.service.AssetTagService assetTagService) {
		this.assetTagService = assetTagService;
	}

	/**
	 * Returns the asset tag persistence.
	 *
	 * @return the asset tag persistence
	 */
	public AssetTagPersistence getAssetTagPersistence() {
		return assetTagPersistence;
	}

	/**
	 * Sets the asset tag persistence.
	 *
	 * @param assetTagPersistence the asset tag persistence
	 */
	public void setAssetTagPersistence(AssetTagPersistence assetTagPersistence) {
		this.assetTagPersistence = assetTagPersistence;
	}

	/**
	 * Returns the asset tag finder.
	 *
	 * @return the asset tag finder
	 */
	public AssetTagFinder getAssetTagFinder() {
		return assetTagFinder;
	}

	/**
	 * Sets the asset tag finder.
	 *
	 * @param assetTagFinder the asset tag finder
	 */
	public void setAssetTagFinder(AssetTagFinder assetTagFinder) {
		this.assetTagFinder = assetTagFinder;
	}

	/**
	 * Returns the asset tag property local service.
	 *
	 * @return the asset tag property local service
	 */
	public com.liferay.portlet.asset.service.AssetTagPropertyLocalService getAssetTagPropertyLocalService() {
		return assetTagPropertyLocalService;
	}

	/**
	 * Sets the asset tag property local service.
	 *
	 * @param assetTagPropertyLocalService the asset tag property local service
	 */
	public void setAssetTagPropertyLocalService(
		com.liferay.portlet.asset.service.AssetTagPropertyLocalService assetTagPropertyLocalService) {
		this.assetTagPropertyLocalService = assetTagPropertyLocalService;
	}

	/**
	 * Returns the asset tag property remote service.
	 *
	 * @return the asset tag property remote service
	 */
	public com.liferay.portlet.asset.service.AssetTagPropertyService getAssetTagPropertyService() {
		return assetTagPropertyService;
	}

	/**
	 * Sets the asset tag property remote service.
	 *
	 * @param assetTagPropertyService the asset tag property remote service
	 */
	public void setAssetTagPropertyService(
		com.liferay.portlet.asset.service.AssetTagPropertyService assetTagPropertyService) {
		this.assetTagPropertyService = assetTagPropertyService;
	}

	/**
	 * Returns the asset tag property persistence.
	 *
	 * @return the asset tag property persistence
	 */
	public AssetTagPropertyPersistence getAssetTagPropertyPersistence() {
		return assetTagPropertyPersistence;
	}

	/**
	 * Sets the asset tag property persistence.
	 *
	 * @param assetTagPropertyPersistence the asset tag property persistence
	 */
	public void setAssetTagPropertyPersistence(
		AssetTagPropertyPersistence assetTagPropertyPersistence) {
		this.assetTagPropertyPersistence = assetTagPropertyPersistence;
	}

	/**
	 * Returns the asset tag property finder.
	 *
	 * @return the asset tag property finder
	 */
	public AssetTagPropertyFinder getAssetTagPropertyFinder() {
		return assetTagPropertyFinder;
	}

	/**
	 * Sets the asset tag property finder.
	 *
	 * @param assetTagPropertyFinder the asset tag property finder
	 */
	public void setAssetTagPropertyFinder(
		AssetTagPropertyFinder assetTagPropertyFinder) {
		this.assetTagPropertyFinder = assetTagPropertyFinder;
	}

	/**
	 * Returns the asset tag property key finder.
	 *
	 * @return the asset tag property key finder
	 */
	public AssetTagPropertyKeyFinder getAssetTagPropertyKeyFinder() {
		return assetTagPropertyKeyFinder;
	}

	/**
	 * Sets the asset tag property key finder.
	 *
	 * @param assetTagPropertyKeyFinder the asset tag property key finder
	 */
	public void setAssetTagPropertyKeyFinder(
		AssetTagPropertyKeyFinder assetTagPropertyKeyFinder) {
		this.assetTagPropertyKeyFinder = assetTagPropertyKeyFinder;
	}

	/**
	 * Returns the asset tag stats local service.
	 *
	 * @return the asset tag stats local service
	 */
	public com.liferay.portlet.asset.service.AssetTagStatsLocalService getAssetTagStatsLocalService() {
		return assetTagStatsLocalService;
	}

	/**
	 * Sets the asset tag stats local service.
	 *
	 * @param assetTagStatsLocalService the asset tag stats local service
	 */
	public void setAssetTagStatsLocalService(
		com.liferay.portlet.asset.service.AssetTagStatsLocalService assetTagStatsLocalService) {
		this.assetTagStatsLocalService = assetTagStatsLocalService;
	}

	/**
	 * Returns the asset tag stats persistence.
	 *
	 * @return the asset tag stats persistence
	 */
	public AssetTagStatsPersistence getAssetTagStatsPersistence() {
		return assetTagStatsPersistence;
	}

	/**
	 * Sets the asset tag stats persistence.
	 *
	 * @param assetTagStatsPersistence the asset tag stats persistence
	 */
	public void setAssetTagStatsPersistence(
		AssetTagStatsPersistence assetTagStatsPersistence) {
		this.assetTagStatsPersistence = assetTagStatsPersistence;
	}

	/**
	 * Returns the asset vocabulary local service.
	 *
	 * @return the asset vocabulary local service
	 */
	public com.liferay.portlet.asset.service.AssetVocabularyLocalService getAssetVocabularyLocalService() {
		return assetVocabularyLocalService;
	}

	/**
	 * Sets the asset vocabulary local service.
	 *
	 * @param assetVocabularyLocalService the asset vocabulary local service
	 */
	public void setAssetVocabularyLocalService(
		com.liferay.portlet.asset.service.AssetVocabularyLocalService assetVocabularyLocalService) {
		this.assetVocabularyLocalService = assetVocabularyLocalService;
	}

	/**
	 * Returns the asset vocabulary remote service.
	 *
	 * @return the asset vocabulary remote service
	 */
	public com.liferay.portlet.asset.service.AssetVocabularyService getAssetVocabularyService() {
		return assetVocabularyService;
	}

	/**
	 * Sets the asset vocabulary remote service.
	 *
	 * @param assetVocabularyService the asset vocabulary remote service
	 */
	public void setAssetVocabularyService(
		com.liferay.portlet.asset.service.AssetVocabularyService assetVocabularyService) {
		this.assetVocabularyService = assetVocabularyService;
	}

	/**
	 * Returns the asset vocabulary persistence.
	 *
	 * @return the asset vocabulary persistence
	 */
	public AssetVocabularyPersistence getAssetVocabularyPersistence() {
		return assetVocabularyPersistence;
	}

	/**
	 * Sets the asset vocabulary persistence.
	 *
	 * @param assetVocabularyPersistence the asset vocabulary persistence
	 */
	public void setAssetVocabularyPersistence(
		AssetVocabularyPersistence assetVocabularyPersistence) {
		this.assetVocabularyPersistence = assetVocabularyPersistence;
	}

	/**
	 * Returns the asset vocabulary finder.
	 *
	 * @return the asset vocabulary finder
	 */
	public AssetVocabularyFinder getAssetVocabularyFinder() {
		return assetVocabularyFinder;
	}

	/**
	 * Sets the asset vocabulary finder.
	 *
	 * @param assetVocabularyFinder the asset vocabulary finder
	 */
	public void setAssetVocabularyFinder(
		AssetVocabularyFinder assetVocabularyFinder) {
		this.assetVocabularyFinder = assetVocabularyFinder;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public com.liferay.portal.service.UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(
		com.liferay.portal.service.UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	/**
	 * Returns the user finder.
	 *
	 * @return the user finder
	 */
	public UserFinder getUserFinder() {
		return userFinder;
	}

	/**
	 * Sets the user finder.
	 *
	 * @param userFinder the user finder
	 */
	public void setUserFinder(UserFinder userFinder) {
		this.userFinder = userFinder;
	}

	public void afterPropertiesSet() {
	}

	public void destroy() {
	}

	/**
	 * Returns the Spring bean ID for this bean.
	 *
	 * @return the Spring bean ID for this bean
	 */
	@Override
	public String getBeanIdentifier() {
		return _beanIdentifier;
	}

	/**
	 * Sets the Spring bean ID for this bean.
	 *
	 * @param beanIdentifier the Spring bean ID for this bean
	 */
	@Override
	public void setBeanIdentifier(String beanIdentifier) {
		_beanIdentifier = beanIdentifier;
	}

	protected Class<?> getModelClass() {
		return AssetCategoryProperty.class;
	}

	protected String getModelClassName() {
		return AssetCategoryProperty.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) throws SystemException {
		try {
			DataSource dataSource = assetCategoryPropertyPersistence.getDataSource();

			DB db = DBFactoryUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql, new int[0]);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = com.liferay.portlet.asset.service.AssetCategoryLocalService.class)
	protected com.liferay.portlet.asset.service.AssetCategoryLocalService assetCategoryLocalService;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetCategoryService.class)
	protected com.liferay.portlet.asset.service.AssetCategoryService assetCategoryService;
	@BeanReference(type = AssetCategoryPersistence.class)
	protected AssetCategoryPersistence assetCategoryPersistence;
	@BeanReference(type = AssetCategoryFinder.class)
	protected AssetCategoryFinder assetCategoryFinder;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetCategoryPropertyLocalService.class)
	protected com.liferay.portlet.asset.service.AssetCategoryPropertyLocalService assetCategoryPropertyLocalService;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetCategoryPropertyService.class)
	protected com.liferay.portlet.asset.service.AssetCategoryPropertyService assetCategoryPropertyService;
	@BeanReference(type = AssetCategoryPropertyPersistence.class)
	protected AssetCategoryPropertyPersistence assetCategoryPropertyPersistence;
	@BeanReference(type = AssetCategoryPropertyFinder.class)
	protected AssetCategoryPropertyFinder assetCategoryPropertyFinder;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetEntryLocalService.class)
	protected com.liferay.portlet.asset.service.AssetEntryLocalService assetEntryLocalService;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetEntryService.class)
	protected com.liferay.portlet.asset.service.AssetEntryService assetEntryService;
	@BeanReference(type = AssetEntryPersistence.class)
	protected AssetEntryPersistence assetEntryPersistence;
	@BeanReference(type = AssetEntryFinder.class)
	protected AssetEntryFinder assetEntryFinder;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetEntryStatsLocalService.class)
	protected com.liferay.portlet.asset.service.AssetEntryStatsLocalService assetEntryStatsLocalService;
	@BeanReference(type = AssetEntryStatsPersistence.class)
	protected AssetEntryStatsPersistence assetEntryStatsPersistence;
	@BeanReference(type = AssetEntryStatsFinder.class)
	protected AssetEntryStatsFinder assetEntryStatsFinder;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetLinkLocalService.class)
	protected com.liferay.portlet.asset.service.AssetLinkLocalService assetLinkLocalService;
	@BeanReference(type = AssetLinkPersistence.class)
	protected AssetLinkPersistence assetLinkPersistence;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetTagLocalService.class)
	protected com.liferay.portlet.asset.service.AssetTagLocalService assetTagLocalService;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetTagService.class)
	protected com.liferay.portlet.asset.service.AssetTagService assetTagService;
	@BeanReference(type = AssetTagPersistence.class)
	protected AssetTagPersistence assetTagPersistence;
	@BeanReference(type = AssetTagFinder.class)
	protected AssetTagFinder assetTagFinder;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetTagPropertyLocalService.class)
	protected com.liferay.portlet.asset.service.AssetTagPropertyLocalService assetTagPropertyLocalService;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetTagPropertyService.class)
	protected com.liferay.portlet.asset.service.AssetTagPropertyService assetTagPropertyService;
	@BeanReference(type = AssetTagPropertyPersistence.class)
	protected AssetTagPropertyPersistence assetTagPropertyPersistence;
	@BeanReference(type = AssetTagPropertyFinder.class)
	protected AssetTagPropertyFinder assetTagPropertyFinder;
	@BeanReference(type = AssetTagPropertyKeyFinder.class)
	protected AssetTagPropertyKeyFinder assetTagPropertyKeyFinder;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetTagStatsLocalService.class)
	protected com.liferay.portlet.asset.service.AssetTagStatsLocalService assetTagStatsLocalService;
	@BeanReference(type = AssetTagStatsPersistence.class)
	protected AssetTagStatsPersistence assetTagStatsPersistence;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetVocabularyLocalService.class)
	protected com.liferay.portlet.asset.service.AssetVocabularyLocalService assetVocabularyLocalService;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetVocabularyService.class)
	protected com.liferay.portlet.asset.service.AssetVocabularyService assetVocabularyService;
	@BeanReference(type = AssetVocabularyPersistence.class)
	protected AssetVocabularyPersistence assetVocabularyPersistence;
	@BeanReference(type = AssetVocabularyFinder.class)
	protected AssetVocabularyFinder assetVocabularyFinder;
	@BeanReference(type = com.liferay.counter.service.CounterLocalService.class)
	protected com.liferay.counter.service.CounterLocalService counterLocalService;
	@BeanReference(type = com.liferay.portal.service.ResourceLocalService.class)
	protected com.liferay.portal.service.ResourceLocalService resourceLocalService;
	@BeanReference(type = com.liferay.portal.service.UserLocalService.class)
	protected com.liferay.portal.service.UserLocalService userLocalService;
	@BeanReference(type = com.liferay.portal.service.UserService.class)
	protected com.liferay.portal.service.UserService userService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	@BeanReference(type = UserFinder.class)
	protected UserFinder userFinder;
	private String _beanIdentifier;
}