package com.jonas.stats.charts.excel;

import java.io.IOException;

import javax.swing.JPanel;

import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraStatus;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;
import com.jonas.jira.jirastat.criteria.JiraCriteriaBuilder;
import com.jonas.stats.charts.common.ChartStatPanelBuilder;
import com.jonas.stats.charts.common.CommonTimeDenominatorStyle;
import com.jonas.stats.charts.common.DateRetriever;
import com.jonas.stats.charts.common.LowestCommonDenominatorRegularTime;
import com.jonas.stats.charts.common.PointsInTimeFacade;
import com.jonas.stats.charts.common.PointsInTimeFacadeAbstract;

public class ExcelStatChartTester extends ApplicationFrame {

   JiraClient jiraClient = JiraClient.JiraClientAolBB;
   private JiraCriteriaBuilder criteriabuilder = new JiraCriteriaBuilder();

   public ExcelStatChartTester(String title) {
      super(title);

      CommonTimeDenominatorStyle style = CommonTimeDenominatorStyle.hour;
      boolean aggregate = true;

      try {
         jiraClient.login();

         JiraProject project = JiraProject.TALK;
         jiraClient.cacheJiraVersionsForProject(project);
         JiraVersion fixVersion = project.getFixVersion("Talk v26.0");

//         JiraCriteriaBuilder criteria = criteriabuilder.fixVersion(project, fixVersion);
         JiraCriteriaBuilder criteria = criteriabuilder.deliveryBetween("-2w", "+1w").project(JiraProject.LLUDEVSUP);

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

   private PointsInTimeFacadeAbstract<JiraStatus, RegularTimePeriod> getData(JiraIssue[] jiras, DateRetriever dateRetriever, CommonTimeDenominatorStyle style) {
      PointsInTimeFacade<JiraStatus, RegularTimePeriod> dataSetAggregator = new PointsInTimeFacade<JiraStatus, RegularTimePeriod>();
      for (JiraIssue jiraIssue : jiras) {
         JiraStatus jiraStatus = JiraStatus.getJiraStatusByName(jiraIssue.getStatus());
         RegularTimePeriod timeRetriever = dateRetriever.retrieveTimeLineDateFromJira(jiraIssue);
         LowestCommonDenominatorRegularTime denominator = new LowestCommonDenominatorRegularTime(timeRetriever, style);
         dataSetAggregator.addPointInTime(jiraStatus, denominator);
      }
      return dataSetAggregator;
   }

   public JPanel createChartPanel(PointsInTimeFacadeAbstract<JiraStatus, ? extends RegularTimePeriod> dataSetAggregator, boolean aggregate) {
      ChartStatPanelBuilder panelBuilder = new ChartStatPanelBuilder(aggregate, dataSetAggregator);
      return panelBuilder.createDatasetAndChartFromTimeAggregator();
   }

   /**
    * Starting point for the demonstration application.
    * 
    * @param args
    *           ignored.
    */
   public static void main(String[] args) {
      ExcelStatChartTester demo = new ExcelStatChartTester("JiraStat Chart Tester");
      demo.setVisible(true);
   }

}

class CreationHourRetriever implements DateRetriever<JiraIssue>{
   @Override
   public Hour retrieveTimeLineDateFromJira(JiraIssue jiraIssue) {
      return jiraIssue.getCreationHour();
   }
}

class DeliveryHourRetriever implements DateRetriever<JiraIssue> {
   @Override
   public Hour retrieveTimeLineDateFromJira(JiraIssue jiraIssue) {
      Hour deliveryDateAsDay = jiraIssue.getDeliveryHour();
      return deliveryDateAsDay;
   }
}
class CreationDayRetriever implements DateRetriever<JiraIssue>{
   @Override
   public Day retrieveTimeLineDateFromJira(JiraIssue jiraIssue) {
      return jiraIssue.getCreationDay();
   }
}

class DeliveryDateRetriever implements DateRetriever<JiraIssue> {
   @Override
   public Day retrieveTimeLineDateFromJira(JiraIssue jiraIssue) {
      Day deliveryDateAsDay = jiraIssue.getDeliveryDay();
      return deliveryDateAsDay;
   }
}
