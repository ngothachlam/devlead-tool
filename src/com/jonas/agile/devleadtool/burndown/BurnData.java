package com.jonas.agile.devleadtool.burndown;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jonas.common.logging.MyLogger;

public class BurnData {

   public String getYAxisName() {
      return yAxisName;
   }

   private Map<Category, BurnDataColumns> burnDataPerCategory;
   private BurnType burnType;
   private Integer timeLength;
   private static final Logger log = MyLogger.getLogger(BurnData.class);
   private final String yAxisName;

   public BurnData(BurnType burnType, Integer length, String yAxisName) {
      this(burnType, yAxisName);
      this.timeLength = length;
   }

   public BurnData(BurnType burnType, String yAxisName) {
      this.burnType = burnType;
      this.yAxisName = yAxisName;
      this.burnDataPerCategory = new LinkedHashMap<Category, BurnDataColumns>();
   }

   public void add(Category category, double x, double y) {
      if (log.isDebugEnabled()) {
         log.debug("adding value " + y + " to axis location " + x + " for category \"" + category.getName() + "\"");
      }

      BurnDataColumns dataForCategory = getColumnCollectionForCategory(category);
      dataForCategory.add(x, y);
   }

   public List<BurnDataColumn> getDataForCategory(Category string) {
      return burnDataPerCategory.get(string).getBurnDataColumList();
   }

   public BurnDataColumns getColumnCollectionForCategory(Category string) {
      BurnDataColumns burnDataColumns = burnDataPerCategory.get(string);
      if (burnDataColumns == null) {
         burnDataColumns = new BurnDataColumns();
         burnDataPerCategory.put(string, burnDataColumns);
      }
      return burnDataColumns;
   }

   public Set<Category> getCategoryNames() {
      this.sort();
      Set<Category> keySet = burnDataPerCategory.keySet();
      return keySet;
   }

   public BurnType getBurnType() {
      return burnType;
   }

   public Integer getLength() {
      return timeLength;
   }

   private class BurnDataColumns {

      public List<BurnDataColumn> getBurnDataColumList() {
         return burnDataColumList;
      }

      private List<BurnDataColumn> burnDataColumList = new ArrayList<BurnDataColumn>();

      public void add(double x, double y) {
         BurnDataColumn column = getBurnDataColumn(x);
         column.addY(y);
      }

      private BurnDataColumn getBurnDataColumn(double x) {
         for (BurnDataColumn burnDataColumn : burnDataColumList) {
            System.out.println("trying to find x value " + x + " for burnDataColumn " + burnDataColumn + " (which has x " + burnDataColumn.getX() + ")");
            if (burnDataColumn.getX() == x) {
               System.out.println("found it!!");
               return burnDataColumn;
            }
         }
         System.out.println("creating a new column for " + x + "!!");
         BurnDataColumn e = new BurnDataColumn(x, 0d);
         burnDataColumList.add(e);
         return e;
      }
   }

   private void sort() {
      Map<Category, BurnDataColumns> map = burnDataPerCategory;
      ArrayList<Category> mapValues = new ArrayList<Category>(map.keySet());
      Comparator<Category> comparable = new Comparator<Category>() {
         @Override
         public int compare(Category thisC, Category thatC) {
            return thisC.getDrawPrio() - thatC.getDrawPrio();
         }
      };
      Collections.sort(mapValues, comparable);

      Map<Category, BurnDataColumns> burnDataPerCategoryV2 = new LinkedHashMap<Category, BurnDataColumns>();
      for (Category category : mapValues) {
         System.out.println("putting " + category.getName() + " in first spot!");
         burnDataPerCategoryV2.put(category, burnDataPerCategory.get(category));
      }

      burnDataPerCategory = burnDataPerCategoryV2;
   }


}
