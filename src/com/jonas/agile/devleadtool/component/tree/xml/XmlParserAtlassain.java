package com.jonas.agile.devleadtool.component.tree.xml;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import com.jonas.agile.devleadtool.component.listener.SprintParseListener;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;

public class XmlParserAtlassain extends HttpClient implements XmlParser {

   private Logger log = MyLogger.getLogger(XmlParserAtlassain.class);

   private static final String MAX_RESULTS = "25";
   private XMLReader reader;

   public XmlParserAtlassain(JiraSaxHandler nodeCounter) throws SAXException {
      super();
      reader = XMLReaderFactory.createXMLReader();
      reader.setContentHandler(nodeCounter);
   }

   public void parse(String sprint, JiraProject project) throws IOException, SAXException {
      reader.parse("test-data/ListOfAtlassinJira.xml");
   }

   @Override
   public void addParseListener(SprintParseListener jiraParseListener) {
   }

}
