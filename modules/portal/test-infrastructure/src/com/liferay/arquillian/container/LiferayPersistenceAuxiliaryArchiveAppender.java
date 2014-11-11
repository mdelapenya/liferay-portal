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
package com.liferay.arquillian.container;

import com.liferay.portal.test.TransactionalExecutionTestListener;
import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * @author Carlos Sierra Andrés
 */
public class LiferayPersistenceAuxiliaryArchiveAppender
	implements AuxiliaryArchiveAppender {

	@Override
	public Archive<?> createAuxiliaryArchive() {
		return ShrinkWrap.create(
			JavaArchive.class, "liferay-arquillian-persistence.jar").
			addPackage("com.liferay.portal.kernel.test").
			addAsServiceProvider(
				RemoteLoadableExtension.class,
				LiferayPersistenceRemoteLoadableExtension.class
			).
			addClasses(
				LiferayPersistenceRemoteLoadableExtension.class,
				TransactionalExecutionTestListener.class
			);

	}
}
