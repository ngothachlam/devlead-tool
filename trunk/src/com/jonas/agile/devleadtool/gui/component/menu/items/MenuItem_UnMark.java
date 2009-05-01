/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu.items;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import com.jonas.agile.devleadtool.gui.component.table.MyTable;

public class MenuItem_UnMark extends MenuItem_Marking_Abstract {
   public MenuItem_UnMark(Frame parent, String string, final MyTable source) {
      super(parent, string, source);
   }

   @Override
   protected void doAction(ActionEvent e) {
      source.unMarkSelection();
      ((KeyEvent) e.getSource()).consume();
   }
}