package com.jonas.agile.devleadtool.gui.component.table.model.color;

import java.util.HashMap;
import java.util.Map;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;

public class EstimateValidator {

   private Map<ColumnType, Validator> map = new HashMap<ColumnType, Validator>();

   public EstimateValidator() {
   }

   public Validator getValidator(ColumnType column) {
      return map.get(column);
   }

}
