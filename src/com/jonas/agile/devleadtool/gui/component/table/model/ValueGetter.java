package com.jonas.agile.devleadtool.gui.component.table.model;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;

public interface ValueGetter  {

   public IssueType getType(int row);

   public BoardStatusValue getBoardStatus(int row);

}
