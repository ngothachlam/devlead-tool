package com.jonas.agile.devleadtool.gui.component;

import javax.swing.JRadioButton;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;

public class TableRadioButton extends JRadioButton {
   private final MyTable myTable;
   public TableRadioButton(String title, MyTable myTable) {
      super(title);
      this.myTable = myTable;
   }
   public MyTable getTable() {
      return myTable;
   }
}