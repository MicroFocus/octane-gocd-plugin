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


package com.microfocus.adm.almoctane.ciplugins.gocd.service;

import com.thoughtworks.go.plugin.api.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * This class encapsulates the API call to trigger a specific pipeline to be build in Go.
 * This API service is available since Go Version 14.3.0
 * @see <a href="https://api.gocd.org/current/#scheduling-pipelines">Scheduling Pipelines</a>
 */
public class GoSchedulePipeline {

	private static final Logger Log = Logger.getLoggerFor(GoSchedulePipeline.class);

	private final GoApiClient goApiClient;

	public GoSchedulePipeline(GoApiClient goApiClient) {
		this.goApiClient = goApiClient;
	}

	public boolean trigger(final String pipelineName) {
		try {
			HttpPost request = new HttpPost("/go/api/pipelines/" + URLEncoder.encode(pipelineName, "UTF-8") + "/schedule");
			request.addHeader("Confirm", "true");
			request.addHeader("X-GoCD-Confirm", "true");
			request.addHeader("Accept", "application/vnd.go.cd.v1+json");
			HttpResponse response = goApiClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED) {
				return true;
			} else {
				Log.error("Request '" + request.getMethod() + " " + request.getURI() + "' got HTTP-" + response.getStatusLine().getStatusCode());
			}
		} catch (IOException e) {
			Log.error("Could not perform request", e);
		}
		return false;
	}
}
