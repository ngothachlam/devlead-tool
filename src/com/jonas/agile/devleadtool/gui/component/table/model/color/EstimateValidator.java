package com.jonas.agile.devleadtool.gui.component.table.model.color;

import java.util.HashMap;
import java.util.Map;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;

public class EstimateValidator {

   private static EstimateValidator instance = new EstimateValidator();

   private Map<ColumnType, Validator> map = new HashMap<ColumnType, Validator>();

   private EstimateValidator() {
      map.put(ColumnType.DEst, new DEstValidator());
   }

   public Validator getValidator(ColumnType column) {
      return map.get(column);
   }

   public static EstimateValidator getInstance() {
      return instance;
   }

}
