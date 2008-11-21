package com.jonas.agile.devleadtool.component.listener;

import com.jonas.agile.devleadtool.component.tree.xml.JiraParseListener;

public interface SprintParseListener extends JiraParseListener {

   public void loggingInToJiraServer();

   public void accessingDataOnJiraServer();

}
