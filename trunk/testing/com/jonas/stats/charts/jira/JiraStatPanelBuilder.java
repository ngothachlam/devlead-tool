package com.jonas.stats.charts.jira;

import java.util.Collections;
import java.util.List;

import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.XYDataset;

import com.jonas.common.swing.SwingUtil;
import com.jonas.jira.JiraStatus;
import com.jonas.stats.charts.common.ChartStatPanelBuilder;
import com.jonas.stats.charts.common.PointInTimeAgreggator;
import com.jonas.stats.charts.common.PointsInTimeFacadeAbstract;

public class JiraStatPanelBuilder extends ChartStatPanelBuilder<JiraStatus>{
   
   public JiraStatPanelBuilder(boolean aggregate, PointsInTimeFacadeAbstract<?, ? extends RegularTimePeriod> dataSetAggregator) {
      super(aggregate, dataSetAggregator);
   }

   public XYDataset createDatasetFromTimeAggregator(PointsInTimeFacadeAbstract dataSetAggregator) {
      TimeTableXYDataset dataset = new TimeTableXYDataset();
      List<PointInTimeAgreggator> days = dataSetAggregator.getPointInTimes();
      Collections.sort(days);
      for (PointInTimeAgreggator dayAgreggator : days) {
         RegularTimePeriod day = dayAgreggator.getDay();
         addDataSet(dataset, JiraStatus.Open, dayAgreggator, day);
         addDataSet(dataset, JiraStatus.ReOpened, dayAgreggator, day);
         addDataSet(dataset, JiraStatus.InProgress, dayAgreggator, day);
         addDataSet(dataset, JiraStatus.Resolved, dayAgreggator, day);
         addDataSet(dataset, JiraStatus.Closed, dayAgreggator, day);
      }
      return dataset;
   }

   @Override
   public void setColors(StackedXYBarRenderer renderer) {
      renderer.setSeriesPaint(0, SwingUtil.cellLightGrey);
      renderer.setSeriesPaint(1, SwingUtil.cellRed);
      renderer.setSeriesPaint(2, SwingUtil.cellLightYellow);
      renderer.setSeriesPaint(3, SwingUtil.cellBlue);
      renderer.setSeriesPaint(4, SwingUtil.cellGreen);
   }
}
