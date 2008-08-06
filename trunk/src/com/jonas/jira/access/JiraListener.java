package com.jonas.jira.access;

import java.util.ArrayList;
import java.util.List;

public abstract class JiraListener {
	public enum JiraAccessUpdate {
		LOGGING_IN, GETTING_FIXVERSION, GETTING_JIRA
	}

	private static List<JiraListener> jiraListeners = new ArrayList<JiraListener>();

	public static void notifyListenersOfAccess(JiraAccessUpdate accessUpdate) {
		for (JiraListener jiraListener : jiraListeners) {
			jiraListener.notifyOfAccess(accessUpdate);
		}
	}

	public abstract void notifyOfAccess(JiraAccessUpdate accessUpdate);

	public static void addJiraListener(JiraListener jiraListener) {
		jiraListeners.add(jiraListener);
	}
}
