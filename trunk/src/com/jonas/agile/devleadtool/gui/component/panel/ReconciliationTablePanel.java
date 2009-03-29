package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import com.jonas.agile.devleadtool.gui.component.MyScrollPane;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.swing.MyPanel;

public class ReconciliationTablePanel extends MyPanel {

   private final MyTable[] tables;
   private JTable table;

   public ReconciliationTablePanel(MyTable... tables) {
      super(new BorderLayout());
      this.tables = tables;
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
