/**
 * 
 */
package com.jonas.agile.devleadtool.component.table.model;

public enum Column {
   URL, Open, Bugs, InProgress, Resolved, Complete, inPanel {
      @Override
      public String toString() {
         return "inPanel?";
      }
   },
   Jira, Description;

   public static Column getEnum(String columnName) {
      for (Column col : Column.values()) {
         if (col.toString().equals(columnName)) {
            return col;
         }
      }
      return null;

   }
}