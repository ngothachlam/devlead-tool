package com.jonas.agile.devleadtool.component.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.jonas.agile.devleadtool.component.Action.BasicAbstractGUIAction;

public class BasicMessageFrame extends AbstractBasicFrame {

   private final String message;

   public BasicMessageFrame(Component parent, String message) {
      super(parent, 600, 500);
      this.message = message;
   }

   @Override
   public Container getMyPanel() {
      JPanel panel = new JPanel(new BorderLayout());
      JTextArea messageArea = new JTextArea(message);

      Container scrollPane = new JScrollPane(messageArea);
      panel.add(scrollPane, BorderLayout.CENTER);

      JButton okButton = new JButton(new OkAction("Ok", "Ok", this));
      panel.add(okButton, BorderLayout.SOUTH);

      return panel;
   }
}


class OkAction extends BasicAbstractGUIAction {

   public OkAction(String name, String description, Frame parentFrame) {
      super(name, description, parentFrame);
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      getParentFrame().dispose();
   }

}
