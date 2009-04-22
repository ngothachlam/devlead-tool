package com.jonas.agile.devleadtool.sprint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sprint {

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
      StringBuffer sb = new StringBuffer();
      sb.append(name);
      sb.append(" (start:").append(format(startDate)).append(", ");
      sb.append("end:").append(format(endDate)).append(", ");
      sb.append("length:").append(length).append(")");
      return sb.toString();
   }

   String format(Date date) {
      return format.format(date);
   }

   private Date endDate;
   private int length;
   private String name;
   private Date startDate;

   public Sprint(String name, Date startDate, Date endDate, int length) {
      this.name = name;
      this.startDate = startDate;
      this.endDate = endDate;
      this.length = length;
   }

   public Sprint() {
   }

   public Date getEndDate() {
      return endDate;
   }

   public int getLength() {
      return length;
   }

   public String getName() {
      return name;
   }

   public Date getStartDate() {
      return startDate;
   }

}
