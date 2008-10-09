package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;

public class CheckBoxTableCellEditor extends DefaultCellEditor {

   private int rowEdited;

   public CheckBoxTableCellEditor(JCheckBox box, CellEditorListener checkBoxEditorListener) {
      super(box);
      ((JCheckBox) getComponent()).setHorizontalAlignment(JLabel.CENTER);
      this.addCellEditorListener(checkBoxEditorListener);
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