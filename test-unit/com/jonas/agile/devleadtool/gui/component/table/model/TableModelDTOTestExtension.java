package com.jonas.agile.devleadtool.gui.component.table.model;

import java.util.Vector;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.model.TableModelDTO;

public class TableModelDTOTestExtension extends TableModelDTO {

   public TableModelDTOTestExtension(Vector<ColumnType> header, Vector<Vector<Object>> contents) {
      super(header, contents);
   }

   @Override
   public boolean equals(Object obj) {
      return super.equals(obj);
   }
}
