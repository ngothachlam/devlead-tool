package com.jonas.agile.devleadtool;

import com.jonas.agile.devleadtool.component.dialog.AlertDialog;

public class Main {

	public static void main(String[] args) {
		DevLeadTool tool = new DevLeadTool();
		try {
			tool.start();
		} catch (Exception e) {
			AlertDialog.alertException(tool.getHelper(), e);
		}
	}

	// TODO add filter for *.xls to load dialog
	// TODO Add project textbox for default jiraprefix when adding to board
	// TODO add lock mechanism to unable checkbox ticking, etc to prevent accidental changes
	// TODO add checkboxes to renderer
	// TODO add other colors and make this OO - Jira status vs. Status code (inprogress should be mapped to blue (focus, selected,
	// editable)
	// TODO add a xml file to save what save location for each planner opened.
	// TODO add system properties to store last save and load location directory
	// TODO add system properties to store last window position and size
	// TODO add remove button to Plan
	// TODO add function to 'drag' lines (not columns) for prioritisation
	// TODO add a properties save location C:\devleadproperties.xml containing
	// TODO add property to save frames and locations.
	// TODO when editing jira - update jira link realtime.
}