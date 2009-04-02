/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu;

import javax.swing.JMenuItem;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.listener.SyncWithJiraActionListenerListener;
import com.jonas.agile.devleadtool.gui.listener.SyncWithJiraListener;
import com.jonas.jira.JiraIssue;

class MenuItem_Sync extends JMenuItem {
   public MenuItem_Sync(final MyTable sourceTable, PlannerHelper helper) {
      super("Dowload Jira Info");
      addActionListener(getActionListener(sourceTable, helper));
   }

   private SyncWithJiraListener getActionListener(final MyTable sourceTable, PlannerHelper helper) {
      SyncWithJiraListener actionListener = new SyncWithJiraListener(sourceTable, helper);
      SyncWithJiraActionListenerListener syncWithJiraActionListenerListener = new SyncWithJiraActionListenerListener() {
         public void jiraAdded(JiraIssue jiraIssue) {
            sourceTable.addJira(jiraIssue);
         }

         public void jiraSynced(JiraIssue jiraIssue, int tableRowSynced) {
            sourceTable.syncJira(jiraIssue, tableRowSynced);
         }
      };
      actionListener.addListener(syncWithJiraActionListenerListener);
      return actionListener;
   }
}