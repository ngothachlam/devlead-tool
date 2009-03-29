package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.component.MyScrollPane;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.ReconciliationTable;
import com.jonas.common.swing.MyPanel;
import com.jonas.jira.JiraIssue;

public class ReconciliationTablePanel extends MyPanel {

   private ReconciliationTable table;
   private final MyTable boardTable;

   public ReconciliationTablePanel(MyTable boardTable, Frame parentFrame) {
      super(new BorderLayout());
      this.boardTable = boardTable;
      table = new ReconciliationTable(boardTable);

      JScrollPane scrollpane = new MyScrollPane(table);
      addCenter(scrollpane);
      addSouth(buttonPanel(parentFrame));
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
      table.getReconciledData();
      JiraIssue jiraIssue = new JiraIssue("llu-1", "release");
      boardTable.addJira(jiraIssue);
   }

}