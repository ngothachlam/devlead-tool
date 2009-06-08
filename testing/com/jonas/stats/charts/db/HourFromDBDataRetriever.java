package com.jonas.stats.charts.db;

import org.jfree.data.time.Hour;
import org.jfree.data.time.RegularTimePeriod;
import com.jonas.stats.charts.common.DateRetriever;

public class HourFromDBDataRetriever implements DateRetriever<String> {

   @Override
   public RegularTimePeriod retrieveTimeLinePointFromObject(String object) {
      System.out.print("trying to read: " + object );
      String substring = object.substring(0, object.indexOf(":"));
      System.out.print("... and substring: " + substring);
      Hour parseHour = Hour.parseHour(substring);
      System.out.println("... becoming : " + parseHour);
      return parseHour;
   }

}
