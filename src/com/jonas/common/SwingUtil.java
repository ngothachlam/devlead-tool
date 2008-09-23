package com.jonas.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.UIManager;
import com.jonas.agile.devleadtool.component.InternalFrame;

public class SwingUtil {

   public static final Color COLOR_RED_1 = new Color(200, 0, 0);
   public static final Color COLOR_RED_2 = new Color(225, 0, 0);
   public static final Color COLOR_RED_3 = new Color(250, 0, 0);

   private static Color selectionBackground = null;

   private static Object lock = new Object();

   public static void centreWindow(Window window) {
      Toolkit toolkit = window.getToolkit();
      Dimension screenSize = toolkit.getScreenSize();
      window.setLocation((screenSize.width - window.getWidth()) / 2, (screenSize.height - window.getHeight()) / 2);
   }

   public static void centreWindowWithinWindow(Window window, Window parentWindow) {
      Point parentLocation = parentWindow.getLocation();
      int x = parentLocation.x + (parentWindow.getWidth() - window.getWidth()) / 2;
      int y = parentLocation.y + (parentWindow.getHeight() - window.getHeight()) / 2;
      window.setLocation(x, y);
   }

   public static void locateWindowRelativeToWindow(Window window, Component parentWindow, int xOffset, int yOffset) {
      Point parentLocation = parentWindow.getLocation();
      int x = parentLocation.x + xOffset;
      int y = parentLocation.y + yOffset;
      window.setLocation(x, y);
   }

   public static MyPanel getGridPanel(int rows, int cols, int hgap, int vgap) {
      return new MyPanel(new GridLayout(rows, cols, hgap, vgap));
   }

   public static Color getTableCellFocusBackground() {
      if (selectionBackground == null) {
         synchronized (lock) {
            if (selectionBackground == null) {
               Color color = UIManager.getColor("Table.selectionBackground");
               selectionBackground = new Color(color.getRed() + 25, color.getGreen() + 25, color.getBlue() + 25);
            }
         }
      }
      return selectionBackground;
   }
}
