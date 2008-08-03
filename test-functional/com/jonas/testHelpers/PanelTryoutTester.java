package com.jonas.testHelpers;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.jonas.agile.devleadtool.data.SystemProperties;

public class PanelTryoutTester {

   public static void viewPanel(JPanel panel) {
      JFrame frame = new JFrame();
      frame.setContentPane(panel);
      frame.setSize(800, 400);
      frame.setVisible(true);
      frame.addWindowListener(new WindowAdapter() {

         public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            SystemProperties.close();
            System.exit(0);
         }

      });

   }

}
