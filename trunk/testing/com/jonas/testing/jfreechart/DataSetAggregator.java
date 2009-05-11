package com.jonas.testing.jfreechart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.time.Day;

import com.jonas.jira.JiraStatus;

public class DataSetAggregator {

   private Map<TimeDTO, DaysAgreggator> daysAgreggator = new HashMap<TimeDTO, DaysAgreggator>();
   private final boolean isWeek;

   public DataSetAggregator(boolean isWeek) {
      this.isWeek = isWeek;
   }

   public List<DaysAgreggator> getDays() {
      List<DaysAgreggator> list = new ArrayList<DaysAgreggator>();
      for(DaysAgreggator aggregator : daysAgreggator.values()){
         list.add(aggregator);
      }
      return list;
   }

   public void add(Day day, JiraStatus jiraStatus) {
      TimeDTO daydto = new TimeDTO(day, isWeek);
      DaysAgreggator dayAgreggator = daysAgreggator.get(daydto);
      if (dayAgreggator == null) {
         dayAgreggator = new DaysAgreggator();
         daysAgreggator.put(daydto, dayAgreggator);
         dayAgreggator.add(daydto.getDate(), jiraStatus);
      } else {
         dayAgreggator.add(daydto.getDate(), jiraStatus);
      }
   }
}
