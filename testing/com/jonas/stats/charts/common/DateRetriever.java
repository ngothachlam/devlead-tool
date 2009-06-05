package com.jonas.stats.charts.common;

import org.jfree.data.time.RegularTimePeriod;

public interface DateRetriever<T>{

   public RegularTimePeriod retrieveTimeLineDateFromJira(T jiraIssue);

}
