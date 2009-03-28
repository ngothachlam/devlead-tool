package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.component.tree.SprintTree;
import com.jonas.agile.devleadtool.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.component.tree.xml.DnDTreeBuilder;
import com.jonas.agile.devleadtool.component.tree.xml.JiraSaxHandler;
import com.jonas.agile.devleadtool.component.tree.xml.XmlParser;
import com.jonas.agile.devleadtool.component.tree.xml.XmlParserImpl;
import com.jonas.agile.devleadtool.gui.component.menu.SprintTreePopupMenu;
import com.jonas.agile.devleadtool.gui.component.panel.DnDTreePanel;
import com.jonas.agile.devleadtool.gui.listener.JiraParseListenerImpl;

public class DnDTreePanelTestScreen {
   public static void main(String... args) {
      try {
         JFrame frame = new JFrame();
         DnDTreeModel model = new DnDTreeModel("LLU");
         SprintTree tree = new SprintTree(model);
         JiraSaxHandler saxHandler = new JiraSaxHandler();
         saxHandler.addJiraParseListener(new JiraParseListenerImpl(100, frame));

          XmlParser parser = new XmlParserImpl(saxHandler, 100);
//         XmlParser parser = new XmlParserLargeMock(saxHandler);

         DnDTreeBuilder dndTreeBuilder = new DnDTreeBuilder(parser);
         new SprintTreePopupMenu(frame, tree, dndTreeBuilder);

         DnDTreePanel main = new DnDTreePanel(tree, frame);

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
