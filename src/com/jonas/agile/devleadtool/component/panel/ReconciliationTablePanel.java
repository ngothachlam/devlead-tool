package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import javax.swing.JTable;
import com.jonas.common.MyPanel;

public class ReconciliationTablePanel extends MyPanel {

   private JTable table;

   public ReconciliationTablePanel() {
      super(new BorderLayout());
      table = new JTable();
   }

}
