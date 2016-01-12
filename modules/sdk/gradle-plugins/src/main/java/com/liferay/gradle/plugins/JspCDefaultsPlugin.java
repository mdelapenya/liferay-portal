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

package com.liferay.gradle.plugins;

import com.liferay.gradle.plugins.extensions.LiferayExtension;
import com.liferay.gradle.plugins.jasper.jspc.JspCExtension;
import com.liferay.gradle.plugins.jasper.jspc.JspCPlugin;
import com.liferay.gradle.plugins.util.FileUtil;
import com.liferay.gradle.util.GradleUtil;

import java.io.File;

import java.util.concurrent.Callable;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileTree;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.bundling.Jar;

/**
 * @author Andrea Di Giorgi
 */
public class JspCDefaultsPlugin
	extends BasePortalToolDefaultsPlugin<JspCPlugin> {

	public static final String UNZIP_JAR_TASK_NAME = "unzipJar";

	protected void addDependenciesJspC(Project project) {
		PluginContainer pluginContainer = project.getPlugins();

		if (!pluginContainer.hasPlugin(LiferayPlugin.class)) {
			return;
		}

		LiferayExtension liferayExtension = GradleUtil.getExtension(
			project, LiferayExtension.class);

		GradleUtil.addDependency(
			project, JspCPlugin.CONFIGURATION_NAME,
			liferayExtension.getAppServerLibGlobalDir());

		FileTree fileTree = FileUtil.getJarsFileTree(
			project, liferayExtension.getAppServerLibGlobalDir());

		GradleUtil.addDependency(
			project, JspCPlugin.CONFIGURATION_NAME, fileTree);

		fileTree = FileUtil.getJarsFileTree(
			project,
			new File(liferayExtension.getAppServerPortalDir(), "WEB-INF/lib"));

		GradleUtil.addDependency(
			project, JspCPlugin.CONFIGURATION_NAME, fileTree);

		String appServerType = liferayExtension.getAppServerType();

		if (appServerType.equals("jonas")) {
			File dir = new File(
				liferayExtension.getAppServerDir(), "WEB-INF/lib");

			FileCollection fileCollection = project.files(
				new File(dir, "xercesImpl.jar"), new File(dir, "xml-apis.jar"));

			GradleUtil.addDependency(
				project, JspCPlugin.CONFIGURATION_NAME, fileCollection);
		}

		fileTree = FileUtil.getJarsFileTree(
			project,
			new File(liferayExtension.getLiferayHome(), "osgi/modules"));

		GradleUtil.addDependency(
			project, JspCPlugin.CONFIGURATION_NAME, fileTree);

		ConfigurableFileCollection configurableFileCollection = project.files(
			getUnzippedJarDir(project));

		configurableFileCollection.builtBy(UNZIP_JAR_TASK_NAME);

		GradleUtil.addDependency(
			project, JspCPlugin.CONFIGURATION_NAME, configurableFileCollection);
	}

	@Override
	protected void addPortalToolDependencies(Project project) {
		super.addPortalToolDependencies(project);

		GradleUtil.addDependency(
			project, getPortalToolConfigurationName(), "org.apache.ant", "ant",
			"1.9.4");
	}

	protected Task addTaskUnzipJar(final Project project) {
		Task task = project.task(UNZIP_JAR_TASK_NAME);

		final Jar jar = (Jar)GradleUtil.getTask(
			project, JavaPlugin.JAR_TASK_NAME);

		task.dependsOn(jar);

		task.doLast(
			new Action<Task>() {

				@Override
				public void execute(Task task) {
					Project project = task.getProject();

					FileUtil.unzip(
						project, jar.getArchivePath(),
						getUnzippedJarDir(project));
				}

			});

		return task;
	}

	@Override
	protected void configureDefaults(Project project, JspCPlugin jspCPlugin) {
		super.configureDefaults(project, jspCPlugin);

		addTaskUnzipJar(project);

		configureJspCExtension(project);

		project.afterEvaluate(
			new Action<Project>() {

				@Override
				public void execute(Project project) {
					addDependenciesJspC(project);
				}

			});
	}

	protected void configureJspCExtension(final Project project) {
		JspCExtension jspCExtension = GradleUtil.getExtension(
			project, JspCExtension.class);

		jspCExtension.setModuleWeb(true);

		jspCExtension.setPortalDir(
			new Callable<File>() {

				@Override
				public File call() throws Exception {
					LiferayExtension liferayExtension = GradleUtil.getExtension(
						project, LiferayExtension.class);

					return liferayExtension.getAppServerPortalDir();
				}

			});

		jspCExtension.setWebAppDir(
			new Callable<File>() {

				@Override
				public File call() throws Exception {
					File unzippedJarDir = getUnzippedJarDir(project);

					File resourcesDir = new File(
						unzippedJarDir, "META-INF/resources");

					if (resourcesDir.exists()) {
						return resourcesDir;
					}

					return unzippedJarDir;
				}

			});
	}

	@Override
	protected Class<JspCPlugin> getPluginClass() {
		return JspCPlugin.class;
	}

	@Override
	protected String getPortalToolConfigurationName() {
		return JspCPlugin.TOOL_CONFIGURATION_NAME;
	}

	@Override
	protected String getPortalToolName() {
		return _PORTAL_TOOL_NAME;
	}

	protected File getUnzippedJarDir(Project project) {
		return new File(project.getBuildDir(), "unzipped-jar");
	}

	private static final String _PORTAL_TOOL_NAME = "com.liferay.jasper.jspc";

}