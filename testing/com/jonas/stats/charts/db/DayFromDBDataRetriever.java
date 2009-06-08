package com.jonas.stats.charts.db;

import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import com.jonas.stats.charts.common.DateRetriever;

public class DayFromDBDataRetriever implements DateRetriever<String> {

   @Override
   public RegularTimePeriod retrieveTimeLinePointFromObject(String object) {
      System.out.println("trying to read: " + object);
      return Day.parseDay(object);
   }

}
