/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu.items;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import org.apache.commons.httpclient.HttpException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.jira.access.ClientConstants;
import com.jonas.jira.access.JiraException;
import com.jonas.rollforwardapp.RollforwardParser;
import com.jonas.testHelpers.JiraXMLHelper;

public class MenuItem_Rollforwards extends MyMenuItem {
   private final MyTable sourceTable;
   private final PlannerHelper helper;
   private JiraXMLHelper xmlHelper;
   private RollforwardParser parser;

   public MenuItem_Rollforwards(MyTable sourceTable, PlannerHelper helper) {
      super(helper.getParentFrame(), "Show Rollforwards");
      this.sourceTable = sourceTable;
      this.helper = helper;
      xmlHelper = new JiraXMLHelper(ClientConstants.JIRA_URL_AOLBB);
      parser = new RollforwardParser();
   }

   @Override
   public void myActionPerformed(ActionEvent e) {
      ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Getting information from Jira....", "Starting...", 0, false);
      dialog.setIndeterminate(true);
      SwingWorkerImpl swingWorkerImpl = new SwingWorkerImpl(dialog);
      swingWorkerImpl.execute();

   }

   private final class SwingWorkerImpl extends SwingWorker<Object, Object> {
      private final ProgressDialog dialog;

      private SwingWorkerImpl(ProgressDialog dialog) {
         this.dialog = dialog;
      }

      @Override
      public Object doInBackground() {
         dialog.setVisible(true);
         int[] selectedRows = sourceTable.getSelectedRows();
         dialog.increaseMax("Syncing...", selectedRows.length);
         StringBuffer output = new StringBuffer();
         try {
            xmlHelper.loginToJira();
            dialog.setNote("Logging in to Jira...");
            for (int i : selectedRows) {
               Jira jira = new Jira(((String) sourceTable.getValueAt(ColumnType.Jira, i)));
               try {

                  dialog.increseProgress(jira + " - getting info from server...");
                  String html = xmlHelper.getXML(jira.getUrl());
                  dialog.setNote(jira + " - parsing info from server...");

                  List<String> rollforwards = parser.parseJiraHTMLAndGetSqlRollForwards(html);
                  for (String rollforward : rollforwards) {
                     jira.addRollforward(rollforward);
                  }
                  output.append(jira.getOutput());
               } catch (IOException e2) {
                  e2.printStackTrace();
               }
            }
            AlertDialog.alertMessage(helper.getParentFrame(), "Rollforwards", "The following Rollforwards have been identified", output
                  .toString());
         } catch (HttpException e1) {
            e1.printStackTrace();
         } catch (IOException e1) {
            e1.printStackTrace();
         } catch (JiraException e1) {
            e1.printStackTrace();
         }
         return null;
      }

      @Override
      public void done() {
         dialog.setCompleteWithDelay(300);
      }
   }

   private final class Jira {
      private final String jiraStr;
      private List<String> rollforwards = new ArrayList<String>();

      public Jira(String jiraStr) {
         this.jiraStr = jiraStr;
      }

      public Object getOutput() {
         StringBuffer sb = new StringBuffer();
         sb.append("  ").append(jiraStr).append("\n");
         for (String rollforward : rollforwards) {
            sb.append("     ").append(rollforward).append("\n");
         }
         return sb.toString();
      }

      public void addRollforward(String string) {
         rollforwards.add(string);
      }

      protected String getUrl() {
         StringBuffer url = new StringBuffer();
         url.append("browse/").append(jiraStr).append("?page=com.atlassian.jira.plugin.ext.subversion:subversion-commits-tabpanel");
         return url.toString();
      }
   }
}