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


package com.microfocus.adm.almoctane.ciplugins.gocd.plugin;

import com.google.gson.Gson;
import com.hp.octane.integrations.OctaneConfiguration;
import com.hp.octane.integrations.OctaneSDK;
import com.hp.octane.integrations.exceptions.OctaneConnectivityException;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GenericJsonObject;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoServerInfo;
import com.microfocus.adm.almoctane.ciplugins.gocd.octane.GoPluginServices;
import com.microfocus.adm.almoctane.ciplugins.gocd.plugin.converter.OctaneCIEventBuilder;
import com.microfocus.adm.almoctane.ciplugins.gocd.plugin.converter.StatusInfoWrapper;
import com.microfocus.adm.almoctane.ciplugins.gocd.plugin.settings.OctaneGoCDPluginSettings;
import com.microfocus.adm.almoctane.ciplugins.gocd.plugin.settings.OctaneGoCDPluginSettingsWrapper;
import com.microfocus.adm.almoctane.ciplugins.gocd.plugin.settings.SettingsValidator;
import com.microfocus.adm.almoctane.ciplugins.gocd.plugin.validation.ValidationIssue;
import com.microfocus.adm.almoctane.ciplugins.gocd.service.GoGetServerHealth;
import com.microfocus.adm.almoctane.ciplugins.gocd.util.GoApiUtil;
import com.microfocus.adm.almoctane.ciplugins.gocd.util.MapBuilder;
import com.microfocus.adm.almoctane.ciplugins.gocd.util.Streams;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.DefaultGoApiRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This class is the entry point into the Octane-GoCD-plugin.
 * The communication between plugin and GoCD server happens over a REST API.
 * Whenever the server wants to interact with this plugin it calls {@link #handle(GoPluginApiRequest)}.
 * @see <a href="https://plugin-api.gocd.org/current/notifications/#notification-plugins">Notification Plugins</a>
 */
@Extension
public class OctaneGoCDPlugin implements GoPlugin {

	/** This ID is referred to in the plugin.xml */
	public static String PluginID = "com.microfocus.adm.almoctane.ciplugins.gocd.gocd";
	private static GoApplicationAccessor GoApplicationAccessor;
	private static final Logger Log = Logger.getLoggerFor(OctaneGoCDPlugin.class);
	private static GoPluginIdentifier PluginIdentifier;
	private GoPluginServices goPluginServices = new GoPluginServices();
	private static OctaneGoCDPluginSettings settings;

	@Override
	public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
		{   // retrieve the current plugin settings from the server.
			this.GoApplicationAccessor = goApplicationAccessor;
			DefaultGoApiRequest request = new DefaultGoApiRequest(GoApiUtil.GET_PLUGIN_SETTINGS_REQ, "1.0", pluginIdentifier());
			request.setRequestBody(new Gson().toJson(Collections.singletonMap("plugin-id", PluginID)));
			GoApiResponse response = goApplicationAccessor.submit(request);
			if (response.responseCode() == 200) {
				OctaneGoCDPluginSettings pluginSettings = new Gson().fromJson(response.responseBody(), OctaneGoCDPluginSettings.class);
				settings = new OctaneGoCDPluginSettings(pluginSettings);
			} else {
				settings = new OctaneGoCDPluginSettings();
			}
		}
		{ // retrieve server info.
			DefaultGoApiRequest request = new DefaultGoApiRequest(GoApiUtil.GET_SERVER_INFO_REQ, "1.0", pluginIdentifier());
			GoApiResponse response = goApplicationAccessor.submit(request);
			if (response.responseCode() == 200) {
				OctaneGoCDPlugin.setGoServerUrl(goPluginServices);
			}
		}

		try {
			OctaneSDK.addClient(createOctaneConfiguration(settings), GoPluginServices.class);
		} catch (Exception e){
			Log.error("Plugin initialization: unable to create Octane client, please check the connection with Octane. error:"+ e.getMessage(), e);
		}

		Log.info("OpenText ALM Octane initialized with '" + settings.getServerURL() + "'");
	}

	private OctaneConfiguration createOctaneConfiguration(OctaneGoCDPluginSettings octaneGoCDPluginSettings){

		OctaneConfiguration conf = OctaneConfiguration.createWithUiLocation(goPluginServices.getGoServerID(), octaneGoCDPluginSettings.getServerURL());
		conf.setSecret(octaneGoCDPluginSettings.getClientSecret());
		conf.setClient(octaneGoCDPluginSettings.getClientID());
		return conf;
	}

	@Override
	public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
		if ("stage-status".equals(request.requestName())) { // server is informing about a status change.
			GenericJsonObject statusInfo = new Gson().fromJson(request.requestBody(), GenericJsonObject.class);
			try { // trying to retrieve the OctaneSDK-instance might create an exception if Octane is not yet initialized.
				//new OctaneCIEventBuilder(goPluginServices.createGoApiClient(), OctaneSDK.getInstance()).sendCIEvent(statusInfo);
				new OctaneCIEventBuilder(goPluginServices.createGoApiClient()).sendCIEvent(new StatusInfoWrapper(request.requestBody()));

			} catch (IllegalArgumentException e) {
				Log.info("Octane plugin not yet configured. Skipping sending status info. " + e.getMessage());
			}
			return new DefaultGoPluginApiResponse(200, new Gson().toJson(Collections.singletonMap("status", "success")));
		} else if (GoApiUtil.GET_SETTINGS_VIEW_REQ.equals(request.requestName())) {
			// server is requesting the HTML template for this plugin's configuration.
			try {
				return new DefaultGoPluginApiResponse(200, new Gson().toJson(Collections.singletonMap("template", Streams.readAsString(getClass().getClassLoader().getResourceAsStream("settings.template.html")))));
			} catch (IOException e) {
				Log.error("could not load settings template", e);
			}
		} else if (GoApiUtil.GET_SETTINGS_CONFIGURATION_REQ.equals(request.requestName())) { // server is requesting the possible configuration values.
			return new DefaultGoPluginApiResponse(200, new Gson().toJson(new MapBuilder<>(new HashMap<String,Object>())
				.put("serverURL", new MapBuilder<>(new HashMap<String,Object>())
					.put("display-name", "Server URL")
					.put("display-order", "0")
					.put("required", true)
					.build())
				.put("clientID", new MapBuilder<>(new HashMap<String,Object>())
					.put("display-name", "Client ID")
					.put("display-order", "1")
					.put("required", true)
					.build())
				.put("clientSecret", new MapBuilder<>(new HashMap<String,Object>())
					.put("display-name", "Client Secret")
					.put("display-order", "2")
					.put("required", true)
					.put("secure", true)
					.build())
				.put("goUsername", new MapBuilder<>(new HashMap<String,Object>())
					.put("display-name", "Go API Username")
					.put("display-order", "3")
					.put("required", true)
					.build())
				.put("goPassword", new MapBuilder<>(new HashMap<String,Object>())
					.put("display-name", "Go API Password")
					.put("display-order", "4")
					.put("required", true)
					.put("secure", true)
					.build())
				.build()));
		} else if (GoApiUtil.VALIDATE_SETTINGS_CONFIGURATION_REQ.equals(request.requestName())) { // server is asking for a validation of the given values.
			final OctaneGoCDPluginSettingsWrapper wrapper = new Gson().fromJson(request.requestBody(), OctaneGoCDPluginSettingsWrapper.class);
			final OctaneGoCDPluginSettings newSettings = wrapper.getPluginSettings();
			final List<ValidationIssue> issues = new SettingsValidator().validate(newSettings);

			OctaneConfiguration newConf = null;
			if (issues.isEmpty()) { // test the connection if no validation issues have been found so far.
				final GoPluginServices pluginServices = new GoPluginServices();

				//1. test the connection with Octane
				try {
					newConf = createOctaneConfiguration(newSettings);
					OctaneSDK.testOctaneConfigurationAndFetchAvailableWorkspaces(
						newConf.getUrl(),  newConf.getSharedSpace(), newConf.getClient(), newConf.getSecret(), GoPluginServices.class);
				} catch (OctaneConnectivityException connExc) {
					Log.error("test Octane connection error:",connExc);
					if(OctaneConnectivityException.AUTHENTICATION_FAILURE_KEY.equals(connExc.getErrorMessageKey())){
						issues.add(new ValidationIssue("clientID", connExc.getErrorMessageVal() +" Response: "+ connExc.getErrorCode()));
					} else {
						issues.add(new ValidationIssue("serverURL", connExc.getErrorMessageVal() + " Response: " + connExc.getErrorCode()));
					}
				} catch (Exception e){
					Log.error("test Octane connection error:",e);
					issues.add(new ValidationIssue("serverURL", "Could not connect to Octane. Exception thrown: " + e));
				}
				//2. test the connection towards GoCD.
				HttpResponse httpResponse = new GoGetServerHealth(pluginServices.createGoApiClient(newSettings.getGoUsername(),newSettings.getGoPassword())).getHttpResponse();
				if (httpResponse.getStatusLine().getStatusCode() != 200) {
					Log.error("test GoCD connection error:"+ httpResponse.getStatusLine().getStatusCode() + " " + httpResponse.getStatusLine().getReasonPhrase());
					issues.add(new ValidationIssue("goUsername", "Could not authenticate with GoCD. Response: " + httpResponse.getStatusLine().getStatusCode() + " " + httpResponse.getStatusLine().getReasonPhrase()));
				}

				//3. if there is no errors - update the SDK with the new connection properties
				if (issues.isEmpty()){
					//update the current configuration
					OctaneGoCDPlugin.settings = new OctaneGoCDPluginSettings(newSettings);
					try {
						if (OctaneSDK.getClients().isEmpty()) {
							OctaneSDK.addClient(newConf, GoPluginServices.class);
						} else {
							OctaneConfiguration currentConf = OctaneSDK.getClients().get(0).getConfigurationService().getConfiguration();
							currentConf.setSecret(newConf.getSecret());
							currentConf.setClient(newConf.getClient());
							currentConf.setUrlAndSpace(newConf.getUrl(),newConf.getSharedSpace());
						}
					} catch ( Error e) {
						Log.error("Validate and connect plugin to Octane: Unable to create Octane client, please check the connection with Octane. error:"+ e.getMessage(), e);
					}
				}
			}

			return new DefaultGoPluginApiResponse(200, new Gson().toJson(issues));
		} else if (GoApiUtil.GET_NOTIFICATIONS_INTERESTED_IN_REQ.equals(request.requestName())) {
			return new DefaultGoPluginApiResponse(200, new Gson().toJson(Collections.singletonMap("notifications", Collections.singletonList("stage-status"))));
		}
		throw new UnhandledRequestTypeException(request.requestName());
	}

	@Override
	public GoPluginIdentifier pluginIdentifier() {
		PluginIdentifier = new GoPluginIdentifier("notification", Collections.singletonList("1.0"));
		return PluginIdentifier;
	}

	public static OctaneGoCDPluginSettings getSettings(){
		return settings;
	}

	public static void setGoServerUrl(GoPluginServices goPluginServices){
		try {
			DefaultGoApiRequest request = new DefaultGoApiRequest(GoApiUtil.GET_SERVER_INFO_REQ, "1.0", OctaneGoCDPlugin.PluginIdentifier);
			GoApiResponse response = GoApplicationAccessor.submit(request);
			GoServerInfo serverInfo = new Gson().fromJson(response.responseBody(), GoServerInfo.class);

			String serverURL = serverInfo.getSecureSiteURL();
			if (serverURL == null || serverURL.isEmpty()) { // fall-back if there is no secure URL
				serverURL = serverInfo.getSiteURL();
			}
			if (serverURL == null || serverURL.isEmpty()) { // auto discover the site URL if there is still none.
				try {
					serverURL = "http://" + InetAddress.getLocalHost().getHostName() + ":8153/go";
				} catch (UnknownHostException e) {
					throw new IllegalArgumentException("Could not determine the serverURL. Please configure it in Go's Server Configuration.");
				}
			}


			goPluginServices.setGoServerURL(serverURL);
			Log.info("Go Server URL: " + goPluginServices.getGoServerURL());
			goPluginServices.setGoServerID(serverInfo.getServerID());
			Log.info("Go Server ID: " + goPluginServices.getGoServerID());
		}catch (Exception e){
			Log.info("Error setting gocd server url",e);
		}
	}
}
