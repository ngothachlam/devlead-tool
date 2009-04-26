package com.jonas.agile.devleadtool.sprint.table;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.menu.MyPopupMenu;
import com.jonas.agile.devleadtool.gui.component.menu.items.MyMenuItem;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.logging.MyLogger;

public class SprintsTablePopupMenu extends MyPopupMenu {

   public SprintsTablePopupMenu(Component source, Frame parent, ExcelSprintDao sprintDao, PlannerHelper helper) {
      super(source);
      add(new DeleteSprintMenuItem((JXTable) source, parent, "Delete Sprint", sprintDao, helper));
   }
}


class DeleteSprintMenuItem extends MyMenuItem {

   private static final Logger log = MyLogger.getLogger(DeleteSprintMenuItem.class);
   private final JXTable source;
   private final JXSprintTableModel model;
   private final ExcelSprintDao sprintDao;
   private final PlannerHelper helper;

   public DeleteSprintMenuItem(JXTable source, Frame parentFrame, String text, ExcelSprintDao sprintDao, PlannerHelper helper) {
      super(parentFrame, text);
      this.source = source;
      this.helper = helper;
      model = (JXSprintTableModel) source.getModel();
      this.sprintDao = sprintDao;
   }

   @Override
   public void myActionPerformed(ActionEvent e) throws Throwable {
      SprintCache sprintCache = helper.getSprintCache();
      int[] rows = source.getSelectedRows();
      for (int counter = rows.length - 1; counter > -1; counter--) {
         int viewRow = rows[counter];
         int row = source.convertRowIndexToModel(viewRow);
         log.debug("trying to delete row: " + viewRow + " (model row: " + row + ")");
         Sprint sprint = sprintCache.getSprintFromRow(row);
         sprintCache.removeSprint(sprint);
         sprintDao.save(helper.getExcelFile(), sprintCache);
         model.fireTableDataChanged();
      }
   }

}