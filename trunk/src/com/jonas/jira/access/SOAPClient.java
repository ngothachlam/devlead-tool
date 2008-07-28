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

import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;

/**
 * Sample JIRA SOAP client. Note that the constants sit in the {@link ClientConstants} interface
 */
public class SOAPClient implements ClientConstants {
	private final JiraSoapService jiraSoapService;
	
	public SOAPClient() throws Exception {
		JiraSoapServiceService jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();
		this.jiraSoapService = jiraSoapServiceGetter.getJirasoapserviceV2();
	}

	// To edit the constants, see the ClientConstants interface
	public static void main(String[] args) throws Exception {
		System.out.println("Running test SOAP client...");
		SOAPClient client = new SOAPClient();
		client.test();

		System.out.println("Closing test SOAP client...");
	}

	private void test() throws Exception {
		String token = jiraSoapService.login(LOGIN_NAME, LOGIN_PASSWORD);

		RemoteIssue issue = testGetJira(token, "LLU-1234");
		// printIssueDetails(issue);

		RemoteVersion[] fixVersions = issue.getFixVersions();
		for (int i = 0; i < fixVersions.length; i++) {
			System.out.println(fixVersions[i].getName());
		}
	}

	private RemoteIssue testGetJira(String token, String jiraName) throws RemoteException {
		System.out.println("Testing getIssueById ...");
		RemoteIssue issue = jiraSoapService.getIssue(token, jiraName);
		System.out.println("Returned an issue id: " + issue.getId() + " key: " + issue.getKey());
		return issue;
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
