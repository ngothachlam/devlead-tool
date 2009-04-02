/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu;

import java.awt.Frame;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;

class MenuItem_UnMark extends MenuItem_Marking_Abstract {
   public MenuItem_UnMark(Frame parent, String string, final MyTable source) {
      super(parent, string, source);
   }

   @Override
   protected void doAction() {
      source.unMarkSelection();
   }
}