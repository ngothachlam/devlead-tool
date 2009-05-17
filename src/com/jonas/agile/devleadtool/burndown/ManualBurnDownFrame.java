package com.jonas.agile.devleadtool.burndown;

import java.awt.Component;

import com.jonas.common.DateHelper;

public class ManualBurnDownFrame extends AbstractManualBurnFrame {

   public static void main(String[] args) {
      ManualBurnDownFrame frame = new ManualBurnDownFrame(null, null, new BurnDataRetriever() {

         BurnDataCategory data;

         @Override
         public void calculateBurndownData() {
            data = new BurnDataCategory();
            
            Category category2 = new Category("Ideal Progression", BurnType.BurnDown);
            double yValue = 15d + 7d;
            double length = 10d;
            double decrease = yValue/length;
            for (int xValue = 0; xValue <= length; xValue++) {
               data.add(category2, xValue, yValue);
               yValue -= decrease;
            }
            
            Category category1 = new Category("Real Progression", BurnType.BurnDown);
            data.add(category1, 0d, 15d + 7d);
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

}
