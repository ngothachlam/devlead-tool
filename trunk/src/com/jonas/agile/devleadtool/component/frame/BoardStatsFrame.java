package com.jonas.agile.devleadtool.component.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
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
import com.jonas.agile.devleadtool.component.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.DateHelper;
import com.jonas.common.string.StringHelper;

public class BoardStatsFrame extends AbstractBasicFrame implements SprintBurndownGrapher {

   private XYSeries dataSeries;
   // private XYSeries prognosisSeries;
   private XYSeries idealSeries;
   private JTextField dayInSprintTextField;
   private ValueAxis domainAxis;
   private JTextField lengthOfSprintTextField;
   private ChartPanel panel;
   private final MyTable sourceTable;
   private DateHelper dateHelper;
   private TextTitle source;
   private NumberAxis rangeAxis;
   private LegendTitle legend;

   public BoardStatsFrame(Component parent, int width, int height, MyTable sourceTable, DateHelper dateHelper) {
      super(parent, width, height);
      this.sourceTable = sourceTable;
      this.dateHelper = dateHelper;
      this.prepareBurndown();
   }

   public void calculateAndPrintBurndown(double totalDevEstimates, double remainingDevEstimates, Set<String> projects) {
      source.setText(StringHelper.getNiceString(projects));
      dataSeries.clear();
      // prognosisSeries.clear();
      idealSeries.clear();

      rangeAxis.setAutoRange(true);
      domainAxis.setLowerBound(0d);
      dataSeries.add(0d, totalDevEstimates);

      double dayInSprint = StringHelper.getDouble(dayInSprintTextField.getText());
      double lengthOfSprint = StringHelper.getDouble(lengthOfSprintTextField.getText());

      dataSeries.add(dayInSprint, remainingDevEstimates);
      domainAxis.setUpperBound(Math.max(lengthOfSprint, dayInSprint) + 0.2d);

      if (dayInSprint < lengthOfSprint) {
         double prognosisChangePerDay = ((totalDevEstimates - remainingDevEstimates) / dayInSprint);
         double prognosisDays = lengthOfSprint - dayInSprint;
         // prognosisSeries.add(dayInSprint, remainingDevEstimates);
         // prognosisSeries.add(lengthOfSprint, remainingDevEstimates - (prognosisChangePerDay * prognosisDays));
      }
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
      JPanel panel = new JPanel(new GridLayout(2, 2, 3, 3));
      panel.add(new JLabel("Day in Sprint: "));
      dayInSprintTextField = new JTextField(5);
      panel.add(dayInSprintTextField);
      panel.add(new JLabel("Length of Sprint: "));
      lengthOfSprintTextField = new JTextField(5);
      panel.add(lengthOfSprintTextField);
      return panel;
   }

   public void prepareBurndown() {
      dataSeries = new XYSeries("Current Development Progression");
      // prognosisSeries = new XYSeries("Prognosed Dev Estimates");
      idealSeries = new XYSeries("Ideal Development Progression");

      XYSeriesCollection dataset = new XYSeriesCollection();
      dataset.addSeries(dataSeries);
      // dataset.addSeries(prognosisSeries);
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
      legend = chart.getLegend();
      
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
      private final Map<String, JiraStat> jiras;
      double remainingDevEstimates = 0d;
      double totalDevEstimates = 0d;
      double totalQaEstimates = 0d;
      private Set<String> jiraProjects = new HashSet<String>();

      protected BurnDownDataDTO(Map<String, JiraStat> jiras) {
         this.jiras = jiras;
      }

      public void calculateBurndownData() {
         for (JiraStat statrow : jiras.values()) {

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

   private class JiraStat {
      private String jira;
      private double devEstimate = 0d;
      private double remainingDevEstimate = 0d;
      private double qaEstimate = 0d;

      private boolean isInDevProgress = false;
      private boolean isPreDevProgress = false;
      private boolean isToIncludeInTotals = true;

      public JiraStat(String jira, MyTable boardTable, int row) {
         this.jira = jira;
         this.devEstimate = StringHelper.getDouble(boardTable.getValueAt(Column.Dev_Estimate, row));
         this.qaEstimate = StringHelper.getDouble(boardTable.getValueAt(Column.QA_Estimate, row));

         BoardStatusValue boardStatus = (BoardStatusValue) boardTable.getValueAt(Column.BoardStatus, row);

         switch (boardStatus) {
         case Open:
         case NA:
            isToIncludeInTotals = false;
         case UnKnown:
            isToIncludeInTotals = false;
         case Bug:
            this.isPreDevProgress = true;
            break;
         case InDevProgress:
            this.remainingDevEstimate = StringHelper.getDouble(boardTable.getValueAt(Column.Dev_Remain, row));
            this.isInDevProgress = true;
            break;
         case Approved:
         case Complete:
         case ForShowCase:
         case InQAProgress:
         case Resolved:
            break;
         }

         Object isOld = boardTable.getValueAt(Column.Old, row);
         if (isOld != null && Boolean.TRUE == isOld) {
            isToIncludeInTotals = false;
         }

         System.out.print(jira);
         System.out.print(" devEst: " + devEstimate);
         System.out.print(" remDevEst: " + remainingDevEstimate);
         System.out.print(" qaEst: " + qaEstimate);
         System.out.print(" [isToIncludeInTotals: " + isToIncludeInTotals);
         System.out.print(", isPreDevProgress: " + isPreDevProgress);
         System.out.print(", isInDevProgress: " + isInDevProgress);
         System.out.println("]");
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
      private Map<String, JiraStat> jiras;
      private final MyTable sourceTable;

      public JiraStatsDataDTO(MyTable sourceTable) {
         this.sourceTable = sourceTable;
      }

      public void calculateJiraStats() {
         int rows = sourceTable.getRowCount();
         jiras = new HashMap<String, JiraStat>();
         duplicateJiras = new HashSet<String>();
         System.out.println("calculating Jira Stats");
         for (int row = 0; row < rows; row++) {
            String jira = (String) sourceTable.getValueAt(Column.Jira, row);
            if (!jiras.containsKey(jira)) {
               JiraStat statRow = new JiraStat(jira, sourceTable, row);
               jiras.put(jira, statRow);
            } else {
               duplicateJiras.add(jira);
            }
         }
      }

      public Set<String> getDuplicateJiras() {
         return duplicateJiras;
      }

      public Map<String, JiraStat> getJiras() {
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
