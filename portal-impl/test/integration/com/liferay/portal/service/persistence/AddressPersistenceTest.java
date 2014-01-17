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

package com.liferay.portal.service.persistence;

import com.liferay.portal.NoSuchAddressException;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.model.Address;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 */
@ExecutionTestListeners(listeners =  {
	PersistenceExecutionTestListener.class})
@RunWith(LiferayPersistenceIntegrationJUnitTestRunner.class)
public class AddressPersistenceTest {
	@After
	public void tearDown() throws Exception {
		Map<Serializable, BasePersistence<?>> basePersistences = _transactionalPersistenceAdvice.getBasePersistences();

		Set<Serializable> primaryKeys = basePersistences.keySet();

		for (Serializable primaryKey : primaryKeys) {
			BasePersistence<?> basePersistence = basePersistences.get(primaryKey);

			try {
				basePersistence.remove(primaryKey);
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug("The model with primary key " + primaryKey +
						" was already deleted");
				}
			}
		}

		_transactionalPersistenceAdvice.reset();
	}

	@Test
	public void testCreate() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Address address = _persistence.create(pk);

		Assert.assertNotNull(address);

		Assert.assertEquals(address.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		Address newAddress = addAddress();

		_persistence.remove(newAddress);

		Address existingAddress = _persistence.fetchByPrimaryKey(newAddress.getPrimaryKey());

		Assert.assertNull(existingAddress);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addAddress();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Address newAddress = _persistence.create(pk);

		newAddress.setUuid(ServiceTestUtil.randomString());

		newAddress.setCompanyId(ServiceTestUtil.nextLong());

		newAddress.setUserId(ServiceTestUtil.nextLong());

		newAddress.setUserName(ServiceTestUtil.randomString());

		newAddress.setCreateDate(ServiceTestUtil.nextDate());

		newAddress.setModifiedDate(ServiceTestUtil.nextDate());

		newAddress.setClassNameId(ServiceTestUtil.nextLong());

		newAddress.setClassPK(ServiceTestUtil.nextLong());

		newAddress.setStreet1(ServiceTestUtil.randomString());

		newAddress.setStreet2(ServiceTestUtil.randomString());

		newAddress.setStreet3(ServiceTestUtil.randomString());

		newAddress.setCity(ServiceTestUtil.randomString());

		newAddress.setZip(ServiceTestUtil.randomString());

		newAddress.setRegionId(ServiceTestUtil.nextLong());

		newAddress.setCountryId(ServiceTestUtil.nextLong());

		newAddress.setTypeId(ServiceTestUtil.nextInt());

		newAddress.setMailing(ServiceTestUtil.randomBoolean());

		newAddress.setPrimary(ServiceTestUtil.randomBoolean());

		_persistence.update(newAddress);

		Address existingAddress = _persistence.findByPrimaryKey(newAddress.getPrimaryKey());

		Assert.assertEquals(existingAddress.getUuid(), newAddress.getUuid());
		Assert.assertEquals(existingAddress.getAddressId(),
			newAddress.getAddressId());
		Assert.assertEquals(existingAddress.getCompanyId(),
			newAddress.getCompanyId());
		Assert.assertEquals(existingAddress.getUserId(), newAddress.getUserId());
		Assert.assertEquals(existingAddress.getUserName(),
			newAddress.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingAddress.getCreateDate()),
			Time.getShortTimestamp(newAddress.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingAddress.getModifiedDate()),
			Time.getShortTimestamp(newAddress.getModifiedDate()));
		Assert.assertEquals(existingAddress.getClassNameId(),
			newAddress.getClassNameId());
		Assert.assertEquals(existingAddress.getClassPK(),
			newAddress.getClassPK());
		Assert.assertEquals(existingAddress.getStreet1(),
			newAddress.getStreet1());
		Assert.assertEquals(existingAddress.getStreet2(),
			newAddress.getStreet2());
		Assert.assertEquals(existingAddress.getStreet3(),
			newAddress.getStreet3());
		Assert.assertEquals(existingAddress.getCity(), newAddress.getCity());
		Assert.assertEquals(existingAddress.getZip(), newAddress.getZip());
		Assert.assertEquals(existingAddress.getRegionId(),
			newAddress.getRegionId());
		Assert.assertEquals(existingAddress.getCountryId(),
			newAddress.getCountryId());
		Assert.assertEquals(existingAddress.getTypeId(), newAddress.getTypeId());
		Assert.assertEquals(existingAddress.getMailing(),
			newAddress.getMailing());
		Assert.assertEquals(existingAddress.getPrimary(),
			newAddress.getPrimary());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		Address newAddress = addAddress();

		Address existingAddress = _persistence.findByPrimaryKey(newAddress.getPrimaryKey());

		Assert.assertEquals(existingAddress, newAddress);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchAddressException");
		}
		catch (NoSuchAddressException nsee) {
		}
	}

	@Test
	public void testFindAll() throws Exception {
		try {
			_persistence.findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS,
				getOrderByComparator());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testFindByUuid() throws Exception {
		Address address = addAddress();

		String uuid = address.getUuid();

		List<Address> addresses = _persistence.findByUuid(uuid);

		Assert.assertEquals(1, addresses.size());

		Assert.assertEquals(address.getPrimaryKey(),
			addresses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuidNotFound() throws Exception {
		addAddress();

		String uuid = ServiceTestUtil.randomString();

		List<Address> addresses = _persistence.findByUuid(uuid);

		Assert.assertEquals(0, addresses.size());
	}

	@Test
	public void testFindByUuidStartEnd() throws Exception {
		testFindByUuidStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByUuidStartEndWrongRange() throws Exception {
		testFindByUuidStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByUuidStartEndZeroZero() throws Exception {
		testFindByUuidStartEnd(0, 0, 0);
	}

	protected void testFindByUuidStartEnd(int start, int end, int expected)
		throws Exception {
		Address address = addAddress();

		String uuid = address.getUuid();

		List<Address> addresses = _persistence.findByUuid(uuid, start, end);

		Assert.assertEquals(expected, addresses.size());
	}

	@Test
	public void testFindByUuid_C() throws Exception {
		Address address = addAddress();

		String uuid = address.getUuid();

		long companyId = address.getCompanyId();

		List<Address> addresses = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(1, addresses.size());

		Assert.assertEquals(address.getPrimaryKey(),
			addresses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUuid_CNotFound() throws Exception {
		addAddress();

		String uuid = ServiceTestUtil.randomString();

		long companyId = ServiceTestUtil.nextLong();

		List<Address> addresses = _persistence.findByUuid_C(uuid, companyId);

		Assert.assertEquals(0, addresses.size());
	}

	@Test
	public void testFindByUuid_CStartEnd() throws Exception {
		testFindByUuid_CStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByUuid_CStartEndWrongRange() throws Exception {
		testFindByUuid_CStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByUuid_CStartEndZeroZero() throws Exception {
		testFindByUuid_CStartEnd(0, 0, 0);
	}

	protected void testFindByUuid_CStartEnd(int start, int end, int expected)
		throws Exception {
		Address address = addAddress();

		String uuid = address.getUuid();

		long companyId = address.getCompanyId();

		List<Address> addresses = _persistence.findByUuid_C(uuid, companyId,
				start, end);

		Assert.assertEquals(expected, addresses.size());
	}

	@Test
	public void testFindByCompanyId() throws Exception {
		Address address = addAddress();

		long companyId = address.getCompanyId();

		List<Address> addresses = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(1, addresses.size());

		Assert.assertEquals(address.getPrimaryKey(),
			addresses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByCompanyIdNotFound() throws Exception {
		addAddress();

		long companyId = ServiceTestUtil.nextLong();

		List<Address> addresses = _persistence.findByCompanyId(companyId);

		Assert.assertEquals(0, addresses.size());
	}

	@Test
	public void testFindByCompanyIdStartEnd() throws Exception {
		testFindByCompanyIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByCompanyIdStartEndWrongRange()
		throws Exception {
		testFindByCompanyIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByCompanyIdStartEndZeroZero() throws Exception {
		testFindByCompanyIdStartEnd(0, 0, 0);
	}

	protected void testFindByCompanyIdStartEnd(int start, int end, int expected)
		throws Exception {
		Address address = addAddress();

		long companyId = address.getCompanyId();

		List<Address> addresses = _persistence.findByCompanyId(companyId,
				start, end);

		Assert.assertEquals(expected, addresses.size());
	}

	@Test
	public void testFindByUserId() throws Exception {
		Address address = addAddress();

		long userId = address.getUserId();

		List<Address> addresses = _persistence.findByUserId(userId);

		Assert.assertEquals(1, addresses.size());

		Assert.assertEquals(address.getPrimaryKey(),
			addresses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByUserIdNotFound() throws Exception {
		addAddress();

		long userId = ServiceTestUtil.nextLong();

		List<Address> addresses = _persistence.findByUserId(userId);

		Assert.assertEquals(0, addresses.size());
	}

	@Test
	public void testFindByUserIdStartEnd() throws Exception {
		testFindByUserIdStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByUserIdStartEndWrongRange() throws Exception {
		testFindByUserIdStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByUserIdStartEndZeroZero() throws Exception {
		testFindByUserIdStartEnd(0, 0, 0);
	}

	protected void testFindByUserIdStartEnd(int start, int end, int expected)
		throws Exception {
		Address address = addAddress();

		long userId = address.getUserId();

		List<Address> addresses = _persistence.findByUserId(userId, start, end);

		Assert.assertEquals(expected, addresses.size());
	}

	@Test
	public void testFindByC_C() throws Exception {
		Address address = addAddress();

		long companyId = address.getCompanyId();

		long classNameId = address.getClassNameId();

		List<Address> addresses = _persistence.findByC_C(companyId, classNameId);

		Assert.assertEquals(1, addresses.size());

		Assert.assertEquals(address.getPrimaryKey(),
			addresses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_CNotFound() throws Exception {
		addAddress();

		long companyId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		List<Address> addresses = _persistence.findByC_C(companyId, classNameId);

		Assert.assertEquals(0, addresses.size());
	}

	@Test
	public void testFindByC_CStartEnd() throws Exception {
		testFindByC_CStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_CStartEndWrongRange() throws Exception {
		testFindByC_CStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_CStartEndZeroZero() throws Exception {
		testFindByC_CStartEnd(0, 0, 0);
	}

	protected void testFindByC_CStartEnd(int start, int end, int expected)
		throws Exception {
		Address address = addAddress();

		long companyId = address.getCompanyId();

		long classNameId = address.getClassNameId();

		List<Address> addresses = _persistence.findByC_C(companyId,
				classNameId, start, end);

		Assert.assertEquals(expected, addresses.size());
	}

	@Test
	public void testFindByC_C_C() throws Exception {
		Address address = addAddress();

		long companyId = address.getCompanyId();

		long classNameId = address.getClassNameId();

		long classPK = address.getClassPK();

		List<Address> addresses = _persistence.findByC_C_C(companyId,
				classNameId, classPK);

		Assert.assertEquals(1, addresses.size());

		Assert.assertEquals(address.getPrimaryKey(),
			addresses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_C_CNotFound() throws Exception {
		addAddress();

		long companyId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		List<Address> addresses = _persistence.findByC_C_C(companyId,
				classNameId, classPK);

		Assert.assertEquals(0, addresses.size());
	}

	@Test
	public void testFindByC_C_CStartEnd() throws Exception {
		testFindByC_C_CStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_C_CStartEndWrongRange() throws Exception {
		testFindByC_C_CStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_C_CStartEndZeroZero() throws Exception {
		testFindByC_C_CStartEnd(0, 0, 0);
	}

	protected void testFindByC_C_CStartEnd(int start, int end, int expected)
		throws Exception {
		Address address = addAddress();

		long companyId = address.getCompanyId();

		long classNameId = address.getClassNameId();

		long classPK = address.getClassPK();

		List<Address> addresses = _persistence.findByC_C_C(companyId,
				classNameId, classPK, start, end);

		Assert.assertEquals(expected, addresses.size());
	}

	@Test
	public void testFindByC_C_C_M() throws Exception {
		Address address = addAddress();

		long companyId = address.getCompanyId();

		long classNameId = address.getClassNameId();

		long classPK = address.getClassPK();

		boolean mailing = address.getMailing();

		List<Address> addresses = _persistence.findByC_C_C_M(companyId,
				classNameId, classPK, mailing);

		Assert.assertEquals(1, addresses.size());

		Assert.assertEquals(address.getPrimaryKey(),
			addresses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_C_C_MNotFound() throws Exception {
		addAddress();

		long companyId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		boolean mailing = ServiceTestUtil.randomBoolean();

		List<Address> addresses = _persistence.findByC_C_C_M(companyId,
				classNameId, classPK, mailing);

		Assert.assertEquals(0, addresses.size());
	}

	@Test
	public void testFindByC_C_C_MStartEnd() throws Exception {
		testFindByC_C_C_MStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_C_C_MStartEndWrongRange() throws Exception {
		testFindByC_C_C_MStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_C_C_MStartEndZeroZero() throws Exception {
		testFindByC_C_C_MStartEnd(0, 0, 0);
	}

	protected void testFindByC_C_C_MStartEnd(int start, int end, int expected)
		throws Exception {
		Address address = addAddress();

		long companyId = address.getCompanyId();

		long classNameId = address.getClassNameId();

		long classPK = address.getClassPK();

		boolean mailing = address.getMailing();

		List<Address> addresses = _persistence.findByC_C_C_M(companyId,
				classNameId, classPK, mailing, start, end);

		Assert.assertEquals(expected, addresses.size());
	}

	@Test
	public void testFindByC_C_C_P() throws Exception {
		Address address = addAddress();

		long companyId = address.getCompanyId();

		long classNameId = address.getClassNameId();

		long classPK = address.getClassPK();

		boolean primary = address.getPrimary();

		List<Address> addresses = _persistence.findByC_C_C_P(companyId,
				classNameId, classPK, primary);

		Assert.assertEquals(1, addresses.size());

		Assert.assertEquals(address.getPrimaryKey(),
			addresses.get(0).getPrimaryKey());
	}

	@Test
	public void testFindByC_C_C_PNotFound() throws Exception {
		addAddress();

		long companyId = ServiceTestUtil.nextLong();

		long classNameId = ServiceTestUtil.nextLong();

		long classPK = ServiceTestUtil.nextLong();

		boolean primary = ServiceTestUtil.randomBoolean();

		List<Address> addresses = _persistence.findByC_C_C_P(companyId,
				classNameId, classPK, primary);

		Assert.assertEquals(0, addresses.size());
	}

	@Test
	public void testFindByC_C_C_PStartEnd() throws Exception {
		testFindByC_C_C_PStartEnd(0, 5, 1);
	}

	@Test
	public void testFindByC_C_C_PStartEndWrongRange() throws Exception {
		testFindByC_C_C_PStartEnd(5, 0, 0);
	}

	@Test
	public void testFindByC_C_C_PStartEndZeroZero() throws Exception {
		testFindByC_C_C_PStartEnd(0, 0, 0);
	}

	protected void testFindByC_C_C_PStartEnd(int start, int end, int expected)
		throws Exception {
		Address address = addAddress();

		long companyId = address.getCompanyId();

		long classNameId = address.getClassNameId();

		long classPK = address.getClassPK();

		boolean primary = address.getPrimary();

		List<Address> addresses = _persistence.findByC_C_C_P(companyId,
				classNameId, classPK, primary, start, end);

		Assert.assertEquals(expected, addresses.size());
	}

	protected OrderByComparator getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("Address", "uuid", true,
			"addressId", true, "companyId", true, "userId", true, "userName",
			true, "createDate", true, "modifiedDate", true, "classNameId",
			true, "classPK", true, "street1", true, "street2", true, "street3",
			true, "city", true, "zip", true, "regionId", true, "countryId",
			true, "typeId", true, "mailing", true, "primary", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		Address newAddress = addAddress();

		Address existingAddress = _persistence.fetchByPrimaryKey(newAddress.getPrimaryKey());

		Assert.assertEquals(existingAddress, newAddress);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Address missingAddress = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingAddress);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = new AddressActionableDynamicQuery() {
				@Override
				protected void performAction(Object object) {
					Address address = (Address)object;

					Assert.assertNotNull(address);

					count.increment();
				}
			};

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		Address newAddress = addAddress();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Address.class,
				Address.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("addressId",
				newAddress.getAddressId()));

		List<Address> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Address existingAddress = result.get(0);

		Assert.assertEquals(existingAddress, newAddress);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Address.class,
				Address.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("addressId",
				ServiceTestUtil.nextLong()));

		List<Address> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		Address newAddress = addAddress();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Address.class,
				Address.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("addressId"));

		Object newAddressId = newAddress.getAddressId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("addressId",
				new Object[] { newAddressId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingAddressId = result.get(0);

		Assert.assertEquals(existingAddressId, newAddressId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Address.class,
				Address.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("addressId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("addressId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected Address addAddress() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Address address = _persistence.create(pk);

		address.setUuid(ServiceTestUtil.randomString());

		address.setCompanyId(ServiceTestUtil.nextLong());

		address.setUserId(ServiceTestUtil.nextLong());

		address.setUserName(ServiceTestUtil.randomString());

		address.setCreateDate(ServiceTestUtil.nextDate());

		address.setModifiedDate(ServiceTestUtil.nextDate());

		address.setClassNameId(ServiceTestUtil.nextLong());

		address.setClassPK(ServiceTestUtil.nextLong());

		address.setStreet1(ServiceTestUtil.randomString());

		address.setStreet2(ServiceTestUtil.randomString());

		address.setStreet3(ServiceTestUtil.randomString());

		address.setCity(ServiceTestUtil.randomString());

		address.setZip(ServiceTestUtil.randomString());

		address.setRegionId(ServiceTestUtil.nextLong());

		address.setCountryId(ServiceTestUtil.nextLong());

		address.setTypeId(ServiceTestUtil.nextInt());

		address.setMailing(ServiceTestUtil.randomBoolean());

		address.setPrimary(ServiceTestUtil.randomBoolean());

		_persistence.update(address);

		return address;
	}

	private static Log _log = LogFactoryUtil.getLog(AddressPersistenceTest.class);
	private AddressPersistence _persistence = (AddressPersistence)PortalBeanLocatorUtil.locate(AddressPersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}