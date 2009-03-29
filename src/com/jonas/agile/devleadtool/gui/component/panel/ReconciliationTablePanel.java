package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import com.jonas.agile.devleadtool.gui.component.MyScrollPane;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.ReconciliationTable;
import com.jonas.common.swing.MyPanel;

public class ReconciliationTablePanel extends MyPanel {

   private ReconciliationTable table;

   public ReconciliationTablePanel(MyTable boardTable) {
      super(new BorderLayout());
      table = new ReconciliationTable(boardTable);

      JScrollPane scrollpane = new MyScrollPane(table);
      addCenter(scrollpane);
   }

}
