package com.jonas.testing.tree.fromScratch.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class DnDTreeModel extends DefaultTreeModel {

   public DnDTreeModel(String rootName) {
      super(new DefaultMutableTreeNode(rootName));
   }


}
