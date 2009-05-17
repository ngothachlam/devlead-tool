package com.jonas.agile.devleadtool.burndown;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BurnDataCategory {

   private Map<String, BurnDataCategory> burnDataPerCategory;
   private List<BurnDataColumn> burnDays = new ArrayList<BurnDataColumn>();

   public BurnDataCategory(Map<String, BurnDataCategory> burnDownDataPerCategory) {
      this.burnDataPerCategory = burnDownDataPerCategory;
   }

   public BurnDataCategory() {
      this.burnDataPerCategory = new LinkedHashMap<String, BurnDataCategory>();
   }

   public void add(String category, double x, double y) {
      BurnDataCategory data = getCategory(category);
      if (data == null) {
         data = new BurnDataCategory(burnDataPerCategory);
         burnDataPerCategory.put(category, data);
      }
      data.add(new BurnDataColumn(x, y));
   }

   private void add(BurnDataColumn burnDownDay) {
      burnDays.add(burnDownDay);
   }

   private BurnDataCategory getCategory(String category) {
      return burnDataPerCategory.get(category);
   }

   public List<BurnDataColumn> getDataForCategory(String string) {
      return getCategory(string).getData();
   }

   private List<BurnDataColumn> getData() {
      return burnDays;
   }

   public Set<String> getCategoryNames() {
      return burnDataPerCategory.keySet();
   }

}
