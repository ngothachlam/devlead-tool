package com.jonas.agile.devleadtool.gui.component.table.renderer;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;

public class StringTableCellRenderer extends MyTableCellRenderer {

   private MyTableModel model = null;

   public StringTableCellRenderer(AbstractTableModel model) {
      if (model instanceof MyTableModel)
         this.model = (MyTableModel) model;
   }

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

      setBackground(table, isSelected, hasFocus, row, column, cell, model, value, (JComponent) cell, (MyTable) table);

      return cell;
   }
}
