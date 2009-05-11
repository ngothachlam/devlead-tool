package com.jonas.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.jonas.common.logging.MyLogger;

public class DateHelper {
   private final static Logger log = MyLogger.getLogger(DateHelper.class);
   private final static SimpleDateFormat simpleDateformatOrderable = new SimpleDateFormat("yyyy-MM-dd");
   private final static SimpleDateFormat simpleDateformat = new SimpleDateFormat("dd-MM-yyyy");
   private final static SimpleDateFormat advancDateformat = new SimpleDateFormat("EEE dd-MM-yyyy");

   public static int getRealDayOfWeek(Calendar startCalendar) {
      int i = startCalendar.get(Calendar.DAY_OF_WEEK) - 1;
      return i == 0 ? 7 : i;
   }

   public static boolean isWorkingDay(Calendar startCalendar) {
      int dayOfWeek = startCalendar.get(Calendar.DAY_OF_WEEK);
      if (dayOfWeek == 1 || dayOfWeek == 7) {
         return false;
      }
      return true;
   }

   public static String getTodaysDateAsString() {
      Date dateNow = new Date();
      return getDateAsString(dateNow);
   }

   public static String getDateAsString(Date dateNow) {
      return advancDateformat.format(dateNow);
   }

   public static String getDateAsSimpleString(Date dateNow) {
      return simpleDateformat.format(dateNow);
   }

   public static String getDateAsSimpleOrderableString(Date dateNow) {
      return simpleDateformatOrderable.format(dateNow);
   }

   public static int getWorkingDaysBetween(Date startDate, Date endDate) {
      if (startDate == null || endDate == null) {
         return -1;
      }

      Calendar startCalendar = getCalendar(startDate);
      Calendar endCalendar = getCalendar(endDate);

      int fullWeeksBetween = getFullWorkingWeeksBetween(startCalendar, endCalendar);

      int startDayOfWeek = DateHelper.getRealDayOfWeek(startCalendar);
      int endDayOfWeek = DateHelper.getRealDayOfWeek(endCalendar);

      int additional = 0;
      int endAdditional = endAdditional(endDayOfWeek);
      if (startCalendar.get(Calendar.WEEK_OF_YEAR) == endCalendar.get(Calendar.WEEK_OF_YEAR)) {
         additional = endAdditional - endAdditional(startDayOfWeek) + (DateHelper.isWorkingDay(startCalendar) ? 1 : 0);
      } else {
         additional = endAdditional + startAdditional(startDayOfWeek);
      }

      return fullWeeksBetween * 5 + additional;
   }

   static Calendar getCalendar(Date date) {
      Calendar startCalendar = Calendar.getInstance();
      startCalendar.setTime(date);
      return startCalendar;
   }

   static int endAdditional(int endDayOfWeek) {
      return (endDayOfWeek / 6 < 1) ? endDayOfWeek % 6 : 5;
   }

   static int getFullWorkingWeeksBetween(Calendar startCalendar, Calendar endCalendar) {
      int firstWeek = startCalendar.get(Calendar.WEEK_OF_YEAR);
      int endWeek = endCalendar.get(Calendar.WEEK_OF_YEAR);

      int fullWeeksBetween = endWeek - firstWeek - 1;
      fullWeeksBetween = fullWeeksBetween < 0 ? 0 : fullWeeksBetween;
      return fullWeeksBetween;
   }

   public static Calendar getDate(SimpleDateFormat format, String date) {
      try {
         if (log.isDebugEnabled())
            log.debug("trying to parse " + date);
         Date d = format.parse(date);
         Calendar cal = Calendar.getInstance();
         cal.setTime(d);
         return cal;
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return null;
   }

   static int startAdditional(int startDayOfWeek) {
      int i = 6 - startDayOfWeek;
      return i < 0 ? 0 : i;
   }

   public static boolean isFirstAfterSecond(Date first, Date second) {
      return second.compareTo(first) <= 0;
   }

   public static boolean isFirstBeforeSecond(Date first, Date second) {
      return second.compareTo(first) >= 0;
   }

   public static boolean isSameDay(Date start, Date second) {
      Calendar cal1 = Calendar.getInstance();
      cal1.setTime(start);

      Calendar cal2 = Calendar.getInstance();
      cal2.setTime(second);

      return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
   }
}