package com.jonas.agile.devleadtool.gui.listener;

import com.jonas.agile.devleadtool.gui.component.table.MyTable;

public interface JiraToBeReconciledListener {

   public void jiraAdded(String jira, MyTable table, String devEst, String devAct, String release, String remainder, String qaEst);

}
