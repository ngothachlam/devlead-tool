package com.jonas.agile.devleadtool.sprint;

import java.util.Date;

public class Sprint {

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
      sb.append("[Name:").append(name).append(", ");
      sb.append("endDate:").append(endDate).append(", ");
      sb.append("startDate:").append(startDate).append(", ");
      sb.append("length:").append(length).append("]");
      return sb.toString();
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
