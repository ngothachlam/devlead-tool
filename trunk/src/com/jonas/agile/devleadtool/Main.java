package com.jonas.agile.devleadtool;

import com.jonas.agile.devleadtool.component.dialog.AlertDialog;

public class Main {

   public static void main(String[] args) {
      DevLeadTool tool = new DevLeadTool();
      try {
         tool.start();
      } catch (Throwable e) {
         AlertDialog.alertException(tool.getHelper().getParentFrame(), e);
      }
   } 

   // TODO drag'n'drop table rows = all tables!!
   // TODO add function to 'drag' lines (not columns) for prioritisation
   // TODO when refreshing jira info on Jira tab - update combo box for fixversions for plan
   // TODO ... and also save them for the next load session.
   // TODO handle 'issue not found' when syncing to jira and getting info (LLU-2)
   // TODO handle issue moved to other project (LLU-6 that has become LLUOLD-1)
   // TODO use enums to generic handle table model updates and set correct color?
   // TODO add a xml file to save what save location for each planner opened.
   // TODO add system properties to store last save and load location directory
   // TODO add a properties save location C:\devleadproperties.xml containing
   // TODO add property to save frames and locations.
}
