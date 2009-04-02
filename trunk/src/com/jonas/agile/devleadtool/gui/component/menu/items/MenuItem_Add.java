/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu.items;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import com.jonas.agile.devleadtool.gui.component.dialog.AddManualDialog;
import com.jonas.agile.devleadtool.gui.component.menu.MyTablePopupMenu;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;

public class MenuItem_Add extends MyMenuItem {

   private final MyTable source;
   private final MyTable[] dtos;

   public MenuItem_Add(MyTablePopupMenu myTablePopupMenu, String string, final MyTable source, final JFrame frame, final MyTable... dtos) {
      super(frame, string);
      this.source = source;
      this.dtos = dtos;
   }

   @Override
   public void myActionPerformed(ActionEvent e) {
      AddManualDialog addManualDialog = new AddManualDialog(getParentFrame(), dtos);
      addManualDialog.setSourceTable(source);
      addManualDialog.focusPrefix();
   }
}