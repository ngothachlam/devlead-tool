/**
 * 
 */
package com.jonas.guibuilding;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

final class TheTransferHandler extends TransferHandler {

   private final JList alist;

   public TheTransferHandler(JList alist) {
      super();
      this.alist = alist;
   }

   // On the source object:
   
   public int getSourceActions(JComponent c) {
      return MOVE;
   }
   protected Transferable createTransferable(JComponent c) {
      JList list = (JList) c;
      if (list.getSelectedValues().length > 1)
         throw new RuntimeException("Can currently not handle moving more than one selection!!");
      {
         // ListElement values = (ListElement) list.getSelectedValues()[0];
         // return values;
      }
      {
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
   }
   protected void exportDone(JComponent source, Transferable data, int action) {
      super.exportDone(source, data, action);
      if (action == MOVE)
         if (source instanceof JList) {
            JList list = (JList) source;
            if ( list.getModel() instanceof DefaultListModel) {
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

      JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
      if (dl.getIndex() == -1) {
         return false;
      }
      return true;
   }
   public boolean importData(TransferSupport info) {
      if (!info.isDrop()) {
         return false;
      }
      // Check for String flavor
      if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
         BasicDnD.displayDropLocation("List doesn't accept a drop of this type.");
         return false;
      }
      JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
      DefaultListModel listModel = (DefaultListModel) alist.getModel();
      int index = dl.getIndex();
      boolean insert = dl.isInsert();
      // Get the current string under the drop.
      String value = (String) listModel.getElementAt(index);

      // Get the string that is being dropped.
      Transferable t = info.getTransferable();
      String data;
      try {
         data = (String) t.getTransferData(DataFlavor.stringFlavor);
      } catch (Exception e) {
         return false;
      }

      // Display a dialog with the drop information.
      String dropValue = "\"" + data + "\" dropped ";
      if (dl.isInsert()) {
         if (dl.getIndex() == 0) {
            BasicDnD.displayDropLocation(dropValue + "at beginning of list");
         } else if (dl.getIndex() >= alist.getModel().getSize()) {
            BasicDnD.displayDropLocation(dropValue + "at end of list");
         } else {
            String value1 = (String) alist.getModel().getElementAt(dl.getIndex() - 1);
            String value2 = (String) alist.getModel().getElementAt(dl.getIndex());
            BasicDnD.displayDropLocation(dropValue + "between \"" + value1 + "\" and \"" + value2 + "\"");
         }
      } else {
         BasicDnD.displayDropLocation(dropValue + "on top of " + "\"" + value + "\"");
      }

      /**
       * This is commented out for the basicdemo.html tutorial page. If you add this code snippet back and delete the "return false;" line, the
       * list will accept drops of type string.
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

}