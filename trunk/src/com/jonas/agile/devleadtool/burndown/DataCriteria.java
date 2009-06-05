package com.jonas.agile.devleadtool.burndown;

public class DataCriteria {

   public String getValue() {
      return value;
   }

   private final String header;
   private final String value;

   public DataCriteria(String header, String value) {
      this.header = header;
      this.value = value;
   }

   public String getHeader() {
      return header;
   }

}
