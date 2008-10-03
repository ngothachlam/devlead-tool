/**
 * 
 */
package com.jonas.guibuilding.basicdnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;

final class TheTransferHandler extends TransferHandler {

   private DefaultTableModel tableModel;
   private DefaultListModel listModel;

   public TheTransferHandler(JTable list) {
      super();
      tableModel = (DefaultTableModel) list.getModel();
   }

   public TheTransferHandler(JList list) {
      super();
      listModel = (DefaultListModel) list.getModel();
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
      if (c instanceof JList) {
         JList list = (JList) c;
         if (list.getSelectedValues().length > 1)
            throw new RuntimeException("Can currently not handle moving more than one selection!!");
         Object[] values = (Object[]) list.getSelectedValues();
         StringBuffer buff = new StringBuffer();

         for (int i = 0; i < values.length; i++) {
            Object val = values[i];
            buff.append(val == null ? "" : val.toString());
            if (i != values.length - 1) {
               buff.append("\n");
            }
         }
         return new StringSelection(buff.toString());
      } else if (c instanceof JTable) {
         JTable list = (JTable) c;
         if (list.getSelectedRows().length > 1)
            throw new RuntimeException("Can currently not handle moving more than one selection!!");
         int[] values = list.getSelectedRows();
         StringBuffer buff = new StringBuffer();

         for (int i = 0; i < values.length; i++) {
            Object val = list.getValueAt(values[i], 0);
            buff.append(val == null ? "" : val.toString());
            if (i != values.length - 1) {
               buff.append("\n");
            }
         }
         return new StringSelection(buff.toString());
      } else {
         return null;
      }
   }

   protected void exportDone(JComponent source, Transferable data, int action) {
      super.exportDone(source, data, action);
      if (action == MOVE)
         if (source instanceof JList) {
            JList list = (JList) source;
            if (list.getModel() instanceof DefaultListModel) {
               DefaultListModel model = (DefaultListModel) list.getModel();
               int selectedIndex = list.getSelectedIndex();
               model.removeElementAt(selectedIndex);
            }
         } else if (source instanceof JTable) {
            JList list = (JList) source;
            if (list.getModel() instanceof DefaultListModel) {
               DefaultListModel model = (DefaultListModel) list.getModel();
               int selectedIndex = list.getSelectedIndex();
               model.removeElementAt(selectedIndex);
            }
         }
   }

   // On the destination object...
   public boolean canImport(TransferSupport info) {
      // we only import Strings
      // if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
      // return false;
      // }
      if (info.getComponent() instanceof JList) {
         if (((JList.DropLocation) info.getDropLocation()).getIndex() != -1) {
            return true;
         }
      } else if (info.getComponent() instanceof JTable) {
         if (((JTable.DropLocation) info.getDropLocation()).getRow() != -1) {
            return true;
         }
      }
      return false;
   }

   public boolean importData(TransferSupport info) {
      if (!info.isDrop()) {
         BasicDnD.displayDropLocation("List doesn't accept !isDrop");
         return false;
      }
      // Check for String flavor
      // if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
      // BasicDnD.displayDropLocation("List doesn't accept a drop of this type.");
      // return false;
      // }
      if (info.getComponent() instanceof JList) {
         JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
         int index = dl.getIndex();
         boolean insert = dl.isInsert();

         // Get the string that is being dropped.
         Transferable t = info.getTransferable();
         String data;
         try {
            data = (String) t.getTransferData(DataFlavor.stringFlavor);
         } catch (Exception e) {
            e.printStackTrace();
            return false;
         }

         // Display a dialog with the drop information.
         String dropValue = "\"" + data + "\" dropped ";
         if (dl.isInsert()) {
            if (index == 0) {
               BasicDnD.displayDropLocation(dropValue + "at beginning of list");
            } else if (index >= listModel.getSize()) {
               BasicDnD.displayDropLocation(dropValue + "at end of list");
            } else {
               Object value1 = listModel.getElementAt(dl.getIndex() - 1);
               Object value2 = listModel.getElementAt(dl.getIndex());
               BasicDnD.displayDropLocation(dropValue + "between \"" + value1 + "\" and \"" + value2 + "\"");
            }
         } else {
            // Get the current string under the drop.
            Object value = listModel.getElementAt(index);
            BasicDnD.displayDropLocation(dropValue + "on top of " + "\"" + value + "\"");
         }

         /**
          * This is commented out for the basicdemo.html tutorial page. If you add this code snippet back and delete the "return false;" line, the list will
          * accept drops of type string.
          * 
          * // Perform the actual import. if (insert) { listModel.add(index, data); } else { listModel.set(index, data); } return true;
          */
         // return false;
         // Perform the actual import.
         if (insert) {
            listModel.add(index, data);
         } else {
            listModel.set(index, data);
         }
         return true;
      }
      return false;
   }
}