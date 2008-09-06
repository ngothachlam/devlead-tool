package com.jonas.agile.devleadtool.component.panel;

import javax.swing.JFrame;
import com.jonas.testHelpers.TryoutTester;

public class FixVersionsPanelTestScreen {

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getFrame();
      frame.setContentPane(new FixVersionsPanel());
      frame.setVisible(true);

      JFrame testFrame = TryoutTester.getFrame();
      testFrame.setSize(200, 200);

      testFrame.setContentPane(new TestPanel());
      testFrame.setVisible(true);
   }

}
