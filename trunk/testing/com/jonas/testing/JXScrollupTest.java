package com.jonas.testing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXTaskPane;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;

public class JXScrollupTest extends JPanel {
   private static final int NUM_SCROLL_UPS = 5;

   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(PlasticXPLookAndFeel.class.getName());
      } catch (Exception e) {
      }

      // Create and set up the window.
      JFrame frame = new JFrame("JXScrollUpTest");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Create and set up the layered pane.
      JPanel rootPanel = new JPanel();
      rootPanel.setLayout(new BorderLayout());
      rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      createScrollups(rootPanel);
      frame.setContentPane(rootPanel);

      // Display the window.
      frame.pack();
      frame.setVisible(true);

   }

   private static void createScrollups(JPanel rootPanel) {
      for (int i = 0; i < NUM_SCROLL_UPS; i++) {

         JXTaskPane panel = new JXTaskPane();
         panel.setLayout(new GridLayout(2, 1, 0, 5));
         panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

         panel.add(new JButton("Button on Scrollup: " + i + " #1"));
         panel.add(new JButton("Button on Scrollup: " + i + " #2"));
         
         rootPanel.add(panel);
      }
   }

}
