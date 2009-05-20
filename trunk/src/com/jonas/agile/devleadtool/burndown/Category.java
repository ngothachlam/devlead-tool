package com.jonas.agile.devleadtool.burndown;

import java.awt.Color;

public class Category {

   private final String name;
   private final Color color;
   private final int drawPrio;

   public Category(String name, Color color, int drawPrio) {
      this.name = name;
      this.color = color;
      this.drawPrio = drawPrio;
   }

   public int getDrawPrio() {
      return drawPrio;
   }

   public Color getColor() {
      return color;
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
      Category other = (Category) obj;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      return true;
   }

   
}
