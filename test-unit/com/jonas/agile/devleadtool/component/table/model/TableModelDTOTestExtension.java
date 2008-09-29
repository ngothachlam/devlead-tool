package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;

public class TableModelDTOTestExtension extends TableModelDTO {

   public TableModelDTOTestExtension(Vector<ColumnDataType> header, Vector<Vector<Object>> contents) {
      super(header, contents);
   }

   @Override
   public boolean equals(Object obj) {
      return super.equals(obj);
   }
}
