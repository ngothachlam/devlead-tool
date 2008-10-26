package com.jonas.agile.devleadtool;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyStatusBar extends JPanel {

   private static MyStatusBar instance;

   public static MyStatusBar getInstance() {
      if (instance == null) {
         synchronized (MyStatusBar.class) {
            if (instance == null) {
               instance = new MyStatusBar();
            }
         }
      }
      return instance;
   }

   private JLabel statusMessage;

   private MyStatusBar() {
      super();
      statusMessage = new JLabel("");
      this.add(statusMessage);
   }

   public void setMessage(String message) {
      statusMessage.setText(message);
   }
}