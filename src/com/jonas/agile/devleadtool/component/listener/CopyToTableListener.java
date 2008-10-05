/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.logging.MyLogger;

public class CopyToTableListener implements ActionListener {
   private Logger log = MyLogger.getLogger(this.getClass());
   private final MyTable sourceTable;
   private final PlannerHelper helper2;
   private final Map<String, DestinationRetriever> destinationMap;

   public CopyToTableListener(MyTable sourceTable, Map<String, DestinationRetriever> destinationMap, PlannerHelper helper) {
      this.sourceTable = sourceTable;
      this.destinationMap = destinationMap;
      this.helper2 = helper;
   }

   public void actionPerformed(ActionEvent e) {
      Set<String> destinationTitles = destinationMap.keySet();
      Object[] possibilities = destinationTitles.toArray(new String[destinationTitles.size()]);
      Object s = JOptionPane.showInputDialog(null, "Copy the Jira to...", "Copy to...", JOptionPane.PLAIN_MESSAGE, null, possibilities, "Planner");
      if (s == null)
         return;

      DestinationRetriever destinationRetriever = destinationMap.get(s);

      final int[] selectedRows = sourceTable.getSelectedRows();
      final ProgressDialog dialog = new ProgressDialog(helper2.getParentFrame(), "Copying...", "Copying selected messages from Board to Plan...",
            selectedRows.length);
      try {
         for (int i = 0; i < selectedRows.length; i++) {
            String jiraString = (String) sourceTable.getValueAt(Column.Jira, selectedRows[i]);
            addJira(jiraString, destinationRetriever);
            dialog.increseProgress();
         }
      } catch (Exception ex) {
         AlertDialog.alertException(helper2, ex);
         ex.printStackTrace();
      }
      dialog.setCompleteWithDelay(300);
   }

   void addJira(String jiraString, DestinationRetriever destinationRetriever) {
      Map<Column, Object> map = new HashMap<Column, Object>();
      Column[] columns = sourceTable.getColumns();
      for (Column column : columns) {
         MyTableModel model = (MyTableModel) sourceTable.getModel();
         int row = model.getRowWithJira(jiraString, Column.Jira);
         map.put(column, model.getValueAt(column, row));
      }
      destinationRetriever.getDestinationTable().addJira(jiraString, map);
   }

}