package com.jonas.agile.devleadtool.gui.component.menu;

import javax.swing.JFrame;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.listener.OpenJirasListener;
import com.jonas.common.logging.MyLogger;

public class MyTablePopupMenu extends MyPopupMenu {

   final static Logger log = MyLogger.getLogger(MyTablePopupMenu.class);
   private final MyTable sourceTable;

   public MyTablePopupMenu(final MyTable source, PlannerHelper helper, MyTable... tables) {
      super(source);
      this.sourceTable = source;
      JFrame parentFrame = helper.getParentFrame();

      MenuItem_Mark menuItem = new MenuItem_Mark(parentFrame, "Mark Selected Rows", source);
      log.debug("menuItem Parent: " + menuItem.getParent());
      add(menuItem);
      add(new MenuItem_UnMark(parentFrame, "unMark Selected Rows", source));
      add(new MenuItem_UnSelect(parentFrame, "clear Selected Rows", source));
      add(new MenuItem_Print(parentFrame, "print Table", source));
      addSeparator();
      add(new MenuItem_Add(this, "Add Jiras", source, parentFrame, tables));
      add(new MenuItem_Remove("Remove Jiras", sourceTable, parentFrame));
      addMenuItem_Copys(source, parentFrame, tables);
      addSeparator();
      add(new MenuItem_Default("Open in Browser", new OpenJirasListener(sourceTable, parentFrame), parentFrame));
      add(new MenuItem_Sync(sourceTable, helper));
      addSeparator();
      add(new MenuItem_Rollforwards(sourceTable, helper));
      add(new MenuItem_CreateMerge("Create Merge", source, parentFrame));
   }

   private void addMenuItem_Copys(MyTable source, JFrame parentFrame, MyTable... tables) {
      for (MyTable tableDTO : tables) {
         if (!tableDTO.equals(source)) {
            String title = "Copy to other Table: " + tableDTO.getTitle();
            add(new MenuItem_Copy(this, parentFrame, title, sourceTable, tableDTO));
         }
      }
   }
};
