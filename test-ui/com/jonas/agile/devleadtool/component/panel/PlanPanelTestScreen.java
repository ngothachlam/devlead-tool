package com.jonas.agile.devleadtool.component.panel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.testHelpers.TryoutTester;

public class PlanPanelTestScreen {

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getFrame();
      JPanel panel = new PlanPanel(new PlannerHelper(frame, "test"), new PlanTableModel());
      frame.setContentPane(panel);
      frame.setVisible(true);
   }

}
