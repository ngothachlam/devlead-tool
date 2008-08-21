package com.jonas.jira.utils;

import com.jonas.common.xml.JonasXpathEvaluator;
import com.jonas.jira.utils.JiraBuilder;

public interface Factory {

   public JiraBuilder getJiraBuilder();

   public JonasXpathEvaluator getXpathEvaluator(String xpath);

}
