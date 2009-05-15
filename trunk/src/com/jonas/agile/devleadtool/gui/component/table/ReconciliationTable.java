package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
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

   public void addForReconciliation(String jira, String dEst, String dAct, String release, String dRem, String qEst, BoardStatusValue status, String qRem, String qAct) {
      this.addJira(jira);
      int row = this.getRowWithJira(jira);
      setValueInTableIfNotNull(release, row, ColumnType.Release);
      setValueInTableIfNotNull(status, row, ColumnType.BoardStatus);
      setValueInTableIfNotNull(dEst, row, ColumnType.DEst);
      setValueInTableIfNotNull(dRem, row, ColumnType.DRem);
      setValueInTableIfNotNull(dAct, row, ColumnType.DAct);
      setValueInTableIfNotNull(qEst, row, ColumnType.QEst);
      setValueInTableIfNotNull(qRem, row, ColumnType.QRem);
      setValueInTableIfNotNull(qAct, row, ColumnType.QAct);
   }

   private void setValueInTableIfNotNull(Object value, int row, ColumnType col) {
      if (value != null)
         this.setValueAt(value, row, col);
   }

   public boolean isModified(int row, int col) {
      return reconciliationTableModel.isModified(row, col);
   }

}
