package com.jonas.agile.devleadtool.component.listener;

import junit.framework.TestCase;

public class AddNewRowActionListenerTest extends TestCase {

	public void testShouldComputeJiraOk() {
		AddNewRowActionListener listener = new AddNewRowActionListener(null, null, null);

		assertEquals("llu-1", listener.getJiraString("llu", "1"));
		assertEquals("llu", listener.getJiraString("llu", null));
		assertEquals("1", listener.getJiraString(null, "1"));
		assertEquals("", listener.getJiraString(null, null));
	}
	
	public void testShouldComputeBothEmptyOk() {
		AddNewRowActionListener listener = new AddNewRowActionListener(null, null, null);
		
		assertEquals(true, listener.isHyphenRequired("llu", "1"));
		assertEquals(false, listener.isHyphenRequired("llu", null));
		assertEquals(false, listener.isHyphenRequired(null, "1"));
		assertEquals(false, listener.isHyphenRequired(null, null));
	}
}
