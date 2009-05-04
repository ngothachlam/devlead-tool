/**
 * 
 */
package com.jonas.agile.devleadtool.gui.listener;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import com.jonas.agile.devleadtool.NotJiraException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.HyperLinker;

public class OpenJirasListener implements ActionListener {
   private final MyTable table;
   private Frame parentFrame;

   /**
    * @param parentFrame 
    * @param jiraPanel
    */
   public OpenJirasListener(MyTable tab1e, Frame parentFrame) {
      this.table = tab1e;
      this.parentFrame = parentFrame;
   }

   public void actionPerformed(ActionEvent e) {
      int[] rows = table.getSelectedRows();
      if (rows.length == 0)
         AlertDialog.alertMessage(parentFrame, "No rows selected!");
      StringBuffer sb = new StringBuffer();
      for (int j = 0; j < rows.length; j++) {
         String jira = (String) table.getValueAt(ColumnType.Jira, rows[j]);
         String jira_url = null;
         try {
            jira_url = PlannerHelper.getJiraUrl(jira);
            try {
               HyperLinker.displayURL(jira_url + "/browse/" + jira);
            } catch (URISyntaxException e1) {
               e1.printStackTrace();
            } catch (IOException e1) {
               e1.printStackTrace();
            }
         } catch (NotJiraException e1) {
            if (sb.length() > 0) {
               sb.append(", ");
            }
            sb.append(jira);
         }
      }
      if (sb.length() > 0) {
         sb.append(" are incorrect!");
         AlertDialog.alertMessage(parentFrame, sb.toString());
      }
   }
}