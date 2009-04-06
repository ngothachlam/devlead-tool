package com.jonas.agile.devleadtool.gui.component.table;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;

public class MyDefaultCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

   private MyTableModel model;

   public MyDefaultCellRenderer(TableModel model) {
      super();
      if (model instanceof MyTableModel)
         this.model = (MyTableModel) model;
   }

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      
//      MyTableCellRenderer.setBackground(table, isSelected, hasFocus, row, column, this, model, value, this, (MyTable) table);
      
      return this;
   }

}
