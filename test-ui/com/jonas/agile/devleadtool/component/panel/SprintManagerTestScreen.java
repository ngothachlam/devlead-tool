package com.jonas.agile.devleadtool.component.panel;

import javax.swing.JFrame;
import com.jonas.agile.devleadtool.gui.component.panel.SprintManagerPanel;
import com.jonas.common.swing.MyPanel;
import com.jonas.testHelpers.TryoutTester;

public class SprintManagerTestScreen {

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getEmptyFrame();
      MyPanel panel = new SprintManagerPanel(null, null, frame);
      frame.setContentPane(panel);
      frame.pack();
      frame.setVisible(true);
   }

}
