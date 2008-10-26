package com.jonas.testing.tree.fromScratch.tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class SprintNode extends DefaultMutableTreeNode {

   public SprintNode(String sprintName) {
      super(sprintName);
   }

   @Override
   public boolean getAllowsChildren() {
      return true;
   }

}
