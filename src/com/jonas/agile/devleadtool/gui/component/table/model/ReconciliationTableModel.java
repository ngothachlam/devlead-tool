package com.jonas.agile.devleadtool.gui.component.table.model;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.string.StringHelper;
import com.jonas.common.swing.SwingUtil;

public class ReconciliationTableModel extends MyTableModel {

   private static final Column[] columns = { Column.Jira, Column.Release, Column.BoardStatus, Column.Dev_Estimate, Column.Dev_Remain,
         Column.Dev_Actual, Column.QA_Estimate };
   private final MyTable boardTableModel;
   private Map<Integer, String> jiras = new HashMap<Integer, String>();

   public ReconciliationTableModel(MyTable boardTableModel) {
      super(columns);
      this.boardTableModel = boardTableModel;
   }

   @Override
   public Color getColor(Object value, int row, Column column) {
      String jira;
      Integer jiraRow;
      switch (column) {
      case Jira:
         jira = (String) value;
         jiras.put(row, jira);
         if (boardTableModel.isJiraPresent(jira)) {
            return SwingUtil.cellGreen;
         }
         break;
      case Release:
      case BoardStatus:
         System.out.println("Investigate if the BoardStatus is green or red!! (" + this.getClass() + ")");
      case Dev_Estimate:
      case Dev_Remain:
      case Dev_Actual:
      case QA_Estimate:
         jira = jiras.get(row);
         if (!boardTableModel.isJiraPresent(jira)) {
            return null;
         }
         System.out.println("getting " + jira + " for " + column + " where value in this table is " + value);
         Object valueAt = boardTableModel.getValueAt(column, jira);
         if (isEqual(valueAt, value)) {
            return SwingUtil.cellGreen;
         }
         return SwingUtil.cellRed;

      }
      return null;
   }

   boolean isEqual(Object boardValue, Object reconcileValue) {
      System.out.println("boardValue: \"" + boardValue + "\" - reconcileValue: \"" + reconcileValue + "\"");
      if (boardValue == null && reconcileValue == null) {
         return true;
      }
      if (nullTest(boardValue, reconcileValue) || nullTest(reconcileValue, boardValue)) {
         return true;
      }
      Double boardDouble = StringHelper.getDoubleOrNull(boardValue);
      Double reconcileDouble = StringHelper.getDoubleOrNull(reconcileValue);
      System.out.println("boardDouble: \"" + boardDouble + "\" - reconcileDouble: \"" + reconcileDouble + "\"");
      if (boardDouble == null) {
         return boardValue.toString().trim().equals(reconcileValue.toString().trim());
      }
      return boardDouble.equals(reconcileDouble);
   }

   private boolean nullTest(Object valueOne, Object valueTwo) {
      return valueOne == null && valueTwo != null && valueTwo.toString().trim().length() == 0;
   }
}
