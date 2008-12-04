package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.ColorUtil;
import com.jonas.common.SwingUtil;

public abstract class MyTableCellRenderer extends DefaultTableCellRenderer {

   public static void setBackground(JTable table, boolean isSelected, boolean hasFocus, int row, int column, Component cell, MyTableModel model,
         Object value, JComponent borderComponent, MyTable myTable) {
      if (hasFocus) {
         cell.setBackground(SwingUtil.getTableCellFocusBackground());
         if (borderComponent != null) {
            borderComponent.setBorder(SwingUtil.focusCellBorder);
         }
      } else if (isSelected) {
         cell.setBackground(table.getSelectionBackground());
         if (borderComponent != null) {
            borderComponent.setBorder(SwingUtil.defaultCellBorder);
         }
      } else {
         cell.setBackground(table.getBackground());
         if (borderComponent != null) {
            borderComponent.setBorder(SwingUtil.defaultCellBorder);
         }
      }
      if (!table.isCellEditable(row, column) && !hasFocus) {
         cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), -75));
      }

      if (model != null) {
         // FIXME we should only do this when we edit a field!!
         Color color = model.getColor(value, table.convertRowIndexToModel(row), table.convertColumnIndexToModel(column));
         if (color != null) {
            if (isSelected) {
               color = ColorUtil.darkenColor(color, -75);
            }
            cell.setBackground(color);
         } else if (myTable != null && !isSelected && !hasFocus && myTable.isMarked(row)) {
            cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), -25));
         }
      }
   }
}
