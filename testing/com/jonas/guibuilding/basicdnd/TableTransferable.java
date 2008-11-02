package com.jonas.guibuilding.basicdnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;

public class TableTransferable implements Transferable {
   private DataFlavor[] flavors = new DataFlavor[] { TableTransferHandler.vectorFlavor };
   private Vector<Vector<Object>> data;
   private final Vector<Column> columns;

   public TableTransferable(Vector<Column> columns, Vector<Vector<Object>> data) {
      super();
      this.columns = columns;
      this.data = data;
   }

   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
      if (TableTransferHandler.vectorFlavor.equals(flavor)) {
         return new TableDTO(columns,data);
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

class TableDTO{

   private final Vector<Column> columns;
   private final Vector<Vector<Object>> data;

   public TableDTO(Vector<Column> columns, Vector<Vector<Object>> data) {
      this.columns = columns;
      this.data = data;
   }

   public Vector<Column> getColumns() {
      return columns;
   }

   public Vector<Vector<Object>> getData() {
      return data;
   }
}
