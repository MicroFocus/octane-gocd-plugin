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


package com.microfocus.adm.almoctane.ciplugins.gocd.service;

import com.google.gson.Gson;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoArtifact;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoArtifactList;
import com.microfocus.adm.almoctane.ciplugins.gocd.util.Streams;
import com.thoughtworks.go.plugin.api.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

/**
 * This class encapsulates the request to get all artifacts of a job from Go.
 * This API service is available since Go Version 14.3.0
 * @see <a href="https://api.gocd.org/current/#get-all-artifacts"> Get All Artifacts</a>
 *
 * Please notice that this request is no API request (it doesn't begin with "/go/api/").
 * This means Basic-Authentication does not work. The {@link #goApiClient} has to be set
 * up with an authentication cookie. This can be achieved be excuting an API request
 * before this request is done. Since the {@link GoApiClient} is stateful, it will store
 * and reuse any session cookies.
 */
public class GoGetAllArtifacts {

	private static final Logger Log = Logger.getLoggerFor(GoGetAllArtifacts.class);

	private final GoApiClient goApiClient;

	public GoGetAllArtifacts(GoApiClient goApiClient) {
		this.goApiClient = goApiClient;
	}

	public List<GoArtifact> get(final String pipelineName, final int pipelineCounter, final String stageName, final int stageCounter, final String jobName) {
		try {
			HttpResponse response = goApiClient.execute(new HttpGet("/go/files/" +
				URLEncoder.encode(pipelineName, "UTF-8") + "/" + pipelineCounter + "/" +
				URLEncoder.encode(stageName, "UTF-8") + "/" + stageCounter + "/" +
				URLEncoder.encode(jobName, "UTF-8") + ".json"));
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = Streams.readAsString(response.getEntity().getContent());
				return new Gson().fromJson(content, GoArtifactList.class);
			} else {
				Log.error("Request got HTTP-" + response.getStatusLine().getStatusCode());
			}
		} catch (IOException e) {
			Log.error("Could not perform request", e);
		}
		return Collections.emptyList();
	}
}
