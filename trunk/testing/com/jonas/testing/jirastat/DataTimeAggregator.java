package com.jonas.testing.jirastat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.time.Day;

import com.jonas.jira.JiraStatus;

public class DataTimeAggregator {

   private Map<TimeDTO, DataDayAgreggator> dataDaysAgreggators = new HashMap<TimeDTO, DataDayAgreggator>();
   private final boolean isWeek;

   public DataTimeAggregator(boolean isWeek) {
      this.isWeek = isWeek;
   }

   public List<DataDayAgreggator> getDays() {
      List<DataDayAgreggator> list = new ArrayList<DataDayAgreggator>();
      for(DataDayAgreggator aggregator : dataDaysAgreggators.values()){
         list.add(aggregator);
      }
      return list;
   }

   public void add(Day day, JiraStatus jiraStatus) {
      TimeDTO daydto = new TimeDTO(day, isWeek);
      DataDayAgreggator dayAgreggator = dataDaysAgreggators.get(daydto);
      if (dayAgreggator == null) {
         dayAgreggator = new DataDayAgreggator();
         dataDaysAgreggators.put(daydto, dayAgreggator);
         dayAgreggator.add(daydto.getDate(), jiraStatus);
      } else {
         dayAgreggator.add(daydto.getDate(), jiraStatus);
      }
   }
}
