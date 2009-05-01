package com.jonas.guibuilding.basicdnd;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;

public class BasicDnD extends JPanel {
   private static JFrame frame;

   static void debugDropLocation(final String string) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            // JOptionPane.showMessageDialog(null, string);
            System.out.println(string);
         }
      });
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

   public BasicDnD() {
      super(new BorderLayout());
      
      MyTable boardTable = getTable(getTestModelBoard());
      JScrollPane scrollPane1 = new JScrollPane(boardTable);
      scrollPane1.setPreferredSize(new Dimension(580, 100));
      JPanel panel1 = getBorderPanel(scrollPane1, "Board");
      
      MyTable jiraTable = getTable(getTestModelJira());
      JScrollPane scrollPane3 = new JScrollPane(jiraTable);
      scrollPane3.setPreferredSize(new Dimension(580, 100));
      JPanel panel3 = getBorderPanel(scrollPane3, "Jira");
      
      Component tabPane = combineIntoSplitPane(panel1, panel3);

      add(tabPane, BorderLayout.CENTER);
      add(addPanel(getTestModelBoard()), BorderLayout.SOUTH);
      setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
   }

   private Component addPanel(MyTableModel testModelBoard) {
      JPanel panel = new JPanel(new FlowLayout());
      JButton button = new JButton("Add");
      panel.add(button);

      button.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Object[] possibilities = { "Board", "Planner", "Jira" };
            Object s = JOptionPane.showInputDialog(frame, "Copy the Jira to...", "Copy to...", JOptionPane.PLAIN_MESSAGE, null, possibilities, "Board");
            System.out.println("result " + s);
         }
      });
      return panel;
   }

   private Component combineIntoSplitPane(JPanel panel1, JPanel panel2) {
      JSplitPane tabPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
      tabPane.add(panel1);
      tabPane.add(panel2);
      return tabPane;
   }

   public JPanel createPanelForComponent(JComponent comp, String title) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(comp, BorderLayout.CENTER);
      if (title != null) {
         panel.setBorder(BorderFactory.createTitledBorder(title));
      }
      return panel;
   }

   private JPanel getBorderPanel(JScrollPane scrollPane, String string) {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
      panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      panel.add(createPanelForComponent(scrollPane, string));
      return panel;
   }

   private MyTable getTable(MyTableModel model) {
      MyTable table = new MyTable("BasicDND", model, false);
      table.setFillsViewportHeight(true);
      table.setTransferHandler(new TableTransferHandler(table));
      table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      return table;
   }

   private Vector<Column> getTestColumns() {
      Vector<Column> columns = new Vector<Column>();
      columns.add(Column.Jira);
      return columns;
   }

   private MyTableModel getTestModelBoard() {
      Vector<Vector<Object>> data = new Vector<Vector<Object>>();
      data.add(getTestRow("Board-1"));
      data.add(getTestRow("Board-2"));
      Vector<Column> columns = getTestColumns();
      return new BoardTableModel(data, columns, null);
   }

   private MyTableModel getTestModelJira() {
      Vector<Vector<Object>> data = new Vector<Vector<Object>>();
      data.add(getTestRow("Jira-1"));
      data.add(getTestRow("Jira-2"));
      Vector<Column> columns = getTestColumns();
      return new JiraTableModel(data, columns);
   }

   private Vector<Object> getTestRow(String string) {
      Vector<Object> row = new Vector<Object>();
      row.add(string);
      return row;
   }
}
