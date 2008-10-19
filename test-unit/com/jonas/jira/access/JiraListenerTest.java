package com.jonas.jira.access;

import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.access.listener.JiraListener;

public class JiraListenerTest extends JonasTestCase {

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