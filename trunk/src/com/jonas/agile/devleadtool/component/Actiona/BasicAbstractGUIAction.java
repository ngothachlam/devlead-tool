package com.jonas.agile.devleadtool.component.Actiona;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;

public abstract class BasicAbstractGUIAction extends AbstractAction {

   public Frame getParentFrame() {
      return parentFrame;
   }

   private Frame parentFrame;

   public BasicAbstractGUIAction(String name, String description, Frame parentFrame) {
      super(name);
      putValue(Action.SHORT_DESCRIPTION, name);
      this.parentFrame = parentFrame;
   }

   @Override
   public final void actionPerformed(ActionEvent e) {
      try {
         doActionPerformed(e);
      } catch (Throwable ex) {
         AlertDialog.alertException(parentFrame, ex);
      }
   }

   public abstract void doActionPerformed(ActionEvent e);

   public boolean isCheckBoxAction(){
      return false;
   }
   
}