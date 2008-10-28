package com.jonas.testing.tree.fromScratch.tree.nodes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

public class JiraNode extends DefaultMutableTreeNode {

   private final String description;
   private final List<String> fixVersions = new ArrayList<String>();
   private boolean isToSync;
   private final String key;
   private final FixVersionNode parent;
   private final String resolution;
   private final String sprint;
   private final String status;

   public JiraNode(String jira, FixVersionNode parent, String resolution, String status, String description, String sprint, List<String> fixVersions, boolean isToSync) {
      super(jira);
      this.key = jira;
      this.parent = parent;
      this.resolution = resolution;
      this.status = status;
      this.description = description;
      this.sprint = sprint;
      this.isToSync = isToSync;
      for (String string : fixVersions) {
         this.fixVersions.add(string);
      }
   }

   @Override
   public boolean getAllowsChildren() {
      return false;
   }

   public String getDescription() {
      return description;
   }

   public List<String> getFixVersions() {
      return fixVersions;
   }

   public String getKey() {
      return key;
   }

   public String getResolution() {
      return resolution;
   }

   public String getSprint() {
      return sprint;
   }

   public String getStatus() {
      return status;
   }

   public boolean isToSync() {
      return isToSync;
   }

   public void setToSynced() {
      isToSync = false;
   }
}
