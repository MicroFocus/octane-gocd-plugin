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

package com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v30;

import com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v30.dom.NUnitTestRun;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * This parser will parse a given stream into a NUnit-DOM.
 */
public class NUnit30ReportParser {

	private final Unmarshaller unmarshaller;

	public NUnit30ReportParser() {
		try {
			unmarshaller = JAXBContext.newInstance(NUnitTestRun.class).createUnmarshaller();
		} catch (JAXBException e) {
			throw new IllegalArgumentException("Could not initialize Unmarshaller", e);
		}
	}

	public NUnitTestRun parseFrom(InputStream stream) throws JAXBException {
		return (NUnitTestRun)unmarshaller.unmarshal(stream);
	}
}
