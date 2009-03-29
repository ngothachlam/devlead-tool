package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.jonas.common.swing.MyPanel;

public class AddReconcilePanel extends AbstractAddPanel {

   private JButton addButton;

   public AddReconcilePanel(final Frame frame) {
      super(new BorderLayout(), frame);
   }

   @Override
   protected Component getButtonPanel() {
      MyPanel buttonPanel = new MyPanel(new GridLayout(1,2));
      buttonPanel.bordered();
      
      addButton = new JButton();
      buttonPanel.add(new JLabel(""));
      buttonPanel.add(addButton);
      // this.addButton(buttonPanel, "Close", new ActionListener() {
      // @Override
      // public void actionPerformed(ActionEvent e) {
      // frame.dispose();
      // }
      // });
      return buttonPanel;
   }

   public void setAddButtonAction(Action action) {
      addButton.setAction(action);
   }

}
