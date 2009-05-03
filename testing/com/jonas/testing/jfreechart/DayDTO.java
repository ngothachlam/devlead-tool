package com.jonas.testing.jfreechart;

import java.util.Calendar;

import org.jfree.data.time.Day;

import com.jonas.common.DateHelper;

public class DayDTO {

   private int year;
   private int month;
   private int day;

   public DayDTO(Day day, boolean isWeek) {
      if (isWeek) {
         Calendar calendar = Calendar.getInstance();
         calendar.set(day.getYear(), day.getMonth() - 1, day.getDayOfMonth());
         int dayofweek = DateHelper.getRealDayOfWeek(calendar);
         calendar.add(Calendar.DAY_OF_MONTH, -(dayofweek - 1));

         this.year = calendar.get(Calendar.YEAR);
         this.month = calendar.get(Calendar.MONTH) + 1;
         this.day = calendar.get(Calendar.DAY_OF_MONTH);
      } else {
         this.year = day.getYear();
         this.month = day.getMonth();
         this.day = day.getDayOfMonth();
      }
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
      DayDTO other = (DayDTO) obj;
      if (day != other.day)
         return false;
      if (month != other.month)
         return false;
      if (year != other.year)
         return false;
      return true;
   }

}
