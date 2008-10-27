package com.jonas.testing.tree.fromScratch.tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class JiraNode extends DefaultMutableTreeNode {

   private final FixVersionNode parent;
   private String resolution;
   private String status;
   private String description;

   public JiraNode(String jira, FixVersionNode parent, String resolution, String status, String description) {
      super(jira);
      this.parent = parent;
      this.resolution = resolution;
      this.status = status;
      this.description = description;
   }

   @Override
   public boolean getAllowsChildren() {
      return false;
   }

   public String getResolution() {
      return resolution;
   }

   public String getStatus() {
      return status;
   }

   public String getDescription() {
      return description;
   }
}
