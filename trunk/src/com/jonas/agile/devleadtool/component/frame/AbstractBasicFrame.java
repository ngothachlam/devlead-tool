package com.jonas.agile.devleadtool.component.frame;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JFrame;
import com.jonas.common.SwingUtil;

public abstract class AbstractBasicFrame extends JFrame {
   
   public AbstractBasicFrame(Component parent, int width, int height) {
      this.setContentPane(getMyPanel());
      this.pack();
      this.setSize(width, height);

      SwingUtil.centreWindowWithinWindow(this, parent);
      setVisible(true);
   }

   public abstract Container getMyPanel();
}
