package com.jonas.testing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

public class TaskPaneExample extends JPanel {
   /** simple main driver for this class */

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            new TaskPaneExample();
         }
      });
   }

   public TaskPaneExample() {
      JFrame frame = new JFrame("TaskPane Example 1");
      frame.add(makeContent());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocationRelativeTo(null);
      frame.pack();
      frame.setVisible(true);
   }

   /** creates a JXLabel and attaches a painter to it. */

   private Component makeContent() {
      JXPanel panel = new JXPanel();
      panel.setLayout(new BorderLayout());

      JXTaskPaneContainer taskpanecontainer = new JXTaskPaneContainer();
      JXTaskPane taskpane = getJXTaskPane();

      taskpanecontainer.add(taskpane);
      panel.add(taskpanecontainer, BorderLayout.CENTER);
      panel.setPreferredSize(new Dimension(250, 200));
      return panel;

   }

   private JXTaskPane getJXTaskPane() {
      JXTaskPane taskpane = new JXTaskPane();
      taskpane.setTitle("My Tasks");

      final JXLabel label = new JXLabel("label");
      taskpane.add(label);

      taskpane.add(new SecondAction(label));
      return taskpane;
   }

   private class SecondAction extends AbstractAction {
      private final JXLabel label;
      {
         putValue(Action.NAME, "task pane item 2 : an action");
         putValue(Action.SHORT_DESCRIPTION, "perform an action");
      }

      public SecondAction(JXLabel label) {
         this.label = label;
      }

      public void actionPerformed(ActionEvent e) {
         label.setText("an action performed");
      }
   }
}
