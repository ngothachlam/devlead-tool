package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.w3c.dom.views.AbstractView;
import com.jonas.agile.devleadtool.component.table.MyTable;

public abstract class MyTableModel extends DefaultTableModel {

   public MyTableModel(Object[][] objects, String[] tableHeader) {
      super(objects, tableHeader);
   }

   public MyTableModel(Vector contents, Vector headers) {
      super(contents, headers);
   }

   public MyTableModel(Vector columnNames, int i) {
      super(columnNames, i);
   }

   public abstract boolean isRed(Object value, int row, int column);

   public void removeSelectedRows(JTable table) {
      while (table.getSelectedRowCount() > 0) {
         this.removeRow(table.getSelectedRow());
      }
   }

}
