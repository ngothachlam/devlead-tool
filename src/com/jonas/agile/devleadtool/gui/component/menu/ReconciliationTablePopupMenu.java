package com.jonas.agile.devleadtool.gui.component.menu;

import java.awt.Frame;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.listener.OpenJirasListener;
import com.jonas.common.logging.MyLogger;

public class ReconciliationTablePopupMenu extends MyPopupMenu {

   final static Logger log = MyLogger.getLogger(ReconciliationTablePopupMenu.class);
   private final MyTable sourceTable;

   public ReconciliationTablePopupMenu(final MyTable source, Frame parentFrame) {
      super(source);
      this.sourceTable = source;

      //FIXME add a menuitem that can copy from board (when jira exists) onto this cell in the reconciliation table. 
      add(new MenuItem_UnSelect(parentFrame, "Clear Selected Rows", source));
      add(new MenuItem_Print(parentFrame, "Print Table", source));
      addSeparator();
      add(new MenuItem_Remove("Remove Jiras", sourceTable, parentFrame));
      addSeparator();
      add(new MenuItem_Default("Open in Browser", new OpenJirasListener(sourceTable, parentFrame), parentFrame));
   }
};
