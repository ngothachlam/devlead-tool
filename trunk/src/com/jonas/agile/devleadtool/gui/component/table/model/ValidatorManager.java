package com.jonas.agile.devleadtool.gui.component.table.model;

import com.jonas.agile.devleadtool.gui.component.table.model.color.Validator;
import com.jonas.agile.devleadtool.gui.component.table.model.color.ValidatorResponse;

public class ValidatorManager {

   public ValidatorResponse validate(Validator validator, Object value, int row, ValueGetter valueGetter) {
      return validator.validate(value, valueGetter.getType(row), valueGetter.getBoardStatus(row));
   }

}
