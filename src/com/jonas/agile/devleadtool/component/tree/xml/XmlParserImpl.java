package com.jonas.agile.devleadtool.component.tree.xml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.access.JiraException;

public class XmlParserImpl implements XmlParser {

   public int getMaxResults() {
      return maxResults;
   }

   private Logger log = MyLogger.getLogger(XmlParserImpl.class);

   private XMLReader xmlReader;
   private String baseUrl;

   private final int maxResults;

   public XmlParserImpl(JiraSaxHandler nodeCounter, int maxResults) throws SAXException {
      super();
      this.maxResults = maxResults;
      xmlReader = XMLReaderFactory.createXMLReader();
      xmlReader.setContentHandler(nodeCounter);
      baseUrl = "http://10.155.38.105/jira";
   }

   public void parse(String sprint) throws IOException, SAXException, JiraException {
      GetMethod method = null;
      try {
         HttpClient httpClient = new HttpClient();
         String url = baseUrl + "/secure/IssueNavigator.jspa?view=rss&fixfor=-2&pid=10070&reset=true&decorator=none&tempMax=" + maxResults;
         if(sprint != null)
            url = url +"&" + "customfield_10282=" + sprint;
         log.debug("calling " + url);
         login(httpClient);

         // fixfor=-2 @ Fix For: all unreleased versions
         // pid=10070 @ Project: LLU Systems Provisioning

         // fixfor=-2
         // customfield_10282=12.1 @ Sprint: 12.1
         // pid=10070

         MyStatusBar.getInstance().setMessage("Accessing Jiras...", false);
         method = new GetMethod(url);
         int result = httpClient.executeMethod(method);

         Reader reader2 = new InputStreamReader(method.getResponseBodyAsStream(), method.getResponseCharSet());

         xmlReader.parse(new InputSource(reader2));
      } finally {
         if (method != null)
            method.releaseConnection();
      }
   }

   private void login(HttpClient httpClient) throws IOException, HttpException, JiraException {
      MyStatusBar.getInstance().setMessage("Logging in to Jira...", false);
      PostMethod loginMethod = new PostMethod(baseUrl + "/login.jsp");
      loginMethod.addParameter("os_username", "soaptester");
      loginMethod.addParameter("os_password", "soaptester");
      httpClient.executeMethod(loginMethod);
      throwJiraExceptionIfRequired(loginMethod);
      log.debug("Logging onto Jira Done!");
   }

   private void throwJiraExceptionIfRequired(HttpMethodBase method) throws JiraException {
      // FIXME map jira exception to a jira action so that we know in the code what is going on!
      if (method.getStatusCode() != 200) {
         log.debug("Throwing jira exception!");
         throw new JiraException(debugMethod(method));
      }
   }

   private String debugMethod(HttpMethodBase method) {
      StringBuffer sb = new StringBuffer("Jira Server responded: ");
      sb.append(method.getStatusText());
      sb.append(" (");
      sb.append(method.getStatusCode());
      sb.append(")");
      return sb.toString();
   }
}
