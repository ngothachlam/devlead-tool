package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JButton;
import com.jonas.common.swing.MyPanel;

public class AddReconcilePanel extends AbstractAddPanel {

   private final Frame frame;
   private ActionListener addButtonListener;
   private JButton addButton;

   public AddReconcilePanel(final Frame frame) {
      super(new BorderLayout(), frame);
      this.frame = frame;
   }

   @Override
   protected Component getButtonPanel() {
      MyPanel buttonPanel = new MyPanel(new GridLayout(1, 2, 5, 5));
      buttonPanel.bordered();
      addButton = this.addButton(buttonPanel, "Add");
      this.addButton(buttonPanel, "Close", new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            frame.dispose();
         }
      });
      return buttonPanel;
   }
   
   public void setAddButtonAction(Action action){
      addButton.setAction(action);
   }
}
