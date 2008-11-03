package com.jonas.agile.devleadtool.component.tree.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

final class DnDTransferable implements Transferable {
   private final JTree tree;
   private DataFlavor[] flavors;

   DnDTransferable(JTree tree, DataFlavor... dataFlavor) {
      this.flavors = dataFlavor;
      this.tree = tree;
   }

   @Override
   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      TreePath selectionPath = tree.getSelectionPath();
      DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
      return new TransferableDTO(lastPathComponent);
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