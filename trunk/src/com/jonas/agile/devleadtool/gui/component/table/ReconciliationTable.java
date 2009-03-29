package com.jonas.agile.devleadtool.gui.component.table;


public class ReconciliationTable extends MyTable {

   private final MyTable boardTableModel;

   public ReconciliationTable(MyTable boardTableModel) {
      super("ReconcileTable", false);
      this.boardTableModel = boardTableModel;
      
   }

}
