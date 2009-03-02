package com.jonas.agile.devleadtool.component.panel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import com.jonas.testHelpers.TryoutTester;

public class ReleaseNotePanelTestScreen  {

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getFrame();
      JPanel panel = new ReleaseNotePanel();
      frame.setContentPane(panel);
      frame.setVisible(true);
   }
   
}
