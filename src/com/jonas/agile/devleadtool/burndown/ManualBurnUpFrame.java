package com.jonas.agile.devleadtool.burndown;

import java.awt.Component;

import com.jonas.common.DateHelper;

public class ManualBurnUpFrame extends AbstractManualBurnFrame {

   public static void main(String[] args) {
      AbstractManualBurnFrame frame = new ManualBurnUpFrame(null, null, new BurnDataRetriever() {

         BurnDataCategory data;

         @Override
         public void calculateBurndownData() {
            data = new BurnDataCategory();
            
            Category category = new Category("Closed", BurnType.BurnUp);
            data.add(category, 0d, 0d);
            data.add(category, 1d, 1d);
            data.add(category, 2d, 2d);

            Category category2 = new Category("Resolved", BurnType.BurnUp);
            data.add(category2, 0d, 0d);
            data.add(category2, 1d, 1d);
            data.add(category2, 2d, 2d);

            Category category3 = new Category("In-Progress", BurnType.BurnUp);
            data.add(category3, 0d, 0d);
            data.add(category3, 1d, 1d);
            data.add(category3, 2d, 1d);

            Category category4 = new Category("Failed", BurnType.BurnUp);
            data.add(category4, 0d, 0d);
            data.add(category4, 1d, 1d);
            data.add(category4, 2d, 0d);

            Category category5 = new Category("Open", BurnType.BurnUp);
            data.add(category5, 0d, 3d);
            data.add(category5, 1d, 2d);
            data.add(category5, 2d, 1d);

            Category category6 = new Category("Datafixes completed", BurnType.BurnUp);
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
}
