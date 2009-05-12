package com.jonas.testing.jirastat;

import org.jfree.data.time.Day;

public class PointsInTimeFacade<T> extends PointsInTimeFacadeAbstract<T>{

   private final boolean isWeek;

   public PointsInTimeFacade(boolean isWeek) {
      this.isWeek = isWeek;
   }

   public void addPointInTime(Day day, T barSeparator) {
      TimeDTO daydto = new TimeDTO(day, isWeek);
      PointInTimeAgreggator<T> pointInTimeAgreggator = dataDaysAgreggators.get(daydto);
      if (pointInTimeAgreggator == null) {
         pointInTimeAgreggator = new PointInTimeAgreggator<T>();
         dataDaysAgreggators.put(daydto, pointInTimeAgreggator);
         pointInTimeAgreggator.add(daydto.getDate(), barSeparator);
      } else {
         pointInTimeAgreggator.add(daydto.getDate(), barSeparator);
      }
   }
}
