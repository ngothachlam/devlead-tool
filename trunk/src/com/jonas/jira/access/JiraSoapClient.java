package com.jonas.jira.access;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;

import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;

import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.logging.MyLogger;

/**
 * Sample JIRA SOAP client. Note that the constants sit in the {@link ClientConstants} interface
 */
public class JiraSoapClient {

	private static final String PROJECT_LLU = "LLU";
	private static final String LOGIN_NAME = "soaptester";
	private static final String LOGIN_PASSWORD = "soaptester";

	interface JiraAccessAction {

		Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException, RemoteException,
				java.rmi.RemoteException;

	}

	class JiraTokenCommand {

		private final JiraAccessAction action;

		public JiraTokenCommand(JiraAccessAction action) {
			this.action = action;
		}

		public Object execute() throws Exception {
			Object issue;
			try {
				MyLogger.getLogger(JiraSoapClient.class).trace("Conducting Action");
				issue = action.accessJiraAndReturn();
				MyLogger.getLogger(JiraSoapClient.class).trace("Conducting Action Done!");
			} catch (RemotePermissionException e) {
				MyLogger.getLogger(JiraSoapClient.class).error(e);
			} catch (RemoteAuthenticationException e) {
				MyLogger.getLogger(JiraSoapClient.class).error(e);
			} catch (com.atlassian.jira.rpc.exception.RemoteException e) {
				MyLogger.getLogger(JiraSoapClient.class).warn(e);
				renewToken();
			} catch (RemoteException e) {
				MyLogger.getLogger(JiraSoapClient.class).warn(e);
				renewToken();
			}
			issue = action.accessJiraAndReturn();

			return issue;
		}

	}

	private final JiraSoapService jiraSoapService;
	private String token;

	public JiraSoapClient() throws Exception {
		JiraSoapServiceService jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();
		this.jiraSoapService = jiraSoapServiceGetter.getJirasoapserviceV2();
	}

	// To edit the constants, see the ClientConstants interface
	public static void main(String[] args) throws Exception {
		MyLogger.getLogger(JiraSoapClient.class).debug("Running test SOAP client...");
		JiraSoapClient client = new JiraSoapClient();
//		client.test("LLU-1234");
//		client.test("LLU-1235");
		
		RemoteVersion fixVersion = client.getFixVersion("Version 11");
		
		
		MyLogger.getLogger(JiraSoapClient.class).debug("Closing test SOAP client...");
	}

	private void test(String jira) throws Exception {
		RemoteIssue issue = getJira(jira);
		// printIssueDetails(issue);

		RemoteVersion[] fixVersions = issue.getFixVersions();
		for (int i = 0; i < fixVersions.length; i++) {
			System.out.println(fixVersions[i].getName());
		}
	}

	private String getToken() {
		MyLogger.getLogger(JiraSoapClient.class).debug("Getting Token");
		if (token == null) {
			MyLogger.getLogger(JiraSoapClient.class).trace("Syncing Token Block");
			synchronized (this) {
				MyLogger.getLogger(JiraSoapClient.class).trace("Syncing Token Block has been synced");
				if (token == null) {
					try {
						renewToken();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				MyLogger.getLogger(JiraSoapClient.class).trace("Block Synced Done!");
			}
		}
		MyLogger.getLogger(JiraSoapClient.class).trace("Getting Token Done!");
		return token;
	}

	private void renewToken() {
		MyLogger.getLogger(JiraSoapClient.class).debug("Renewing Token");
		try {
			token = jiraSoapService.login(LOGIN_NAME, LOGIN_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
			MyLogger.getLogger(JiraSoapClient.class).error(e);
		}
		MyLogger.getLogger(JiraSoapClient.class).trace("Renewing Token Done!");
	}

	public RemoteIssue getJira(final String jiraName) throws Exception {
		MyLogger.getLogger(JiraSoapClient.class).debug("Getting Jira");

		JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {

			public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
					com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
				return jiraSoapService.getIssue(getToken(), jiraName);
			}

		});
		MyLogger.getLogger(JiraSoapClient.class).trace("Getting Jira Done!");
		return (RemoteIssue) command.execute();
	}

	public RemoteVersion getFixVersion(final String fixName) throws Exception {
		MyLogger.getLogger(JiraSoapClient.class).debug("Getting FixVersion");
		
		JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
			
			public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
			com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
				return jiraSoapService.getVersions(getToken(), PROJECT_LLU);
			}
			
		});
		MyLogger.getLogger(JiraSoapClient.class).trace("Getting Jira Done!");
		RemoteVersion[] versions = (RemoteVersion[]) command.execute();
		for (int i = 0; i < versions.length; i++) {
			if(fixName.trim().equals(versions[i].getName().trim())){
				return versions[i];
			}
		}
		return null;
		
	}
	
	private void printIssueDetails(RemoteIssue issue) {
		System.out.println("Issue Details");
		Method[] declaredMethods = issue.getClass().getDeclaredMethods();
		for (int i = 0; i < declaredMethods.length; i++) {
			Method declaredMethod = declaredMethods[i];
			if (declaredMethod.getName().startsWith("get") && declaredMethod.getParameterTypes().length == 0) {
				System.out.print("Issue." + declaredMethod.getName() + "() -> ");
				try {
					Object o = declaredMethod.invoke(issue, new Object[] {});
					if (o instanceof Object[])
						System.out.println(printArray((Object[]) o));
					else
						System.out.println(o);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String printArray(Object[] o) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < o.length; i++) {
			sb.append(o[i] + " ");
		}
		return sb.toString();
	}

	// Returns the contents of the file in a byte array.
	// From http://javaalmanac.com/egs/java.io/File2ByteArray.html
	private byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length < Integer.MAX_VALUE) {
			// Create the byte array to hold the data
			byte[] bytes = new byte[(int) length];

			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}

			// Close the input stream and return bytes
			is.close();
			return bytes;
		} else {
			System.out.println("File is too large");
			return null;
		}

	}

}
