package com.jonas.agile.devleadtool.burndown;

import java.awt.Component;

import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
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
            CategoryType string = new CategoryType("Real Progression",BurnType.BurnDown);
            data.add(string, 0d, 15d + 7d);
            data.add(string, 1d, 16d + 7d);
            data.add(string, 2d, 16d + 5d);
            data.add(string, 3d, 13d + 3d);
            data.add(string, 4d, 13d + 1.5d);
            data.add(string, 5d, 13d + 0d);
            data.add(string, 6d, 4d + 7d);
            data.add(string, 7d, 2d + 2d);
            data.add(string, 8d, 2d + 4d);
            data.add(string, 9d, 1.75d + 3d);
            data.add(string, 10d, 1.75d + 2d);

            CategoryType string2 = new CategoryType("Ideal Progression",BurnType.BurnDown);
            data.add(string2, 0d, 15d + 7d);
            data.add(string2, 10d, 0d);
         }

         @Override
         public BurnDataCategory getBurnData() {
            return data;
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
   public XYItemRenderer getRenderer() {
      return new XYLineAndShapeRenderer(true, false);
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
