package com.jonas.stats.charts.excel;

import java.util.Collections;
import java.util.List;

import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.XYDataset;

import com.jonas.common.swing.SwingUtil;
import com.jonas.stats.charts.common.ChartStatPanelBuilder;
import com.jonas.stats.charts.common.PointInTimeAgreggator;
import com.jonas.stats.charts.common.PointsInTimeFacadeAbstract;

public class ExcelStatPanelBuilder extends ChartStatPanelBuilder<StatChartCategory>{
   
   public ExcelStatPanelBuilder(boolean aggregate, PointsInTimeFacadeAbstract<?, ? extends RegularTimePeriod> dataSetAggregator) {
      super(aggregate, dataSetAggregator);
   }

   public XYDataset createDatasetFromTimeAggregator(PointsInTimeFacadeAbstract dataSetAggregator) {
      TimeTableXYDataset dataset = new TimeTableXYDataset();
      List<PointInTimeAgreggator> days = dataSetAggregator.getPointInTimes();
      Collections.sort(days);
      for (PointInTimeAgreggator dayAgreggator : days) {
         RegularTimePeriod day = dayAgreggator.getDay();
         addDataSet(dataset, StatChartCategory.REQUESTS, dayAgreggator, day);
      }
      return dataset;
   }

   @Override
   public void setColors(StackedXYBarRenderer renderer) {
      renderer.setSeriesPaint(0, SwingUtil.cellBlue);
   }
}
