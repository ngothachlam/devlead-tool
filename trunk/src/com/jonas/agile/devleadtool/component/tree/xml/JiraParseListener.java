package com.jonas.agile.devleadtool.component.tree.xml;

public interface JiraParseListener {

   public void notifyParsingStarted();
   public void notifyParsed(JiraDTO jira);
   public void notifyParsingFinished();

}
