/**
 * 
 */
package com.jonas.testing.tree.fromScratch;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

final class DnDTransferable implements Transferable {
   private final JTree tree;
   private DataFlavor[] flavors;

   DnDTransferable(JTree tree, DataFlavor... dataFlavor) {
      this.flavors = dataFlavor;
      this.tree = tree;
   }

   @Override
   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
      return new TransferableDTO((DefaultTreeModel) tree.getModel(), lastPathComponent);
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return flavors;
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor flavor) {
      for (DataFlavor supportedFlavor : flavors) {
         if (supportedFlavor.equals(flavor))
            return true;
      }
      return false;
   }
}