package com.jonas.agile.devleadtool.burndown;

import java.awt.Component;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.jonas.common.DateHelper;
import com.jonas.common.swing.SwingUtil;

public class ManualBurnDownFrame extends AbstractManualBurnFrame {

   private XYSeriesCollection seriesCollection;

   public static void main(String[] args) {
      ManualBurnDownFrame frame = new ManualBurnDownFrame(null, null, new BurnDataRetriever() {

         BurnData data;

         @Override
         public BurnData getBurnData() {
            return data;
         }

         @Override
         public void calculateBurndownData() {
            data = new BurnData();
            data.add("Real Progression", 0d, 15d + 7d);
            data.add("Real Progression", 1d, 16d + 7d);
            data.add("Real Progression", 2d, 16d + 5d);
            data.add("Real Progression", 3d, 13d + 3d);
            data.add("Real Progression", 4d, 13d + 1.5d);
            data.add("Real Progression", 5d, 13d + 0d);
            data.add("Real Progression", 6d, 4d + 7d);
            data.add("Real Progression", 7d, 2d + 2d);
            data.add("Real Progression", 8d, 2d + 4d);
            data.add("Real Progression", 9d, 1.75d + 3d);
            data.add("Real Progression", 10d, 1.75d + 2d);

            data.add("Ideal Progression", 0d, 15d + 7d);
            data.add("Ideal Progression", 10d, 0d);
        }

      }, true);
      frame.setVisible(true);
   }

   public ManualBurnDownFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever) {
      this(parent, dateHelper, retriever, false);
   }

   public ManualBurnDownFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever, boolean closeOnExit) {
      super(parent, dateHelper, retriever, closeOnExit);
   }

   @Override
   public void prepareBurndown() {
      seriesCollection = new XYSeriesCollection();

      // create the chart...
      JFreeChart chart = ChartFactory.createXYLineChart("Sprint Burndown" + (dateHelper != null ? " - " + dateHelper.getTodaysDateAsString() : ""), // chart title
            "Day in Sprint", // x axis label
            "Outstanding Points", // y axis label
            seriesCollection, // data
            PlotOrientation.VERTICAL, true, // include legend
            true, // tooltips
            false // urls
            );

      XYPlot plot = chart.getXYPlot();
      xAxis = plot.getDomainAxis();
      xAxis.setLowerBound(0);
      xAxis.setUpperBound(10);

      XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

      int row = 0;
      renderer.setSeriesPaint(row++, SwingUtil.cellBlue);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightBlue);
      renderer.setSeriesPaint(row++, SwingUtil.cellRed);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightRed);
      renderer.setSeriesPaint(row++, SwingUtil.cellGreen);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightGreen);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightYellow);

      renderer.setShapesVisible(true);
      renderer.setShapesFilled(true);

      source = new TextTitle();
      chart.addSubtitle(source);

      yAxis = (NumberAxis) plot.getRangeAxis();
      xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      panel = new ChartPanel(chart);
   }

   public void createNewSeriesAndAddToCollection(String categoryName, List<BurnDataColumn> burndownDays) {
      XYSeries newSeries = new XYSeries(categoryName);
      newSeries.setKey(categoryName);
   
      for (BurnDataColumn burnDownDay : burndownDays) {
         newSeries.add(burnDownDay.getX(), burnDownDay.getY());
      }
   
      seriesCollection.addSeries(newSeries);
   }

   public void clearAllSeries() {
      seriesCollection.removeAllSeries();
   }

}
