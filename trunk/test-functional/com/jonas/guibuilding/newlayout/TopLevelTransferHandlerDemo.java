package com.jonas.guibuilding.newlayout;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Demonstration of the top-level {@code TransferHandler} support on {@code JFrame}.
 * 
 * @author Shannon Hickey
 */
public class TopLevelTransferHandlerDemo extends JFrame {

   JDesktopPane dp = new JDesktopPane();
   DefaultListModel listModel = new DefaultListModel();
   JList list = new JList(listModel);
   static int left;
   static int top;
   JCheckBoxMenuItem copyItem;
   JCheckBoxMenuItem checkBox_removeTHFromListAndText;
   private JCheckBoxMenuItem checkBox_useTopLevelTH;

   private TransferHandler handler = new TheTransferHandler(this);

   static void incr() {
      left += 30;
      top += 30;
      if (top == 150) {
         top = 0;
      }
   }

   public TopLevelTransferHandlerDemo() {
      super("TopLevelTransferHandlerDemo");
      setJMenuBar(createDummyMenuBar());
      JToolBar createDummyToolBar = createDummyToolBar();
      getContentPane().add(createDummyToolBar, BorderLayout.NORTH);

      JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, list, dp);
      sp.setDividerLocation(120);
      getContentPane().add(sp);

      list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      list.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
               return;
            }

            Doc val = (Doc) list.getSelectedValue();
            if (val != null) {
               val.select();
            }
         }
      });

      final TransferHandler th = list.getTransferHandler();

      checkBox_removeTHFromListAndText.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            if (checkBox_removeTHFromListAndText.isSelected()) {
               list.setTransferHandler(null);
            } else {
               list.setTransferHandler(th);
            }
         }
      });
      checkBox_useTopLevelTH.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            if (checkBox_useTopLevelTH.isSelected()) {
               setTransferHandler(handler);
            } else {
               setTransferHandler(null);
            }
         }
      });
      dp.setTransferHandler(handler);
   }

   private static void createAndShowGUI(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
      }

      TopLevelTransferHandlerDemo test = new TopLevelTransferHandlerDemo();
      test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      test.setSize(800, 600);

      test.setLocationRelativeTo(null);
      test.setVisible(true);
      test.list.requestFocus();
   }

   public static void main(final String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            // Turn off metal's use of bold fonts
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            createAndShowGUI(args);
         }
      });
   }

   private JToolBar createDummyToolBar() {
      JToolBar tb = new JToolBar();
      addButton(tb, "New");
      addButton(tb, "Open");
      addButton(tb, "Save");
      addButton(tb, "Print");
      addButton(tb, "Preview");
      tb.setFloatable(true);
      return tb;
   }

   private void addButton(JToolBar tb, String text) {
      JButton b = new JButton(text);
      b.setRequestFocusEnabled(false);
      tb.add(b);
   }

   private JMenuBar createDummyMenuBar() {
      JMenuBar mb = new JMenuBar();
      mb.add(createDummyMenu("File"));
      mb.add(createDummyMenu("Edit"));
      mb.add(createDummyMenu("Search"));
      mb.add(createDummyMenu("View"));
      mb.add(createDummyMenu("Tools"));
      mb.add(createDummyMenu("Help"));

      JMenu demo = new JMenu("Demo");
      demo.setMnemonic(KeyEvent.VK_D);
      mb.add(demo);

      checkBox_useTopLevelTH = new JCheckBoxMenuItem("Use Top-Level TransferHandler");
      checkBox_useTopLevelTH.setMnemonic(KeyEvent.VK_T);
      demo.add(checkBox_useTopLevelTH);

      checkBox_removeTHFromListAndText = new JCheckBoxMenuItem("Remove TransferHandler from List and Text");
      checkBox_removeTHFromListAndText.setMnemonic(KeyEvent.VK_R);
      demo.add(checkBox_removeTHFromListAndText);

      copyItem = new JCheckBoxMenuItem("Use COPY Action");
      copyItem.setMnemonic(KeyEvent.VK_C);
      demo.add(copyItem);

      return mb;
   }

   private JMenu createDummyMenu(String str) {
      JMenu menu = new JMenu(str);
      JMenuItem item = new JMenuItem("[Empty]");
      item.setEnabled(false);
      menu.add(item);
      return menu;
   }
}
