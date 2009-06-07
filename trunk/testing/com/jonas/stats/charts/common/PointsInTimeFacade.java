package com.jonas.stats.charts.common;

import org.jfree.data.time.RegularTimePeriod;


public class PointsInTimeFacade<T, A extends RegularTimePeriod> extends PointsInTimeFacadeAbstract<T, A> {

   public PointsInTimeFacade() {
   }

   public void addPointInTimeWithValue(T barSeparator, RegularTimeDenominator<A> timeDenominator, int value) {
      RegularTimeDenominator<A> daydto = timeDenominator;
      PointInTimeAgreggator<T, A> pointInTimeAgreggator = dataDaysAgreggators.get(daydto);
      A date = daydto.getDate();

      if (pointInTimeAgreggator == null) {
         pointInTimeAgreggator = new PointInTimeAgreggator<T, A>();
         dataDaysAgreggators.put(daydto, pointInTimeAgreggator);
      }

      System.out.println("Adding point in time for " + barSeparator + " on " + date + " using timeDenominator " + timeDenominator.getClass());
      pointInTimeAgreggator.add(date, barSeparator, value);
   }
}
