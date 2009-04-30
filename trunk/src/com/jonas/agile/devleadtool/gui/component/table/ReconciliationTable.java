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

   public void addForReconciliation(String jira, String devEst, String devAct, String release, String remainder, String qaEst,
         BoardStatusValue status, String qaRem) {
      this.addJira(jira);
      int row = this.getRowWithJira(jira);
      setValueInTableIfNotNull(release, row, Column.Release);
      setValueInTableIfNotNull(status, row, Column.BoardStatus);
      setValueInTableIfNotNull(devEst, row, Column.DEst);
      setValueInTableIfNotNull(remainder, row, Column.DRem);
      setValueInTableIfNotNull(devAct, row, Column.DAct);
      setValueInTableIfNotNull(qaEst, row, Column.QEst);
      setValueInTableIfNotNull(qaRem, row, Column.QRem);
   }

   private void setValueInTableIfNotNull(Object release, int row, Column col) {
      this.setValueAt(release, row, col);
   }

   public boolean isModified(int row, int col) {
      return reconciliationTableModel.isModified(row, col);
   }

}
