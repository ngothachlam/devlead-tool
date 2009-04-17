package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import com.jonas.testing.jxtreetable.Column;

public abstract class DefaultUserObject implements Comparable<Jira>, Transferable {
   @Override
   public final Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      if (isDataFlavorSupported(flavor)) {
         return this;
      }
      throw new UnsupportedFlavorException(flavor);
   }

   public abstract String getValueForColumn(Column column);

   @Override
   public final DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[] { getDataFlavor() };
   }

   @Override
   public final boolean isDataFlavorSupported(DataFlavor flavor) {
      return flavor.equals(getDataFlavor());
   }

   protected abstract DataFlavor getDataFlavor();

   public abstract void setValue(Column column, Object value);
}