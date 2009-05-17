package com.jonas.testing.jfreechart.demos;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class OverlaidXYPlotDemo1 extends ApplicationFrame {

   public OverlaidXYPlotDemo1(String s) {
      super(s);
      JPanel jpanel = createDemoPanel();
      jpanel.setPreferredSize(new Dimension(500, 270));
      setContentPane(jpanel);
   }

   private static JFreeChart createChart() {
      DateAxis dateaxis = new DateAxis("Date");
      dateaxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
      NumberAxis numberaxis = new NumberAxis("Value");
      
      XYPlot xyplot = new XYPlot(null, dateaxis, numberaxis, null);
      xyplot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
      
      JFreeChart freeChart = new JFreeChart("Overlaid XYPlot Demo 1", JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
      
      //Do the rest...
      List<XYDataset> xyDatasets = new ArrayList<XYDataset>();
      xyDatasets.add(createDataset1());
      xyDatasets.add(createDataset2());
      
      for (int counter = 0; counter < xyDatasets.size(); counter++) {
         xyplot.setDataset(counter, xyDatasets.get(counter));
      }
      
      List<XYItemRenderer> xyItemRenderers = new ArrayList<XYItemRenderer>();
      xyItemRenderers.add(new XYBarRenderer());
      xyItemRenderers.add(new StandardXYItemRenderer());
      
      for (int counter = 0; counter < xyItemRenderers.size(); counter++) {
         XYItemRenderer itemRenderer = xyItemRenderers.get(counter);
         xyplot.setRenderer(counter, itemRenderer);
      }
      
      return freeChart;
   }

   private static XYDataset createDataset1() {
      TimeSeries timeseries = new TimeSeries("Series 1", org.jfree.data.time.Day.class);
      timeseries.add(new Day(1, 3, 2002), 12353.299999999999D);
      timeseries.add(new Day(2, 3, 2002), 13734.4D);
      timeseries.add(new Day(3, 3, 2002), 14525.299999999999D);
      timeseries.add(new Day(4, 3, 2002), 13984.299999999999D);
      timeseries.add(new Day(5, 3, 2002), 12999.4D);
      timeseries.add(new Day(6, 3, 2002), 14274.299999999999D);
      timeseries.add(new Day(7, 3, 2002), 15943.5D);
      timeseries.add(new Day(8, 3, 2002), 14845.299999999999D);
      timeseries.add(new Day(9, 3, 2002), 14645.4D);
      timeseries.add(new Day(10, 3, 2002), 16234.6D);
      timeseries.add(new Day(11, 3, 2002), 17232.299999999999D);
      timeseries.add(new Day(12, 3, 2002), 14232.200000000001D);
      timeseries.add(new Day(13, 3, 2002), 13102.200000000001D);
      timeseries.add(new Day(14, 3, 2002), 14230.200000000001D);
      timeseries.add(new Day(15, 3, 2002), 11235.200000000001D);
      return new TimeSeriesCollection(timeseries);
   }

   private static XYDataset createDataset2() {
      TimeSeries timeseries = new TimeSeries("Series 2", org.jfree.data.time.Day.class);
      timeseries.add(new Day(3, 3, 2002), 16853.200000000001D);
      timeseries.add(new Day(4, 3, 2002), 19642.299999999999D);
      timeseries.add(new Day(5, 3, 2002), 18253.5D);
      timeseries.add(new Day(6, 3, 2002), 15352.299999999999D);
      timeseries.add(new Day(7, 3, 2002), 13532D);
      timeseries.add(new Day(8, 3, 2002), 12635.299999999999D);
      timeseries.add(new Day(9, 3, 2002), 13998.200000000001D);
      timeseries.add(new Day(10, 3, 2002), 11943.200000000001D);
      timeseries.add(new Day(11, 3, 2002), 16943.900000000001D);
      timeseries.add(new Day(12, 3, 2002), 17843.200000000001D);
      timeseries.add(new Day(13, 3, 2002), 16495.299999999999D);
      timeseries.add(new Day(14, 3, 2002), 17943.599999999999D);
      timeseries.add(new Day(15, 3, 2002), 18500.700000000001D);
      timeseries.add(new Day(16, 3, 2002), 19595.900000000001D);
      return new TimeSeriesCollection(timeseries);
   }

   public static JPanel createDemoPanel() {
      JFreeChart jfreechart = createChart();
      return new ChartPanel(jfreechart);
   }

   public static void main(String args[]) {
      OverlaidXYPlotDemo1 overlaidxyplotdemo1 = new OverlaidXYPlotDemo1("Overlaid XYPlot Demo");
      overlaidxyplotdemo1.pack();
      RefineryUtilities.centerFrameOnScreen(overlaidxyplotdemo1);
      overlaidxyplotdemo1.setVisible(true);
   }
}
