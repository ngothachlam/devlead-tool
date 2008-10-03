/**
 * 
 */
package com.jonas.guibuilding.newlayout;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.TransferHandler;

final class TheTransferHandler extends TransferHandler {
   /**
    * 
    */
   private final TopLevelTransferHandlerDemo topLevelTransferHandlerDemo;

   /**
    * @param topLevelTransferHandlerDemo
    */
   TheTransferHandler(TopLevelTransferHandlerDemo topLevelTransferHandlerDemo) {
      this.topLevelTransferHandlerDemo = topLevelTransferHandlerDemo;
   }

   public boolean canImport(TransferHandler.TransferSupport support) {
      if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
         return false;
      }

      if (this.topLevelTransferHandlerDemo.copyItem.isSelected()) {
         boolean copySupported = (COPY & support.getSourceDropActions()) == COPY;

         if (!copySupported) {
            return false;
         }

         support.setDropAction(COPY);
      }

      return true;
   }

   public boolean importData(TransferHandler.TransferSupport support) {
      if (!canImport(support)) {
         return false;
      }

      Transferable t = support.getTransferable();

      try {
         java.util.List<File> l = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

         for (File f : l) {
            new Doc(this.topLevelTransferHandlerDemo, f);
         }
      } catch (UnsupportedFlavorException e) {
         return false;
      } catch (IOException e) {
         return false;
      }

      return true;
   }
}