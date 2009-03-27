package com.jonas.agile.devleadtool.controller.listener;

import com.jonas.agile.devleadtool.component.tree.xml.JiraDTO;

public interface JiraParseListener {

   public void notifyParsingStarted();
   public void notifyParsed(JiraDTO jira);
   public void notifyParsingFinished();

}
