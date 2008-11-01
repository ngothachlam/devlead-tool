package com.jonas.agile.devleadtool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.MyDesktopPane;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.component.dialog.LoadPlannerDialog;
import com.jonas.agile.devleadtool.component.dialog.NewPlannerDialog;
import com.jonas.agile.devleadtool.component.dialog.PlannerListener;
import com.jonas.agile.devleadtool.component.dialog.PlannerListeners;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.dialog.SavePlannerDialog;
import com.jonas.agile.devleadtool.component.listener.DaoListener;
import com.jonas.agile.devleadtool.component.listener.MainFrameListener;
import com.jonas.agile.devleadtool.data.DaoListenerEvent;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;

public class DevLeadTool {

   private Logger log = MyLogger.getLogger(DevLeadTool.class);

   private PlannerDAOExcelImpl plannerDAO;

   private PlannerHelper plannerHelper;

   private JMenu windowMenu;

   DevLeadTool() {
   }

   private JMenu createFileMenu(String title, JMenuItem[] menuItemList) {
      JMenu fileMenu = new JMenu(title);
      for (int i = 0; i < menuItemList.length; i++) {
         fileMenu.add(menuItemList[i]);
      }
      return fileMenu;
   }

   private JMenuBar createMenuBar(final JFrame frame, MyDesktopPane desktop) {
      JMenu fileMenuFile = createFileMenu("File", getFileMenuItemArray(frame, desktop));
      windowMenu = new JMenu("Windows");
      JMenuBar menuBar = new JMenuBar();
      menuBar.add(fileMenuFile);
      menuBar.add(windowMenu);
      return menuBar;
   }

   private JMenuItem createMenuItem(String menuTitle, ActionListener actionListener) {
      JMenuItem menuItem = new JMenuItem(menuTitle);
      menuItem.addActionListener(actionListener);
      return menuItem;
   }

   private JMenuItem[] getFileMenuItemArray(final JFrame frame, final MyDesktopPane desktop) {
      plannerDAO = new PlannerDAOExcelImpl();
      JMenuItem planner = createMenuItem("New Planner", new NewPlannerActionListener(desktop));
      JMenuItem open = createMenuItem("Open Planner", new LoadPlannerActionListener(desktop, frame));
      JMenuItem save = createMenuItem("Save Planner", new SavePlannerActionListener(frame, false));
      JMenuItem saveAs = createMenuItem("Save Planner As", new SavePlannerActionListener(frame, true));
      return new JMenuItem[] { planner, open, save, saveAs };
   }

   public PlannerHelper getHelper() {
      return plannerHelper;
   }

   private void makeUI() {
      JFrame frame = setLookAndFeel();
      plannerHelper = new PlannerHelper(frame, "Planner");

      MyDesktopPane desktop = new MyDesktopPane();
      JPanel contentPanel = new MyPanel(new BorderLayout());

      frame.setJMenuBar(createMenuBar(frame, desktop));
      contentPanel.add(desktop, BorderLayout.CENTER);
      contentPanel.add(MyStatusBar.getInstance(), BorderLayout.SOUTH);
      frame.setContentPane(contentPanel);

      frame.setSize(new Dimension(1200, 900));

      SwingUtil.sizeFrameRelativeToScreen(frame, 20, 55);
      SwingUtil.centreWindowWithHeightOffset(frame, 55);

      frame.setVisible(true);

      wireListeners(frame);
   }

   private JFrame setLookAndFeel() {
      JFrame frame = new JFrame("Jonas' Dev Lead Tool");
      // Use the Java look and feel.
      try {
         String laf = UIManager.getCrossPlatformLookAndFeelClassName();
         laf = UIManager.getSystemLookAndFeelClassName();
         UIManager.setLookAndFeel(laf);
      } catch (Exception e) {
      }

      // Make sure we have nice window decorations.
      JFrame.setDefaultLookAndFeelDecorated(true);
      JDialog.setDefaultLookAndFeelDecorated(true);
      return frame;
   }

   public void start() {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            makeUI();
         }
      });
   }

   private void wireListeners(final JFrame frame) {
      PlannerListeners.addListener(new PlannerListener() {
         @Override
         public void frameWasClosed(MyInternalFrame internalFrame) {
            windowMenu.remove(InternalFrameMenuItem.get(internalFrame));
         }

         @Override
         public void frameWasCreated(MyInternalFrame internalFrame) {
            InternalFrameMenuItem windowMenuItem = new InternalFrameMenuItem(internalFrame);
            windowMenu.add(windowMenuItem);
         }

         @Override
         public void frameChangedTitle(MyInternalFrame internalFrame) {
            InternalFrameMenuItem internalFrameMenuItem = InternalFrameMenuItem.get(internalFrame);
            if (internalFrameMenuItem != null)
               internalFrameMenuItem.titleChanged();
         }
      });
      frame.addWindowListener(new MainFrameListener(frame, plannerHelper));
      plannerDAO.addListener(new DaoListener() {
         ProgressDialog dialog;

         @Override
         public void notify(DaoListenerEvent event, String message) {
            switch (event) {
            case SavingStarted:
               dialog = new ProgressDialog(frame, "Saving Planner", "Saving Planner", 0);
               dialog.setIndeterminate(false);
               break;
            case SavingModelStarted:
               dialog.setNote(message);
               break;
            case SavingFinished:
               dialog.setNote(message);
               dialog.setCompleteWithDelay(300);
               break;
            case LoadingFinished:
               dialog.setNote(message);
               dialog.setCompleteWithDelay(300);
               break;
            case LoadingModelStarted:
               dialog.setNote(message);
               dialog.setCompleteWithDelay(300);
               break;
            case LoadingStarted:
               dialog.setNote(message);
               dialog.setCompleteWithDelay(300);
               break;
            default:
               break;
            }
         }
      });
   }

   private final class NewPlannerActionListener implements ActionListener {
      private final MyDesktopPane desktop;

      private NewPlannerActionListener(MyDesktopPane desktop) {
         this.desktop = desktop;
      }

      public void actionPerformed(ActionEvent e) {
         new NewPlannerDialog(desktop, plannerHelper, plannerDAO);
      }
   }

   private final class LoadPlannerActionListener implements ActionListener {
      private final MyDesktopPane desktop;
      private final JFrame frame;
      private DaoListener daoListener;
      private LoadPlannerDialog loadPlanner;

      private LoadPlannerActionListener(MyDesktopPane desktop, final JFrame frame) {
         daoListener = new DaoListener() {
            ProgressDialog dialog;

            @Override
            public void notify(DaoListenerEvent event, String message) {
               switch (event) {
               case LoadingStarted:
                  dialog = new ProgressDialog(frame, "Loading Planner", "Loading Planner", 0);
                  dialog.setIndeterminate(false);
                  break;
               case LoadingModelStarted:
                  dialog.setNote(message);
                  break;
               case LoadingFinished:
                  dialog.setNote(message);
                  dialog.setCompleteWithDelay(300);
                  break;
               default:
                  break;
               }
            }
         };
         this.desktop = desktop;
         this.frame = frame;
         loadPlanner = new LoadPlannerDialog(desktop, plannerDAO, frame, plannerHelper, daoListener);
      }

      public void actionPerformed(ActionEvent e) {
         loadPlanner.start();
      }
   }

   private final class SavePlannerActionListener implements ActionListener {
      private boolean chooseFile;
      private final JFrame frame;

      private SavePlannerActionListener(JFrame frame, boolean chooseFile) {
         this.frame = frame;
         this.chooseFile = chooseFile;
      }

      public void actionPerformed(ActionEvent e) {
         new SavePlannerDialog(plannerDAO, frame, plannerHelper.getActiveInternalFrame(), chooseFile);
      }
   }

}
