package com.jonas.testing.tree.fromScratch.tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class JiraNode extends DefaultMutableTreeNode {

   public JiraNode(String jira) {
      super(jira);
   }

   @Override
   public boolean getAllowsChildren() {
      return false;
   }
}
