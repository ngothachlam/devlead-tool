package com.jonas.testing.tree.fromScratch.tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class FixVersionNode extends DefaultMutableTreeNode {

   public FixVersionNode(String fixVersionName) {
      super(fixVersionName);
   }

   @Override
   public boolean getAllowsChildren() {
      return true;
   }

   @Override
   public String toString() {
      return super.toString();
   }
}
