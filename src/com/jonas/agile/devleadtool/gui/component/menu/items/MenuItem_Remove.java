/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu.items;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import com.jonas.agile.devleadtool.gui.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;

public class MenuItem_Remove extends MyMenuItem {
   private MyTable sourceTable;
   private ProgressDialog dialog;

   public MenuItem_Remove(String string, MyTable sourceTable, Frame parent) {
      super(parent, string);
      this.sourceTable = sourceTable;
   }

   @Override
   public void myActionPerformed(ActionEvent e) {
      int[] selectedRows = sourceTable.getSelectedRows();

      StringBuffer sb = new StringBuffer("Do you want to remove the following jiras from ");
      sb.append(sourceTable.getTitle()).append("?");

      for (int aSelectedRow : selectedRows) {
         sb.append("\n").append(sourceTable.getValueAt(ColumnType.Jira, aSelectedRow));
      }
      int result = JOptionPane.showConfirmDialog(getParentFrame(), sb.toString(), "Remove jiras?", JOptionPane.YES_NO_OPTION);
      if (result == JOptionPane.YES_OPTION) {
         dialog = new ProgressDialog(getParentFrame(), "Removing...", "Removing selected Jiras...", 0, true);
         if (sourceTable.getSelectedRowCount() <= 0) {
            dialog.setNote("Nothing selected!!");
            dialog.setCompleteWithDelay(2000);
         }

         sourceTable.removeSelectedRows();
      }
   }

   @Override
   public void executeOnFinal() {
      if (dialog != null)
         dialog.setCompleteWithDelay(300);
   }
}