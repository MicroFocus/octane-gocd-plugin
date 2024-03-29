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


package com.microfocus.adm.almoctane.ciplugins.gocd.plugin.converter;

import com.hp.octane.integrations.dto.DTOFactory;
import com.hp.octane.integrations.dto.tests.TestRun;
import com.hp.octane.integrations.dto.tests.TestRunError;
import com.hp.octane.integrations.dto.tests.TestRunResult;
import com.hp.octane.integrations.dto.tests.TestsResult;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.junit.JUnitReportParser;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.junit.dom.JUnitFailure;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.junit.dom.JUnitTestCase;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.junit.dom.JUnitTestSuite;
import com.microfocus.adm.almoctane.ciplugins.gocd.service.GoApiClient;
import com.microfocus.adm.almoctane.ciplugins.gocd.service.GoGetArtifact;
import com.thoughtworks.go.plugin.api.logging.Logger;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This builder helps converting junit test reports into Octane {@link TestsResult}.
 */
public class OctaneJUnitTestResultsBuilder {

	protected static final Logger Log = Logger.getLoggerFor(OctaneJUnitTestResultsBuilder.class);

	private final GoApiClient goApiClient;

	public OctaneJUnitTestResultsBuilder(GoApiClient goApiClient) {
		this.goApiClient = goApiClient;
	}

	public List<TestRun> convert(String artifactUrl) {
		final List<TestRun> testResults = new ArrayList<>();
		try (InputStream report = new GoGetArtifact(goApiClient).get(artifactUrl)) { // try to parse the given artifact as JUnit report file.
			return convert(report);
		} catch (JAXBException e) {
			Log.debug("parsing artifact '" + artifactUrl + "' as JUnit report failed");
		} catch (IOException e) {
			Log.error("could not read artifact '" + artifactUrl + "' from server", e);
		}
		return testResults;
	}

	public static List<TestRun> convert(InputStream artifactStream) throws JAXBException {
		final List<TestRun> testResults = new ArrayList<>();
		JUnitTestSuite testSuite = new JUnitReportParser().parseFrom(artifactStream);
		if (testSuite != null) {
			List<JUnitTestCase> testCases = testSuite.getTestCases();
			long startTime;
			try {
				startTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(testSuite.getTimestamp()).getTime();
			} catch (Exception e) {
				Log.warn("Could not parse timestamp '" + testSuite.getTimestamp() + "' using current timestamp instead");
				startTime = new Date().getTime();
			}
			for (JUnitTestCase testCase : testCases) {
				testResults.add(DTOFactory.getInstance().newDTO(TestRun.class)
					.setModuleName(testSuite.getName())
					.setPackageName(extractPackageName(testCase.getClassName()))
					.setClassName(extractSimpleClassName(testCase.getClassName()))
					.setTestName(testCase.getName())
					.setDuration((long)(testCase.getTime() * 1000))
					.setStarted(startTime)
					.setResult(convert(testCase))
					.setError(extractTestRunError(testCase)));
			}
		}
		return testResults;
	}

	public static TestRunResult convert(JUnitTestCase testCase) {
		if (testCase.wasSkipped()) {
			return TestRunResult.SKIPPED;
		} else if (testCase.getFailures() != null && !testCase.getFailures().isEmpty()) {
			return TestRunResult.FAILED;
		} else {
			return TestRunResult.PASSED;
		}
	}

	public static String extractPackageName(String className) {
		int index = className.lastIndexOf(".");
		return index > -1 ? className.substring(0, index) : className;
	}

	public static String extractSimpleClassName(String className) {
		int index = className.lastIndexOf(".");
		return index > -1 ? className.substring(index + 1) : className;
	}

	public static TestRunError extractTestRunError(JUnitTestCase testCase) {
		if (testCase.getFailures() != null && !testCase.getFailures().isEmpty()) {
			JUnitFailure failure = testCase.getFailures().get(0);
			return DTOFactory.getInstance().newDTO(TestRunError.class)
				.setErrorMessage(failure.getMessage())
				.setErrorType(failure.getType())
				.setStackTrace(failure.getContent());
		}
		return null;
	}
}
