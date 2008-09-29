/**
 * 
 */
package com.jonas.agile.devleadtool.component.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.jonas.agile.devleadtool.NotJiraException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.HyperLinker;

final class OpenJirasListener implements ActionListener {
   private final MyTable table;
   private final PlannerHelper helper;

   /**
    * @param jiraPanel
    */
   OpenJirasListener(MyTable tab1e, PlannerHelper helper) {
      this.table = tab1e;
      this.helper = helper;
   }

   public void actionPerformed(ActionEvent e) {
      int[] rows = table.getSelectedRows();
      StringBuffer sb = new StringBuffer();
      for (int j = 0; j < rows.length; j++) {
         String jira = (String) table.getValueAt(ColumnDataType.Jira, rows[j]);
//         String jira = (String) table.getModel().getValueAt(table.convertRowIndexToModel(rows[j]), 0);
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