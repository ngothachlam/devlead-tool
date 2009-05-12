package com.jonas.agile.devleadtool.gui.component.table.model;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.model.color.Validator;

public class ValidatorManager {

   public ValidatorResponse validate(Validator validator, Object value, int row, ValueGetter valueGetter) {
      BoardStatusValue boardStatus = valueGetter.getBoardStatus(row);
      return validator.validate(value, valueGetter.getType(row), boardStatus);
   }

}
