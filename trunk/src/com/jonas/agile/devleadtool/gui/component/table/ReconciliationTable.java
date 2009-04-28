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
      this.addJira(jira);
      int row = this.getRowWithJira(jira);
      this.setValueAt(devEst, row, Column.DEst);
      this.setValueAt(devAct, row, Column.DAct);
      this.setValueAt(release, row, Column.Release);
      this.setValueAt(remainder, row, Column.DRem);
      this.setValueAt(qaEst, row, Column.QEst);
      this.setValueAt(status, row, Column.BoardStatus);
   }

   public boolean isModified(int row, int col) {
      return reconciliationTableModel.isModified(row, col);
   }

}
