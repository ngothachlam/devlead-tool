package com.jonas.agile.devleadtool.burndown;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.data.JiraStatistic;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.common.swing.SwingUtil;

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

   public BurnData getSortedDataUsingCriteria(HistoricalData historicalData, HistoricalDataCriteria criteria, Sprint currentSprint) {
      Vector<String> historicalHeader = historicalData.getHeaders();
      Vector<Vector<Object>> historicalBody = historicalData.getBody();

      dataColumns.add(HistoricalBoardDao.DAY_IN_SPRINT);
      dataColumns.add(ColumnType.BoardStatus.toString());
      dataColumns.add(ColumnType.DEst.toString());
      dataColumns.add(ColumnType.QEst.toString());
      dataColumns.add(ColumnType.DRem.toString());
      dataColumns.add(ColumnType.QRem.toString());

      Integer currentSprintLength = currentSprint.getLength();
      BurnData burnData = new BurnData(BurnType.BurnUp, currentSprintLength);

      Vector<Integer> criteriaCols = new Vector<Integer>();
      for (int counter = 0; counter < historicalHeader.size(); counter++) {
         String header = historicalHeader.get(counter);
         if (header.equals(criteria.getHeader())) {
            criteriaCols.add(counter);
         }
         if (dataColumns.indexOf(header) >= 0) {
            dataColumnsMap.put(header, counter);
         }
      }

      for (Vector<Object> bodyRow : historicalBody) {
         Object bodyRowCriteriaValue = bodyRow.get(criteriaCols.get(0));

         if (criteria.getValue().equals(bodyRowCriteriaValue)) {
            Integer categoryCol = dataColumnsMap.get(ColumnType.BoardStatus.toString());
            Integer dayInSprintCol = dataColumnsMap.get(HistoricalBoardDao.DAY_IN_SPRINT);
            Integer dEstCol = dataColumnsMap.get(ColumnType.DEst.toString());
            Integer qEstCol = dataColumnsMap.get(ColumnType.QEst.toString());
            Integer dRemCol = dataColumnsMap.get(ColumnType.DRem.toString());
            Integer qRemCol = dataColumnsMap.get(ColumnType.QRem.toString());

            String boardstatus = bodyRow.get(categoryCol).toString();
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

            Category category = new Category(boardstatus, getColor(boardstatus), getPrintPrio(boardstatus));
            Object bodyRowDayInSprint = bodyRow.get(dayInSprintCol);
            double dayInSprint = Double.parseDouble(bodyRowDayInSprint.toString());
            burnData.add(category, dayInSprint, value);
         }
      }

      return burnData;
   }

   private Color getColor(String boardstatus) {
      BoardStatusValue value = BoardStatusValue.get(boardstatus);
      switch (value) {
         case Complete:
            return SwingUtil.cellLightGreen;
         case Failed:
            return SwingUtil.cellRed;
         case InProgress:
            return SwingUtil.cellLightYellow;
         case Open:
            return SwingUtil.cellLightGrey;
         case Resolved:
            return SwingUtil.cellBlue;
         case Approved:
            return SwingUtil.cellLightBlue;
         case ForShowCase:
            return SwingUtil.cellGreen;
      }
      return null;
   }

   private int getPrintPrio(String boardstatus) {
      BoardStatusValue value = BoardStatusValue.get(boardstatus);
      switch (value) {
         case Complete:
            return 0;
         case Resolved:
            return 1;
         case InProgress:
            return 2;
         case Failed:
            return 3;
         case Open:
            return 4;
         case Approved:
            return 5;
         case ForShowCase:
            return 6;
         default:
            return 7;
      }
   }

}
