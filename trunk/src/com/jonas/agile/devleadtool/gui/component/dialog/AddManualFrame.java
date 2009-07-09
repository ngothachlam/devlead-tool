package com.jonas.agile.devleadtool.gui.component.dialog;

import java.awt.Window;
import javax.swing.JFrame;
import com.jonas.agile.devleadtool.gui.component.panel.AbstractAddPanel;
import com.jonas.agile.devleadtool.gui.component.panel.AddManualPanel;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.swing.SwingUtil;

public class AddManualFrame extends JFrame {

   private AbstractAddPanel addManualPanel;

   public AddManualFrame(Window frame, MyTable... tables) {
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
