package com.jonas.agile.devleadtool.component.tree.xml;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.component.listener.SprintParseListener;
import com.jonas.agile.devleadtool.component.menu.MyTreePopupMenu;
import com.jonas.agile.devleadtool.component.tree.DnDTree;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;
import com.jonas.jira.access.JiraException;

public class DnDTreeBuilder {

   private Logger log = MyLogger.getLogger(DnDTreeBuilder.class);

   private final XmlParser parser;

   public DnDTreeBuilder(XmlParser parser, DnDTree tree, JFrame parentFrame) {
      super();
      this.parser = parser;
      new MyTreePopupMenu(parentFrame, tree, this);
   }

   public void buildTree(DnDTree tree, final String sprint, final JiraProject project) {
      SwingWorker worker = new SwingWorker() {
         @Override
         protected Object doInBackground() {
            try {
               parser.parse(sprint, project);
            } catch (IOException e) {
               e.printStackTrace();
            } catch (SAXException e) {
               e.printStackTrace();
            } catch (JiraException e) {
               e.printStackTrace();
            }
            return null;
         }
      };
      worker.execute();
//      try {
//         worker.get();
//      } catch (InterruptedException e) {
//         e.printStackTrace();
//      } catch (ExecutionException e) {
//         e.printStackTrace();
//      }
   }

   public void addParseListener(SprintParseListener jiraParseListener) {
      parser.addParseListener(jiraParseListener);
   }

}
