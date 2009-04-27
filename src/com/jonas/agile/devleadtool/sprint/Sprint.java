package com.jonas.agile.devleadtool.sprint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import com.jonas.common.DateHelper;
import com.jonas.common.logging.MyLogger;

public class Sprint {

   private static final Logger log = MyLogger.getLogger(Sprint.class);
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
   private boolean isForCombobox;
   private static final Vector<Sprint> EXTRASPRINTS = new Vector<Sprint>();

   public Sprint(String name, Date startDate, Date endDate, int length) {
      this.name = name.trim();
      this.startDate = startDate;
      this.endDate = endDate;
      this.length = length;
   }

   public boolean isForCombobox() {
      return isForCombobox;
   }

   public Sprint setForCombobox() {
      EXTRASPRINTS.add(this);
      isForCombobox = true;
      return this;
   }

   public static Vector<Sprint> getComboSprints() {
      return EXTRASPRINTS;
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

      if (startDate == null || endDate == null)
         return SprintTime.unKnown;

      Calendar calendar = Calendar.getInstance();
      Date today = calendar.getTime();

      boolean startDateIsTodayOrBefore = DateHelper.isFirstBeforeSecond(startDate, today);
      boolean endDateIsTodayOrAfter = DateHelper.isSameDay(endDate, today) || DateHelper.isFirstAfterSecond(endDate, today);

      log.debug("startDate: " + startDate + " endDate: " + endDate + " startDatePreToday: " + startDateIsTodayOrBefore + " endDatePostToday: "
            + endDateIsTodayOrAfter);

      if (startDateIsTodayOrBefore && endDateIsTodayOrAfter) {
         return SprintTime.currentSprint;
      } else if (startDateIsTodayOrBefore && !endDateIsTodayOrAfter) {
         return SprintTime.beforeCurrentSprint;
      } else if (!startDateIsTodayOrBefore && endDateIsTodayOrAfter) {
         return SprintTime.afterCurrentSprint;
      }
      return null;
   }

   public Integer calculateDayInSprint() {
      Calendar calendar = Calendar.getInstance();
      Date today = calendar.getTime();
      return DateHelper.getWorkingDaysBetween(startDate, today) - 1;
   }

}
