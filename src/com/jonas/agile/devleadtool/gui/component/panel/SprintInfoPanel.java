package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.SprintNode;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.Status;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.SprintNode.SprintAnalyser;
import com.jonas.common.CalculatorHelper;

public class SprintInfoPanel extends JPanel {

   private static final int JIRACOUNTTEXTFIELD_COLUMNCOUNT = 2;
   private static final int JIRAPERCENTAGETEXTFIELD_COLUMNCOUNT = 2;
   private JTextField jiraCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField estimateCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField actualCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField openCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField openPercentage = new JTextField(JIRAPERCENTAGETEXTFIELD_COLUMNCOUNT);
   private JTextField reOpenedCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField reOpenedPercentage = new JTextField(JIRAPERCENTAGETEXTFIELD_COLUMNCOUNT);
   private JTextField inProgressCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField inProgressPercentage = new JTextField(JIRAPERCENTAGETEXTFIELD_COLUMNCOUNT);
   private JTextField resolvedCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField resolvedPercentage = new JTextField(JIRAPERCENTAGETEXTFIELD_COLUMNCOUNT);
   private JTextField closedCount = new JTextField(JIRACOUNTTEXTFIELD_COLUMNCOUNT);
   private JTextField closedPercentage = new JTextField(JIRAPERCENTAGETEXTFIELD_COLUMNCOUNT);
   private JLabel mainLabel;

   public SprintInfoPanel() {
      super(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();

      JPanel comp = new JPanel();
      mainLabel = new JLabel("");
      comp.add(mainLabel);

      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 1.0;
      gbc.weighty = 0;
      gbc.gridwidth = 2;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.anchor = GridBagConstraints.CENTER;
      add(comp, gbc);

      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.weightx = 0.5;
      gbc.weighty = 1;
      gbc.gridwidth = 1;
      gbc.fill = GridBagConstraints.BOTH;
      add(getJiraCountPanel(), gbc);

      gbc.gridx = 1;
      gbc.gridy = 1;
      add(getSprintSpecificInfoPanel(), gbc);
   }

   private Component getSprintSpecificInfoPanel() {
      JPanel panel = new JPanel(new GridLayout(5, 3, 2, 2));
      addJiraInfoLine(panel, "Total Jiras:", jiraCount);
      addJiraInfoLine(panel, "Total Estimate:", estimateCount);
      addJiraInfoLine(panel, "Total Actuals", actualCount);
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

   public void calculateInfo(Object component) {
      if (component instanceof SprintNode) {
         SprintNode sprintNode = (SprintNode) component;
         mainLabel.setText("Statistics for Sprint " + sprintNode.getSprintName());

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

         jiraCount.setText("" + analysis.getJiraCount());

         float estimateInDays = CalculatorHelper.getSecondsAsDays(analysis.getEstimateTotal());
         float actualInDays = CalculatorHelper.getSecondsAsDays(analysis.getActualTotal());

         estimateCount.setText("" + estimateInDays);
         actualCount.setText("" + actualInDays);
      }
   }

}
