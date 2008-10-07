package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Component;
import javax.swing.JTable;

public class StringTableCellRenderer extends MyTableCellRenderer {

   public StringTableCellRenderer() {
   }

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

      setBackground(table, isSelected, hasFocus, row, column, cell);

      return cell;
   }
}
