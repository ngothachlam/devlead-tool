package com.jonas.testing.jirastat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.time.Day;

import com.jonas.jira.JiraStatus;

public class PointsInTimeFacade {

   private Map<TimeDTO, PointInTimeAgreggator> dataDaysAgreggators = new HashMap<TimeDTO, PointInTimeAgreggator>();
   private final boolean isWeek;

   public PointsInTimeFacade(boolean isWeek) {
      this.isWeek = isWeek;
   }

   public List<PointInTimeAgreggator> getPointInTimes() {
      List<PointInTimeAgreggator> list = new ArrayList<PointInTimeAgreggator>();
      for(PointInTimeAgreggator aggregator : dataDaysAgreggators.values()){
         list.add(aggregator);
      }
      return list;
   }

   public void addPointInTime(Day day, JiraStatus jiraStatus) {
      TimeDTO daydto = new TimeDTO(day, isWeek);
      PointInTimeAgreggator pointInTimeAgreggator = dataDaysAgreggators.get(daydto);
      if (pointInTimeAgreggator == null) {
         pointInTimeAgreggator = new PointInTimeAgreggator();
         dataDaysAgreggators.put(daydto, pointInTimeAgreggator);
         pointInTimeAgreggator.add(daydto.getDate(), jiraStatus);
      } else {
         pointInTimeAgreggator.add(daydto.getDate(), jiraStatus);
      }
   }
}
