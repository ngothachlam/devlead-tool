package com.jonas.agile.devleadtool.component.frame;

import java.awt.Component;
import java.awt.Container;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class BoardStatsFrame extends AbstractBasicFrame{

   private ChartPanel panel;

   public BoardStatsFrame(Component parent, int width, int height) {
      super(parent, width, height);
      
      calculateData();
   }

   private void calculateData() {
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

      panel = new ChartPanel(chart);
   }

   @Override
   public Container getMyPanel() {
      return panel;
   }

}
