package com.jonas.jira.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Element;
import com.atlassian.jira.rpc.soap.beans.RemoteCustomFieldValue;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.common.CalculatorHelper;
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

   private static final String CUSTOMFIELD_LLULISTPRIO = "customfield_10241";
   private static final String CUSTOMFIELD_BUILDNO = "customfield_10160";
   private static final String CUSTOMFIELD_LLUSPRINT = "customfield_10282";
   private static JiraBuilder instance = new JiraBuilder();
   private static final List<XPathImplementor> jiraXpathActions = new ArrayList<XPathImplementor>();
   private static final Logger log = MyLogger.getLogger(JiraBuilder.class);

   static {
      addXpathAction("/item/customfields/customfield[@id='" + CUSTOMFIELD_BUILDNO + "']/customfieldvalues/customfieldvalue", new XpathAction() {
         public void XPathValueFound(String xpathValue, JiraIssue jira) {
            jira.setBuildNo(xpathValue);
         }
      });
      addXpathAction("/item/customfields/customfield[@id='" + CUSTOMFIELD_LLULISTPRIO + "']/customfieldvalues/customfieldvalue", new XpathAction() {
         public void XPathValueFound(String xpathValue, JiraIssue jira) {
            jira.setLLUListPriority(getStringAsIntIfNumeric(xpathValue));
         }
      });
      addXpathAction("/item/timeoriginalestimate/@seconds", new XpathAction() {
         public void XPathValueFound(String xpathValue, JiraIssue jira) {
            if (xpathValue != null && xpathValue.trim().length() > 0) {
               jira.setEstimate(CalculatorHelper.getSecondsAsDays(xpathValue));
            }
         }
      });
      addXpathAction("/item/timespent/@seconds", new XpathAction() {
         public void XPathValueFound(String xpathValue, JiraIssue jira) {
            if (xpathValue != null && xpathValue.trim().length() > 0) {
               jira.setSpent(CalculatorHelper.getSecondsAsDays(xpathValue));
            }
         }
      });
      addXpathAction("/item/customfields/customfield[@id='" + CUSTOMFIELD_LLUSPRINT + "']/customfieldvalues/customfieldvalue", new XpathAction() {
         public void XPathValueFound(String xpathValue, JiraIssue jira) {
            jira.setSprint(xpathValue);
         }
      });
   }

   private static void addXpathAction(String xPath, XpathAction action) {
      jiraXpathActions.add(new XPathImplementor(xPath, action));
   }

   JiraBuilder() {
   }

   public static JiraBuilder getInstance() {
      return instance;
   }

   JiraIssue buildJira(Element e) {
      JiraIssue jiraIssue = new JiraIssue(get(e, "key"), get(e, "summary"), get(e, "status"), get(e, "resolution"), get(e, "type"));
      for (XPathImplementor xPathImplementor : jiraXpathActions) {
         log.debug("executing xpathImplementor on Jira " + jiraIssue.getKey());
         xPathImplementor.execute(e, jiraIssue);
      }
      return jiraIssue;
   }

   public JiraIssue buildJira(Element e, List<JiraVersion> fixVersions) {
      JiraIssue jira = buildJira(e);
      log.debug("building jira " + jira.getKey());
      // if (fixVersions.size() == 1) {
      for (JiraVersion jiraVersion : fixVersions) {
         jira.addFixVersions(jiraVersion);
      }
      // } else if (fixVersions.size() > 1) {
      // jira.addFixVersions(fixVersions.get(0));
      // log.warn("Cannot handle more than one fix version at the moment for " + jira.getKey());
      // }
      return jira;
   }

   public JiraIssue buildJira(RemoteIssue remoteJira, JiraProject project) {
      JiraResolution resolution = JiraResolution.getResolution(remoteJira.getResolution());
      JiraType type = JiraType.getResolution(remoteJira.getType());
      JiraStatus status = JiraStatus.getJiraStatusById(remoteJira.getStatus());
      String statusName = status != null ? status.getName() : null;
      String resolutionName = resolution != null ? resolution.getName() : null;
      String typeName = type != null ? type.getName() : null;
      RemoteCustomFieldValue[] customFieldValues = remoteJira.getCustomFieldValues();
      String buildNo = getCustomFieldValue(customFieldValues, CUSTOMFIELD_BUILDNO);
      String sprint = getCustomFieldValue(customFieldValues, CUSTOMFIELD_LLUSPRINT);
      int listPrio = getStringAsIntIfNumeric(getCustomFieldValue(customFieldValues, CUSTOMFIELD_LLULISTPRIO));
      // fixme - doesn't work with estimate at the moment.
      JiraIssue jira = new JiraIssue(remoteJira.getKey(), remoteJira.getSummary(), statusName, resolutionName, typeName, buildNo, "", listPrio, sprint);
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

   static int getStringAsIntIfNumeric(String string) {
      int intValue = -1;
      if (string != null && !string.isEmpty())
         try {
            intValue = (int) Float.parseFloat(string);
         } catch (NumberFormatException e) {
            log.debug("String \"" + string + "\" cannot be transformed to an int!");
         }
      return intValue;
   }

   private String getCustomFieldValue(RemoteCustomFieldValue[] customFieldValues, String anObject) {
      log.debug("Trying to find custom field value: " + anObject);
      for (RemoteCustomFieldValue remoteCustomFieldValue : customFieldValues) {
         String customfieldId = remoteCustomFieldValue.getCustomfieldId();
         if (log.isDebugEnabled()) {
            log.debug("\tfound: " + customfieldId);
            String[] values = remoteCustomFieldValue.getValues();
            for (int i = 0; i < values.length; i++) {
               log.debug("\t\tvalue[" + i + "]: " + values[i]);
            }
         }
         if (customfieldId.equals(anObject))
            return remoteCustomFieldValue.getValues()[0];
      }
      return "";
   }

   public List<JiraIssue> buildJiras(List<Element> list) {
      List<JiraIssue> jiras = new ArrayList<JiraIssue>();
      log.debug("got " + list.size() + " jiras");
      for (Iterator<Element> iterator = list.iterator(); iterator.hasNext();) {
         Element e = iterator.next();
         List<JiraVersion> versions = buildJiraVersion(e);
         JiraIssue jiraIssue = buildJira(e, versions);
         log.debug("built jira " + jiraIssue.getKey());
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
