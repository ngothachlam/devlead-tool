package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;

public abstract class AbstractColorRule implements ColorRule {

   public MyTable getThisTable() {
      return table;
   }

   @Override
   public String getJira() {
      return jira;
   }

   Column column;
   private final MyTable table;
   private String jira;
   private final Color color;

   public AbstractColorRule(Column column, MyTable table, Color color) {
      super();
      this.column = column;
      this.table = table;
      this.color = color;
   }

   final public Color getColor(){
      return color;
   }

   @Override
   public final boolean isTrue(Column column, Object value, int row) {
      if (!this.column.equals(column)) {
         return false;
      }
      if (table != null)
         jira = (String) table.getValueAt(Column.Jira, row);
      return isTrue(value, row);
   }

   public abstract boolean isTrue(Object value, int row);
   
   
}
