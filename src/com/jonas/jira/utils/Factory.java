package com.jonas.jira.utils;

import com.jonas.common.xml.JonasXpathEvaluator;
import com.jonas.jira.access.JiraSoapClient;
import com.jonas.jira.utils.JiraBuilder;

public interface Factory {

	public JiraSoapClient getJiraSoapClient(String address);

}
