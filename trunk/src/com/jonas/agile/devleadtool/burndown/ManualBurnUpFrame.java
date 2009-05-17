package com.jonas.agile.devleadtool.burndown;

import java.awt.Component;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

import com.jonas.common.DateHelper;
import com.jonas.common.swing.SwingUtil;

public class ManualBurnUpFrame extends AbstractManualBurnFrame {

   private DefaultTableXYDataset seriesCollectionForBurnUp;

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

   public void createNewSeriesAndAddToCollection(String categoryName, List<BurnDataColumn> burndownDays) {
      XYSeries newSeries = new XYSeries(categoryName, true, false);
      System.out.println(" new Series " + categoryName);
      for (BurnDataColumn burnDownDay : burndownDays) {
         System.out.println(" new value: " + burnDownDay.getX());
         newSeries.add(burnDownDay.getX(), burnDownDay.getY());
      }

      seriesCollectionForBurnUp.addSeries(newSeries);
   }

   public void clearAllSeries() {
      System.out.println(" clearing series!");
      seriesCollectionForBurnUp.removeAllSeries();
   }

   public void setRendererPaints(AbstractRenderer renderer) {
      int row = 0;
      renderer.setSeriesPaint(row++, SwingUtil.cellGreen);
      renderer.setSeriesPaint(row++, SwingUtil.cellBlue);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightBlue);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightRed);
      renderer.setSeriesPaint(row++, SwingUtil.cellWhite);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightYellow);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightGreen);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightYellow);
   }

   @Override
   public JFreeChart getChart() {
      seriesCollectionForBurnUp = new DefaultTableXYDataset();
      return ChartFactory.createStackedXYAreaChart("Sprint Burndown" + (dateHelper != null ? " - " + dateHelper.getTodaysDateAsString() : ""), // chart title
            "Day in Sprint", // x axis label
            "Completed Points", // y axis label
            seriesCollectionForBurnUp, // data
            PlotOrientation.VERTICAL, true, // include legend
            true, // tooltips
            false // urls
            );
   }
}
