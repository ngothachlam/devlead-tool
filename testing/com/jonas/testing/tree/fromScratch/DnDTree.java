package com.jonas.testing.tree.fromScratch;

import javax.swing.DropMode;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

public class DnDTree extends JTree {

   public DnDTree(TreeModel model) {
      super(model);

      setDnD();

   }

   private void setDnD() {
      setDragEnabled(true);
      setDropMode(DropMode.INSERT);
      setTransferHandler(new DnDTreeTransferHandler());

      setEditable(false);
      getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

   }

}
