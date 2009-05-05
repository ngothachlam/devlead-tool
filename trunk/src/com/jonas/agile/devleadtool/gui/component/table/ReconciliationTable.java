package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.gui.component.table.model.ReconciliationTableModel;

public class ReconciliationTable extends MyTable {

   private static ReconciliationTableModel reconciliationTableModel;

   public ReconciliationTable(MyTable boardTableModel) {
      super("ReconcileTable", getModel(boardTableModel), false);
   }

   private static ReconciliationTableModel getModel(MyTable boardTableModel) {
      reconciliationTableModel = new ReconciliationTableModel(boardTableModel);
      return reconciliationTableModel;
   }

   public void addForReconciliation(String jira, String devEst, String devAct, String release, String remainder, String qaEst, BoardStatusValue status, String qaRem) {
      this.addJira(jira);
      int row = this.getRowWithJira(jira);
      setValueInTableIfNotNull(release, row, ColumnType.Release);
      setValueInTableIfNotNull(status, row, ColumnType.BoardStatus);
      setValueInTableIfNotNull(devEst, row, ColumnType.DEst);
      setValueInTableIfNotNull(remainder, row, ColumnType.DRem);
      setValueInTableIfNotNull(devAct, row, ColumnType.DAct);
      setValueInTableIfNotNull(qaEst, row, ColumnType.QEst);
      setValueInTableIfNotNull(qaRem, row, ColumnType.QRem);
   }

   private void setValueInTableIfNotNull(Object value, int row, ColumnType col) {
      if (value != null)
         this.setValueAt(value, row, col);
   }

   public boolean isModified(int row, int col) {
      return reconciliationTableModel.isModified(row, col);
   }

}
