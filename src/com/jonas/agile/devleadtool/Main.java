package com.jonas.agile.devleadtool;

import com.jonas.agile.devleadtool.component.dialog.AlertDialog;

public class Main {

   public static void main(String[] args) {
      DevLeadTool tool = new DevLeadTool();
      try {
         tool.start();
      } catch (Throwable e) {
         AlertDialog.alertException(tool.getHelper(), e);
      }
   }

   // FIXME when loading in a excel sheet with a header with incorrect name it doesn't work!!
   // TODO drag'n'drop table rows = all tables!!
   // FIXME the Open and Resolved need from Soap Client need to get the text of the object - not the id.
   // FIXME Fixed? the sync issues in planpanel doesn't work after we have reorderered
   // TODO get SoapClient to get status/resolvement and return as strings.
   // TODO when refreshing jira info on Jira tab - update combo box for fixversions for plan
   // TODO ... and also save them for the next load session.
   // TODO Add project textbox for default jiraprefix when adding to board
   // TODO in boardPanel create the HyperLinker within the JiraIssue or make something nicer so you can set
   // "defaultCellRenderer(HyperLink.class, blah)"
   // TODO handle 'issue not found' when syncing to jira and getting info (LLU-2)
   // TODO handle issue moved to other project (LLU-6 that has become LLUOLD-1)
   // TODO add lock mechanism to unable checkbox ticking, etc to prevent accidental changes
   // TODO add other colors and make this OO - Jira status vs. Status code (inprogress should be mapped to blue (focus, selected, editable)
   // TODO use enums to generic handle table model updates and set correct color?
   // TODO add a xml file to save what save location for each planner opened.
   // TODO add system properties to store last save and load location directory
   // TODO add system properties to store last window position and size
   // TODO add remove button to Plan
   // TODO add function to 'drag' lines (not columns) for prioritisation
   // TODO add a properties save location C:\devleadproperties.xml containing
   // TODO add property to save frames and locations.
   // TODO when editing jira - update jira link realtime.
}
