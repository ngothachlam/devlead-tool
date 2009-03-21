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
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import com.jonas.agile.devleadtool.burndown.SprintBurndownGrapher;
import com.jonas.agile.devleadtool.component.Action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.DateHelper;
import com.jonas.common.string.StringHelper;

public class BoardStatsFrame extends AbstractBasicFrame implements SprintBurndownGrapher {

   private XYSeries dataSeries;
   private JTextField dayInSprintTextField;
   private ValueAxis domainAxis;
   private JTextField lengthOfSprintTextField;
   private ChartPanel panel;
   private final MyTable sourceTable;

   public BoardStatsFrame(Component parent, int width, int height, MyTable sourceTable) {
      super(parent, width, height);
      this.sourceTable = sourceTable;
      this.prepareBurndown();
   }

   public void calculateAndPrintBurndown(double totalDevEstimates, double remainingDevEstimates) {
      domainAxis.setLowerBound(0d);
      dataSeries.clear();
      dataSeries.add(0, totalDevEstimates);
      dataSeries.add(StringHelper.getDouble(dayInSprintTextField.getText()), remainingDevEstimates);
      domainAxis.setUpperBound(StringHelper.getDouble(lengthOfSprintTextField.getText()));
   }

   @Override
   public Container getMyPanel() {
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.add(getTopPanel(), BorderLayout.NORTH);
      mainPanel.add(panel, BorderLayout.CENTER);
      return mainPanel;
   }

   private Component getTopPanel() {
      JPanel panel = new JPanel(new GridLayout(3, 2, 3, 3));
      panel.add(new JLabel("Day in Sprint: "));
      dayInSprintTextField = new JTextField(5);
      panel.add(dayInSprintTextField);
      panel.add(new JLabel("Length of Sprint: "));
      lengthOfSprintTextField = new JTextField(5);
      panel.add(lengthOfSprintTextField);
      panel.add(new JLabel(""));
      panel.add(new JButton(new CalculateSprintBurndownAction("Calculate Burndown", "Calculate Burndown", this, sourceTable, this)));
      panel.setBorder(BorderFactory.createTitledBorder(""));
      return panel;
   }

   public void prepareBurndown() {
      dataSeries = new XYSeries("Remaining Dev Estimates");

      XYSeriesCollection dataset = new XYSeriesCollection();
      dataset.addSeries(dataSeries);

      // create the chart...
      JFreeChart chart = ChartFactory.createXYLineChart("Sprint Burndown - " + DateHelper.getTodaysDateAsString(), // chart title
            "Day in Sprint", // x axis label
            "Points", // y axis label
            dataset, // data
            PlotOrientation.VERTICAL, false, // include legend
            true, // tooltips
            false // urls
            );

      XYPlot plot = chart.getXYPlot();
      domainAxis = plot.getDomainAxis();
      domainAxis.setLowerBound(0);
      domainAxis.setUpperBound(10);

      // change the auto tick unit selection to integer units only...
      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
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

      protected BurnDownDataDTO(Map<String, JiraStat> jiras) {
         this.jiras = jiras;
      }

      public void calculateBurndownData() {
         for (JiraStat statrow : jiras.values()) {
            totalDevEstimates += statrow.getDevEstimate();
            totalQaEstimates += statrow.getQaEstimate();
            
            if (statrow.isInDevProgress()) {
               remainingDevEstimates += statrow.getRemainingDevEstimate();
            } else if (statrow.hasNotStartedDev()) {
               remainingDevEstimates += statrow.getDevEstimate();
            }
         }
      }

      public double getRemainingDevEstimates() {
         return remainingDevEstimates;
      }

      public double getTotalDevEstimates() {
         return totalDevEstimates;
      }

   }
   private class JiraStat {
      private double devEstimate = 0d;
      private boolean isInDevProgress;
      private boolean isPreDevProgress;
      private String jira;
      private double qaEstimate = 0d;
      private double remainingDevEstimate = 0d;

      public JiraStat(String jira, MyTable boardTable, int row) {
         this.jira = jira;
         this.devEstimate = StringHelper.getDouble(boardTable.getValueAt(Column.Dev_Estimate, row));
         this.qaEstimate = StringHelper.getDouble(boardTable.getValueAt(Column.QA_Estimate, row));
         this.qaEstimate = StringHelper.getDouble(boardTable.getValueAt(Column.Dev_Remain, row));
         Object valueAt = boardTable.getValueAt(Column.BoardStatus, row);
         this.isInDevProgress = BoardStatusValue.InDevProgress.equals(valueAt);

         BoardStatusValue boardStatus = (BoardStatusValue) valueAt;
         switch (boardStatus) {
         case Open:
         case Parked:
            this.isPreDevProgress = true;
            break;
         default:
            this.isPreDevProgress = false;
            break;
         }
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

      public boolean hasNotStartedDev() {
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
      grapher.calculateAndPrintBurndown(burnDownDataDTO.getTotalDevEstimates(), burnDownDataDTO.getRemainingDevEstimates());
   }

}
