package com.jonas.agile.devleadtool.burndown;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.jonas.agile.devleadtool.data.JiraStatistic;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;
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

   public BurnData getSortedDataUsingCriteria(ContentsDto historicalData, DataCriteria criteria, Sprint currentSprint) {
      Vector<String> historicalHeader = historicalData.getHeader();
      Vector<Vector<Object>> historicalBody = historicalData.getBody();

      Vector<String> dataColumns = new Vector();
      dataColumns.add(HistoricalBoardDao.DAY_IN_SPRINT);
      dataColumns.add(ColumnType.BoardStatus.toString());
      dataColumns.add(ColumnType.DEst.toString());
      dataColumns.add(ColumnType.QEst.toString());
      dataColumns.add(ColumnType.DRem.toString());
      dataColumns.add(ColumnType.QRem.toString());
      dataColumns.add(ColumnType.Type.toString());
      dataColumns.add(ColumnType.Jira.toString());

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
            Integer typeCol = columnNameAndItsLocationMap.get(ColumnType.Type.toString());
            Integer jiraCol = columnNameAndItsLocationMap.get(ColumnType.Jira.toString());

            String boardstatus = bodyRow.get(categoryCol).toString();

            JiraStatistic jiraStat = new JiraStatistic(BoardStatusValue.get(boardstatus));

            IssueType type = IssueType.get(bodyRow.get(typeCol).toString());
            Set<String> jirasAddedAsCount = new HashSet<String>();

            double value = 0d;
            Category category = null;
            switch (type) {
               case BUG:
               case PRODISSUE:
               case DATAFIX:
               case MERGE:
                  String jiraString = bodyRow.get(jiraCol).toString();
                  // for the following to work the bodyRows need to be sorted by progression! It currently (at time of writing) is!
                  switch (jiraStat.devStatus()) {
                     case jiraIsInDevelopmentAndInProgress:
                     case jiraIsInDevelopmentAndResolved:
                     case jiraIsInPostDevelopment:
                        jirasAddedAsCount.add(jiraString);
                        value += 1;
                        break;
                  }
                  System.out.println("creating type category: " + type.toString() + " with value " + value + " as jiraDevstatus is " + jiraStat.devStatus() + " for " + jiraString);
                  category = new Category(type.toString(), getColor(type), getPrintPrio(type), true);
                  break;
               case DEV:
               case TEST:
               case STORY:
               case TBD:
                  value += getDouble(bodyRow.get(dEstCol.intValue()));
                  value += getDouble(bodyRow.get(qEstCol.intValue()));
                  category = new Category(boardstatus, getColor(boardstatus), getPrintPrio(boardstatus), false);
                  break;
            }

            addValueToData(burnData, bodyRow, dayInSprintCol, value, category);
         }
      }

      return burnData;
   }

   private void addValueToData(BurnData burnData, Vector<Object> bodyRow, Integer dayInSprintCol, double value, Category category) {
      Object bodyRowDayInSprint = bodyRow.get(dayInSprintCol);
      double dayInSprint = Double.parseDouble(bodyRowDayInSprint.toString());
      burnData.add(category, dayInSprint, value);
   }

   private MapDTO findTheColumnNamesAndTheirLocations(DataCriteria criteria, Vector<String> historicalHeader, Vector<String> dataColumns) {
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

   private Color getColor(IssueType type) {
      switch (type) {
         case BUG:
            return SwingUtil.cellLightRed;
         case DATAFIX:
            return SwingUtil.cellYellow;
         case MERGE:
            return SwingUtil.cellGrey;
         case PRODISSUE:
            return SwingUtil.cellLightLightRed;
      }
      return null;
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

   private int getPrintPrio(IssueType type) {
      switch (type) {
         case BUG:
            return 8;
         case DATAFIX:
            return 9;
         case MERGE:
            return 10;
         default:
            return 11;
      }
   }

}
