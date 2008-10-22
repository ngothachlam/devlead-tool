/**
 * 
 */
package com.jonas.guibuilding.basicdnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;

final class TableTransferHandler extends TransferHandler {

   public static DataFlavor vectorFlavor;

   static {
      try {
         vectorFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + Vector.class.getName() + "\"");
      } catch (ClassNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   private DefaultTableModel tableModel;
   private MyTable table;

   public TableTransferHandler(MyTable list) {
      super();
      table = list;
      tableModel = (DefaultTableModel) list.getModel();
   }

   public int getSourceActions(JComponent c) {
      return MOVE;
   }

   protected Transferable createTransferable(JComponent c) {
      MyTable list = (MyTable) c;
      if (list.getSelectedRows().length > 1)
         throw new RuntimeException("Can currently not handle moving more than one selection!!");
      int[] values = list.getSelectedRows();
      StringBuffer buff = new StringBuffer();

      Vector<Column> columns = new Vector<Column>();
      for (int i = 0; i < list.getColumnCount(); i++) {
         columns.add(list.getColumnEnum(i));
      }
      Vector<Vector<Object>> data = new Vector<Vector<Object>>();
      for (int i = 0; i < values.length; i++) {
         Vector<Object> row = new Vector<Object>();
         for (int j = 0; j < list.getColumnCount(); j++) {
            Object val = list.getValueAt(values[i], j);
            row.add(val);
            // FIXME remove:
            continue;
         }
         data.add(row);
      }
      return new TableTransferable(columns, data);
   }

   protected void exportDone(JComponent source, Transferable data, int action) {
      super.exportDone(source, data, action);
      if (action == MOVE) {
         JTable table = (JTable) source;
         if (table.getModel() instanceof DefaultTableModel) {
            int selectedIndex = table.getSelectedRow();
            BasicDnD.debugDropLocation("Deleting source at " + selectedIndex);
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(selectedIndex);
         }
      }
   }

   // On the destination object...
   public boolean canImport(TransferSupport info) {
      // we only import Strings
      if (!info.isDataFlavorSupported(vectorFlavor)) {
         return false;
      }
      JTable.DropLocation dropLocation = (JTable.DropLocation) info.getDropLocation();
      if (dropLocation.isInsertRow() && dropLocation.getRow() != -1) {
         return true;
      }
      return false;
   }

   public boolean importData(TransferSupport info) {
      if (!info.isDrop()) {
         BasicDnD.debugDropLocation("List doesn't accept drop");
         return false;
      }

      JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
      int index = dl.getRow();
      boolean insert = dl.isInsertRow();

      // Get the string that is being dropped.
      Transferable t = info.getTransferable();
      TableDTO transferableDTO;
      try {
         transferableDTO = (TableDTO) t.getTransferData(vectorFlavor);
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }

      String dropValue = "\"" + transferableDTO.getData().get(0) + "\" dropped ";
      if (insert) {
         for (Vector<Object> rowData : transferableDTO.getData()) {
            if (index >= table.getRowCount()) {
               BasicDnD.debugDropLocation(dropValue + "at end of table");
               table.addRow(rowData);
            } else {
               if (index == 0) {
                  BasicDnD.debugDropLocation(dropValue + "at beginning of table");
                  table.insertRow(index, rowData);
               } else {
                  Object value1 = table.getValueAt(index - 1, 0);
                  Object value2 = table.getValueAt(index, 0);
                  BasicDnD.debugDropLocation(dropValue + "between \"" + value1 + "\" and \"" + value2 + "\"");
                  table.insertRow(index, rowData);
               }
            }
         }
      } else {
         Object value = table.getValueAt(index, 0);
         BasicDnD.debugDropLocation(dropValue + "on top of " + "\"" + value + "\"");
         throw new RuntimeException("Nothing of insert is currently supported in this TransferHandler");
      }
      return true;
   }
}