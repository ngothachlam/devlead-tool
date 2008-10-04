package com.jonas.guibuilding.basicdnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class TableTransferable implements Transferable {
   private DataFlavor[] flavors = new DataFlavor[] { TableTransferHandler.vectorFlavor };
   private Object data;

   public TableTransferable(Object data) {
      super();
      this.data = data;
   }

   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
      if (TableTransferHandler.vectorFlavor.equals(flavor)) {
         return data;
      }
      throw new UnsupportedFlavorException(flavor);
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return flavors.clone();
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor flavor) {
      for (DataFlavor aflavor : flavors) {
         if (aflavor.equals(flavor)) {
            return true;
         }
      }
      return false;
   }

}
