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


package com.microfocus.adm.almoctane.ciplugins.gocd.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This generic JSON object can be used when no specific DTO class does exist so far.
 */
public class GenericJsonObject extends HashMap<String,Object> {

	public Object getValue(String... path) {
		return getValueFromMap(this, path);
	}

	public static Object getValueFromMap(Map<String,Object> map, String... path) {
		if (map == null || path == null || path.length < 1) {
			return null;
		}
		Object value = map.get(path[0]);
		if (path.length == 1) {
			return value;
		} else if (value instanceof Map) {
			return getValueFromMap((Map)value, Arrays.copyOfRange(path, 1, path.length));
		}
		return null;
	}
}
