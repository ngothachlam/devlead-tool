package com.jonas.agile.devleadtool.gui.component.tree.nodes;

import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.gui.component.tree.ToolTipper;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.FixVersionNode.FixVersionChildAnalyser;
import com.jonas.common.CalculatorHelper;
import com.jonas.common.logging.MyLogger;

public class SprintNode extends DefaultMutableTreeNode implements ToolTipper {

   private static final Logger log = MyLogger.getLogger(SprintNode.class);
   private final String sprintName;
   private SprintAnalyser dataAnalyser = new SprintAnalyser();

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
      SprintAnalyser anlysis = analyseData();
      int estimateTotalSeconds = anlysis.getEstimateTotal();
      String estimateTotalDaysAsString = CalculatorHelper.getSecondsAsDaysAndString(estimateTotalSeconds);

      StringBuffer sb = new StringBuffer(getUserObject().toString());
      sb.append(" (Jiras: ").append(anlysis.getJiraCount());

      sb.append(" where Open: ").append(anlysis.getCount(Status.Open));
      sb.append(" (").append(getPercentage(anlysis, Status.Open)).append(")");
      sb.append(", ReOpened: ").append(anlysis.getCount(Status.Reopened));
      sb.append(" (").append(getPercentage(anlysis, Status.Reopened)).append(")");
      sb.append(", InProgress: ").append(anlysis.getCount(Status.InProgress));
      sb.append(" (").append(getPercentage(anlysis, Status.InProgress)).append(")");
      sb.append(", Resolved: ").append(anlysis.getCount(Status.Resolved));
      sb.append(" (").append(getPercentage(anlysis, Status.Resolved)).append(")");
      sb.append(", Closed: ").append(anlysis.getCount(Status.Closed));
      sb.append(" (").append(getPercentage(anlysis, Status.Closed)).append(")");

      return sb.toString();
   }

   private String getPercentage(SprintAnalyser anlysis, Status open) {
      return CalculatorHelper.getPercentage(anlysis.getPercentage(open)) + "%";
   }

   public SprintAnalyser analyseData() {
      dataAnalyser.analyse();
      return dataAnalyser;
   }

   public class SprintAnalyser {

      private Status lowestStatus;
      private int jiraCount;
      private Map<Status, Integer> countMap = new HashMap<Status, Integer>(5);
      private int estimateTotal;
      private int actualTotal;

      Status getLowestStatus() {
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

      public String getPercentage(Status status) {
         Integer count = countMap.get(status);
         Float countAsFloat = count == null ? 0f : count;
         float percentage = (countAsFloat / jiraCount) * 100;
         log.debug("jiraCount: " + jiraCount + " and countAsFloat: " + countAsFloat + " for status " + status + " gives " + percentage);
         return CalculatorHelper.getPercentage(percentage);
      }
      
      public void analyse() {
         log.debug("analysing");
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
         estimateTotal += fixVersionAnalyser.getEstimateTotal();
         actualTotal += fixVersionAnalyser.getActualTotal();
         jiraCount += fixVersionAnalyser.getJiraCount();
      }

      private void resetCounts() {
         jiraCount = 0;
         estimateTotal = 0;
         countMap.clear();
      }

      public int getEstimateTotal() {
         return estimateTotal;
      }

      public int getActualTotal() {
         return actualTotal;
      }
      
   }
   
}
