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

package com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit;

import com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v30.dom.NUnitTestRun;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v30.NUnit30ReportParser;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v30.dom.NUnitTestCase;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v30.dom.NUnitTestResult;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v30.dom.NUnitTestSuite;

import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * Ensure that {@link NUnit30ReportParser} is working correctly.
 */
public class NUnit30ReportParserTest {

	@Test
	public void testParsingNUnit30Report() throws JAXBException {
		NUnitTestRun testRun = new NUnit30ReportParser().parseFrom(getClass().getClassLoader().getResourceAsStream("nunit30.testResults.xml"));
		Assert.assertNotNull("results should not be null", testRun);
		Assert.assertEquals("start time", "2018-01-05 10:21:30Z", testRun.getStartTime());
		Assert.assertEquals("end time", "2018-01-05 10:25:40Z", testRun.getEndTime());
		Assert.assertEquals("number of contained test-cases", 27, testRun.getTotal());

		List<NUnitTestCase> testCases = testRun.getAllTestCases();
		Assert.assertNotNull("testCases should not be null", testCases);
		Assert.assertEquals("number of test cases", 27, testCases.size());

		List<NUnitTestSuite> testSuites = testRun.getTestSuites();
		Assert.assertNotNull("test-suites should not be null", testSuites);
		Assert.assertFalse("test-suites should not be empty", testSuites.isEmpty());
		NUnitTestSuite testSuite = testSuites.get(0);
		Assert.assertNotNull("test-suite should not be null", testSuite);
		Assert.assertEquals("test-suite type", "Assembly", testSuite.getType());
		Assert.assertEquals("test-suite name", "AP.Tests.dll", testSuite.getName());
		Assert.assertEquals("test-suite total", 23, testSuite.getTotal());
		Assert.assertEquals("test-suite result", NUnitTestResult.Skipped, testSuite.getResult());
		Assert.assertEquals("test-suite passed", 21, testSuite.getPassed());
		Assert.assertTrue("test-suite duration is greater zero", 0 < testSuite.getDuration());
		Assert.assertEquals("test-suite number of asserts", 37, testSuite.getAsserts());
	}

	@Test
	public void testParsingNUnit30FailedReport() throws JAXBException {
		NUnitTestRun testRun = new NUnit30ReportParser().parseFrom(getClass().getClassLoader().getResourceAsStream("nunit30.failed.testResults.xml"));
		Assert.assertNotNull("results should not be null", testRun);
		Assert.assertEquals("start time", "2018-01-05 08:37:59Z", testRun.getStartTime());
		Assert.assertEquals("end time", "2018-01-05 08:42:17Z", testRun.getEndTime());
		Assert.assertEquals("number of contained test-cases", 27, testRun.getTotal());

		List<NUnitTestCase> testCases = testRun.getAllTestCases();
		Assert.assertNotNull("testCases should not be null", testCases);
		Assert.assertEquals("number of test cases", 27, testCases.size());

		List<NUnitTestSuite> testSuites = testRun.getTestSuites();
		Assert.assertNotNull("test-suites should not be null", testSuites);
		Assert.assertFalse("test-suites should not be empty", testSuites.isEmpty());
		NUnitTestSuite testSuite = testSuites.get(0);
		Assert.assertNotNull("test-suite should not be null", testSuite);
		Assert.assertEquals("test-suite type", "Assembly", testSuite.getType());
		Assert.assertEquals("test-suite name", "AP.Tests.dll", testSuite.getName());
		Assert.assertEquals("test-suite total", 23, testSuite.getTotal());
		Assert.assertEquals("test-suite result", NUnitTestResult.Failed, testSuite.getResult());
		Assert.assertEquals("test-suite passed", 18, testSuite.getPassed());
		Assert.assertTrue("test-suite duration is greater zero", 0 < testSuite.getDuration());
		Assert.assertEquals("test-suite number of asserts", 37, testSuite.getAsserts());

	}
}
