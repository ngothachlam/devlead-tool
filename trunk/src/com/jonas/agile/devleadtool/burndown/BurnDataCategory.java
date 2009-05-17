package com.jonas.agile.devleadtool.burndown;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BurnDataCategory {

   private Map<Category, BurnDataCategory> burnDataPerCategory;
   private List<BurnDataColumn> burnDays = new ArrayList<BurnDataColumn>();

   public BurnDataCategory(Map<Category, BurnDataCategory> burnDownDataPerCategory) {
      this.burnDataPerCategory = burnDownDataPerCategory;
   }

   public BurnDataCategory() {
      this.burnDataPerCategory = new LinkedHashMap<Category, BurnDataCategory>();
   }

   public void add(Category string, double x, double y) {
      BurnDataCategory data = getCategory(string);
      if (data == null) {
         data = new BurnDataCategory(burnDataPerCategory);
         burnDataPerCategory.put(string, data);
      }
      data.add(new BurnDataColumn(x, y));
   }

   private void add(BurnDataColumn burnDownDay) {
      burnDays.add(burnDownDay);
   }

   private BurnDataCategory getCategory(Category string) {
      return burnDataPerCategory.get(string);
   }

   public List<BurnDataColumn> getDataForCategory(Category string) {
      return getCategory(string).getData();
   }

   private List<BurnDataColumn> getData() {
      return burnDays;
   }

   public Set<Category> getCategoryNames() {
      Set<Category> keySet = burnDataPerCategory.keySet();
      return keySet;
   }
}
