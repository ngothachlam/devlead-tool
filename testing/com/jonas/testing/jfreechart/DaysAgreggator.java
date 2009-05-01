package com.jonas.testing.jfreechart;

import java.util.HashMap;
import java.util.Map;

import org.jfree.data.time.Day;

import com.jonas.jira.JiraStatus;

public class DaysAgreggator {

   private Day day;

   private Map<JiraStatus, Integer> amountMap = new HashMap<JiraStatus, Integer>();

   public int getAmount(JiraStatus jiraStatus) {
      Integer integer = amountMap.get(jiraStatus);
      if (integer == null)
         return 0;
      return integer;
   }

   public Day getDay() {
      return day;
   }

   public void add(Day day, JiraStatus jiraStatus) {
      System.out.println("adding " + jiraStatus + " to " + day);
      this.day = day;
      Integer amount = amountMap.get(jiraStatus);
      if(amount == null){
         amount = new Integer(0);
      }
      amountMap.put(jiraStatus, ++amount);
   }

}
