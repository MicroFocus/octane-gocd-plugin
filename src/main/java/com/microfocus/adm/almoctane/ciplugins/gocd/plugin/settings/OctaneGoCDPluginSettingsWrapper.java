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

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * This wrapper is used to render the response of <a href="https://plugin-api.gocd.org/current/notifications/#get-plugin-settings">Get Plugin Settings</a>
 */
public class OctaneGoCDPluginSettingsWrapper {

	@SerializedName("plugin-settings")
	private Map<String, Object> properties;

	/**
	 * This method renders the given properties into a settings POJO.
	 * @return OctaneGoCDPluginSettings
	 */
	public OctaneGoCDPluginSettings getPluginSettings() {
		return new OctaneGoCDPluginSettings()
			.setServerURL((String)getValueFor("serverURL"))
			.setClientID((String)getValueFor("clientID"))
			.setClientSecret((String)getValueFor("clientSecret"))
			.setGoUsername((String)getValueFor("goUsername"))
			.setGoPassword((String)getValueFor("goPassword"));
	}

	protected Object getValueFor(final String property) {
		final Object valueMap = properties.get(property);
		if (valueMap != null && valueMap instanceof Map) {
			return ((Map)valueMap).get("value");

		}
		return null;
	}
}
