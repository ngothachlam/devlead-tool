package com.jonas.agile.devleadtool.component.tree.nodes;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.tree.ToolTipper;
import com.jonas.agile.devleadtool.component.tree.nodes.FixVersionNode.FixVersionChildAnalyser;
import com.jonas.common.logging.MyLogger;

public class SprintNode extends DefaultMutableTreeNode implements ToolTipper {

   private static final Logger log = MyLogger.getLogger(SprintNode.class); 
   private final String sprintName;
   private SprintChildAnalyser toolTipAnalyser = new SprintChildAnalyser();
   private DecimalFormat decimalFormat = new DecimalFormat("#");
   
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
      return toolTipAnalyser.getLowestStatus();
   }

   public FixVersionNode getFixVersionNode(String fixVersionName) {
      for (int i = 0; i < getChildCount(); i++) {
         FixVersionNode fixVersionNode = (FixVersionNode) getChildAt(i);
         if (fixVersionNode.getFixVersionName().equals(fixVersionName)) {
            return fixVersionNode;
         }
      }
      return null;
   }

   public String getToolTipText() {
      SprintChildAnalyser anlysis = analyseChildrenForToolTip();
      StringBuffer sb = new StringBuffer(getUserObject().toString());
      sb.append(" (Lowest Resolution: ").append(anlysis.getLowestStatus()).append(", Jiras: ").append(anlysis.getJiraCount()).append(")\n");
      
      Status open = Status.Open;
      Status reopened = Status.Reopened;
      Status inProgress = Status.InProgress;
      Status resolved = Status.Resolved;
      Status closed = Status.Closed;
      
      sb.append(" Open: ").append(anlysis.getCount(open));
      sb.append(" ReOpened: ").append(anlysis.getCount(reopened));
      sb.append(" InProgress: ").append(anlysis.getCount(inProgress));
      sb.append(" Resolved: ").append(anlysis.getCount(resolved));
      sb.append(" Closed: ").append(anlysis.getCount(closed));
      
      sb.append(" Open (%): ").append(getPercentage(anlysis, open));
      sb.append(" ReOpened (%): ").append(getPercentage(anlysis, reopened));
      sb.append(" InProgress (%): ").append(getPercentage(anlysis, inProgress));
      sb.append(" Resolved (%): ").append(getPercentage(anlysis, resolved));
      sb.append(" Closed (%): ").append(getPercentage(anlysis, closed));
      return sb.toString();
   }

   private String getPercentage(SprintChildAnalyser anlysis, Status open) {
      return decimalFormat.format(anlysis.getPercentage(open)) + "%";
   }

   private SprintChildAnalyser analyseChildrenForToolTip() {
      toolTipAnalyser.analyse();
      return toolTipAnalyser;
   }

   private class SprintChildAnalyser {

      private Status lowestStatus;
      private int jiraCount;
      private Map<Status, Integer> countMap = new HashMap<Status, Integer>(5);

      private Status getLowestStatus() {
         return lowestStatus;
      }

      public int getJiraCount() {
         return jiraCount;
      }

      public int getCount(Status status) {
         log.debug("Amount of Counts:  " + countMap.size());
         Integer value = countMap.get(status);
         return value == null ? 0 : value;
      }
      public float getPercentage(Status status) {
         Integer count = countMap.get(status);
         Float value = count == null ? 0f : count;
         float percentage = (value / jiraCount) *100;
         log.debug("jiras: " + jiraCount + " and count: " + count + " for status " + status + " gives " + percentage);
         return percentage;
      }

      public void analyse() {
         resetCounts();
         Status result = Status.UnKnown;
         for (int i = 0; i < getChildCount(); i++) {
            FixVersionNode fixVersionNode = (FixVersionNode) getChildAt(i);
            FixVersionChildAnalyser fixVersionAnalyser = fixVersionNode.getAnalysis();
            Status status = fixVersionAnalyser.getLowestStatus();
            if (status.isLowerThan(result)) {
               result = status;
            }
            updateCounts(fixVersionAnalyser);
         }
         lowestStatus = result;
      }

      private void updateCounts(FixVersionChildAnalyser fixVersionAnalyser) {
         Map<Status, Integer> fixVersionStatuses = fixVersionAnalyser.getStatusCounter();
         for (Status status : fixVersionStatuses.keySet()) {
            Integer countForLastFixVersion = countMap.get(status);
            int value = (countForLastFixVersion != null ? countForLastFixVersion : 0) + fixVersionStatuses.get(status);
            countMap.put(status, value);
            log.debug("Status " + status + " now has " + value);
         }
         jiraCount += fixVersionAnalyser.getJiraCount();
      }

      private void resetCounts() {
         jiraCount = 0;
         countMap.clear();
      }

   }

   public String getPercentage(Status status) {
      float percentage = toolTipAnalyser.getPercentage(status);
      return decimalFormat.format(percentage)+"%";
   }
   public int getCount(Status status) {
      return toolTipAnalyser.getCount(status);
   }
   
   public void analyseToolTip() {
      toolTipAnalyser.analyse();
   }

}
