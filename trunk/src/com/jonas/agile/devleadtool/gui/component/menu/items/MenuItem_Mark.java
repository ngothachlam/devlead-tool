/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu.items;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;

public class MenuItem_Mark extends MenuItem_Marking_Abstract {

   public MenuItem_Mark(Frame parentFrame, String string, final MyTable source) {
      super(parentFrame, string, source);
      setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK));

   }

   @Override
   protected void doAction(ActionEvent e) {
      source.markSelected();
      ((KeyEvent) e.getSource()).consume();
   }

}