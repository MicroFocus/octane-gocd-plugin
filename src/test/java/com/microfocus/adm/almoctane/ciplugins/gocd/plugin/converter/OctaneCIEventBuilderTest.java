/*
 *
 *  (c) Copyright 2018 Micro Focus or one of its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * /
 *
 */

package com.microfocus.adm.almoctane.ciplugins.gocd.plugin.converter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * This test ensures that {@link OctaneCIEventBuilder} is working correctly.
 */
public class OctaneCIEventBuilderTest {

	@Test
	public void testParsingFakeZuluDate() {
		Date date = StatusInfoWrapper.parseTime("2017-11-07T14:26:08.720Z");
		Assert.assertNotNull("date should not be null", date);
	}

	@Test
	public void testParsingRFC822TimezoneDate() {
		Date date = StatusInfoWrapper.parseTime("2017-11-07T14:26:08.720+0100");
		Assert.assertNotNull("date should not be null", date);
	}
}
