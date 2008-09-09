package com.jonas.jira.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Element;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.xml.JonasXpathEvaluator;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraResolution;
import com.jonas.jira.JiraStatus;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraType;

public class JiraBuilder {

   private static JiraBuilder instance = new JiraBuilder();
   private static final List<XPathImplementor> jiraXpathActions = new ArrayList<XPathImplementor>();
   private static final Logger log = MyLogger.getLogger(JiraBuilder.class);

   static {
      String xPath = "/item/customfields/customfield[@id='customfield_10160']/customfieldvalues/customfieldvalue";
      XpathAction xpathAction = new XpathAction() {
         public void XPathValueFound(String xpathValue, JiraIssue jira) {
            jira.setBuildNo(xpathValue);
         }
      };
      String xPath2 = "/item/timeoriginalestimate/@seconds";
      XpathAction xpathAction2 = new XpathAction() {
         public void XPathValueFound(String xpathValue, JiraIssue jira) {
            if (xpathValue != null && xpathValue.trim().length() > 0) {
               int xpathResultAsInt = new Integer(xpathValue).intValue();
               jira.setEstimate(JiraBuilder.getSecondsAsDays(xpathResultAsInt));
            }
         }
      };
      jiraXpathActions.add(new XPathImplementor(xPath, xpathAction));
      jiraXpathActions.add(new XPathImplementor(xPath2, xpathAction2));
   }

   public static float getSecondsAsDays(int seconds) {
      float secondsConverter = 60 * 60 * 8;
      return seconds / secondsConverter;
   }

   JiraBuilder() {
   }

   public static JiraBuilder getInstance() {
      return instance;
   }

   public JiraIssue buildJira(Element e) {
      return new JiraIssue(get(e, "key"), get(e, "summary"), get(e, "status"), get(e, "resolution"), get(e, "type"));
   }

   public JiraIssue buildJira(Element e, List<JiraVersion> fixVersions) {
      JiraIssue jira = buildJira(e);
      log.debug("building jira " + jira.getKey());
      jira.addFixVersions(fixVersions.get(0));
      if (fixVersions.size() > 1) {
         log.error("Cannot handle more than one fix version at the moment for " + jira.getKey());
      }
      return jira;
   }

   public JiraIssue buildJira(RemoteIssue remoteJira, JiraProject project) {
      JiraResolution resolution = JiraResolution.getResolution(remoteJira.getResolution());
      JiraType type = JiraType.getResolution(remoteJira.getType());
      JiraStatus status = JiraStatus.getJiraStatusById(remoteJira.getStatus());
      String statusName = status != null ? status.getName() : null;
      String resolutionName = resolution != null ? resolution.getName() : null;
      String typeName = type != null ? type.getName() : null;
      JiraIssue jira = new JiraIssue(remoteJira.getKey(), remoteJira.getSummary(), statusName, resolutionName, typeName);
      RemoteVersion[] tempFixVersions = remoteJira.getFixVersions();
      for (int i = 0; i < tempFixVersions.length; i++) {
         RemoteVersion remoteVersion = tempFixVersions[i];
         JiraVersion fixVers = JiraVersion.getVersionById(remoteVersion.getId());
         if (fixVers == null) {
            fixVers = buildJiraVersion(remoteVersion, project);
         }
         jira.addFixVersions(fixVers);
         if (i > 1) {
            log.error("Cannot handle more than one fix version at the moment for " + jira.getKey());
         }
      }
      return jira;
   }

   private String getJiraFieldIfNotNull(JiraType type) {
      return type != null ? type.getName() : null;
   }

   public List<JiraIssue> buildJiras(List<Element> list) {
      List<JiraIssue> jiras = new ArrayList<JiraIssue>();
      log.debug("got " + list.size() + " jiras");
      for (Iterator<Element> iterator = list.iterator(); iterator.hasNext();) {
         Element e = iterator.next();
         List<JiraVersion> versions = buildJiraVersion(e);
         JiraIssue jiraIssue = buildJira(e, versions);
         log.debug("built jira " + jiraIssue.getKey());

         for (XPathImplementor xPathImplementor : jiraXpathActions) {
            log.debug("executing xpathImplementor on Jira " + jiraIssue.getKey());
            xPathImplementor.execute(e, jiraIssue);
         }

         jiras.add(jiraIssue);

      }
      return jiras;
   }

   public JiraVersion buildJiraVersion(RemoteVersion remoteVersion, JiraProject jiraProject) {
      return new JiraVersion(remoteVersion.getId(), jiraProject, remoteVersion.getName(), remoteVersion.isArchived());
   }

   public String get(Element element, String string) {
      return element.getChildText(string);
   }

   @SuppressWarnings("unchecked")
   private List<JiraVersion> buildJiraVersion(Element e) {
      List<Element> fixVersionStrings = e.getChildren("fixVersion");
      List<JiraVersion> versions = new ArrayList<JiraVersion>();
      for (Iterator<Element> iterator2 = fixVersionStrings.iterator(); iterator2.hasNext();) {
         Element element = iterator2.next();
         JiraVersion versionByName = JiraVersion.getVersionByName(element.getText());
         if (versionByName == null) {
            // FIXME use id to get JiraVersion!!
            JiraProject projectByKey = JiraProject.getProjectByKey(PlannerHelper.getProjectKey(get(e, "key")));
            try {
               JiraClient jiraClient = projectByKey.getJiraClient();
               jiraClient.getFixVersionsFromProject(projectByKey, false);
            } catch (Exception e1) {
               e1.printStackTrace();
            }
         }
         versionByName = JiraVersion.getVersionByName(element.getText());
         JiraIssue.log.debug("Getting version byName: \"" + element.getText() + "\" is \"" + versionByName + "\"");
         versions.add(versionByName);
      }
      return versions;
   }
}


interface XpathAction {
   public void XPathValueFound(String xpathValue, JiraIssue jira);
}


class XPathImplementor {

   private final XpathAction xpathAction;
   private JonasXpathEvaluator xpathEvaluator;

   public XPathImplementor(String xpathExpression, XpathAction xpathAction) {
      this.xpathAction = xpathAction;
      xpathEvaluator = new JonasXpathEvaluator(xpathExpression);
   }

   public void execute(Element element, JiraIssue jira) {
      xpathAction.XPathValueFound(xpathEvaluator.getElementText(element), jira);
   }
}
