package com.jonas.testing.tree.fromScratch.xml;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import com.jonas.common.logging.MyLogger;

public class SaxHandler extends DefaultHandler {

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
   private Logger log = MyLogger.getLogger(SaxHandler.class);
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
      System.out.println("\tValue : " + value);
      if ("".equals(uri))
         System.out.println("End element: " + qName);
      else
         System.out.println("End element: {" + uri + "}" + name);
      if (KEY.equals(qName)) {
         jira.setKey(value);
      } else if (FIXVERSION.equals(qName)) {
         jira.setFixVersion(value);
      } else if (SPRINT.equals(value) && CUSTOMFIELDNAME.equals(element)) {
         log.debug("** SPRINT **");
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
      log.debug("Parsing Jira " + jira.getKey());
      for (JiraParseListener listener : jiraParseListeners) {
         listener.notifyParsed(jira);
      }
   }

   private void notifyParsingFinished() {
      log.debug("Parsing Finished!");
      for (JiraParseListener listener : jiraParseListeners) {
         listener.notifyParsingFinished();
      }
   }

   private void notifyParsingStarted() {
      log.debug("Parsing Started!");
      for (JiraParseListener listener : jiraParseListeners) {
         listener.notifyParsingStarted();
      }
   }

   public void startDocument() throws SAXException {
      notifyParsingStarted();
      nodeCount = 0;
   }

   public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
      if ("".equals(uri))
         System.out.println("Start element: " + qName);
      else
         System.out.println("Start element: {" + uri + "}" + name);
      for (int i = 0; i < atts.getLength(); i++) {
         System.out.println("\tAtt : " + atts.getLocalName(i) + " - " + atts.getValue(i));
      }

      if (ITEM.equals(qName)) {
         log.debug("creating Jira!");
         jira = new JiraDTO();
      }
      element = qName;
      nodeCount++;
   }

}