/**
 * 
 */
package com.jonas.guibuilding.basicdnd.list;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

final class ListTransferHandler extends TransferHandler {

   private DefaultListModel listModel;

   public ListTransferHandler(JList list) {
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
   }

   protected void exportDone(JComponent source, Transferable data, int action) {
      super.exportDone(source, data, action);
      if (action == MOVE) {
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
      }
      return false;
   }

   public boolean importData(TransferSupport info) {
      if (!info.isDrop()) {
         System.out.println("List doesn't accept !isDrop");
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
               System.out.println(dropValue + "at beginning of list");
            } else if (index >= listModel.getSize()) {
               System.out.println(dropValue + "at end of list");
            } else {
               Object value1 = listModel.getElementAt(dl.getIndex() - 1);
               Object value2 = listModel.getElementAt(dl.getIndex());
               System.out.println(dropValue + "between \"" + value1 + "\" and \"" + value2 + "\"");
            }
         } else {
            // Get the current string under the drop.
            Object value = listModel.getElementAt(index);
            System.out.println(dropValue + "on top of " + "\"" + value + "\"");
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