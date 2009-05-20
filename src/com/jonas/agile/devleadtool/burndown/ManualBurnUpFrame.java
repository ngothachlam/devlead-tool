package com.jonas.agile.devleadtool.burndown;

import java.awt.Component;

import com.jonas.common.DateHelper;
import com.jonas.common.swing.SwingUtil;

public class ManualBurnUpFrame extends ManualBurnFrame {

   public static void main(String[] args) {
      ManualBurnFrame frame = new ManualBurnUpFrame(null, null, new BurnDataRetriever() {

         BurnData data;

         @Override
         public void calculateBurndownData() {
            data = new BurnData(BurnType.BurnUp);
            
            Category category = new Category("Complete", SwingUtil.cellGreen, 0);
            data.add(category, 0d, 0d);
            data.add(category, 1d, 1d);
            data.add(category, 2d, 2d);

            Category category2 = new Category("Resolved", SwingUtil.cellBlue, 1);
            data.add(category2, 0d, 0d);
            data.add(category2, 1d, 1d);
            data.add(category2, 2d, 2d);

            Category category3 = new Category("InProgress", SwingUtil.cellLightYellow, 2);
            data.add(category3, 0d, 0d);
            data.add(category3, 1d, 1d);
            data.add(category3, 2d, 1d);

            Category category4 = new Category("Failed", SwingUtil.cellRed, 3);
            data.add(category4, 0d, 0d);
            data.add(category4, 1d, 1d);
            data.add(category4, 2d, 0d);

            Category category5 = new Category("Open", SwingUtil.cellWhite, 4);
            data.add(category5, 0d, 3d);
            data.add(category5, 1d, 2d);
            data.add(category5, 2d, 1d);

            Category category6 = new Category("Datafixes completed", SwingUtil.cellLightRed, 5);
            double dataFixes = 0d;
            data.add(category6, 0d, dataFixes += 2d);
            data.add(category6, 1d, dataFixes += 1d);
            data.add(category6, 2d, dataFixes += 2d);
         }

         @Override
         public BurnData getBurnData() {
            return data;
         }
      });
      frame.setVisible(true);
   }

   private ManualBurnUpFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever) {
      super(parent, dateHelper, retriever, false);
   }

}
