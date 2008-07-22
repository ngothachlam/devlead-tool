package com.jonas.agile.devleadtool;

public class Main {

	public static void main(String[] args) {
		DevLeadTool tool = new DevLeadTool();
		tool.start();
	}

	// TODO add saveDialog to only save the focused "planner" 
	// TODO add checkboxes to renderer
	// TODO add other colors and make this OO - Jira status vs. Status code (inprogress should be mapped to blue (focus, selected, editable)
	// TODO add a xml file to save what save location for each planner opened.
	// TODO add system properties to store last save and load location directory
	// TODO add system properties to store last window position and size
}
