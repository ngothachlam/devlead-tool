package com.jonas.agile.devleadtool.gui.component.table;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import com.jonas.agile.devleadtool.gui.component.table.renderer.MyTableCellRenderer;

public class MyDefaultCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

   public MyDefaultCellRenderer() {
      super();
   }

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      
      MyTableCellRenderer.setBackground(table, isSelected, hasFocus, row, column, this, null, value, this, (MyTable) table);
      
      return this;
   }

}
