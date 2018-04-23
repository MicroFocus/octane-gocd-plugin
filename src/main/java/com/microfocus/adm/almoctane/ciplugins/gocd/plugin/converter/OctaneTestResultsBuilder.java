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


package com.microfocus.adm.almoctane.ciplugins.gocd.plugin.converter;

import com.hp.octane.integrations.dto.events.CIEvent;
import com.hp.octane.integrations.dto.tests.TestRun;
import com.hp.octane.integrations.dto.tests.TestsResult;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoArtifact;
import com.microfocus.adm.almoctane.ciplugins.gocd.service.GoApiClient;
import com.thoughtworks.go.plugin.api.logging.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This builder helps converting test reports into Octane {@link TestsResult}.
 */
public class OctaneTestResultsBuilder {

	protected static final Logger Log = Logger.getLoggerFor(OctaneTestResultsBuilder.class);

	private final GoApiClient goApiClient;

	public OctaneTestResultsBuilder(GoApiClient goApiClient) {
		this.goApiClient = goApiClient;
	}

	/**
	 * This method will analyze the given 'statusInfo' and may or may not generate
	 * and send a {@link CIEvent} to Octane.
	 * @param artifacts
	 * @return TestRun's list
	 */
	public List<TestRun> convert(List<GoArtifact> artifacts) {
		final List<TestRun> testResults = new ArrayList<>();
		if (artifacts != null) {
			for (GoArtifact artifact : artifacts) {
				if ("folder".equals(artifact.getType())) {
					testResults.addAll(convert(artifact.getFiles()));
				} else if ("file".equals(artifact.getType()) && artifact.getName() != null && artifact.getName().toLowerCase().endsWith(".xml")) {
					testResults.addAll(convert(artifact));
				}
			}
		}
		return testResults;
	}

	public List<TestRun> convert(GoArtifact artifact) {
		final List<TestRun> testResults = new ArrayList<>();
		testResults.addAll(new OctaneJUnitTestResultsBuilder(goApiClient).convert(artifact.getUrl()));
		testResults.addAll(new OctaneNUnit25TestResultsBuilder(goApiClient).convert(artifact.getUrl()));
		testResults.addAll(new OctaneNUnit30TestResultsBuilder(goApiClient).convert(artifact.getUrl()));
		// create some logfile output.
		if (!testResults.isEmpty()) {
			Log.info("artifact '" + artifact.getUrl() + "' could be parsed as test-result-file. Found " + testResults.size() + " tests.");
		} else {
			Log.debug("artifact '" + artifact.getUrl() + "' could not be parsed as test-result-file");
		}
		return testResults;
	}
}
