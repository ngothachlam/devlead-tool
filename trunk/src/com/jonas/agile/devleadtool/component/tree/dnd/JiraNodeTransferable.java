package com.jonas.agile.devleadtool.component.tree.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import com.jonas.agile.devleadtool.component.tree.nodes.JiraNode;

final class JiraNodeTransferable implements Transferable {
   private final JTree tree;
   private DataFlavor[] flavors;
   private TreePath[] selectionPaths;

   JiraNodeTransferable(JTree tree, DataFlavor... dataFlavor) {
      this.flavors = dataFlavor;
      this.tree = tree;
      selectionPaths = tree.getSelectionPaths();
   }

   @Override
   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      if (!isDataFlavorSupported(flavor))
         throw new UnsupportedFlavorException(flavor);
      JiraNodesTransferableDTO dto = new JiraNodesTransferableDTO();
      for (TreePath treePath : selectionPaths) {
         DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) treePath.getLastPathComponent();
         if (lastPathComponent instanceof JiraNode)
            dto.addNode((JiraNode) lastPathComponent);
      }
      return dto;
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