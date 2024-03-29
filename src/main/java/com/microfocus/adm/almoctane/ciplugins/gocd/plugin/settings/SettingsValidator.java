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


package com.microfocus.adm.almoctane.ciplugins.gocd.plugin.settings;


import com.microfocus.adm.almoctane.ciplugins.gocd.plugin.validation.ValidationIssue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class validates the given {@link OctaneGoCDPluginSettings} for correctness.
 */
public class SettingsValidator {

	public List<ValidationIssue> validate(OctaneGoCDPluginSettings settings) {
		if (settings == null) {
			throw new IllegalArgumentException("settings can not be null");
		}

		final List<ValidationIssue> issues = new ArrayList<>();

		if (settings.getServerURL() == null || settings.getServerURL().isEmpty()) {
			issues.add(new ValidationIssue("serverURL", "Server URL can not be empty"));
		} else { // check the server URL to be a valid URL
			try {
				new URL(settings.getServerURL());
			} catch (MalformedURLException e) {
				issues.add(new ValidationIssue("serverURL", "Server URL is malformed"));
			}
		}

		if (settings.getClientID() == null || settings.getClientID().isEmpty()) {
			issues.add(new ValidationIssue("clientID", "Client ID can not be empty"));
		}

		if (settings.getClientSecret() == null || settings.getClientSecret().isEmpty()) {
			issues.add(new ValidationIssue("clientSecret", "Client Secret can not be empty"));
			issues.add(new ValidationIssue("clientSecretError", "Client Secret can not be empty"));
		}

		if (settings.getGoUsername() == null || settings.getGoUsername().isEmpty()) {
			issues.add(new ValidationIssue("goUsername", "Go API Username can not be empty"));
		}

		if (settings.getGoPassword() == null || settings.getGoPassword().isEmpty()) {
			issues.add(new ValidationIssue("goPassword", "Go API Password can not be empty"));
			issues.add(new ValidationIssue("goPasswordError", "Go API Password can not be empty"));

		}

		return issues;
	}
}
