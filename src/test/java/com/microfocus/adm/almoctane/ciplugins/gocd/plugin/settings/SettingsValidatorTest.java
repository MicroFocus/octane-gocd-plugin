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
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * This test ensures that {@link SettingsValidator} is working correctly.
 */
public class SettingsValidatorTest {

	@Test(expected = IllegalArgumentException.class)
	public void testAgainstNullSettings() {
		new SettingsValidator().validate(null);
	}

	@Test
	public void testCorrectSettings() {
		final List<ValidationIssue> issues = new SettingsValidator().validate(new OctaneGoCDPluginSettings()
			.setServerURL("https://foobar.org/wildpath?token=s")
			.setClientID("nobody")
			.setClientSecret("key")
			.setGoUsername("alice")
			.setGoPassword("42"));
		Assert.assertNotNull("list of issues should not be null", issues);
		Assert.assertTrue("list of issues should be empty", issues.isEmpty());
	}

	@Test
	public void testAgainstSettingsWithMissingServerURL() {
		final List<ValidationIssue> issues = new SettingsValidator().validate(new OctaneGoCDPluginSettings()
			.setClientID("nobody")
			.setClientSecret("key"));
		Assert.assertNotNull("list of issues should not be null", issues);
		Assert.assertFalse("list of issues should not be empty", issues.isEmpty());
		Assert.assertTrue("list should contain validation issue for missing serverURL", issues.contains(new ValidationIssue("serverURL", "Server URL can not be empty")));
	}

	@Test
	public void testAgainstSettingsWithMalformedServerURL() {
		final List<ValidationIssue> issues = new SettingsValidator().validate(new OctaneGoCDPluginSettings()
			.setServerURL("://forbar.org")
			.setClientID("nobody")
			.setClientSecret("key"));
		Assert.assertNotNull("list of issues should not be null", issues);
		Assert.assertFalse("list of issues should not be empty", issues.isEmpty());
		Assert.assertTrue("list should contain validation issue for malformed serverURL", issues.contains(new ValidationIssue("serverURL", "Server URL is malformed")));
	}

	@Test
	public void testAgainstSettingsWithMissingClientID() {
		final List<ValidationIssue> issues = new SettingsValidator().validate(new OctaneGoCDPluginSettings()
			.setServerURL("https://forbar.org")
			.setClientSecret("key"));
		Assert.assertNotNull("list of issues should not be null", issues);
		Assert.assertFalse("list of issues should not be empty", issues.isEmpty());
		Assert.assertTrue("list should contain validation issue for missing clientID", issues.contains(new ValidationIssue("clientID", "Client ID can not be empty")));
	}

	@Test
	public void testAgainstSettingsWithMissingClientSecret() {
		final List<ValidationIssue> issues = new SettingsValidator().validate(new OctaneGoCDPluginSettings()
			.setServerURL("https://forbar.org")
			.setClientID("nobody"));
		Assert.assertNotNull("list of issues should not be null", issues);
		Assert.assertFalse("list of issues should not be empty", issues.isEmpty());
		Assert.assertTrue("list should contain validation issue for missing clientSecret", issues.contains(new ValidationIssue("clientSecret", "Client Secret can not be empty")));
	}

	@Test
	public void testAgainstSettingsWithMissingGoUsername() {
		final List<ValidationIssue> issues = new SettingsValidator().validate(new OctaneGoCDPluginSettings()
			.setServerURL("https://forbar.org")
			.setClientID("nobody")
			.setClientSecret("key")
			.setGoPassword("57"));
		Assert.assertNotNull("list of issues should not be null", issues);
		Assert.assertFalse("list of issues should not be empty", issues.isEmpty());
		Assert.assertTrue("list should contain validation issue for missing clientSecret", issues.contains(new ValidationIssue("goUsername", "Go API Username can not be empty")));
	}

	@Test
	public void testAgainstSettingsWithMissingGoPassword() {
		final List<ValidationIssue> issues = new SettingsValidator().validate(new OctaneGoCDPluginSettings()
			.setServerURL("https://forbar.org")
			.setClientID("nobody")
			.setClientSecret("key")
			.setGoUsername("alice"));
		Assert.assertNotNull("list of issues should not be null", issues);
		Assert.assertFalse("list of issues should not be empty", issues.isEmpty());
		Assert.assertTrue("list should contain validation issue for missing clientSecret", issues.contains(new ValidationIssue("goPassword", "Go API Password can not be empty")));
	}

	@Test
	public void testAgainstEmptySettings() {
		final List<ValidationIssue> issues = new SettingsValidator().validate(new OctaneGoCDPluginSettings());
		Assert.assertNotNull("list of issues should not be null", issues);
		Assert.assertFalse("list of issues should not be empty", issues.isEmpty());
		Assert.assertTrue("list should contain validation issue for missing serverURL", issues.contains(new ValidationIssue("serverURL", "Server URL can not be empty")));
		Assert.assertTrue("list should contain validation issue for missing clientID", issues.contains(new ValidationIssue("clientID", "Client ID can not be empty")));
		Assert.assertTrue("list should contain validation issue for missing clientSecret", issues.contains(new ValidationIssue("clientSecret", "Client Secret can not be empty")));
	}
}
