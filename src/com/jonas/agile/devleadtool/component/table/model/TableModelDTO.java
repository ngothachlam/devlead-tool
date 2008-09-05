package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;

public class TableModelDTO {

   private final Vector<Vector<Object>> contents;
   private final Vector<Column> header;

   public TableModelDTO(Vector<Column> header, Vector<Vector<Object>> contents) {
      this.header = header;
      this.contents = contents;
   }

   public Vector<Vector<Object>> getContents() {
      return contents;
   }

   public Vector<Column> getHeader() {
      return header;
   }

}