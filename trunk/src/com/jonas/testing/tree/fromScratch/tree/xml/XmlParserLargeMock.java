package com.jonas.testing.tree.fromScratch.tree.xml;

import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlParserLargeMock implements XmlParser {

   private XMLReader reader;

   public XmlParserLargeMock(JiraSaxHandler nodeCounter) throws SAXException {
      super();
      reader = XMLReaderFactory.createXMLReader();
      reader.setContentHandler(nodeCounter);
   }

   public void parse() throws IOException, SAXException {
      reader.parse("test-data/ListOfSprintJirasMany.xml");
   }
}
