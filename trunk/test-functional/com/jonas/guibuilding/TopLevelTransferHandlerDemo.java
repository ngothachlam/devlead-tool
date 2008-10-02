package com.jonas.guibuilding;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Demonstration of the top-level {@code TransferHandler} support on {@code JFrame}.
 * 
 * @author Shannon Hickey
 */
public class TopLevelTransferHandlerDemo extends JFrame {

   private static boolean DEMO = false;

   private JDesktopPane dp = new JDesktopPane();
   private DefaultListModel listModel = new DefaultListModel();
   private JList list = new JList(listModel);
   private static int left;
   private static int top;
   private JCheckBoxMenuItem copyItem;
   private JCheckBoxMenuItem checkBox_removeTHFromListAndText;
   private JCheckBoxMenuItem checkBox_useTopLevelTH;

   private class Doc extends InternalFrameAdapter implements ActionListener {
      String name;
      JInternalFrame frame;
      TransferHandler th;
      JTextArea area;

      public Doc(File file) {
         this.name = file.getName();
         try {
            init(file.toURI().toURL());
         } catch (MalformedURLException e) {
            e.printStackTrace();
         }
      }

      public Doc(String name) {
         this.name = name;
         init(getClass().getResource(name));
      }

      private void init(URL url) {
         frame = new JInternalFrame(name);
         frame.addInternalFrameListener(this);
         listModel.add(listModel.size(), this);

         area = new JTextArea();
         area.setMargin(new Insets(5, 5, 5, 5));

         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String in;
            while ((in = reader.readLine()) != null) {
               area.append(in);
               area.append("\n");
            }
            reader.close();
         } catch (Exception e) {
            e.printStackTrace();
            return;
         }

         th = area.getTransferHandler();
         area.setFont(new Font("monospaced", Font.PLAIN, 12));
         area.setCaretPosition(0);
         area.setDragEnabled(true);
         area.setDropMode(DropMode.INSERT);
         frame.getContentPane().add(new JScrollPane(area));
         dp.add(frame);
         frame.show();
         if (DEMO) {
            frame.setSize(300, 200);
         } else {
            frame.setSize(400, 300);
         }
         frame.setResizable(true);
         frame.setClosable(true);
         frame.setIconifiable(true);
         frame.setMaximizable(true);
         frame.setLocation(left, top);
         incr();
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               select();
            }
         });
         checkBox_removeTHFromListAndText.addActionListener(this);
         setNullTH();
      }

      public void internalFrameClosing(InternalFrameEvent event) {
         listModel.removeElement(this);
         checkBox_removeTHFromListAndText.removeActionListener(this);
      }

      public void internalFrameOpened(InternalFrameEvent event) {
         int index = listModel.indexOf(this);
         list.getSelectionModel().setSelectionInterval(index, index);
      }

      public void internalFrameActivated(InternalFrameEvent event) {
         int index = listModel.indexOf(this);
         list.getSelectionModel().setSelectionInterval(index, index);
      }

      public String toString() {
         return name;
      }

      public void select() {
         try {
            frame.toFront();
            frame.setSelected(true);
         } catch (java.beans.PropertyVetoException e) {
         }
      }

      public void actionPerformed(ActionEvent ae) {
         setNullTH();
      }

      public void setNullTH() {
         if (checkBox_removeTHFromListAndText.isSelected()) {
            area.setTransferHandler(null);
         } else {
            area.setTransferHandler(th);
         }
      }
   }

   private TransferHandler handler = new TransferHandler() {
      public boolean canImport(TransferHandler.TransferSupport support) {
         if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            return false;
         }

         if (copyItem.isSelected()) {
            boolean copySupported = (COPY & support.getSourceDropActions()) == COPY;

            if (!copySupported) {
               return false;
            }

            support.setDropAction(COPY);
         }

         return true;
      }

      public boolean importData(TransferHandler.TransferSupport support) {
         if (!canImport(support)) {
            return false;
         }

         Transferable t = support.getTransferable();

         try {
            java.util.List<File> l = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

            for (File f : l) {
               new Doc(f);
            }
         } catch (UnsupportedFlavorException e) {
            return false;
         } catch (IOException e) {
            return false;
         }

         return true;
      }
   };

   private static void incr() {
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
      if (DEMO) {
         test.setSize(493, 307);
      } else {
         test.setSize(800, 600);
      }
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
