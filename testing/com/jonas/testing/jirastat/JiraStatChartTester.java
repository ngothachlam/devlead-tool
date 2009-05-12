/* ---------------------------
 * StackedXYBarChartDemo2.java
 * ---------------------------
 * (C) Copyright 2005, 2007, by Object Refinery Limited.
 *
 */

package com.jonas.testing.jirastat;

import java.io.IOException;

import javax.swing.JPanel;

import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;
import org.jfree.data.time.Day;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraStatus;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;
import com.jonas.testing.jirastat.criterias.JiraCriteriaBuilder;
import com.jonas.testing.jirastat.criterias.JiraHttpCriteria;

/**
 * A simple stacked bar chart using time series data.
 */
public class JiraStatChartTester extends ApplicationFrame {

   JiraClient jiraClient = JiraClient.JiraClientAolBB;
   private JiraCriteriaBuilder criteriabuilder = new JiraCriteriaBuilder();

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

         jiraClient.cacheJiraVersionsForProject(JiraProject.TALK);
         JiraVersion fixVersion = JiraProject.TALK.getFixVersion("Talk v26.0");
         JiraHttpCriteria criteria = criteriabuilder.fixVersion(JiraProject.TALK, fixVersion).getCriteria();
//         JiraHttpCriteria criteria = criteriabuilder.deliveryBetween("-2w", "+1w").project(JiraProject.LLUDEVSUP).getCriteria();
         
         JiraIssue[] jiras = jiraClient.getJiras(criteria);

         DateRetriever dateRetriever = new CreationDateRetriever();
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
      Day deliveryDateAsDay = jiraIssue.getDeliveryDateAsDay();
      return deliveryDateAsDay;
   }
}
