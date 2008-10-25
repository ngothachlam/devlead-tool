package com.jonas.testing.tree.fromScratch.xml;

import junit.framework.TestCase;
import org.xml.sax.SAXException;

public class DnDTreeBuilderTest extends TestCase {

   public void testTreeBuilding() throws SAXException {
      SaxHandler saxHandler = new SaxHandler();
      XmlParser parser = new XmlParserImpl(saxHandler);
      DnDTreeBuilder builder = new DnDTreeBuilder(parser);
      
      saxHandler.addJiraParseListener(builder);
      
      builder.buildTree();
      
      assertTrue(false);
   }

}
