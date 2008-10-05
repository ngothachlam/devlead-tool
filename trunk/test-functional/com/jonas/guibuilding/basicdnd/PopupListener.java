/**
 * 
 */
package com.jonas.guibuilding.basicdnd;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.jonas.agile.devleadtool.component.MyTablePopupMenu;

public class PopupListener extends MouseAdapter {
   private MyTablePopupMenu popup;

   public PopupListener(MyTablePopupMenu popup) {
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