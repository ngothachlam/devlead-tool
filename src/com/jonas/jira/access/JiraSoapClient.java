package com.jonas.jira.access;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;

import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;

/**
 * Sample JIRA SOAP client. Note that the constants sit in the {@link ClientConstants} interface
 */
public class JiraSoapClient {

	private final Logger Log = MyLogger.getLogger(JiraSoapClient.class);
	private static final String LOGIN_NAME = "soaptester";
	private static final String LOGIN_PASSWORD = "soaptester";

	interface JiraAccessAction {
		Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException, RemoteException,
				java.rmi.RemoteException;
	}

	class JiraTokenCommand {

		private final Logger Log = MyLogger.getLogger(JiraTokenCommand.class);
		private final JiraAccessAction action;

		public JiraTokenCommand(JiraAccessAction action) {
			this.action = action;
		}

		public Object execute() throws RemotePermissionException, RemoteAuthenticationException, RemoteException {
			Object issue;
			try {
				Log.debug("ConductingAction : " + action);
				return action.accessJiraAndReturn();
			} catch (RemotePermissionException e) {
				Log.error(e);
			} catch (RemoteAuthenticationException e) {
				Log.error(e);
			} catch (com.atlassian.jira.rpc.exception.RemoteException e) {
				Log.warn(e);
			} catch (RemoteException e) {
				Log.warn(e);
			}
			Log.info("2nd renew within Action!!");
			renewToken();
			issue = action.accessJiraAndReturn();
			return issue;
		}

	}

	private JiraSoapService jiraSoapService = null;
	private String token;

	public JiraSoapClient(JiraSoapService jiraSoapService) {
		this.jiraSoapService = jiraSoapService;
	}

	private String getToken() {
		Log.debug("Getting Token!");
		if (token == null) {
			Log.trace("Syncing Token Block");
			synchronized (this) {
				Log.trace("Syncing Token Block has been synced");
				if (token == null) {
					Log.debug("Token needs renewing!");
					try {
						renewToken();
					} catch (Exception e) {
						e.printStackTrace();
						Log.error(e);
					}
				}
				Log.trace("Block Synced Done!");
			}
		}
		return token;
	}

	private void renewToken() {
		Log.debug("calling Renewing Token");
		try {
			token = jiraSoapService.login(LOGIN_NAME, LOGIN_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e);
		}
		Log.debug("Renewing Token Done!");
	}

	public RemoteIssue getJira(final String jiraName) throws Exception {
		Log.debug("Getting Jira");

		JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {

			public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
					com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
				Log.trace("Getting Jira Action!! (" + getToken() + " - " + jiraName + ")");
				return jiraSoapService.getIssue(getToken(), jiraName);
			}

		});
		RemoteIssue execute = (RemoteIssue) command.execute();
		Log.trace("Getting Jira Done!");
		return execute;
	}

	public RemoteVersion getFixVersion(final String fixName, JiraProject jiraProject) throws Exception {
		Log.debug("Getting FixVersion");

		RemoteVersion[] versions = getFixVersions(jiraProject);
		for (int i = 0; i < versions.length; i++) {
			Log.debug("Checking fixversion: " + versions[i].getName() + " is equal to " + fixName);
			if (fixName.trim().equals(versions[i].getName().trim())) {
				return versions[i];
			}
		}
		Log.debug("Getting FixVersion Done!");
		return null;
	}

	public RemoteVersion[] getFixVersions(final JiraProject jiraProject) throws RemotePermissionException, RemoteAuthenticationException,
			RemoteException {
		Log.debug("Getting Fix Versions!");
		JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
			public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
					com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
				return jiraSoapService.getVersions(getToken(), jiraProject.getJiraKey());
			}

		});
		RemoteVersion[] versions = (RemoteVersion[]) command.execute();
		if (Log.isDebugEnabled()) {
			Log.debug("Getting Fix Versions Done!");
			for (int i = 0; i < versions.length; i++) {
				StringBuffer sb = new StringBuffer();
				sb.append("Fix Version[").append(i).append("] {");
				sb.append("name=").append(versions[i].getName());
				sb.append(", id=").append(versions[i].getId());
				sb.append("}");
				Log.debug(sb.toString());
				
			}
		}

		return versions;
	}

	private void printIssueDetails(RemoteIssue issue) {
		Log.debug("Issue Details");
		Method[] declaredMethods = issue.getClass().getDeclaredMethods();
		for (int i = 0; i < declaredMethods.length; i++) {
			Method declaredMethod = declaredMethods[i];
			if (declaredMethod.getName().startsWith("get") && declaredMethod.getParameterTypes().length == 0) {
				Log.debug("Issue." + declaredMethod.getName() + "() -> ");
				try {
					Object o = declaredMethod.invoke(issue, new Object[] {});
					if (o instanceof Object[])
						Log.debug(printArray((Object[]) o));
					else
						Log.debug(o);
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
			Log.debug("File is too large");
			return null;
		}

	}

}
