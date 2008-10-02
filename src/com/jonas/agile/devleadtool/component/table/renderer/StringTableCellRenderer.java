package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Component;
import java.io.Serializable;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.ColorUtil;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;

public class StringTableCellRenderer extends DefaultTableCellRenderer {

   private static final Logger log = MyLogger.getLogger(StringTableCellRenderer.class);

   public StringTableCellRenderer() {
   }

   public Component getTableCellRendererComponent(MyTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      log.debug("String for column: " + column + " with value: " + value + " (class: " + debugClassOfValue(value) + ")");

      if (hasFocus) {
         cell.setBackground(SwingUtil.getTableCellFocusBackground());
      } else if (isSelected) {
         cell.setBackground(table.getSelectionBackground());
      } else {
         cell.setBackground(table.getBackground());
      }
      if (!table.isCellEditable(row, column) && !hasFocus) {
         cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), -55));
      }

      return cell;
   }

   private Serializable debugClassOfValue(Object value) {
      return (value != null ? value.getClass() : "null");
   }

   @SuppressWarnings("cast")
   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      return this.getTableCellRendererComponent((MyTable) table, value, isSelected, hasFocus, row, column);
   }
}
