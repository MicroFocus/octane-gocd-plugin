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

import com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v25.BooleanAdapter;
import com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v25.dom.NUnitFailure;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Superclass of {@link NUnitTestSuite} and {@link NUnitTestCase}.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class NUnitTest {

	@XmlAttribute
	private String name;
	@XmlAttribute
	@XmlJavaTypeAdapter(BooleanAdapter.class)
	private Boolean executed;
	@XmlAttribute
	private String result;
	@XmlAttribute
	@XmlJavaTypeAdapter(BooleanAdapter.class)
	private Boolean success;
	@XmlAttribute
	private double time;
	@XmlAttribute
	private int asserts;
	@XmlElement
	private NUnitFailure failure;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean wasExecuted() {
		return executed;
	}

	public void setExecuted(Boolean executed) {
		this.executed = executed;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Boolean wasSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
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
}
