package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class CheckBoxTableCellEditor extends DefaultCellEditor implements MyEditor {

   private int rowEdited;
   private int colEdited;
   private Object value;
   private final JCheckBox box;
   private MyTableModel defaultTableModel;

   public CheckBoxTableCellEditor(AbstractTableModel defaultTableModel, JCheckBox box) {
      super(box);
      if (defaultTableModel instanceof MyTableModel) {
         this.defaultTableModel = (MyTableModel) defaultTableModel;
      }
      this.box = box;
      ((JCheckBox) getComponent()).setHorizontalAlignment(JLabel.CENTER);
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
      rowEdited = row;
      colEdited = col;
      if(defaultTableModel != null)
         blah;
      
      return super.getTableCellEditorComponent(table, value, isSelected, row, col);
   }

   public Object getValue() {
      stopCellEditing();
      return box.getText();
   }

   public int getRowEdited() {
      return rowEdited;
   }

   public int getColEdited() {
      return colEdited;
   }
}