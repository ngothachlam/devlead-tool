package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Component;
import javax.swing.JTable;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class StringTableCellRenderer extends MyTableCellRenderer {

   private static final Logger log = MyLogger.getLogger(StringTableCellRenderer.class);

   public StringTableCellRenderer() {
   }

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

      setBackground(table, isSelected, hasFocus, row, column, cell);

      return cell;
   }
}
