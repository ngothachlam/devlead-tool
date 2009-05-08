package com.jonas.agile.devleadtool.gui.component.table.model.color;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;

public interface Validator {

   public ValidatorResponse validate(Object value, IssueType type, BoardStatusValue boardStatus);

}
