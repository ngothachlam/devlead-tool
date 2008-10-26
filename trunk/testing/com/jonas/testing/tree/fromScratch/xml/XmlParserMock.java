package com.jonas.testing.tree.fromScratch.xml;

import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlParserMock implements XmlParser {

   private XMLReader reader;

   public XmlParserMock(JiraSaxHandler nodeCounter) throws SAXException {
      super();
      reader = XMLReaderFactory.createXMLReader();
      reader.setContentHandler(nodeCounter);
   }

   public void parse() throws IOException, SAXException {
      reader.parse("test-data/ListOfSprintJirasMany.xml");
   }
}
