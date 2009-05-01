package com.jonas.agile.devleadtool.component.panel;

import javax.swing.JFrame;
import com.jonas.agile.devleadtool.gui.component.panel.JiraPanel;
import com.jonas.agile.devleadtool.gui.component.panel.MyDataPanel;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.JiraTableModel;
import com.jonas.testHelpers.TryoutTester;

public class JiraPanelTestScreen {

   private static MyTable table;

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getEmptyFrame();
      MyDataPanel panel = new JiraPanel(new JiraTableModel());

      table = panel.getTable();

      frame.setContentPane(panel);
      frame.pack();
      frame.setVisible(true);
   }
}
