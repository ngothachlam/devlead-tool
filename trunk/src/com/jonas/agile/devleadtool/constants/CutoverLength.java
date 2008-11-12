package com.jonas.agile.devleadtool.constants;

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
