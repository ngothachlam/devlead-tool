package com.jonas.agile.devleadtool.component.panel;

import javax.swing.JFrame;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.common.MyPanel;
import com.jonas.testHelpers.TryoutTester;

public class BoardPanelTestScreen {

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getFrame();
      PlannerHelper plannerHelper = new PlannerHelper(frame, "test");
      MyPanel panel = new BoardPanel(plannerHelper);
      frame.setContentPane(panel);
      frame.setVisible(true);
   }
}
