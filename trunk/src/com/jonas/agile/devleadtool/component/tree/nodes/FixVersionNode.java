package com.jonas.agile.devleadtool.component.tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class FixVersionNode extends DefaultMutableTreeNode {

   private final SprintNode parent;

   public FixVersionNode(String fixVersionName, SprintNode parent) {
      super(fixVersionName);
      this.parent = parent;
   }

   @Override
   public boolean getAllowsChildren() {
      return true;
   }

   @Override
   public String toString() {
      return super.toString();
   }

   public boolean isReleased() {
      //FIXME this should be set to be released. 
      return false;
   }
}
