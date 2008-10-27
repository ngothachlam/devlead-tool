package com.jonas.testing.tree.fromScratch.xml;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import com.jonas.common.logging.MyLogger;

public class XmlParserAtlassain extends HttpClient implements XmlParser {

   private Logger log = MyLogger.getLogger(XmlParserAtlassain.class);

   private static final String MAX_RESULTS = "25";
   private XMLReader reader;

   public XmlParserAtlassain(JiraSaxHandler nodeCounter) throws SAXException {
      super();
      reader = XMLReaderFactory.createXMLReader();
      reader.setContentHandler(nodeCounter);
   }

   public void parse() throws IOException, SAXException {
      
      String baseUrl = "http://jira.atlassian.com";
      String url = baseUrl + "/secure/IssueNavigator.jspa?view=rss&&sorter/field=issuekey&sorter/order=DESC&tempMax=" + MAX_RESULTS + "&reset=true&decorator=none";
      log.debug("calling " + url);
      reader.parse(url);
   }

}
