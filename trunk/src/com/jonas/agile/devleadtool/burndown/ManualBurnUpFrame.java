package com.jonas.agile.devleadtool.burndown;

import java.awt.Component;

import com.jonas.common.DateHelper;

public class ManualBurnUpFrame extends ManualBurnFrame {

   public static void main(String[] args) {
      ManualBurnFrame frame = new ManualBurnUpFrame(null, null, new BurnDataRetriever() {

         BurnDataCategory data;

         @Override
         public void calculateBurndownData() {
            data = new BurnDataCategory(BurnType.BurnUp);
            
            Category category = new Category("Closed");
            data.add(category, 0d, 0d);
            data.add(category, 1d, 1d);
            data.add(category, 2d, 2d);

            Category category2 = new Category("Resolved");
            data.add(category2, 0d, 0d);
            data.add(category2, 1d, 1d);
            data.add(category2, 2d, 2d);

            Category category3 = new Category("In-Progress");
            data.add(category3, 0d, 0d);
            data.add(category3, 1d, 1d);
            data.add(category3, 2d, 1d);

            Category category4 = new Category("Failed");
            data.add(category4, 0d, 0d);
            data.add(category4, 1d, 1d);
            data.add(category4, 2d, 0d);

            Category category5 = new Category("Open");
            data.add(category5, 0d, 3d);
            data.add(category5, 1d, 2d);
            data.add(category5, 2d, 1d);

            Category category6 = new Category("Datafixes completed");
            double dataFixes = 0d;
            data.add(category6, 0d, dataFixes += 2d);
            data.add(category6, 1d, dataFixes += 1d);
            data.add(category6, 2d, dataFixes += 2d);
         }

         @Override
         public BurnDataCategory getBurnData() {
            return data;
         }
      });
      frame.setVisible(true);
   }

   private ManualBurnUpFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever) {
      super(parent, dateHelper, retriever, false);
   }

}
