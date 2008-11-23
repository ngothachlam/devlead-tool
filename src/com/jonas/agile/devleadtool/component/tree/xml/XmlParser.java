package com.jonas.agile.devleadtool.component.tree.xml;

import java.io.IOException;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.component.listener.SprintParseListener;
import com.jonas.jira.JiraProject;
import com.jonas.jira.access.JiraException;

public interface XmlParser {

   public void parse(JiraProject project, String sprint) throws IOException, SAXException, JiraException;

   public void addParseListener(SprintParseListener jiraParseListener);

}
