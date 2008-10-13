package com.jonas.agile.devleadtool.component.table;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class MyTableBoardStatusTest extends JonasTestCase {

   public void testShouldSetBoardStatusOk(){
      BoardTableModel boardTableModel = new BoardTableModel();
      JiraTableModel jiraTableModel = new JiraTableModel();
      jiraTableModel.setBoardModel(boardTableModel);
      
      MyTable boardTable = new MyTable("Board", boardTableModel);
      MyTable jiraTable = new MyTable("Jira", jiraTableModel);
      
      assertEquals(0,boardTable.getRowCount());
      assertEquals(0,jiraTable.getRowCount());
      
      // add empty jiras to both board and jira tables.
      boardTable.addJira("llu-1");
      jiraTable.addJira("llu-1");
      
      // Board status on Jira Table should be 'unknown'
      assertEquals(BoardStatusValue.UnKnown, jiraTable.getValueAt(Column.B_BoardStatus, 0));
      
      // Set the Board status on Board Table to 'Open'
      boardTable.setValueAt(true, 0, Column.isOpen);
      
      // Board status on Jira Table should be 'Open'
      assertEquals(BoardStatusValue.Open, jiraTable.getValueAt(Column.B_BoardStatus, 0));
      
      // Set the Board status on Board Table to 'Open'
      boardTable.setValueAt(false, 0, Column.isOpen);
      boardTable.setValueAt(true, 0, Column.isComplete);
      
      // Board status on Jira Table should be 'Open'
      assertEquals(BoardStatusValue.Complete, jiraTable.getValueAt(Column.B_BoardStatus, 0));
   }
   
}
