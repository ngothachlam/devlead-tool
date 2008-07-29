package com.jonas.jira.access;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;

import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.jira.JiraProject;
import com.jonas.logging.MyLogger;

/**
 * Sample JIRA SOAP client. Note that the constants sit in the {@link ClientConstants} interface
 */
public class JiraSoapClient {

	private static final Logger LOGGER = MyLogger.getLogger(JiraSoapClient.class);
	private static final String LOGIN_NAME = "soaptester";
	private static final String LOGIN_PASSWORD = "soaptester";

	interface JiraAccessAction {
		Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException, RemoteException,
				java.rmi.RemoteException;
	}

	class JiraTokenCommand {

		private final Logger LOGGER = MyLogger.getLogger(JiraTokenCommand.class);
		private final JiraAccessAction action;

		public JiraTokenCommand(JiraAccessAction action) {
			this.action = action;
		}

		public Object execute() throws Exception {
			Object issue;
			try {
				LOGGER.trace("Conducting Action");
				issue = action.accessJiraAndReturn();
				LOGGER.trace("Conducting Action Done!");
			} catch (RemotePermissionException e) {
				LOGGER.error(e);
			} catch (RemoteAuthenticationException e) {
				LOGGER.error(e);
			} catch (com.atlassian.jira.rpc.exception.RemoteException e) {
				LOGGER.warn(e);
				renewToken();
			} catch (RemoteException e) {
				LOGGER.warn(e);
				renewToken();
			}
			issue = action.accessJiraAndReturn();

			return issue;
		}

	}

	private JiraSoapService jiraSoapService = null;
	private String token;

	public JiraSoapClient() {
		try {
			JiraSoapServiceService jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();
			this.jiraSoapService = jiraSoapServiceGetter.getJirasoapserviceV2();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// To edit the constants, see the ClientConstants interface
	public static void main(String[] args) throws Exception {
		LOGGER.debug("Running test SOAP client...");
		JiraSoapClient client = new JiraSoapClient();
		// client.test("LLU-1234");
		// client.test("LLU-1235");

		RemoteVersion fixVersion = client.getFixVersion("Version 11");

		LOGGER.debug("Closing test SOAP client...");
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
		LOGGER.debug("Getting Token");
		if (token == null) {
			LOGGER.trace("Syncing Token Block");
			synchronized (this) {
				LOGGER.trace("Syncing Token Block has been synced");
				if (token == null) {
					try {
						renewToken();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				LOGGER.trace("Block Synced Done!");
			}
		}
		LOGGER.trace("Getting Token Done!");
		return token;
	}

	private void renewToken() {
		LOGGER.debug("Renewing Token");
		try {
			token = jiraSoapService.login(LOGIN_NAME, LOGIN_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e);
		}
		LOGGER.trace("Renewing Token Done!");
	}

	public RemoteIssue getJira(final String jiraName) throws Exception {
		LOGGER.debug("Getting Jira");

		JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {

			public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
					com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
				return jiraSoapService.getIssue(getToken(), jiraName);
			}

		});
		LOGGER.trace("Getting Jira Done!");
		return (RemoteIssue) command.execute();
	}

	public RemoteVersion getFixVersion(final String fixName) throws Exception {
		LOGGER.debug("Getting FixVersion");

		RemoteVersion[] versions = getFixVersions();
		for (int i = 0; i < versions.length; i++) {
			if (fixName.trim().equals(versions[i].getName().trim())) {
				return versions[i];
			}
		}
		return null;
	}

	public RemoteVersion[] getFixVersions() throws Exception {
		JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
			public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
					com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
				return jiraSoapService.getVersions(getToken(), JiraProject.LLU_SYSTEMS_PROVISIONING.getJiraKey());
			}

		});
		LOGGER.trace("Getting Jira Done!");
		RemoteVersion[] versions = (RemoteVersion[]) command.execute();
		return versions;
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
