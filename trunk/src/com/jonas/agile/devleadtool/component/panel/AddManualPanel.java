package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.text.JTextComponent;
import com.jonas.agile.devleadtool.component.TableRadioButton;
import com.jonas.agile.devleadtool.component.dialog.NewOldValues;
import com.jonas.agile.devleadtool.component.listener.AddNewRowActionListener;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.MyPanel;
import com.jonas.jira.JiraIssue;

public class AddManualPanel extends AbstractAddPanel {

   private final JFrame frame;

   public AddManualPanel(final JFrame frame, MyTable... tables) {
      super(new BorderLayout(), frame, tables);
      this.frame = frame;
   }

   @Override
   protected Component getButtonPanel() {
      MyPanel buttonPanel = new MyPanel(new GridLayout(1, 2, 5, 5));
      buttonPanel.bordered();
      AddFromRadioButtons addFromRadioButtons = new AddFromRadioButtons(getTablesButtonGroup(), getJiraPrefixTextField(),
            getJiraCommasTextField(), getDefaultReleaseTextField(), getStatusComboBox(), frame);
      this.addButton(buttonPanel, "Add", addFromRadioButtons);
      this.addButton(buttonPanel, "Close", new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            frame.dispose();
         }
      });
      return buttonPanel;
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