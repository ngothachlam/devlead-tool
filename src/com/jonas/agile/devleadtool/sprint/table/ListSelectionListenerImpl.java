package com.jonas.agile.devleadtool.sprint.table;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.logging.MyLogger;

public class ListSelectionListenerImpl implements ListSelectionListener {

   private JTextField nameTextField;
   private JXDatePicker startDatePicker;
   private JXDatePicker endDatePicker;
   private JTextField lengthTextField;
   private JXTable table;
   private SprintCache sprintCache;
   
   public ListSelectionListenerImpl(){
   }

   public void setSprintCache(SprintCache sprintCache) {
      this.sprintCache = sprintCache;
   }

   private static final Logger log = MyLogger.getLogger(ListSelectionListenerImpl.class);
   private JTextArea noteTextArea;

   @Override
   public void valueChanged(ListSelectionEvent e) {
      if (!e.getValueIsAdjusting()) {
         // The mouse button has not yet been released
         int row = table.getSelectedRow();
         if (row < 0)
            return;
         row = table.convertRowIndexToModel(row);

         log.debug("source: " + e.getSource());
         log.debug("getting row: " + row);
         String sprintName = (String) table.getModel().getValueAt(row, 0);
         Sprint oldSprint = sprintCache.getSprintFromRow(row);

         nameTextField.setText(oldSprint.getName());
         startDatePicker.setDate(oldSprint.getStartDate());
         endDatePicker.setDate(oldSprint.getEndDate());
         lengthTextField.setText(oldSprint.getLength().toString());
         noteTextArea.setText(oldSprint.getNote());
      }

   }

   public void setSourceTable(JXTable table) {
      this.table = table;

   }

   public void setTargets(JTextField nameTextField, JXDatePicker startDatePicker, JXDatePicker endDatePicker, JTextField lengthTextField, JTextArea noteTextArea) {
      this.nameTextField = nameTextField;
      this.startDatePicker = startDatePicker;
      this.endDatePicker = endDatePicker;
      this.lengthTextField = lengthTextField;
      this.noteTextArea = noteTextArea;
   }

}
