package com.jonas.agile.devleadtool.gui.component.table;

import java.awt.Color;

public class ColorDTO {

   private final Color color;
   private final boolean marked;

   public ColorDTO(Color color, boolean marked) {
      this.color = color;
      this.marked = marked;
   }

   public Color getColor() {
      return color;
   }
   
   public boolean isMarked(){
      return marked;
   }

}
