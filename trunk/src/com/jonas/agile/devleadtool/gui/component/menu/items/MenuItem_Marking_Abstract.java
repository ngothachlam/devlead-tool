/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu.items;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.logging.MyLogger;

abstract class MenuItem_Marking_Abstract extends MyMenuItem {
   protected MyTable source;
   static final Logger log = MyLogger.getLogger(MenuItem_Marking_Abstract.class);

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

   protected void consume(ActionEvent e) {
      if (e.getSource() instanceof KeyEvent) {
         ((KeyEvent) e.getSource()).consume();
      } else{
         log.debug("Not of keyevent type!");
      }
   }
}