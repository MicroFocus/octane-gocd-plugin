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

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensure that {@link GenericJsonObject} works correctly.
 */
public class GenericJsonObjectTest {

	@Test
	public void testParsingStageStartNotification() {
		final String json = "{\n" +
			"  \"pipeline\": {\n" +
			"    \"name\": \"pipeline-name\",\n" +
			"    \"counter\": \"9\",\n" +
			"    \"group\": \"defaultGroup\",\n" +
			"    \"build-cause\": [\n" +
			"      {\n" +
			"        \"material\": {\n" +
			"          \"git-configuration\": {\n" +
			"            \"shallow-clone\": false,\n" +
			"            \"branch\": \"2.x\",\n" +
			"            \"url\": \"https://github.com/organization/repository\"\n" +
			"          },\n" +
			"          \"type\": \"git\"\n" +
			"        },\n" +
			"        \"changed\": false,\n" +
			"        \"modifications\": [\n" +
			"          {\n" +
			"            \"revision\": \"8f60b12439840e5a0a4d464379dd3a48881008b4\",\n" +
			"            \"modified-time\": \"2017-03-23T17:27:58.000Z\",\n" +
			"            \"data\": {}\n" +
			"          }\n" +
			"        ]\n" +
			"      }\n" +
			"    ],\n" +
			"    \"stage\": {\n" +
			"      \"name\": \"stageOne\",\n" +
			"      \"counter\": \"1\",\n" +
			"      \"approval-type\": \"success\",\n" +
			"      \"approved-by\": \"timer\",\n" +
			"      \"state\": \"Building\",\n" +
			"      \"result\": \"Unknown\",\n" +
			"      \"create-time\": \"2017-03-23T20:44:02.119Z\",\n" +
			"      \"jobs\": [\n" +
			"        {\n" +
			"          \"name\": \"job1\",\n" +
			"          \"schedule-time\": \"2017-03-23T20:44:02.119Z\",\n" +
			"          \"state\": \"Scheduled\",\n" +
			"          \"result\": \"Unknown\"\n" +
			"        },\n" +
			"        {\n" +
			"          \"name\": \"job2\",\n" +
			"          \"schedule-time\": \"2017-03-23T20:44:02.119Z\",\n" +
			"          \"state\": \"Scheduled\",\n" +
			"          \"result\": \"Unknown\"\n" +
			"        },\n" +
			"        {\n" +
			"          \"name\": \"job3\",\n" +
			"          \"schedule-time\": \"2017-03-23T20:44:02.119Z\",\n" +
			"          \"state\": \"Scheduled\",\n" +
			"          \"result\": \"Unknown\"\n" +
			"        }\n" +
			"      ]\n" +
			"    }\n" +
			"  }\n" +
			"}";

		GenericJsonObject object = new Gson().fromJson(json, GenericJsonObject.class);
		Assert.assertNotNull("generic JSON object should not be null", object);
		Assert.assertEquals("pipeline name", "pipeline-name", object.getValue("pipeline", "name"));
		Assert.assertEquals("stage state", "Building", object.getValue("pipeline", "stage", "state"));
	}
}
