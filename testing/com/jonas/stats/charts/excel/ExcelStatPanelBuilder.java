package com.jonas.stats.charts.excel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

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
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

import com.jonas.common.swing.SwingUtil;
import com.jonas.stats.charts.common.PointInTimeAgreggator;
import com.jonas.stats.charts.common.PointsInTimeFacadeAbstract;

public class ExcelStatPanelBuilder {

   private boolean aggregate;

   private Map<StatChartCategory, Integer> aggregators = new HashMap<StatChartCategory, Integer>();
   
   private final PointsInTimeFacadeAbstract dataSetAggregator;
   public ExcelStatPanelBuilder(boolean aggregate, PointsInTimeFacadeAbstract dataSetAggregator) {
      this.aggregate = aggregate;
      this.dataSetAggregator = dataSetAggregator;
   }

   private int addAggregatedAmountAndStoreItForNext(StatChartCategory jiraStatus, int amount) {
      Integer aggregatedAmount = aggregators.get(jiraStatus);
      if (aggregatedAmount == null) {
         aggregatedAmount = 0;
      }
      amount = amount + aggregatedAmount;
      
      System.out.println("adding " + amount + " to " + jiraStatus);
      
      aggregators.put(jiraStatus, amount);
      return amount;
   }

   private void addDataSet(TimeTableXYDataset dataset, StatChartCategory jiraStatus, PointInTimeAgreggator daysAgreggator, RegularTimePeriod day) {
      int amount = daysAgreggator.getAmount(jiraStatus);
      if (aggregate) {
         amount = addAggregatedAmountAndStoreItForNext(jiraStatus, amount);
      }
      System.out.println("adding to dataset day: " + day + " amount of " + amount + " to category " + jiraStatus.toString());
      dataset.add(day, amount, jiraStatus.toString());
   }

   private JFreeChart createChart(XYDataset dataset) {
      DateAxis domainAxis = new DateAxis("Date");
      domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
      domainAxis.setLowerMargin(0.01);
      domainAxis.setUpperMargin(0.01);
      NumberAxis rangeAxis = new NumberAxis("Number of Requests");
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      rangeAxis.setUpperMargin(0.10); // leave some space for item labels
      StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.15);
      renderer.setDrawBarOutline(true);
      renderer.setBaseItemLabelsVisible(true);
      renderer.setShadowVisible(false);
      renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());

      renderer.setSeriesPaint(0, SwingUtil.cellGreen);

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

   public JPanel createDatasetAndChartFromTimeAggregator() {
      XYDataset dataset = createDatasetFromTimeAggregator();
      JFreeChart chart = createChart(dataset);
      return new ChartPanel(chart);
   }

   private XYDataset createDatasetFromTimeAggregator() {
      TimeTableXYDataset dataset = new TimeTableXYDataset();
      List<PointInTimeAgreggator> days = dataSetAggregator.getPointInTimes();
      Collections.sort(days);
      for (PointInTimeAgreggator dayAgreggator : days) {
         RegularTimePeriod day = dayAgreggator.getDay();
         addDataSet(dataset, StatChartCategory.REQUESTS, dayAgreggator, day);
      }
      return dataset;
   }

}
