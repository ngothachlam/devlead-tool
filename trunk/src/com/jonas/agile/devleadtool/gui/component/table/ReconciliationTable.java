package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.gui.component.table.model.ReconciliationTableModel;


public class ReconciliationTable extends MyTable {

   private final MyTable boardTableModel;

   public ReconciliationTable(MyTable boardTableModel) {
      super("ReconcileTable", new ReconciliationTableModel(boardTableModel), false);
      this.boardTableModel = boardTableModel;
      
   }

   public void getReconciledData() {
      // TODO Auto-generated method stub
      throw new RuntimeException("Method not implemented yet!");
   }

}
