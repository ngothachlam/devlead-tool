package com.jonas.agile.devleadtool.gui.listener;

import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;

public interface JiraToBeReconciledListener {

   public void jiraAdded(String jira, String devEst, String devAct, String release, String remainder, String qaEst, BoardStatusValue status);

}
