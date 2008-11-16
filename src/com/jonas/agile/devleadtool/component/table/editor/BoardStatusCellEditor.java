package com.jonas.agile.devleadtool.component.table.editor;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import com.jonas.agile.devleadtool.component.table.renderer.MyTableCellRenderer;

public class BoardStatusCellEditor extends DefaultCellEditor {

   public BoardStatusCellEditor(JComboBox combo) {
      super(combo);
      if(getComponent() instanceof JComponent){
         ((JComponent)getComponent()).setBorder(MyTableCellRenderer.focusBorder);
      }
   }
}
