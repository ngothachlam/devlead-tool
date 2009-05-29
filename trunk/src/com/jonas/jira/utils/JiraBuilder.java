package com.jonas.jira.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.common.CalculatorHelper;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.xml.JonasXpathEvaluator;
import com.jonas.jira.JiraCustomFields;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraClient;

public class JiraBuilder {

   private static JiraBuilder instance = new JiraBuilder();
   private static final List<XPathImplementor> jiraXpathActions = new ArrayList<XPathImplementor>();
   private static final Logger log = MyLogger.getLogger(JiraBuilder.class);

   static {
      addXpathAction("/item/customfields/customfield[@id='" + JiraCustomFields.LLUBuildNo + "']/customfieldvalues/customfieldvalue", new XpathAction() {
         public void XPathValueFound(String xpathValue, JiraIssue jira) {
            jira.setBuildNo(xpathValue);
         }
      });
      addXpathAction("/item/customfields/customfield[@id='" + JiraCustomFields.LLUListPrio + "']/customfieldvalues/customfieldvalue", new XpathAction() {
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
      addXpathAction("/item/customfields/customfield[@id='" + JiraCustomFields.LLUSprint + "']/customfieldvalues/customfieldvalue", new XpathAction() {
         public void XPathValueFound(String xpathValue, JiraIssue jira) {
            jira.setSprint(xpathValue);
         }
      });
      addXpathAction("/item/customfields/customfield[@id='" + JiraCustomFields.LLUProject + "']/customfieldvalues/customfieldvalue", new XpathAction() {
         public void XPathValueFound(String xpathValue, JiraIssue jira) {
            jira.setProject(xpathValue);
         }
      });
      addXpathAction("/item/customfields/customfield[@id='" + JiraCustomFields.LLUDeliveryDate + "']/customfieldvalues/customfieldvalue", new XpathAction() {
         public void XPathValueFound(String xpathValue, JiraIssue jira) {
            try {
               jira.setDeliveryDate(formatToDate(xpathValue));
            } catch (ParseException e) {
               e.printStackTrace();
            }
         }
      });
      addXpathAction("/item/customfields/customfield[@id='" + JiraCustomFields.LLUEnvironment + "']/customfieldvalues/customfieldvalue", new XpathAction() {
         public void XPathValueFound(String xpathValue, JiraIssue jira) {
            jira.setEnvironment(xpathValue);
         }
      });
      addXpathAction("/item/customfields/customfield[@id='" + JiraCustomFields.LLUOwner + "']/customfieldvalues/customfieldvalue", new XpathAction() {
         public void XPathValueFound(String xpathValue, JiraIssue jira) {
            jira.setOwner(xpathValue);
         }
      });
   }
   private static SimpleDateFormat JiraDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
   private static SimpleDateFormat OutputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

   protected static String formatToDate(String string) throws ParseException {
      // Thu, 29 Jan 2009 00:00:00 +0000 (GMT)
      if (string == null || string.trim().length() == 0)
         return "";
      Date date = JiraDateFormat.parse(string);
      return OutputDateFormat.format(date);
   }

   private static void addXpathAction(String xPath, XpathAction action) {
      jiraXpathActions.add(new XPathImplementor(action, new JonasXpathEvaluator(xPath, new XMLOutputter())));
   }

   JiraBuilder() {
   }

   public static JiraBuilder getInstance() {
      return instance;
   }

   JiraIssue buildJira(Element e) {
      JiraIssue jiraIssue = new JiraIssue(get(e, "key"), get(e, "summary"), get(e, "status"), get(e, "resolution"), get(e, "type"), get(e, "created"));
      for (XPathImplementor xPathImplementor : jiraXpathActions) {
         if (log.isDebugEnabled())
            log.debug("executing xpathImplementor on Jira " + jiraIssue.getKey());
         xPathImplementor.execute(e, jiraIssue);
      }
      return jiraIssue;
   }

   public JiraIssue buildJira(Element e, List<JiraVersion> fixVersions) {
      JiraIssue jira = buildJira(e);
      if (log.isDebugEnabled())
         log.debug("building jira " + jira.getKey());
      for (JiraVersion jiraVersion : fixVersions) {
         jira.addFixVersions(jiraVersion);
      }
      return jira;
   }

   static int getStringAsIntIfNumeric(String string) {
      int intValue = -1;
      if (string != null && !string.isEmpty())
         try {
            intValue = (int) Float.parseFloat(string);
         } catch (NumberFormatException e) {
            if (log.isDebugEnabled())
               log.debug("String \"" + string + "\" cannot be transformed to an int!");
         }
      return intValue;
   }

   public List<JiraIssue> buildJiras(List<Element> list) {
      List<JiraIssue> jiras = new ArrayList<JiraIssue>();
      if (log.isDebugEnabled())
         log.debug("got " + list.size() + " jiras");
      for (Iterator<Element> iterator = list.iterator(); iterator.hasNext();) {
         Element e = iterator.next();
         List<JiraVersion> versions = buildJiraVersion(e);
         JiraIssue jiraIssue = buildJira(e, versions);
         if (log.isDebugEnabled())
            log.debug("built jira " + jiraIssue.getKey());
         jiras.add(jiraIssue);

      }
      return jiras;
   }

   public void cachedJiraVersion(RemoteVersion remoteVersion, JiraProject jiraProject) {
      new JiraVersion(remoteVersion.getId(), jiraProject, remoteVersion.getName(), remoteVersion.isArchived()).cache();
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
         if (log.isDebugEnabled())
            log.debug("Getting version byName: \"" + element.getText() + "\" is \"" + versionByName + "\"");
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

   public XPathImplementor(XpathAction xpathAction, JonasXpathEvaluator jonasXpathEvaluator) {
      this.xpathAction = xpathAction;
      xpathEvaluator = jonasXpathEvaluator;
   }

   public void execute(Element element, JiraIssue jira) {
      xpathAction.XPathValueFound(xpathEvaluator.getElementText(element), jira);
   }
}
