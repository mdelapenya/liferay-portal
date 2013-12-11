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

package com.liferay.portal.tools.staticanalysis;

import java.util.Deque;
import java.util.LinkedList;

import org.freud.core.listener.AnalysisListener;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;

import org.junit.Assert;

/**
 * @author Manuel de la Peña
 */
public class LiferayAnalisysListener implements AnalysisListener {

	public void assertFailed(int total) {
		Assert.assertEquals("failed count", total, failed.size());
	}

	public void assertFailed(Object analysed) {
		assertOutcome(getMatcher(analysed), failed, "failed");
	}

	public void assertFiltered(int total) {
		Assert.assertEquals("filtered count", total, filtered.size());
	}

	public void assertFiltered(Object analysed) {
		assertOutcome(getMatcher(analysed), filtered, "filtered");
	}

	public void assertNotFailed() {
		assertFailed(0);
	}

	public void assertNotFiltered() {
		assertFiltered(0);
	}

	public void assertNotPassed() {
		assertPassed(0);
	}

	public void assertPassed(int total) {
		Assert.assertEquals("passed count", total, passed.size());
	}

	public void assertPassed(Object analysed) {
		assertOutcome(getMatcher(analysed), passed, "passed");
	}

	@Override
	public void done() {
	}

	@Override
	public void failed(final Object analysedObject, final String details) {
		failed.add(analysedObject);
	}

	@Override
	public void filtered(final Object analysedObject, final String details) {
		filtered.add(analysedObject);
	}

	public Deque<Object> getFailed() {
		return failed;
	}

	public Deque<Object> getFiltered() {
		return filtered;
	}

	public Deque<Object> getPassed() {
		return passed;
	}

	public int getTotalObjectsAnalysed() {
		return passed.size() + failed.size() + filtered.size() +
			unexpected.size();
	}

	public Deque<Exception> getUnexpected() {
		return unexpected;
	}

	@Override
	public void passed(final Object analysedObject) {
		passed.add(analysedObject);
	}

	@Override
	public void unexpected(
		final Object analysedObject, final Exception exception) {

		unexpected.add(exception);
	}

	@SuppressWarnings("unchecked")
	private void assertOutcome(
		Matcher matcher, final Deque<Object> deque, String eventDescription) {

		for (Object analysedObject : deque) {
			if (matcher.matches(analysedObject)) {
				return;
			}
		}

		StringDescription description = new StringDescription();

		matcher.describeTo(description);

		Assert.fail(eventDescription + ": " + description.toString());
	}

	private Matcher getMatcher(Object analysed) {
		if (analysed instanceof Matcher) {
			return (Matcher)analysed;
		}

		return Matchers.equalTo(analysed);
	}

	private Deque<Object> failed = new LinkedList<Object>();
	private Deque<Object> filtered = new LinkedList<Object>();
	private Deque<Object> passed = new LinkedList<Object>();
	private Deque<Exception> unexpected = new LinkedList<Exception>();

}