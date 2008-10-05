package com.jonas.agile.devleadtool.component.dnd;

import com.jonas.agile.devleadtool.component.table.MyTable;

public class TableAndTitleDTO{

   private String title;
   private MyTable table;

   public TableAndTitleDTO(String title, MyTable table) {
      super();
      this.title = title;
      this.table = table;
   }

   public String getTitle() {
      return title;
   }

   public MyTable getTable() {
      return table;
   }
}