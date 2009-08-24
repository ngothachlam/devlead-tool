/* ---------------------------
 * StackedXYBarChartDemo2.java
 * ---------------------------
 * (C) Copyright 2005, 2007, by Object Refinery Limited.
 *
 */

package com.jonas.stats.charts.jira;

import java.io.IOException;
import javax.swing.JPanel;
import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.jonas.common.swing.SwingUtil;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraStatus;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;
import com.jonas.jira.jirastat.criteria.JiraCriteriaBuilder;
import com.jonas.stats.charts.common.ChartStatPanelBuilder;
import com.jonas.stats.charts.common.CommonTimeDenominatorStyle;
import com.jonas.stats.charts.common.DateRetriever;
import com.jonas.stats.charts.common.GraphPanelBuilder;
import com.jonas.stats.charts.common.GroupingDTO;
import com.jonas.stats.charts.common.LowestCommonDenominatorRegularTime;
import com.jonas.stats.charts.common.PointsInTimeFacade;
import com.jonas.stats.charts.common.PointsInTimeFacadeAbstract;

/**
 * A simple stacked bar chart using time series data.
 */
public class JiraStatChart extends ApplicationFrame {

   JiraClient jiraClient = JiraClient.JiraClientAolBB;
   private JiraCriteriaBuilder criteriabuilder = new JiraCriteriaBuilder();

   /**
    * Creates a new demo.
    * 
    * @param title
    *           the frame title.
    */
   public JiraStatChart(String title) {
      super(title);


      try {
         jiraClient.login();

         CommonTimeDenominatorStyle style = CommonTimeDenominatorStyle.hour;
         boolean aggregate = true;
         //JiraCriteriaBuilder criteria = getCriteria(JiraProject.TALK, "Talk v26.0");
         //JiraCriteriaBuilder criteria = getCriteria(JiraProject.LLU, "LLU 16");
         JiraCriteriaBuilder criteria = getCriteria(JiraProject.LLU, "LLU 17 - Trio Integration");
         //JiraCriteriaBuilder criteria = criteriabuilder.fixVersion(project.getFixVersion("Talk v26.0"));
         //JiraCriteriaBuilder criteria = criteriabuilder.createdBetween("-6w", "+1w").project(project);

         JiraIssue[] jiras = jiraClient.getJiras(criteria);

         DateRetriever timeRetriever = null;
         switch (style) {
         case day:
            timeRetriever = new CreationDayRetriever();
            break;
         case week:
            timeRetriever = new CreationDayRetriever();
            break;
         case hour:
            timeRetriever = new CreationHourRetriever();
            break;
         }

         PointsInTimeFacadeAbstract<JiraStatus, RegularTimePeriod> data = getData(jiras, timeRetriever, style);

         JPanel chartPanel = createChartPanel(data, aggregate);
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

   private JiraCriteriaBuilder getCriteria(JiraProject project, String fixVersion) throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException {
      jiraClient.cacheJiraVersionsForProject(project);
      JiraCriteriaBuilder criteria = criteriabuilder.fixVersion(project.getFixVersion(fixVersion));
      return criteria;
   }

   private PointsInTimeFacadeAbstract<JiraStatus, RegularTimePeriod> getData(JiraIssue[] jiras, DateRetriever dateRetriever,
         CommonTimeDenominatorStyle style) {
      PointsInTimeFacade<JiraStatus, RegularTimePeriod> dataSetAggregator = new PointsInTimeFacade<JiraStatus, RegularTimePeriod>();
      for (JiraIssue jiraIssue : jiras) {
         JiraStatus jiraStatus = JiraStatus.getJiraStatusByName(jiraIssue.getStatus());
         RegularTimePeriod timeRetriever = dateRetriever.retrieveTimeLinePointFromObject(jiraIssue);
         LowestCommonDenominatorRegularTime denominator = new LowestCommonDenominatorRegularTime(timeRetriever, style);
         dataSetAggregator.addPointInTimeWithValue(jiraStatus, denominator, 1);
      }
      return dataSetAggregator;
   }

   public JPanel createChartPanel(PointsInTimeFacadeAbstract<JiraStatus, ? extends RegularTimePeriod> dataSetAggregator, boolean aggregate) {
      GroupingDTO<JiraStatus>[] groupings = new GroupingDTO[] {
            new GroupingDTO<JiraStatus>(JiraStatus.Open, SwingUtil.cellLightGrey),
            new GroupingDTO<JiraStatus>(JiraStatus.ReOpened, SwingUtil.cellRed),
            new GroupingDTO<JiraStatus>(JiraStatus.InProgress, SwingUtil.cellLightYellow),
            new GroupingDTO<JiraStatus>(JiraStatus.Resolved, SwingUtil.cellBlue),
            new GroupingDTO<JiraStatus>(JiraStatus.Closed, SwingUtil.cellGreen) };

      // addDataSet(dataset, JiraStatus.Open, dayAgreggator, day);
      // addDataSet(dataset, JiraStatus.ReOpened, dayAgreggator, day);
      // addDataSet(dataset, JiraStatus.InProgress, dayAgreggator, day);
      // addDataSet(dataset, JiraStatus.Resolved, dayAgreggator, day);
      // addDataSet(dataset, JiraStatus.Closed, dayAgreggator, day);
      // renderer.setSeriesPaint(0, SwingUtil.cellLightGrey);
      // renderer.setSeriesPaint(1, SwingUtil.cellRed);
      // renderer.setSeriesPaint(2, SwingUtil.cellLightYellow);
      // renderer.setSeriesPaint(3, SwingUtil.cellBlue);
      // renderer.setSeriesPaint(4, SwingUtil.cellGreen);

      ChartStatPanelBuilder panelBuilder = new GraphPanelBuilder(aggregate, dataSetAggregator, groupings);
      return panelBuilder.createDatasetAndChartFromTimeAggregator("Jira States compared against their Creation Date", "Number of Jiras");
   }

   /**
    * Starting point for the demonstration application.
    * 
    * @param args
    *           ignored.
    */
   public static void main(String[] args) {
      JiraStatChart demo = new JiraStatChart("JiraStat Chart Tester");
      demo.setVisible(true);
   }

}


class CreationHourRetriever implements DateRetriever<JiraIssue> {
   @Override
   public Hour retrieveTimeLinePointFromObject(JiraIssue jiraIssue) {
      return jiraIssue.getCreationHour();
   }
}


class DeliveryHourRetriever implements DateRetriever<JiraIssue> {
   @Override
   public Hour retrieveTimeLinePointFromObject(JiraIssue jiraIssue) {
      Hour deliveryDateAsDay = jiraIssue.getDeliveryHour();
      return deliveryDateAsDay;
   }
}


class CreationDayRetriever implements DateRetriever<JiraIssue> {
   @Override
   public Day retrieveTimeLinePointFromObject(JiraIssue jiraIssue) {
      return jiraIssue.getCreationDay();
   }
}


class DeliveryDateRetriever implements DateRetriever<JiraIssue> {
   @Override
   public Day retrieveTimeLinePointFromObject(JiraIssue jiraIssue) {
      Day deliveryDateAsDay = jiraIssue.getDeliveryDay();
      return deliveryDateAsDay;
   }
}
