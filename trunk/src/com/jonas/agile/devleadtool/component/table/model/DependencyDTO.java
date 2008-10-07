package com.jonas.agile.devleadtool.component.table.model;

import com.jonas.agile.devleadtool.component.table.Column;

public class DependencyDTO{
   private final Column[] columns;
   public DependencyDTO(Column... columns){
      this.columns = columns;
   }
}