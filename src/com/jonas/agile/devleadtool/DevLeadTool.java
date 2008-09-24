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
import javax.swing.UIManager;
import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.component.dialog.ClosePlannerDialog;
import com.jonas.agile.devleadtool.component.dialog.LoadPlannerDialog;
import com.jonas.agile.devleadtool.component.dialog.OpenPlannerDialog;
import com.jonas.agile.devleadtool.component.dialog.SavePlannerDialog;
import com.jonas.agile.devleadtool.component.listener.MainFrameListener;
import com.jonas.agile.devleadtool.component.table.model.TableModelBuilder;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.common.MyPanel;

public class DevLeadTool {

   private PlannerHelper plannerHelper;

   DevLeadTool() {
   }

   public void start() {
      // SwingUtilities.invokeLater(new Runnable() {
      // public void run() {
      makeUI();
      // }
      // });
   }

   private void makeUI() {
      JFrame frame = setLookAndFeel();
      plannerHelper = new PlannerHelper(frame, "Planner");

      DesktopPane desktop = new DesktopPane();
      JPanel contentPanel = new MyPanel(new BorderLayout());
      contentPanel.add(desktop);
      plannerHelper.setDesktop(desktop);
      frame.setJMenuBar(createMenuBar(frame, desktop));
      frame.setContentPane(contentPanel);

      frame.setSize(new Dimension(1000, 500));
      frame.setVisible(true);
      frame.addWindowListener(new MainFrameListener(frame, plannerHelper));

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

   private JMenuItem[] getFileMenuItemArray(final JFrame frame, final DesktopPane desktop) {
      final PlannerDAOExcelImpl plannerDAO = new PlannerDAOExcelImpl(new TableModelBuilder());

      JMenuItem planner = createMenuItem("New Planner", new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new OpenPlannerDialog(frame, desktop, plannerHelper);
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
//      JMenuItem exit = createMenuItem("Exit All", new ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            new ClosePlannerDialog(frame, plannerHelper);
//         }
//      });
      return new JMenuItem[] { planner, open, save };
   }

   private JMenu createFileMenu(String title, JMenuItem[] menuItemList) {
      JMenu fileMenu = new JMenu(title);
      for (int i = 0; i < menuItemList.length; i++) {
         fileMenu.add(menuItemList[i]);
      }
      return fileMenu;
   }

   private JMenuItem createMenuItem(String menuTitle, ActionListener actionListener) {
      JMenuItem menuItem = new JMenuItem(menuTitle);
      menuItem.addActionListener(actionListener);
      return menuItem;
   }

   private JMenuBar createMenuBar(final JFrame frame, DesktopPane desktop) {
      JMenu fileMenuFile = createFileMenu("File", getFileMenuItemArray(frame, desktop));
      JMenuBar menuBar = new JMenuBar();
      menuBar.add(fileMenuFile);
      return menuBar;
   }

   public PlannerHelper getHelper() {
      return plannerHelper;
   }

}
