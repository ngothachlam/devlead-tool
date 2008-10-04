package com.jonas.guibuilding.basicdnd;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;

public class BasicDnD extends JPanel {
   private static JFrame frame;

   public BasicDnD() {
      super(new BorderLayout());

      JPanel panel1 = getBorderPanel(getScrollPaneWithTable(0), "Board");
      JPanel panel2 = getBorderPanel(getScrollPaneWithTable(1), "Plan");
      JPanel panel3 = getBorderPanel(getScrollPaneWithTable(2), "Jira");

      JSplitPane tabPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
      JSplitPane tabPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
      tabPane.add(panel1);
      tabPane.add(tabPane2);
      tabPane2.add(panel2);
      tabPane2.add(panel3);
      add(tabPane, BorderLayout.CENTER);
      setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

   }

   private JScrollPane getScrollPaneWithList(String no) {
      DefaultListModel listModel1 = new DefaultListModel();
      listModel1.addElement(no + ".1");
      listModel1.addElement(no + ".2");
      listModel1.addElement(no + ".3");
      listModel1.addElement(no + ".4");
      JList list = getJList(listModel1);
      list.setTransferHandler(new ListTransferHandler(list));
      JScrollPane scrollPane1 = new JScrollPane(list);
      scrollPane1.setPreferredSize(new Dimension(300, 100));
      return scrollPane1;
   }

   private JScrollPane getScrollPaneWithTable(int no) {
      MyTable table = getJTable(no);
      table.setFillsViewportHeight(true);
      table.setTransferHandler(new TableTransferHandler(table));
      JScrollPane scrollPane1 = new JScrollPane(table);
      scrollPane1.setPreferredSize(new Dimension(300, 100));
      return scrollPane1;
   }

   private JPanel getBorderPanel(JScrollPane scrollPane, String string) {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
      panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      panel.add(createPanelForComponent(scrollPane, string));
      return panel;
   }

   private JList getJList(DefaultListModel listModel) {
      JList list = new JList(listModel);
      list.setVisibleRowCount(-1);
      list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      list.setDragEnabled(true);
      list.setDropMode(DropMode.INSERT);
      return list;
   }

   private MyTable getJTable(int no) {
      MyTable table = null;
      MyTableModel model;
      switch (no) {
      case 0:
         model = new BoardTableModel();
         model.addJira("LLU" + no);
         table = new MyTable(model);
         break;
      case 1:
         model = new JiraTableModel();
         model.addJira("LLU" + no);
         table = new MyTable(model);
         break;
      case 2:
         model = new PlanTableModel();
         model.addJira("LLU" + no);
         table = new MyTable(model);
         break;
      }
      return table;
   }

   public static void main(String[] args) {
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            frame = new JFrame("BasicDnD");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JComponent contentPane = new BasicDnD();
            contentPane.setOpaque(true); // content panes must be opaque
            frame.setContentPane(contentPane);

            frame.pack();
            frame.setVisible(true);
         }
      });
   }

   public JPanel createPanelForComponent(JComponent comp, String title) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(comp, BorderLayout.CENTER);
      if (title != null) {
         panel.setBorder(BorderFactory.createTitledBorder(title));
      }
      return panel;
   }

   static void displayDropLocation(final String string) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            // JOptionPane.showMessageDialog(null, string);
            System.out.println(string);
         }
      });
   }
}
