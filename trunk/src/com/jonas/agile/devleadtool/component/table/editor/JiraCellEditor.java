package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class JiraCellEditor extends DefaultCellEditor implements MyEditor {

   private Logger log = MyLogger.getLogger(JiraCellEditor.class);
   private int col;
   private int row;
   
   public JiraCellEditor(JTextField textField) {
      super(textField);
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      this.row = row;
      this.col = column;
      return super.getTableCellEditorComponent(table, value, isSelected, row, column);
   }

   @Override
   public int getColEdited() {
      return col;
   }

   @Override
   public int getRowEdited() {
      return row;
   }

   @Override
   public Object getValue() {
      return getCellEditorValue();
   }


}
