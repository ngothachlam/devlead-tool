package com.jonas.agile.devleadtool.component.tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public class SprintNode extends DefaultMutableTreeNode {

   private final String sprintName;

   public SprintNode(String sprintName) {
      super(sprintName);
      this.sprintName = sprintName;
   }
   
   public SprintNode(String sprintName, String realSprintName) {
      super(sprintName);
      this.sprintName = realSprintName;
   }

   public String getSprintName() {
      return sprintName;
   }

   @Override
   public boolean getAllowsChildren() {
      return true;
   }

   public Status getLowestStatus() {
      Status result = Status.UnKnown;
      for (int i = 0; i < getChildCount(); i++) {
         FixVersionNode fixVersionNode = (FixVersionNode) getChildAt(i);
         Status status = fixVersionNode.getLowestStatus();
         if (status.isLowerThan(result)){
            result = status;
         }
      }
      return result;
   }

}
