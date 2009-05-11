/* ---------------------------
 * StackedXYBarChartDemo2.java
 * ---------------------------
 * (C) Copyright 2005, 2007, by Object Refinery Limited.
 *
 */

package com.jonas.testing.jfreechart;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;

import com.jonas.common.swing.SwingUtil;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraStatus;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;

/**
 * A simple stacked bar chart using time series data.
 */
public class StackedXYBarChartDemo2 extends ApplicationFrame {

   JiraClient jiraClient = JiraClient.JiraClientAolBB;

   /**
    * Creates a new demo.
    * 
    * @param title
    *           the frame title.
    */
   public StackedXYBarChartDemo2(String title) {
      super(title);

      DataSetAggregator dataSetAggregator = new DataSetAggregator();
      try {
         jiraClient.login();

         boolean isWeek = false;
         boolean aggregate = true;

//         JiraIssue[] jiras = jiraClient.getJirasFromProject(JiraProject.LLU, getCreatedBetweenCriteria("-5w", "+1w"));
//         JiraIssue[] jiras = jiraClient.getJirasFromProject(JiraProject.LLU, getCreatedBetweenCriteria("-10w", "+1w"),"&type=45");
         JiraIssue[] jiras = jiraClient.getJirasFromProject(JiraProject.LLUDEVSUP, getDeliveryBetweenCriteria("-2w", "+1w"));
         
         
         for (JiraIssue jiraIssue : jiras) {
            JiraStatus jiraStatus = JiraStatus.getJiraStatusByName(jiraIssue.getStatus());
//            dataSetAggregator.add(jiraIssue.getCreationDay(), jiraStatus, isWeek);
            dataSetAggregator.add(jiraIssue.getDeliveryDateAsDay(), jiraStatus, isWeek);
         }

         // setupTestData(dataSetAggregator, isWeek);

         JPanel chartPanel = createDemoPanel(dataSetAggregator, aggregate);
         chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
         System.out.println("creating chartPanel");
         setContentPane(chartPanel);

      } catch (HttpException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (JiraException e) {
         e.printStackTrace();
      } catch (JDOMException e) {
         e.printStackTrace();
      }

   }

   private String getDeliveryBetweenCriteria(String first, String later) {
      return "&customfield_10188%3Aprevious=" + first + "&customfield_10188%3Anext=" + later;
   }

   private String getCreatedBetweenCriteria(String first, String later) {
      return "&created%3Aprevious=" + first + "&created%3Anext=" + later;
   }

   private void setupTestData(DataSetAggregator dataSetAggregator, boolean isWeek) {
      dataSetAggregator.add(getJiraIssue("Mon, 5 Apr 2009 18:28:25 +0000 (GMT)").getCreationDay(), JiraStatus.Open, isWeek);

      dataSetAggregator.add(getJiraIssue("Mon, 6 Apr 2009 18:28:25 +0000 (GMT)").getCreationDay(), JiraStatus.Open, isWeek);
      dataSetAggregator.add(getJiraIssue("Mon, 7 Apr 2009 18:28:25 +0000 (GMT)").getCreationDay(), JiraStatus.Open, isWeek);
      dataSetAggregator.add(getJiraIssue("Mon, 8 Apr 2009 18:28:25 +0000 (GMT)").getCreationDay(), JiraStatus.ReOpened, isWeek);
      dataSetAggregator.add(getJiraIssue("Mon, 9 Apr 2009 18:28:25 +0000 (GMT)").getCreationDay(), JiraStatus.InProgress, isWeek);
      dataSetAggregator.add(getJiraIssue("Mon, 10 Apr 2009 18:28:25 +0000 (GMT)").getCreationDay(), JiraStatus.Closed, isWeek);
      dataSetAggregator.add(getJiraIssue("Mon, 11 Apr 2009 18:28:25 +0000 (GMT)").getCreationDay(), JiraStatus.ReOpened, isWeek);
      dataSetAggregator.add(getJiraIssue("Mon, 12 Apr 2009 18:28:25 +0000 (GMT)").getCreationDay(), JiraStatus.Closed, isWeek);

      dataSetAggregator.add(getJiraIssue("Mon, 13 Apr 2009 18:28:25 +0000 (GMT)").getCreationDay(), JiraStatus.Open, isWeek);
      dataSetAggregator.add(getJiraIssue("Mon, 14 Apr 2009 18:28:25 +0000 (GMT)").getCreationDay(), JiraStatus.ReOpened, isWeek);
      dataSetAggregator.add(getJiraIssue("Mon, 15 Apr 2009 18:28:25 +0000 (GMT)").getCreationDay(), JiraStatus.Resolved, isWeek);
      dataSetAggregator.add(getJiraIssue("Mon, 16 Apr 2009 18:28:25 +0000 (GMT)").getCreationDay(), JiraStatus.Closed, isWeek);

      dataSetAggregator.add(getJiraIssue("Mon, 5 Apr 2009 18:28:25 +0000 (GMT)").getCreationDay(), JiraStatus.Closed, isWeek);
   }

   private JiraIssue getJiraIssue(String date) {
      return new JiraIssue("", "", "", "", "", date);
   }

   /**
    * Creates a sample dataset.
    * 
    * @return a sample dataset.
    */
   private XYDataset createDataset(DataSetAggregator dataSetAggregator, boolean aggregate) {
      TimeTableXYDataset dataset = new TimeTableXYDataset();
      // FIXME 1 - this is the stacked example to use!!
      List<DaysAgreggator> days = dataSetAggregator.getDays();
      Collections.sort(days);
      for (DaysAgreggator daysAgreggator : days) {
         Day day = daysAgreggator.getDay();
         addDataSet(dataset, JiraStatus.Open, daysAgreggator, day, aggregate);
         addDataSet(dataset, JiraStatus.ReOpened, daysAgreggator, day, aggregate);
         addDataSet(dataset, JiraStatus.InProgress, daysAgreggator, day, aggregate);
         addDataSet(dataset, JiraStatus.Resolved, daysAgreggator, day, aggregate);
         addDataSet(dataset, JiraStatus.Closed, daysAgreggator, day, aggregate);
      }
      return dataset;
   }

   private Map<JiraStatus, Integer> aggregators = new HashMap<JiraStatus, Integer>();

   private void addDataSet(TimeTableXYDataset dataset, JiraStatus jiraStatus, DaysAgreggator daysAgreggator, Day day, boolean aggregate) {
      int amount = daysAgreggator.getAmount(jiraStatus);
      Integer aggregator = 0;
      if (aggregate) {
         aggregator = aggregators.get(jiraStatus);
         if (aggregator == null) {
            aggregator = 0;
            aggregators.put(jiraStatus, amount);
         } else {
            aggregators.put(jiraStatus, amount + aggregator);
         }
      }

      System.out.println("amount for " + jiraStatus + " (on " + day + ") is " + amount + " and the aggregate before this was " + aggregator);

      addDataset(dataset, jiraStatus, day, amount + aggregator);
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
   private static JFreeChart createChart(XYDataset dataset) {
      DateAxis domainAxis = new DateAxis("Date");
      domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
      domainAxis.setLowerMargin(0.01);
      domainAxis.setUpperMargin(0.01);
      NumberAxis rangeAxis = new NumberAxis("Number of Jiras");
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      rangeAxis.setUpperMargin(0.10); // leave some space for item labels
      StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.15);
      renderer.setDrawBarOutline(true);
      renderer.setBaseItemLabelsVisible(true);
      renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());

      renderer.setSeriesPaint(0, SwingUtil.cellWhite);
      renderer.setSeriesPaint(1, SwingUtil.cellRed);
      renderer.setSeriesPaint(2, SwingUtil.cellLightYellow);
      renderer.setSeriesPaint(3, SwingUtil.cellBlue);
      renderer.setSeriesPaint(4, SwingUtil.cellGreen);
      renderer.setSeriesPaint(4, SwingUtil.cellGreen);

      XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
      JFreeChart chart = new JFreeChart("Jira States compared against their Creation Date", plot);
      chart.removeLegend();
      // chart.addSubtitle(new TextTitle("PGA Tour, 1983 to 2003"));
      LegendTitle legend = new LegendTitle(plot);
      legend.setFrame(new BlockBorder());
      legend.setPosition(RectangleEdge.BOTTOM);
      chart.addSubtitle(legend);

      return chart;

   }

   /**
    * Creates a panel for the demo (used by SuperDemo.java).
    * 
    * @param dataSetAggregator
    * @param isWeek
    * 
    * @return A panel.
    */
   public JPanel createDemoPanel(DataSetAggregator dataSetAggregator, boolean aggregate) {
      XYDataset createDataset = createDataset(dataSetAggregator, aggregate);
      JFreeChart chart = createChart(createDataset);
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
