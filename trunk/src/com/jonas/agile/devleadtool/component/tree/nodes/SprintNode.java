package com.jonas.agile.devleadtool.component.tree.nodes;

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
