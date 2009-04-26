package com.jonas.agile.devleadtool.component.panel;

import javax.swing.JFrame;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.panel.BoardPanel;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.testHelpers.TryoutTester;

public class BoardPanelTestScreen {

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getEmptyFrame();
      PlannerHelper plannerHelper = new PlannerHelper(null, "test");
      SprintCache sprintCache = null;
      BoardPanel panel = new BoardPanel(sprintCache);
      frame.setContentPane(panel);
      frame.setVisible(true);
      
      (panel.getModel()).addJira("LLU-1");
   }
}
