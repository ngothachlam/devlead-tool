package com.jonas.agile.devleadtool.component.table.model;

import com.jonas.jira.JiraIssue;

import junit.framework.TestCase;

public class PlanTableModelTest extends TestCase {

	PlanTableModel model = new PlanTableModel();

	public void testDoesJiraExist() {
		assertFalse(model.doesJiraExist("llu-1"));
		model.addRow(new JiraIssue("llu-1", "status", "resolution"));
		assertTrue(model.doesJiraExist("llu-1"));
		assertFalse(model.doesJiraExist("llu-2"));
		model.addRow(new JiraIssue("llu-2", "status", "resolution"));
		model.removeRow(0);
		assertFalse(model.doesJiraExist("llu-1"));
		assertTrue(model.doesJiraExist("llu-2"));
	}
	
	public void testAddAndSetRowWorks() {
		assertEquals(0, model.getRowCount());
		assertTrue(model.addRow(new JiraIssue("test1", "status1", "resolution1")));
		assertEquals(1, model.getRowCount());
		assertEquals("test1", model.getValueAt(0,0));
		assertEquals("status1", model.getValueAt(0,2));
		assertEquals("resolution1", model.getValueAt(0,3));
		assertTrue(model.addRow(new JiraIssue("test3", "status3", "resolution3")));
		assertTrue(model.setRow(new JiraIssue("test2", "status2", "resolution2"), 0));
		assertEquals(2, model.getRowCount());
		assertEquals("test2", model.getValueAt(0,0));
		assertEquals("status2", model.getValueAt(0,2));
		assertEquals("resolution2", model.getValueAt(0,3));
		assertEquals("test3", model.getValueAt(1,0));
		assertEquals("status3", model.getValueAt(1,2));
		assertEquals("resolution3", model.getValueAt(1,3));
	}
}
