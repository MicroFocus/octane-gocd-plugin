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

package com.microfocus.adm.almoctane.ciplugins.gocd.plugin.converter;

import com.google.gson.Gson;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.GenericJsonObject;
import com.thoughtworks.go.plugin.api.logging.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatusInfoWrapper {
	private GenericJsonObject statusInfo;
	protected static final Logger Log = Logger.getLoggerFor(StatusInfoWrapper.class);

	public StatusInfoWrapper(String requestBody){
		GenericJsonObject statusInfo = new Gson().fromJson(requestBody, GenericJsonObject.class);
		this.statusInfo = statusInfo;
	}

	public String getStageName(){
		return String.valueOf(statusInfo.getValue("pipeline", "stage", "name"));
	}

	public String getPipelineName(){
		return String.valueOf(statusInfo.getValue("pipeline", "name"));
	}

	public OctaneCIEventBuilder.PipelineStageState getStageStatus(){
		return OctaneCIEventBuilder.PipelineStageState.valueOf(String.valueOf(statusInfo.getValue("pipeline", "stage", "state")));
	}

	public String getPipelineCounter(){
		return String.valueOf(statusInfo.getValue("pipeline", "counter"));
	}

	public Date getStageCreateTime(){
		return parseTime(String.valueOf(statusInfo.getValue("pipeline", "stage", "create-time")));
	}

	public Date getStageLastTransitionTime(){
		return parseTime(String.valueOf(statusInfo.getValue("pipeline", "stage", "last-transition-time")));
	}

	/**
	 * This helper method help parsing a given time into a {@link Date}.
	 *
	 * @param time time to parse
	 * @return the time in Date format
	 */
	public static Date parseTime(String time) {
		if (time == null) {
			return null;
		}
		/* Even though the time looks like being a Zulu-time it probably isn't.
		 * Please see whether bug https://github.com/gocd/gocd/issues/3989 is still open.
		 * The StageConverter used to render the time has probably used the default local to
		 * render the time and did just append the letter 'Z'. */
		try { // try to parse the time with an RFC822 timezone
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(time);
		} catch (ParseException e) {
			Log.warn("Could not parse given time with RFC822 timezone '" + time + "'");
		}
		try { // try to parse the time with the pattern the StageConverter was probably using
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(time);
		} catch (ParseException e) {
			Log.error("Could not parse given time with the assumed pattern", e);
		}
		return null; // giving up
	}
}
