package com.jonas.testing.tree.fromScratch.xml;

public interface JiraParseListener {

   public void notifyParsed(JiraDTO jira);

   public void notifyParsingStarted();

   public void notifyParsingFinished();

}
