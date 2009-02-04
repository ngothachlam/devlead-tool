package com.jonas.agile.devleadtool.component.tree.xml;

import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import com.jonas.agile.devleadtool.component.listener.SprintParseListener;
import com.jonas.jira.JiraProject;

public class XmlParserSmallMock implements XmlParser {

   private XMLReader reader;

   public XmlParserSmallMock(JiraSaxHandler saxHandler) throws SAXException {
      super();
      reader = XMLReaderFactory.createXMLReader();
      reader.setContentHandler(saxHandler);
   }

   public void parse(JiraProject project, String sprint) throws IOException, SAXException {
      reader.parse("test-data/ListOfSprintJiras.xml");
   }

   @Override
   public void addParseListener(SprintParseListener jiraParseListener) {
   }
}
