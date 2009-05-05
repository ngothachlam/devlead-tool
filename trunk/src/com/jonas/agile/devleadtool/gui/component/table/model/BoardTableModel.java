package com.jonas.agile.devleadtool.gui.component.table.model;

import java.awt.Color;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.data.BoardStatusValueToJiraStatusMap;
import com.jonas.agile.devleadtool.data.JiraStatistic;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.agile.devleadtool.sprint.SprintTime;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.logging.PerfTimer;
import com.jonas.common.string.StringHelper;
import com.jonas.common.swing.SwingUtil;

public class BoardTableModel extends MyTableModel {

   private static final ColumnType[] columns = { ColumnType.Jira, ColumnType.Description, ColumnType.Resolution, ColumnType.Release, ColumnType.Merge, ColumnType.BoardStatus, ColumnType.Old, ColumnType.DEst, ColumnType.QEst, ColumnType.DRem, ColumnType.QRem, ColumnType.DAct, ColumnType.prio,
         ColumnType.Note, ColumnType.Sprint };
   private Logger log = MyLogger.getLogger(BoardTableModel.class);
   private BoardCellColorHelper cellColorHelper = BoardCellColorHelper.getInstance();
   private MyTableModel jiraModel;

   public BoardTableModel(SprintCache sprintCache) {
      super(columns);
      setSprintCache(sprintCache);
   }

   public BoardTableModel(Vector<Vector<Object>> contents, Vector<ColumnType> header, SprintCache sprintCache) {
      super(columns, contents, header);
      setSprintCache(sprintCache);
   }

   public void setJiraModel(MyTableModel jiraModel) {
      this.jiraModel = jiraModel;
   }

   @Override
   public Color getColor(Object value, int row, ColumnType column) {
      if (log.isDebugEnabled())
         log.debug("column: " + column + " value: \"" + value + "\" row: " + row);

      if (value == null) {
         setToolTipText(row, getColumnIndex(column), "Should be filled out based!");
         return SwingUtil.cellRed;
      }

      String stringValue;
      switch (column) {
         case Release:
            PerfTimer.log("Release 1");
            stringValue = value.toString();
            if (isEmptyString(stringValue)) {
               setToolTipText(row, getColumnIndex(column), "Is empty!");
               return SwingUtil.cellRed;
            }
            PerfTimer.log("Release 2");
            break;
         case Jira:
            PerfTimer.log("Jira 1");
            if (shouldNotRenderColors() || jiraModel == null)
               return null;
            if (jiraModel.isJiraPresent(value.toString())) {
               setToolTipText(row, getColumnIndex(column), "Exists in the Jira Panel!");
               return SwingUtil.cellGreen;
            }
            PerfTimer.log("Jira 2");
            break;
         case Resolution:
            PerfTimer.log("Resolution 1");
            stringValue = value.toString();
            if (!isEmptyString(stringValue)) {
               BoardStatusValue boardStatus = (BoardStatusValue) getValueAt(ColumnType.BoardStatus, row);
               if (!BoardStatusValueToJiraStatusMap.isMappedOk(boardStatus, stringValue)) {
                  setToolTipText(row, getColumnIndex(column), "Does not match with the BoardStatus value!");
                  return SwingUtil.cellRed;
               }
            }
            PerfTimer.log("Resolution 2");
            break;
         case Sprint:
            PerfTimer.log("Sprint 1");
            if (getSprintCache() == null) {
               String errorMessage = "Error! No sprint cache defined!!";
               setToolTipText(row, getColumnIndex(column), errorMessage);
               log.error(errorMessage);
               return SwingUtil.cellRed;
            }
            Sprint sprint = getSprintCache().getSprintWithName(value.toString());
            if (log.isDebugEnabled())
               log.debug("Value: " + value + " sprint: " + sprint);
            JiraStatistic jiraStat = getJiraStat(row);
            if (jiraStat == null) {
               setToolTipText(row, getColumnIndex(column), "BoardStatus is null so we can't calcualte jira stats!!");
               return SwingUtil.cellRed;
            }
            SprintTime sprintTime = sprint.calculateTime();
            switch (jiraStat.devStatus()) {
               case preDevelopment:
                  switch (sprintTime) {
                     case beforeCurrentSprint:
                        setToolTipText(row, getColumnIndex(column), "The jira is in pre-development (" + jiraStat.devStatus() + ") and this sprint is not in the past (" + sprintTime + ")!");
                        return SwingUtil.cellRed;
                  }
                  return null;
               case inDevelopment:
               case inTesting:
                  switch (sprintTime) {
                     case unKnown:
                     case afterCurrentSprint:
                     case beforeCurrentSprint:
                        setToolTipText(row, getColumnIndex(column), "The jira is in-progress (" + jiraStat.devStatus() + ") and this sprint is not current (" + sprintTime + ")!");
                        return SwingUtil.cellRed;
                  }
                  return null;
               case closed:
                  switch (sprintTime) {
                     case unKnown:
                     case afterCurrentSprint:
                        setToolTipText(row, getColumnIndex(column), "The jira is closed (" + jiraStat.devStatus() + ") and this sprint is not current nor in the past (" + sprintTime + ")!");
                        return SwingUtil.cellRed;
                  }
                  return null;
            }
            PerfTimer.log("Sprint 2");
            break;
         case DEst:
            PerfTimer.log("Dest 1");
            stringValue = value.toString();
            if (isEmptyString(stringValue)) {
               if (isBoardValueEither(row, cellColorHelper.getRequiredDevEstimates())) {
                  setToolTipText(row, getColumnIndex(column), "Should be filled out based on the BoardStatus value!");
                  return SwingUtil.cellRed;
               }
            }
            PerfTimer.log("Dest 2");
            break;
         case QEst:
            PerfTimer.log("QEst 1");
            stringValue = value.toString();
            if (isEmptyString(stringValue)) {
               if (isBoardValueEither(row, cellColorHelper.getRequiredQAEstimates())) {
                  setToolTipText(row, getColumnIndex(column), "Should be filled out based on the BoardStatus value!");
                  return SwingUtil.cellRed;
               }
            }
            PerfTimer.log("QEst 2");
            break;
         case DRem:
            PerfTimer.log("DRem 1");
            stringValue = value.toString();
            if (isEmptyString(stringValue)) {
               if (isBoardValueEither(row, cellColorHelper.getRequiredDevRemains())) {
                  setToolTipText(row, getColumnIndex(column), "Should be filled out based on the BoardStatus value!");
                  return SwingUtil.cellRed;
               }
            } else {
               if (!isBoardValueEither(row, cellColorHelper.getRequiredDevRemains())) {
                  setToolTipText(row, getColumnIndex(column), "Should not be filled out based on the BoardStatus value!");
                  return SwingUtil.cellRed;
               } else if (StringHelper.isDouble(value) && !StringHelper.isDouble(getValueAt(ColumnType.DEst, row))) {
                  setToolTipText(row, getColumnIndex(column), "Cannot be numeric if the Dev Estimate is not!");
                  return SwingUtil.cellRed;
               }
            }
            PerfTimer.log("DRem 2");
            break;
         case QRem:
            PerfTimer.log("QRem 1");
            stringValue = value.toString();
            if (isEmptyString(stringValue)) {
               if (isBoardValueEither(row, cellColorHelper.getRequiredQARemains())) {
                  setToolTipText(row, getColumnIndex(column), "Should be filled out based on the BoardStatus value!");
                  return SwingUtil.cellRed;
               }
            } else {
               if (!isBoardValueEither(row, cellColorHelper.getRequiredQARemains())) {
                  setToolTipText(row, getColumnIndex(column), "Should not be filled out based on the BoardStatus value!");
                  return SwingUtil.cellRed;
               } else if (StringHelper.isDouble(value) && !StringHelper.isDouble(getValueAt(ColumnType.QEst, row))) {
                  setToolTipText(row, getColumnIndex(column), "Cannot be numeric if the QA Estimate is not!");
                  return SwingUtil.cellRed;
               }
            }
            PerfTimer.log("QRem 2");
            break;
         case DAct:
            PerfTimer.log("DAct 1");
            stringValue = value.toString();
            if (isEmptyString(stringValue)) {
               if (isBoardValueEither(row, cellColorHelper.getRequiredDevActuals())) {
                  setToolTipText(row, getColumnIndex(column), "Should be filled out based on the BoardStatus value!");
                  return SwingUtil.cellRed;
               }
            } else {
               if (isBoardValueEither(row, cellColorHelper.getRequiredBlankDevActuals())) {
                  setToolTipText(row, getColumnIndex(column), "Should not be filled out based on the BoardStatus value!");
                  return SwingUtil.cellRed;
               }
            }
            PerfTimer.log("DAct 2");
            break;
         case BoardStatus:
            PerfTimer.log("BoardStatus 1");
            BoardStatusValue newValue = (BoardStatusValue) value;
            if (log.isDebugEnabled()) {
               log.debug("boardStatus is " + newValue);
            }
            switch (newValue) {
               case InProgress:
                  if (log.isDebugEnabled()) {
                     log.debug("in progress");
                  }
                  return SwingUtil.cellLightYellow;
               case Bug:
                  setToolTipText(row, getColumnIndex(column), "This is a bug!");
                  return SwingUtil.cellLightRed;
               case Resolved:
                  return SwingUtil.cellLightBlue;
               case Approved:
               case Complete:
               case ForShowCase:
                  return SwingUtil.cellLightGreen;
            }
            PerfTimer.log("BoardStatus 2");
            break;
      }
      return null;
   }

   private JiraStatistic getJiraStat(int row) {
      Object valueAt = this.getValueAt(ColumnType.BoardStatus, row);
      if (log.isDebugEnabled())
         log.debug("Getting BoardStatus value for row " + row + " is " + valueAt);
      if (valueAt == null)
         return null;
      return new JiraStatistic((BoardStatusValue) valueAt);
   }

   private boolean isEmptyString(String stringValue) {
      return stringValue == null || stringValue.trim().length() <= 0;
   }

   public BoardStatusValue getStatus(String jira) {
      int row = getRowWithJira(jira);
      if (log.isDebugEnabled())
         log.debug("row: " + row + " for jira: " + jira);
      if (row >= 0) {
         BoardStatusValue valueAt = (BoardStatusValue) getValueAt(ColumnType.BoardStatus, jira);
         if (log.isDebugEnabled())
            log.debug("valueat: " + valueAt);
         return valueAt;
      }
      return BoardStatusValue.NA;
   }

   public boolean isBoardValueEither(int row, Set<BoardStatusValue> set) {
      Object value = getValueAt(ColumnType.BoardStatus, row);
      boolean contains = set.contains(value);
      Iterator<BoardStatusValue> iter = set.iterator();
      while (iter.hasNext()) {
         BoardStatusValue type = iter.next();
         if (log.isDebugEnabled())
            log.debug("\tset contains: " + type);
      }
      return contains;
   }
}
