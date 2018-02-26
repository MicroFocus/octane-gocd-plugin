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

package com.microfocus.adm.almoctane.ciplugins.gocd.plugin.converter;

import com.hp.octane.integrations.dto.DTOFactory;
import com.hp.octane.integrations.dto.scm.SCMCommit;
import com.hp.octane.integrations.dto.scm.SCMData;
import com.hp.octane.integrations.dto.scm.SCMRepository;
import com.hp.octane.integrations.dto.scm.SCMType;
import com.microfocus.adm.almoctane.ciplugins.gocd.dto.*;

import java.util.ArrayList;

/**
 * This builder helps retrieving the {@link SCMData} from a {@link GoPipelineInstance}.
 */
public class OctaneSCMDataBuilder {

	public SCMData retrieveFrom(GoPipelineInstance pipelineInstance) {
		SCMData scmData = DTOFactory.getInstance().newDTO(SCMData.class);
		GoBuildCause buildCause = pipelineInstance.getBuildCause();
		if (buildCause != null && buildCause.getMaterialRevision() != null) {
			// search for the repository.
			for (GoMaterialRevision materialRevision : buildCause.getMaterialRevision()) {
				if (materialRevision.isChanged() && materialRevision.getMaterial() != null && !"Pipeline".equals(materialRevision.getMaterial().getType())) {
					if (materialRevision.getMaterial() != null) {
						scmData.setRepository(retrieveFrom(materialRevision.getMaterial()));
					}
					if (materialRevision.getModifications() != null) {
						scmData.setCommits(new ArrayList<SCMCommit>());
						for (GoModification modification : materialRevision.getModifications()) {
							scmData.getCommits().add(DTOFactory.getInstance().newDTO(SCMCommit.class)
								.setUser(modification.getUserName())
								.setUserEmail(modification.getEmailAddress())
								.setComment(modification.getComment())
								.setRevId(modification.getRevision())
								.setTime(modification.getModifiedTime()));
						}
					}
				}
			}
		}
		return scmData;
	}

	public SCMRepository retrieveFrom(GoMaterial material) {
		SCMRepository repository = DTOFactory.getInstance().newDTO(SCMRepository.class)
			.setType(SCMType.fromValue(material.getType().toLowerCase()));

		for (String fragment : material.getDescription().split(",")) {
			fragment = fragment.trim();
			if (fragment.startsWith("URL:")) {
				repository.setUrl(fragment.substring(4).trim());
			} else if (fragment.startsWith("Branch:")) {
				repository.setBranch(fragment.substring(7).trim());
			}
		}
		return repository;
	}
}
