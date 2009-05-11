/* ---------------------------
 * StackedXYBarChartDemo2.java
 * ---------------------------
 * (C) Copyright 2005, 2007, by Object Refinery Limited.
 *
 */

package com.jonas.testing.jirastat;

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
public class JiraStatChartTester extends ApplicationFrame {

   JiraClient jiraClient = JiraClient.JiraClientAolBB;

   /**
    * Creates a new demo.
    * 
    * @param title
    *           the frame title.
    */
   public JiraStatChartTester(String title) {
      super(title);

      boolean isWeek = false;
      boolean aggregate = true;
      
      try {
         jiraClient.login();

         // JiraIssue[] jiras = jiraClient.getJirasFromProject(JiraProject.LLU, getCreatedBetweenCriteria("-5w", "+1w"));
         // JiraIssue[] jiras = jiraClient.getJirasFromProject(JiraProject.LLU, getCreatedBetweenCriteria("-10w", "+1w"),"&type=45");
         JiraIssue[] jiras = jiraClient.getJirasFromProject(JiraProject.LLUDEVSUP, getDeliveryBetweenCriteria("-2w", "+1w"));

         DateRetriever dateRetriever = new DeliveryDateRetriever();
         PointsInTimeFacade data = getData(jiras, dateRetriever, isWeek);

         JPanel chartPanel = getChartPanel(data, aggregate);
         chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

         setContentPane(chartPanel);
         pack();
         RefineryUtilities.centerFrameOnScreen(this);
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

   private PointsInTimeFacade getData(JiraIssue[] jiras, DateRetriever dateRetriever, boolean isWeek) {
      PointsInTimeFacade dataSetAggregator = new PointsInTimeFacade(isWeek);
      for (JiraIssue jiraIssue : jiras) {
         JiraStatus jiraStatus = JiraStatus.getJiraStatusByName(jiraIssue.getStatus());
         dataSetAggregator.addPointInTime(dateRetriever.retrieveTimeLineDateFromJira(jiraIssue), jiraStatus);
      }
      return dataSetAggregator;
   }

   private String getDeliveryBetweenCriteria(String first, String later) {
      return "&customfield_10188%3Aprevious=" + first + "&customfield_10188%3Anext=" + later;
   }

   private String getCreatedBetweenCriteria(String first, String later) {
      return "&created%3Aprevious=" + first + "&created%3Anext=" + later;
   }

   public JPanel getChartPanel(PointsInTimeFacade dataSetAggregator, boolean aggregate) {
      JiraStatPanelBuilder panelBuilder = new JiraStatPanelBuilder(aggregate, dataSetAggregator);
      return panelBuilder.createDatasetAndChartFromTimeAggregator();
   }

   /**
    * Starting point for the demonstration application.
    * 
    * @param args
    *           ignored.
    */
   public static void main(String[] args) {
      JiraStatChartTester demo = new JiraStatChartTester("JiraStat Chart Tester");
      demo.setVisible(true);
   }

}

class CreationDateRetriever implements DateRetriever {
   @Override
   public Day retrieveTimeLineDateFromJira(JiraIssue jiraIssue) {
      return jiraIssue.getCreationDay();
   }
}

class DeliveryDateRetriever implements DateRetriever {
   @Override
   public Day retrieveTimeLineDateFromJira(JiraIssue jiraIssue) {
      return jiraIssue.getDeliveryDateAsDay();
   }
}
