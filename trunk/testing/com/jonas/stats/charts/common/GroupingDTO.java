package com.jonas.stats.charts.common;

import java.awt.Color;

public class GroupingDTO<T> {

   public T getValue() {
      return value;
   }

   public Color getColor() {
      return color;
   }

   private final T value;
   private final Color color;

   public GroupingDTO(T value, Color color) {
      this.value = value;
      this.color = color;
   }

}