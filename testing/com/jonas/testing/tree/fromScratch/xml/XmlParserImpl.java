package com.jonas.testing.tree.fromScratch.xml;

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

public class XmlParserImpl extends HttpClient implements XmlParser {

   private Logger log = MyLogger.getLogger(XmlParserImpl.class);

   private XMLReader xmlReader;
   private String baseUrl;

   public XmlParserImpl(JiraSaxHandler nodeCounter) throws SAXException {
      super();
      xmlReader = XMLReaderFactory.createXMLReader();
      xmlReader.setContentHandler(nodeCounter);
      baseUrl = "http://10.155.38.105/jira";
   }

   public void parse() throws IOException, SAXException, JiraException {
      GetMethod method = null;
      try {
         String url = baseUrl + "/secure/IssueNavigator.jspa?view=rss&&fixfor=-2&pid=10070&reset=true&decorator=none&tempMax=2000";
         log.debug("calling " + url);
         login();

         MyStatusBar.getInstance().setMessage("Accessing Jiras...", false);
         method = new GetMethod(url);
         executeMethod(method);

         Reader reader2 = new InputStreamReader(method.getResponseBodyAsStream(), method.getResponseCharSet());

         xmlReader.parse(new InputSource(reader2));
      } finally {
         if (method != null)
            method.releaseConnection();
      }
   }

   public void login() throws IOException, HttpException, JiraException {
      MyStatusBar.getInstance().setMessage("Logging in to Jira...", false);
      PostMethod loginMethod = new PostMethod(baseUrl + "/login.jsp");
      loginMethod.addParameter("os_username", "soaptester");
      loginMethod.addParameter("os_password", "soaptester");
      executeMethod(loginMethod);
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
