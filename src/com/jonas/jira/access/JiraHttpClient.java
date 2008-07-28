package com.jonas.jira.access;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.jonas.jira.Jira;

public class JiraHttpClient extends HttpClient {
	private final String baseUrl;

	public JiraHttpClient() {
		this.baseUrl = "http://10.155.38.105";
	}

	public void loginToJira() throws IOException, HttpException {
		PostMethod loginMethod = new PostMethod(baseUrl + "/jira/login.jsp");
		loginMethod.addParameter("os_username", "soaptester");
		loginMethod.addParameter("os_password", "soaptester");
		executeMethod(loginMethod);
	}

	public List<Jira> getJiras() {
		// String url = "/jira/secure/IssueNavigator.jspa?view=rss&&fixfor=11382&pid=10070&reset=true&decorator=none";
		String url = "/jira/secure/IssueNavigator.jspa?view=rss&&fixfor=11382&pid=10070&sorter/field=issuekey&sorter/order=DESC&reset=true&decorator=none";
		GetMethod method = new GetMethod(baseUrl + url);
		List<Jira> jiras = null;
		try {
			executeMethod(method);
			byte[] responseAsBytes = method.getResponseBody();
			method.releaseConnection();

			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(new StringReader(new String(responseAsBytes)));

			jiras = getXPath(doc, "/rss/channel/item");

			for (Iterator<Jira> iterator = jiras.iterator(); iterator.hasNext();) {
				Jira jira = iterator.next();
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

	private List<Jira> getXPath(Document doc, String xPath) throws JDOMException {
		XPath xpath = XPath.newInstance(xPath);
		List<Element> list = xpath.selectNodes(doc);
		return Jira.buildJiras(list);
	}

}
