package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PlanTableModel extends MyTableModel {

   private static Vector<String> columnNames = new Vector<String>();

   static {
      columnNames.add("Jira");
      columnNames.add("FixVersion");
      columnNames.add("Status");
      columnNames.add("Resolution");
   }

   public PlanTableModel() {
      super(columnNames, 0);
   }

   public PlanTableModel(Vector<Vector<Object>> contents, Vector<Object> header) {
      super(contents, header);
   }
   
   public void addEmptyRow() {
   }

   public void removeSelectedRows(JTable table) {
   }

   public boolean isRed(Object value, int row, int column) {
      return false;
   }

}
