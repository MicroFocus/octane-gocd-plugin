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
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoBuildCause;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GoStageInstance;

import java.util.List;

/**
 * A pipeline instance documents the run of a pipeline.
 */
public class GoPipelineInstance {

	private String id;
	private String name;
	private String label;
	private int counter;
	@SerializedName("natural_order")
	private int naturalOrder;
	@SerializedName("can_run")
	private boolean canRun;
	private String comment;
	@SerializedName("build_cause")
	private GoBuildCause buildCause;
	private List<GoStageInstance> stages;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getNaturalOrder() {
		return naturalOrder;
	}

	public void setNaturalOrder(int naturalOrder) {
		this.naturalOrder = naturalOrder;
	}

	public boolean isCanRun() {
		return canRun;
	}

	public void setCanRun(boolean canRun) {
		this.canRun = canRun;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public GoBuildCause getBuildCause() {
		return buildCause;
	}

	public void setBuildCause(GoBuildCause buildCause) {
		this.buildCause = buildCause;
	}

	public List<GoStageInstance> getStages() {
		return stages;
	}

	public void setStages(List<GoStageInstance> stages) {
		this.stages = stages;
	}

	public GoStageInstance getLastStage() {
		return stages != null && !stages.isEmpty() ? stages.get(stages.size() - 1) : null;
	}

	public Long getFirstScheduledDate() {
		if (stages != null && !stages.isEmpty()) {
			return stages.get(0).getFirstScheduledDate();
		} else {
			return null;
		}
	}

	public Long getLastJobTransitionDate() {
		if (stages != null && !stages.isEmpty()) {
			return stages.get(stages.size() - 1).getLastJobTransitionDate();
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

	public boolean isPassed() {
		for (GoStageInstance stageInstance : stages) {
			if (!"Passed".equals(stageInstance.getResult())) {
				return false;
			}
		}
		return true;
	}
}
