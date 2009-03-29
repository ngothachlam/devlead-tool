package com.jonas.agile.devleadtool.gui.component.tree.nodes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import com.jonas.agile.devleadtool.gui.component.tree.ToolTipper;
import com.jonas.common.CalculatorHelper;

public class JiraNode extends DefaultMutableTreeNode implements ToolTipper{

   private final String summary;
   private final List<String> fixVersions = new ArrayList<String>();
   private boolean isToSync;
   private final String key;
   private final String resolution;
   private final String sprint;
   private final String status;
   private final String id;
   private final int originalEstimate;
   private int actual;
   private String project;

   public JiraNode(String jira, String id, String summary, String resolution, String status, String sprint, List<String> fixVersions, boolean isToSync, int originalEstimate, int actual, String project) {
      super(jira);
      this.key = jira;
      this.resolution = resolution;
      this.status = status;
      this.summary = summary;
      this.sprint = sprint;
      this.isToSync = isToSync;
      this.id = id;
      this.originalEstimate = originalEstimate;
      this.actual = actual;
      this.project = project;
      for (String string : fixVersions) {
         this.fixVersions.add(string);
      }
   }

   public String getId() {
      return id;
   }

   @Override
   public boolean getAllowsChildren() {
      return false;
   }

   public String getSummary() {
      return summary;
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

   public int getOriginalEstimate() {
      return originalEstimate;
   }

   public String getToolTipText() {
      String secondsAsDaysAndString = CalculatorHelper.getSecondsAsDaysAndString(getOriginalEstimate());
      
      StringBuffer sb = new StringBuffer(getUserObject().toString());
      String tempSummary = CalculatorHelper.cutString(getSummary(), 70, "...");
      sb.append(" ").append(tempSummary);
      sb.append(" - ");
      sb.append(getStatus());
      sb.append(" (").append(getResolution()).append(")");
      sb.append(", Estimate: ").append(secondsAsDaysAndString).append(" days");
      return sb.toString();
   }

   public int getActual() {
      return actual;
   }

   public String getProject() {
      return project;
   }
}
