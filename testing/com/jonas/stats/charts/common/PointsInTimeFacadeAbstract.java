package com.jonas.stats.charts.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.time.RegularTimePeriod;


public abstract class PointsInTimeFacadeAbstract<T, A extends RegularTimePeriod> {
   Map<RegularTimeDenominator, PointInTimeAgreggator<T, A>> dataDaysAgreggators = new HashMap<RegularTimeDenominator, PointInTimeAgreggator<T, A>>();

   public List<PointInTimeAgreggator<T, A>> getPointInTimes() {
      List<PointInTimeAgreggator<T, A>> list = new ArrayList<PointInTimeAgreggator<T, A>>();
      for(PointInTimeAgreggator<T, A> aggregator : dataDaysAgreggators.values()){
         list.add(aggregator);
      }
      return list;
   }

}
