package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.component.MyScrollPane;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.ReconciliationTable;
import com.jonas.common.swing.MyPanel;

public class ReconciliationTablePanel extends MyPanel {

   private ReconciliationTable table;
   private final MyTable boardTable;

   public ReconciliationTablePanel(MyTable boardTable, Frame parentFrame) {
      super(new GridBagLayout());
      this.boardTable = boardTable;

      GridBagConstraints c = new GridBagConstraints();

      c.insets = new Insets(5, 5, 5, 5);
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1;
      c.weighty = 1;
      c.gridx = 0;
      c.gridy = 0;

      add(getTablePanel(), c);

      c.gridy++;
      c.weighty = 0;
      c.fill = GridBagConstraints.NONE;
      c.anchor = GridBagConstraints.EAST;
      add(buttonPanel(parentFrame), c);
   }

   private JComponent getTablePanel() {
      table = new ReconciliationTable(boardTable);
      MyScrollPane myScrollPane = new MyScrollPane(table);
      return myScrollPane;
   }

   private Component buttonPanel(Frame parentFrame) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(new JButton(new AddFromReconciliationToBoardAction(parentFrame, table, boardTable)));
      return panel;
   }

   public ReconciliationTable getTable() {
      return table;
   }

}


class AddFromReconciliationToBoardAction extends BasicAbstractGUIAction {

   private final ReconciliationTable table;
   private final MyTable boardTable;

   public AddFromReconciliationToBoardAction(Frame parentFrame, ReconciliationTable table, MyTable boardTable) {
      super("Add", "Add to Board Panel", parentFrame);
      this.table = table;
      this.boardTable = boardTable;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      for (int row = 0; row < table.getRowCount(); row++) {
         String jira = (String) table.getValueAt(Column.Jira, row);
         
         if(!boardTable.isJiraPresent(jira)){
            boardTable.addJira(jira);
         }
         
         for (int col = 0; col < table.getColumnCount(); col++) {
            Column column = table.getColumnEnum(col);
            if (column != Column.Jira) {
               Object newValue = table.getValueAt(row, col);
               boardTable.setValueAt(newValue, jira, column);
            }
         }
      }

   }

}