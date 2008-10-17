package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class BoardStatusCellEditor extends DefaultCellEditor {

   public BoardStatusCellEditor(JComboBox combo) {
      super(combo);
   }
}
