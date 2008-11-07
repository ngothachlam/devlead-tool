package com.jonas.agile.devleadtool.component.tree.xml;

import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlParserSmallMock implements XmlParser {

   private XMLReader reader;

   public XmlParserSmallMock(JiraSaxHandler saxHandler) throws SAXException {
      super();
      reader = XMLReaderFactory.createXMLReader();
      reader.setContentHandler(saxHandler);
   }

   public void parse(String sprint) throws IOException, SAXException {
      System.out.println("parsing!");
      reader.parse("test-data/ListOfSprintJiras.xml");
   }
}
