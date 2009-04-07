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
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;

public class MenuItem_Copy extends MyMenuItem {

   /**
    * 
    */
   private final MyTable destinationTable;
   private MyTable sourceTable;
   private ProgressDialog dialog;

   public MenuItem_Copy(MyTablePopupMenu myTablePopupMenu, JFrame frame, String string, MyTable source, MyTable table) {
      super(frame, string);
      this.sourceTable = source;
      this.destinationTable = table;
   }

   public void myActionPerformed(ActionEvent e) {
      final int[] selectedRows = sourceTable.getSelectedRows();
      if (selectedRows.length == 0) {
         AlertDialog.alertMessage(getParentFrame(), "No rows selected!");
      }
      dialog = new ProgressDialog(getParentFrame(), "Copying...", "Copying selected messages from Board to Plan...", selectedRows.length,
            true);
      for (int i = 0; i < selectedRows.length; i++) {
         String jiraString = (String) sourceTable.getValueAt(Column.Jira, selectedRows[i]);
         addJira(jiraString, destinationTable);
         dialog.increseProgress();
      }
   }

   void addJira(String jiraString, MyTable table) {
      Map<Column, Object> map = new HashMap<Column, Object>();
      Column[] columns = sourceTable.getCols();
      for (Column column : columns) {
         MyTableModel model = sourceTable.getMyModel();
         int row = model.getRowWithJira(jiraString);
         map.put(column, model.getValueAt(column, row));
      }
      table.addJiraAndMarkIfNew(jiraString, map);
   }

   @Override
   public void executeOnFinal() {
      if (dialog != null)
         dialog.setCompleteWithDelay(300);
   }
}