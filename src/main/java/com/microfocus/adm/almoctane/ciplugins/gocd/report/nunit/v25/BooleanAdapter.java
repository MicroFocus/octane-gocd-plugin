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

package com.microfocus.adm.almoctane.ciplugins.gocd.report.nunit.v25;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * In NUnit xml file boolean values are written as "True" or "False".
 * This adapter helps converting them.
 */
public class BooleanAdapter extends XmlAdapter<String,Boolean> {

	@Override
	public Boolean unmarshal(String value) throws Exception {
		if ("True".equals(value)) {
			return Boolean.TRUE;
		} else if ("False".equals(value)) {
			return Boolean.FALSE;
		}
		return null;
	}

	@Override
	public String marshal(Boolean value) throws Exception {
		if (Boolean.TRUE.equals(value)) {
			return "True";
		} else if (Boolean.FALSE.equals(value)) {
			return "False";
		}
		return null;
	}
}
