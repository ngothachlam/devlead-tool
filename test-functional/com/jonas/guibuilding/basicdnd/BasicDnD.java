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
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;

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

      BoardTableModel testModelBoard = getTestModelBoard();
      JPanel panel1 = getBorderPanel(getScrollPaneWithTable(testModelBoard), "Board");
      JPanel panel2 = getBorderPanel(getScrollPaneWithTable(getTestModelPlan()), "Plan");
      JPanel panel3 = getBorderPanel(getScrollPaneWithTable(getTestModelJira()), "Jira");

      Component tabPane = combineIntoSplitPane(panel1, panel2, panel3);

      add(tabPane, BorderLayout.CENTER);
      add(addPanel(testModelBoard), BorderLayout.SOUTH);
      setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
   }

   private Component addPanel(BoardTableModel testModelBoard) {
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

   private Component combineIntoSplitPane(JPanel panel1, JPanel panel2, JPanel panel3) {
      JSplitPane tabPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
      JSplitPane tabPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
      tabPane.add(panel1);
      tabPane.add(tabPane2);
      tabPane2.add(panel2);
      tabPane2.add(panel3);
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

   private JScrollPane getScrollPaneWithTable(MyTableModel model) {
      MyTable table = getTable(model);
      JScrollPane scrollPane = new JScrollPane(table);
      scrollPane.setPreferredSize(new Dimension(580, 100));
      return scrollPane;
   }

   private MyTable getTable(MyTableModel model) {
      MyTable table = new MyTable(model);
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

   private BoardTableModel getTestModelBoard() {
      Vector<Vector<Object>> data = new Vector<Vector<Object>>();
      data.add(getTestRow("Board-1"));
      data.add(getTestRow("Board-2"));
      Vector<Column> columns = getTestColumns();
      return new BoardTableModel(data, columns);
   }

   private JiraTableModel getTestModelJira() {
      Vector<Vector<Object>> data = new Vector<Vector<Object>>();
      data.add(getTestRow("Jira-1"));
      data.add(getTestRow("Jira-2"));
      Vector<Column> columns = getTestColumns();
      return new JiraTableModel(data, columns);
   }

   private PlanTableModel getTestModelPlan() {
      Vector<Vector<Object>> data = new Vector<Vector<Object>>();
      data.add(getTestRow("Plan-1"));
      data.add(getTestRow("Plan-2"));
      Vector<Column> columns = getTestColumns();
      return new PlanTableModel(data, columns);
   }

   private Vector<Object> getTestRow(String string) {
      Vector<Object> row = new Vector<Object>();
      row.add(string);
      return row;
   }
}
