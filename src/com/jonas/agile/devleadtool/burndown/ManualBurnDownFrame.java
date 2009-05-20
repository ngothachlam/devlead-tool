package com.jonas.agile.devleadtool.burndown;

import java.awt.Component;

import com.jonas.common.DateHelper;

public class ManualBurnDownFrame extends ManualBurnFrame {

   public static void main(String[] args) {
      ManualBurnDownFrame frame = new ManualBurnDownFrame(null, null, new BurnDataRetriever() {

         BurnData data;

         @Override
         public void calculateBurndownData() {
            data = new BurnData(BurnType.BurnDown);


            Category category1 = new Category("Real Progression");
            data.add(category1, 0d, 15d);
            data.add(category1, 0d, 7d);
            data.add(category1, 1d, 16d + 7d);
            data.add(category1, 2d, 16d + 5d);
            data.add(category1, 3d, 13d + 3d);
            data.add(category1, 4d, 13d + 1.5d);
            data.add(category1, 5d, 13d + 0d);
            data.add(category1, 6d, 4d + 7d);
            data.add(category1, 7d, 2d + 2d);
            data.add(category1, 8d, 2d + 4d);
            data.add(category1, 9d, 1.75d + 3d);
            data.add(category1, 10d, 1.75d + 2d);

            Category category2 = new Category("Ideal Progression");
            data.add(category2, 0, 15d + 7d);
            data.add(category2, 10d, 0d);
         }

         @Override
         public BurnData getBurnData() {
            return data;
         }
      });
      frame.setVisible(true);
   }

   private ManualBurnDownFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever) {
      super(parent, dateHelper, retriever, true);
   }

}
