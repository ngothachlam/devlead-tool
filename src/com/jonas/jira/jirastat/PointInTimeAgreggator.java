package com.jonas.jira.jirastat;

import java.util.HashMap;
import java.util.Map;

import org.jfree.data.time.Day;

public class PointInTimeAgreggator<T> implements Comparable<PointInTimeAgreggator> {

   private Day day;

   private Map<T, Integer> amountMap = new HashMap<T, Integer>();

   public int getAmount(T groupObject) {
      Integer integer = amountMap.get(groupObject);
      if (integer == null)
         return 0;
      return integer;
   }

   public Day getDay() {
      return day;
   }

   public void add(Day day, T groupObject) {
      this.day = day;
      Integer amount = amountMap.get(groupObject);
      if (amount == null) {
         amount = new Integer(0);
      }
      amountMap.put(groupObject, ++amount);
   }

   @Override
   public int compareTo(PointInTimeAgreggator o) {
      Day day2 = o.getDay();
      if (day.getYear() == day2.getYear()) {
         if (day.getMonth() == day2.getMonth()) {
            return day.getDayOfMonth() - day2.getDayOfMonth();
         }
         return day.getMonth() - day2.getMonth();
      }
      return day.getYear() - day2.getYear();
   }
}
