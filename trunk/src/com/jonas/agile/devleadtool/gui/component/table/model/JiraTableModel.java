package com.jonas.agile.devleadtool.gui.component.table.model;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.data.BoardStatusValueToJiraStatusMap;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.TBDEnum;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.Environment;
import com.jonas.agile.devleadtool.gui.component.table.column.Owner;
import com.jonas.agile.devleadtool.gui.component.table.column.Project;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.common.CalculatorHelper;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.SwingUtil;

public class JiraTableModel extends MyTableModel {

   private static final ColumnType[] columns = { ColumnType.Jira, ColumnType.Description, ColumnType.J_Type, ColumnType.J_Sprint, ColumnType.Project, ColumnType.FixVersion, ColumnType.Owner, ColumnType.Environment, ColumnType.Delivery,
         ColumnType.Resolution, ColumnType.BuildNo, ColumnType.J_DevEst, ColumnType.J_DevAct, ColumnType.prio };

   private Logger log = MyLogger.getLogger(JiraTableModel.class);
   private MyTableModel boardModel;
   private int jiraRowInBoardModel;

   public JiraTableModel() {
      super(columns);
   }

   public JiraTableModel(Vector<Vector<Object>> contents, Vector<ColumnType> header) {
      super(columns, contents, header);
   }

   public void setBoardModel(MyTableModel boardModel) {
      this.boardModel = boardModel;
   }

   @Override
   public Color getColor(Object value, int row, ColumnType column, int colNo) {
      if (shouldNotRenderColors()) {
         return null;
      }

      if (boardModel == null) {
         return null;
      }
      if (log.isDebugEnabled())
         log.debug("The row getting color for is " + row + " (col: " + column + ") ");
      if (colNo == 0) {
         String jira = getValueAt(ColumnType.Jira, row).toString();
         jiraRowInBoardModel = boardModel.getRowWithJira(jira);
         if (log.isDebugEnabled())
            log.debug("... so we are editing a new row! Lets get the jira, which is " + jira + " and the board row for this jira: " + jiraRowInBoardModel);
      }

      if (jiraRowInBoardModel == -1)
         return null;

      if (log.isDebugEnabled())
         log.debug("... The Jira was found in the board. We now want to check the " + column + " as it's value is \"" + value + "\"");
      switch (column) {
         case Jira:
            setToolTipText(row, colNo, "Exists in the board!");
            return SwingUtil.cellGreen;
         case Resolution:
            String stringValue = value.toString();
            if (!isEmptyString(stringValue)) {
               BoardStatusValue boardStatus = (BoardStatusValue) boardModel.getValueAt(ColumnType.BoardStatus, row);
               if (!BoardStatusValueToJiraStatusMap.isMappedOk(boardStatus, stringValue)) {
                  setToolTipText(row, colNo, "Does not match with the Board value (" + getObjectAsNonNull(boardStatus) + ")!");
                  return SwingUtil.cellRed;
               }
            }
            break;

         case J_Type:
            Object type = boardModel.getValueAt(ColumnType.Type, jiraRowInBoardModel);
            if (!isTypeOk(type, value)) {
               return SwingUtil.cellRed;
            }
            break;
         case FixVersion:
            Object bRel = boardModel.getValueAt(ColumnType.Release, jiraRowInBoardModel);
            if (!isFixVersionOk(bRel, value)) {
               setToolTipText(row, colNo, "Does not match with the Board value (" + bRel + ")!");
               return SwingUtil.cellRed;
            }
            break;
         case J_Sprint:
            Sprint bSprint = (Sprint) boardModel.getValueAt(ColumnType.Sprint, jiraRowInBoardModel);
            if (!areDropDownValuesEqual(bSprint, value)) {
               setToolTipText(row, colNo, "Does not match with the Board value (" + bSprint + ")!");
               return SwingUtil.cellRed;
            }
            break;
         case J_DevEst:
            Object dEst = boardModel.getValueAt(ColumnType.DEst, jiraRowInBoardModel);
            if (!isJiraNumberOk(dEst, value)) {
               setToolTipText(row, colNo, "Does not match with the Board value (" + dEst + ")!");
               return SwingUtil.cellRed;
            }
            break;
         case J_DevAct:
            Object dAct = boardModel.getValueAt(ColumnType.DAct, jiraRowInBoardModel);
            if (!isJiraNumberOk(dAct, value)) {
               setToolTipText(row, colNo, "Does not match with the Board value (" + dAct + ")!");
               return SwingUtil.cellRed;
            }
            break;
         case Project:
            Project bproject = (Project) boardModel.getValueAt(ColumnType.Project_M, jiraRowInBoardModel);
            if (!areDropDownValuesEqual(bproject, value)) {
               setToolTipText(row, colNo, "Does not match with the Board value (" + bproject + ")!");
               return SwingUtil.cellRed;
            }
            break;
         case Owner:
            Owner bowner = (Owner) boardModel.getValueAt(ColumnType.Owner_M, jiraRowInBoardModel);
            if (!areDropDownValuesEqual(bowner, value)) {
               setToolTipText(row, colNo, "Does not match with the Board value (" + bowner + ")!");
               return SwingUtil.cellRed;
            }
            break;
         case Environment:
            Environment benvironment = (Environment) boardModel.getValueAt(ColumnType.Environment_M, jiraRowInBoardModel);
            if (!areDropDownValuesEqual(benvironment, value)) {
               setToolTipText(row, colNo, "Does not match with the Board value (" + benvironment + ")!");
               return SwingUtil.cellRed;
            }
            break;
         case Delivery:
            break;
      }
      return null;
   }

   boolean areDropDownValuesEqual(TBDEnum boardObject, Object jiraObject) {
      if (jiraObject == null || boardObject == null) {
         return false;
      }

      if (boardObject.getTBD().toString().equals(jiraObject.toString())) {
         return false;
      }

      return jiraObject.toString().trim().equalsIgnoreCase(boardObject.toString().trim());
   }

   boolean isFixVersionOk(Object boardValue, Object jiraValue) {
      return areValuesAsStringsEqual(boardValue, jiraValue);
   }

   private boolean isTypeOk(Object boardValue, Object jiraValue) {
      return areValuesAsStringsEqual(boardValue, jiraValue);
   }
   
   private boolean areValuesAsStringsEqual(Object boardValue, Object jiraValue) {
      if (log.isDebugEnabled())
         log.debug("Fix Version boardValue: \"" + boardValue + "\" jiraValue: " + jiraValue);
      if (jiraValue == null || jiraValue.toString().trim().length() == 0) {
         if (boardValue == null || boardValue.toString().trim().length() == 0)
            return true;
         return false;
      }
      if (boardValue == null) {
         return false;
      }

      String jiraValueAsString = jiraValue.toString();
      String boardValueAsString = boardValue.toString();
      return jiraValueAsString.equalsIgnoreCase(boardValueAsString);
   }

   boolean isJiraNumberOk(Object boardValue, Object jiraValue) {
      if (log.isDebugEnabled())
         log.debug("... We are trying to check if either the board or jira has numberical or string values (boardValue: " + boardValue + ", jiraValue: " + jiraValue + ")");
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

}
