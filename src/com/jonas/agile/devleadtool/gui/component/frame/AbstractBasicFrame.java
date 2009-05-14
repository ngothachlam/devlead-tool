package com.jonas.agile.devleadtool.gui.component.frame;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JFrame;
import com.jonas.common.swing.SwingUtil;

public abstract class AbstractBasicFrame extends JFrame {

   private Component parent;
   private Integer width;
   private Integer height;

   public AbstractBasicFrame(Integer width, Integer height) {
      this.width = width;
      this.height = height;
   }

   public AbstractBasicFrame(Component parent, Integer width, Integer height) {
      this(width, height);
      this.parent = parent;
   }

   @Override
   public final void setVisible(boolean isVisible) {
      if (isVisible) {
         this.setContentPane(getMyPanel());
         this.pack();
         if (width != null && height != null)
            this.setSize(width, height);

         if (parent != null)
            SwingUtil.centreWindowWithinWindow(this, parent);
      }
      super.setVisible(isVisible);
   }

   public abstract Container getMyPanel();
}
