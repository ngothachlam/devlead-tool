package com.jonas.agile.devleadtool.gui.component.table.model.color;

import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;
import com.jonas.agile.devleadtool.gui.component.table.model.ValidatorResponse;

public interface Validator {

   public ValidatorResponse validate(Object value, IssueType type, Object boardStatus);

}
