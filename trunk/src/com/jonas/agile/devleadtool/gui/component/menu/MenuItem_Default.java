/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MenuItem_Default extends MyMenuItem {

   private final ActionListener actionListener;

   public MenuItem_Default(String string, ActionListener actionListener, Frame parent) {
      super(parent, string);
      this.actionListener = actionListener;
   }

   @Override
   public void myActionPerformed(ActionEvent e) throws Throwable {
      actionListener.actionPerformed(e);
   }
}