/**
 * 
 */
package com.jonas.agile.devleadtool.component.table;

public enum Column {
   URL, Open, Bugs, InProgress, Resolved, Complete, inPanel {
      @Override
      public String toString() {
         return "inPanel?";
      }
   },
   Jira, Description, FixVersion, Status, Resolution, BuildNo, BoardStatus, Estimate, Type, Planned_Sprint, Resolved_Sprint, Closed_Sprint, Actual, Note;

   public static Column getEnum(String columnName) {
      for (Column col : Column.values()) {
         if (col.toString().equals(columnName)) {
            return col;
         }
      }
      return null;

   }
}