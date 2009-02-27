package com.jonas.jira.access;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.xml.JonasXpathEvaluator;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.JiraFilter;
import com.jonas.jira.utils.JiraBuilder;

public class JiraHttpClient extends HttpClient {
   private static final Logger log = MyLogger.getLogger(JiraHttpClient.class);

   private static final String MAX_RESULTS = "100000";

   private String baseUrl;

   public JiraHttpClient(String jiraUrl) {
      this.baseUrl = jiraUrl;
   }

   public List<JiraIssue> getJiras(JiraVersion fixVersion, JonasXpathEvaluator jonasXpathEvaluator, JiraBuilder jiraBuilder) throws HttpException, IOException,
         JDOMException, JiraException {
      log.debug("getting Jiras");
      String url = baseUrl + "/secure/IssueNavigator.jspa?view=rss&&fixfor=" + fixVersion.getId() + "&pid=" + fixVersion.getProject().getId()
            + "&sorter/field=issuekey&sorter/order=DESC&tempMax=" + MAX_RESULTS + "&reset=true&decorator=none";
      log.debug("calling " + url);
      GetMethod method = new GetMethod(url);
      executeMethod(method);
      byte[] responseAsBytes = method.getResponseBody();
      method.releaseConnection();

      throwJiraExceptionIfRequired(method);

      String string = new String(responseAsBytes);
      List<JiraIssue> jiras = buildJirasFromXML(string, jiraBuilder, jonasXpathEvaluator);

      if (log.isDebugEnabled()) {
         for (Iterator<JiraIssue> iterator = jiras.iterator(); iterator.hasNext();) {
            JiraIssue jira = iterator.next();
            log.debug(jira);
         }
      }
      return jiras;
   }

   public JiraIssue getJira(String jira, JonasXpathEvaluator jonasXpathEvaluator, JiraBuilder jiraBuilder) throws HttpException, IOException,
         JDOMException, JiraException {
      log.debug("getting Jira");

      // http://10.155.38.105/jira/browse/LLUDEVSUP-522?decorator=none&view=rss
      String url = baseUrl + "/browse/" + jira + "?decorator=none&view=rss";
      log.debug("calling " + url);
      GetMethod method = new GetMethod(url);
      executeMethod(method);
      byte[] responseAsBytes = method.getResponseBody();
      method.releaseConnection();

      throwJiraExceptionIfRequired(method);

      String string = new String(responseAsBytes);
      List<JiraIssue> jiras = buildJirasFromXML(string, jiraBuilder, jonasXpathEvaluator);

      JiraIssue jiraIssue = jiras.get(0);
      log.debug(jiraIssue);
      
      return jiraIssue;
   }

   public String getJiraUrl() {
      log.debug("baseUrl: " + baseUrl);
      return baseUrl;
   }

   public void loginToJira() throws IOException, HttpException, JiraException {
      // TODO make this work as logging in, in the background as it does take some time to login
      // TODO how long will the session stay as logged in? Will it require a timeout before automatically logging in (in the
      // background)again?
      log.debug("Logging onto Jira");
      PostMethod loginMethod = new PostMethod(baseUrl + "/login.jsp");
      loginMethod.addParameter("os_username", "soaptester");
      loginMethod.addParameter("os_password", "soaptester");
      executeMethod(loginMethod);
      throwJiraExceptionIfRequired(loginMethod);
      log.debug("Logging onto Jira Done!");
   }

   public void setJiraUrl(String jiraUrl) {
      this.baseUrl = jiraUrl;
   }

   private String debugMethod(HttpMethodBase method) {
      return "Jira Server responded: " + method.getStatusText() + "(" + method.getStatusCode() + ")";
   }

   private void throwJiraExceptionIfRequired(HttpMethodBase method) throws JiraException {
      //FIXME map jira exception to a jira action so that we know in the code what is going on!
      if (method.getStatusCode() != 200) {
         log.debug("Throwing jira exception!");
         throw new JiraException(debugMethod(method));
      }
   }

   protected List<JiraIssue> buildJirasFromXML(String string, JiraBuilder jiraBuilder, JonasXpathEvaluator jonasXpathEvaluator) throws JDOMException,
         IOException {
//      log.debug("RSS feed responded with \"" + string + "\"");
      JonasXpathEvaluator evaluator = jonasXpathEvaluator;
      List<Element> jiras = evaluator.getXpathElements(string);

      return jiraBuilder.buildJiras(jiras);
   }

   interface JiraAccessAction {
      Object accessJiraAndReturn();
   }

   class JiraTokenCommand {

      private final JiraAccessAction action;

      private final Logger LOGGER = MyLogger.getLogger(JiraHttpClient.class);

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

   public List<JiraIssue> getJirasFromFilter(JiraFilter devsupportPrioFilter, JonasXpathEvaluator jonasXpathEvaluator, JiraBuilder jiraBuilder) throws HttpException, IOException, JiraException, JDOMException {
      log.debug("getting Jiras");
      String url = baseUrl + devsupportPrioFilter.getUrl();
      log.debug("calling " + url);
      GetMethod method = new GetMethod(url);
      executeMethod(method);
      byte[] responseAsBytes = method.getResponseBody();
      method.releaseConnection();

      throwJiraExceptionIfRequired(method);

      String string = new String(responseAsBytes);
      List<JiraIssue> jiras = buildJirasFromXML(string, jiraBuilder, jonasXpathEvaluator);

      if (log.isDebugEnabled()) {
         for (Iterator<JiraIssue> iterator = jiras.iterator(); iterator.hasNext();) {
            JiraIssue jira = iterator.next();
            log.debug(jira);
         }
      }
      return jiras;
   }
   
   public void updateSprint(){
      
   }

   public String getBaseUrl() {
      return baseUrl;
   }

}
