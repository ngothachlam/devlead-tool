package com.jonas.testing.tree.fromScratch.tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class JiraNode extends DefaultMutableTreeNode {

   private final FixVersionNode parent;

   public JiraNode(String jira, FixVersionNode parent) {
      super(jira);
      this.parent = parent;
   }

   @Override
   public boolean getAllowsChildren() {
      return false;
   }
}
