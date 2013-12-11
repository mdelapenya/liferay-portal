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

package com.liferay.portal.tools.staticanalysis.examples.java;

import static com.liferay.portal.tools.staticanalysis.examples.matchers.CodeBlockMatchers.*;
import static com.liferay.portal.tools.staticanalysis.examples.matchers.MethodDeclarationMatchers.*;
import static com.liferay.portal.tools.staticanalysis.examples.matchers.ClassObjectMatchers.*;
import static com.liferay.portal.tools.staticanalysis.examples.matchers.PackageDeclarationMatchers.*;

import static java.util.Arrays.asList;

import static org.freud.analysed.javasource.JavaSourceDsl.*;
import static org.freud.java.matcher.FreudDsl.*;

import com.liferay.portal.tools.staticanalysis.examples.BaseStaticAnalysisTestCase;
import com.liferay.portal.tools.staticanalysis.examples.matchers.CodeBlockMatchers;
import org.freud.analysed.javasource.CodeBlock;
import org.freud.analysed.javasource.PackageDeclaration;
import org.freud.java.Freud;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public final class JavaSourceExampleTest extends BaseStaticAnalysisTestCase {

	@Test
	public void packageDepthMustBeBetween3And7() throws Exception {
		Freud.iterateOver(PackageDeclaration.class).
			assertThat(
				packageDepth().lessThanOrEqualTo(7).
					and(
						packageDepth().greaterThanOrEqualTo(3)
					)).
			in(
				packageDeclarationsWithin(
					javaSourceOf(
						asList(
							getDependency("ClassWith30LineMethod.javasrc"),
							getDependency("ClassWithIgnoredLongMethod.javasrc")
						))
				)
			).analyse(listener);
	}

	@Test
	public void noSystemOutPrintInCode() throws Exception {
		Freud.iterateOver(CodeBlock.class).
			assertThat(
				no(hasMethodCall("System.out.print")).
			and(
				no(hasMethodCall("System.out.println"))
			)).
			in(
				codeBlocksWithin(
					methodDeclarationsWithin(
						classDeclarationsWithin(
							javaSourceOf(
								asList(
									getDependency(
										"ClassWith30LineMethod.javasrc"),
									getDependency(
										"ClassWithIgnoredLongMethod.javasrc")
								)
							)
						)
					)
				)
			).analyse(listener);
	}

	@Test
	public void codeBlockSizeIsLimitedTo30Lines() throws Exception {
		Freud.iterateOver(CodeBlock.class).
			assertThat(
				codeBlockLines().lessThanOrEqualTo(30)
			).
			in(
				codeBlocksWithin(
					methodDeclarationsWithin(
						classDeclarationsWithin(
							javaSourceOf(
								asList(
									getDependency(
										"ClassWithLongMethod.javasrc")
								)
							)
						)
					)
				)
			).analyse(listener);
	}

	@Test
	public void singletonWithPublicConstructor()
		throws Exception {

		Freud.iterateOver(CodeBlock.class).
			assertThat(
				CodeBlockMatchers.method(
					hasDefaultConstructor()
				)
			).in(
			fieldDeclarationsWithin(
				classDeclarationsWithin(
					javaSourceOf(
						asList(
							getDependency(
								"SingletonWithPublicConstructor.javasrc")
						)
					)
				)
			)
		).analyse(listener);
	}

	@Test
	public void codeBlockSizeIsLimitedToThirtyLinesIfFreudNotSuppressed()
		throws Exception {

		Freud.iterateOver(CodeBlock.class).
			forEach(
				no(
					method(
						hasDeclaredAnnotation(
							"SuppressWarnings",
							Matchers.containsString("Ignore this Freud")
						)
					)
				)
			).
			assertThat(
				codeBlockLines().lessThanOrEqualTo(1)
			).
			in(
				codeBlocksWithin(
					methodDeclarationsWithin(
						classDeclarationsWithin(
							javaSourceOf(
								asList(
									getDependency(
										"ClassWith30LineMethod.javasrc"),
									getDependency(
										"ClassWithIgnoredLongMethod.javasrc")
								)
							)
						)
					)
				)
			).analyse(listener);
	}

	public Class<?> getClazz() {
		return JavaSourceExampleTest.class;
	}

}