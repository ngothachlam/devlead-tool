package com.jonas.stats.charts.jira;

import java.awt.Color;

public class GroupingDTO<T> {

   public T getValue() {
      return value;
   }

   public int getPriority() {
      return priority;
   }

   public Color getColor() {
      return color;
   }

   private final T value;
   private final int priority;
   private final Color color;

   public GroupingDTO(T value, int priority, Color color) {
      this.value = value;
      this.priority = priority;
      this.color = color;
   }

}