package com.jonas.agile.devleadtool.gui.component.table.model;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.data.BoardStatusValueToJiraStatusMap;
import com.jonas.agile.devleadtool.data.JiraStatistic;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.Environment;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;
import com.jonas.agile.devleadtool.gui.component.table.column.Project;
import com.jonas.agile.devleadtool.gui.component.table.model.color.EstimateValidator;
import com.jonas.agile.devleadtool.gui.component.table.model.color.Validator;
import com.jonas.agile.devleadtool.gui.component.table.model.color.ValidatorResponseType;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.agile.devleadtool.sprint.SprintTime;
import com.jonas.common.CalculatorHelper;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.SwingUtil;

public class BoardTableModel extends MyTableModel implements ValueGetter {

   private static final ColorAndNullCheck NOTCHECKED_THUSNOCOLOR = new ColorAndNullCheck(null, false);
   private static final ColorAndNullCheck CHECKED_NOCOLOR = new ColorAndNullCheck(null, true);
   private static final ColorAndNullCheck CHECKED_REDCOLOR = new ColorAndNullCheck(SwingUtil.cellRed, true);
   private final static Logger log = MyLogger.getLogger(BoardTableModel.class);
   private final static Set<String> nonAcceptedJiraFields = new HashSet<String>();
   static {
      nonAcceptedJiraFields.add("TBD");
   }
   private static final ColumnType[] columns = {
         ColumnType.Jira,
         ColumnType.Description,
         ColumnType.Type,
         ColumnType.Resolution,
         ColumnType.Release,
         ColumnType.Merge,
         ColumnType.BoardStatus,
         ColumnType.Old,
         ColumnType.DEst,
         ColumnType.QEst,
         ColumnType.DRem,
         ColumnType.QRem,
         ColumnType.DAct,
         ColumnType.QAct,
         ColumnType.prio,
         ColumnType.Note,
         ColumnType.Sprint,
         ColumnType.Owner_M,
         ColumnType.Project_M,
         ColumnType.Environment_M };

   private MyTableModel jiraModel;
   private EstimateValidator estimateValidator = EstimateValidator.getInstance();
   private ValidatorManager validatorManager = new ValidatorManager();

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
   public Color getColor(Object value, int row, ColumnType column, int colNo) {
      if (log.isDebugEnabled())
         log.debug("column: " + column + " value: \"" + value + "\" row: " + row);

      if (value == null) {
         setToolTipText(row, colNo, "Is null!");
         return SwingUtil.cellRed;
      }

      String stringValue;
      switch (column) {
      case Type:
         if (value.equals(IssueType.TBD)) {
            setToolTipText(row, colNo, "Cannot be TBD - define it!");
            return SwingUtil.cellRed;
         }
         break;
      case Release:
         stringValue = value.toString();
         if (isEmptyString(stringValue)) {
            setToolTipText(row, colNo, "Is empty!");
            return SwingUtil.cellRed;
         }
         break;
      case Jira:
         if (shouldNotRenderColors() || jiraModel == null)
            return null;
         if (jiraModel.isJiraPresent(value.toString())) {
            setToolTipText(row, colNo, "Exists in the Jira Panel!");
            return SwingUtil.cellGreen;
         }
         break;
      case Resolution:
         stringValue = value.toString();
         if (!isEmptyString(stringValue)) {
            BoardStatusValue boardStatus = (BoardStatusValue) getValueAt(ColumnType.BoardStatus, row);
            if (!BoardStatusValueToJiraStatusMap.isMappedOk(boardStatus, stringValue)) {
               setToolTipText(row, colNo, "Does not match with the BoardStatus value (" + getObjectAsNonNull(boardStatus) + ")!");
               return SwingUtil.cellRed;
            }
         }
         break;
      case Sprint:
         if (getSprintCache() == null) {
            String errorMessage = "Error! No sprint cache defined!!";
            setToolTipText(row, colNo, errorMessage);
            log.error(errorMessage);
            return SwingUtil.cellRed;
         }
         Sprint sprint = getSprintCache().getSprintWithName(value.toString());
         if (log.isDebugEnabled())
            log.debug("Value: " + value + " sprint: " + sprint);
         JiraStatistic jiraStat = getJiraStat(row);
         if (jiraStat == null) {
            setToolTipText(row, colNo, "BoardStatus is null so we can't calcualte jira stats!!");
            return SwingUtil.cellRed;
         }
         SprintTime sprintTime = sprint.calculateTime();

         if (sprint == SprintCache.EMPTYSPRINT) {
            switch (jiraStat.devStatus()) {
            case jiraIsInDevelopmentAndOpen:
            case jiraIsInDevelopmentAndInProgress:
            case jiraIsInDevelopmentAndResolved:
               setToolTipText(row, colNo, "Sprint cannot be empty if jira is in Development!");
               return SwingUtil.cellRed;
            case jiraIsInPostDevelopment:
               setToolTipText(row, colNo, "Sprint cannot be empty if jira has been completed!");
               return SwingUtil.cellRed;
            }
         }

         switch (sprintTime) {
         case sprintIsInThePast:
            switch (jiraStat.devStatus()) {
            case jiraIsInPreDevelopment:
               setToolTipText(row, colNo, "The jira " + getValueAt(ColumnType.Jira, row) + " is in pre-development (" + jiraStat.devStatus()
                     + ") and the sprint is not in the future nor current (" + sprintTime + ")!");
               return SwingUtil.cellRed;
            case jiraIsInDevelopmentAndOpen:
            case jiraIsInDevelopmentAndInProgress:
               setToolTipText(row, colNo, "The jira " + getValueAt(ColumnType.Jira, row) + " is in-progress (" + jiraStat.devStatus()
                     + ") and the sprint is not current (" + sprintTime + ")!");
               return SwingUtil.cellRed;
            }
            setToolTipText(row, colNo, "Is before current sprint!");
            return SwingUtil.cellLightGreen;
         case currentSprint:
            switch (jiraStat.devStatus()) {
            case jiraIsInPreDevelopment:
               break;
            case jiraIsInDevelopmentAndOpen:
            case jiraIsInDevelopmentAndInProgress:
               break;
            case jiraIsInPostDevelopment:
               break;
            }
            setToolTipText(row, colNo, "Is current sprint!");
            return SwingUtil.cellLightYellow;
         case sprintIsInTheFuture:
            switch (jiraStat.devStatus()) {
            case jiraIsInPreDevelopment:
               break;
            case jiraIsInDevelopmentAndOpen:
            case jiraIsInDevelopmentAndInProgress:
               setToolTipText(row, colNo, "The jira " + getValueAt(ColumnType.Jira, row) + " is in-progress (" + jiraStat.devStatus()
                     + ") and this sprint is not current (" + sprintTime + ")!");
               return SwingUtil.cellRed;
            case jiraIsInPostDevelopment:
               setToolTipText(row, colNo, "The jira " + getValueAt(ColumnType.Jira, row) + " is closed (" + jiraStat.devStatus()
                     + ") and this sprint is not current nor in the past (" + sprintTime + ")!");
               return SwingUtil.cellRed;
            }
            setToolTipText(row, colNo, "Is after current sprint!");
            return SwingUtil.cellLightRed;
         }
         return null;
      case DEst:
      case QEst:
      case DRem:
      case QRem:
      case DAct:
      case QAct:
         Validator validator = estimateValidator.getValidator(column);
         ValidatorResponse response = validatorManager.validate(validator, value, row, this);
         if (response.getType() == ValidatorResponseType.FAIL) {
            setToolTipText(row, colNo, response.getMessage());
            return SwingUtil.cellRed;
         }
         return null;
      case BoardStatus:
         BoardStatusValue newValue = (BoardStatusValue) value;
         if (log.isDebugEnabled()) {
            log.debug("boardStatus is " + newValue);
         }
         switch (newValue) {
         case UnKnown:
            setToolTipText(row, colNo, "This is set to unKnown!");
            return SwingUtil.cellRed;
         case InProgress:
            if (log.isDebugEnabled()) {
               log.debug("in progress");
            }
            setToolTipText(row, colNo, "This is in Progress!");
            return SwingUtil.cellLightYellow;
         case Failed:
            setToolTipText(row, colNo, "This is a Bug!");
            return SwingUtil.cellLightRed;
         case Resolved:
            setToolTipText(row, colNo, "This is Resolved!");
            return SwingUtil.cellLightBlue;
         case Approved:
            setToolTipText(row, colNo, "This is Approved!");
            return SwingUtil.cellLightGreen;
         case Complete:
            setToolTipText(row, colNo, "This is Complete!");
            return SwingUtil.cellLightGreen;
         case ForShowCase:
            setToolTipText(row, colNo, "This is ForShowCase!");
            return SwingUtil.cellLightGreen;
         }
         break;
      case Owner_M:
         if (value == null || value.toString().trim().length() == 0) {
            setToolTipText(row, colNo, "Cannot be empty!");
            return SwingUtil.cellLightGreen;
         }
         break;
      case Project_M:
         if (value.equals(Project.TBD)) {
            setToolTipText(row, colNo, "Cannot be TBD - define it!");
            return SwingUtil.cellRed;
         }
         break;
      case Environment_M:
         if (value.equals(Environment.TBD)) {
            setToolTipText(row, colNo, "Cannot be TBD - define it!");
            return SwingUtil.cellRed;
         }
         break;
      }
      return null;
   }

   private String getObjectAsNonNull(Object object) {
      return object == null ? "<NULL>" : object.toString();
   }

   private ColorAndNullCheck preNullColor(Object value, int row, ColumnType column) {
      switch (column) {
      case FixVersion:
         Object bRel = this.getValueAt(ColumnType.Release, row);
         if (!isFixVersionOk(bRel, value)) {
            setToolTipText(row, getColumnIndex(column), "This  incorrectly filled out based on the Board's Release value (" + bRel + ")!");
            return CHECKED_REDCOLOR;
         }
         return CHECKED_NOCOLOR;
      case J_Sprint:
         Object bSprint = this.getValueAt(ColumnType.Sprint, row);
         if (!isSprintOk(bSprint, value)) {
            setToolTipText(row, getColumnIndex(column), "This  incorrectly filled out based on the Board's Sprint value (" + bSprint + ")!");
            return CHECKED_REDCOLOR;
         }
         return CHECKED_NOCOLOR;
      case Project:
         if (!isProjectOk(value)) {
            setToolTipText(row, getColumnIndex(column), "Should not be empty!");
            return CHECKED_REDCOLOR;
         }
         return CHECKED_NOCOLOR;
      case J_DevEst:
         Object dEst = this.getValueAt(ColumnType.DEst, row);
         if (!isJiraNumberOk(dEst, value)) {
            setToolTipText(row, getColumnIndex(column), "Is incorrectly filled out based on the BoardStatus value (" + dEst + ")!");
            return CHECKED_REDCOLOR;
         }
         return CHECKED_NOCOLOR;
      case J_DevAct:
         Object dAct = this.getValueAt(ColumnType.DAct, row);
         if (!isJiraNumberOk(dAct, value)) {
            setToolTipText(row, getColumnIndex(column), "Is incorrectly filled out based on the BoardStatus value (" + dAct + ")!");
            return CHECKED_REDCOLOR;
         }
         return CHECKED_NOCOLOR;
      case Delivery:
         if (false) {
            setToolTipText(row, getColumnIndex(column), "Not implemented yet!!");
            return CHECKED_REDCOLOR;
         }
         return CHECKED_NOCOLOR;
      }
      return NOTCHECKED_THUSNOCOLOR;
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

   boolean isBoardValueEither(int row, Set<BoardStatusValue> set, Object boardStatus) {
      boolean contains = set.contains(boardStatus);
      if (log.isDebugEnabled()) {
         Iterator<BoardStatusValue> iter = set.iterator();
         while (iter.hasNext()) {
            BoardStatusValue type = iter.next();
            log.debug("\tset contains: " + type);
         }
      }
      return contains;
   }

   boolean isProjectOk(Object value) {
      if (value == null) {
         return false;
      }

      if (nonAcceptedJiraFields.contains(value.toString())) {
         return false;
      }

      return value.toString().trim().length() != 0;
   }

   boolean isSprintOk(Object boardSprint, Object value) {
      if (value == null || boardSprint == null) {
         return false;
      }

      if (nonAcceptedJiraFields.contains(value.toString())) {
         return false;
      }

      return value.toString().trim().equalsIgnoreCase(boardSprint.toString());
   }

   boolean isFixVersionOk(Object boardValue, Object jiraValue) {
      if (log.isDebugEnabled())
         log.debug("boardValue: \"" + boardValue + "\" jiraValue: " + jiraValue);
      if (jiraValue == null) {
         if (boardValue == null || boardValue.toString().trim().length() == 0)
            return true;
         return false;
      }

      String jiraFixVersions = jiraValue.toString();
      String boardValueAsString = boardValue.toString();
      return jiraFixVersions.equalsIgnoreCase(boardValueAsString);
   }

   boolean isJiraNumberOk(Object boardValue, Object jiraValue) {
      if (log.isDebugEnabled())
         log.debug("... We are trying to check if either the board or jira has numberical or string values (boardValue: " + boardValue
               + ", jiraValue: " + jiraValue + ")");
      String boardString = boardValue == null ? null : boardValue.toString().trim();
      String jiraString = jiraValue == null ? null : jiraValue.toString().trim();

      if (boardString == null && jiraString == null) {
         return true;
      } else if (boardString == null) {
         return isDoubleValueNullOrZero(jiraString);
      } else if (jiraString == null) {
         return isDoubleValueNullOrZero(boardString);
      }

      if (boardValue.equals(jiraValue))
         return true;

      Double boardDouble = null;
      Double jiraDouble = null;

      boardDouble = CalculatorHelper.getDouble(boardString);
      jiraDouble = CalculatorHelper.getDouble(jiraString);

      if (boardDouble == null || boardDouble == 0d) {
         if ((jiraDouble == null || jiraDouble == 0d))
            return true;
         return false;
      }
      if (boardDouble.equals(jiraDouble))
         return true;
      return false;
   }

   private boolean isDoubleValueNullOrZero(String jiraString) {
      Double jiraDouble;
      jiraDouble = CalculatorHelper.getDouble(jiraString);
      if (jiraDouble == null || jiraDouble == 0d) {
         return true;
      }
      return false;
   }

   @Override
   public BoardStatusValue getBoardStatus(int row) {
      if (log.isDebugEnabled()) {
         log.debug("finding boardStatus for row " + row);
      }
      return (BoardStatusValue) getValueAt(ColumnType.BoardStatus, row);
   }

   @Override
   public IssueType getType(int row) {
      if (log.isDebugEnabled()) {
         log.debug("finding type for row " + row);
      }
      return (IssueType) getValueAt(ColumnType.Type, row);
   }
}


class ColorAndNullCheck {

   private final boolean handledAlready;
   private final Color color;

   public ColorAndNullCheck(Color color, boolean handledAlready) {
      this.color = color;
      this.handledAlready = handledAlready;
   }

   public Color getColor() {
      return color;
   }

   public boolean isAlreadyChecked() {
      return handledAlready;
   }
}