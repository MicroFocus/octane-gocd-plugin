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

package com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v25.dom;

import javax.xml.bind.annotation.*;

/**
 * This is the root element of a xml-results-file.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "test-results")
public class NUnitTestResults {

	@XmlAttribute
	private String name;
	@XmlAttribute
	private int total;
	@XmlAttribute
	private int errors;
	@XmlAttribute
	private int failures;
	@XmlAttribute(name = "not-run")
	private int notRun;
	@XmlAttribute
	private int inconclusive;
	@XmlAttribute
	private int ignored;
	@XmlAttribute
	private int skipped;
	@XmlAttribute
	private int invalid;
	@XmlAttribute
	private String date;
	@XmlAttribute
	private String time;

	@XmlElement(name = "environment")
	private NUnitEnvironment environment;
	@XmlElement(name = "culture-info")
	private NUnitCultureInfo cultureInfo;
	@XmlElement(name = "test-suite")
	private NUnitTestSuite testSuite;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getErrors() {
		return errors;
	}

	public void setErrors(int errors) {
		this.errors = errors;
	}

	public int getFailures() {
		return failures;
	}

	public void setFailures(int failures) {
		this.failures = failures;
	}

	public int getNotRun() {
		return notRun;
	}

	public void setNotRun(int notRun) {
		this.notRun = notRun;
	}

	public int getInconclusive() {
		return inconclusive;
	}

	public void setInconclusive(int inconclusive) {
		this.inconclusive = inconclusive;
	}

	public int getIgnored() {
		return ignored;
	}

	public void setIgnored(int ignored) {
		this.ignored = ignored;
	}

	public int getSkipped() {
		return skipped;
	}

	public void setSkipped(int skipped) {
		this.skipped = skipped;
	}

	public int getInvalid() {
		return invalid;
	}

	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public NUnitEnvironment getEnvironment() {
		return environment;
	}

	public void setEnvironment(NUnitEnvironment environment) {
		this.environment = environment;
	}

	public NUnitCultureInfo getCultureInfo() {
		return cultureInfo;
	}

	public void setCultureInfo(NUnitCultureInfo cultureInfo) {
		this.cultureInfo = cultureInfo;
	}

	public NUnitTestSuite getTestSuite() {
		return testSuite;
	}

	public void setTestSuite(NUnitTestSuite testSuite) {
		this.testSuite = testSuite;
	}
}
