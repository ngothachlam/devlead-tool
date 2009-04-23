package com.jonas.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateHelper {
   private SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM-yyyy");

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

   public String getTodaysDateAsString() {
      Date dateNow = new Date ();
      return getDateAsString(dateNow);
   }

   String getDateAsString(Date dateNow) {
      return dateformat.format( dateNow );
   }

}