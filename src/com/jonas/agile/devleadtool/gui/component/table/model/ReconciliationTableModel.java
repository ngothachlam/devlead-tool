package com.jonas.agile.devleadtool.gui.component.table.model;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
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
         if ((valueAt == null && value == null) || valueAt.equals(value)) {
            return SwingUtil.cellGreen;
         } else {
            return SwingUtil.cellRed;
         }
      }
      return null;
   }

}
