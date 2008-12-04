package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;

public abstract class AbstractColorRule implements ColorRule {
   
   @Override
   public String getJira() {
      return jira;
   }

   private Column column;
   private final MyTable table;
   private String jira;

   public AbstractColorRule(Column column, MyTable table) {
      super();
      this.column = column;
      this.table = table;
   }

   public abstract Color getColor();

   @Override
   public final boolean isTrue(Column column, Object value, int row) {
      System.out.println("field: " + this.column + " - passed in: " + column);
      if(!this.column.equals(column)){
         return false;
      }
      jira = (String) table.getValueAt(Column.Jira, row);
      return isTrue(value, row);
   }

   public abstract boolean isTrue(Object value, int row);
}
