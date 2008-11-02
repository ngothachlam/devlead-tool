package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import com.jonas.common.ColorUtil;
import com.jonas.common.SwingUtil;

public abstract class MyTableCellRenderer extends DefaultTableCellRenderer {

   protected void setBackground(JTable table, boolean isSelected, boolean hasFocus, int row, int column, Component cell) {
      if (hasFocus) {
         cell.setBackground(SwingUtil.getTableCellFocusBackground());
      } else if (isSelected) {
         cell.setBackground(table.getSelectionBackground());
      } else {
         cell.setBackground(table.getBackground());
      }
      if (!table.isCellEditable(row, column) && !hasFocus) {
         // FIXME make this red for jiratable as well!
         // if (model.isRed(value, table.convertRowIndexToModel(row), table.convertColumnIndexToModel(column))) {
         // if (isSelected) {
         // if (hasFocus) {
         // checkbox.setBackground(ColorUtil.darkenColor(new Color(200, 0, 0), +25));
         // } else {
         // checkbox.setBackground(ColorUtil.darkenColor(new Color(200, 0, 0), -55));
         // }
         // } else {
         // checkbox.setBackground(new Color(200, 0, 0));
         // }
         // }
         cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), -55));
      }
   }

}
