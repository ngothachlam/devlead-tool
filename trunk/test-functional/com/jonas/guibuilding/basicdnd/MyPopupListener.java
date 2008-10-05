/**
 * 
 */
package com.jonas.guibuilding.basicdnd;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.jonas.agile.devleadtool.component.MyTablePopupMenu;
import com.jonas.agile.devleadtool.component.table.MyTable;

public class MyPopupListener extends MouseAdapter {
   private MyTablePopupMenu popup;

   public MyPopupListener(MyTablePopupMenu popup) {
      this.popup = popup;
   }

   private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
         popup.setSourceAndShow((MyTable) e.getComponent(), e.getX(), e.getY());
      }
   }

   public void mousePressed(MouseEvent e) {
      maybeShowPopup(e);
   }

   public void mouseReleased(MouseEvent e) {
      maybeShowPopup(e);
   }
}