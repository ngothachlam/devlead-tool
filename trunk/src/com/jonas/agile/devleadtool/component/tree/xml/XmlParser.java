package com.jonas.agile.devleadtool.component.tree.xml;

import java.io.IOException;
import org.xml.sax.SAXException;
import com.jonas.jira.access.JiraException;

public interface XmlParser {

   public void parse(String sprint) throws IOException, SAXException, JiraException;

}
