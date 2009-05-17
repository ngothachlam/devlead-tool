package com.jonas.agile.devleadtool.burndown;

import java.awt.Component;

import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataset;

import com.jonas.common.DateHelper;
import com.jonas.common.swing.SwingUtil;

public class ManualBurnUpFrame extends AbstractManualBurnFrame {

   public static void main(String[] args) {
      AbstractManualBurnFrame frame = new ManualBurnUpFrame(null, null, new BurnDataRetriever() {

         BurnDataCategory data;

         @Override
         public void calculateBurndownData() {
            data = new BurnDataCategory();
            CategoryType category = new CategoryType("Closed",BurnType.BurnUp);
            data.add(category, 0d, 0d);
            data.add(category, 1d, 1d);
            data.add(category, 2d, 2d);

            CategoryType category2 = new CategoryType("Resolved",BurnType.BurnUp);
            data.add(category2, 0d, 0d);
            data.add(category2, 1d, 1d);
            data.add(category2, 2d, 2d);

            CategoryType category3 = new CategoryType("In-Progress",BurnType.BurnUp);
            data.add(category3, 0d, 0d);
            data.add(category3, 1d, 1d);
            data.add(category3, 2d, 1d);

            CategoryType category4 = new CategoryType("Failed",BurnType.BurnUp);
            data.add(category4, 0d, 0d);
            data.add(category4, 1d, 1d);
            data.add(category4, 2d, 0d);

            CategoryType category5 = new CategoryType("Open",BurnType.BurnUp);
            data.add(category5, 0d, 3d);
            data.add(category5, 1d, 2d);
            data.add(category5, 2d, 1d);

            CategoryType category6 = new CategoryType("Datafixes completed",BurnType.BurnUp);
            double dataFixes = 0d;
            data.add(category6, 0d, dataFixes += 2d);
            data.add(category6, 1d, dataFixes += 1d);
            data.add(category6, 2d, dataFixes += 2d);
         }

         @Override
         public BurnDataCategory getBurnData() {
            return data;
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


   @Override
   public XYItemRenderer getRenderer() {
      return new StackedXYAreaRenderer2();
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
