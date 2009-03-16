package com.jonas.agile.devleadtool.component.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import com.jonas.agile.devleadtool.component.TableRadioButton;
import com.jonas.agile.devleadtool.component.listener.AddNewRowActionListener;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.MyPanel;
import com.jonas.jira.JiraIssue;

public class AddManualPanel extends MyPanel {
   private ButtonGroup group;
   private JTextField jiraPrefix;
   private JTextArea jiraCommas;
   private JTextField defaultRelease;
   private JComboBox statusCombo;

   public AddManualPanel(final JFrame frame, MyTable... tables) {
      super(new BorderLayout());
      MyPanel panel = new MyPanel(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();

      c.insets = new Insets(5, 5, 5, 5);
      c.fill = GridBagConstraints.BOTH;
      c.gridy = -1;

      if (tables.length > 0) {
         setNewRow(c);
         panel.add(new JLabel("Table:"), c);
         set2ndCol(c);
         panel.add(getTableRadios(tables), c);
      }

      setNewRow(c);
      panel.add(new JLabel("Prefix:"), c);
      set2ndCol(c);
      jiraPrefix = panel.addTextField(panel, 10, c);

      setNewRow(c);
      panel.add(new JLabel("Default Release:"), c);
      set2ndCol(c);
      defaultRelease = panel.addTextField(panel, 10, c);

      setNewRow(c);
      panel.add(new JLabel("Default Status:"), c);
      set2ndCol(c);
      statusCombo = panel.addComboBox(panel, BoardStatusValue.values(), c);

      setNewRow(c);
      panel.add(new JLabel("Numbers:"), c);
      set2ndCol(c);
      c.weighty = 1;
      jiraCommas = panel.addTextArea(panel, 4, 10, c);
      jiraCommas.setLineWrap(true);
      jiraCommas.setWrapStyleWord(true);

      MyPanel buttonPanel = new MyPanel(new GridLayout(1, 2, 5, 5));
      buttonPanel.bordered();
      AddFromRadioButtons addFromRadioButtons = new AddFromRadioButtons(group, jiraPrefix, jiraCommas, defaultRelease, statusCombo, frame);
      this.addButton(buttonPanel, "Add", addFromRadioButtons);
      this.addButton(buttonPanel, "Close", new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            frame.dispose();
         }
      });

      this.add(panel, BorderLayout.CENTER);
      this.add(buttonPanel, BorderLayout.PAGE_END);

      setRequestFocusEnabled(true);
   }

   public void focusPrefix() {
      jiraPrefix.requestFocus();
   }

   public void setTarget(MyTable target) {
      Enumeration<?> elements = group.getElements();
      while (elements.hasMoreElements()) {
         TableRadioButton button = (TableRadioButton) elements.nextElement();
         if (button.getTable().equals(target)) {
            button.setSelected(true);
            return;
         }
      }
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
}


class AddFromRadioButtons extends AddNewRowActionListener {

   private ButtonGroup group;
   private final JTextComponent release;
   private final JComboBox status;

   public AddFromRadioButtons(ButtonGroup group, JTextComponent jiraPrefix, JTextComponent jiraCommas, JTextComponent release,
         JComboBox statusCombo, Frame parentFrame) {
      super(null, jiraPrefix, jiraCommas, parentFrame);
      this.group = group;
      this.release = release;
      this.status = statusCombo;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      MyTable table = null;
      Enumeration<AbstractButton> elements = group.getElements();
      while (elements.hasMoreElements()) {
         TableRadioButton button = (TableRadioButton) elements.nextElement();
         if (button.isSelected()) {
            table = button.getTable();
         }
      }

      if (table == null)
         return;
      super.setTable(table);
      addJiraToTable();
   }

   @Override
   public void jiraAdded(String jira, MyTable table, String estimate, String actual, String release) {
      if (table.doesJiraExist(jira)) {
         List<NewOldValues> newOldValues = new ArrayList<NewOldValues>();

         addNewOldValueIfColumnIsInTable(table, Column.BoardStatus, jira, status.getSelectedItem(), newOldValues);
         addNewOldValueIfColumnIsInTable(table, Column.Dev_Estimate, jira, estimate, newOldValues);
         addNewOldValueIfColumnIsInTable(table, Column.Dev_Actual, jira, actual, newOldValues);
         addNewOldValueIfColumnIsInTable(table, Column.Release, jira, release, newOldValues);

         for (NewOldValues newOldValue : newOldValues) {
            if (newOldValue.isValueNew())
               table.setValueAt(newOldValue.getNewValue(), jira, newOldValue.getColumn());
         }
      }
   }

   private void addNewOldValueIfColumnIsInTable(MyTable table, Column column, String jira, Object newValue, List<NewOldValues> newOldValues) {
      NewOldValues value = getNewOldValueIfColumnIsInTable(table, column, jira, newValue);
      if (value != null) {
         newOldValues.add(value);
      }
   }

   private NewOldValues getNewOldValueIfColumnIsInTable(MyTable table, Column column, String jira, Object newValue) {
      if (table.getColumnIndex(column) < 0 || newValue == null || newValue.toString().trim().length() == 0)
         return null;

      return new NewOldValues(column, getValue(jira, table, column), newValue);
   }

   protected String getValue(String jira, MyTable table, Column column) {
      if (!table.doesJiraExist(jira))
         return null;
      Object valueAt = table.getValueAt(column, jira);
      return valueAt == null ? "" : valueAt.toString();
   }

   @Override
   public JiraIssue getJiraIssue(String jira) {
      return new JiraIssue(jira, release.getText());
   }
}