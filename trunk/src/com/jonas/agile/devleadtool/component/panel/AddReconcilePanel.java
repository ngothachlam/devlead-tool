package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.JFrame;
import com.jonas.agile.devleadtool.component.TableRadioButton;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.MyPanel;

public class AddReconcilePanel extends AbstractAddPanel {

   private final JFrame frame;
   private ActionListener addButtonListener;
   private Object addButton;

   public AddReconcilePanel(final JFrame frame, MyTable jiraTable) {
      super(new BorderLayout(), frame, jiraTable);
      this.frame = frame;
   }

   public void focusPrefix() {
      getJiraPrefixTextField().requestFocus();
   }

   public void setTarget(MyTable target) {
      Enumeration<?> elements = getTablesButtonGroup().getElements();
      while (elements.hasMoreElements()) {
         TableRadioButton button = (TableRadioButton) elements.nextElement();
         if (button.getTable().equals(target)) {
            button.setSelected(true);
            return;
         }
      }
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
}
