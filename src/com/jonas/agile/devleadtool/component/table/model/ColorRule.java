package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
import com.jonas.agile.devleadtool.component.table.Column;

public interface ColorRule {

   public boolean isTrue(Column column, Object value, int row);

   public Color getColor();
   
   public String getJira();

   public boolean shouldPerformRealTime();

}
