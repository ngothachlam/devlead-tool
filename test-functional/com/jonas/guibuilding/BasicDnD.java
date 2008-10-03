package com.jonas.guibuilding;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class BasicDnD extends JPanel {
   private static JFrame frame;
   private JList list;

   public BasicDnD() {
      super(new BorderLayout());

      // Create a list model and a list.
      DefaultListModel listModel = new DefaultListModel();
      listModel.addElement("Martha Washington");
      listModel.addElement("Abigail Adams");
      listModel.addElement("Martha Randolph");
      listModel.addElement("Dolley Madison");
      listModel.addElement("Elizabeth Monroe");
      listModel.addElement("Louisa Adams");
      listModel.addElement("Emily Donelson");
//      listModel.addElement(new ListElement("Martha Washington"));
//      listModel.addElement(new ListElement("Abigail Adams"));
//      listModel.addElement(new ListElement("Martha Randolph"));
//      listModel.addElement(new ListElement("Dolley Madison"));
//      listModel.addElement(new ListElement("Elizabeth Monroe"));
//      listModel.addElement(new ListElement("Louisa Adams"));
//      listModel.addElement(new ListElement("Emily Donelson"));

      list = new JList(listModel);
      list.setVisibleRowCount(-1);
      list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      list.setDragEnabled(true);
      list.setDropMode(DropMode.INSERT);
      list.setTransferHandler(new TheTransferHandler(list));

      JScrollPane listView = new JScrollPane(list);
      listView.setPreferredSize(new Dimension(300, 100));
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
      p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

      JPanel rightPanel = p;
      rightPanel.add(createPanelForComponent(listView, "Projects"));
      add(rightPanel, BorderLayout.CENTER);
      setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

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
            JOptionPane.showMessageDialog(null, string);
         }
      });
   }
}
