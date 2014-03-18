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

package com.liferay.portal.spring.context;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.ParallelDestination;
import com.liferay.portal.kernel.messaging.SerialDestination;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author Manuel de la Peña
 */
public class SyncDestinationsBeanFactoryPostProcessor
	implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory)
		throws BeansException {

		String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();

		for (String beanDefinitionName : beanDefinitionNames) {
			BeanDefinition beanDefinition = beanFactory.getBeanDefinition(
				beanDefinitionName);

			String beanClassName = beanDefinition.getBeanClassName();

			String parallelDestinationClassName =
				ParallelDestination.class.getName();
			String serialDestinationClassName =
				SerialDestination.class.getName();

			if ((beanClassName != null) &&
				beanClassName.equals(parallelDestinationClassName)) {

				beanDefinition.setBeanClassName(serialDestinationClassName);

				if (_log.isInfoEnabled()) {
					_log.info(
						"Bean " + beanDefinitionName +
						"'s type " + parallelDestinationClassName +
						" has been redefined as " +
						serialDestinationClassName);
				}
			}
		}
	}

	private static Log _log =
		LogFactoryUtil.getLog(SyncDestinationsBeanFactoryPostProcessor.class);

}