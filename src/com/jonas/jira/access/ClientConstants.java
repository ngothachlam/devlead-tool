package com.jonas.jira.access;

import java.util.Date;

public interface ClientConstants
{
    // Login details
    static final String LOGIN_NAME = "soaptester";
    static final String LOGIN_PASSWORD = "soaptester";

    // Constants for issue creation
    static final String PROJECT_KEY = "LLU";
	static final String JIRA_URL_AOLBB = "http://10.155.38.105/jira";
	static final String JIRA_URL_ATLASSIN = "http://jira.atlassian.com";
	
	static final String WS_LOCATION = "/rpc/soap/jirasoapservice-v2";
}
