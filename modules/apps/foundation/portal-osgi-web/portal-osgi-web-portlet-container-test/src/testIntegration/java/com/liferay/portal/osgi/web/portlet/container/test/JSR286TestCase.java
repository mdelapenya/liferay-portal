package com.liferay.portal.osgi.web.portlet.container.test;

/**
 * @author Manuel de la Peña
 */
public interface JSR286TestCase {

	public void testCopyCurrentRenderParameters() throws Exception;

	public void testRemoveCopiedCurrentRenderParameter() throws Exception;

	public void testRemoveRegularParameter() throws Exception;

}
