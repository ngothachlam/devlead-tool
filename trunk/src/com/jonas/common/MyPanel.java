package com.jonas.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class MyPanel extends JPanel {

   private Font defaultFont;

   public MyPanel(LayoutManager layoutManager) {
      super(layoutManager);
   }

   public final void addNorth(Component component) {
      super.add(component, BorderLayout.NORTH);
   }

   public final void addEast(Component component) {
      super.add(component, BorderLayout.EAST);
   }

   public final void addSouth(Component component) {
      super.add(component, BorderLayout.SOUTH);
   }

   public final void addWest(Component component) {
      super.add(component, BorderLayout.WEST);
   }

   public final void addCenter(Component component) {
      super.add(component, BorderLayout.CENTER);
   }

   public final MyPanel bordered() {
      setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      return this;
   }

   public final MyPanel bordered(int top, int left, int bottom, int right) {
      setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
      return this;
   }

   public final MyPanel borderedHighlightingThisPanels() {
      setBorder(BorderFactory.createLineBorder(Color.GREEN));
      return this;
   }

   public final MyPanel bordered(Border border) {
      setBorder(border);
      return this;
   }

   public JButton addButton(JPanel buttonPanel, String string, ActionListener listener, Object constraints) {
      JButton button = new JButton(string);
      button.addActionListener(listener);
      buttonPanel.add(button, constraints);
      return button;
   }

   public JButton addButton(JPanel buttonPanel, String string) {
      JButton button = new JButton(string);
      buttonPanel.add(button);
      return button;
   }
   
   public JButton addButton(JPanel buttonPanel, Action action) {
      JButton button = new JButton(action);
      buttonPanel.add(button);
      return button;
   }
   
   public JButton getButton(String string, ActionListener listener) {
      JButton button = new JButton(string);
      button.addActionListener(listener);
      return button;
   }
   
   public JButton addButton(JPanel buttonPanel, String string, ActionListener listener) {
      JButton button = addButton(buttonPanel, string);
      button.addActionListener(listener);
      return button;
   }

   public JLabel addLabel(JPanel buttons, String labelText) {
      JLabel label = new JLabel(labelText);
      buttons.add(label);
      return label;
   }

   public JTextField addTextField(JPanel buttons, int textFieldLength) {
      JTextField jiraCommas = new JTextField(textFieldLength);
      buttons.add(jiraCommas);
      return jiraCommas;
   }

   public JTextField addTextField(JPanel buttons, int textFieldLength, Object constraint) {
      JTextField textField = new JTextField(textFieldLength);
      if (defaultFont == null)
         setDefaultFont(textField.getFont());
      buttons.add(textField, constraint);
      return textField;
   }

   private void setDefaultFont(Font font) {
      defaultFont = font;
   }

   public JTextArea addTextArea(JPanel buttons, int rows, int cols, Object constraint) {
      JTextArea textArea = new JTextArea(rows, cols);
      if (defaultFont == null){
         setDefaultFont(new JTextField().getFont());
      }
      textArea.setFont(defaultFont);
      buttons.add(new JScrollPane(textArea), constraint);
      return textArea;
   }

   public JComboBox addComboBox(JPanel buttons, Object[] array) {
      JComboBox component = new JComboBox(array);
      buttons.add(component);
      return component;
   }
   
   public JComboBox addComboBox(JPanel buttons, Object[] boardStatusValues, Object constraint) {
      JComboBox component = new JComboBox(boardStatusValues);
      buttons.add(component, constraint);
      return component;
   }

}
