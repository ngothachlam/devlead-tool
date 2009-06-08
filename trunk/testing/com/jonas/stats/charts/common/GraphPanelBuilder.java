package com.jonas.stats.charts.common;

import java.util.Collections;
import java.util.List;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.XYDataset;


public class GraphPanelBuilder<A> extends ChartStatPanelBuilder<A> {

   private GroupingDTO<A>[] groupingDTOs;

   public GraphPanelBuilder(boolean aggregate, PointsInTimeFacadeAbstract<?, ? extends RegularTimePeriod> dataSetAggregator, GroupingDTO<A>... groupingDTOs) {
      super(aggregate, dataSetAggregator);
      this.groupingDTOs = groupingDTOs;
   }

   public XYDataset createDatasetFromTimeAggregator(PointsInTimeFacadeAbstract dataSetAggregator) {
      TimeTableXYDataset dataset = new TimeTableXYDataset();
      List<PointInTimeAgreggator> days = dataSetAggregator.getPointInTimes();
      Collections.sort(days);
      for (PointInTimeAgreggator dayAgreggator : days) {
         RegularTimePeriod day = dayAgreggator.getDay();
         for (GroupingDTO<A> grouping : groupingDTOs) {
            addDataSet(dataset, grouping.getValue(), dayAgreggator, day);
         }
      }
      return dataset;
   }

   @Override
   public void setColors(StackedXYBarRenderer renderer) {
      int priority = 0;
      for (GroupingDTO<A> grouping : groupingDTOs) {
         renderer.setSeriesPaint(priority++, grouping.getColor());
      }
   }
}


