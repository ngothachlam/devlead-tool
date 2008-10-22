package com.jonas.guibuilding.basicdnd.list;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ListElement implements Transferable{

   private final String name;

   public ListElement(String name) {
      this.name = name;
   }

   @Override
   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor flavor) {
      // TODO Auto-generated method stub
      return false;
   }

}
