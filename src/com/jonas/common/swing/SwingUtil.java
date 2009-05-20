package com.jonas.common.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;

import com.jonas.common.ColorUtil;
import com.jonas.common.logging.MyLogger;

public class SwingUtil {

   public static final Color cellRed = new Color(210, 10, 10);
   public static final Color cellBlue = new Color(0, 0, 180);
   public static final Color cellGreen = new Color(30, 200, 30);
   public static final Color cellLightRed = new Color(255, 95, 95);
   public static final Color cellLightBlue = new Color(51, 195, 255);
   public static final Color cellLightGreen = new Color(150, 250, 150);
   public static final Color cellLightYellow = new Color(255, 255, 150);
   public static final Color cellWhite = new Color(255, 255, 255);
   public static final Color cellLightGrey = new Color(225, 225, 225);
   public static final Color cellBlack = new Color(0, 0, 0);

   public static final Border focusCellBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.yellow);
   public static final Border defaultCellBorder = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");

   private static Object lock = new Object();

   private static final Logger log = MyLogger.getLogger(SwingUtil.class);

   private static Color selectionBackground = null;
   private static final Stack<Short> freeHssfColors = new Stack<Short>();
   
   static{
      resetFreeHSSFColors();
   }

   public static void resetFreeHSSFColors() {
      freeHssfColors.clear();
      freeHssfColors.push(HSSFColor.LAVENDER.index);
      freeHssfColors.push(HSSFColor.BROWN.index);
      freeHssfColors.push(HSSFColor.DARK_TEAL.index);
      freeHssfColors.push(HSSFColor.CORNFLOWER_BLUE.index);
      freeHssfColors.push(HSSFColor.AQUA.index);
      freeHssfColors.push(HSSFColor.GOLD.index);
   }

   private static int atLeastZero(int no) {
      return no < 0 ? 0 : no;
   }

   public static void centreWindow(Window window) {
      Toolkit toolkit = window.getToolkit();
      Dimension screenSize = toolkit.getScreenSize();
      window.setLocation((screenSize.width - window.getWidth()) / 2, (screenSize.height - window.getHeight()) / 2);
   }

   public static void centreWindowWithHeightOffset(Window window, int startMenuSize) {
      Toolkit toolkit = window.getToolkit();
      Dimension screenSize = toolkit.getScreenSize();
      window.setLocation(getRelativeXLocationConsideringStartMenu(screenSize.width, window.getWidth()),
            getRelativeYLocationConsideringStartMenu(screenSize.height, window.getHeight(), startMenuSize));
   }

   static int getRelativeXLocationConsideringStartMenu(int screenWidth, int windowWidth) {
      return (screenWidth - windowWidth) / 2;
   }

   static int getRelativeYLocationConsideringStartMenu(int screenHeight, int windowHeight, int startMenuSize) {
      return ((screenHeight - (windowHeight + startMenuSize)) / 2);
   }

   public static void centreWindowWithinWindow(Component window, Component parentWindow) {
      Point parentLocation = parentWindow.getLocation();
      int x = parentLocation.x + (parentWindow.getWidth() - window.getWidth()) / 2;
      int y = parentLocation.y + (parentWindow.getHeight() - window.getHeight()) / 2;
      window.setLocation(atLeastZero(x), atLeastZero(y));
   }

   public static MyPanel getGridPanel(int rows, int cols, int hgap, int vgap) {
      return new MyPanel(new GridLayout(rows, cols, hgap, vgap));
   }

   public static Color getTableCellFocusBackground() {
      if (selectionBackground == null) {
         synchronized (lock) {
            if (selectionBackground == null) {
               Color color = UIManager.getColor("Table.selectionBackground");
               selectionBackground = ColorUtil.darkenColor(color, 45);
            }
         }
      }
      return selectionBackground;
   }

   public static void locateWindowRelativeToWindow(Component window, Component parentWindow, int xOffset, int yOffset) {
      Point parentLocation = parentWindow.getLocation();
      int parentx = parentLocation.x;
      int parenty = parentLocation.y;
      int destinationx = parentx + xOffset;
      int destinationy = parenty + yOffset;

      log.debug("parent location : " + parentx + ", " + parenty);
      log.debug("setting location : " + destinationx + ", " + destinationy);

      Toolkit defaultToolkit = java.awt.Toolkit.getDefaultToolkit();
      Dimension screenSize = defaultToolkit.getScreenSize();

      log.debug("Screen Size: " + screenSize.width + ", " + screenSize.height);
      log.debug("component width: " + window.getWidth() + ", " + window.getHeight());

      window.setLocation(destinationx, destinationy);
   }

   public static void sizeFrameRelativeToScreen(Window window, int offset, int startMenuHeight) {
      Toolkit toolkit = window.getToolkit();
      Dimension screenSize = toolkit.getScreenSize();
      window.setSize(screenSize.width - offset * 2, screenSize.height - offset * 2 - startMenuHeight);
   }

   public static void executeInSwingEventThread(Runnable swingRunnable) {
      if (SwingUtilities.isEventDispatchThread()) {
         swingRunnable.run();
      } else {
         try {
            SwingUtilities.invokeAndWait(swingRunnable);
         } catch (InterruptedException e) {
            e.printStackTrace();
         } catch (InvocationTargetException e) {
            e.printStackTrace();
         }
      }
   }

   public static void defaultGridBagConstraints(GridBagConstraints gbc) {
      gbc.insets = new Insets(2, 5, 2, 5);
      gbc.gridheight = 1;
      gbc.gridwidth = 1;
      gbc.gridy = 0;
      gbc.weightx = 0.5;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.WEST;
   }

   public static Short getFreeHSSFColor() {
      log.debug("popping!!");
      return freeHssfColors.pop();
   }

}
