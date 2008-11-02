package com.jonas.testing.tree.fromScratch.tree.xml;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import com.jonas.common.logging.MyLogger;

public class JiraSaxHandler extends DefaultHandler {

   private static final String SUMMARY = "summary";
   private static final String RESOLUTION = "resolution";
   private static final String STATUS = "status";
   private static final String CUSTOMFIELDNAME = "customfieldname";
   private static final Object CUSTOMFIELDVALUE = "customfieldvalue";
   private static final String FIXVERSION = "fixVersion";
   private static final String ITEM = "item";
   private static final String KEY = "key";
   private static final String SPRINT = "Sprint";

   private JiraDTO jira;
   private List<JiraParseListener> jiraParseListeners = new ArrayList<JiraParseListener>();
   private int nodeCount;
   private StringBuffer sb = new StringBuffer();
   private boolean sprintNextValue = false;
   private Logger log = MyLogger.getLogger(JiraSaxHandler.class);
   private String element;

   public void addJiraParseListener(JiraParseListener jiraParseListener) {
      jiraParseListeners.add(jiraParseListener);
   }

   public void characters(char ch[], int start, int length) {
      for (int i = start; i < start + length; i++) {
         switch (ch[i]) {
         case '\n':
         case '\r':
         case '\t':
            break;
         default:
            sb.append(ch[i]);
            break;
         }
      }
   }

   @Override
   public void endDocument() throws SAXException {
      notifyParsingFinished();
   }

   public void endElement(String uri, String name, String qName) {
      String value = sb.toString().trim();
      if (KEY.equals(qName)) {
         log.debug("JiraKey: " + value);
         jira.setKey(value);
      } else if (FIXVERSION.equals(qName)) {
         log.debug("Adding fixVersion: " + value + " to jira " + jira.getKey());
         jira.addFixVersion(value);
      } else if (SUMMARY.equals(qName)) {
         jira.setSummary(value);
      } else if (STATUS.equals(qName)) {
         jira.setStatus(value);
      } else if (RESOLUTION.equals(qName)) {
         jira.setResolution(value);
      } else if (SPRINT.equals(value) && CUSTOMFIELDNAME.equals(element)) {
         sprintNextValue = true;
      } else if (CUSTOMFIELDVALUE.equals(qName) && sprintNextValue) {
         sprintNextValue = false;
         jira.setSprint(value);
      } else if (ITEM.equals(qName)) {
         notifyJiraParsed(jira);
      }
      sb.delete(0, sb.length());
   }

   private void notifyJiraParsed(JiraDTO jira) {
      for (JiraParseListener listener : jiraParseListeners) {
         listener.notifyParsed(jira);
      }
   }

   private void notifyParsingFinished() {
      for (JiraParseListener listener : jiraParseListeners) {
         listener.notifyParsingFinished();
      }
   }

   private void notifyParsingStarted() {
      for (JiraParseListener listener : jiraParseListeners) {
         listener.notifyParsingStarted();
      }
   }

   public void startDocument() throws SAXException {
      notifyParsingStarted();
      nodeCount = 0;
   }

   public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
      if (ITEM.equals(qName)) {
         jira = new JiraDTO();
      } else if (KEY.equals(qName)) {
         jira.setId(atts.getValue("id"));
      }
      element = qName;
      nodeCount++;
   }

}