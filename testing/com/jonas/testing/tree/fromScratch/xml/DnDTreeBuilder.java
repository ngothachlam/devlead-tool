package com.jonas.testing.tree.fromScratch.xml;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.tree.fromScratch.DnDTree;

public class DnDTreeBuilder {

   private Logger log = MyLogger.getLogger(DnDTreeBuilder.class);

   private final XmlParser parser;

   public DnDTreeBuilder(XmlParser parser) {
      super();
      this.parser = parser;
   }

   public void buildTree(DnDTree tree) {
      try {
         parser.parse();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (SAXException e) {
         e.printStackTrace();
      }
   }

}
