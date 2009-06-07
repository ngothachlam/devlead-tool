package com.jonas.stats.charts.common;

import java.util.HashMap;
import java.util.Map;

import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.RegularTimePeriod;

public class PointInTimeAgreggator<T, A extends RegularTimePeriod> implements Comparable<PointInTimeAgreggator<T, A>> {

   private A day;

   private Map<T, Integer> amountMap = new HashMap<T, Integer>();

   public int getAmount(T groupObject) {
      Integer integer = amountMap.get(groupObject);
      if (integer == null)
         return 0;
      return integer;
   }

   public A getDay() {
      return day;
   }

   public void add(A day, T groupObject, int value) {
      this.day = day;
      Integer amount = amountMap.get(groupObject);
      if (amount == null) {
         amount = new Integer(0);
      }
      amountMap.put(groupObject, amount + value);
   }

   public int test() {
      if (day instanceof Day) {

      }
      return 0;
   }

   @Override
   public int compareTo(PointInTimeAgreggator<T, A> o) {
      if (day instanceof Day) {
         Day day1 = (Day) getDay();
         Day day2 = (Day) o.getDay();
         if (day1.getYear() == day2.getYear()) {
            if (day1.getMonth() == day2.getMonth()) {
               return day1.getDayOfMonth() - day2.getDayOfMonth();
            }
            return day1.getMonth() - day2.getMonth();
         }
         return day1.getYear() - day2.getYear();
      }

      Hour day1 = (Hour) getDay();
      Hour day2 = (Hour) o.getDay();
      if (day1.getYear() == day2.getYear()) {
         if (day1.getMonth() == day2.getMonth()) {
            if (day1.getDayOfMonth() == day2.getDayOfMonth()) {
               return day1.getHour() - day2.getHour();
            }
            return day1.getDayOfMonth() - day2.getDayOfMonth();
         }
         return day1.getMonth() - day2.getMonth();
      }
      return day1.getYear() - day2.getYear();
   }
}
