package com.jonas.agile.devleadtool.component.listener;

import java.awt.Frame;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class TableModelListenerAlerter implements TableModelListener {
   private Map<Integer, String> rowsModified = new HashMap<Integer, String>();
   private boolean activated;
   private StringBuffer sb = new StringBuffer();
   private Frame parent;

   public TableModelListenerAlerter() {
   }

   @Override
   public void tableChanged(TableModelEvent e) {
      if (!activated)
         return;
      int row = e.getFirstRow();
      if (!rowsModified.containsKey(row)) {
         MyTableModel model = (MyTableModel) e.getSource();
         String jira = (String) model.getValueAt(Column.Jira, row);
         sb.append(jira).append(" was ");

         switch (e.getType()) {
         case TableModelEvent.INSERT:
            sb.append("inserted\n");
            break;
         case TableModelEvent.UPDATE:
            sb.append("updated. ");
            break;
         case TableModelEvent.DELETE:
            sb.append("deleted\n");
         default:
            break;
         }

         rowsModified.put(row, jira);
      }
   }

   public void activate() {
      this.activated = true;
   }

   public void deActivateAndAlert() {
      AlertDialog.alertMessage(parent, "Jiras Added", "The follow jiras were added or amended", sb.toString());
      reset();
   }

   private void reset() {
      activated = false;
      sb.delete(0, sb.length());
      rowsModified.clear();
   }

   public void setParent(Frame frame) {
      this.parent = frame;
   }

}