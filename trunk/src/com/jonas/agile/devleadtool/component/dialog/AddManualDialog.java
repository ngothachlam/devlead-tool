package com.jonas.agile.devleadtool.component.dialog;

import java.awt.Window;
import javax.swing.JFrame;
import com.jonas.agile.devleadtool.component.panel.AbstractAddPanel;
import com.jonas.agile.devleadtool.component.panel.AddManualPanel;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.SwingUtil;

public class AddManualDialog extends JFrame {

   private AbstractAddPanel addManualPanel;

   public AddManualDialog(Window frame, MyTable... tables) {
      super();
      addManualPanel = new AddManualPanel(this, tables);
      this.setContentPane(addManualPanel);
      this.pack();
      this.setSize(220, 450);

      SwingUtil.centreWindowWithinWindow(this, frame);
      setVisible(true);
   }

   public void setSourceTable(MyTable target) {
      addManualPanel.setTarget(target);
   }

   public void focusPrefix() {
      addManualPanel.focusPrefix();
   }

}
