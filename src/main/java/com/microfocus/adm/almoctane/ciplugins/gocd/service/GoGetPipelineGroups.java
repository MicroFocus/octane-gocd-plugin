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

import com.google.gson.Gson;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoPipelineGroup;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoPipelineGroupsContainer;
import com.microfocus.adm.almoctane.ciplugins.gocd.util.Streams;
import com.thoughtworks.go.plugin.api.logging.Logger;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * This class encapsulates the API call to get all configured pipeline groups from Go.
 * This API service is available since Go Version 14.3.0
 * @see <a href="https://api.gocd.org/17.9.0/#config-listing">Config listing</a>
 */
public class GoGetPipelineGroups {

	private static final Logger Log = Logger.getLoggerFor(GoGetPipelineGroups.class);

	private final GoApiClient goApiClient;

	public GoGetPipelineGroups(GoApiClient goApiClient) {
		this.goApiClient = goApiClient;
	}

	public List<GoPipelineGroup> get() {
		try {
			HttpRequest request = new HttpGet("/go/api/admin/pipeline_groups");
			request.addHeader("Accept", "application/vnd.go.cd.v1+json");
			HttpResponse response = goApiClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = Streams.readAsString(response.getEntity().getContent());
			return new Gson().fromJson(content, GoPipelineGroupsContainer.class).get_embedded().getGroups();
			} else {
				Log.error("Request got HTTP-" + response.getStatusLine().getStatusCode());
			}
		} catch (IOException e) {
			Log.error("Could not perform request", e);
		}
		return Collections.emptyList();
	}
}
