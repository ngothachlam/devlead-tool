package com.jonas.guibuilding.newlayout;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MyComponentFactory {

   public static JMenu createMenu(String str, JMenuItem... items) {
      JMenu menu = new JMenu(str);
      for (JMenuItem menuItem : items) {
         menu.add(menuItem);
      }
      return menu;
   }
   
   public static JMenuItem createMenuItem(String str, int mnemonic){
      return new JMenuItem(str, mnemonic);
   }
}
