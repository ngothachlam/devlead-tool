package com.jonas.agile.devleadtool.gui.listener;

import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;

public interface JiraToBeReconciledListener {

   public void jiraAdded(MyTable table, String jira, String devEst, String devAct, String release, String remainder, String qaEst, BoardStatusValue status);

}
