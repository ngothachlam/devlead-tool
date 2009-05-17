package com.jonas.agile.devleadtool.burndown;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BurnData {

   private Map<String, BurnData> burnDataPerCategory;
   private List<BurnDataColumn> burnDays = new ArrayList<BurnDataColumn>();

   public BurnData(Map<String, BurnData> burnDownDataPerCategory) {
      this.burnDataPerCategory = burnDownDataPerCategory;
   }

   public BurnData() {
      this.burnDataPerCategory = new LinkedHashMap<String, BurnData>();
   }

   public void add(String category, double x, double y) {
      BurnData data = getCategory(category);
      if (data == null) {
         data = new BurnData(burnDataPerCategory);
         burnDataPerCategory.put(category, data);
      }
      data.add(new BurnDataColumn(x, y));
   }

   private void add(BurnDataColumn burnDownDay) {
      burnDays.add(burnDownDay);
   }

   private BurnData getCategory(String category) {
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
