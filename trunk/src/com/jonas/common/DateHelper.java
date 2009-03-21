package com.jonas.common;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateHelper {
   private SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM-yyyy");

   public String getTodaysDateAsString() {
      Date dateNow = new Date ();
      return getDateAsString(dateNow);
   }

   String getDateAsString(Date dateNow) {
      return dateformat.format( dateNow );
   }


}