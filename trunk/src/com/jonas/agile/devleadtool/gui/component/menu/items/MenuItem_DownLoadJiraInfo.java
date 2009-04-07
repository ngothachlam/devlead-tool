/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu.items;

import javax.swing.JMenuItem;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.listener.SyncWithJiraActionListenerListener;
import com.jonas.agile.devleadtool.gui.listener.SyncWithJiraListener;
import com.jonas.jira.JiraIssue;

public class MenuItem_DownLoadJiraInfo extends JMenuItem {
   public MenuItem_DownLoadJiraInfo(final MyTable sourceTable, PlannerHelper helper) {
      super("Dowload Jira Info");
      addActionListener(getActionListener(sourceTable, helper));
   }

   private SyncWithJiraListener getActionListener(final MyTable sourceTable, PlannerHelper helper) {
      SyncWithJiraListener actionListener = new SyncWithJiraListener(sourceTable, helper);
      SyncWithJiraActionListenerListener syncWithJiraActionListenerListener = new SyncWithJiraActionListenerListener() {
         public void jiraAdded(JiraIssue jiraIssue) {
            sourceTable.addJiraAndMarkIfNew(jiraIssue);
         }

         public void jiraSynced(JiraIssue jiraIssue) {
            sourceTable.addJiraAndMarkIfNew(jiraIssue);
         }
      };
      actionListener.addListener(syncWithJiraActionListenerListener);
      return actionListener;
   }
}