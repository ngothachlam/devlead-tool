package com.jonas.agile.devleadtool.gui.component.table;

import java.awt.Color;
import org.apache.poi.hssf.util.HSSFColor;

public class ColorDTO {

   private final Color color;
   private final boolean marked;
   private HSSFColor hssfColor;
   private final boolean greyed;

   public ColorDTO(Color color, boolean marked, boolean greyed) {
      this.color = color;
      this.marked = marked;
      this.greyed = greyed;
   }

   public Color getColor() {
      return color;
   }
   
   public boolean isMarked(){
      return marked;
   }

   public boolean isGreyed(){
      return greyed;
   }
   
   public HSSFColor getHSSFColor() {
      return hssfColor;
   }

   public void setHSSFColor(HSSFColor hssfColor) {
      this.hssfColor = hssfColor;
   }

}
