package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.ColorUtil;
import com.jonas.common.SwingUtil;

public abstract class MyTableCellRenderer extends DefaultTableCellRenderer {

   public static final Border focusBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.yellow);
   // public static Border focusBorder = UIManager.getBorder("Table.focusCellHighlightBorder");
   private static final Border defaultBorder = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");

   public static void setBackground(JTable table, boolean isSelected, boolean hasFocus, int row, int column, Component cell, MyTableModel model, Object value,
         JComponent borderComponent) {
      if (hasFocus) {
         cell.setBackground(SwingUtil.getTableCellFocusBackground());
         if (borderComponent != null) {
            borderComponent.setBorder(focusBorder);
         }
      } else if (isSelected) {
         cell.setBackground(table.getSelectionBackground());
         if (borderComponent != null) {
            borderComponent.setBorder(defaultBorder);
         }
      } else {
         cell.setBackground(table.getBackground());
         if (borderComponent != null) {
            borderComponent.setBorder(defaultBorder);
         }
      }
      if (!table.isCellEditable(row, column) && !hasFocus) {
         cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), -55));
      }

      if (model != null) {
         if (model.isRed(value, table.convertRowIndexToModel(row), table.convertColumnIndexToModel(column))) {
            if (isSelected) {
               if (hasFocus) {
                  cell.setBackground(ColorUtil.darkenColor(new Color(200, 0, 0), +55));
               } else {
                  cell.setBackground(ColorUtil.darkenColor(new Color(200, 0, 0), -55));
               }
            } else {
               cell.setBackground(new Color(200, 0, 0));
            }
         }
      }
   }
}
