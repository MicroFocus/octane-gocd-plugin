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


package com.microfocus.adm.almoctane.ciplugins.gocd.dto;

import com.google.gson.annotations.SerializedName;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoJobInstance;

import java.util.List;

/**
 * This DTO represents a run of a single stage.
 */
public class GoStageInstance {

	private int id;
	private String name;
	@SerializedName("approved_by")
	private String approvedBy;
	@SerializedName("approval_type")
	private String approvalType;
	@SerializedName("can_run")
	private boolean canRun;
	private String result;
	private String counter;
	private List<GoJobInstance> jobs;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}

	public boolean isCanRun() {
		return canRun;
	}

	public void setCanRun(boolean canRun) {
		this.canRun = canRun;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public List<GoJobInstance> getJobs() {
		return jobs;
	}

	public void setJobs(List<GoJobInstance> jobs) {
		this.jobs = jobs;
	}

	public Long getFirstScheduledDate() {
		if (jobs != null && !jobs.isEmpty()) {
			return jobs.get(0).getScheduledDate();
		} else {
			return null;
		}
	}

	public Long getLastJobTransitionDate() {
		if (jobs != null && !jobs.isEmpty()) {
			return jobs.get(jobs.size() - 1).getLastJobTransitionDate();
		} else {
			return null;
		}
	}

	public Long getDuration() {
		Long scheduledDate = getFirstScheduledDate();
		Long lastTransitionDate = getLastJobTransitionDate();
		if (scheduledDate != null && lastTransitionDate != null) {
			return lastTransitionDate - scheduledDate;
		} else {
			return null;
		}
	}
}
