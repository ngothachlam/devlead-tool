package com.jonas.agile.devleadtool.gui.component.tree.nodes;

import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.gui.component.tree.ToolTipper;
import com.jonas.common.logging.MyLogger;

public class FixVersionNode extends DefaultMutableTreeNode implements ToolTipper {

   private static final Logger log = MyLogger.getLogger(FixVersionNode.class); 
   private final String fixVersionName;

   public FixVersionNode(String fixVersionName) {
      super(fixVersionName);
      this.fixVersionName = fixVersionName;
   }

   public String getFixVersionName() {
      return fixVersionName;
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
      // FIXME this should be set to be released.
      return false;
   }

   public FixVersionChildAnalyser getAnalysis() {
      FixVersionChildAnalyser analyser = new FixVersionChildAnalyser();
      analyser.analyse();
      return analyser;
   }

   public JiraNode getJiraNode(String key) {
      for (int i = 0; i < getChildCount(); i++) {
         JiraNode jiraNode = (JiraNode) getChildAt(i);
         if (jiraNode.getKey().equals(key)) {
            return jiraNode;
         }
      }
      return null;
   }

   public String getToolTipText() {
      StringBuffer sb = new StringBuffer(getUserObject().toString());
      sb.append(" (Children: ").append(getChildCount()).append(")");
      return sb.toString();
   }

   public class FixVersionChildAnalyser {

      private Status lowestStatus;
      private int childCount;
      private Map<Status, Integer> countMap = new HashMap<Status, Integer>(5);
      private int estimateTotal;
      private int actualTotal;

      public Status getLowestStatus() {
         return lowestStatus;
      }

      public int getJiraCount() {
         return childCount;
      }

      public int getCount(Status status) {
         Integer value = countMap.get(status);
         return value == null ? 0 : value;
      }

      public float getPercentage(Status status) {
         Integer count = countMap.get(status);
         int value = count == null ? 0 : count;
         return (value / childCount) * 100;
      }

      private void analyse() {
         resetCounts();
         childCount = getChildCount();

         Status result = Status.UnKnown;
         for (int i = 0; i < getChildCount(); i++) {
            JiraNode jiraNode = (JiraNode) getChildAt(i);
            Status status = Status.get(jiraNode.getStatus());
            if (status.isLowerThan(result)) {
               result = status;
            }
            Integer integer = countMap.get(status);
            int value = (integer != null ? integer : 0) + 1;
            countMap.put(status, value);
            log.debug("jiraNode: " + jiraNode.getKey() +" has original Estimate: " + jiraNode.getOriginalEstimate());
            estimateTotal += jiraNode.getOriginalEstimate();
            actualTotal += jiraNode.getActual();
         }
         lowestStatus = result;
      }

      private void resetCounts() {
         estimateTotal = 0;
         actualTotal = 0;
         countMap.clear();
      }

      public Map<Status, Integer> getStatusCounter() {
         return countMap;
      }

      public int getEstimateTotal() {
         return estimateTotal;
      }

      public int getActualTotal() {
         return actualTotal;
      }
   }
}
