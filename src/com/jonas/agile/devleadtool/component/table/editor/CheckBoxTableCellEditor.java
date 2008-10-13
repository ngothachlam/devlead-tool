package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;

public class CheckBoxTableCellEditor extends DefaultCellEditor {

   private int rowEdited;
   private int colEdited;

   public CheckBoxTableCellEditor(JCheckBox box) {
      super(box);
      ((JCheckBox) getComponent()).setHorizontalAlignment(JLabel.CENTER);
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
      rowEdited = row;
      colEdited = col;
      return super.getTableCellEditorComponent(table, value, isSelected, row, col);
   }

   public int getRowEdited() {
      return rowEdited;
   }
   public int getColEdited() {
      return colEdited;
   }
}