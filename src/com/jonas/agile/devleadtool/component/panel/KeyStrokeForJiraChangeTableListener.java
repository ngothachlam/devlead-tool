/**
 * 
 */
package com.jonas.agile.devleadtool.component.panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTable;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.logging.MyLogger;

final class KeyStrokeForJiraChangeTableListener extends KeyAdapter {
   /**
	 * 
	 */
   private Logger log = MyLogger.getLogger(KeyStrokeForJiraChangeTableListener.class);
   private final MyTable table;
   private MyTableModel model;
   private Column jiraColumn;

   /**
    * @param table
    * @param columnToListenForKeystrokesOn TODO
    */
   KeyStrokeForJiraChangeTableListener(MyTable table, Column columnToListenForKeystrokesOn) {
      this.table = table;
      model = (MyTableModel) table.getModel();
      jiraColumn = columnToListenForKeystrokesOn;
   }

   public void keyTyped(KeyEvent e) {
      // FIXME is there a better way of doing this - not very nice!
      JTable aTable = (JTable) e.getSource();
      int itsRow = aTable.getEditingRow();
      int itsColumn = aTable.getEditingColumn();
      if (table.isColumn(jiraColumn, itsColumn)) {
//      if (table.isColumEqual(itsColumn, Column.Jira)) {
         char keyChar = e.getKeyChar();
         log.debug("itsRow: " + itsRow + "itsColumn: " + itsColumn + " keyChar: " + keyChar);
         String valueAt = (String) model.getValueAt(itsRow, itsColumn);
         table.setValueAt(valueAt + keyChar, itsRow, itsColumn);
         if (keyChar == 8) {
            if (valueAt != null && valueAt.length() > 0)
               table.setValueAt(valueAt.substring(0, valueAt.length() - 1), itsRow, itsColumn);
         }
         log.debug("value  " + table.getValueAt(itsRow, itsColumn));
      }
   }

}