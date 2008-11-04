package com.jonas.agile.devleadtool.component.listener;

import junit.framework.TestCase;

public class AddNewRowActionListenerTest extends TestCase {

	public void testShouldComputeJiraOk() {
		AddNewRowActionListener listener = new AddNewRowActionListener(null, null, null);
		assertEquals("llu-1", listener.getJiraString("llu", "1"));
		assertEquals("llu-1", listener.getJiraString("llu", "1'2"));
		assertEquals("llu-1", listener.getJiraString("llu", "1'2'3"));
		assertEquals("llu", listener.getJiraString("llu", null));
		assertEquals("1", listener.getJiraString(null, "1"));
		assertEquals("", listener.getJiraString(null, null));
	}
	
	public void testShouldComputeEstimatesOk() {
	   AddNewRowActionListener listener = new AddNewRowActionListener(null, null, null);
	   assertEquals("", listener.getEstimateString(null));
	   assertEquals("", listener.getEstimateString(""));
	   assertEquals("", listener.getEstimateString("1"));
	   assertEquals("2", listener.getEstimateString("1'2"));
	   assertEquals("2", listener.getEstimateString("1'2'3"));
	}
	
	public void testShouldComputeActualsOk() {
	   AddNewRowActionListener listener = new AddNewRowActionListener(null, null, null);
	   assertEquals("", listener.getAtualString(null));
	   assertEquals("", listener.getAtualString(""));
	   assertEquals("", listener.getAtualString("1"));
	   assertEquals("", listener.getAtualString("1'2"));
	   assertEquals("3", listener.getAtualString("1'2'3"));
	   assertEquals("3", listener.getAtualString("1'2'3'4"));
	}
	
	public void testShouldComputeBothEmptyOk() {
		AddNewRowActionListener listener = new AddNewRowActionListener(null, null, null);
		
		assertEquals(true, listener.isHyphenRequired("llu", "1"));
		assertEquals(false, listener.isHyphenRequired("llu", null));
		assertEquals(false, listener.isHyphenRequired(null, "1"));
		assertEquals(false, listener.isHyphenRequired(null, null));
	}
}
