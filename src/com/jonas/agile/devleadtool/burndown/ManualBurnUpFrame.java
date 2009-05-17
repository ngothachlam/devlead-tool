package com.jonas.agile.devleadtool.burndown;

import java.awt.Component;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

import com.jonas.common.DateHelper;
import com.jonas.common.swing.SwingUtil;

public class ManualBurnUpFrame extends AbstractManualBurnFrame {

   private DefaultTableXYDataset seriesCollection;

   public static void main(String[] args) {
      ManualBurnUpFrame frame = new ManualBurnUpFrame(null, null, new BurnDataRetriever() {

         BurnData data;

         @Override
         public BurnData getBurnData() {
            return data;
         }

         @Override
         public void calculateBurndownData() {
            data = new BurnData();
            data.add("Closed", 0d, 0d);
            data.add("Closed", 1d, 1d);
            data.add("Closed", 2d, 2d);

            data.add("Resolved", 0d, 0d);
            data.add("Resolved", 1d, 1d);
            data.add("Resolved", 2d, 2d);

            data.add("In-Progress", 0d, 0d);
            data.add("In-Progress", 1d, 1d);
            data.add("In-Progress", 2d, 1d);

            data.add("Failed", 0d, 0d);
            data.add("Failed", 1d, 1d);
            data.add("Failed", 2d, 0d);

            data.add("Open", 0d, 3d);
            data.add("Open", 1d, 2d);
            data.add("Open", 2d, 1d);

            double dataFixes = 0d;
            data.add("Datafixes completed", 0d, dataFixes += 2d);
            data.add("Datafixes completed", 1d, dataFixes += 1d);
            data.add("Datafixes completed", 2d, dataFixes += 2d);
         }

      }, true);
      frame.setVisible(true);
   }

   public ManualBurnUpFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever) {
      this(parent, dateHelper, retriever, false);
   }

   public ManualBurnUpFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever, boolean closeOnExit) {
      super(parent, dateHelper, retriever, closeOnExit);
   }

   public void forEachSeriesIdentified(String categoryName, List<BurnDataColumn> burndownDays) {
      XYSeries newSeries = new XYSeries(categoryName, true, false);
   
      for (BurnDataColumn burnDownDay : burndownDays) {
         newSeries.add(burnDownDay.getX(), burnDownDay.getY());
      }
   
      seriesCollection.addSeries(newSeries);
   }

   public void prepareSeries() {
      seriesCollection.removeAllSeries();
   }
   
   public void prepareBurndown() {
      seriesCollection = new DefaultTableXYDataset();

      JFreeChart chart = ChartFactory.createStackedXYAreaChart("Sprint Burndown" + (dateHelper != null ? " - " + dateHelper.getTodaysDateAsString() : ""), // chart title
            "Day in Sprint", // x axis label
            "Outstanding Points", // y axis label
            seriesCollection, // data
            PlotOrientation.VERTICAL, true, // include legend
            true, // tooltips
            false // urls
            );

      XYPlot plot = chart.getXYPlot();
      xAxis = plot.getDomainAxis();
      xAxis.setUpperMargin(0);
      xAxis.setLowerMargin(0);

      StackedXYAreaRenderer2 renderer = (StackedXYAreaRenderer2) plot.getRenderer();

      int row = 0;
      renderer.setSeriesPaint(row++, SwingUtil.cellGreen);
      renderer.setSeriesPaint(row++, SwingUtil.cellBlue);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightBlue);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightRed);
      renderer.setSeriesPaint(row++, SwingUtil.cellWhite);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightYellow);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightGreen);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightYellow);

      source = new TextTitle();
      chart.addSubtitle(source);

      yAxis = (NumberAxis) plot.getRangeAxis();
      xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      panel = new ChartPanel(chart);
   }
}
