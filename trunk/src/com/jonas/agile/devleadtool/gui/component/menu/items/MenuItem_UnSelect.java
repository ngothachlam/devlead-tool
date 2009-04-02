/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu.items;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;

public class MenuItem_UnSelect extends MyMenuItem {
   private final MyTable source;

   public MenuItem_UnSelect(Frame parent, String string, final MyTable source) {
      super(parent, string);
      this.source = source;
   }

   @Override
   public void myActionPerformed(ActionEvent e) throws Throwable {
      source.clearSelection();
   }
}