package com.jonas.stats.charts.common;

import java.util.HashMap;
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
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

import com.jonas.common.swing.SwingUtil;

public abstract class ChartStatPanelBuilder<A> {

   private boolean aggregate;

   private Map<A, Integer> aggregators = new HashMap<A, Integer>();
   
   private final PointsInTimeFacadeAbstract dataSetAggregator;

   private JFreeChart chart;
   public ChartStatPanelBuilder(boolean aggregate, PointsInTimeFacadeAbstract<?, ? extends RegularTimePeriod> dataSetAggregator) {
      this.aggregate = aggregate;
      this.dataSetAggregator = dataSetAggregator;
   }

   private int addAggregatedAmountAndStoreItForNext(A jiraStatus, int amount) {
      Integer aggregatedAmount = aggregators.get(jiraStatus);
      if (aggregatedAmount == null) {
         aggregatedAmount = 0;
      }
      amount = amount + aggregatedAmount;
      aggregators.put(jiraStatus, amount);
      return amount;
   }

   protected void addDataSet(TimeTableXYDataset dataset, A jiraStatus, PointInTimeAgreggator daysAgreggator, RegularTimePeriod day) {
      int amount = daysAgreggator.getAmount(jiraStatus);
      if (aggregate) {
         amount = addAggregatedAmountAndStoreItForNext(jiraStatus, amount);
      }
      dataset.add(day, amount, jiraStatus.toString());
   }

   private JFreeChart createChart(XYDataset dataset, String title, String yTitle) {
      DateAxis domainAxis = new DateAxis("Date");
      domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
      domainAxis.setLowerMargin(0.01);
      domainAxis.setUpperMargin(0.01);
      NumberAxis rangeAxis = new NumberAxis(yTitle);
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      rangeAxis.setUpperMargin(0.10); // leave some space for item labels
      StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.15);
      renderer.setDrawBarOutline(true);
      renderer.setBaseItemLabelsVisible(true);
      renderer.setShadowVisible(false);
      renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());

      setColors(renderer);

      XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
      chart = new JFreeChart(title, plot);
      chart.removeLegend();
      // chart.addSubtitle(new TextTitle("PGA Tour, 1983 to 2003"));
      LegendTitle legend = new LegendTitle(plot);
      legend.setFrame(new BlockBorder());
      legend.setPosition(RectangleEdge.BOTTOM);
      chart.addSubtitle(legend);

      return chart;

   }

   public abstract void setColors(StackedXYBarRenderer renderer);

   public JPanel createDatasetAndChartFromTimeAggregator(String chartTitle, String yTitle) {
      XYDataset dataset = createDatasetFromTimeAggregator(dataSetAggregator);
      JFreeChart chart = createChart(dataset, chartTitle, yTitle);
      return new ChartPanel(chart);
   }

   public abstract XYDataset createDatasetFromTimeAggregator(PointsInTimeFacadeAbstract dataSetAggregator);

}
