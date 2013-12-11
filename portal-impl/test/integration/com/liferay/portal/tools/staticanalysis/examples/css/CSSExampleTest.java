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

package com.liferay.portal.tools.staticanalysis.examples.css;

import static com.liferay.portal.tools.staticanalysis.examples.matchers.CssDeclarationMatchers.declarationKey;
import static com.liferay.portal.tools.staticanalysis.examples.matchers.CssDeclarationMatchers.declarationValue;
import static com.liferay.portal.tools.staticanalysis.examples.matchers.CssRuleMatchers.containsSelector;
import static com.liferay.portal.tools.staticanalysis.examples.matchers.CssRuleMatchers.lastIndexOfSelector;
import static com.liferay.portal.tools.staticanalysis.examples.matchers.CssRuleMatchers.selectors;
import static com.liferay.portal.tools.staticanalysis.examples.matchers.CssSelectorMatchers.classSelector;
import static com.liferay.portal.tools.staticanalysis.examples.matchers.CssSelectorMatchers.idSelector;
import static com.liferay.portal.tools.staticanalysis.examples.matchers.CssSelectorMatchers.selector;

import static org.freud.analysed.css.CssDsl.cssDeclarationsWithin;
import static org.freud.analysed.css.CssDsl.cssRulesOf;
import static org.freud.analysed.css.CssDsl.cssSelectorsWithin;
import static org.freud.analysed.css.rule.selector.CssSelector.Type.CLASS;
import static org.freud.analysed.css.rule.selector.CssSelector.Type.ID;
import static org.freud.analysed.css.rule.selector.CssSelector.Type.TAG;
import static org.freud.java.matcher.FreudDsl.no;

import static java.util.Arrays.asList;

import java.net.URL;

import com.liferay.portal.tools.staticanalysis.examples.BaseStaticAnalysisTestCase;
import org.freud.analysed.css.rule.CssRule;
import org.freud.analysed.css.rule.declaration.CssDeclaration;
import org.freud.analysed.css.rule.selector.CssSelector;
import org.freud.java.Freud;

import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class CSSExampleTest extends BaseStaticAnalysisTestCase {

	/**
	* @see https://developer.mozilla.org/en/Writing_Efficient_CSS
	*/
	@Test
	public void doNotQualifyIdRuleWithTagName() throws Exception {
		Freud.iterateOver(CssRule.class).
			assertThat(
				no(
					containsSelector(TAG).
						and(
							lastIndexOfSelector(TAG).lessThan(
								lastIndexOfSelector(ID)
							)
						)
				)
			).in(
				cssRulesOf(
					asList(
						getDependency("file.css")
					)
				)
			).analyse(listener);
	}

	/**
	* @see https://developer.mozilla.org/en/Writing_Efficient_CSS
	*/
	@Test
	public void doNotQualifyIdRuleWithClassName() throws Exception {
		Freud.iterateOver(CssRule.class).
			assertThat(
				no(
					containsSelector(CLASS).
						and(
							lastIndexOfSelector(CLASS).lessThan(
								lastIndexOfSelector(ID)
							)
						)
				)
			).in(
				cssRulesOf(
					asList(
						getDependency(
							"doNotQualifyIdRuleWithTagOrClassName.css")
					)
				)
			).analyse(listener);

		listener.assertFailed(1);
		listener.assertPassed(1);
	}

	/**
	* @see http://css-tricks.com/efficiently-rendering-css/
	*/
	@Test
	public void descendantSelectorsAreTheWorst() throws Exception {
		Freud.iterateOver(CssRule.class).
			assertThat(
				selectors(TAG).lessThanOrEqualTo(1)
			).in(
				cssRulesOf(
					asList(
						getDependency("descendantSelectorsAreTheWorst.css")
					)
				)
			).analyse(listener);

		listener.assertNotFailed();
	}

	/**
	* @see http://css-tricks.com/efficiently-rendering-css/
	*/
	@Test
	public void descendantSelectorsAreTheWorstFailing() throws Exception {
		Freud.iterateOver(CssRule.class).
			assertThat(
				selectors(TAG).lessThanOrEqualTo(1)
			).in(
				cssRulesOf(
					asList(
						getDependency(
							"descendantSelectorsAreTheWorst_failing.css")
					)
				)
			).analyse(listener);

		listener.assertFailed(1);
		listener.assertPassed(2);
	}

	@Test
	public void classOrIdCssSelectorsNameMustNotContainUpperCaseCharacters()
		throws Exception {

		Freud.iterateOver(CssSelector.class).
			forEach(
				classSelector().or(idSelector())
			).
			assertThat(
				no(
					selector().contains("[A-Z]")
				)
			).in(
				cssSelectorsWithin(
					cssRulesOf(
						asList(
							getDependency("file.css")
						)
					)
				)
			).analyse(listener);

		listener.assertNotFailed();
	}

	@Test
	public void cssDisplayDeclarationIsAlwaysNone() throws Exception {
		Freud.iterateOver(CssDeclaration.class).
			forEach(
				declarationKey().matches("display")
			).
			assertThat(
				declarationValue().matches("none")
			).in(
				cssDeclarationsWithin(
					cssRulesOf(
						asList(
							getDependency("file.css")
						)
					)
				)
			).analyse(listener);

		listener.assertPassed(7);
		listener.assertFailed(4);
	}

	public Class<?> getClazz() {
		return CSSExampleTest.class;
	}

}