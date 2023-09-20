/**
 * Copyright 2018-2023 Open Text
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.microfocus.adm.almoctane.ciplugins.gocd.report.junit;

import com.microfocus.adm.almoctane.ciplugins.gocd.report.junit.dom.JUnitTestCase;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.junit.dom.JUnitTestSuite;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * Ensure that {@link JUnitReportParser} is working correctly.
 */
public class JUnitReportParserTest {

	@Test
	public void testParsingJUnit4Report() throws JAXBException {
		JUnitTestSuite testSuite = new JUnitReportParser().parseFrom(getClass().getClassLoader().getResourceAsStream("junit.testResults.xml"));
		Assert.assertNotNull("testSuite should not be null", testSuite);
		Assert.assertEquals("testSuite name", "com.microfocus.adm.almoctane.ciplugins.gocd.report.junit.JUnitReportParserTest", testSuite.getName());
		Assert.assertEquals("testSuite hostname", "hypo", testSuite.getHostname());
		Assert.assertEquals("testSuite timestamp", "2017-11-08T13:13:06", testSuite.getTimestamp());
		Assert.assertTrue("testSuite time is greater zero", 0 < testSuite.getTime());
		List<JUnitTestCase> testCase = testSuite.getTestCases();
		Assert.assertNotNull("testCase should not be null", testCase);
		Assert.assertFalse("testCase should not be empty", testCase.isEmpty());
	}
}
