package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;

public class JiraCellEditor extends DefaultCellEditor implements MyEditor {

   private Logger log = MyLogger.getLogger(JiraCellEditor.class);
   private int col;
   private int row;
   private Object value;
   
   public JiraCellEditor(JTextField textField) {
      super(textField);
      if(getComponent() instanceof JComponent){
         ((JComponent)getComponent()).setBorder(SwingUtil.focusCellBorder);
      }
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      this.row = row;
      this.col = column;
      this.value = value;
      log.debug("Editing value " + value);
      //FIXME prevent same jira numbers to be edited in or alert!
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
   public Object getOldValue() {
      return value;
   }


}
