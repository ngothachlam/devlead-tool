package com.jonas.jira.access;

import junit.framework.TestCase;

public class JiraListenerTest extends TestCase {

	public static void testJiraListener() {
		final BooleanWrapper access = new BooleanWrapper(false);
		JiraListener jiraListener = new JiraListener() {
			public void notifyOfAccess(JiraAccessUpdate accessUpdate) {
				access.setValue(true);
			}
		};
		JiraListener.addJiraListener(jiraListener);
		assertFalse(access.getValue());
		JiraListener.notifyListenersOfAccess(JiraListener.JiraAccessUpdate.GETTING_FIXVERSION);
		assertTrue(access.getValue());
	}

}

class BooleanWrapper {
	private boolean b;

	public BooleanWrapper(boolean b) {
		this.b = b;
	}

	public void setValue(boolean c) {
		b = c;
	}

	public boolean getValue() {
		return b;
	}
}