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
import java.util.List;

/**
 * Superclass of {@link NUnitTestSuite} and {@link NUnitTestCase}.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class NUnitTest {

	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	@XmlAttribute(name = "fullname")
	private String fullName;
	@XmlAttribute(name = "classname")
	private String className;
	@XmlAttribute(name = "testcasecount")
	private int testCaseCount;
	@XmlAttribute(name = "runstate")
	private NUnitRunState runState;
	@XmlAttribute
	private NUnitTestResult result;
	@XmlAttribute
	private String label;
	@XmlAttribute
	private String site;
	@XmlAttribute(name = "start-time")
	private String startTime;
	@XmlAttribute(name = "end-time")
	private String endTime;
	@XmlAttribute
	private double duration;
	@XmlAttribute
	private int asserts;

	@XmlElement
	private NUnitFailure failure;

	@XmlElement(name = "property")
	@XmlElementWrapper(name = "properties")
	private List<NUnitProperty> properties;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getTestCaseCount() {
		return testCaseCount;
	}

	public void setTestCaseCount(int testCaseCount) {
		this.testCaseCount = testCaseCount;
	}

	public NUnitRunState getRunState() {
		return runState;
	}

	public void setRunState(NUnitRunState runState) {
		this.runState = runState;
	}

	public NUnitTestResult getResult() {
		return result;
	}

	public void setResult(NUnitTestResult result) {
		this.result = result;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
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

	public int getAsserts() {
		return asserts;
	}

	public void setAsserts(int asserts) {
		this.asserts = asserts;
	}

	public NUnitFailure getFailure() {
		return failure;
	}

	public void setFailure(NUnitFailure failure) {
		this.failure = failure;
	}

	public List<NUnitProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<NUnitProperty> properties) {
		this.properties = properties;
	}
}
