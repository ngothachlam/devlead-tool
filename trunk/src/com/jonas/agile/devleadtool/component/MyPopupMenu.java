package com.jonas.agile.devleadtool.component;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

public abstract class MyPopupMenu extends JPopupMenu {

   public MyPopupMenu(Component source) {
      source.addMouseListener(new PopupListener(this));
   }

   @Override
   public void show(Component invoker, int x, int y) {
      super.show(invoker, x, y);
   }

   private class PopupListener extends MouseAdapter {
      private final MyPopupMenu popup;

      public PopupListener(MyPopupMenu popup) {
         this.popup = popup;
      }

      private void maybeShowPopup(MouseEvent e) {
         if (e.isPopupTrigger()) {
            popup.show(e.getComponent(), e.getX(), e.getY());
         }
      }

      public void mousePressed(MouseEvent e) {
         maybeShowPopup(e);
      }

      public void mouseReleased(MouseEvent e) {
         maybeShowPopup(e);
      }
   }
}
