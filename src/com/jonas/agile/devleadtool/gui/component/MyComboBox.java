/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component;

import javax.swing.JComboBox;

import com.jonas.jira.JiraProject;

public final class MyComboBox extends JComboBox{
	private JiraProject project;
	public MyComboBox(JiraProject project) {
		super(project.getFixVersions(false));
		this.project = project;
	}
	public JiraProject getProject() {
		return project;
	}
}