package com.jonas.testing.tree.fromScratch.tree.xml;

public interface JiraParseListener {

   public void notifyParsed(JiraDTO jira);

   public void notifyParsingStarted();

   public void notifyParsingFinished();

}
