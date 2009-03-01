package com.jonas.agile.devleadtool;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.common.logging.MyLogger;

public class Main {

   
   public static void main(String[] args) {
      if(args.length > 0){
         MyLogger.setup(args[0]);
      }
      
      Injector injector = Guice.createInjector(new DevLeadToolModule());
      DevLeadTool tool = injector.getInstance(DevLeadTool.class);
      
      try {
         tool.start();
      } catch (Throwable e) {
         AlertDialog.alertException(tool.getHelper().getParentFrame(), e);
      }
   }

   //FIXME removing selected jiras popup menu doesn't close the progress dialogue!
   // USABILITY add color coding to the JiraPanel 1) Where Dev/Actuals are incorrect, 2) Where sprint is incorrect 3) Where fix Version is incorrect
   // FIXME checking the setEditable checkbox resets the table column widths - it shouldn't!
   // TODO add a checkbox at the top for finding jiras! When ticked - all selects in tables are propagated to other tables and highlighted
   // TODO add a Window menu that allows you to see all open windows and swap to them
   // TODO add a status bar at the bottom showing the total of 1) selected, 2) table entries and 3) model entries for 4)each table
   // TODO add a 'sync with BOARD' entry to set the board status.
   // TODO make board status in jira panel uneditable.
   // TODO make checkbox, boardstatus and numeric renderer to be greyed out when uneditable.
   // TODO add tooltip to show other tables' entries when hovering.
   // TODO drag'n'drop table rows = all tables!!
   // TODO add function to 'drag' lines (not columns) for prioritisation
   // TODO when refreshing jir a info on Jira tab - update combo box for fixversions for plan
   // TODO ... and also save them for the next load session.
   // TODO handle 'issue not found' when syncing to jira and getting info (LLU-2)
   // TODO handle issue moved to other project (LLU-6 that has become LLUOLD-1)
   // TODO use enums to generic handle table model updates and set correct color?
   // TODO add a xml file to save what save location for each planner opened.
   // TODO add system properties to store last save and load location directory
   // TODO add a properties save location C:\devleadproperties.xml containing
   // TODO add property to save frames and locations.
}
