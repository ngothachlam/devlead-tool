package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.gui.component.table.model.ReconciliationTableModel;


public class ReconciliationTable extends MyTable {

   private final MyTable boardTableModel;

   public ReconciliationTable(MyTable boardTableModel) {
      super("ReconcileTable", new ReconciliationTableModel(boardTableModel), false);
      this.boardTableModel = boardTableModel;
      
   }

   public void getReconciledData() {
      System.out.println("getReconciledData");
   }

   public void addForReconciliation(String jira, String devEst, String devAct, String release, String remainder, String qaEst,
         BoardStatusValue status) {
      System.out.println("addForReconciliation to " + this.getTitle());
      this.addJira(jira);
   }

}
