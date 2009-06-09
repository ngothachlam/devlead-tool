package com.jonas.agile.devleadtool.gui.component.table.model;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.string.StringHelper;
import com.jonas.common.swing.SwingUtil;

public class ReconciliationTableModel extends MyTableModel {

   private static final ColumnType[] columns = { ColumnType.Jira, ColumnType.Release, ColumnType.BoardStatus, ColumnType.DEst, ColumnType.QEst, ColumnType.DRem, ColumnType.QRem, ColumnType.DAct, ColumnType.QAct };
   private final MyTable boardTableModel;
   private Map<Integer, String> jiras = new HashMap<Integer, String>();

   public ReconciliationTableModel(MyTable boardTableModel) {
      super(columns);
      this.boardTableModel = boardTableModel;
   }

   @Override
   // FIXME 1 - put this into a highlighter!
   public Color getColor(Object value, int row, ColumnType column, int colNo) {
      String jira;
      switch (column) {
         case Jira:
            jira = (String) value;
            jiras.put(row, jira);
            if (boardTableModel.isJiraPresent(jira)) {
               return SwingUtil.cellGreen;
            }
            break;
         case BoardStatus:
         case Release:
         case DEst:
         case DRem:
         case DAct:
         case QEst:
         case QRem:
         case QAct:
            jira = jiras.get(row);
            if (!boardTableModel.isJiraPresent(jira)) {
               return null;
            }
            if (!isModified(value)) {
               return null;
            }
            Object boardValue = boardTableModel.getValueAt(column, jira);
            if (isEqual(boardValue, value)) {
               return SwingUtil.cellGreen;
            }
            setToolTipText(row, colNo, getStringForToolTip(value, boardValue));
            return SwingUtil.cellRed;
      }
      return null;
   }

   private boolean isModified(Object value) {
      return !isEmpty(value);
   }

   public boolean isModified(int row, int col) {
      return isModified(getValueAt(row, col));
   }

   private String getStringForToolTip(Object value, Object boardValue) {
      return "\"" + value + "\" is not equal to the board's \"" + boardValue == null ? "<null>" : boardValue.toString() + "\"";
   }

   boolean isEqual(Object boardValue, Object reconcileValue) {
      if (isEmpty(boardValue) && isEmpty(reconcileValue)) {
         return true;
      }

      if ((isEmpty(boardValue) && !isEmpty(reconcileValue)) || (!isEmpty(boardValue) && isEmpty(reconcileValue))) {
         return false;
      }

      Double boardDouble = StringHelper.getDoubleOrNull(boardValue);
      Double reconcileDouble = StringHelper.getDoubleOrNull(reconcileValue);
      if (boardDouble == null) {
         return boardValue.toString().trim().toUpperCase().equals(reconcileValue.toString().trim().toUpperCase());
      }

      return boardDouble.equals(reconcileDouble);
   }

   private boolean isEmpty(Object valueOne) {
      return valueOne == null || valueOne.toString().trim().length() == 0;
   }

}
