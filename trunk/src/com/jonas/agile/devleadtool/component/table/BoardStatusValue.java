package com.jonas.agile.devleadtool.component.table;

public enum BoardStatusValue {
   UnKnown(0), NA(0), Parked(0), Open(1), Bug(1), InDevProgress(2), Resolved(3), InQAProgress(4), Complete(5), ForShowCase(6), Approved(7);

   private String toString;
   private String name;

   private BoardStatusValue(int number) {
      StringBuffer sb = new StringBuffer();
      sb.append(number).append(". ").append(super.toString());
      toString = sb.toString();
      name = super.toString();
   }

   public String toString() {
      return toString;
   }
   public String getName() {
      return name;
   }

   public static BoardStatusValue get(String cellContents) {
      for (BoardStatusValue boardStatusValue : values()) {
         if (boardStatusValue.toString.equalsIgnoreCase(cellContents))
            return boardStatusValue;
      }
      for (BoardStatusValue boardStatusValue : values()) {
         if (boardStatusValue.name.equalsIgnoreCase(cellContents))
            return boardStatusValue;
      }
      return null;
   }

}
