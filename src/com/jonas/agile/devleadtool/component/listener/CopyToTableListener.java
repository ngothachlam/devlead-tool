/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
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
   private final DestinationRetriever listener;
   private final Column[] columns;

   public CopyToTableListener(MyTable sourceTable, DestinationRetriever listener, PlannerHelper helper, Column... columns) {
      this.sourceTable = sourceTable;
      this.listener = listener;
      this.helper2 = helper;
      this.columns = columns;
   }

   public void actionPerformed(ActionEvent e) {

      final int[] selectedRows = sourceTable.getSelectedRows();
      final ProgressDialog dialog = new ProgressDialog(helper2.getParentFrame(), "Copying...",
            "Copying selected messages from Board to Plan...", selectedRows.length);
      try {
         for (int i = 0; i < selectedRows.length; i++) {
            String jiraString = (String) sourceTable.getValueAt(Column.Jira, selectedRows[i]);
            addJira(jiraString, columns);
            dialog.increseProgress();
         }
      } catch (Exception ex) {
         AlertDialog.alertException(helper2, ex);
         ex.printStackTrace();
      }
      dialog.setCompleteWithDelay(300);
   }

   void addJira(String jiraString, Column... columns) {
      Map<Column, Object> map = new HashMap<Column, Object>();
      for (Column column : columns) {
         MyTableModel model = (MyTableModel)sourceTable.getModel();
         int row = model.getRowWithJira(jiraString, Column.Jira) ;
         map.put(column, model.getValueAt(column, row));
      }
      listener.getDestinationTable().addJira(jiraString, map);
   }

}