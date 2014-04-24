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

package com.liferay.portal.service.base;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.Lock;
import com.liferay.portal.model.PersistedModel;
import com.liferay.portal.service.BaseLocalServiceImpl;
import com.liferay.portal.service.LockLocalService;
import com.liferay.portal.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.service.persistence.LockFinder;
import com.liferay.portal.service.persistence.LockPersistence;
import com.liferay.portal.service.persistence.UserFinder;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the lock local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portal.service.impl.LockLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portal.service.impl.LockLocalServiceImpl
 * @see com.liferay.portal.service.LockLocalServiceUtil
 * @generated
 */
public abstract class LockLocalServiceBaseImpl extends BaseLocalServiceImpl
	implements LockLocalService, IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.portal.service.LockLocalServiceUtil} to access the lock local service.
	 */

	/**
	 * Adds the lock to the database. Also notifies the appropriate model listeners.
	 *
	 * @param lock the lock
	 * @return the lock that was added
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public Lock addLock(Lock lock) throws SystemException {
		lock.setNew(true);

		return lockPersistence.update(lock);
	}

	/**
	 * Creates a new lock with the primary key. Does not add the lock to the database.
	 *
	 * @param lockId the primary key for the new lock
	 * @return the new lock
	 */
	@Override
	public Lock createLock(long lockId) {
		return lockPersistence.create(lockId);
	}

	/**
	 * Deletes the lock with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param lockId the primary key of the lock
	 * @return the lock that was removed
	 * @throws PortalException if a lock with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public Lock deleteLock(long lockId) throws PortalException, SystemException {
		return lockPersistence.remove(lockId);
	}

	/**
	 * Deletes the lock from the database. Also notifies the appropriate model listeners.
	 *
	 * @param lock the lock
	 * @return the lock that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public Lock deleteLock(Lock lock) throws SystemException {
		return lockPersistence.remove(lock);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(Lock.class,
			clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery)
		throws SystemException {
		return lockPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.LockModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery, int start, int end)
		throws SystemException {
		return lockPersistence.findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.LockModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		return lockPersistence.findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * Returns the number of rows that match the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows that match the dynamic query
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery)
		throws SystemException {
		return lockPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows that match the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows that match the dynamic query
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery,
		Projection projection) throws SystemException {
		return lockPersistence.countWithDynamicQuery(dynamicQuery, projection);
	}

	@Override
	public Lock fetchLock(long lockId) throws SystemException {
		return lockPersistence.fetchByPrimaryKey(lockId);
	}

	/**
	 * Returns the lock with the matching UUID and company.
	 *
	 * @param uuid the lock's UUID
	 * @param  companyId the primary key of the company
	 * @return the matching lock, or <code>null</code> if a matching lock could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Lock fetchLockByUuidAndCompanyId(String uuid, long companyId)
		throws SystemException {
		return lockPersistence.fetchByUuid_C_First(uuid, companyId, null);
	}

	/**
	 * Returns the lock with the primary key.
	 *
	 * @param lockId the primary key of the lock
	 * @return the lock
	 * @throws PortalException if a lock with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Lock getLock(long lockId) throws PortalException, SystemException {
		return lockPersistence.findByPrimaryKey(lockId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery()
		throws SystemException {
		ActionableDynamicQuery actionableDynamicQuery = new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(com.liferay.portal.service.LockLocalServiceUtil.getService());
		actionableDynamicQuery.setClass(Lock.class);
		actionableDynamicQuery.setClassLoader(getClassLoader());

		actionableDynamicQuery.setPrimaryKeyPropertyName("lockId");

		return actionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery)
		throws SystemException {
		actionableDynamicQuery.setBaseLocalService(com.liferay.portal.service.LockLocalServiceUtil.getService());
		actionableDynamicQuery.setClass(Lock.class);
		actionableDynamicQuery.setClassLoader(getClassLoader());

		actionableDynamicQuery.setPrimaryKeyPropertyName("lockId");
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException, SystemException {
		return lockPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns the lock with the matching UUID and company.
	 *
	 * @param uuid the lock's UUID
	 * @param  companyId the primary key of the company
	 * @return the matching lock
	 * @throws PortalException if a matching lock could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Lock getLockByUuidAndCompanyId(String uuid, long companyId)
		throws PortalException, SystemException {
		return lockPersistence.findByUuid_C_First(uuid, companyId, null);
	}

	/**
	 * Returns a range of all the locks.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.model.impl.LockModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of locks
	 * @param end the upper bound of the range of locks (not inclusive)
	 * @return the range of locks
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Lock> getLocks(int start, int end) throws SystemException {
		return lockPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of locks.
	 *
	 * @return the number of locks
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int getLocksCount() throws SystemException {
		return lockPersistence.countAll();
	}

	/**
	 * Updates the lock in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param lock the lock
	 * @return the lock that was updated
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public Lock updateLock(Lock lock) throws SystemException {
		return lockPersistence.update(lock);
	}

	/**
	 * Returns the lock local service.
	 *
	 * @return the lock local service
	 */
	public com.liferay.portal.service.LockLocalService getLockLocalService() {
		return lockLocalService;
	}

	/**
	 * Sets the lock local service.
	 *
	 * @param lockLocalService the lock local service
	 */
	public void setLockLocalService(
		com.liferay.portal.service.LockLocalService lockLocalService) {
		this.lockLocalService = lockLocalService;
	}

	/**
	 * Returns the lock persistence.
	 *
	 * @return the lock persistence
	 */
	public LockPersistence getLockPersistence() {
		return lockPersistence;
	}

	/**
	 * Sets the lock persistence.
	 *
	 * @param lockPersistence the lock persistence
	 */
	public void setLockPersistence(LockPersistence lockPersistence) {
		this.lockPersistence = lockPersistence;
	}

	/**
	 * Returns the lock finder.
	 *
	 * @return the lock finder
	 */
	public LockFinder getLockFinder() {
		return lockFinder;
	}

	/**
	 * Sets the lock finder.
	 *
	 * @param lockFinder the lock finder
	 */
	public void setLockFinder(LockFinder lockFinder) {
		this.lockFinder = lockFinder;
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
		persistedModelLocalServiceRegistry.register("com.liferay.portal.model.Lock",
			lockLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.portal.model.Lock");
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
		return Lock.class;
	}

	protected String getModelClassName() {
		return Lock.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) throws SystemException {
		try {
			DataSource dataSource = lockPersistence.getDataSource();

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

	@BeanReference(type = com.liferay.portal.service.LockLocalService.class)
	protected com.liferay.portal.service.LockLocalService lockLocalService;
	@BeanReference(type = LockPersistence.class)
	protected LockPersistence lockPersistence;
	@BeanReference(type = LockFinder.class)
	protected LockFinder lockFinder;
	@BeanReference(type = com.liferay.counter.service.CounterLocalService.class)
	protected com.liferay.counter.service.CounterLocalService counterLocalService;
	@BeanReference(type = com.liferay.portal.service.UserLocalService.class)
	protected com.liferay.portal.service.UserLocalService userLocalService;
	@BeanReference(type = com.liferay.portal.service.UserService.class)
	protected com.liferay.portal.service.UserService userService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	@BeanReference(type = UserFinder.class)
	protected UserFinder userFinder;
	@BeanReference(type = PersistedModelLocalServiceRegistry.class)
	protected PersistedModelLocalServiceRegistry persistedModelLocalServiceRegistry;
	private String _beanIdentifier;
}