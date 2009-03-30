package com.jonas.agile.devleadtool.gui.component.table.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.common.ColorUtil;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.SwingUtil;

public abstract class MyTableCellRenderer extends DefaultTableCellRenderer {

   private static Logger log = MyLogger.getLogger(MyTableCellRenderer.class);

   public static void setBackground(final JTable table, final boolean isSelected, final boolean hasFocus, final int row, final int column,
         final Component cell, final MyTableModel model, final Object value, JComponent borderComponent, final MyTable myTable) {
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
         setColor(table, isSelected, hasFocus, row, column, cell, model, value, myTable);
      }
   }

   private static void setColor(final JTable table, final boolean isSelected, final boolean hasFocus, final int row, final int column,
         final Component cell, final MyTableModel model, final Object value, final MyTable myTable) {
      Color color = model.getColor(value, table.convertRowIndexToModel(row), table.convertColumnIndexToModel(column));
      if (color != null) {
         if (isSelected) {
            color = ColorUtil.darkenColor(color, -75);
         }
         cell.setBackground(color);
      } else if (myTable != null && !isSelected && !hasFocus && myTable.isMarked(row)) {
         cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), -35));
      }
   }
}
