package com.jonas.agile.devleadtool.gui.component.frame;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JFrame;
import com.jonas.common.swing.SwingUtil;

public abstract class AbstractBasicFrame extends JFrame {

   private Component parent;
   private int width;
   private int height;

   public AbstractBasicFrame(int width, int height) {
      this.width = width;
      this.height = height;
   }

   public AbstractBasicFrame(Component parent, int width, int height) {
      this(width, height);
      this.parent = parent;
   }

   @Override
   public final void setVisible(boolean isVisible) {
      if (isVisible) {
         this.setContentPane(getMyPanel());
         this.pack();
         this.setSize(width, height);

         if (parent != null)
            SwingUtil.centreWindowWithinWindow(this, parent);
      }
      super.setVisible(isVisible);
   }

   public abstract Container getMyPanel();
}
