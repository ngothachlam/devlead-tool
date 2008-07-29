package com.jonas.jira.access;

import java.io.IOException;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.access.JiraSoapClient.JiraAccessAction;
import com.jonas.logging.MyLogger;

public class JiraHttpClient extends HttpClient {
	private final String baseUrl;

	private static final Logger LOGGER = MyLogger.getLogger(JiraHttpClient.class);

	interface JiraAccessAction {
		Object accessJiraAndReturn();
	}

	class JiraTokenCommand {

		private final Logger LOGGER = MyLogger.getLogger(JiraHttpClient.class);

		private final JiraAccessAction action;

		public JiraTokenCommand(JiraAccessAction action) {
			this.action = action;
		}

		public Object execute() {
			Object issue;
			LOGGER.trace("Conducting Action");
			issue = action.accessJiraAndReturn();
			return issue;
		}

	}

	public JiraHttpClient() {
		this.baseUrl = "http://10.155.38.105";
	}

	public void loginToJira() throws IOException, HttpException {
		// TODO make this work as logging in, in the background as it does take some time to login
		// TODO how long will the session stay as logged in? Will it require a timeout before automatically logging in (in the
		// background)again?
		LOGGER.debug("Logging onto Jira");
		PostMethod loginMethod = new PostMethod(baseUrl + "/jira/login.jsp");
		loginMethod.addParameter("os_username", "soaptester");
		loginMethod.addParameter("os_password", "soaptester");
		executeMethod(loginMethod);
		LOGGER.debug("Logging onto Jira Done!");
	}

	public List<JiraIssue> getJiras(JiraVersion fixVersion) {
		LOGGER.debug("getting Jiras");
		String url = "/jira/secure/IssueNavigator.jspa?view=rss&&fixfor=" + fixVersion.getJiraKey()
				+ "&pid=10070&sorter/field=issuekey&sorter/order=DESC&reset=true&decorator=none";
		GetMethod method = new GetMethod(baseUrl + url);
		List<JiraIssue> jiras = null;
		try {
			executeMethod(method);
			byte[] responseAsBytes = method.getResponseBody();
			method.releaseConnection();

			SAXBuilder sb = new SAXBuilder();
			String string = new String(responseAsBytes);
			LOGGER.debug("RSS feed responded with \"" + string + "\"");
			Document doc = sb.build(new StringReader(string));

			jiras = getXPath(doc, "/rss/channel/item");

			for (Iterator<JiraIssue> iterator = jiras.iterator(); iterator.hasNext();) {
				JiraIssue jira = iterator.next();
				System.out.println(jira);
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jiras;
	}

	private List<JiraIssue> getXPath(Document doc, String xPath) throws JDOMException {
		XPath xpath = XPath.newInstance(xPath);
		List<Element> list = xpath.selectNodes(doc);
		return JiraIssue.buildJiras(list);
	}

}
