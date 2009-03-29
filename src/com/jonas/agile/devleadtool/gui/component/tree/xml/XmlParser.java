package com.jonas.agile.devleadtool.gui.component.tree.xml;

import java.io.IOException;
import org.apache.commons.httpclient.HttpException;
import org.xml.sax.SAXException;
import com.jonas.jira.JiraProject;
import com.jonas.jira.access.JiraException;

public interface XmlParser {

   public void parse(JiraProject project, String sprint) throws IOException, SAXException, JiraException;

   public void addParseListener(JiraParseListener jiraParseListener);

   public void login() throws IOException, HttpException, JiraException;

}
