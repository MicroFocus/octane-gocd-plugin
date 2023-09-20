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

package com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v30.dom;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the root element of a nunit-xml-results-file version 3.0.
 * See also: https://github.com/nunit/docs/wiki/Test-Result-XML-Format
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "test-run")
public class NUnitTestRun {

	@XmlAttribute
	private String id; // supposed to always be "2"
	@XmlAttribute(name = "testcasecount")
	private int testCaseCount;
	@XmlAttribute
	private String result;
	@XmlAttribute
	private String label;
	@XmlAttribute
	private int total;
	@XmlAttribute
	private int passed;
	@XmlAttribute
	private int failed;
	@XmlAttribute
	private int inconclusive;
	@XmlAttribute
	private int skipped;
	@XmlAttribute
	private int asserts;
	@XmlAttribute(name = "engine-version")
	private String engineVersion;
	@XmlAttribute(name = "clr-version")
	private String clrVersion;
	@XmlAttribute(name = "start-time")
	private String startTime;
	@XmlAttribute(name = "end-time")
	private String endTime;
	@XmlAttribute
	private double duration;

	@XmlElement(name = "command-line")
	private String commandLine;
	@XmlElement(name = "test-suite")
	private List<NUnitTestSuite> testSuites;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTestCaseCount() {
		return testCaseCount;
	}

	public void setTestCaseCount(int testCaseCount) {
		this.testCaseCount = testCaseCount;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPassed() {
		return passed;
	}

	public void setPassed(int passed) {
		this.passed = passed;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}

	public int getInconclusive() {
		return inconclusive;
	}

	public void setInconclusive(int inconclusive) {
		this.inconclusive = inconclusive;
	}

	public int getSkipped() {
		return skipped;
	}

	public void setSkipped(int skipped) {
		this.skipped = skipped;
	}

	public int getAsserts() {
		return asserts;
	}

	public void setAsserts(int asserts) {
		this.asserts = asserts;
	}

	public String getEngineVersion() {
		return engineVersion;
	}

	public void setEngineVersion(String engineVersion) {
		this.engineVersion = engineVersion;
	}

	public String getClrVersion() {
		return clrVersion;
	}

	public void setClrVersion(String clrVersion) {
		this.clrVersion = clrVersion;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String getCommandLine() {
		return commandLine;
	}

	public void setCommandLine(String commandLine) {
		this.commandLine = commandLine;
	}

	public List<NUnitTestSuite> getTestSuites() {
		return testSuites;
	}

	public void setTestSuites(List<NUnitTestSuite> testSuites) {
		this.testSuites = testSuites;
	}

	public List<NUnitTestCase> getAllTestCases() {
		final List<NUnitTestCase> cases = new ArrayList<>();
		for (NUnitTestSuite testSuite : testSuites) {
			cases.addAll(testSuite.getAllTestCases());
		}
		return cases;
	}
}
