/*
 * (c) Copyright 2018 Micro Focus or one of its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v25.dom;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A culture info.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class NUnitTestSuite extends NUnitTest {

	@XmlAttribute
	private String type;

	@XmlElement(name = "property")
	@XmlElementWrapper(name = "properties")
	private List<NUnitProperty> properties;

	@XmlElements({
		@XmlElement(name = "test-suite", type = NUnitTestSuite.class),
		@XmlElement(name = "test-case", type = NUnitTestCase.class)
	})
	@XmlElementWrapper(name = "results")
	private List<NUnitTest> tests;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<NUnitProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<NUnitProperty> properties) {
		this.properties = properties;
	}

	public List<NUnitTest> getTests() {
		return tests;
	}

	public void setTests(List<NUnitTest> tests) {
		this.tests = tests;
	}

	public List<NUnitTestCase> getAllTestCases() {
		final List<NUnitTestCase> cases = new ArrayList<>();
		if(tests!=null && tests.size() > 0) {
			for (NUnitTest test : tests) {
				if (test instanceof NUnitTestSuite) {
					cases.addAll(((NUnitTestSuite) test).getAllTestCases());
				} else if (test instanceof NUnitTestCase) {
					cases.add((NUnitTestCase) test);
				}
			}
		}
		return cases;
	}
}
