package com.jonas.agile.devleadtool.burndown;

import java.awt.Component;
import java.util.List;

import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.jonas.common.DateHelper;
import com.jonas.common.swing.SwingUtil;

public class ManualBurnDownFrame extends AbstractManualBurnFrame {

   public static void main(String[] args) {
      ManualBurnDownFrame frame = new ManualBurnDownFrame(null, null, new BurnDataRetriever() {

         BurnDataCategory data;

         @Override
         public void calculateBurndownData() {
            data = new BurnDataCategory();
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

         @Override
         public BurnDataCategory getBurnData() {
            return data;
         }
      }, true);
      frame.setVisible(true);
   }

   private XYSeriesCollection seriesCollectionForBurnDown;

   public ManualBurnDownFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever) {
      this(parent, dateHelper, retriever, false);
   }

   public ManualBurnDownFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever, boolean closeOnExit) {
      super(parent, dateHelper, retriever, closeOnExit);
   }

   @Override
   public void clearAllSeries() {
      seriesCollectionForBurnDown.removeAllSeries();
   }

   @Override
   public XYItemRenderer getRenderer() {
      return new XYLineAndShapeRenderer(true, false);
   }

   @Override
   public XYDataset getXyDataset() {
      seriesCollectionForBurnDown = seriesCollectionForBurnDown == null ? new XYSeriesCollection() : seriesCollectionForBurnDown;
      return seriesCollectionForBurnDown;
   }

   @Override
   public void setRendererPaints(AbstractRenderer renderer) {
      int row = 0;
      renderer.setSeriesPaint(row++, SwingUtil.cellBlue);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightBlue);
      renderer.setSeriesPaint(row++, SwingUtil.cellRed);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightRed);
      renderer.setSeriesPaint(row++, SwingUtil.cellGreen);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightGreen);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightYellow);
      ((XYLineAndShapeRenderer) renderer).setShapesVisible(true);
      ((XYLineAndShapeRenderer) renderer).setShapesFilled(true);
   }
}
