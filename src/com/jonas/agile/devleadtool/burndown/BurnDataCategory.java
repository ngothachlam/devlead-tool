package com.jonas.agile.devleadtool.burndown;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BurnDataCategory {

   private Map<CategoryType, BurnDataCategory> burnDataPerCategory;
   private List<BurnDataColumn> burnDays = new ArrayList<BurnDataColumn>();

   public BurnDataCategory(Map<CategoryType, BurnDataCategory> burnDownDataPerCategory) {
      this.burnDataPerCategory = burnDownDataPerCategory;
   }

   public BurnDataCategory() {
      this.burnDataPerCategory = new LinkedHashMap<CategoryType, BurnDataCategory>();
   }

   public void add(CategoryType string, double x, double y) {
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

   private BurnDataCategory getCategory(CategoryType string) {
      return burnDataPerCategory.get(string);
   }

   public List<BurnDataColumn> getDataForCategory(CategoryType string) {
      return getCategory(string).getData();
   }

   private List<BurnDataColumn> getData() {
      return burnDays;
   }

   public Set<CategoryType> getCategoryNames() {
      return burnDataPerCategory.keySet();
   }

   public String getTypeOfBurn(String categoryName) {
      // TODO Auto-generated method stub
      throw new RuntimeException("Method not implemented yet!");
   }

}
