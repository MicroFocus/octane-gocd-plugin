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

import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URL;

/**
 * This client is used to execute all requests to the GoCD server.
 *
 * This client is stateful: If a HTTP-200 response has the "Set-Cookie" header set,
 * then this client will reuse this cookie for all subsequent requests. This cookie
 * handling is necessary for requests, which are not considered API requests like
 * <a href="https://api.gocd.org/current/#get-all-artifacts">Get All Artifacts</a>
 * (notice that GetAllArtifacts starts with "/go/files/" instead of "/go/api/")
 *
 * @see <a href="https://api.gocd.org/current">GoCD API Reference</a>
 */
public class GoApiClient {

	private final HttpHost httpHost;
	private final HttpClient httpClient;

	public GoApiClient(URL serverUrl, String username, String password) {
		httpHost = new HttpHost(serverUrl.getHost(), serverUrl.getPort(), serverUrl.getProtocol());

		final CredentialsProvider provider = new BasicCredentialsProvider();
		provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

		httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
	}

	public HttpResponse execute(final HttpRequest request) throws IOException {
		return httpClient.execute(httpHost, request);
	}
}
