package com.jonas.testing.tree.fromScratch;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import org.xml.sax.SAXException;
import com.jonas.testing.tree.fromScratch.xml.DnDTreeBuilder;
import com.jonas.testing.tree.fromScratch.xml.SaxHandler;
import com.jonas.testing.tree.fromScratch.xml.XmlParser;
import com.jonas.testing.tree.fromScratch.xml.XmlParserImpl;

public class DnDTreeMain extends JFrame {

   private DnDTreeMain() {
      super("Rearrangeable Tree");
      try {
         setSize(300, 200);
         setDefaultCloseOperation(EXIT_ON_CLOSE);

         SaxHandler saxHandler = new SaxHandler();
         XmlParser parser = new XmlParserImpl(saxHandler);
         DnDTreeBuilder dndTreeBuilder = new DnDTreeBuilder(parser);
         
         saxHandler.addJiraParseListener(dndTreeBuilder);
         
         JTree tree = dndTreeBuilder.buildTree();

         getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);
         setVisible(true);
      } catch (SAXException e) {
         e.printStackTrace();
      }
   }

   public static void main(String... args) {
      DnDTreeMain main = new DnDTreeMain();
   }
}
