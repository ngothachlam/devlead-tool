package com.jonas.agile.devleadtool.component.tree.xml;

import java.io.IOException;
import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.controller.listener.JiraParseListener;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;
import com.jonas.jira.access.JiraException;

public class DnDTreeBuilder {

   private Logger log = MyLogger.getLogger(DnDTreeBuilder.class);

   private final XmlParser parser;

   public DnDTreeBuilder(XmlParser parser) {
      super();
      this.parser = parser;
   }

   public void buildTree(final String sprint, final JiraProject project) {
      try {
         parser.parse(project, sprint);
      } catch (IOException e) {
         e.printStackTrace();
      } catch (SAXException e) {
         e.printStackTrace();
      } catch (JiraException e) {
         e.printStackTrace();
      }
   }

   public void addParseListener(JiraParseListener jiraParseListener) {
      parser.addParseListener(jiraParseListener);
   }

   public void login() throws HttpException, IOException, JiraException {
      parser.login();
   }

}
