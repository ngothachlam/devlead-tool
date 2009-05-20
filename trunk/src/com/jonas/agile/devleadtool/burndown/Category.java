package com.jonas.agile.devleadtool.burndown;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;

public class Category implements Comparable {

   private final String name;

   public Category(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
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
      final Category other = (Category) obj;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      return true;
   }

   @Override
   public int compareTo(Object that) {
      BoardStatusValue thisBoardStatus = BoardStatusValue.get(getName());
      Category thatCategory = (Category) that;
      BoardStatusValue thatBoardStatus = BoardStatusValue.get(thatCategory.getName());

      System.out.println("this board status: " + thisBoardStatus + " and that board status: " + thatBoardStatus);

      if (thisBoardStatus != null && thatBoardStatus != null) {
         return thatBoardStatus.getLocation() - thisBoardStatus.getLocation();
      }
      if (thisBoardStatus != null)
         return -1;
      if (thatBoardStatus != null)
         return 1;

      return 0;
   }
}
