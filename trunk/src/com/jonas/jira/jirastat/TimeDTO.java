package com.jonas.jira.jirastat;

import java.util.Calendar;

import org.jfree.data.time.Day;

import com.jonas.common.DateHelper;

public class TimeDTO {

   private int year;
   private int month;
   private int day;

   public TimeDTO(Day day, boolean isWeek) {
      if (isWeek) {
         Calendar calendar = setDayToFirstDayOfWeek(day);
         this.year = calendar.get(Calendar.YEAR);
         this.month = calendar.get(Calendar.MONTH) + 1;
         this.day = calendar.get(Calendar.DAY_OF_MONTH);
      } else {
         this.year = day.getYear();
         this.month = day.getMonth();
         this.day = day.getDayOfMonth();
      }
   }

   private Calendar setDayToFirstDayOfWeek(Day day) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(day.getYear(), day.getMonth() - 1, day.getDayOfMonth());
      int dayofweek = DateHelper.getRealDayOfWeek(calendar);
      calendar.add(Calendar.DAY_OF_MONTH, -(dayofweek - 1));
      return calendar;
   }

   public Day getDate() {
      return new Day(day, month, year);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + day;
      result = prime * result + month;
      result = prime * result + year;
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
      TimeDTO other = (TimeDTO) obj;
      if (day != other.day)
         return false;
      if (month != other.month)
         return false;
      if (year != other.year)
         return false;
      return true;
   }

}