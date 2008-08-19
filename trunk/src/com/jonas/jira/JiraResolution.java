package com.jonas.jira;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.jira.rpc.soap.beans.RemoteResolution;

public class JiraResolution {

	private final static Map<String, JiraResolution> jiraResolutions = new HashMap<String, JiraResolution>();
	private final String id;
	private final String name;
	private final String description;

	private JiraResolution(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public static JiraResolution getResolution(String id) {
		return jiraResolutions.get(id);
	}
	
	public static int getAmount() {
		return jiraResolutions.size();
	}

	public static void setResolutions(RemoteResolution[] remoteResolutions) {
		for (RemoteResolution remoteResolution : remoteResolutions) {
			jiraResolutions.put(remoteResolution.getId(), new JiraResolution(remoteResolution.getId(), remoteResolution.getName(),
					remoteResolution.getDescription()));
		}
	}

	public String getName() {
		return name;
	}
}
