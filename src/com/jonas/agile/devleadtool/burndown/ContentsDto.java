package com.jonas.agile.devleadtool.burndown;

import java.util.Vector;

public class ContentsDto {

   public Vector<Object> getHeader() {
      return header;
   }

   public Vector<Vector<Object>> getBody() {
      return body;
   }

   private final Vector<Object> header;
   private final Vector<Vector<Object>> body;

   public ContentsDto(Vector<Object> header, Vector<Vector<Object>> body) {
      this.header = header;
      this.body = body;
   }


}
