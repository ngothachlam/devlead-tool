package com.jonas.agile.devleadtool.component.table.model;

public class TableModelBuilder {

   public PlanTableModel buildPlanTableModel(TableModelDTO dto) {
      return new PlanTableModel(dto.getContents(), dto.getHeader());
   }

}
