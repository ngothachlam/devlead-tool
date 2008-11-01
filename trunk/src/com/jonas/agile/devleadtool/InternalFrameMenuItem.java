package com.jonas.agile.devleadtool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DesktopManager;
import javax.swing.JMenuItem;
import com.jonas.agile.devleadtool.component.MyInternalFrame;

public class InternalFrameMenuItem extends JMenuItem {

   private final class TheActionListener implements ActionListener {
      private final MyInternalFrame internalFrame;
      public TheActionListener(MyInternalFrame internalFrame) {
         this.internalFrame = internalFrame;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         DesktopManager desktopManager = internalFrame.getDesktopPane().getDesktopManager();
         desktopManager.activateFrame(internalFrame);
      }
   }

   private static final Map<MyInternalFrame, InternalFrameMenuItem> map = new HashMap<MyInternalFrame, InternalFrameMenuItem>();
   private final MyInternalFrame internalFrame;

   public InternalFrameMenuItem(MyInternalFrame internalFrame) {
      super(internalFrame.getTitle());
      map.put(internalFrame, this);
      this.internalFrame = internalFrame;
      addActionListener(new TheActionListener(internalFrame));
   }

   public static InternalFrameMenuItem get(MyInternalFrame internalFrame) {
      return map.get(internalFrame);
   }

   public void titleChanged() {
      this.setText(internalFrame.getTitle());
   }

}
