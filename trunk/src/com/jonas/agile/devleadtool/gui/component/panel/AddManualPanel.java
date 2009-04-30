package com.jonas.agile.devleadtool.gui.component.panel;

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
import com.jonas.agile.devleadtool.gui.component.TableRadioButton;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.NewOldValues;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.gui.listener.AddNewRowAction;
import com.jonas.agile.devleadtool.gui.listener.JiraToBeReconciledListener;
import com.jonas.common.swing.MyPanel;

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
      this.addButton(buttonPanel, addFromRadioButtons);
      this.addButton(buttonPanel, "Close", new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            frame.dispose();
         }
      });
      return buttonPanel;
   }

}


class AddFromRadioButtons extends AddNewRowAction implements JiraToBeReconciledListener {

   private ButtonGroup group;
   private MyTable table;

   public AddFromRadioButtons(ButtonGroup group, JTextComponent jiraPrefix, JTextComponent jiraCommas, JTextComponent release,
         JComboBox statusCombo, Frame parentFrame) {
      super("Add", "Adding manually", jiraPrefix, jiraCommas, release, statusCombo, parentFrame);
      this.group = group;
      addJiraToBeReconciledListener(this);
   }

   @Override
   public MyTable getTargetTable() {
      table = null;
      Enumeration<AbstractButton> elements = group.getElements();
      while (elements.hasMoreElements()) {
         TableRadioButton button = (TableRadioButton) elements.nextElement();
         if (button.isSelected()) {
            table = button.getTable();
         }
      }
      return table;
   }

   @Override
   public void jiraAdded(String jira, String estimate, String actual, String release, String remainder, String qaEst, BoardStatusValue status, String qaRem) {
      if (table == null) {
         AlertDialog.alertMessage(getParentFrame(), "No table Selected!!");
      }
      table.addJira(jira);

      List<NewOldValues> newOldValues = new ArrayList<NewOldValues>();

      addNewOldValueIfColumnIsInTable(table, Column.BoardStatus, jira, status, newOldValues);
      addNewOldValueIfColumnIsInTable(table, Column.Release, jira, release, newOldValues);
      addNewOldValueIfColumnIsInTable(table, Column.DEst, jira, estimate, newOldValues);
      addNewOldValueIfColumnIsInTable(table, Column.DRem, jira, remainder, newOldValues);
      addNewOldValueIfColumnIsInTable(table, Column.DAct, jira, actual, newOldValues);
      addNewOldValueIfColumnIsInTable(table, Column.QEst, jira, qaEst, newOldValues);
      addNewOldValueIfColumnIsInTable(table, Column.QRem, jira, qaRem, newOldValues);

      for (NewOldValues newOldValue : newOldValues) {
         if (newOldValue.isValueNew())
            table.setValueAt(newOldValue.getNewValue(), jira, newOldValue.getColumn());
      }
   }

   private void addNewOldValueIfColumnIsInTable(MyTable table, Column column, String jira, Object newValue, List<NewOldValues> newOldValues) {
      NewOldValues value = getNewOldValueIfColumnIsInModel(table.getMyModel(), column, jira, newValue);
      if (value != null) {
         newOldValues.add(value);
      }
   }

   private NewOldValues getNewOldValueIfColumnIsInModel(MyTableModel model, Column column, String jira, Object newValue) {
      if (model.getColumnIndex(column) < 0 || newValue == null || newValue.toString().trim().length() == 0)
         return null;

      return new NewOldValues(column, getValue(jira, model, column), newValue);
   }

   protected String getValue(String jira, MyTableModel model, Column column) {
      if (!model.isJiraPresent(jira))
         return null;
      Object valueAt = model.getValueAt(column, jira);
      return valueAt == null ? "" : valueAt.toString();
   }
}