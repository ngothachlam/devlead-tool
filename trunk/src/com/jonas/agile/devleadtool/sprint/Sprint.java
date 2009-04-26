package com.jonas.agile.devleadtool.sprint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Sprint {

   private static final DateFormat format = new SimpleDateFormat("EEE dd-MMM-yy");
   
   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }

   public void setLength(int length) {
      this.length = length;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setStartDate(Date startDate) {
      this.startDate = startDate;
   }

   @Override
   public String toString() {
      return name;
   }

   String format(Date date) {
      return format.format(date);
   }

   private Date endDate;
   private int length;
   private String name;
   private Date startDate;

   public Sprint(String name, Date startDate, Date endDate, int length) {
      this.name = name.trim();
      this.startDate = startDate;
      this.endDate = endDate;
      this.length = length;
   }

   public Sprint() {
   }

   public Date getEndDate() {
      return endDate;
   }

   public Integer getLength() {
      return length;
   }

   public String getName() {
      return name;
   }

   public Date getStartDate() {
      return startDate;
   }

   public SprintTime calculateTime() {
      Date startDate = getStartDate();
      Date endDate = getEndDate();

      Calendar calendar = Calendar.getInstance();
      Date today = calendar.getTime();

      boolean startDatePreToday = today.compareTo(startDate) >= 0;
      boolean endDatePostToday = today.compareTo(endDate) <= 0;

      if (startDatePreToday && endDatePostToday) {
         return SprintTime.currentSprint;
      } else if (startDatePreToday && !endDatePostToday) {
         return SprintTime.beforeCurrentSprint;
      } else if (!startDatePreToday && endDatePostToday) {
         return SprintTime.afterCurrentSprint;
      }
      return null;
   }

   public int calculateDayInSprint() {
      // TODO Auto-generated method stub
      throw new RuntimeException("Method not implemented yet!");
   }

}
