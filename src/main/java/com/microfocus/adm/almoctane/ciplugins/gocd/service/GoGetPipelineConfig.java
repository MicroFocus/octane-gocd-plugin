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
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoPipelineConfig;
import com.microfocus.adm.almoctane.ciplugins.gocd.util.GoApiUtil;
import com.thoughtworks.go.plugin.api.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import com.microfocus.adm.almoctane.ciplugins.gocd.util.Streams;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * This class encapsulates the API call to get an complete pipeline configuration from Go.
 * This API service is available since Go Version 15.3.0
 * @see <a href="https://api.gocd.org/current/#get-pipeline-config">Get Pipeline Config</a>
 */
public class GoGetPipelineConfig {

	private static final Logger Log = Logger.getLoggerFor(GoGetPipelineConfig.class);

	private final GoApiClient goApiClient;

	public GoGetPipelineConfig(GoApiClient goApiClient) {
		this.goApiClient = goApiClient;
	}

	public GoPipelineConfig get(final String pipelineName) {
		try {
			HttpGet request = new HttpGet(GoApiUtil.PIPELINE_CONFIG_API + URLEncoder.encode(pipelineName, "UTF-8"));
			request.addHeader("Accept", "application/vnd.go.cd.v11+json");
			HttpResponse response = goApiClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = Streams.readAsString(response.getEntity().getContent());
				return new Gson().fromJson(content, GoPipelineConfig.class);
			} else {
				Log.error("Request got HTTP-" + response.getStatusLine().getStatusCode());
			}
		} catch (IOException e) {
			Log.error("Could not perform request", e);
		} catch (Exception e){
			Log.error("Could not perform request", e);
		}
		return null;
	}
}
