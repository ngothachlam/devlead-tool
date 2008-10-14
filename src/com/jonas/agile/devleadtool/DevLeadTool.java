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
import com.jonas.agile.devleadtool.component.MyDesktopPane;
import com.jonas.agile.devleadtool.component.dialog.LoadPlannerDialog;
import com.jonas.agile.devleadtool.component.dialog.OpenPlannerDialog;
import com.jonas.agile.devleadtool.component.dialog.SavePlannerDialog;
import com.jonas.agile.devleadtool.component.listener.MainFrameListener;
import com.jonas.agile.devleadtool.component.table.model.TableModelBuilder;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;

public class DevLeadTool {

   private PlannerHelper plannerHelper;

   DevLeadTool() {
   }

   public PlannerHelper getHelper() {
      return plannerHelper;
   }

   public void start() {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            makeUI();
         }
      });
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
      JMenuBar menuBar = new JMenuBar();
      menuBar.add(fileMenuFile);
      return menuBar;
   }

   private JMenuItem createMenuItem(String menuTitle, ActionListener actionListener) {
      JMenuItem menuItem = new JMenuItem(menuTitle);
      menuItem.addActionListener(actionListener);
      return menuItem;
   }

   private JMenuItem[] getFileMenuItemArray(final JFrame frame, final MyDesktopPane desktop) {
      final PlannerDAOExcelImpl plannerDAO = new PlannerDAOExcelImpl(new TableModelBuilder());

      JMenuItem planner = createMenuItem("New Planner", new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new OpenPlannerDialog(desktop, plannerHelper, plannerDAO);
         }
      });
      JMenuItem open = createMenuItem("Open Planner", new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new LoadPlannerDialog(desktop, plannerDAO, frame, plannerHelper);
         }
      });
      JMenuItem save = createMenuItem("Save Planner", new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new SavePlannerDialog(plannerDAO, frame, plannerHelper);
         }
      });
      return new JMenuItem[] { planner, open, save };
   }

   private void makeUI() {
      JFrame frame = setLookAndFeel();
      plannerHelper = new PlannerHelper(frame, "Planner");

      MyDesktopPane desktop = new MyDesktopPane();
      JPanel contentPanel = new MyPanel(new BorderLayout());

      frame.setJMenuBar(createMenuBar(frame, desktop));
      contentPanel.add(desktop);
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

   private void wireListeners(JFrame frame) {
      frame.addWindowListener(new MainFrameListener(frame, plannerHelper));
   }

}
