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
            data = new BurnData(BurnType.BurnUp, "Estimated Points");
            
            Category category = new Category("Complete", SwingUtil.cellGreen, 0, false);
            data.add(category, 0d, 0d);
            data.add(category, 1d, 1d);
            data.add(category, 2d, 2d);

            Category category2 = new Category("Resolved", SwingUtil.cellBlue, 1, false);
            data.add(category2, 0d, 0d);
            data.add(category2, 1d, 1d);
            data.add(category2, 2d, 2d);

            Category category3 = new Category("InProgress", SwingUtil.cellLightYellow, 2, false);
            data.add(category3, 0d, 0d);
            data.add(category3, 1d, 1d);
            data.add(category3, 2d, 1d);

            Category category4 = new Category("Failed", SwingUtil.cellRed, 3, false);
            data.add(category4, 0d, 0d);
            data.add(category4, 1d, 1d);
            data.add(category4, 2d, 0d);

            Category category5 = new Category("Open", SwingUtil.cellWhite, 4, false);
            data.add(category5, 0d, 3d);
            data.add(category5, 1d, 2d);
            data.add(category5, 2d, 1d);

            Category category6 = new Category("Datafixes completed", SwingUtil.cellLightRed, 5, true);
            double fixes = 0d;
            data.add(category6, 0d, fixes += 2d);
            data.add(category6, 1d, fixes += 1d);
            data.add(category6, 2d, fixes += 2d);
            data.add(category6, 4d, fixes += 2d);
            data.add(category6, 5d, fixes = 1d);
            
            Category category7 = new Category("Prodfixes completed", SwingUtil.cellLightGreen, 6, true);
            fixes = 0d;
            data.add(category7, 0d, fixes = 2d);
            data.add(category7, 1d, fixes = 1d);
            data.add(category7, 2d, fixes = 2d);
            data.add(category7, 3d, fixes = 2d);
            
            Category category8 = new Category("merges completed", SwingUtil.cellLightLightRed, 7, true);
            fixes = 0d;
            data.add(category8, 5d, fixes = 2d);
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
