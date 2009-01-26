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

   public DnDTreeBuilder(XmlParser parser) {
      super();
      this.parser = parser;
   }

   public void buildTree(final String sprint, final JiraProject project) {
      SwingWorker worker = new SwingWorker() {
         @Override
         protected Object doInBackground() {
            try {
               parser.parse(project, sprint);
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
