package com.jonas.agile.devleadtool.gui.component.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.component.TableRadioButton;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.swing.MyPanel;
import com.jonas.common.swing.SwingUtil;

public class AddJiraFromRegexFrame extends JFrame {

   public AddJiraFromRegexFrame(Window frame, MyTable... tables) {
      super();
      this.setContentPane(new AddRegexPanel(this, tables));
      this.pack();

      SwingUtil.centreWindowWithinWindow(this, frame);
      setVisible(true);
   }

   private class AddRegexPanel extends MyPanel {
      private ButtonGroup group;
      private JTextField regExTextField;
      private JTextArea textArea;
      private final JFrame frame;

      public AddRegexPanel(final JFrame frame, MyTable... tables) {
         super(new BorderLayout());
         this.frame = frame;
         MyPanel panel = getCenterPanel(tables);
         MyPanel buttonPanel = getButtonPanel(tables);
         this.add(panel, BorderLayout.CENTER);
         this.add(buttonPanel, BorderLayout.SOUTH);
      }

      private MyPanel getButtonPanel(MyTable... tables) {
         MyPanel centerPanel = new MyPanel(new GridLayout(1, 1, 5, 5));
         AddRegExButtonAction action = new AddRegExButtonAction("Add", "Caculate Jiras from Regular Expression", frame, regExTextField, textArea);
         centerPanel.add(new JButton(action));
         return centerPanel;
      }

      private MyPanel getCenterPanel(MyTable... tables) {
         MyPanel centerPanel = new MyPanel(new GridBagLayout());
         GridBagConstraints c = new GridBagConstraints();

         c.insets = new Insets(5, 5, 5, 5);
         c.fill = GridBagConstraints.BOTH;
         c.gridy = -1;
         c.weighty = 0;
         
         setNewRow(c);
         centerPanel.add(new JLabel("Table:"), c);
         set2ndCol(c);
         centerPanel.add(getTableRadios(tables), c);

         setNewRow(c);
         centerPanel.add(new JLabel("Regex:"), c);
         set2ndCol(c);
         regExTextField = new JTextField();
         centerPanel.add(regExTextField, c);

         setNewRow(c);
         centerPanel.add(new JLabel("Text:"), c);
         set2ndCol(c);
         c.weighty = 1;
         c.gridheight = 4;
         textArea = new JTextArea();
         centerPanel.add(new JScrollPane( textArea ), c);

         c.gridheight = 1;
         setNewRow(c);
         centerPanel.add(new JLabel(""), c);
        
         return centerPanel;
      }

      private Component getTableRadios(MyTable... tables) {
         JPanel panel = new JPanel();
         panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
         group = new ButtonGroup();

         for (MyTable myTable : tables) {
            TableRadioButton tableRadioButton = new TableRadioButton(myTable.getTitle(), myTable);
            group.add(tableRadioButton);
            panel.add(tableRadioButton);
         }

         return panel;
      }

      private void set2ndCol(GridBagConstraints c) {
         c.gridx = 1;
         c.weightx = 1;
      }

      private void setNewRow(GridBagConstraints c) {
         c.gridx = 0;
         c.gridy++;
         c.weightx = 0;
      }
   }
}

class AddRegExButtonAction extends BasicAbstractGUIAction{

   private JTextField regExTextField;
   private JTextArea textArea;

   public AddRegExButtonAction(String name, String description, JFrame parentFrame, JTextField regExTextField, JTextArea textArea) {
      this(name, description, parentFrame);
      this.regExTextField = regExTextField;
      this.textArea = textArea;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      throw new RuntimeException("Method not implemented yet!");
   }
   
}