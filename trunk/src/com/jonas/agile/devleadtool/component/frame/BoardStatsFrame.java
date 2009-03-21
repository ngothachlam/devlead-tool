package com.jonas.agile.devleadtool.component.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
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
import com.jonas.agile.devleadtool.burndown.SprintBurndownGpapher;
import com.jonas.agile.devleadtool.component.Action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.string.StringHelper;

public class BoardStatsFrame extends AbstractBasicFrame implements SprintBurndownGpapher {

   private ChartPanel panel;
   private Component dayInSprintTextField;
   private Component lengthOfSprintTextField;
   private final MyTable sourceTable;
   private XYSeries series1;

   public BoardStatsFrame(Component parent, int width, int height, MyTable sourceTable) {
      super(parent, width, height);
      this.sourceTable = sourceTable;
      this.prepareBurndown();
   }

   public void calculateAndPrintBurndown(boolean data) {
      series1.clear();
      series1.add(0, 20);
      series1.add(1, 19);
      series1.add(2, 16);
   }

   public void prepareBurndown() {
      series1 = new XYSeries("Remaining Dev Estimates");

      XYSeriesCollection dataset = new XYSeriesCollection();
      dataset.addSeries(series1);

      // create a chart...
      // create the chart...
      JFreeChart chart = ChartFactory.createXYLineChart("Burndown of Current Sprint", // chart title
            "Day in Sprint", // x axis label
            "Points", // y axis label
            dataset, // data
            PlotOrientation.VERTICAL, false, // include legend
            true, // tooltips
            false // urls
            );

      XYPlot plot = chart.getXYPlot();
      ValueAxis domainAxis = plot.getDomainAxis();
      domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      domainAxis.setLowerBound(0);
      domainAxis.setUpperBound(10);

      // change the auto tick unit selection to integer units only...
      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      panel = new ChartPanel(chart);
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
      dayInSprintTextField = panel.add(new JTextField(5));
      panel.add(new JLabel("Length of Sprint: "));
      lengthOfSprintTextField = panel.add(new JTextField(5));
      panel.add(new JLabel(""));
      panel.add(new JButton(new CalculateSprintBurndownAction("Calculate Burndown", "Calculate Burndown", this, sourceTable, this)));
      panel.setBorder(BorderFactory.createTitledBorder(""));
      return panel;
   }
}


class CalculateSprintBurndownAction extends BasicAbstractGUIAction {

   private final MyTable sourceTable;
   private final SprintBurndownGpapher grapher;

   public CalculateSprintBurndownAction(String name, String description, Frame parentFrame, MyTable sourceTable, SprintBurndownGpapher grapher) {
      super(name, description, parentFrame);
      this.sourceTable = sourceTable;
      this.grapher = grapher;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      int rows = sourceTable.getRowCount();
      Map<String, StatRow> jiras = new HashMap<String, StatRow>();
      for (int row = 0; row < rows; row++) {
         String jira = (String) sourceTable.getValueAt(Column.Jira, row);
         if (!jiras.containsKey(jira)) {
            StatRow statRow = new StatRow(jira, sourceTable, row);
            jiras.put(jira, statRow);
         }
      }

      StringBuffer sb = new StringBuffer();
      double totalDevEstimates = 0d;
      double remainingDevEstimates = 0d;
      double totalQaEstimates = 0d;
      for (StatRow statrow : jiras.values()) {
         sb.append(statrow.jira).append(": ");
         totalDevEstimates += statrow.getDevEstimate();
         sb.append(" total Dev Est: ").append(statrow.getDevEstimate());
         totalQaEstimates += statrow.getQaEstimate();
         sb.append(" total QA Est: ").append(statrow.getQaEstimate());

         if (statrow.isInDevProgress()) {
            remainingDevEstimates += statrow.getRemainingDevEstimate();
            sb.append(" remaining Dev Est: ").append(statrow.getRemainingDevEstimate());
         } else if (statrow.hasNotStartedDev()) {
            remainingDevEstimates += statrow.getDevEstimate();
            sb.append(" remaining Dev Est: ").append(statrow.getDevEstimate());
         } else {
            sb.append(" remaining Dev Est: ").append(0d);
         }
         sb.append("\n");
      }
      sb.append("Total ").append("Dev Est: ").append(totalDevEstimates).append(", QA Est: ").append(totalQaEstimates);
      sb.append("\n").append("Remaining Dev Est: ").append(remainingDevEstimates);

//      BasicMessageFrame basicMessageFrame = new BasicMessageFrame(getParentFrame(), sb.toString());
//      basicMessageFrame.setVisible(true);

      grapher.calculateAndPrintBurndown(true);
   }

   private class StatRow {
      private double devEstimate = 0d;
      private double remainingDevEstimate = 0d;
      private double qaEstimate = 0d;
      private String jira;
      private boolean isInDevProgress;
      private boolean isPreDevProgress;

      public StatRow(String jira, MyTable boardTable, int row) {
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

      public double getQaEstimate() {
         return qaEstimate;
      }

      public boolean hasNotStartedDev() {
         return isPreDevProgress;
      }

      public double getRemainingDevEstimate() {
         return remainingDevEstimate;
      }

      public boolean isInDevProgress() {
         return isInDevProgress;
      }

      public double getDevEstimate() {
         return devEstimate;
      }
   }

}
