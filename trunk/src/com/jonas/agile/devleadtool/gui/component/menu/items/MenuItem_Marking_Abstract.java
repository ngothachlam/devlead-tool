/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu.items;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;

abstract class MenuItem_Marking_Abstract extends MyMenuItem {
   protected MyTable source;

   public MenuItem_Marking_Abstract(Frame parentFrame, String string, final MyTable source) {
      super(parentFrame, string);
      init(source);
   }

   private void init(final MyTable source) {
      this.source = source;
      if (source.isMarkingAllowed()) {
         setEnabled(true);
      } else {
         setEnabled(false);
      }
   }

   @Override
   public void myActionPerformed(ActionEvent e) {
      doAction(e);
      MyStatusBar.getInstance().setMessage("Rows in " + source.getTitle() + " where marked/unmarked!", true);
   }

   protected abstract void doAction(ActionEvent e);
}