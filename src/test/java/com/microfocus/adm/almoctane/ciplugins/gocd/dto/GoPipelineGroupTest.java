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

import java.util.List;

/**
 * This test ensure that {@link GoPipelineGroup} can be parsed correctly.
 */
public class GoPipelineGroupTest {

	@Test
	public void testParsingJsonIntoWrapper() {
		final String json = "[\n" +
			"  {\n" +
			"    \"pipelines\": [\n" +
			"      {\n" +
			"        \"stages\": [\n" +
			"          {\n" +
			"            \"name\": \"up42_stage\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"name\": \"up42\",\n" +
			"        \"materials\": [\n" +
			"          {\n" +
			"            \"description\": \"URL: https://github.com/gocd/gocd, Branch: master\",\n" +
			"            \"fingerprint\": \"2d05446cd52a998fe3afd840fc2c46b7c7e421051f0209c7f619c95bedc28b88\",\n" +
			"            \"type\": \"Git\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"label\": \"${COUNT}\"\n" +
			"      }\n" +
			"    ],\n" +
			"    \"name\": \"first\"\n" +
			"  }\n" +
			"]";

		final List<GoPipelineGroup> groups = new Gson().fromJson(json, GoPipelineGroups.class);
		Assert.assertNotNull("groups should not be null", groups);
		Assert.assertEquals("groups should contain one group", 1, groups.size());
		GoPipelineGroup group = groups.get(0);
		Assert.assertEquals("name of pipeline group", "first", group.getName());
		Assert.assertEquals("number of pipelines in group", 1, group.getPipelines().size());
		GoPipeline pipeline = group.getPipelines().get(0);
		Assert.assertEquals("name of pipeline", "up42", pipeline.getName());
		Assert.assertEquals("label of pipeline", "${COUNT}", pipeline.getLabel());
		Assert.assertEquals("number of stages in pipeline", 1, pipeline.getStages().size());
		Assert.assertEquals("number of materials in pipeline", 1, pipeline.getMaterials().size());
		Assert.assertEquals("name of stage", "up42_stage", pipeline.getStages().get(0).getName());
		GoMaterial material = pipeline.getMaterials().get(0);
		Assert.assertEquals("type of material", "Git", material.getType());
		Assert.assertEquals("description of material", "URL: https://github.com/gocd/gocd, Branch: master", material.getDescription());
		Assert.assertEquals("fingerprint of material", "2d05446cd52a998fe3afd840fc2c46b7c7e421051f0209c7f619c95bedc28b88", material.getFingerprint());
	}
}
