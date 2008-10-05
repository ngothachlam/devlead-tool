/**
 * 
 */
package com.jonas.agile.devleadtool.component.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.jonas.agile.devleadtool.NotJiraException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.HyperLinker;

public class OpenJirasListener implements ActionListener {
   private final MyTable table;
   private final PlannerHelper helper;

   /**
    * @param jiraPanel
    */
   public OpenJirasListener(MyTable tab1e, PlannerHelper helper) {
      this.table = tab1e;
      this.helper = helper;
   }

   public void actionPerformed(ActionEvent e) {
      int[] rows = table.getSelectedRows();
      StringBuffer sb = new StringBuffer();
      for (int j = 0; j < rows.length; j++) {
         String jira = (String) table.getValueAt(Column.Jira, rows[j]);
         String jira_url = null;
         boolean error = false;
         try {
            jira_url = helper.getJiraUrl(jira);
         } catch (NotJiraException e1) {
            if (sb.length() > 0) {
               sb.append(", ");
            }
            sb.append(jira);
            error = true;
         }
         if (!error) {
            HyperLinker.displayURL(jira_url + "/browse/" + jira);
         }
      }
      if (sb.length() > 0) {
         sb.append(" are incorrect!");
         AlertDialog.alertException(helper, e.toString());
      }
   }
}