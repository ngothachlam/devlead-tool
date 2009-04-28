package com.jonas.agile.devleadtool.gui.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBoxMenuItem;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;

public class HighlightIssuesAction extends BasicAbstractGUIAction {

   private final MyTable[] tables;

   public HighlightIssuesAction(String name, Frame parentFrame, MyTable... tables) {
      super(name, "Highlight issues and jiras between tables and panels", parentFrame);
      this.tables = tables;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      JCheckBoxMenuItem cb = (JCheckBoxMenuItem) e.getSource();
      boolean isSel = cb.isSelected();
      
      for (MyTable table : tables) {
         int[] selectedRows = table.getSelectedRows();
         MyTableModel model = table.getMyModel();
         model.setRenderColors(isSel);
         model.fireTableDataChanged();
         restoreSelection(selectedRows, table);
      }
   }

   private void restoreSelection(int[] selectedRows, MyTable table) {
      for (int i : selectedRows) {
         table.setRowSelectionInterval(i, i);
      }
   }

   @Override
   public boolean isCheckBoxAction() {
      return true;
   }
   
   
}
