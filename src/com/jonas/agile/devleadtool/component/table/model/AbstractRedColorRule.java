package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.SwingUtil;

public abstract class AbstractRedColorRule extends AbstractColorRule {

   public AbstractRedColorRule(Column column, MyTable table) {
      super(column, table);
   }

   @Override
   public Color getColor() {
      return SwingUtil.cellRED;
   }

   @Override
   public abstract boolean isTrue(Object value, int row);

}
