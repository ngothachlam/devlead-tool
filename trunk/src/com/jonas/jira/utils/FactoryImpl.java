package com.jonas.jira.utils;

import com.jonas.common.xml.JonasXpathEvaluator;

public class FactoryImpl implements Factory {

   @Override
   public JiraBuilder getJiraBuilder() {
      return JiraBuilder.getInstance();
   }

   @Override
   public JonasXpathEvaluator getXpathEvaluator(String xpath) {
      return new JonasXpathEvaluator(xpath);
   }

   
}
