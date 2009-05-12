package com.jonas.jira.jirastat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PointsInTimeFacadeAbstract<T> {
   Map<TimeDTO, PointInTimeAgreggator<T>> dataDaysAgreggators = new HashMap<TimeDTO, PointInTimeAgreggator<T>>();

   public List<PointInTimeAgreggator<T>> getPointInTimes() {
      List<PointInTimeAgreggator<T>> list = new ArrayList<PointInTimeAgreggator<T>>();
      for(PointInTimeAgreggator<T> aggregator : dataDaysAgreggators.values()){
         list.add(aggregator);
      }
      return list;
   }

}
