package com.jonas.agile.devleadtool.component;

public enum CutoverLength {
   DEFAULT, TEST_5{
      @Override
      public int value() {
         return 5;
      }};


   public int value() {
      return 65;
   }

}
