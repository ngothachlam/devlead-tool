package com.jonas.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateHelper {
   private final static SimpleDateFormat simpleDateformat = new SimpleDateFormat("dd-MM-yyyy");
   private final static SimpleDateFormat advancDateformat = new SimpleDateFormat("EEE dd-MM-yyyy");

   public static int getRealDayOfWeek(Calendar startCalendar) {
      int i = startCalendar.get(Calendar.DAY_OF_WEEK) - 1;
      return i == 0 ? 7 : i;
   }

   public static boolean isWorkingDay(Calendar startCalendar) {
      int dayOfWeek = startCalendar.get(Calendar.DAY_OF_WEEK);
      if(dayOfWeek == 1 || dayOfWeek == 7){
         return false;
      }
      return true;
   }

   public static String getTodaysDateAsString() {
      Date dateNow = new Date ();
      return getDateAsString(dateNow);
   }

   public static String getDateAsString(Date dateNow) {
      return advancDateformat.format( dateNow );
   }

   public static String getDateAsSimpleString(Date dateNow) {
      return simpleDateformat.format( dateNow );
   }

}