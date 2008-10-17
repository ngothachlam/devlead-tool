package com.jonas.agile.devleadtool.component.panel;

import javax.swing.JFrame;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.testHelpers.TryoutTester;

public class BoardPanelTestScreen {

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getFrame();
      PlannerHelper plannerHelper = new PlannerHelper(frame, "test");
      BoardPanel panel = new BoardPanel();
      frame.setContentPane(panel);
      frame.setVisible(true);
      
      ((MyTableModel)panel.getModel()).addJira("LLU-1");
   }
}