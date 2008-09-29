package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;

public class TableModelDTO {

   private final Vector<Vector<Object>> contents;
   private final Vector<ColumnDataType> header;

   public TableModelDTO(Vector<ColumnDataType> header, Vector<Vector<Object>> contents) {
      this.header = header;
      this.contents = contents;
   }

   public Vector<Vector<Object>> getContents() {
      return contents;
   }

   public Vector<ColumnDataType> getHeader() {
      return header;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((contents == null) ? 0 : contents.hashCode());
      result = prime * result + ((header == null) ? 0 : header.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      final TableModelDTO other = (TableModelDTO) obj;
      if (contents == null) {
         if (other.contents != null)
            return false;
      } else if (!contents.equals(other.contents))
         return false;
      if (header == null) {
         if (other.header != null)
            return false;
      } else if (!header.equals(other.header))
         return false;
      return true;
   }

}