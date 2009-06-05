package com.jonas.stats.charts.common;

import java.util.Calendar;

import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.RegularTimePeriod;

import com.jonas.common.DateHelper;

public class LowestCommonDenominatorRegularTime implements RegularTimeDenominator<RegularTimePeriod> {

   private int year;
   private int month;
   private int day;
   private int hour;
   private final CommonTimeDenominatorStyle style;

   public LowestCommonDenominatorRegularTime(RegularTimePeriod day, CommonTimeDenominatorStyle style) {
      this.style = style;
      Day dayI;
      switch (style) {
         case week:
            dayI = (Day) day;
            Calendar calendar = setDayToFirstDayOfWeek(dayI);
            this.year = calendar.get(Calendar.YEAR);
            this.month = calendar.get(Calendar.MONTH) + 1;
            this.day = calendar.get(Calendar.DAY_OF_MONTH);
            break;
         case day:
            dayI = (Day) day;
            this.year = dayI.getYear();
            this.month = dayI.getMonth();
            this.day = dayI.getDayOfMonth();
            break;
         case hour:
            Hour aHour = (Hour) day;
            this.year = aHour.getYear();
            this.month = aHour.getMonth();
            this.day = aHour.getDayOfMonth();
            this.hour = aHour.getHour();
            break;
      }
   }

   private Calendar setDayToFirstDayOfWeek(Day day) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(day.getYear(), day.getMonth() - 1, day.getDayOfMonth());
      int dayofweek = DateHelper.getRealDayOfWeek(calendar);
      calendar.add(Calendar.DAY_OF_MONTH, -(dayofweek - 1));
      return calendar;
   }

   public RegularTimePeriod getDate() {
      switch (style) {
         case week:
         case day:
            return new Day(day, month, year);
         case hour:
            return new Hour(hour, day, month, year);
      }
      throw new RuntimeException("Style " + style + " not handled!");
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + day;
      result = prime * result + hour;
      result = prime * result + month;
      result = prime * result + ((style == null) ? 0 : style.hashCode());
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
      LowestCommonDenominatorRegularTime other = (LowestCommonDenominatorRegularTime) obj;
      if (day != other.day)
         return false;
      if (hour != other.hour)
         return false;
      if (month != other.month)
         return false;
      if (style == null) {
         if (other.style != null)
            return false;
      } else if (!style.equals(other.style))
         return false;
      if (year != other.year)
         return false;
      return true;
   }

}
