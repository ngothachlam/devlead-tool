package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.ActionEvent;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.jira.JiraIssue;
import junit.framework.TestCase;

public class AddNewRowActionListenerTest extends TestCase {

	public void testShouldComputeJiraOk() {
		AddNewRowActionListener listener = getListener();
		assertEquals("llu-1", listener.getJiraString("llu", "1"));
		assertEquals("llu-1", listener.getJiraString("llu", "1'2"));
		assertEquals("llu-1.2", listener.getJiraString("llu", "1.2"));
		assertEquals("llu-1", listener.getJiraString("llu", "1/2"));
		assertEquals("llu", listener.getJiraString("llu", null));
		assertEquals("1", listener.getJiraString(null, "1"));
		assertEquals("", listener.getJiraString(null, null));
	}

   private AddNewRowActionListener getListener() {
      return new AddNewRowActionListener(null, null, null, null){
         public JiraIssue getJiraIssue(String jira) {
            return null;
         }

         @Override
         public void jiraAdded(String jiraStringm, MyTable table, String estimate, String actual) {
            return;
         }

         @Override
         public void actionPerformed(ActionEvent e) {
            addJiraToTable();
         }
		};
   }
	
	public void testShouldComputeEstimatesOk() {
	   AddNewRowActionListener listener = getListener();
	   assertEquals("", listener.getEstimateString(null));
	   assertEquals("", listener.getEstimateString(""));
	   assertEquals("", listener.getEstimateString("1"));
	   assertEquals("2", listener.getEstimateString("1'2"));
	   assertEquals("2", listener.getEstimateString("1'2'3"));
	   assertEquals("2.3", listener.getEstimateString("1'2.3'3"));
	   assertEquals("2.3", listener.getEstimateString("1'2.3/3"));
	   assertEquals("2.3", listener.getEstimateString("1/2.3/3"));
	}
	
	public void testShouldComputeActualsOk() {
	   AddNewRowActionListener listener = getListener();
	   assertEquals("", listener.getActualString(null));
	   assertEquals("", listener.getActualString(""));
	   assertEquals("", listener.getActualString("1"));
	   assertEquals("", listener.getActualString("1'2"));
	   assertEquals("3", listener.getActualString("1'2'3"));
	   assertEquals("3.5", listener.getActualString("1'2'3.5'4"));
	   assertEquals("3.5", listener.getActualString("1''3.5'4"));
	   assertEquals("3.5", listener.getActualString("''3.5'4"));
	   assertEquals("3.5", listener.getActualString("1'/3.5/4"));
	   assertEquals("3.5", listener.getActualString("//3.5/4"));
	}
	
	public void testShouldComputeBothEmptyOk() {
		AddNewRowActionListener listener = getListener();
		
		assertEquals(true, listener.isHyphenRequired("llu", "1"));
		assertEquals(false, listener.isHyphenRequired("llu", null));
		assertEquals(false, listener.isHyphenRequired(null, "1"));
		assertEquals(false, listener.isHyphenRequired(null, null));
	}
}
