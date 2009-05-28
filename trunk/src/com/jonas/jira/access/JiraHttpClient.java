package com.jonas.jira.access;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;

import com.jonas.common.logging.MyLogger;
import com.jonas.common.xml.JonasXpathEvaluator;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.jirastat.criteria.JiraCriteriaBuilder;
import com.jonas.jira.jirastat.criteria.JiraHttpCriteria;
import com.jonas.jira.utils.JiraBuilder;

public class JiraHttpClient extends HttpClient {
   public static final JiraHttpClient AOLBB = new JiraHttpClient(ClientConstants.JIRA_URL_AOLBB);

   public static final JiraHttpClient ATLASSIN = new JiraHttpClient(ClientConstants.JIRA_URL_ATLASSIN);

   private static final Logger log = MyLogger.getLogger(JiraHttpClient.class);
   private static final String MAX_RESULTS = "100000";

   private String baseUrl;

   private JiraHttpClient(String jiraUrl) {
      this.baseUrl = jiraUrl;
   }

   public void closeJira(String id, String resolution, JiraProject project) throws HttpException, IOException, JiraException {
      defaultExecute(id, resolution, project, project.getCloseAction());
   }

   public String getBaseUrl() {
      return baseUrl;
   }

   public JiraIssue getJira(String jira, JonasXpathEvaluator jonasXpathEvaluator, JiraBuilder jiraBuilder) throws HttpException, IOException, JDOMException, JiraException {
      String url = baseUrl + "/browse/" + jira + "?decorator=none&view=rss";

      List<JiraIssue> jiras = executeAndGetJiras(jonasXpathEvaluator, jiraBuilder, url);
      return jiras.get(0);
   }

   public List<JiraIssue> getJiras(JonasXpathEvaluator jonasXpathEvaluator, JiraBuilder jiraBuilder, JiraCriteriaBuilder criteriaBuilder) throws HttpException, IOException, JiraException, JDOMException {
      criteriaBuilder.setStandardFindCriterias(MAX_RESULTS).setBaseUrl(baseUrl);
      
      List<JiraIssue> jiras = executeAndGetJiras(jonasXpathEvaluator, jiraBuilder, criteriaBuilder.toString());
      return jiras;
   }



   public String getJiraUrl() {
      if (log.isDebugEnabled())
         log.debug("baseUrl: " + baseUrl);
      return baseUrl;
   }

   public void loginToJira() throws IOException, HttpException, JiraException {
      // TODO make this work as logging in, in the background as it does take
      // some time to login
      // TODO how long will the session stay as logged in? Will it require a
      // timeout before automatically logging in (in the
      // background)again?
      PostMethod loginMethod = new PostMethod(baseUrl + "/login.jsp");
      loginMethod.addParameter("os_username", "soaptester");
      loginMethod.addParameter("os_password", "soaptester");
      executeMethod(loginMethod);
      throwJiraExceptionIfRequired(loginMethod);
      log.debug("Logged into Jira!");
   }

   public void reOpenJira(String id, String resolution, JiraProject project) throws HttpException, IOException, JiraException {
      defaultExecute(id, resolution, project, project.getReOpenAction());
   }

   public void setJiraUrl(String jiraUrl) {
      this.baseUrl = jiraUrl;
   }

   public void updateSprint() {

   }

   private void addDefaultJiraWorkflowParams(PostMethod method, String id, String actionId) {
      method.addParameter("action", actionId); // name of input
      method.addParameter("id", id); // name of input
      method.addParameter("viewIssueKey", "viewIssueKey"); // name of input
   }

   private String debugMethod(HttpMethodBase method) {
      return "Jira Server responded: " + method.getStatusText() + "(" + method.getStatusCode() + ")";
   }

   private void defaultExecute(String id, String resolution, JiraProject project, String actionId) throws URIException, IOException, HttpException, JiraException {
      // TODO make this work as logging in, in the background as it does take
      // some time to login
      // TODO how long will the session stay as logged in? Will it require a
      // timeout before automatically logging in again?
      if (actionId == null) {
         throw new RuntimeException("Action \"" + actionId + "\" is null for " + project);
      }

      Map<String, String> parameters = new HashMap<String, String>();
      parameters.put("resolution", resolution);
      parameters.put("assignee", "");

      PostMethod method = getPostMethod(parameters);
      addDefaultJiraWorkflowParams(method, id, actionId);

      log.debug("executing URI: " + method.getURI() + " with action " + actionId + " on project " + project);
      executeMethod(method);
      throwJiraExceptionIfRequired(method);
   }

   private List<JiraIssue> executeAndGetJiras(JonasXpathEvaluator jonasXpathEvaluator, JiraBuilder jiraBuilder, String url) throws IOException, HttpException, JiraException, JDOMException {
      if (log.isDebugEnabled())
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

   private PostMethod getPostMethod(Map<String, String> parameters) {
      PostMethod method = new PostMethod(baseUrl + "/secure/CommentAssignIssue.jspa"); // form
      // action
      // //
      // see
      // form
      // method
      // here
      // as
      // well!!
      for (String parameter : parameters.keySet()) {
         method.addParameter(parameter, parameters.get(parameter)); // name of
         // input
      }
      return method;
   }

   private void throwJiraExceptionIfRequired(HttpMethodBase method) throws JiraException {
      // FIXME map jira exception to a jira action so that we know in the code
      // what is going on!
      if (method.getStatusCode() != 200) {
         log.debug("Throwing jira exception!");
         throw new JiraException(debugMethod(method));
      }
   }

   protected List<JiraIssue> buildJirasFromXML(String string, JiraBuilder jiraBuilder, JonasXpathEvaluator jonasXpathEvaluator) throws IOException, JDOMException {
      List<Element> jiras;
      try {
         jiras = jonasXpathEvaluator.getXpathElements(string);
      } catch (JDOMException e) {
         log.error(string);
         throw e;
      }

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

}
