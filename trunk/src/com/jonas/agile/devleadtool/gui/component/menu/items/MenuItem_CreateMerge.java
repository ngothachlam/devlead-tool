/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu.items;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.jonas.agile.devleadtool.NotJiraException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.HyperLinker;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraClient;

public class MenuItem_CreateMerge extends MyMenuItem {

   private final MyTable source;

   public MenuItem_CreateMerge(String string, final MyTable source, JFrame parent) {
      super(parent, string);
      this.source = source;

   }

   @Override
   public void myActionPerformed(ActionEvent e) throws RemotePermissionException, RemoteAuthenticationException, RemoteException,
         java.rmi.RemoteException, NotJiraException {
      int[] selectedRows = source.getSelectedRows();
      if (selectedRows.length == 0) {
         JOptionPane.showMessageDialog(getParentFrame(), "No Jiras Selected!");
         return;
      }

      // FIXME only allow one merge at a time or get the project, etc for each one!!

      List<String> originalJiras = new ArrayList<String>();
      StringBuffer sb = new StringBuffer("Do you want to create merge Jiras for the following?\n");
      for (int aSelectedRow : selectedRows) {
         String aJiraToBeMerged = (String) source.getValueAt(Column.Jira, aSelectedRow);
         sb.append(aJiraToBeMerged).append(" ");
         originalJiras.add(aJiraToBeMerged);
      }

      JiraProject project = JiraProject.getProjectByJira(originalJiras.get(0));
      JiraClient client = project.getJiraClient();

      JiraVersion[] fixVersions = client.getFixVersionsFromProject(project, false);
      JiraVersion fixVersionToCreateMergesAgainst = (JiraVersion) JOptionPane.showInputDialog(getParentFrame(), sb.toString(),
            "What Fix Version for the merges?", JOptionPane.QUESTION_MESSAGE, null, fixVersions, null);
      if (fixVersionToCreateMergesAgainst != null) {
         sb = new StringBuffer("Created Merge Jiras:\n");
         List<String> jirasMerged = new ArrayList<String>();
         for (String originalJira : originalJiras) {
            project = JiraProject.getProjectByJira(originalJira);
            try {
               JiraClient jiraClient = project.getJiraClient();
               String fixVersionName = fixVersionToCreateMergesAgainst.getName();
               String mergeJiraCreated = jiraClient.createMergeJira(originalJira, fixVersionName);
               sb.append(mergeJiraCreated).append(" ");
               jirasMerged.add(mergeJiraCreated);

               setTableValuesForOriginal(originalJira, mergeJiraCreated);
               setTableValuesForMerge(originalJira, fixVersionName, mergeJiraCreated);

            } catch (Throwable e1) {
               AlertDialog.alertException(getParentFrame(), e1);
            }
         }
         sb.append("\nOpen them in browser?\n\n(You need to link them to the original Jira!!");
         int shallOpenInBrowser = JOptionPane.showConfirmDialog(getParentFrame(), sb.toString());
         if (shallOpenInBrowser == JOptionPane.YES_OPTION) {
            try {
               for (String aJiraMerged : jirasMerged) {
                  String jira_url = PlannerHelper.getJiraUrl(aJiraMerged);
                  HyperLinker.displayURL(jira_url + "/browse/" + aJiraMerged);
               }
            } catch (URISyntaxException e1) {
               e1.printStackTrace();
            } catch (IOException e1) {
               e1.printStackTrace();
            }
         }
      }
   }

   private void setTableValuesForOriginal(String originalJira, String mergeJiraCreated) {
      source.setValueAt("Has: " + mergeJiraCreated, originalJira, Column.Merge);
   }

   private void setTableValuesForMerge(String originalJira, String fixVersionName, String mergeJiraCreated) {
      source.addJiraAndMarkIfNew(new JiraIssue(mergeJiraCreated, fixVersionName));
      source.setValueAt("For: " + originalJira, mergeJiraCreated, Column.Merge);
      source.setValueAt("merge", mergeJiraCreated, Column.DEst);
   }
}