package com.jonas.agile.devleadtool.burndown;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BurnData {

   private Map<Category, BurnData> burnDataPerCategory;
   private List<BurnDataColumn> burnDays = new ArrayList<BurnDataColumn>();
   private BurnType burnType;
   private Integer timeLength;

   public BurnData(Map<Category, BurnData> burnDownDataPerCategory) {
      this.burnDataPerCategory = burnDownDataPerCategory;
   }

   public BurnData(BurnType burnType, Integer length) {
      this(burnType);
      this.timeLength = length;
   }

   public BurnData(BurnType burnType) {
      this.burnType = burnType;
      this.burnDataPerCategory = new LinkedHashMap<Category, BurnData>();
   }

   public void add(Category string, double x, double y) {
      BurnData data = getCategory(string);
      if (data == null) {
         data = new BurnData(burnDataPerCategory);
         burnDataPerCategory.put(string, data);
      }
      data.add(new BurnDataColumn(x, y));
   }

   private void add(BurnDataColumn burnDownDay) {
      burnDays.add(burnDownDay);
   }

   private BurnData getCategory(Category string) {
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

   public BurnType getBurnType() {
      return burnType;
   }

   public Integer getLength() {
      return timeLength;
   }
}
