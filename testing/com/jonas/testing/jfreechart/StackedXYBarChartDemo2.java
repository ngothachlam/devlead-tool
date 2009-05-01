/* ---------------------------
 * StackedXYBarChartDemo2.java
 * ---------------------------
 * (C) Copyright 2005, 2007, by Object Refinery Limited.
 *
 */

package com.jonas.testing.jfreechart;

import java.awt.Color;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

import com.jonas.jira.JiraStatus;
import com.jonas.jira.access.listener.JiraListener.JiraAccessUpdate;

/**
 * A simple stacked bar chart using time series data.
 */
public class StackedXYBarChartDemo2 extends ApplicationFrame {

   /**
    * Creates a new demo.
    * 
    * @param title
    *           the frame title.
    */
   public StackedXYBarChartDemo2(String title) {
      super(title);
      DataSetAggregator dataSetAggregator = new DataSetAggregator();
      dataSetAggregator.add(new Day(2, 5, 2009), JiraStatus.Open);
      dataSetAggregator.add(new Day(2, 5, 2009), JiraStatus.Open);
      dataSetAggregator.add(new Day(3, 5, 2009), JiraStatus.ReOpened);
      dataSetAggregator.add(new Day(3, 5, 2009), JiraStatus.Resolved);
      dataSetAggregator.add(new Day(4, 5, 2009), JiraStatus.Open);
      JPanel chartPanel = createDemoPanel(dataSetAggregator);
      chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
      setContentPane(chartPanel);
   }

   /**
    * Creates a sample dataset.
    * 
    * @return a sample dataset.
    */
   private static TableXYDataset createDataset(DataSetAggregator dataSetAggregator) {
      TimeTableXYDataset dataset = new TimeTableXYDataset();
      // FIXME 1 - this is the stacked example to use!!
      Collection<DaysAgreggator> days = dataSetAggregator.getDays();
      for (DaysAgreggator daysAgreggator : days) {
         Day day = daysAgreggator.getDay();
         addDataSet(dataset, JiraStatus.Open, daysAgreggator, day);
         addDataSet(dataset, JiraStatus.ReOpened, daysAgreggator, day);
         addDataSet(dataset, JiraStatus.InProgress, daysAgreggator, day);
         addDataSet(dataset, JiraStatus.Resolved, daysAgreggator, day);
         addDataSet(dataset, JiraStatus.Closed, daysAgreggator, day);
      }
      return dataset;
   }

   private static void addDataSet(TimeTableXYDataset dataset, JiraStatus jiraStatus, DaysAgreggator daysAgreggator, Day day) {
      int amount = daysAgreggator.getAmount(jiraStatus);
      System.out.println("adding amount of " + amount + " for " + jiraStatus + " on " + day);
      addDataset(dataset, jiraStatus, day, amount);
   }

   private static void addDataset(TimeTableXYDataset dataset, JiraStatus jiraStatus, Day day, int amount) {
      dataset.add(day, amount, jiraStatus.toString());
   }

   /**
    * Creates a sample chart.
    * 
    * @param dataset
    *           the dataset for the chart.
    * 
    * @return a sample chart.
    */
   private static JFreeChart createChart(TableXYDataset dataset) {
      DateAxis domainAxis = new DateAxis("Date");
      domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
      domainAxis.setLowerMargin(0.01);
      domainAxis.setUpperMargin(0.01);
      NumberAxis rangeAxis = new NumberAxis("Number of Jiras");
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      rangeAxis.setUpperMargin(0.10); // leave some space for item labels
      StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.15);
      renderer.setDrawBarOutline(false);
      renderer.setBaseItemLabelsVisible(true);
      renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
      renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER));
      renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0} : {1} = {2}", new SimpleDateFormat("yyyy"), new DecimalFormat("0")));

      XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
      JFreeChart chart = new JFreeChart("Jira States compared against their Creation Date", plot);
      chart.removeLegend();
      // chart.addSubtitle(new TextTitle("PGA Tour, 1983 to 2003"));
      chart.setTextAntiAlias(RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
      LegendTitle legend = new LegendTitle(plot);
      legend.setBackgroundPaint(Color.white);
      legend.setFrame(new BlockBorder());
      legend.setPosition(RectangleEdge.BOTTOM);
      chart.addSubtitle(legend);

      ChartUtilities.applyCurrentTheme(chart);

      return chart;

   }

   /**
    * Creates a panel for the demo (used by SuperDemo.java).
    * 
    * @param dataSetAggregator
    * 
    * @return A panel.
    */
   public static JPanel createDemoPanel(DataSetAggregator dataSetAggregator) {
      JFreeChart chart = createChart(createDataset(dataSetAggregator));
      return new ChartPanel(chart);
   }

   /**
    * Starting point for the demonstration application.
    * 
    * @param args
    *           ignored.
    */
   public static void main(String[] args) {
      StackedXYBarChartDemo2 demo = new StackedXYBarChartDemo2("JFreeChart: Stacked XY Bar Chart Demo 2");
      demo.pack();
      RefineryUtilities.centerFrameOnScreen(demo);
      demo.setVisible(true);
   }

}
