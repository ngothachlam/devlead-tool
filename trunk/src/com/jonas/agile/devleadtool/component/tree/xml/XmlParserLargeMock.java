package com.jonas.agile.devleadtool.component.tree.xml;

import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import com.jonas.agile.devleadtool.component.listener.SprintParseListener;
import com.jonas.jira.JiraProject;

public class XmlParserLargeMock implements XmlParser {

   private XMLReader reader;

   public XmlParserLargeMock(JiraSaxHandler nodeCounter) throws SAXException {
      super();
      reader = XMLReaderFactory.createXMLReader();
      reader.setContentHandler(nodeCounter);
   }

   public void parse(String sprint, JiraProject project) throws IOException, SAXException {
      reader.parse("test-data/ListOfSprintJirasMany.xml");
   }

   @Override
   public void addParseListener(SprintParseListener jiraParseListener) {
   }
}
