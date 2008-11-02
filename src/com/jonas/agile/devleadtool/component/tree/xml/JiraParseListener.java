package com.jonas.agile.devleadtool.component.tree.xml;

public interface JiraParseListener {

   public void notifyParsed(JiraDTO jira);

   public void notifyParsingStarted();

   public void notifyParsingFinished();

}
