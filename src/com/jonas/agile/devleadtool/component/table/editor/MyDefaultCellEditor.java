package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.SwingUtil;

public class MyDefaultCellEditor extends DefaultCellEditor implements MyEditor {

   private Logger log = MyLogger.getLogger(MyDefaultCellEditor.class);
   private int colEdited;
   private int rowEdited;

   public MyDefaultCellEditor(JTextField field) {
      super(field);
      if(getComponent() instanceof JComponent){
         ((JComponent)getComponent()).setBorder(SwingUtil.focusCellBorder);
      }
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
      log.debug(value + " is being edited on row " + row + " and column " + column);
      return super.getTableCellEditorComponent(table, value, isSelected, row, column);
   }

}