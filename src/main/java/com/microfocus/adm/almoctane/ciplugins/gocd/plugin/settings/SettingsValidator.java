/*
 *
 *  (c) Copyright 2018 Micro Focus or one of its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * /
 *
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
