package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.logging.MyLogger;

public abstract class AbstractColorRule implements ColorRule {

   private Logger log = MyLogger.getLogger(AbstractColorRule.class);

   
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
   private final boolean realTimeCheck;

   public AbstractColorRule(Column column, MyTable table, Color color, boolean realTimeCheck) {
      super();
      this.column = column;
      this.table = table;
      this.color = color;
      this.realTimeCheck = realTimeCheck;
   }

   final public Color getColor(){
      return color;
   }

   @Override
   public final boolean isTrue(Column column, Object value, int row) {
      log.debug("value: " + value + " row: " + row + " column: " + column + " this.column: " + this.column + " table: " + table);
      if (!this.column.equals(column)) {
         return false;
      }
      if (table != null)
         jira = (String) table.getValueAt(Column.Jira, row);
      return isTrue(value, row);
   }

   public abstract boolean isTrue(Object value, int row);
   
   public boolean shouldPerformRealTime(){
      return realTimeCheck;
   }
}
