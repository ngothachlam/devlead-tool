/**
 * 
 */
package com.jonas.agile.devleadtool.component.table;

public enum ColumnValue {
   Yes, No, NA {
      @Override
      public String toString() {
         return "";
      }
   }
}