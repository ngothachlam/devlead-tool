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
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;

import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.component.menu.ReconciliationTablePopupMenu;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.ReconciliationTable;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.ReconciliationTableModel;
import com.jonas.agile.devleadtool.gui.listener.KeyListenerToHighlightSprintSelectionElsewhere;
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
      c.gridwidth = 2;
      c.gridx = 0;
      c.gridy = 0;

      add(getTablePanel(parentFrame), c);

      c.gridy++;
      c.weighty = 0;
      c.fill = GridBagConstraints.NONE;
      c.anchor = GridBagConstraints.WEST;
      c.gridwidth = 1;
      add(buttonPanel(parentFrame, new ClearFromReconciliationToBoardAction(parentFrame, table)), c);

      c.gridx++;
      c.anchor = GridBagConstraints.EAST;
      add(buttonPanel(parentFrame, new AddFromReconciliationToBoardAction(parentFrame, table, boardTable)), c);
   }

   private JComponent getTablePanel(Frame parentFrame) {
      table = new ReconciliationTable(boardTable);
      new ReconciliationTablePopupMenu(table, parentFrame);
      table.addKeyListener(new KeyListenerToHighlightSprintSelectionElsewhere(table, boardTable));
      JScrollPane myScrollPane = new JScrollPane(table);
      return myScrollPane;
   }

   private Component buttonPanel(Frame parentFrame, BasicAbstractGUIAction action) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(new JButton(action));
      return panel;
   }

   public ReconciliationTable getTable() {
      return table;
   }

}


class ClearFromReconciliationToBoardAction extends BasicAbstractGUIAction {

   private final ReconciliationTable table;

   public ClearFromReconciliationToBoardAction(Frame parentFrame, ReconciliationTable table) {
      super("Clear Table", "Clears this temporary table", parentFrame);
      this.table = table;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      table.getMyModel().setRowCount(0);
   }
   
}
class AddFromReconciliationToBoardAction extends BasicAbstractGUIAction {

   private final ReconciliationTableModel reconModel;
   private final MyTableModel boardModel;

   public AddFromReconciliationToBoardAction(Frame parentFrame, ReconciliationTable table, MyTable boardTable) {
      super("Add to Board", "Add to Board Panel", parentFrame);
      this.reconModel = (ReconciliationTableModel) table.getMyModel();
      this.boardModel = boardTable.getMyModel();
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      for (int row = 0; row < reconModel.getRowCount(); row++) {
         String jira = (String) reconModel.getValueAt(ColumnType.Jira, row);

         boardModel.addJira(jira);

         for (int col = 0; col < reconModel.getColumnCount(); col++) {
            ColumnType column = reconModel.getColumnType(col);
            if (column != ColumnType.Jira) {
               Object newValue = reconModel.getValueAt(row, col);
               if (reconModel.isModified(row, col)) {
                  boardModel.setValueAt(newValue, jira, column);
               }
            }
         }
      }
   }

}