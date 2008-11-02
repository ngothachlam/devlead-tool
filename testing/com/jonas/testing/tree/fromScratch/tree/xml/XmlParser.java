package com.jonas.testing.tree.fromScratch.tree.xml;

import java.io.IOException;
import org.xml.sax.SAXException;
import com.jonas.jira.access.JiraException;

public interface XmlParser {

   public void parse() throws IOException, SAXException, JiraException;

}
