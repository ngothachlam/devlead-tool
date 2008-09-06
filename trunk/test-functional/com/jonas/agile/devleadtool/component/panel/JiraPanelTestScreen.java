package com.jonas.agile.devleadtool.component.panel;

import javax.swing.JFrame;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.testHelpers.TryoutTester;

public class JiraPanelTestScreen {

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getFrame();
      PlannerHelper plannerHelper = new PlannerHelper(frame, "test");
      JiraPanel panel = new JiraPanel(plannerHelper);
      frame.setContentPane(panel);
      frame.setVisible(true);
   }

}
