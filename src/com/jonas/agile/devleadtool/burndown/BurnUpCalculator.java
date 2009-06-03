package com.jonas.agile.devleadtool.burndown;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

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

   public BurnData getSortedDataUsingCriteria(HistoricalData historicalData, HistoricalDataCriteria criteria, Sprint currentSprint) {
      Vector<String> historicalHeader = historicalData.getHeaders();
      Vector<Vector<Object>> historicalBody = historicalData.getBody();

      Vector<String> dataColumns = new Vector();
      dataColumns.add(HistoricalBoardDao.DAY_IN_SPRINT);
      dataColumns.add(ColumnType.BoardStatus.toString());
      dataColumns.add(ColumnType.DEst.toString());
      dataColumns.add(ColumnType.QEst.toString());
      dataColumns.add(ColumnType.DRem.toString());
      dataColumns.add(ColumnType.QRem.toString());

      Integer currentSprintLength = currentSprint.getLength();
      BurnData burnData = new BurnData(BurnType.BurnUp, currentSprintLength, "Estimated Points");

      MapDTO mapDto = findTheColumnNamesAndTheirLocations(criteria, historicalHeader, dataColumns);
      Map<String, Integer> columnNameAndItsLocationMap = mapDto.getColumnNameAndItsLocationMap();
      Vector<Integer> criteriaCols = mapDto.getCriteriaCols();

      for (Vector<Object> bodyRow : historicalBody) {
         // FIXME 1 - Criteria cols should be dynamic (in case more criterias are added).
         Object bodyRowCriteriaValue = bodyRow.get(criteriaCols.get(0));

         if (criteria.getValue().equals(bodyRowCriteriaValue)) {
            Integer categoryCol = columnNameAndItsLocationMap.get(ColumnType.BoardStatus.toString());
            Integer dayInSprintCol = columnNameAndItsLocationMap.get(HistoricalBoardDao.DAY_IN_SPRINT);
            Integer dEstCol = columnNameAndItsLocationMap.get(ColumnType.DEst.toString());
            Integer qEstCol = columnNameAndItsLocationMap.get(ColumnType.QEst.toString());

            String boardstatus = bodyRow.get(categoryCol).toString();

            JiraStatistic jiraStat = new JiraStatistic(BoardStatusValue.get(boardstatus));
            double value = 0d;
            switch (jiraStat.devStatus()) {
               default:
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

   private MapDTO findTheColumnNamesAndTheirLocations(HistoricalDataCriteria criteria, Vector<String> historicalHeader, Vector<String> dataColumns) {
      Map<String, Integer> columnNameAndItsLocationMap = new LinkedHashMap<String, Integer>();
      Vector<Integer> criteriaCols = new Vector<Integer>();

      for (int counter = 0; counter < historicalHeader.size(); counter++) {
         String header = historicalHeader.get(counter);
         if (header.equals(criteria.getHeader())) {
            criteriaCols.add(counter);
         }
         if (dataColumns.indexOf(header) >= 0) {
            columnNameAndItsLocationMap.put(header, counter);
         }
      }

      return new MapDTO(columnNameAndItsLocationMap, criteriaCols);
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
