package com.jonas.agile.devleadtool.component.tree.nodes;

import javax.swing.ImageIcon;

public enum Status {
   UnKnown(100, new ImageIcon("icons/jira_fixVersion_open.gif")), 
   Closed(40, new ImageIcon("icons/jira_fixVersion_closed.gif")), 
   Resolved(30, new ImageIcon("icons/jira_fixVersion_resolved.gif")), 
   Reopened(20, new ImageIcon("icons/jira_fixVersion_reopened.gif")), 
   InProgress(10, new ImageIcon("icons/jira_fixVersion_inprogress.gif")), 
   Open(0, new ImageIcon("icons/jira_fixVersion_open.gif"));

   private int value;
   private final ImageIcon icon;

   private Status(int value, ImageIcon icon) {
      this.value = value;
      this.icon = icon;
   }

   public boolean isLowerThan(Status comparing) {
      if (getValue() < comparing.getValue()) {
         return true;
      }
      return false;
   }

   private int getValue() {
      return value;
   }

   public static Status get(String name) {
      for (Status resolution : values()) {
         if (resolution.toString().equalsIgnoreCase(name)) {
            return resolution;
         }
      }
      return UnKnown;
   }

   public ImageIcon getIcon() {
      return icon;
   }
   
}
