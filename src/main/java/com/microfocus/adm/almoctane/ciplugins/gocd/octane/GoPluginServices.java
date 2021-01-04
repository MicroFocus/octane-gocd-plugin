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

package com.microfocus.adm.almoctane.ciplugins.gocd.octane;

import com.hp.octane.integrations.CIPluginServices;
import com.hp.octane.integrations.dto.DTOFactory;
import com.hp.octane.integrations.dto.general.CIJobsList;
import com.hp.octane.integrations.dto.general.CIPluginInfo;
import com.hp.octane.integrations.dto.general.CIServerInfo;
import com.hp.octane.integrations.dto.general.CIServerTypes;
import com.hp.octane.integrations.dto.parameters.CIParameters;
import com.hp.octane.integrations.dto.pipelines.PipelineNode;
import com.hp.octane.integrations.dto.pipelines.PipelinePhase;
import com.hp.octane.integrations.dto.tests.BuildContext;
import com.hp.octane.integrations.dto.tests.TestRun;
import com.hp.octane.integrations.dto.tests.TestsResult;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.*;
import com.microfocus.adm.almoctane.ciplugins.gocd.plugin.OctaneGoCDPlugin;
import com.microfocus.adm.almoctane.ciplugins.gocd.plugin.converter.OctaneTestResultsBuilder;
import com.microfocus.adm.almoctane.ciplugins.gocd.service.*;
import com.microfocus.adm.almoctane.ciplugins.gocd.util.converter.Converter;
import com.microfocus.adm.almoctane.ciplugins.gocd.util.converter.ListConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.net.*;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * This class is the entry point into the Octane-PluginService.
 * As described in <a href="https://github.com/MicroFocus/octane-ci-java-sdk/blob/master/README.md">ReadMe</a>
 * it derives from {@link CIPluginServices}.
 */
public class GoPluginServices extends CIPluginServices {

	private static final Logger Log = LogManager.getLogger(GoPluginServices.class);

	private static String goServerID;
	private static String goServerURL;

	public String getGoServerID() {
		return goServerID;
	}

	public void setGoServerID(String goServerID) {
		this.goServerID = goServerID;
	}

	public String getGoServerURL() {
		return goServerURL;
	}

	public void setGoServerURL(String goServerURL) {
		this.goServerURL = goServerURL;
	}

	public GoApiClient createGoApiClient() {
		return createGoApiClient( OctaneGoCDPlugin.getSettings().getGoUsername(),OctaneGoCDPlugin.getSettings().getGoPassword());
	}

	public GoApiClient createGoApiClient(String goAPIUserName, String goAPIPassword) {
		try {
			return new GoApiClient(new URL(goServerURL), goAPIUserName, goAPIPassword);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Could not parse the given serverURL '" + goServerURL + "'", e);
		}
	}

	@Override
	public CIPluginInfo getPluginInfo() {
		return DTOFactory.getInstance().newDTO(CIPluginInfo.class).setVersion("1.0.6");
	}

	@Override
	public CIServerInfo getServerInfo() {

		OctaneGoCDPlugin.setGoServerUrl(this);

		return DTOFactory.getInstance().newDTO(CIServerInfo.class)
			.setUrl(goServerURL)
			.setType(CIServerTypes.GOCD.value())
			.setSendingTime(System.currentTimeMillis())
			.setInstanceId(goServerID);
	}


	@Override
	public CIJobsList getJobsList(boolean includeParameters, Long workspaceId) {
		Log.debug("Retrieving all current pipelines with includeParameters=" + includeParameters);
		List<PipelineNode> pipelineNodes = new ArrayList<>();
		GoApiClient goApiClient = createGoApiClient();
		List<String> excludedPipelines = new ArrayList<>();
		for (GoPipelineGroup group : new GoGetPipelineGroups(goApiClient).get()) {
			for (GoPipeline pipeline : group.getPipelines()) {
				GoPipelineConfig pipelineConfig = new GoGetPipelineConfig(goApiClient).get(pipeline.getName());
				if(pipelineConfig != null) {
					pipelineNodes.add(DTOFactory.getInstance().newDTO(PipelineNode.class)
						.setJobCiId(pipeline.getName())
						.setName(pipeline.getName()));
				} else {
					excludedPipelines.add(pipeline.getName());
				}
			}
		}
		if(excludedPipelines.size() > 0) {
			Log.warn(String.format("Failed to fetch configuration for pipelines (%s). The GoCD user '%s' must have pipeline admin permissions",
				String.join(",", excludedPipelines), OctaneGoCDPlugin.getSettings().getGoUsername()));
		}
		return DTOFactory.getInstance().newDTO(CIJobsList.class)
			.setJobs(pipelineNodes.toArray(new PipelineNode[pipelineNodes.size()]));
	}


	private PipelineNode createPipelineStructure(GoPipelineConfig config){

		return DTOFactory.getInstance().newDTO(PipelineNode.class)
			.setJobCiId(config.getName())
			.setName(config.getName())
			.setPhasesInternal(ListConverter.convert(config.getStages(),  new Converter<GoStageConfig, PipelinePhase>(){
				@Override
				public PipelinePhase convert(GoStageConfig stage) {
					return DTOFactory.getInstance().newDTO(PipelinePhase.class)
						.setName("stages_"+ stage.getName())
						.setBlocking(true)
						.setJobs(Collections.singletonList(DTOFactory.getInstance().newDTO(PipelineNode.class)
							.setJobCiId(stage.getName())
							.setName(stage.getName())
							.setPhasesInternal(Collections.singletonList(DTOFactory.getInstance().newDTO(PipelinePhase.class)
								.setName("jobs")
								.setBlocking(true)
								.setJobs(ListConverter.convert(stage.getJobs(), new Converter<GoJobConfig, PipelineNode>() {
									@Override
									public PipelineNode convert(GoJobConfig jobConfig) {
										return DTOFactory.getInstance().newDTO(PipelineNode.class)
											.setJobCiId(jobConfig.getName())
											.setName(jobConfig.getName());
									}
								}))
							))));
				}}));
	}

	@Override
	public PipelineNode getPipeline(final String rootCIJobId) {
		Log.debug("Retrieving pipeline configuration for '" + rootCIJobId + "'");
		if (rootCIJobId == null || rootCIJobId.isEmpty()) {
			throw new IllegalArgumentException("no pipeline identifier was given");
		}
		final GoPipelineConfig config = new GoGetPipelineConfig(createGoApiClient()).get(rootCIJobId);
		if (config == null) {
			return null;
		}

		return createPipelineStructure(config);

//		return DTOFactory.getInstance().newDTO(PipelineNode.class)
//			.setJobCiId(config.getName())
//			.setName(config.getName())
//			//.setPhasesInternal(Collections.singletonList(DTOFactory.getInstance().newDTO(PipelinePhase.class)
//			.setPhasesInternal(Collections.singletonList(DTOFactory.getInstance().newDTO(PipelinePhase.class)
//				.setName("stages")
//				.setBlocking(true)
//				.setJobs(ListConverter.convert(config.getStages(), new Converter<GoStageConfig, PipelineNode>() {
//					@Override
//					public PipelineNode convert(GoStageConfig stage) {
//						return DTOFactory.getInstance().newDTO(PipelineNode.class)
//							.setJobCiId(stage.getName())
//							.setName(stage.getName())
//							.setPhasesInternal(Collections.singletonList(DTOFactory.getInstance().newDTO(PipelinePhase.class)
//								.setName("jobs")
//								.setBlocking(true)
//								.setJobs(ListConverter.convert(stage.getJobs(), new Converter<GoJobConfig, PipelineNode>() {
//									@Override
//									public PipelineNode convert(GoJobConfig jobConfig) {
//										return DTOFactory.getInstance().newDTO(PipelineNode.class)
//											.setJobCiId(jobConfig.getName())
//											.setName(jobConfig.getName());
//									}
//								}))));
//
//					}
//				}))));
	}

	@Override
	public InputStream getTestsResult(final String jobId, final String buildNumber) {
		Log.debug("Retrieving test results for '" + jobId + "' and buildNumber '" + buildNumber + "'");
		final TestsResult result = DTOFactory.getInstance().newDTO(TestsResult.class)
			.setBuildContext(DTOFactory.getInstance().newDTO(BuildContext.class).setServerId(goServerID))
			.setTestRuns(new ArrayList<TestRun>());

		/** Use the same client for all requests in this method. Notice that {@link GoGetAllArtifacts}
		 * needs an authentication cookie which is received by the client when performing an API request. */
		final GoApiClient goApiClient = createGoApiClient();
		GoPipelineInstance pipelineInstance = new GoGetPipelineInstance(goApiClient).get(jobId, Integer.valueOf(buildNumber));
		if (pipelineInstance != null && pipelineInstance.getStages() != null) {
			result.getBuildContext()
				.setJobId(pipelineInstance.getName())
				.setJobName(pipelineInstance.getName())
				.setBuildId(String.valueOf(pipelineInstance.getCounter()))
				.setBuildName(pipelineInstance.getLabel());

			for (GoStageInstance stageInstance : pipelineInstance.getStages()) {
				if (stageInstance.getJobs() != null) {
					for (GoJobInstance jobInstance : stageInstance.getJobs()) {
						List<GoArtifact> artifacts = new GoGetAllArtifacts(goApiClient).get(pipelineInstance.getName(), pipelineInstance.getCounter(), stageInstance.getName(), Integer.valueOf(stageInstance.getCounter()), jobInstance.getName());
						result.getTestRuns().addAll(new OctaneTestResultsBuilder(goApiClient).convert(artifacts));
					}
				}
			}
		}

		if(result.getTestRuns() == null || result.getTestRuns().isEmpty()){
			Log.info("There are no test results for '" + jobId + "' and buildNumber '" + buildNumber + "'");
			return null;
		}
		Log.info("Sending "+ result.getTestRuns().size() +" test results for '" + jobId + "', buildNumber '" + buildNumber + "'");
		InputStream output =  DTOFactory.getInstance().dtoToXmlStream(result);
		return output;
	}

	@Override
	public void runPipeline(String pipelineName, CIParameters ciParameters) {
		Log.debug("Triggering pipeline '" + pipelineName + "' to run");
		GoApiClient goApiClient = createGoApiClient();
		GoPipelineConfig pipelineConfig = new GoGetPipelineConfig(goApiClient).get(pipelineName);
		String username = OctaneGoCDPlugin.getSettings().getGoUsername();
		if(pipelineConfig == null) throw new AccessControlException("Failed to fetch configuration for pipeline '" + pipelineName + "'. The GoCD user " + username + " must have pipeline admin permissions");
		boolean res = new GoSchedulePipeline(goApiClient).trigger(pipelineName);
		if(!res) throw new RuntimeException("Failed to run pipeline '" + pipelineName + "'. See GoCD server logs for more details.");
	}
}
