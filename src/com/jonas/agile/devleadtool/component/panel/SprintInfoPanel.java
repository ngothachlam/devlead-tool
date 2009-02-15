package com.jonas.agile.devleadtool.component.panel;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;
import com.jonas.agile.devleadtool.component.tree.SprintTree;
import com.jonas.agile.devleadtool.component.tree.nodes.SprintNode;
import com.jonas.agile.devleadtool.component.tree.nodes.Status;
import com.jonas.agile.devleadtool.component.tree.nodes.SprintNode.SprintAnalyser;
import com.jonas.common.SwingUtil;

public class SprintInfoPanel extends JPanel {
   
   private static final int JIRACOUNTTEXTFIELD_COLUMNCOUNT = 2;
   private static final int JIRAPERCENTAGETEXTFIELD_COLUMNCOUNT = 2;
   private JTextField JiraCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField EstimateCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField ActualCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField openCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField openPercentage= new JTextField(JIRAPERCENTAGETEXTFIELD_COLUMNCOUNT);
   private JTextField reOpenedCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField reOpenedPercentage= new JTextField(JIRAPERCENTAGETEXTFIELD_COLUMNCOUNT);
   private JTextField inProgressCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField inProgressPercentage= new JTextField(JIRAPERCENTAGETEXTFIELD_COLUMNCOUNT);
   private JTextField resolvedCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField resolvedPercentage= new JTextField(JIRAPERCENTAGETEXTFIELD_COLUMNCOUNT);
   private JTextField closedCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField closedPercentage= new JTextField(JIRAPERCENTAGETEXTFIELD_COLUMNCOUNT);
   private final SprintTree tree;

   public SprintInfoPanel(SprintTree tree){
      this.tree = tree;
      add(getJiraCountPanel());
      add(getSprintSpecificInfoPanel());
   }

   private Component getSprintSpecificInfoPanel() {
      JPanel panel = new JPanel(new GridLayout(5, 3, 2, 2));
      addJiraInfoLine(panel, "Total Jiras:", JiraCount);
      addJiraInfoLine(panel, "Total Estimate:", EstimateCount);
      addJiraInfoLine(panel, "Total Actuals", ActualCount);
      panel.setBorder(BorderFactory.createTitledBorder("Sprint info"));
      return panel;
   }

   public JPanel getJiraCountPanel() {
      JPanel panel = new JPanel(new GridLayout(5, 3, 2, 2));
      addJiraInfoLine(panel, "Open:", openCount, openPercentage);
      addJiraInfoLine(panel, "InProgress:", inProgressCount, inProgressPercentage);
      addJiraInfoLine(panel, "Re-Opened:", reOpenedCount, reOpenedPercentage);
      addJiraInfoLine(panel, "Resolved:", resolvedCount, resolvedPercentage);
      addJiraInfoLine(panel, "Closed:", closedCount, closedPercentage);
      panel.setBorder(BorderFactory.createTitledBorder("Jira Counts"));
      return panel;
   }
   
   private void addJiraInfoLine(JPanel panel, String text, JTextField countField) {
      panel.add(new JLabel(text));
      panel.add(countField);
   }
   
   private void addJiraInfoLine(JPanel panel, String text, JTextField countField, JTextField percentageField) {
      addJiraInfoLine(panel, text, countField);
      panel.add(percentageField);
   }

   public void calculateInfo() {
      TreePath[] paths = tree.getSelectionPaths();
      for (TreePath treePath : paths) {
         Object component = treePath.getLastPathComponent();
         if (component instanceof SprintNode) {
            SprintNode sprintNode = (SprintNode) component;

            SprintAnalyser analysis = sprintNode.analyseData();
            
            openCount.setText("" + analysis.getCount(Status.Open));
            reOpenedCount.setText("" + analysis.getCount(Status.Reopened));
            inProgressCount.setText("" + analysis.getCount(Status.InProgress));
            resolvedCount.setText("" + analysis.getCount(Status.Resolved));
            closedCount.setText("" + analysis.getCount(Status.Closed));
            
            openPercentage.setText("" + analysis.getPercentage(Status.Open));
            reOpenedPercentage.setText("" + analysis.getPercentage(Status.Reopened));
            inProgressPercentage.setText("" + analysis.getPercentage(Status.InProgress));
            resolvedPercentage.setText("" + analysis.getPercentage(Status.Resolved));
            closedPercentage.setText("" + analysis.getPercentage(Status.Closed));
         }
      }
   }
  
}
