package com.jonas.agile.devleadtool.gui.component.menu.items;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;

public abstract class MyMenuItem extends JMenuItem implements ActionListener {

   private Frame parentFrame;

   public MyMenuItem(Frame parentFrame, String text) {
      super(text);
      addActionListener(this);
   }

   @Override
   public final void actionPerformed(ActionEvent e) {
      try {
         myActionPerformed(e);
      } catch (Throwable throwable) {
         AlertDialog.alertException(parentFrame, throwable);
         throwable.printStackTrace();
      } finally{
         executeOnFinal();
      }
   }

   public abstract void myActionPerformed(ActionEvent e) throws Throwable;

   /**
    * Override as needed!
    */
   public void executeOnFinal(){
   }

   public Frame getParentFrame() {
      return parentFrame;
   }

}