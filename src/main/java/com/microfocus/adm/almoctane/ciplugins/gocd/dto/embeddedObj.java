package com.microfocus.adm.almoctane.ciplugins.gocd.dto;

import java.util.List;

public class embeddedObj {
	public GoPipelineGroups getGroups() {
		return groups;
	}

	public void setGroups(GoPipelineGroups groups) {
		this.groups = groups;
	}

	GoPipelineGroups groups;
}
