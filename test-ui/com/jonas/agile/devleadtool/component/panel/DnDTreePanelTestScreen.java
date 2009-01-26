package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.component.listener.JiraParseListenerImpl;
import com.jonas.agile.devleadtool.component.tree.DnDTree;
import com.jonas.agile.devleadtool.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.component.tree.xml.DnDTreeBuilder;
import com.jonas.agile.devleadtool.component.tree.xml.JiraSaxHandler;
import com.jonas.agile.devleadtool.component.tree.xml.XmlParser;
import com.jonas.agile.devleadtool.component.tree.xml.XmlParserLargeMock;

public class DnDTreePanelTestScreen {
   public static void main(String... args) {
      try {
         JFrame frame = new JFrame();
         DnDTreeModel model = new DnDTreeModel("LLU");
         DnDTree tree = new DnDTree(model);
         JiraSaxHandler saxHandler = new JiraSaxHandler();
         saxHandler.addJiraParseListener(new JiraParseListenerImpl(tree, 100, frame));

         // XmlParser parser = new XmlParserImpl(saxHandler, 100);
         XmlParser parser = new XmlParserLargeMock(saxHandler);

         DnDTreeBuilder dndTreeBuilder = new DnDTreeBuilder(parser);

         DnDTreePanel main = new DnDTreePanel(tree, dndTreeBuilder, frame);

         frame.setSize(400, 700);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         frame.getContentPane().add(main, BorderLayout.CENTER);
         frame.getContentPane().add(MyStatusBar.getInstance(), BorderLayout.SOUTH);
         frame.setVisible(true);
      } catch (SAXException e) {
         e.printStackTrace();
      }
   }

}
