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

/**
 * This POJO carries all plugin settings.
 */
public class OctaneGoCDPluginSettings {

	private String serverURL;
	private String clientID;
	private String clientSecret;
	private String goUsername;
	private String goPassword;

	public OctaneGoCDPluginSettings(OctaneGoCDPluginSettings settings){
		if(settings!=null) {
			this.serverURL = settings.serverURL;
			this.clientID = settings.clientID;
			this.clientSecret = settings.clientSecret;
			this.goUsername = settings.goUsername;
			this.goPassword = settings.goPassword;
		}
	}

	public OctaneGoCDPluginSettings(){

	}

	public String getServerURL() {
		return serverURL;
	}

	public OctaneGoCDPluginSettings setServerURL(String serverURL) {
		this.serverURL = serverURL;
		return this;
	}

	public String getClientID() {
		return clientID;
	}

	public OctaneGoCDPluginSettings setClientID(String clientID) {
		this.clientID = clientID;
		return this;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public OctaneGoCDPluginSettings setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
		return this;
	}

	public String getGoUsername() {
		return goUsername;
	}

	public OctaneGoCDPluginSettings setGoUsername(String goUsername) {
		this.goUsername = goUsername;
		return this;
	}

	public String getGoPassword() {
		return goPassword;
	}

	public OctaneGoCDPluginSettings setGoPassword(String goPassword) {
		this.goPassword = goPassword;
		return this;
	}
}
