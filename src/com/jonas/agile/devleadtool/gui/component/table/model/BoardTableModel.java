package com.jonas.agile.devleadtool.gui.component.table.model;

import java.awt.Color;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.data.BoardStatusValueToJiraStatusMap;
import com.jonas.agile.devleadtool.data.JiraStatistic;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.agile.devleadtool.sprint.SprintTime;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.string.StringHelper;
import com.jonas.common.swing.SwingUtil;

public class BoardTableModel extends MyTableModel {

   private static final Column[] columns = { Column.Jira, Column.Description, Column.Resolution, Column.Release, Column.Merge,
         Column.BoardStatus, Column.Old, Column.DevEst, Column.DevRem, Column.DevAct, Column.QAEst, Column.prio, Column.Note, Column.Sprint };
   private Logger log = MyLogger.getLogger(BoardTableModel.class);
   private BoardCellColorHelper cellColorHelper = BoardCellColorHelper.getInstance();
   private MyTableModel jiraModel;

   public BoardTableModel(SprintCache sprintCache) {
      super(columns);
      setSprintCache(sprintCache);
   }

   public BoardTableModel(Vector<Vector<Object>> contents, Vector<Column> header, SprintCache sprintCache) {
      super(columns, contents, header);
      setSprintCache(sprintCache);
   }

   public void setJiraModel(MyTableModel jiraModel) {
      this.jiraModel = jiraModel;
   }

   @Override
   public Color getColor(Object value, int row, Column column) {
      if (log.isDebugEnabled())
         log.debug("column: " + column + " value: \"" + value + "\" row: " + row);
      String stringValue;

      if (value == null) {
         setToolTipText(row, getColumnIndex(column), "Should be filled out based!");
         return SwingUtil.cellRed;
      }

      switch (column) {
      case Jira:
         if (shouldNotRenderColors() || jiraModel == null)
            return null;
         if (jiraModel.isJiraPresent(value.toString())) {
            setToolTipText(row, getColumnIndex(column), "Exists in the Jira Panel!");
            return SwingUtil.cellGreen;
         }
         break;
      case Resolution:
         stringValue = (String) value;
         if (!isEmptyString(stringValue)) {
            BoardStatusValue boardStatus = (BoardStatusValue) getValueAt(Column.BoardStatus, row);
            if (!BoardStatusValueToJiraStatusMap.isMappedOk(boardStatus, stringValue)) {
               setToolTipText(row, getColumnIndex(column), "Does not match with the BoardStatus value!");
               return SwingUtil.cellRed;
            }
         }
         break;
      case DevRem:
         stringValue = (String) value;
         if (isEmptyString(stringValue)) {
            if (isBoardValueEither(row, cellColorHelper.getRequiredDevRemains())) {
               setToolTipText(row, getColumnIndex(column), "Should be filled out based on the BoardStatus value!");
               return SwingUtil.cellRed;
            }
         } else {
            if (!isBoardValueEither(row, cellColorHelper.getRequiredDevRemains())) {
               setToolTipText(row, getColumnIndex(column), "Should not be filled out based on the BoardStatus value!");
               return SwingUtil.cellRed;
            } else if (StringHelper.isDouble(value) && !StringHelper.isDouble(getValueAt(Column.DevEst, row))) {
               setToolTipText(row, getColumnIndex(column), "Cannot be numeric if Dev Estimate is not!");
               return SwingUtil.cellRed;
            }
         }
         break;
      case Sprint:

         if (getSprintCache() == null) {
            String errorMessage = "Error! No sprint cache defined!!";
            setToolTipText(row, getColumnIndex(column), errorMessage);
            log.error(errorMessage);
            return SwingUtil.cellRed;
         }
         Sprint sprint = getSprintCache().getSprintWithName(value.toString());
         if (log.isDebugEnabled())
            log.debug("Value: " + value + " sprint: " + sprint);
         JiraStatistic jiraStat = getJiraStat(row, column);
         SprintTime sprintTime = sprint.calculateTime();
         switch (jiraStat.devStatus()) {
         case preDevelopment:
            switch (sprintTime) {
            case beforeCurrentSprint:
               setToolTipText(row, getColumnIndex(column), "The jira is in pre-development (" + jiraStat.devStatus()
                     + ") and this sprint is not in the past (" + sprintTime + ")!");
               return SwingUtil.cellRed;
            }
            return null;
         case inDevelopment:
         case inTesting:
            switch (sprintTime) {
            case unKnown:
            case afterCurrentSprint:
            case beforeCurrentSprint:
               setToolTipText(row, getColumnIndex(column), "The jira is in-progress (" + jiraStat.devStatus()
                     + ") and this sprint is not current (" + sprintTime + ")!");
               return SwingUtil.cellRed;
            }
            return null;
         case closed:
            switch (sprintTime) {
            case unKnown:
            case afterCurrentSprint:
               setToolTipText(row, getColumnIndex(column), "The jira is closed (" + jiraStat.devStatus()
                     + ") and this sprint is not current nor in the past (" + sprintTime + ")!");
               return SwingUtil.cellRed;
            }
            return null;
         }
      case DevEst:
         stringValue = (String) value;
         if (isEmptyString(stringValue)) {
            if (isBoardValueEither(row, cellColorHelper.getRequiredDevEstimates())) {
               setToolTipText(row, getColumnIndex(column), "Should be filled out based on the BoardStatus value!");
               return SwingUtil.cellRed;
            }
         } else {

         }
         break;
      case DevAct:
         stringValue = (String) value;
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
         break;
      case QAEst:
         stringValue = (String) value;
         if (isEmptyString(stringValue)) {
            if (isBoardValueEither(row, cellColorHelper.getRequiredQAEstimates())) {
               setToolTipText(row, getColumnIndex(column), "Should be filled out based on the BoardStatus value!");
               return SwingUtil.cellRed;
            }
         }
         break;
      case BoardStatus:
         BoardStatusValue newValue = (BoardStatusValue) value;
         switch (newValue) {
         case Resolved:
         case InQAProgress:
            return SwingUtil.cellLightBlue;
         case Approved:
         case Complete:
         case ForShowCase:
            return SwingUtil.cellLightGreen;
         case Bug:
            setToolTipText(row, getColumnIndex(column), "This is a bug!");
            return SwingUtil.cellLightRed;
         default:
            break;
         }
         break;
      }
      return null;
   }

   private JiraStatistic getJiraStat(int row, Column column) {
      return new JiraStatistic((BoardStatusValue) this.getValueAt(Column.BoardStatus, row));
   }

   private boolean isEmptyString(String stringValue) {
      return stringValue == null || stringValue.trim().length() <= 0;
   }

   public String getRelease(String jira) {
      if (isJiraPresent(jira))
         return (String) getValueAt(Column.Release, jira);
      return "";
   }

   public BoardStatusValue getStatus(String jira) {
      int row = getRowWithJira(jira);
      if (log.isDebugEnabled())
         log.debug("row: " + row + " for jira: " + jira);
      if (row >= 0) {
         BoardStatusValue valueAt = (BoardStatusValue) getValueAt(Column.BoardStatus, jira);
         if (log.isDebugEnabled())
            log.debug("valueat: " + valueAt);
         return valueAt;
      }
      return BoardStatusValue.NA;
   }

   public boolean isBoardValueEither(int row, Set<BoardStatusValue> set) {
      Object value = getValueAt(Column.BoardStatus, row);
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
