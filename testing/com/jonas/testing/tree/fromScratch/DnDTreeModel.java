package com.jonas.testing.tree.fromScratch;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import sun.awt.geom.AreaOp.AddOp;

public class DnDTreeModel extends DefaultTreeModel{

   public DnDTreeModel(String rootName) {
      super(new DefaultMutableTreeNode(rootName));
   }


}
