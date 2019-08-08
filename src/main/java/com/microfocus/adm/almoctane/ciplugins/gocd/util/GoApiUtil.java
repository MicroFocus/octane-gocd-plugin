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

package com.microfocus.adm.almoctane.ciplugins.gocd.util;

import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoVersion;
import com.microfocus.adm.almoctane.ciplugins.gocd.octane.GoPluginServices;
import com.microfocus.adm.almoctane.ciplugins.gocd.service.GoApiClient;
import com.microfocus.adm.almoctane.ciplugins.gocd.service.GoGetAPIVersion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GoApiUtil {

	private static String GO_VERSION;
	private static final String defaultAcceptHeader = "application/vnd.go.cd.v6+json";
	private static final String defaultGoVersion = "18.7.0";

	public static final String PIPELINE_CONFIG_API = "/go/api/admin/pipelines/";
	public static final String GO_VERSION_API = "/go/api/version/";


	public static final String GET_PLUGIN_SETTINGS_REQ = "go.processor.plugin-settings.get";
	public static final String GET_SERVER_INFO_REQ = "go.processor.server-info.get";
	public static final String GET_SETTINGS_VIEW_REQ = "go.plugin-settings.get-view";
	public static final String GET_SETTINGS_CONFIGURATION_REQ = "go.plugin-settings.get-configuration";
	public static final String VALIDATE_SETTINGS_CONFIGURATION_REQ = "go.plugin-settings.validate-configuration";
	public static final String GET_NOTIFICATIONS_INTERESTED_IN_REQ = "notifications-interested-in";

	private static final Logger Log = LogManager.getLogger(GoApiUtil.class);

	public static synchronized void setGoVersion(GoApiClient goApiClient) {
		if(GoApiUtil.GO_VERSION == null) {
			Log.info("Set the version of GO server in order to support the correct API");
			GoVersion goVersion = new GoGetAPIVersion(goApiClient).get();
			if (goVersion != null && goVersion.getVersion() != null && !goVersion.getVersion().isEmpty()) {
				GoApiUtil.GO_VERSION = goVersion.getVersion();
			} else {
				GoApiUtil.GO_VERSION = defaultGoVersion;
			}
			Log.info("GO server version is:"+GoApiUtil.GO_VERSION);
		}
	}

	/**
	 *
	 * @param api
	 * @param apiClient
	 * @return the acceptHeader according to the version. all information was taken from: https://api.gocd.org/current/
	 */
	public static String getAcceptHeader(String api, GoApiClient apiClient){

		String acceptHeader = defaultAcceptHeader;
		setGoVersion(apiClient);
		if(PIPELINE_CONFIG_API.equalsIgnoreCase(api)){
			Log.info("Retrieving Go server version");

			if(versionCompare(GO_VERSION,"18.7.0") >= 0 ) {
				acceptHeader = "application/vnd.go.cd.v6+json";
			} else if (versionCompare(GO_VERSION, "18.7.0") <0 && versionCompare(GO_VERSION,"17.12.0" ) >=0){
				acceptHeader = "application/vnd.go.cd.v5+json";
			} else if ( versionCompare(GO_VERSION,"17.12.0" ) < 0){
				acceptHeader = "application/vnd.go.cd.v4+json";
			}
		}

		Log.info("Accept header for request: "+api +" and Go version:"+ GO_VERSION +" is: "+acceptHeader);
		return acceptHeader;
	}


	public static int versionCompare(String str1, String str2) {
		String[] vals1 = str1.split("\\.");
		String[] vals2 = str2.split("\\.");
		int i = 0;
		// set index to first non-equal ordinal or length of shortest version string
		while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
			i++;
		}
		// compare first non-equal ordinal number
		if (i < vals1.length && i < vals2.length) {
			int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
			return Integer.signum(diff);
		}
		// the strings are equal or one string is a substring of the other
		// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
		return Integer.signum(vals1.length - vals2.length);
	}
}
