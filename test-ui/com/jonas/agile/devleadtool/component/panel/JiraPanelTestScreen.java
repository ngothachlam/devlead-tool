package com.jonas.agile.devleadtool.component.panel;

import javax.swing.JFrame;
import com.jonas.agile.devleadtool.gui.component.panel.JiraPanel;
import com.jonas.agile.devleadtool.gui.component.table.model.JiraTableModel;
import com.jonas.testHelpers.TryoutTester;

public class JiraPanelTestScreen {

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getFrame();
      JiraPanel panel = new JiraPanel(new JiraTableModel());
      frame.setContentPane(panel);
      frame.setVisible(true);
   }

}
