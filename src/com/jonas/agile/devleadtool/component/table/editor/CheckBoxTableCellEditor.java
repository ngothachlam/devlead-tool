package com.jonas.agile.devleadtool.component.table.editor;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class CheckBoxTableCellEditor extends DefaultCellEditor {

   public CheckBoxTableCellEditor(JCheckBox box) {
      super(box);
      ((JCheckBox) getComponent()).setHorizontalAlignment(JLabel.CENTER);
   }
}