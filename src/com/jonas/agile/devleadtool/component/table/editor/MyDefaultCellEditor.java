package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MyDefaultCellEditor extends DefaultCellEditor implements MyEditor {


   private int colEdited;
   private int rowEdited;

   public MyDefaultCellEditor(JTextField field) {
      super(field);
   }
   
   @Override
   public int getColEdited() {
      return colEdited;
   }

   @Override
   public int getRowEdited() {
      return rowEdited;
   }

   @Override
   public Object getValue() {
      return getCellEditorValue();
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      rowEdited = row;
      colEdited = column;
      return super.getTableCellEditorComponent(table, value, isSelected, row, column);
   }
   
   
}