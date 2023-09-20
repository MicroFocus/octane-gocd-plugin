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
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoApprovalConfig;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoEnvironmentVariable;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoJobConfig;

import java.util.List;

/**
 * This DTO represents a stage config.
 */
public class GoStageConfig {

	private String name;
	@SerializedName("fetch_materials")
	private boolean fetchMaterials;
	@SerializedName("clean_working_directory")
	private boolean cleanWorkingDirectory;
	@SerializedName("never_cleanup_artifacts")
	private boolean neverCleanupArtifacts;
	private GoApprovalConfig approval;
	@SerializedName("environment_variables")
	private List<GoEnvironmentVariable> environmentVariables;
	private List<GoJobConfig> jobs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFetchMaterials() {
		return fetchMaterials;
	}

	public void setFetchMaterials(boolean fetchMaterials) {
		this.fetchMaterials = fetchMaterials;
	}

	public boolean isCleanWorkingDirectory() {
		return cleanWorkingDirectory;
	}

	public void setCleanWorkingDirectory(boolean cleanWorkingDirectory) {
		this.cleanWorkingDirectory = cleanWorkingDirectory;
	}

	public boolean isNeverCleanupArtifacts() {
		return neverCleanupArtifacts;
	}

	public void setNeverCleanupArtifacts(boolean neverCleanupArtifacts) {
		this.neverCleanupArtifacts = neverCleanupArtifacts;
	}

	public GoApprovalConfig getApproval() {
		return approval;
	}

	public void setApproval(GoApprovalConfig approval) {
		this.approval = approval;
	}

	public List<GoEnvironmentVariable> getEnvironmentVariables() {
		return environmentVariables;
	}

	public void setEnvironmentVariables(List<GoEnvironmentVariable> environmentVariables) {
		this.environmentVariables = environmentVariables;
	}

	public List<GoJobConfig> getJobs() {
		return jobs;
	}

	public void setJobs(List<GoJobConfig> jobs) {
		this.jobs = jobs;
	}
}
