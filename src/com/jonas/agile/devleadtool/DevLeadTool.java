package com.jonas.agile.devleadtool;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import com.google.inject.Inject;
import com.jonas.agile.devleadtool.data.DaoListenerEvent;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.agile.devleadtool.gui.component.DesktopPane;
import com.jonas.agile.devleadtool.gui.component.MyInternalFrame;
import com.jonas.agile.devleadtool.gui.component.SaveKeyListener;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.LoadPlannerDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.NewPlannerDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.SavePlannerDialog;
import com.jonas.agile.devleadtool.gui.component.frame.main.MainFrame;
import com.jonas.agile.devleadtool.gui.listener.DaoListener;
import com.jonas.agile.devleadtool.gui.listener.MainFrameListener;
import com.jonas.agile.devleadtool.gui.listener.PlannerListener;
import com.jonas.agile.devleadtool.gui.listener.PlannerListeners;
import com.jonas.agile.devleadtool.properties.Property;
import com.jonas.agile.devleadtool.properties.SprinterPropertieSetter;
import com.jonas.agile.devleadtool.properties.SprinterProperties;
import com.jonas.agile.devleadtool.properties.SprinterPropertiesManager;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.MyPanel;
import com.jonas.common.swing.SwingUtil;

public class DevLeadTool {

   private Logger log = MyLogger.getLogger(DevLeadTool.class);
   private final PlannerDAOExcelImpl plannerDAO;
   private final PlannerHelper helper;
   private JMenu windowMenu;
   private final JFrame frame;
   private final DesktopPane desktop;

   @Inject
   public DevLeadTool(MainFrame frame, DesktopPane desktop, PlannerDAOExcelImpl plannerDAO, PlannerHelper helper) {
      this.helper = helper;
      this.frame = frame;
      this.desktop = desktop;
      this.plannerDAO = plannerDAO;
   }

   private JMenu createFileMenu(String title, JMenuItem[] menuItemList) {
      JMenu fileMenu = new JMenu(title);
      for (int i = 0; i < menuItemList.length; i++) {
         fileMenu.add(menuItemList[i]);
      }
      return fileMenu;
   }

   private JMenuBar createMenuBar() {
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

   private JMenuItem[] getFileMenuItemArray(final JFrame frame, final DesktopPane desktop) {
      SavePlannerDialog savePlannerDialog = new SavePlannerDialog(plannerDAO, frame);
      SaveKeyListener saveKeyListener = new SaveKeyListener(helper, savePlannerDialog);
      NewPlannerDialog newPlannerDialog = new NewPlannerDialog(desktop, helper, plannerDAO, savePlannerDialog, saveKeyListener);
      JMenuItem planner = createMenuItem("New Planner", new NewPlannerActionListener(newPlannerDialog));
      LoadPlannerDialog loadPlannerDialog = new LoadPlannerDialog(desktop, plannerDAO, frame, helper, savePlannerDialog, saveKeyListener);
      JMenuItem open = createMenuItem("Open Planner", new LoadPlannerActionListener(loadPlannerDialog));
      JMenuItem save = createMenuItem("Save Planner", new SavePlannerActionListener(false, savePlannerDialog));
      JMenuItem saveAs = createMenuItem("Save Planner As", new SavePlannerActionListener(true, savePlannerDialog));
      return new JMenuItem[] { planner, open, save, saveAs };
   }

   public PlannerHelper getHelper() {
      return helper;
   }

   private File file = new File("C:\\Sprinter.properties");
   private SprinterProperties properties = new SprinterProperties(file);
   private SprinterPropertieSetter propSetter = new SprinterPropertieSetter();
   private SprinterPropertiesManager propManager = new SprinterPropertiesManager(properties, propSetter);

   private void makeUI() {
      JPanel contentPanel = new MyPanel(new BorderLayout());

      frame.setJMenuBar(createMenuBar());
      contentPanel.add(desktop, BorderLayout.CENTER);
      contentPanel.add(MyStatusBar.getInstance(), BorderLayout.SOUTH);
      frame.setContentPane(contentPanel);

      SwingUtil.sizeFrameRelativeToScreen(frame, 20, 55);
      SwingUtil.centreWindowWithHeightOffset(frame, 55);
      wireListeners(frame);

      frame.setVisible(true);

   }

   public void start() {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            try {
               makeUI();
               loadOrInitiateProperties();
               File file = (File) properties.getPropertyObject(Property.SAVE_DIRECTORY);
               if (file != null)
                  System.out.println(file.getAbsolutePath() + " exists? " + file.exists());
            } catch (Throwable e) {
               AlertDialog.alertException(helper.getParentFrame(), e);
            }
         }

      });
   }

   private void loadOrInitiateProperties() throws IOException {
      propSetter.setFrameForDefaultPropertiesQuery(frame);
      propManager.loadProperties();
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
      frame.addWindowListener(new MainFrameListener(frame, helper));
      plannerDAO.addListener(new DaoListener() {
         ProgressDialog dialog;

         // FIXME - not very well done!
         @Override
         public void notify(DaoListenerEvent event, String message) {
            switch (event) {
            case SavingStarted:
               dialog = new ProgressDialog(frame, "Saving Planner", "Saving Planner", 0, true);
               dialog.setIndeterminate(false);
               break;
            case SavingModelStarted:
               dialog.setNote(message);
               break;
            case SavingFinished:
               dialog.setNote(message);
               dialog.setCompleteWithDelay(300);
               break;
            case LoadingStarted:
               dialog = new ProgressDialog(frame, "Loading Planner", "Loading Planner", 0, true);
               dialog.setIndeterminate(false);
               break;
            case LoadingModelStarted:
               dialog.setNote(message);
               break;
            case LoadingFinished:
               dialog.setNote(message);
               dialog.setCompleteWithDelay(300);
               break;
            case LoadingErrored:
               dialog.setNote(message);
               dialog.setIndeterminate(false);
               dialog.setCompleteWithDelay(1500);
               break;
            default:
               break;
            }
         }
      });
   }

   private final class NewPlannerActionListener implements ActionListener {
      private NewPlannerDialog newPlannerDialog;

      private NewPlannerActionListener(NewPlannerDialog newPlannerDialog) {
         this.newPlannerDialog = newPlannerDialog;
      }

      public void actionPerformed(ActionEvent e) {
         newPlannerDialog.openNew();
      }
   }

   private final class LoadPlannerActionListener implements ActionListener {
      private LoadPlannerDialog loadPlannerDialog;

      private LoadPlannerActionListener(LoadPlannerDialog loadPlannerDialog) {
         this.loadPlannerDialog = loadPlannerDialog;
      }

      public void actionPerformed(ActionEvent e) {
         loadPlannerDialog.load();
      }
   }

   private final class SavePlannerActionListener implements ActionListener {
      private boolean chooseFile;
      private SavePlannerDialog savePlannerDialog;

      private SavePlannerActionListener(boolean chooseFile, SavePlannerDialog savePlannerDialog) {
         this.chooseFile = chooseFile;
         this.savePlannerDialog = savePlannerDialog;
      }

      public void actionPerformed(ActionEvent e) {
         savePlannerDialog.save(helper.getActiveInternalFrame(), chooseFile);
      }
   }

}
