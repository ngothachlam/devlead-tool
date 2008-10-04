/**
 * 
 */
package com.jonas.guibuilding.basicdnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.DropLocation;
import javax.swing.table.DefaultTableModel;

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

   public TableTransferHandler(JTable list) {
      super();
      tableModel = (DefaultTableModel) list.getModel();
      // if (vectorFlavor == null) {
      // try {
      // vectorFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + Vector.class.getName() + "\"");
      // } catch (ClassNotFoundException e) {
      // e.printStackTrace();
      // }
      // }
   }

   // On the source object:
   public int getSourceActions(JComponent c) {
      return MOVE;
   }

   protected Transferable createTransferable(JComponent c) {
      {
         // ListElement values = (ListElement) list.getSelectedValues()[0];
         // return values;
      }
      JTable list = (JTable) c;
      if (list.getSelectedRows().length > 1)
         throw new RuntimeException("Can currently not handle moving more than one selection!!");
      int[] values = list.getSelectedRows();
      StringBuffer buff = new StringBuffer();

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
      return new TableTransferable(data);
   }

   protected void exportDone(JComponent source, Transferable data, int action) {
      super.exportDone(source, data, action);
      if (action == MOVE) {
         JTable table = (JTable) source;
         if (table.getModel() instanceof DefaultTableModel) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int selectedIndex = table.getSelectedRow();
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
      System.out.println("1");
      if (!info.isDrop()) {
         BasicDnD.displayDropLocation("List doesn't accept !isDrop");
         return false;
      }
      // Check for String flavor
      // if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
      // BasicDnD.displayDropLocation("List doesn't accept a drop of this type.");
      // return false;
      // }
      JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
      int index = dl.getRow();
      boolean insert = dl.isInsertRow();

      // Get the string that is being dropped.
      Transferable t = info.getTransferable();
      Vector<Vector<Object>> data;
      try {
         data = (Vector<Vector<Object>>) t.getTransferData(vectorFlavor);
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }

      // Display a dialog with the drop information.
      String dropValue = "\"" + data + "\" dropped ";
      if (dl.isInsertRow()) {
         if (index == 0) {
            BasicDnD.displayDropLocation(dropValue + "at beginning of table");
         } else if (index >= tableModel.getRowCount()) {
            BasicDnD.displayDropLocation(dropValue + "at end of table");
         } else {
            Object value1 = tableModel.getValueAt(dl.getRow() - 1, 0);
            Object value2 = tableModel.getValueAt(dl.getRow(), 0);
            BasicDnD.displayDropLocation(dropValue + "between \"" + value1 + "\" and \"" + value2 + "\"");
         }
      } else {
         // Get the current string under the drop.
         // Object value = tableModel.getValueAt(index, 0);
         // BasicDnD.displayDropLocation(dropValue + "on top of " + "\"" + value + "\"");
         return false;
      }

      /**
       * This is commented out for the basicdemo.html tutorial page. If you add this code snippet back and delete the "return false;" line, the list will accept
       * drops of type string.
       * 
       * // Perform the actual import. if (insert) { listModel.add(index, data); } else { listModel.set(index, data); } return true;
       */
      // return false;
      // Perform the actual import.
      System.out.println("10");
      if (insert) {
         // tableModel.addRow(index, data);
         for (Vector<Object> rowData : data) {
            tableModel.insertRow(index, rowData);
         }
      } else {
         tableModel.removeRow(index);
         for (Vector<Object> rowData : data) {
            tableModel.insertRow(index, rowData);
         }
      }
      return true;
   }
}