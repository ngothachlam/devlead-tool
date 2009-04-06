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
         BoardStatusValue status) {
      this.addJira(jira, false);
      int row = this.getRowWithJira(jira);
      this.setValueAt(devEst, row, Column.DevEst);
      this.setValueAt(devAct, row, Column.DevAct);
      this.setValueAt(release, row, Column.Release);
      this.setValueAt(remainder, row, Column.DevRem);
      this.setValueAt(qaEst, row, Column.QAEst);
      this.setValueAt(status, row, Column.BoardStatus);
   }

   public boolean isModified(int row, int col) {
      return reconciliationTableModel.isModified(row, col);
   }

}
