package com.jonas.agile.devleadtool.component.menu;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;

public abstract class MyMenuItem extends JMenuItem implements ActionListener {

   public MyMenuItem() {
      super();
      addActionListener(this);
   }

   public MyMenuItem(Icon icon) {
      super(icon);
      addActionListener(this);
   }

   public MyMenuItem(String text) {
      super(text);
      addActionListener(this);
   }

   public MyMenuItem(Action a) {
      super(a);
      addActionListener(this);
   }

   public MyMenuItem(String text, Icon icon) {
      super(text, icon);
      addActionListener(this);
   }

   public MyMenuItem(String text, int mnemonic) {
      super(text, mnemonic);
      addActionListener(this);
   }

   @Override
   public final void actionPerformed(ActionEvent e) {
      try {
         myActionPerformed(e);
      } catch (Throwable throwable) {
         AlertDialog.alertException(getParentFrame(), throwable);
         throwable.printStackTrace();
      } finally{
         executeOnFinal();
      }
   }

   public abstract void myActionPerformed(ActionEvent e) throws Throwable;

   public abstract Frame getParentFrame();

   public abstract void executeOnFinal();

}