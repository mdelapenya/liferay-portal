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

package com.liferay.persistence.arquillian.transactional.observer;

import com.liferay.persistence.arquillian.transactional.util.TransactionalUtil;
import com.liferay.persistence.arquillian.transactional.util.TransactionalUtilImpl;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;

/**
 * @author Cristina González
 */
public class TransactionUtilProducer {

	public void createTransactionalUtil(@Observes ArquillianDescriptor event) {
		_transactionalUtilProducer.set( new TransactionalUtilImpl());
	}

	@ApplicationScoped
	@Inject
	private InstanceProducer<TransactionalUtil> _transactionalUtilProducer;

}