package com.jonas.agile.devleadtool.burndown;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BurnDownData {

   private Map<String, BurnDownData> burnDownDataPerCategory;
   private List<BurnDownDay> burndownDays = new ArrayList<BurnDownDay>();

   public BurnDownData(Map<String, BurnDownData> burnDownDataPerCategory) {
      this.burnDownDataPerCategory = burnDownDataPerCategory;
   }

   public BurnDownData() {
      this.burnDownDataPerCategory = new LinkedHashMap<String, BurnDownData>();
   }

   public void add(String category, double x, double y) {
      BurnDownData data = getCategory(category);
      if (data == null) {
         data = new BurnDownData(burnDownDataPerCategory);
         burnDownDataPerCategory.put(category, data);
      }
      data.add(new BurnDownDay(x, y));
   }

   private void add(BurnDownDay burnDownDay) {
      burndownDays.add(burnDownDay);
   }

   private BurnDownData getCategory(String category) {
      return burnDownDataPerCategory.get(category);
   }

   public List<BurnDownDay> getDataForCategory(String string) {
      return getCategory(string).getData();
   }

   private List<BurnDownDay> getData() {
      return burndownDays;
   }

   public Set<String> getCategoryNames() {
      return burnDownDataPerCategory.keySet();
   }

}
