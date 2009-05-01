package com.jonas.agile.devleadtool.gui.component.menu.items;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import com.jonas.agile.devleadtool.gui.component.table.MyTable;

public class MenuItem_OnlyShowSpecificColumns extends MyMenuItem {

   private final MyTable table;
   private final String[] colsToShow;
   private boolean reset;

   public MenuItem_OnlyShowSpecificColumns(Frame parentFrame, String text, MyTable table, String... colsToShow) {
      super(parentFrame, text);
      this.table = table;
      this.colsToShow = colsToShow;
   }

   public MenuItem_OnlyShowSpecificColumns(JFrame parentFrame, String text, MyTable source, boolean reset) {
      this(parentFrame, text, source);
      this.reset = reset;
   }

   @Override
   public void myActionPerformed(ActionEvent e) throws Throwable {
      if (reset)
         table.resetColumns();
      else
         table.showAndRearrangeColumns(colsToShow);
   }

}
