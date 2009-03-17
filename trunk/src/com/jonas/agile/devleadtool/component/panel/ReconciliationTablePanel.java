package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import com.jonas.agile.devleadtool.component.MyScrollPane;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.MyPanel;

public class ReconciliationTablePanel extends MyPanel {

   private JTable table;
   private final MyTable jiraTable;

   public ReconciliationTablePanel(MyTable jiraTable) {
      super(new BorderLayout());
      this.jiraTable = jiraTable;
      table = new JTable(getRowData(), getColumnData());

      JScrollPane scrollpane = new MyScrollPane(table);
      addCenter(scrollpane);
   }

   private Vector getColumnData() {
      Vector vector = new Vector();
      vector.add("Jira");
      return vector;
   }

   private Vector getRowData() {
      return new Vector();
   }

}
