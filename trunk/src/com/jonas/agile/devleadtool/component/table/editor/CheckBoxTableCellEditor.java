package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;

public class CheckBoxTableCellEditor extends DefaultCellEditor {

   private int rowEdited;

   public CheckBoxTableCellEditor(JCheckBox box) {
      super(box);
      ((JCheckBox) getComponent()).setHorizontalAlignment(JLabel.CENTER);
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      rowEdited = row;
      return super.getTableCellEditorComponent(table, value, isSelected, row, column);
   }

   public int getRowEdited() {
      return rowEdited;
   }
}