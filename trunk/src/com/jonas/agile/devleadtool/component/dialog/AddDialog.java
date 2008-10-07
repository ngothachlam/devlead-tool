package com.jonas.agile.devleadtool.component.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import com.jonas.agile.devleadtool.component.TableRadioButton;
import com.jonas.agile.devleadtool.component.dnd.TableAndTitleDTO;
import com.jonas.agile.devleadtool.component.listener.AddNewRowActionListener;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;

public class AddDialog extends JFrame {

   public AddDialog(Window frame, TableAndTitleDTO... tables) {
      super();
      this.setContentPane(new AddPanel(this, tables));
      this.pack();

      SwingUtil.centreWindowWithinWindow(this, frame);
      setVisible(true);
   }
}


class AddPanel extends MyPanel {
   private ButtonGroup group;

   public AddPanel(final JFrame frame, TableAndTitleDTO... tables) {
      super(new BorderLayout());
      MyPanel panel = new MyPanel(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();

      c.insets = new Insets(5, 5, 5, 5);
      c.fill = c.BOTH;
      c.gridy = -1;
      setNewRow(c);
      panel.add(new JLabel("Table:"), c);
      set2ndCol(c);
      panel.add(getTableRadios(tables), c);
      setNewRow(c);
      panel.add(new JLabel("Prefix:"), c);
      set2ndCol(c);
      JTextField jiraPrefix = panel.addTextField(panel, 10, c);

      setNewRow(c);
      panel.add(new JLabel("Numbers:"), c);
      set2ndCol(c);
      c.weighty = 1;
      JTextArea jiraCommas = panel.addTextArea(panel, 4, 10, c);

      jiraCommas.setLineWrap(true);
      jiraCommas.setWrapStyleWord(true);

      MyPanel buttonPanel = new MyPanel(new GridLayout(1, 2, 5, 5));
      buttonPanel.bordered();
      this.addButton(buttonPanel, "Add", new AddFromRadioButtons(frame, group, jiraPrefix, jiraCommas));
      this.addButton(buttonPanel, "Close", new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            frame.dispose();
         }
      });

      this.add(panel, BorderLayout.CENTER);
      this.add(buttonPanel, BorderLayout.PAGE_END);
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

   private Component getTableRadios(TableAndTitleDTO... tables) {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      group = new ButtonGroup();

      for (TableAndTitleDTO myTable : tables) {
         TableRadioButton tableRadioButton = new TableRadioButton(myTable.getTitle(), myTable.getTable());
         group.add(tableRadioButton);
         panel.add(tableRadioButton);
      }

      return panel;
   }
}


class AddFromRadioButtons extends AddNewRowActionListener {

   private ButtonGroup group;

   public AddFromRadioButtons(Window addPanel, ButtonGroup group, JTextComponent jiraPrefix, JTextComponent jiraCommas) {
      super(null, jiraPrefix, jiraCommas);
      this.group = group;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      MyTable table = null;
      Enumeration elements = group.getElements();
      while (elements.hasMoreElements()) {
         TableRadioButton button = (TableRadioButton) elements.nextElement();
         if (button.isSelected()) {
            table = button.getTable();
         }
      }
      
      if (table == null)
         return;
      super.setTable(table);
      super.actionPerformed(e);
   }

}
