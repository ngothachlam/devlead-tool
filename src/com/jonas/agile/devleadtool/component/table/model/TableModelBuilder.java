package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;

public class TableModelBuilder {

   public PlanTableModel buildPlanTableModel(TableModelDTO dto) {
      return new PlanTableModel(dto.getContents(), dto.getHeader());
   }

}
