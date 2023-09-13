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

import com.google.gson.annotations.SerializedName;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoEnvironmentVariable;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoMaterialConfig;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoParameter;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoStageConfig;

import java.util.List;

/**
 * This DTO represents a single pipeline configuration in Go.
 */
public class GoPipelineConfig {

	private String name;
	@SerializedName("label_template")
	private String labelTemplate;
	@SerializedName("enablePipelineLocking")
	private boolean enablePipelineLocking;
	private String template;
	private List<GoParameter> parameters;
	@SerializedName("environment_variables")
	private List<GoEnvironmentVariable> environmentVariables;
	private List<GoMaterialConfig> materials;
	private List<GoStageConfig> stages;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabelTemplate() {
		return labelTemplate;
	}

	public void setLabelTemplate(String labelTemplate) {
		this.labelTemplate = labelTemplate;
	}

	public boolean isEnablePipelineLocking() {
		return enablePipelineLocking;
	}

	public void setEnablePipelineLocking(boolean enablePipelineLocking) {
		this.enablePipelineLocking = enablePipelineLocking;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public List<GoParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<GoParameter> parameters) {
		this.parameters = parameters;
	}

	public List<GoEnvironmentVariable> getEnvironmentVariables() {
		return environmentVariables;
	}

	public void setEnvironmentVariables(List<GoEnvironmentVariable> environmentVariables) {
		this.environmentVariables = environmentVariables;
	}

	public List<GoMaterialConfig> getMaterials() {
		return materials;
	}

	public void setMaterials(List<GoMaterialConfig> materials) {
		this.materials = materials;
	}

	public List<GoStageConfig> getStages() {
		return stages;
	}

	public void setStages(List<GoStageConfig> stages) {
		this.stages = stages;
	}
}
