/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu.items;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.gui.component.menu.MyTablePopupMenu;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;

public class MenuItem_Copy extends MyMenuItem {

   /**
    * 
    */
   private final MyTable destinationTable;
   private MyTable sourceTable;
   private MyTableModel sourceModel;
   private ProgressDialog dialog;

   public MenuItem_Copy(MyTablePopupMenu myTablePopupMenu, JFrame frame, String string, MyTable source, MyTable table) {
      super(frame, string);
      this.sourceTable = source;
      this.sourceModel = source.getMyModel();
      this.destinationTable = table;
   }

   @Override
   public void myActionPerformed(ActionEvent e) {
      final int[] selectedRows = sourceTable.getSelectedRows();
      if (selectedRows.length == 0) {
         AlertDialog.alertMessage(getParentFrame(), "No rows selected!");
      }
      dialog = new ProgressDialog(getParentFrame(), "Copying...", "Copying selected messages from Board to Plan...", selectedRows.length,
            true);
      for (int i = 0; i < selectedRows.length; i++) {
         int modelRow = sourceTable.convertRowIndexToModel(selectedRows[i]);
         String jiraString = (String) sourceModel.getValueAt(ColumnType.Jira, modelRow);
         addJira(jiraString);
         dialog.increseProgress();
      }
   }

   void addJira(String jiraString) {
      Map<ColumnType, Object> map = new HashMap<ColumnType, Object>();
      ColumnType[] columns = sourceModel.getCols();
      for (ColumnType column : columns) {
         int row = sourceModel.getRowWithJira(jiraString);
         map.put(column, sourceModel.getValueAt(column, row));
      }
      destinationTable.addJiraAndMarkIfNew(jiraString, map);
   }

   @Override
   public void executeOnFinal() {
      if (dialog != null)
         dialog.setCompleteWithDelay(300);
   }
}