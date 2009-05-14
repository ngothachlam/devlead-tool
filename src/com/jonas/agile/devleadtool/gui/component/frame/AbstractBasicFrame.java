package com.jonas.agile.devleadtool.gui.component.frame;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jonas.agile.devleadtool.data.SystemProperties;
import com.jonas.common.swing.SwingUtil;

public abstract class AbstractBasicFrame extends JFrame {

   private Component parent;
   private Integer width;
   private Integer height;

   public AbstractBasicFrame(Component parent, Integer width, Integer height) {
      this(parent, width, height, false);
   }

   public AbstractBasicFrame(Component parent, Integer width, Integer height, boolean exitOnCLose) {
      this.width = width;
      this.height = height;
      if (exitOnCLose) {
         addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               super.windowClosing(e);
               SystemProperties.close();
               System.exit(0);
            }
         });
      }
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
