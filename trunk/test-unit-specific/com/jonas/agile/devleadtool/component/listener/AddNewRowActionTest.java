package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.ActionEvent;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.jira.JiraIssue;
import junit.framework.TestCase;

public class AddNewRowActionTest extends TestCase {

	public void testShouldComputeJiraOk() {
		AddNewRowAction listener = getListener();
		assertEquals("llu-1", listener.getJiraString("llu", "1"));
		assertEquals("llu-1", listener.getJiraString("llu", "1'2"));
		assertEquals("llu-1.2", listener.getJiraString("llu", "1.2"));
		assertEquals("llu-1", listener.getJiraString("llu", "1/2"));
		assertEquals("llu", listener.getJiraString("llu", null));
		assertEquals("1", listener.getJiraString(null, "1"));
		assertEquals("", listener.getJiraString(null, null));
	}

   private AddNewRowAction getListener() {
      return new AddNewRowAction(null, null, null, null){

         @Override
         public JiraIssue getJiraIssue(String jira) {
            return null;
         }

         @Override
         public void jiraAdded(String jiraKey, MyTable table, String estimate, String actual, String release) {
            return;
         }

         @Override
         public void actionPerformed(ActionEvent e) {
            addJiraToTable();
         }
		};
   }
	
	public void testShouldComputeEstimatesOk() {
	   AddNewRowAction listener = getListener();
	   assertEquals("", listener.getEstimateString(null));
	   assertEquals("", listener.getEstimateString(""));
	   assertEquals("", listener.getEstimateString(" "));
	   assertEquals("", listener.getEstimateString("1"));
	   assertEquals("2", listener.getEstimateString("1'2"));
	   assertEquals("", listener.getEstimateString("1''3"));
	   assertEquals("2", listener.getEstimateString("1'2'3"));
	   assertEquals("2.3", listener.getEstimateString("1'2.3'3"));
	   assertEquals("2.3", listener.getEstimateString("1'2.3/3"));
	   assertEquals("2.3", listener.getEstimateString("1/2.3/3"));
	}
	
	public void testShouldComputeActualsOk() {
	   AddNewRowAction listener = getListener();
	   assertEquals("", listener.getActualString(null));
	   assertEquals("", listener.getActualString(""));
	   assertEquals("", listener.getActualString("1"));
	   assertEquals("", listener.getActualString("1'2"));
	   assertEquals("3", listener.getActualString("1'2'3"));
	   assertEquals("3", listener.getActualString("1''3"));
	   assertEquals("3.5", listener.getActualString("1'2'3.5'4"));
	   assertEquals("3.5", listener.getActualString("1''3.5'4"));
	   assertEquals("3.5", listener.getActualString("''3.5'4"));
	   assertEquals("", listener.getActualString("'''4"));
	   assertEquals("3.5", listener.getActualString("1'/3.5/4"));
	   assertEquals("3.5", listener.getActualString("//3.5/4"));
	}
	
	public void testShouldComputeBothEmptyOk() {
		AddNewRowAction listener = getListener();
		
		assertEquals(true, listener.isHyphenRequired("llu", "1"));
		assertEquals(false, listener.isHyphenRequired("llu", null));
		assertEquals(false, listener.isHyphenRequired(null, "1"));
		assertEquals(false, listener.isHyphenRequired(null, null));
	}
}
