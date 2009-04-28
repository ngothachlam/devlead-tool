package com.jonas.agile.devleadtool.gui.component.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.burndown.SprintBurndownGrapher;
import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.DateHelper;
import com.jonas.common.string.StringHelper;

public class BoardStatsFrame extends AbstractBasicFrame implements SprintBurndownGrapher {

   private XYSeries dataSeries;
   private XYSeries idealSeries;
   // private XYSeries prognosisSeries;

   private JTextField dayInSprintTextField;
   private ValueAxis domainAxis;
   private JTextField lengthOfSprintTextField;
   private ChartPanel panel;
   private final MyTable sourceTable;
   private DateHelper dateHelper;
   private TextTitle source;
   private NumberAxis rangeAxis;
   private final SprintCache sprintCache;

   public BoardStatsFrame(Component parent, int width, int height, MyTable sourceTable, DateHelper dateHelper, SprintCache sprintCache) {
      super(parent, width, height);
      this.sourceTable = sourceTable;
      this.dateHelper = dateHelper;
      this.sprintCache = sprintCache;
      this.prepareBurndown();
   }

   public void calculateAndPrintBurndown(double totalDevEstimates, double remainingDevEstimates, Set<String> projects) {
      source.setText(StringHelper.getNiceString(projects));
      // prognosisSeries.clear();
      dataSeries.clear();
      idealSeries.clear();

      rangeAxis.setAutoRange(true);
      domainAxis.setLowerBound(0d);
      dataSeries.add(0d, totalDevEstimates);

      double dayInSprint = StringHelper.getDoubleOrZero(dayInSprintTextField.getText());
      double lengthOfSprint = StringHelper.getDoubleOrZero(lengthOfSprintTextField.getText());

      dataSeries.add(dayInSprint, remainingDevEstimates);
      domainAxis.setUpperBound(Math.max(lengthOfSprint, dayInSprint) + 0.2d);

      // if (dayInSprint < lengthOfSprint) {
      // double prognosisChangePerDay = ((totalDevEstimates - remainingDevEstimates) / dayInSprint);
      // double prognosisDays = lengthOfSprint - dayInSprint;
      // prognosisSeries.add(dayInSprint, remainingDevEstimates);
      // prognosisSeries.add(lengthOfSprint, remainingDevEstimates - (prognosisChangePerDay * prognosisDays));
      // }
      idealSeries.add(0, totalDevEstimates);
      idealSeries.add(lengthOfSprint, 0);

      rangeAxis.setLowerBound(0d);

   }

   @Override
   public Container getMyPanel() {
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.add(getTopPanel(), BorderLayout.NORTH);
      mainPanel.add(panel, BorderLayout.CENTER);
      return mainPanel;
   }

   private Component getTopPanel() {
      JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
      JPanel inputPanel = getSubInputPanel();
      JPanel buttonPanel = getSubButtonPanel();
      panel.setBorder(BorderFactory.createTitledBorder("Sprint Info"));
      panel.add(inputPanel);
      panel.add(buttonPanel);
      return panel;
   }

   private JPanel getSubButtonPanel() {
      JPanel panel = new JPanel(new GridLayout(1, 1, 3, 3));
      panel.add(new JButton(new CalculateSprintBurndownAction("Calculate Burndown", "Calculate Burndown", this, sourceTable, this)));
      return panel;
   }

   private JPanel getSubInputPanel() {
      JPanel panel = new JPanel(new GridLayout(3, 2, 3, 3));
      panel.add(new JLabel("Sprint Name:"));
      JTextField sprintName = new JTextField(5);
      sprintName.setEditable(false);
      sprintName.setToolTipText("To edit - 'Manage Sprints'!");
      panel.add(sprintName);

      panel.add(new JLabel("Day in Sprint:"));
      dayInSprintTextField = new JTextField(5);
      panel.add(dayInSprintTextField);
      
      panel.add(new JLabel("Length of Sprint:"));
      lengthOfSprintTextField = new JTextField(5);
      panel.add(lengthOfSprintTextField);

      Sprint currentSprint = sprintCache.getCurrentSprint();
      sprintName.setText(currentSprint.getName());
      dayInSprintTextField.setText(currentSprint.calculateDayInSprint().toString());
      lengthOfSprintTextField.setText(currentSprint.getLength().toString());

      return panel;
   }

   public void prepareBurndown() {
      dataSeries = new XYSeries("Current Development Progression");
      idealSeries = new XYSeries("Ideal Development Progression");

      XYSeriesCollection dataset = new XYSeriesCollection();
      dataset.addSeries(dataSeries);
      dataset.addSeries(idealSeries);

      // create the chart...
      JFreeChart chart = ChartFactory.createXYLineChart("Sprint Burndown - " + dateHelper.getTodaysDateAsString(), // chart title
            "Day in Sprint", // x axis label
            "Outstanding Points", // y axis label
            dataset, // data
            PlotOrientation.VERTICAL, true, // include legend
            true, // tooltips
            false // urls
            );

      XYPlot plot = chart.getXYPlot();
      domainAxis = plot.getDomainAxis();
      domainAxis.setLowerBound(0);
      domainAxis.setUpperBound(10);

      XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
      renderer.setShapesVisible(true);
      renderer.setShapesFilled(true);

      source = new TextTitle();
      chart.addSubtitle(source);

      rangeAxis = (NumberAxis) plot.getRangeAxis();
      domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      panel = new ChartPanel(chart);
   }
}


class CalculateSprintBurndownAction extends BasicAbstractGUIAction {

   private class BurnDownDataDTO {
      private final Map<String, JiraBurndownStat> jiras;
      double remainingDevEstimates = 0d;
      double totalDevEstimates = 0d;
      double totalQaEstimates = 0d;
      private Set<String> jiraProjects = new HashSet<String>();

      protected BurnDownDataDTO(Map<String, JiraBurndownStat> jiras) {
         this.jiras = jiras;
      }

      public void calculateBurndownData() {
         for (JiraBurndownStat statrow : jiras.values()) {

            if (statrow.isToIncludeInTotals()) {
               totalDevEstimates += statrow.getDevEstimate();
               totalQaEstimates += statrow.getQaEstimate();

               String jiraProject = PlannerHelper.getProjectKey(statrow.getJira());
               if (!jiraProjects.contains(jiraProject)) {
                  jiraProjects.add(jiraProject);
               }

               if (statrow.isPreDevProgress()) {
                  remainingDevEstimates += statrow.getDevEstimate();
               } else if (statrow.isInDevProgress()) {
                  remainingDevEstimates += statrow.getRemainingDevEstimate();
               }
            }
         }
      }

      public double getRemainingDevEstimates() {
         return remainingDevEstimates;
      }

      public double getTotalDevEstimates() {
         return totalDevEstimates;
      }

      public Set<String> getJiraProjects() {
         return jiraProjects;
      }

   }

   private class JiraBurndownStat {
      private String jira;
      private double devEstimate = 0d;
      private double remainingDevEstimate = 0d;
      private double qaEstimate = 0d;

      private boolean isInDevProgress = false;
      private boolean isPreDevProgress = false;
      private boolean isToIncludeInTotals = true;

      public JiraBurndownStat(String jira, MyTable boardTable, int row) {
         this.jira = jira;
         this.devEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(Column.DEst, row));
         this.qaEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(Column.QEst, row));

         BoardStatusValue boardStatus = (BoardStatusValue) boardTable.getValueAt(Column.BoardStatus, row);

         switch (boardStatus) {
         case Open:
            this.isPreDevProgress = true;
            break;
         case NA:
         case UnKnown:
            this.isPreDevProgress = true;
            this.isToIncludeInTotals = false;
            break;
         case Bug:
            this.isPreDevProgress = true;
            break;
         case InProgress:
            this.remainingDevEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(Column.DRem, row));
            this.isInDevProgress = true;
            break;
         case Approved:
         case Complete:
         case ForShowCase:
         case Resolved:
            break;
         }

         Object isOld = boardTable.getValueAt(Column.Old, row);
         if (isOld != null && Boolean.TRUE == isOld) {
            isToIncludeInTotals = false;
         }

      }

      public boolean isToIncludeInTotals() {
         return isToIncludeInTotals;
      }

      public String getJira() {
         return jira;
      }

      public double getDevEstimate() {
         return devEstimate;
      }

      public double getQaEstimate() {
         return qaEstimate;
      }

      public double getRemainingDevEstimate() {
         return remainingDevEstimate;
      }

      public boolean isPreDevProgress() {
         return isPreDevProgress;
      }

      public boolean isInDevProgress() {
         return isInDevProgress;
      }
   }

   private class JiraStatsDataDTO {

      private Set<String> duplicateJiras;
      private Map<String, JiraBurndownStat> jiras;
      private final MyTable sourceTable;

      public JiraStatsDataDTO(MyTable sourceTable) {
         this.sourceTable = sourceTable;
      }

      public void calculateJiraStats() {
         int rows = sourceTable.getRowCount();
         jiras = new HashMap<String, JiraBurndownStat>();
         duplicateJiras = new HashSet<String>();
         System.out.println("calculating Jira Stats");
         for (int row = 0; row < rows; row++) {
            String jira = (String) sourceTable.getValueAt(Column.Jira, row);
            if (!jiras.containsKey(jira)) {
               JiraBurndownStat statRow = new JiraBurndownStat(jira, sourceTable, row);
               jiras.put(jira, statRow);
            } else {
               duplicateJiras.add(jira);
            }
         }
      }

      public Set<String> getDuplicateJiras() {
         return duplicateJiras;
      }

      public Map<String, JiraBurndownStat> getJiras() {
         return jiras;
      }

   }

   private final SprintBurndownGrapher grapher;

   private final MyTable sourceTable;

   public CalculateSprintBurndownAction(String name, String description, Frame parentFrame, MyTable sourceTable, SprintBurndownGrapher grapher) {
      super(name, description, parentFrame);
      this.sourceTable = sourceTable;
      this.grapher = grapher;
   }

   private void alertOnDuplicateJirasIfTheraAreAny(Set<String> duplicateJiras) {
      if (duplicateJiras.size() > 0) {
         StringBuffer sb = new StringBuffer();
         for (String duplicateJira : duplicateJiras) {
            sb.append(duplicateJira).append(" ");
         }

         AlertDialog
               .alertMessage(
                     getParentFrame(),
                     "Duplicate Jiras!",
                     "The following jiras have been found more than once in the board Panel. The first instance of the jiras will be included in the graph. \n\nTo ensure correct burndown, delete the duplicate jiras and ensure the data in the Board Panel is correct",
                     sb.toString());
      }
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      JiraStatsDataDTO jiraStatsDataDTO = new JiraStatsDataDTO(sourceTable);
      jiraStatsDataDTO.calculateJiraStats();

      BurnDownDataDTO burnDownDataDTO = new BurnDownDataDTO(jiraStatsDataDTO.getJiras());
      burnDownDataDTO.calculateBurndownData();

      alertOnDuplicateJirasIfTheraAreAny(jiraStatsDataDTO.getDuplicateJiras());
      grapher.calculateAndPrintBurndown(burnDownDataDTO.getTotalDevEstimates(), burnDownDataDTO.getRemainingDevEstimates(), burnDownDataDTO
            .getJiraProjects());
   }

}
