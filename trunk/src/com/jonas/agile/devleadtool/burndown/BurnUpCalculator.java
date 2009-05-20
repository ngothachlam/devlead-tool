package com.jonas.agile.devleadtool.burndown;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.jonas.agile.devleadtool.data.JiraStatistic;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.jira.TestObjects;

public class BurnUpCalculator {

   public BurnUpCalculator() {
   }

   private double getDouble(Object string) {
      double parseDouble;
      try {
         parseDouble = Double.parseDouble(string.toString());
      } catch (NumberFormatException e) {
         return 0d;
      }
      return parseDouble;
   }

   Vector<String> dataColumns = new Vector();
   Map<String, Integer> dataColumnsMap = new LinkedHashMap<String, Integer>();

   public BurnData getSortedDataUsingCriteria(HistoricalData historicalData, HistoricalDataCriteria criteria) {
      Vector<String> historicalHeader = historicalData.getHeaders();
      Vector<Vector<Object>> historicalBody = historicalData.getBody();

      dataColumns.add(HistoricalBoardDao.DAY_IN_SPRINT);
      dataColumns.add(ColumnType.BoardStatus.toString());
      dataColumns.add(ColumnType.DEst.toString());
      dataColumns.add(ColumnType.QEst.toString());
      dataColumns.add(ColumnType.DRem.toString());
      dataColumns.add(ColumnType.QRem.toString());

      BurnData burnData = new BurnData(BurnType.BurnUp, TestObjects.TEST_SPRINT_CURRENT.getLength());

      Vector<Integer> criteriaCols = new Vector<Integer>();
      for (int counter = 0; counter < historicalHeader.size(); counter++) {
         String header = historicalHeader.get(counter);
         System.out.println("**\"" + header + "\" and counter: " + counter);
         if (header.equals(criteria.getHeader())) {
            System.out.println("*****Criteria found!" + counter);
            criteriaCols.add(counter);
         }
         if (dataColumns.indexOf(header) >= 0) {
            System.out.println("*****dataColumn found!" + counter);
            dataColumnsMap.put(header, counter);
         }
      }

      for (Vector<Object> bodyRow : historicalBody) {
         System.out.println("bodyRow: " + bodyRow + " (" + criteriaCols.get(0) + ")");
         Object bodyRowCriteriaValue = bodyRow.get(criteriaCols.get(0));
         System.out.println("\tcriteria value: " + criteria.getValue() + " and is bodyRow value: " + bodyRowCriteriaValue);
         if (criteria.getValue().equals(bodyRowCriteriaValue)) {

            Integer categoryCol = dataColumnsMap.get(ColumnType.BoardStatus.toString());
            Integer dayInSprintCol = dataColumnsMap.get(HistoricalBoardDao.DAY_IN_SPRINT);
            Integer dEstCol = dataColumnsMap.get(ColumnType.DEst.toString());
            Integer qEstCol = dataColumnsMap.get(ColumnType.QEst.toString());
            Integer dRemCol = dataColumnsMap.get(ColumnType.DRem.toString());
            Integer qRemCol = dataColumnsMap.get(ColumnType.QRem.toString());

            System.out.println("\tdRemCol: " + dRemCol);

            String boardstatus = bodyRow.get(categoryCol).toString();
            Category category = new Category(boardstatus);
            Object boryRowDayInSprint = bodyRow.get(dayInSprintCol);
            double dayInSprint = Double.parseDouble(boryRowDayInSprint.toString());
            double value = 0d;

            JiraStatistic jiraStat = new JiraStatistic(BoardStatusValue.get(boardstatus));
            switch (jiraStat.devStatus()) {
               case jiraIsInDevelopmentProgressState:
                  value += getDouble(bodyRow.get(dRemCol.intValue()));
                  value += getDouble(bodyRow.get(qRemCol.intValue()));
                  break;
               case jiraIsInDevelopmentResolvedState:
                  value += getDouble(bodyRow.get(dEstCol.intValue()));
                  value += getDouble(bodyRow.get(qRemCol.intValue()));
                  break;
               case jiraIsInDevelopmentOpenState:
               case jiraIsInPreDevelopmentState:
               case jiraIsInPostDevelopmentState:
                  value += getDouble(bodyRow.get(dEstCol.intValue()));
                  value += getDouble(bodyRow.get(qEstCol.intValue()));
                  break;
            }

            burnData.add(category, dayInSprint, value);
         }
      }

      burnData.sort();

      Set<Category> categoryNames = burnData.getCategoryNames();

      BoardStatusValue preValue = null;
      for (Category category : categoryNames) {
         BoardStatusValue bValue = BoardStatusValue.get(category.getName());

         if (preValue == null) {
            burnData.setPreValue(category, 5 - bValue.getLocation());
         } else if (preValue.getLocation() - bValue.getLocation() > 1) {
            burnData.setPreValue(category, preValue.getLocation() - bValue.getLocation() + 1);
         }

         System.out.println("bValue: " + bValue + " preValue: " + preValue);

         preValue = bValue;
      }

      return burnData;
   }

}
