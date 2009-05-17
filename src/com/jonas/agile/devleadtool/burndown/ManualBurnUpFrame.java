package com.jonas.agile.devleadtool.burndown;

import java.awt.Component;
import java.util.List;

import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

import com.jonas.common.DateHelper;
import com.jonas.common.swing.SwingUtil;

public class ManualBurnUpFrame extends AbstractManualBurnFrame {

   public static void main(String[] args) {
      AbstractManualBurnFrame frame = new ManualBurnUpFrame(null, null, new BurnDataRetriever() {

         BurnDataCategory data;

         @Override
         public void calculateBurndownData() {
            data = new BurnDataCategory();
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

         @Override
         public BurnDataCategory getBurnData() {
            return data;
         }
      }, true);
      frame.setVisible(true);
   }

   private DefaultTableXYDataset seriesCollectionForBurnUp;

   public ManualBurnUpFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever) {
      this(parent, dateHelper, retriever, false);
   }

   public ManualBurnUpFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever, boolean closeOnExit) {
      super(parent, dateHelper, retriever, closeOnExit);
   }

   public void clearAllSeries() {
      System.out.println(" clearing series!");
      seriesCollectionForBurnUp.removeAllSeries();
   }

   @Override
   public XYItemRenderer getRenderer() {
      return new StackedXYAreaRenderer2();
   }

   @Override
   public XYDataset getXyDataset() {
      seriesCollectionForBurnUp = seriesCollectionForBurnUp == null ? new DefaultTableXYDataset() : seriesCollectionForBurnUp;
      return seriesCollectionForBurnUp;
   }

   @Override
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
}
