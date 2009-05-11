package com.jonas.testing.jfreechart.demos;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class TestChart {

   private TestChart() {
      XYSeries series1 = new XYSeries("Remaining Dev Estimates");
      series1.add(0, 20);
      series1.add(1, 19);
      series1.add(2, 16);

      XYSeriesCollection dataset = new XYSeriesCollection();
      dataset.addSeries(series1);

      // create a chart...
      // create the chart...
      JFreeChart chart = ChartFactory.createXYLineChart("Burndown of Current Sprint", // chart title
            "Day in Sprint", // x axis label
            "Points", // y axis label
            dataset, // data
            PlotOrientation.VERTICAL, false, // include legend
            true, // tooltips
            false // urls
            );

      XYPlot plot = chart.getXYPlot();
      ValueAxis domainAxis = plot.getDomainAxis();
      domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      domainAxis.setLowerBound(0);
      domainAxis.setUpperBound(10);

      // change the auto tick unit selection to integer units only...
      NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      // create and display a frame...
      ChartFrame frame = new ChartFrame("Test", chart);
      frame.pack();
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      TestChart testChart = new TestChart();
   }

}
