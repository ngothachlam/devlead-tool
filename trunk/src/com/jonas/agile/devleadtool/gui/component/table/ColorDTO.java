package com.jonas.agile.devleadtool.gui.component.table;

import java.awt.Color;
import org.apache.poi.hssf.util.HSSFColor;

public class ColorDTO {

   private final Color color;
   private final boolean marked;
   private HSSFColor hssfColor;

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

   public HSSFColor getHSSFColor() {
      return hssfColor;
   }

   public void setHSSFColor(HSSFColor hssfColor) {
      this.hssfColor = hssfColor;
   }

}
