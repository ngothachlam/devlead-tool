package com.jonas.agile.devleadtool.gui.component.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
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
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.jonas.agile.devleadtool.burndown.SprintBurndownGrapher;
import com.jonas.agile.devleadtool.gui.burndown.CalculateSprintBurndownAction;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.DateHelper;
import com.jonas.common.string.StringHelper;
import com.jonas.common.swing.SwingUtil;

public class BoardStatsFrame extends AbstractBasicFrame implements SprintBurndownGrapher {

   private XYSeries idealProgression;
   private XYSeries estimateProgression;

   private JTextField dayInSprintTextField;
   private JTextField lengthOfSprintTextField;

   private ValueAxis xAxis;
   private NumberAxis yAxis;
   
   private ChartPanel panel;
   private final MyTable sourceTable;
   private DateHelper dateHelper;
   private TextTitle source;
   private final SprintCache sprintCache;

   public BoardStatsFrame(Component parent, int width, int height, MyTable sourceTable, DateHelper dateHelper, SprintCache sprintCache) {
      super(parent, width, height);
      this.sourceTable = sourceTable;
      this.dateHelper = dateHelper;
      this.sprintCache = sprintCache;
      this.prepareBurndown();
   }

   public void calculateAndPrintBurndown(double totalEstimates, double remainingEstimates, Set<String> projects, Comparable estimateKey) {
      source.setText(StringHelper.getNiceString(projects));
      idealProgression.clear();
      estimateProgression.clear();
      
      estimateProgression.setKey(estimateKey);

      double dayInSprint = StringHelper.getDoubleOrZero(dayInSprintTextField.getText());
      double lengthOfSprint = StringHelper.getDoubleOrZero(lengthOfSprintTextField.getText());

      idealProgression.add(0, totalEstimates);
      idealProgression.add(lengthOfSprint, 0);

      estimateProgression.add(0d, totalEstimates);
      estimateProgression.add(dayInSprint, remainingEstimates);

      xAxis.setLowerBound(0d);
      xAxis.setUpperBound(Math.max(lengthOfSprint, dayInSprint) + 0.2d);

      yAxis.setAutoRange(true);
      yAxis.setLowerBound(0d);

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
      sprintName.setText(currentSprint == null ? "<NONE DEFINED>" : currentSprint.getName());
      dayInSprintTextField.setText(currentSprint != null ? currentSprint.calculateDayInSprint().toString() : "");
      lengthOfSprintTextField.setText(currentSprint != null ? currentSprint.getLength().toString() : "");

      return panel;
   }

   public void prepareBurndown() {
      estimateProgression = new XYSeries("Current Progression");
      idealProgression = new XYSeries("Ideal Progression");

      XYSeriesCollection dataset = new XYSeriesCollection();
      dataset.addSeries(estimateProgression);
      dataset.addSeries(idealProgression);

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
      xAxis = plot.getDomainAxis();
      xAxis.setLowerBound(0);
      xAxis.setUpperBound(10);

      XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

      renderer.setSeriesPaint(0, SwingUtil.cellRed);
      renderer.setSeriesPaint(1, SwingUtil.cellLightRed);
      renderer.setSeriesPaint(2, SwingUtil.cellBlue);
      renderer.setSeriesPaint(3, SwingUtil.cellLightBlue);

      renderer.setShapesVisible(true);
      renderer.setShapesFilled(true);

      source = new TextTitle();
      chart.addSubtitle(source);

      yAxis = (NumberAxis) plot.getRangeAxis();
      xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      panel = new ChartPanel(chart);
   }
}


