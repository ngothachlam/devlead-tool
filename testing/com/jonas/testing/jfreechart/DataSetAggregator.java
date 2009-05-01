package com.jonas.testing.jfreechart;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jfree.data.time.Day;

import com.jonas.jira.JiraStatus;

public class DataSetAggregator {

   private Map<DayDTO, DaysAgreggator> daysAgreggator = new HashMap<DayDTO, DaysAgreggator>();

   public Collection<DaysAgreggator> getDays() {
      return daysAgreggator.values();
   }

   public void add(Day day, JiraStatus jiraStatus) {
      DayDTO daydto = new DayDTO(day);
      DaysAgreggator dayAgreggator = daysAgreggator.get(daydto);
      if (dayAgreggator == null) {
         dayAgreggator = new DaysAgreggator();
         daysAgreggator.put(daydto, dayAgreggator);
      }
      dayAgreggator.add(day, jiraStatus);
   }
}
