package com.jonas.agile.devleadtool.sprint.table;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;
import com.jonas.agile.devleadtool.gui.component.menu.MyPopupMenu;
import com.jonas.agile.devleadtool.gui.component.menu.items.MyMenuItem;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.logging.MyLogger;

public class SprintsTablePopupMenu extends MyPopupMenu {

   public SprintsTablePopupMenu(Component source, Frame parent) {
      super(source);
      add(new DeleteSprintMenuItem((JXTable) source, parent, "Delete Sprint"));
   }
}


class DeleteSprintMenuItem extends MyMenuItem {

   private static final Logger log = MyLogger.getLogger(DeleteSprintMenuItem.class);
   private final JXTable source;
   private final JXSprintTableModel model;

   public DeleteSprintMenuItem(JXTable source, Frame parentFrame, String text) {
      super(parentFrame, text);
      this.source = source;
      model = (JXSprintTableModel) source.getModel();
   }

   @Override
   public void myActionPerformed(ActionEvent e) throws Throwable {
      SprintCache instance = SprintCache.getInstance();
      int[] rows = source.getSelectedRows();
      for (int counter = rows.length - 1; counter > -1; counter--) {
         int viewRow = rows[counter];
         int row = source.convertRowIndexToModel(viewRow);
         log.debug("trying to delete row: " + viewRow + " (model row: " + row + ")");
         Sprint sprint = instance.getSprintFromRow(row);
         instance.removeSprint(sprint);
         model.fireTableDataChanged();
         
         Thread.sleep(2000L);
      }
   }

}