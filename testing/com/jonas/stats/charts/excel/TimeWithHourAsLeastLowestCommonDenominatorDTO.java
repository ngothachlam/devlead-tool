package com.jonas.stats.charts.excel;

import java.util.Calendar;

import org.jfree.data.time.Hour;
import org.jfree.data.time.RegularTimePeriod;

import com.jonas.common.DateHelper;
import com.jonas.stats.charts.common.RegularTimeDenominator;

public class TimeWithHourAsLeastLowestCommonDenominatorDTO implements RegularTimeDenominator<Hour> {

   private int year;
   private int month;
   private int day;
   private int hour;

   public TimeWithHourAsLeastLowestCommonDenominatorDTO(RegularTimePeriod day) {
      Calendar calendar = setDayToFirstDayOfWeek(day);
      this.year = calendar.get(Calendar.YEAR);
      this.month = calendar.get(Calendar.MONTH) + 1;
      this.day = calendar.get(Calendar.DAY_OF_MONTH);
      this.hour = calendar.get(Calendar.HOUR);
   }

   private Calendar setDayToFirstDayOfWeek(RegularTimePeriod day) {
      Calendar calendar = Calendar.getInstance();
      if (day instanceof Hour) {
         Hour charthour = (Hour) day;
         calendar.set(charthour.getYear(), charthour.getMonth() - 1, charthour.getDayOfMonth(), charthour.getHour(), 0);
      } else {
         throw new RuntimeException("should be of type Hour! It is of type " + day.getClass());
      }

      int dayofweek = DateHelper.getRealDayOfWeek(calendar);
      calendar.add(Calendar.DAY_OF_MONTH, -(dayofweek - 1));
      return calendar;
   }

   public Hour getDate() {
      return new Hour(hour, day, month, year);
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
      TimeWithHourAsLeastLowestCommonDenominatorDTO other = (TimeWithHourAsLeastLowestCommonDenominatorDTO) obj;
      if (day != other.day)
         return false;
      if (month != other.month)
         return false;
      if (year != other.year)
         return false;
      return true;
   }

}
