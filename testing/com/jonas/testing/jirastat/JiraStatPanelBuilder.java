package com.jonas.testing.jirastat;

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
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

import com.jonas.common.swing.SwingUtil;
import com.jonas.jira.JiraStatus;

public class JiraStatPanelBuilder {

   private static void addDataset(TimeTableXYDataset dataset, JiraStatus jiraStatus, Day day, int amount) {
      dataset.add(day, amount, jiraStatus.toString());
   }

   private boolean aggregate;
   private final DataTimeAggregator dataSetAggregator;
   private Map<JiraStatus, Integer> aggregators = new HashMap<JiraStatus, Integer>();

   public JiraStatPanelBuilder(boolean aggregate, DataTimeAggregator dataSetAggregator) {
      this.aggregate = aggregate;
      this.dataSetAggregator = dataSetAggregator;
   }

   private void addDataSet(TimeTableXYDataset dataset, JiraStatus jiraStatus, DataDayAgreggator daysAgreggator, Day day) {
      int amount = daysAgreggator.getAmount(jiraStatus);
      if (aggregate) {
         Integer aggregator = aggregators.get(jiraStatus);
         if (aggregator == null) {
            aggregator = 0;
         }
         amount = amount + aggregator;
         aggregators.put(jiraStatus, amount);
      }

      System.out.println("amount for " + jiraStatus + " (on " + day + ") is " + amount);

      addDataset(dataset, jiraStatus, day, amount);
   }

   private JFreeChart createChart(XYDataset dataset) {
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

   private XYDataset createDatasetFromTimeAggregator() {
      TimeTableXYDataset dataset = new TimeTableXYDataset();
      // FIXME 1 - this is the stacked example to use!!
      List<DataDayAgreggator> days = dataSetAggregator.getDays();
      Collections.sort(days);
      for (DataDayAgreggator dayAgreggator : days) {
         Day day = dayAgreggator.getDay();
         addDataSet(dataset, JiraStatus.Open, dayAgreggator, day);
         addDataSet(dataset, JiraStatus.ReOpened, dayAgreggator, day);
         addDataSet(dataset, JiraStatus.InProgress, dayAgreggator, day);
         addDataSet(dataset, JiraStatus.Resolved, dayAgreggator, day);
         addDataSet(dataset, JiraStatus.Closed, dayAgreggator, day);
      }
      return dataset;
   }

   public JPanel createDatasetAndChartFromTimeAggregator() {
      XYDataset dataset = createDatasetFromTimeAggregator();
      JFreeChart chart = createChart(dataset);
      return new ChartPanel(chart);
   }

}
