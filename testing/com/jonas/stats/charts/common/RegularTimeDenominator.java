package com.jonas.stats.charts.common;

import org.jfree.data.time.RegularTimePeriod;

public interface RegularTimeDenominator<A extends RegularTimePeriod> {

   public A getDate();

}
