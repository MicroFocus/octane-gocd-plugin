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
import com.microfocus.adm.almoctane.ciplugins.gocd.service.GoApiClient;
import com.microfocus.adm.almoctane.ciplugins.gocd.service.GoGetArtifact;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v30.NUnit30ReportParser;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v30.dom.NUnitFailure;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v30.dom.NUnitTestCase;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v30.dom.NUnitTestRun;
import com.thoughtworks.go.plugin.api.logging.Logger;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This builder helps converting nUnit test reports into Octane {@link TestsResult}.
 */
public class OctaneNUnit30TestResultsBuilder {

	protected static final Logger Log = Logger.getLoggerFor(OctaneNUnit30TestResultsBuilder.class);

	private final GoApiClient goApiClient;

	public OctaneNUnit30TestResultsBuilder(GoApiClient goApiClient) {
		this.goApiClient = goApiClient;
	}

	public List<TestRun> convert(String artifactUrl) {
		final List<TestRun> testRuns = new ArrayList<>();
		try (InputStream report = new GoGetArtifact(goApiClient).get(artifactUrl)) { // try to parse the given artifact as JUnit report file.
			return convert(report);
		} catch (JAXBException e) {
			Log.debug("parsing artifact '" + artifactUrl + "' as NUnit(3.0) report failed");
		} catch (IOException e) {
			Log.error("could not read artifact '" + artifactUrl + "' from server", e);
		}
		return testRuns;
	}

	public static List<TestRun> convert(InputStream artifactStream) throws JAXBException {
		final List<TestRun> testRuns = new ArrayList<>();
		NUnitTestRun testRun = new NUnit30ReportParser().parseFrom(artifactStream);
		if (testRun != null) {
			List<NUnitTestCase> testCases = testRun.getAllTestCases();
			long startTime;
			try {
				startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX").parse(testRun.getStartTime()).getTime();
			} catch (ParseException e) {
				Log.warn("Could not parse timestamp '" + testRun.getStartTime() + "' using current timestamp instead");
				startTime = new Date().getTime();
			}
			for (NUnitTestCase testCase : testCases) {
				testRuns.add(DTOFactory.getInstance().newDTO(TestRun.class)
					.setPackageName(extractPackageName(testCase.getClassName()))
					.setClassName(extractSimpleClassName(testCase.getClassName()))
					.setTestName(testCase.getName())
					.setDuration((long)(testCase.getDuration() * 1000))
					.setStarted(startTime)
					.setResult(convert(testCase))
					.setError(extractTestRunError(testCase)));
			}
		}
		return testRuns;
	}

	public static TestRunResult convert(NUnitTestCase testCase) {
		switch (testCase.getResult()) {
			case Passed: return TestRunResult.PASSED;
			case Failed: return TestRunResult.FAILED;
			default: return TestRunResult.SKIPPED; // covers the states Skipped and Inconclusive.
		}
	}

	public static String extractSimpleClassName(String fullyQualifiedClassName) {
		int index = fullyQualifiedClassName.lastIndexOf(".");
		return index > -1 ? fullyQualifiedClassName.substring(index + 1) : fullyQualifiedClassName;
	}

	public static String extractPackageName(String fullyQualifiedClassName) {
		int index = fullyQualifiedClassName.lastIndexOf(".");
		return index > -1 ? fullyQualifiedClassName.substring(0, index) : fullyQualifiedClassName;
	}

	public static String extractTestName(String testCaseName) {
		int index = testCaseName.lastIndexOf(".");
		return index > -1 ? testCaseName.substring(index + 1) : testCaseName;
	}

	public static TestRunError extractTestRunError(NUnitTestCase testCase) {
		NUnitFailure failure = testCase.getFailure();
		if (failure != null) {
			return DTOFactory.getInstance().newDTO(TestRunError.class)
				.setErrorMessage(failure.getMessage())
				.setStackTrace(failure.getStacktrace());
		}
		return null;
	}
}
